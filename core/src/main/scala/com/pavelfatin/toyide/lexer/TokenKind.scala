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

package com.pavelfatin.toyide.lexer

/** @param name   the token name or identifier
  * @param data   if `true`, the token's source text should be used in
  *               `toString` representations, if `false`, the token name should be used
  */
case class TokenKind(name: String, data: Boolean = false)

object Tokens {
  val WS      = TokenKind("WS"      , data = true)
  val UNKNOWN = TokenKind("UNKNOWN" , data = true)
}
