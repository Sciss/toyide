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

package com.pavelfatin.toyide.languages.toy.inspection

import com.pavelfatin.toyide.languages.toy.node.{PrefixExpression, _}
import com.pavelfatin.toyide.inspection.{Inspection, Mark}
import com.pavelfatin.toyide.node.{Expression, Node}

object PrefixApplication extends Inspection {
  val Message: (String, String) => String = "Operator '%s' cannot be applied to '%s'".format(_: String, _: String)

  def inspect(node: Node): Seq[Mark] = node match {
    case prefix @ PrefixExpression(Some(token), Some(Expression(expType))) if prefix.nodeType.isEmpty =>
      Mark(prefix, Message(token.span.text, expType.presentation)) :: Nil
    case _ => Nil
  }
}