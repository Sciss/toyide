/*
 * Copyright 2018 Pavel Fatin, https://pavelfatin.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pavelfatin.toyide.editor

import java.awt.{Dimension, Insets, Point, Rectangle}

import com.pavelfatin.toyide.document.Location

import scala.math.{ceil, floor, max}

class Grid(val cellSize: Dimension, val insets: Insets) {
  def toPoint(location: Location): Point =
    new Point(insets.left + cellSize.width * location.indent,
      insets.top + cellSize.height * location.line)

  def toLocation(point: Point): Location = {
    val line    = floor((point.y - insets.top ).toDouble / cellSize.height.toDouble).toInt
    val indent  = floor((point.x - insets.left).toDouble / cellSize.width .toDouble).toInt
    Location(max(0, line), max(0, indent))
  }

  def toSize(lines: Int, maximumIndent: Int): Dimension = {
    val edge = toPoint(Location(lines, maximumIndent))
    new Dimension(edge.x + cellSize.width + insets.right, edge.y + insets.bottom)
  }

  def toArea(rectangle: Rectangle): Area = {
    val beginLine   = max(0, floor((rectangle.y - insets.top ).toDouble / cellSize.height.toDouble).toInt)
    val beginIndent = max(0, floor((rectangle.x - insets.left).toDouble / cellSize.width .toDouble).toInt)

    val endLine   = ceil((rectangle.y - insets.top  + rectangle.height).toDouble / cellSize.height.toDouble).toInt
    val endIndent = ceil((rectangle.x - insets.left + rectangle.width ).toDouble / cellSize.width .toDouble).toInt

    Area(beginLine, beginIndent, endIndent - beginIndent, endLine - beginLine)
  }

  def toRectangle(area: Area): Rectangle = {
    val point = toPoint(Location(area.line, area.indent))
    new Rectangle(point.x, point.y, cellSize.width * area.width, cellSize.height * area.height)
  }
}
