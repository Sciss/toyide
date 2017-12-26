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

package com.pavelfatin.toyide

import java.awt.Dimension

import com.pavelfatin.toyide.ide.MainFrame
import com.pavelfatin.toyide.languages.lisp.LispLanguage
import com.pavelfatin.toyide.languages.toy.ToyLanguage

import scala.swing._

object Application extends SwingApplication {
  private val Languages = Seq(ToyLanguage, LispLanguage)

  override def startup(args: Array[String]): Unit = {
    selectLanguage().foreach(openMainFrame)
  }

  private def selectLanguage(): Option[Language] = {
    val dialog = new LanguageDialog(Languages)
    open(dialog)
    dialog.selection
  }

  private def openMainFrame(language: Language): Unit = {
    val code = language.examples.headOption.fold("")(_.code)
    val frame = new MainFrame(language, code)
    frame.preferredSize = new Dimension(874, 696)
    open(frame)
  }

  private def open(window: Window): Unit = {
    window.pack()
    window.centerOnScreen()
    window.open()
  }
}