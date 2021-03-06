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

package com.pavelfatin.toyide.languages.toy.parser

import org.junit.Test

class AssignmentParserTest extends ParserTest(AssignmentParser) {
  @Test
  def normal(): Unit = {
    assertParsed("a = 1;",
      """
      assignment
        referenceToValue
          a
        EQ
        literal
          1
        SEMI
      """)
  }

  @Test
  def noSemi(): Unit = {
    assertParsed("a = 1",
      """
      assignment
        referenceToValue
          a
        EQ
        literal
          1
        error: leaf
      """)
  }

  @Test
  def noExpression(): Unit = {
    assertParsed("a =",
      """
      assignment
        referenceToValue
          a
        EQ
        error: leaf
      """)
  }
//
//  @Test
//  def noAssignment = assertParsed("a",
//"""
//assignment
// a
// error: leaf
//""")
//
//  @Test
//  def empty = assertParsed("",
//"""
//assignment
// error: leaf
//""")
}