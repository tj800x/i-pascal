/*
 * Copyright 2000-2009 JetBrains s.r.o.
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
package com.siberika.idea.pascal.lang.lexer;

import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;

/**
 * @author max
 */
public interface PropsTokenTypes {
    IElementType WHITE_SPACE = TokenType.WHITE_SPACE;
    IElementType BAD_CHARACTER = TokenType.BAD_CHARACTER;

    IElementType END_OF_LINE_COMMENT = new PropertiesElementType("END_OF_LINE_COMMENT");
    IElementType KEY_CHARACTERS = new PropertiesElementType("KEY_CHARACTERS");
    IElementType VALUE_CHARACTERS = new PropertiesElementType("VALUE_CHARACTERS");
    IElementType KEY_VALUE_SEPARATOR = new PropertiesElementType("KEY_VALUE_SEPARATOR");

    TokenSet COMMENTS = TokenSet.create(END_OF_LINE_COMMENT);
    TokenSet WHITESPACES = TokenSet.create(WHITE_SPACE);
}
