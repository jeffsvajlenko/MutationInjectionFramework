//
// System.Web.Compilation.Directive
//
// Authors:
//	Gonzalo Paniagua Javier (gonzalo@ximian.com)
//
// (C) 2003 Ximian, Inc (http://www.ximian.com)
//

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
//

#define NET_2_0

using System;
using System.Collections;
using System.Globalization;

namespace MonoDevelop.AspNet.Parser.Internal
{
sealed class Directive
{
    static Hashtable directivesHash;
    static string [] page_atts = {  "AspCompat", "AutoEventWireup", "Buffer",
                                    "ClassName", "ClientTarget", "CodePage",
                                    "CompilerOptions", "ContentType", "Culture", "Debug",
                                    "Description",
#if NET_2_0
                                    "EnableEventValidation", "MaintainScrollPositionOnPostBack",
#endif
                                    "EnableSessionState", "EnableViewState",
                                    "EnableViewStateMac", "ErrorPage", "Explicit",
                                    "Inherits", "Language", "LCID", "ResponseEncoding",
                                    "Src", "SmartNavigation", "Strict", "Trace",
                                    "TraceMode", "Transaction", "UICulture",
                                    "WarningLevel", "CodeBehind" , "ValidateRequest"
                                 };

    static string [] control_atts = { "AutoEventWireup", "ClassName", "CompilerOptions",
                                      "Debug", "Description", "EnableViewState",
                                      "Explicit", "Inherits", "Language", "Strict", "Src",
                                      "WarningLevel", "CodeBehind", "TargetSchema", "LinePragmas"
                                    };

    static string [] import_atts = { "namespace" };
    static string [] implements_atts = { "interface" };
    static string [] assembly_atts = { "name", "src" };
    static string [] register_atts = { "tagprefix", "tagname", "Namespace", "Src", "Assembly" };

    static string [] outputcache_atts = { "Duration", "Location", "VaryByControl",
                                          "VaryByCustom", "VaryByHeader", "VaryByParam"
                                        };

    static string [] reference_atts = { "page", "control" };

    static string [] webservice_atts = { "class", "codebehind", "debug", "language" };

    static string [] application_atts = { "description", "inherits", "codebehind" };

#if NET_2_0
    static string [] mastertype_atts = { "virtualpath", "typename" };
    static string [] previouspagetype_atts = { "virtualpath", "typename" };
#endif

    static Directive ()
    {
        InitHash ();
    }

    private static void InitHash ()
    {
#if NET_2_0
        StringComparer comparer = StringComparer.InvariantCultureIgnoreCase;
        directivesHash = new Hashtable (comparer);
#else
        CaseInsensitiveHashCodeProvider provider = new CaseInsensitiveHashCodeProvider (CultureInfo.InvariantCulture);
        CaseInsensitiveComparer comparer =  new CaseInsensitiveComparer (CultureInfo.InvariantCulture);

        directivesHash = new Hashtable (provider, comparer);
#endif

        // Use Hashtable 'cause is O(1) in Contains (ArrayList is O(n))
#if NET_2_0
        Hashtable valid_attributes = new Hashtable (comparer);
#else
        Hashtable valid_attributes = new Hashtable (provider, comparer);
#endif
        foreach (string att in page_atts) valid_attributes.Add (att, null);
        directivesHash.Add ("PAGE", valid_attributes);

#if NET_2_0
        valid_attributes = new Hashtable (comparer);
#else
        valid_attributes = new Hashtable (provider, comparer);
#endif
        foreach (string att in control_atts) valid_attributes.Add (att, null);
        directivesHash.Add ("CONTROL", valid_attributes);

#if NET_2_0
        valid_attributes = new Hashtable (comparer);
#else
        valid_attributes = new Hashtable (provider, comparer);
#endif
        foreach (string att in import_atts) valid_attributes.Add (att, null);
        directivesHash.Add ("IMPORT", valid_attributes);

#if NET_2_0
        valid_attributes = new Hashtable (comparer);
#else
        valid_attributes = new Hashtable (provider, comparer);
#endif
        foreach (string att in implements_atts) valid_attributes.Add (att, null);
        directivesHash.Add ("IMPLEMENTS", valid_attributes);

#if NET_2_0
        valid_attributes = new Hashtable (comparer);
#else
        valid_attributes = new Hashtable (provider, comparer);
#endif
        foreach (string att in register_atts) valid_attributes.Add (att, null);
        directivesHash.Add ("REGISTER", valid_attributes);

#if NET_2_0
        valid_attributes = new Hashtable (comparer);
#else
        valid_attributes = new Hashtable (provider, comparer);
#endif
        foreach (string att in assembly_atts) valid_attributes.Add (att, null);
        directivesHash.Add ("ASSEMBLY", valid_attributes);

#if NET_2_0
        valid_attributes = new Hashtable (comparer);
#else
        valid_attributes = new Hashtable (provider, comparer);
#endif
        foreach (string att in outputcache_atts) valid_attributes.Add (att, null);
        directivesHash.Add ("OUTPUTCACHE", valid_attributes);

#if NET_2_0
        valid_attributes = new Hashtable (comparer);
#else
        valid_attributes = new Hashtable (provider, comparer);
#endif
        foreach (string att in reference_atts) valid_attributes.Add (att, null);
        directivesHash.Add ("REFERENCE", valid_attributes);

#if NET_2_0
        valid_attributes = new Hashtable (comparer);
#else
        valid_attributes = new Hashtable (provider, comparer);
#endif
        foreach (string att in webservice_atts) valid_attributes.Add (att, null);
        directivesHash.Add ("WEBSERVICE", valid_attributes);

#if NET_2_0
        valid_attributes = new Hashtable (comparer);
#else
        valid_attributes = new Hashtable (provider, comparer);
#endif
        // same attributes as webservice
        foreach (string att in webservice_atts) valid_attributes.Add (att, null);
        directivesHash.Add ("WEBHANDLER", valid_attributes);

#if NET_2_0
        valid_attributes = new Hashtable (comparer);
#else
        valid_attributes = new Hashtable (provider, comparer);
#endif
        foreach (string att in application_atts) valid_attributes.Add (att, null);
        directivesHash.Add ("APPLICATION", valid_attributes);

#if NET_2_0
        valid_attributes = new Hashtable (comparer);
        foreach (string att in mastertype_atts) valid_attributes.Add (att, null);
        directivesHash.Add ("MASTERTYPE", valid_attributes);

        valid_attributes = new Hashtable (comparer);
        foreach (string att in control_atts) valid_attributes.Add (att, null);
        directivesHash.Add ("MASTER", valid_attributes);

        valid_attributes = new Hashtable (comparer);
        foreach (string att in previouspagetype_atts) valid_attributes.Add (att, null);
        directivesHash.Add ("PREVIOUSPAGETYPE", valid_attributes);
#endif
    }

    private Directive () { }

    public static bool IsDirective (string id)
    {
        return directivesHash.Contains (id);
    }
}

}

