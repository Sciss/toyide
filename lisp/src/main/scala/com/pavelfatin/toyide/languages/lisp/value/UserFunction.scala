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

package com.pavelfatin.toyide.languages.lisp.value

import com.pavelfatin.toyide.Output
import com.pavelfatin.toyide.languages.lisp.parameters.Parameters

class UserFunction(val name: Option[String], parameters: Parameters, expressions: Seq[Expression], closure: Map[String, Expression])
  extends FunctionValue with TailCalls {

  def isLazy = false

  def apply(arguments: Seq[Expression], environment: Environment, output: Output): Expression = {
    val bindings = parameters.bind(ListValue(arguments)).fold(error(_, environment), identity)

    val initialEnvironment = environment.clearLocals.addLocals(closure ++ bindings)

    withTailCalls(parameters, initialEnvironment) { env =>
      expressions.map(_.eval(env, output)).lastOption.getOrElse(ListValue.Empty)
    }
  }

  def presentation: String = {
    val prefix = name.map(_ + "_").getOrElse("")
    prefix + "fn" + parameters.presentation
  }
}
