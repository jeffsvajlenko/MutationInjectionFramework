//
// BacktrackingStringMatcherTests.cs
//
// Author:
//       Mike Krüger <mkrueger@xamarin.com>
//
// Copyright (c) 2012 Xamarin Inc. (http://xamarin.com)
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
using System;
using NUnit.Framework;
using MonoDevelop.Core.Text;
using System.Linq;

namespace MonoDevelop.Core
{
public class BacktrackingStringMatcherTests
{
    [Test()]
    public void TestStringMatcher ()
    {
        var matcher = StringMatcher.GetMatcher ("typese", true);
        Assert.IsTrue (matcher.IsMatch ("TypeSystemService"));
    }

    [Test()]
    public void TestGetMatch ()
    {
        var matcher = StringMatcher.GetMatcher ("typese", true);
        var match = matcher.GetMatch("TypeSystemService");
        CompareMatch(match, "****------**-----");
    }

    [Test()]
    public void TestGetMatchWithUpperCaseWord ()
    {
        var matcher = StringMatcher.GetMatcher ("myhtmser", true);
        var match = matcher.GetMatch("MyFunnyHTMLService");
        CompareMatch(match,          "**-----***-***----");
    }

    [Test()]
    public void TestGetMatchWithUpperCaseWordCase2 ()
    {
        var matcher = StringMatcher.GetMatcher ("myhmser", true);
        var match = matcher.GetMatch("MyFunnyHTMLMasterService");
        CompareMatch(match,          "**-----*---*-----***----");
    }

    [Test()]
    public void TestGetMatchWithunderscoreWord ()
    {
        var matcher = StringMatcher.GetMatcher ("myhtmser", true);
        var match = matcher.GetMatch("my_html_Service");
        CompareMatch(match,          "**-***--***----");
    }

    [Test()]
    public void TestDigit ()
    {
        var matcher = StringMatcher.GetMatcher ("my12", true);
        var match = matcher.GetMatch("my_html_Service_123");
        CompareMatch(match,          "**--------------**-");
    }

    [Test()]
    public void TestPunctuation ()
    {
        var matcher = StringMatcher.GetMatcher ("foo:b", true);
        var match = matcher.GetMatch("foo:bar");
        CompareMatch(match,          "*****--");
    }

    [Test()]
    public void TestUnderscoreAtEnd ()
    {
        var matcher = StringMatcher.GetMatcher ("FB", true);
        var match = matcher.GetMatch("foo_");
        Assert.AreEqual (null, match);
    }

    static string GenerateString(int[] match, string str)
    {
        var result = new char[str.Length];
        for (int i = 0; i < result.Length; i++)
        {
            result[i] = match.Contains (i) ? '*' : '-';
        }
        return new string (result);
    }
    static void CompareMatch (int[] match, string str)
    {
        for (int i = 0; i < str.Length; i++)
        {
            if (str[i] == '*' && !match.Any(m => m == i))
            {
                Console.WriteLine (str);
                Console.WriteLine (GenerateString (match, str));
                Assert.Fail ("Match "+ i +" not found match.");
            }
            if (str[i] == '-' && match.Any(m => m == i))
            {
                Console.WriteLine (str);
                Console.WriteLine (GenerateString (match, str));
                Assert.Fail ("Match "+ i +" wrongly found.");
            }
        }

        foreach (var i in match)
        {
            if (str[i] != '*')
            {
                Console.WriteLine (str);
                Console.WriteLine (GenerateString (match, str));
                Assert.Fail ("Match "+ i +" doesn't match.");
            }
        }
    }
}
}

