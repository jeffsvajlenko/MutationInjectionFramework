//
// TokenType.cs
//
// Author:
//   Marek Sieradzki (marek.sieradzki@gmail.com)
//   Jaroslaw Kowalski <jaak@jkowalski.net>
//
// (C) 2006 Marek Sieradzki
// (C) 2004-2006 Jaroslaw Kowalski
//
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


using System;

namespace Microsoft.Build.BuildEngine
{

internal class Token
{

    string		tokenValue;
    TokenType	tokenType;

    public Token (string tokenValue, TokenType tokenType)
    {
        this.tokenValue = tokenValue;
        this.tokenType = tokenType;
    }

    public string Value
    {
        get
        {
            return tokenValue;
        }
    }

    public TokenType Type
    {
        get
        {
            return tokenType;
        }
    }

    public override string ToString ()
    {
        return String.Format ("Token (Type: {0} -> Value: {1})", tokenType, tokenValue);
    }
}

internal enum TokenType
{
    EOF,
    BOF,
    Number,
    String,
    //Keyword,
    Punct,
    WhiteSpace,
    Item,
    Property,
    Metadata,
    FunctionName,
    Transform,
//		LiteralSubExpression,

    FirstPunct,

    Less,
    Greater,
    LessOrEqual,
    GreaterOrEqual,
    Equal,
    NotEqual,
    LeftParen,
    RightParen,
    Dot,
    Comma,
    Not,
    And,
    Or,
    Apostrophe,

    LastPunct,
    Invalid,
}
}

