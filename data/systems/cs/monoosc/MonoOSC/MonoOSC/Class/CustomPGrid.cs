using System;
using System.Collections.Generic;
using System.Text;
using System.ComponentModel;
using System.Windows.Forms;

namespace MonoOSC
{
/// <summary>
// Customer class to be displayed in the property grid
/// </summary>
///
[DefaultPropertyAttribute("WebLink")]
class CustomPGrid
{
    private string _HelpUrl;
    private string _Title;
    private string _Shortcut;
    private string _Author;
    private string _Description;
    private string _Code;
    private string _Format;
    private string _Language;
    // Name property with category attribute and
    // description attribute added
    [CategoryAttribute("Header"), DescriptionAttribute("Web link for Help")]
    public string HelpUrl
    {
        get
        {
            return _HelpUrl;
        }
        set
        {
            _HelpUrl = value;
        }
    }
    [CategoryAttribute("Header"),
     DescriptionAttribute("Author of the snippet")]
    public string Author
    {
        get
        {
            return _Author;
        }
        set
        {
            _Author = value;
        }
    }
    [CategoryAttribute("Header"),
     DescriptionAttribute("A short description of this code")]
    public string Description
    {
        get
        {
            return _Description;
        }
        set
        {
            _Description = value;
        }
    }
    [CategoryAttribute("Header"),
     DescriptionAttribute("Shortcut, when writted in VS, display this snippet")]
    public string Shortcut
    {
        get
        {
            return _Shortcut;
        }
        set
        {
            _Shortcut = value;
        }
    }
    [CategoryAttribute("Header"), DescriptionAttribute("Title to explain as best as possible wath this code do")]
    public string Title
    {
        get
        {
            return _Title;
        }
        set
        {
            _Title = value;
        }
    }
    [CategoryAttribute("Format"), DescriptionAttribute("The format/version of this code")]
    public string Format
    {
        get
        {
            return _Format;
        }
        set
        {
            _Format = value;
        }
    }
    [CategoryAttribute("Language and Code"), DescriptionAttribute("The code in CDATA")]
    public string Code
    {
        get
        {
            return _Code;
        }
        set
        {
            _Code = value;
        }
    }
    [CategoryAttribute("Language and Code"), DescriptionAttribute("The language of the code")]
    public string Language
    {
        get
        {
            return _Language;
        }
        set
        {
            _Language = value;
        }
    }
    public CustomPGrid()
    {
    }
}

}
