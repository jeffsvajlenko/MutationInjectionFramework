////
//// MemberTests.cs
////
//// Author:
////   Mike Krüger <mkrueger@novell.com>
////
//// Copyright (C) 2008 Novell, Inc (http://www.novell.com)
////
//// Permission is hereby granted, free of charge, to any person obtaining
//// a copy of this software and associated documentation files (the
//// "Software"), to deal in the Software without restriction, including
//// without limitation the rights to use, copy, modify, merge, publish,
//// distribute, sublicense, and/or sell copies of the Software, and to
//// permit persons to whom the Software is furnished to do so, subject to
//// the following conditions:
////
//// The above copyright notice and this permission notice shall be
//// included in all copies or substantial portions of the Software.
////
//// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
//// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
//// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
//// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
//// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
////
//
//using System;
//using System.Collections.Generic;
//
//using NUnit.Framework;
//using MonoDevelop.CSharpBinding;
//using MonoDevelop.CSharp.Parser;
//
//namespace MonoDevelop.CSharpBinding.Tests
//{
//	[TestFixture]
//	public class MemberTests
//	{
//		void DoTestFields (IParser parser)
//		{
//			ICompilationUnit unit = parser.Parse (null, "a.cs", @"class AClass { int x, y; }").CompilationUnit;
//			Assert.AreEqual (1, unit.Types.Count);
//			IType type = unit.Types[0];
//
//			Assert.AreEqual (2, type.FieldCount);
//			List<IField> fields = new List<IField> (type.Fields);
//			Assert.AreEqual ("x", fields[0].Name);
//			Assert.AreEqual ("System.Int32", fields[0].ReturnType.FullName);
//			Assert.AreEqual ("y", fields[1].Name);
//			Assert.AreEqual ("System.Int32", fields[1].ReturnType.FullName);
//		}
//
//		[Test]
//		public void TestFields ()
//		{
//			DoTestFields (new McsParser ());
////			DoTestFields (new DomParser ());
//		}
//
//		void DoTestMethods (IParser parser)
//		{
//			ICompilationUnit unit = parser.Parse (null, "a.cs",
//@"class AClass {
//     void TestMethod<T> (string a, int b) {}
//     public static void ExtensionMethod (this int a) {};
//}").CompilationUnit;
//			Assert.AreEqual (1, unit.Types.Count);
//			IType type = unit.Types[0];
//
//			Assert.AreEqual (2, type.MethodCount);
//			List<IMethod> methods = new List<IMethod> (type.Methods);
//			Assert.AreEqual ("TestMethod", methods[0].Name);
//			Assert.AreEqual (2, methods[0].Parameters.Count);
//			Assert.AreEqual ("a", methods[0].Parameters[0].Name);
//			Assert.AreEqual ("b", methods[0].Parameters[1].Name);
//
//			Assert.IsFalse (methods[1].IsConstructor);
//			Assert.IsTrue (methods[1].IsExtension);
//			Assert.AreEqual (1, methods[1].Parameters.Count);
//			Assert.AreEqual ("a", methods[1].Parameters[0].Name);
//		}
//
//		[Test]
//		public void TestMethods ()
//		{
//			DoTestMethods (new McsParser ());
////			DoTestMethods (new DomParser ());
//		}
//
//		void DoTestConstructor (IParser parser)
//		{
//			ICompilationUnit unit = parser.Parse (null, "a.cs",
//@"public abstract class BaseClass {
//     BaseClass () {}
//  	protected BaseClass(int id)
//    {
//    }
//}").CompilationUnit;
//			Assert.AreEqual (1, unit.Types.Count);
//			IType type = unit.Types[0];
//
//			Assert.AreEqual (2, type.ConstructorCount);
//			List<IMethod> methods = new List<IMethod> (type.Methods);
//			Assert.IsTrue (methods[0].IsConstructor);
//			Assert.IsTrue (methods[1].IsConstructor);
//		}
//
//		[Test]
//		public void TestConstructor ()
//		{
//			DoTestConstructor (new McsParser ());
////			DoTestConstructor (new DomParser ());
//		}
//
//		void DoTestProperties (IParser parser)
//		{
//			ICompilationUnit unit = parser.Parse (null, "a.cs",
//@"class AClass {
//	int MyProperty {
//		get { }
//		set { }
//	}
//	string MyProperty2 {
//		set { }
//	}
//	string MyProperty3 {
//		get { }
//	}
//}").CompilationUnit;
//			Assert.AreEqual (1, unit.Types.Count);
//			IType type = unit.Types[0];
//
//			Assert.AreEqual (3, type.PropertyCount);
//			List<IProperty> properties = new List<IProperty> (type.Properties);
//			Assert.AreEqual ("MyProperty", properties[0].Name);
//			Assert.IsTrue (properties[0].HasGet);
//			Assert.IsTrue (properties[0].HasSet);
//			Assert.IsFalse (properties[0].IsIndexer);
//
//			Assert.AreEqual ("MyProperty2", properties[1].Name);
//			Assert.IsFalse (properties[1].HasGet);
//			Assert.IsTrue (properties[1].HasSet);
//			Assert.IsFalse (properties[1].IsIndexer);
//
//			Assert.AreEqual ("MyProperty3", properties[2].Name);
//			Assert.IsTrue (properties[2].HasGet);
//			Assert.IsFalse (properties[2].HasSet);
//			Assert.IsFalse (properties[2].IsIndexer);
//		}
//
//		[Test]
//		public void TestProperties ()
//		{
//			DoTestProperties (new McsParser ());
////			DoTestProperties (new DomParser ());
//		}
//
//		void DoTestIndexer (IParser parser)
//		{
//			ICompilationUnit unit = parser.Parse (null, "a.cs",
//@"class AClass {
//	int this[int a] {
//		get { }
//		set { }
//	}
//}").CompilationUnit;
//			Assert.AreEqual (1, unit.Types.Count);
//			IType type = unit.Types[0];
//
//			Assert.AreEqual (1, type.IndexerCount);
//			List<IProperty> properties = new List<IProperty> (type.Properties);
//			Assert.IsTrue (properties[0].IsIndexer);
//			Assert.AreEqual (1, properties[0].Parameters.Count);
//			Assert.AreEqual ("a", properties[0].Parameters[0].Name);
//		}
//
//		[Test]
//		public void TestIndexer ()
//		{
//			DoTestIndexer (new McsParser ());
////			DoTestIndexer (new DomParser ());
//		}
//
//		void DoTestEvents (IParser parser)
//		{
//			ICompilationUnit unit = parser.Parse (null, "a.cs",
//@"class AClass {
//	event EventHandler TestEvent;
//}").CompilationUnit;
//			Assert.AreEqual (1, unit.Types.Count);
//			IType type = unit.Types[0];
//
//			Assert.AreEqual (1, type.EventCount);
//			List<IEvent> events = new List<IEvent> (type.Events);
//			Assert.AreEqual ("TestEvent", events[0].Name);
//			Assert.AreEqual ("EventHandler", events[0].ReturnType.Name);
//		}
//
//		[Test]
//		public void TestEvents ()
//		{
//			DoTestEvents (new McsParser ());
////			DoTestEvents (new DomParser ());
//		}
//
//	}
//}
