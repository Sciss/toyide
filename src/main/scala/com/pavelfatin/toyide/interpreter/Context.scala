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

package com.pavelfatin.toyide.interpreter

trait Context {
  def get(local: Boolean, name: String): Value[_]

  def put(local: Boolean, name: String, value: Value[_])

  def update(local: Boolean, name: String, value: Value[_])

  def inScope(action: => Unit)

  def inFrame(place: Place)(action: => Unit): Option[Value[_]]

  def dropFrame(value: Option[Value[_]])

  def trace: Seq[Place]
}