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

package com.pavelfatin.toyide.languages.toy.parser

import org.junit.Test

class TypeSpecParserTest extends ParserTest(TypeSpecParser) {
  @Test
  def typeInteger(): Unit = {
    assertParsed(": integer",
      """
      typeSpec
        COLON
        INTEGER
      """)
  }

  @Test
  def typeString(): Unit = {
    assertParsed(": string",
      """
      typeSpec
        COLON
        STRING
      """)
  }

  @Test
  def typeBoolean(): Unit = {
    assertParsed(": boolean",
      """
      typeSpec
        COLON
        BOOLEAN
      """)
  }

  @Test
  def typeVoid(): Unit = {
    assertParsed(": void",
      """
      typeSpec
        COLON
        VOID
      """)
  }
}