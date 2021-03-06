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

package com.pavelfatin.toyide.editor

import com.pavelfatin.toyide.Language
import com.pavelfatin.toyide.document.{Document, DocumentImpl}

object EditorFactory {
  def createEditorFor(language: Language, history: History, coloring: Coloring): Editor = {
    val document  = new DocumentImpl()
    val data      = new DataImpl(document, language.lexer, language.parser, language.inspections)
    val holder    = new ErrorHolderImpl(document, data)

    createEditorFor(document, data, holder, language, history, coloring)
  }

  def createEditorFor(document: Document, data: Data, holder: ErrorHolder, language: Language,
                      history: History, coloring: Coloring): Editor = {

    val listRenderer  = new VariantCellRenderer(language.lexer, coloring)
    val matcher       = new BraceMatcherImpl(language.complements)

    new EditorImpl(document, data, holder, language.lexer, coloring, matcher, language.format,
      language.adviser, listRenderer, language.comment, history)
  }
}