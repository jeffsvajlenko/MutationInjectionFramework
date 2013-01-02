//
// ParameterNotUsedIssueTests.cs
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
public class ParameterNotUsedIssueTests : InspectionActionTestBase
{
    [Test]
    public void TestUnusedParameter ()
    {
        var input = @"
class TestClass {
	void TestMethod (int i)
	{
	}
}";
        Test<ParameterNotUsedIssue> (input, 1);
    }

    [Test]
    public void TestInterfaceImplementation ()
    {
        var input = @"
interface ITestClass {
	void TestMethod (int i);
}
class TestClass : ITestClass {
	public void TestMethod (int i)
	{
	}
}";
        Test<ParameterNotUsedIssue> (input, 0);
    }

    [Test]
    public void TestAbstractMethodImplementation ()
    {
        var input = @"
abstract class TestBase {
	public abstract void TestMethod (int i);
}
class TestClass : TestBase {
	public override void TestMethod (int i)
	{
	}
}";
        Test<ParameterNotUsedIssue> (input, 0);
    }

    [Test]
    public void TestUsedParameter ()
    {
        var input = @"
class TestClass {
	void TestMethod (int i)
	{
		i = 1;
	}
}";
        Test<ParameterNotUsedIssue> (input, 0);
    }

    [Test]
    public void TestLambda ()
    {
        var input = @"
class TestClass {
	void TestMethod ()
	{
		System.Action<int> a = i => {
		};
	}
}";
        Test<ParameterNotUsedIssue> (input, 0);
    }

    [Test]
    public void TestAnonymousMethod ()
    {
        var input = @"
class TestClass {
	void TestMethod ()
	{
		System.Action<int> a = delegate (int i) {
		};
	}
}";
        Test<ParameterNotUsedIssue> (input, 0);
    }


    [Test]
    public void TestMethodUsedAsDelegateMethod ()
    {
        var input = @"using System;
class TestClass {
	public event EventHandler FooEvt;
	void TestMethod ()
	{
		FooEvt += FooBar;
	}
	void FooBar (object sender, EventArgs e) {}
}";
        Test<ParameterNotUsedIssue> (input, 0);
    }
}
}
