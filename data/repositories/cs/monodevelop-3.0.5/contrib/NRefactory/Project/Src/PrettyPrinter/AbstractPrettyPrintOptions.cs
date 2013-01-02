// <file>
//     <copyright see="prj:///doc/copyright.txt"/>
//     <license see="prj:///doc/license.txt"/>
//     <owner name="Mike Krüger" email="mike@icsharpcode.net"/>
//     <version>$Revision: 4482 $</version>
// </file>

namespace ICSharpCode.OldNRefactory.PrettyPrinter
{
/// <summary>
/// Description of PrettyPrintOptions.
/// </summary>
public class AbstractPrettyPrintOptions
{
    char indentationChar = '\t';
    int  tabSize         = 4;
    int  indentSize      = 4;
    string eolMarker     = System.Environment.NewLine;

    public char IndentationChar
    {
        get
        {
            return indentationChar;
        }
        set
        {
            indentationChar = value;
        }
    }

    public int TabSize
    {
        get
        {
            return tabSize;
        }
        set
        {
            tabSize = value;
        }
    }

    public int IndentSize
    {
        get
        {
            return indentSize;
        }
        set
        {
            indentSize = value;
        }
    }

    public string EolMarker
    {
        get
        {
            return eolMarker;
        }
        set
        {
            eolMarker = value;
        }
    }
}
}
