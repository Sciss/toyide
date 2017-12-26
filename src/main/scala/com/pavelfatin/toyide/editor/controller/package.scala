/*
 * Copyright (C) 2011 Pavel Fatin <http://pavelfatin.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pavelfatin.toyide.editor

import com.pavelfatin.toyide.node.{IdentifiedNode, Node, ReferenceNode, ReferenceNodeTarget}
import com.pavelfatin.toyide.document.Document
import com.pavelfatin.toyide.Interval

package object controller {
  private[controller] implicit class DataExt(val data: Data) extends AnyVal {
    def leafAt(offset: Int): Option[Node] = data.structure.flatMap { root =>
      root.offsetOf(offset).flatMap(root.leafAt)
    }

    def referenceAt(offset: Int): Option[ReferenceNode] = data.structure.flatMap { root =>
      root.offsetOf(offset).flatMap(root.referenceAt)
    }

    def identifierAt(offset: Int): Option[IdentifiedNode] = data.structure.flatMap { root =>
      root.offsetOf(offset).flatMap(root.identifierAt)
    }

    def connectedLeafsFor(offset: Int): Seq[Node] = {
      val targetNode = referenceAt(offset) collect {
        case ReferenceNodeTarget(node: IdentifiedNode) => node
      } orElse {
        identifierAt(offset)
      }
      val refs = data.structure.toList.flatMap { root =>
        root.elements.collect {
          case ref @ ReferenceNodeTarget(target) if targetNode.contains(target) => ref
        }
      }
      targetNode.flatMap(_.id).toList ::: refs.flatMap(_.source)
    }
  }

  private[controller] implicit class NodeExt(val node: Node) extends AnyVal {
    def offsetOf(i: Int): Option[Int] = {
      if (node.span.touches(i)) Some(i - node.span.begin) else None
    }
  }

  private[controller] implicit class TerminalExt(val terminal: Terminal) extends AnyVal {
    def currentLineIntervalIn(document: Document): Interval = {
      val line = document.lineNumberOf(terminal.offset)
      val begin = document.startOffsetOf(line)
      val postfix = 1.min(document.linesCount - line - 1)
      val end = document.endOffsetOf(line) + postfix
      Interval(begin, end)
    }

    def insertInto(document: Document, s: String): Unit = {
      if(terminal.selection.isDefined) {
        val sel = terminal.selection.get
        terminal.selection = None
        val shift = sel.begin + s.length - terminal.offset
        if (shift < 0) terminal.offset += shift
        document.replace(sel, s)
        if (shift > 0) terminal.offset += shift
      } else {
        document.insert(terminal.offset, s)
        terminal.offset += s.length
      }
    }
  }
}