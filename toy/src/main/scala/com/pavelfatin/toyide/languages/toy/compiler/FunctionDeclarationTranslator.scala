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

package com.pavelfatin.toyide.languages.toy.compiler

import com.pavelfatin.toyide.compiler.{Code, Labels}
import com.pavelfatin.toyide.languages.toy.node.FunctionDeclaration

trait FunctionDeclarationTranslator extends ToyTranslatable { self: FunctionDeclaration =>
  private val Template =
    """
      |.method private %s(%s)%s
      |   .limit stack 10
      |   .limit locals 10
      |
      |   %s
      |
      |   return
      |.end method
      |""".stripMargin

  override def translate(name: String, labels: Labels): Code = {
    val b = block.getOrElse(
      interrupt("Function block not found: %s", span.text))

    val returnType = nodeType.getOrElse(
      interrupt("Unknown function return type: %s", span.text))

    val parameterTypes = parameters.map { it =>
      it.nodeType.getOrElse(
        interrupt("Unknown parameter type: %s", it.span.text))
    }

    val s = Template.format(identifier,
      parameterTypes.map(_.descriptor).mkString(""),
      returnType.descriptor,
      b.translate(name, new Labels()).instructions)

    Code(methods = s)
  }
}