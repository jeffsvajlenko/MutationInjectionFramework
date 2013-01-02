//
// MethodNeverReturnsIssueTests.cs
//
// Author:
//      Mansheng Yang <lightyang0@gmail.com>
//
// Copyright (c) 2012 Mansheng Yang <lightyang0@gmail.com>
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

using ICSharpCode.NRefactory.CSharp.Refactoring;
using NUnit.Framework;

namespace ICSharpCode.NRefactory.CSharp.CodeIssues
{
[TestFixture]
public class MethodNeverReturnsIssueTests : InspectionActionTestBase
{
    [Test]
    public void TestEnd ()
    {
        var input = @"
class TestClass
{
	void TestMethod () 
	{
		int i = 1;
	}
}";
        Test<MethodNeverReturnsIssue> (input, 0);
    }

    [Test]
    public void TestReturn ()
    {
        var input = @"
class TestClass
{
	void TestMethod ()
	{
		return;
	}
}";
        Test<MethodNeverReturnsIssue> (input, 0);
    }

    [Test]
    public void TestThrow ()
    {
        var input = @"
class TestClass
{
	void TestMethod ()
	{
		throw new System.NotImplementedException();	
	}
}";
        Test<MethodNeverReturnsIssue> (input, 0);
    }

    [Test]
    public void TesNeverReturns ()
    {
        var input = @"
class TestClass
{
	void TestMethod ()
	{
		while (true) ;
	}
}";
        Test<MethodNeverReturnsIssue> (input, 1);
    }

    [Test]
    public void YieldBreak ()
    {
        var input = @"
class TestClass
{
	System.Collections.Generic.IEnumerable<string> TestMethod ()
	{
		yield break;
	}
}";
        Test<MethodNeverReturnsIssue> (input, 0);
    }
}
}
