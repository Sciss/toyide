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

import com.pavelfatin.toyide.{ObservableEvents, Interval}

trait AbstractTerminal extends Terminal with ObservableEvents[TerminalEvent] {
  private var _offset = 0

  private var _selection: Option[Interval] = None

  private var _hover: Option[Int] = None

  private var _highlights: Seq[Interval] = Seq.empty

  def offset: Int = _offset

  def offset_=(i: Int): Unit = {
    if (_offset != i) {
      val previous = _offset
    _offset = i
      notifyObservers(CaretMovement(previous, i))
    }
  }

  def selection: Option[Interval] = _selection

  def selection_=(s: Option[Interval]): Unit = {
    if (_selection != s) {
      val previous = _selection
      _selection = s
      notifyObservers(SelectionChange(previous, s))
    }
  }

  def hover: Option[Int] = _hover

  def hover_=(i: Option[Int]): Unit = {
    if (_hover != i) {
      val previous = _hover
      _hover = i
      notifyObservers(HoverChange(previous, i))
    }
  }

  def highlights: Seq[Interval] = _highlights

  def highlights_=(hs: Seq[Interval]): Unit = {
    if (_highlights != hs) {
      val previous = _highlights
      _highlights = hs
      notifyObservers(HighlightsChange(previous, hs))
    }
  }
}