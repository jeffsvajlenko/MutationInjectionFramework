// <file>
//     <copyright see="prj:///doc/copyright.txt"/>
//     <license see="prj:///doc/license.txt"/>
//     <owner name="Mike Krüger" email="mike@icsharpcode.net"/>
//     <version>$Revision: 4482 $</version>
// </file>

using System;
using System.Collections.Generic;
using ICSharpCode.OldNRefactory.Ast;

namespace ICSharpCode.OldNRefactory
{
/// <summary>
/// Parser interface.
/// </summary>
public interface IParser : IDisposable
{
    Parser.Errors Errors
    {
        get;
    }

    Parser.ILexer Lexer
    {
        get;
    }

    CompilationUnit CompilationUnit
    {
        get;
    }

    bool ParseMethodBodies
    {
        get;
        set;
    }

    void Parse();

    Expression ParseExpression();
    TypeReference ParseTypeReference ();
    BlockStatement ParseBlock();
    List<INode> ParseTypeMembers();
}
}
