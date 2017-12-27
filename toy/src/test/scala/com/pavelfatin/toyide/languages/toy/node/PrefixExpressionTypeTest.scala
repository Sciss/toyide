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

package com.pavelfatin.toyide.languages.toy.node

import com.pavelfatin.toyide.languages.toy.ToyType._
import org.junit.Test

class PrefixExpressionTypeTest extends ExpressionTypeTestBase {
  @Test
  def positive(): Unit = {
    assertTypeIs("+1", IntegerType)
  }

  @Test
  def negative(): Unit = {
    assertTypeIs("-1", IntegerType)
  }

  @Test
  def recursive(): Unit = {
    assertTypeIs("+++1", IntegerType)
  }

  @Test
  def wrong(): Unit = {
    assertNoType("-true")
    assertNoType("-\"foo\"")
  }

  @Test
  def not(): Unit = {
    assertTypeIs("!false", BooleanType)
  }

  @Test
  def notWrong(): Unit = {
    assertNoType("!1")
  }
}