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

package com.pavelfatin.toyide.languages.toy.node

import com.pavelfatin.toyide.Extensions._
import com.pavelfatin.toyide.languages.toy.ToyTokens._
import com.pavelfatin.toyide.languages.toy.ToyType
import com.pavelfatin.toyide.languages.toy.ToyType._
import com.pavelfatin.toyide.languages.toy.compiler.{BinaryExpressionTranslator, CallExpTranslator, GroupTranslator, LiteralTranslator, PrefixExpressionTranslator}
import com.pavelfatin.toyide.languages.toy.interpreter.{BinaryExpressionEvaluator, CallExpEvaluator, GroupEvaluator, LiteralEvaluator, PrefixExpressionEvaluator, TypeCheck}
import com.pavelfatin.toyide.languages.toy.optimizer.ToyExpressionOptimizer
import com.pavelfatin.toyide.lexer.{Token, TokenKind}
import com.pavelfatin.toyide.node._

trait ToyExpression extends Expression with ToyExpressionOptimizer

class Literal extends NodeImpl("literal")
with ToyExpression with LiteralEvaluator with TypeCheck with LiteralTranslator {
  protected def tokenKind: Option[TokenKind] = children.headOption.flatMap(_.token).map(_.kind)

  override def constant = true

  lazy val nodeType: Option[ToyType with Product with Serializable] = tokenKind.collect {
    case STRING_LITERAL  => StringType
    case NUMBER_LITERAL  => IntegerType
    case BOOLEAN_LITERAL => BooleanType
  }

  override def toString: String = "%s(%s)".format(kind, span.text)
}

class PrefixExpression extends NodeImpl("prefixExpression")
with ToyExpression with PrefixExpressionEvaluator with TypeCheck with PrefixExpressionTranslator {
  def prefix: Option[Token] = children.headOption.flatMap(_.token)

  def expression: Option[Expression] = children.findBy[Expression]

  override def constant: Boolean = expression.exists(_.constant)

  lazy val nodeType: Option[ToyType with Product with Serializable] = {
    prefix.map(_.kind).zip(expression.flatMap(_.nodeType)).headOption collect {
      case (BANG, BooleanType) => BooleanType
      case (PLUS | MINUS, IntegerType) => IntegerType
    }
  }
}

object PrefixExpression {
  def unapply(node: PrefixExpression) = Some(node.prefix, node.expression)
}

class BinaryExpression extends NodeImpl("binaryExpression")
with ToyExpression with BinaryExpressionEvaluator with TypeCheck with BinaryExpressionTranslator {
  def parts: Option[(Expression, Token, Expression)] = children match {
    case (left: Expression) :: NodeToken(_token) :: (right: Expression) :: Nil => Some(left, _token, right)
    case _ => None
  }

  override def constant: Boolean = children match {
    case (l: Expression) :: _ ::  (r: Expression) :: Nil if l.constant && r.constant => true
    case (l @ Expression(BooleanType)) :: NodeToken(Token(_kind, _, _)) ::  (Expression(BooleanType)) :: Nil =>
      _kind match {
        case AMP_AMP => l.optimized.contains("false")
        case BAR_BAR => l.optimized.contains("true")
        case _ => false
      }
    case _ => false
  }

  private def signature = parts collect {
    case (Expression(leftType), _token, Expression(rightType)) => (leftType, _token.kind, rightType)
  }

  lazy val nodeType: Option[ToyType with Product with Serializable] = signature.collect {
    case (BooleanType, AMP_AMP, BooleanType) => BooleanType

    case (BooleanType, BAR_BAR, BooleanType) => BooleanType

    case (IntegerType, GT, IntegerType) => BooleanType
    case (IntegerType, GT_EQ, IntegerType) => BooleanType
    case (IntegerType, LT, IntegerType) => BooleanType
    case (IntegerType, LT_EQ, IntegerType) => BooleanType

    case (StringType, EQ_EQ, StringType) => BooleanType
    case (IntegerType, EQ_EQ, IntegerType) => BooleanType
    case (BooleanType, EQ_EQ, BooleanType) => BooleanType

    case (StringType, BANG_EQ, StringType) => BooleanType
    case (IntegerType, BANG_EQ, IntegerType) => BooleanType
    case (BooleanType, BANG_EQ, BooleanType) => BooleanType

    case (IntegerType, STAR, IntegerType) => IntegerType
    case (IntegerType, SLASH, IntegerType) => IntegerType
    case (IntegerType, PERCENT, IntegerType) => IntegerType

    case (IntegerType, PLUS, IntegerType) => IntegerType
    case (IntegerType, MINUS, IntegerType) => IntegerType
    case (StringType, PLUS, _) => StringType
  }
}

object BinaryExpression {
  def unapply(exp: BinaryExpression): Option[(Expression, Token, Expression)] = exp.parts
}

class CallExpression extends NodeImpl("callExpression")
with ToyExpression with CallExpEvaluator with TypeCheck with CallExpTranslator {
  def reference: Option[ReferenceToFunction] = children.findBy[ReferenceToFunction]

  def function: Option[FunctionDeclaration] =
    reference.flatMap(_.target).map(_.asInstanceOf[FunctionDeclaration])

  def arguments: Option[Arguments] = children.findBy[Arguments]

  def expressions: Seq[Expression] = arguments.map(_.expressions).getOrElse(Nil)

  def bindings: (Seq[(Expression, Parameter)], Seq[Expression], Seq[Parameter]) = {
    val parameters = function.map(_.parameters).getOrElse(Nil)
    val es = expressions.iterator
    val ps = parameters.iterator
    (es.zip(ps).toList, es.toSeq, ps.toSeq)
  }

  def rightBrace: Option[Node] = arguments.flatMap(_.children.lastOption)

  lazy val nodeType: Option[ToyType with Product with Serializable] = reference.flatMap { it =>
    if (it.predefined) Some(VoidType) else function.flatMap(_.nodeType)
  }
}

class Group extends NodeImpl("group")
with ToyExpression with GroupEvaluator with TypeCheck with GroupTranslator {
  def child: Option[Expression] = children.findBy[Expression]

  override def constant: Boolean = child.exists(_.constant)

  lazy val nodeType: Option[NodeType] = child.flatMap(_.nodeType)
}