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

package com.pavelfatin.toyide.editor.controller

import com.pavelfatin.toyide.document.Document
import com.pavelfatin.toyide.editor.{AnAction, Terminal}

private class MoveLineDown(document: Document, terminal: Terminal) extends AnAction with Repeater {
  repeat(document, terminal)

  def keys: Seq[String] = List("shift ctrl pressed DOWN")

  override def enabled: Boolean = document.lineNumberOf(terminal.offset) < document.linesCount - 1

  def apply(): Unit = {
    val location = document.toLocation(terminal.offset)

    val source = document.intervalOf(location.line)
    val target = document.intervalOf(location.line + 1)

    val targetText = document.text(target)

    terminal.selection = None

    document.replace(target, document.text(source))
    document.replace(source, targetText)

    document.toOffset(location.copy(line = location.line + 1)).foreach(terminal.offset = _)
  }
}