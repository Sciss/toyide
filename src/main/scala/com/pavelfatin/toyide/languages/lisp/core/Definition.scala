/*
 * Copyright (C) 2014 Pavel Fatin <http://pavelfatin.com>
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

package com.pavelfatin.toyide.languages.lisp.core

import com.pavelfatin.toyide.Output
import com.pavelfatin.toyide.languages.lisp.value._

object Def extends CoreFunction("def", isLazy = true) {
  def apply(arguments: Seq[Expression], environment: Environment, output: Output): ListValue = arguments match {
    case Seq(SymbolValue(name), expression) =>
      environment.setGlobal(name, expression.eval(environment, output))
      ListValue.Empty
    case _ => expected("symbol expression", arguments, environment)
  }
}

object Let extends CoreFunction("let", isLazy = true) with Bindings {
  def apply(arguments: Seq[Expression], environment: Environment, output: Output): Expression = arguments match {
    case Seq(ListValue(elements), expressions @ _*) =>
      val env = bind(elements, environment, output)
      expressions.map(_.eval(env, output)).lastOption.getOrElse(ListValue.Empty)
    case _ => expected("[bindings*] exprs*", arguments, environment)
  }
}
