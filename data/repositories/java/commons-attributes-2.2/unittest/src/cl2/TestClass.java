/*
 * Copyright 2003-2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @@TestAttribute ("TestClass")
 */
public class TestClass
{

    /**
     * @@TestAttribute ("TestClass.ctor")
     * @@.parameter TestAttribute ("TestClass.ctor.parameter")
     */
    public TestClass (int parameter)
    {
    }

    /**
     * @@TestAttribute ("TestClass.Inner")
     */
    public static class Inner {}

    /**
     * @@TestAttribute ("TestClass.field")
     */
    public Object field = null;

    /**
     * @@TestAttribute ("TestClass.method")
     * @@.parameter TestAttribute ("TestClass.method.parameter")
     * @@.return TestAttribute ("TestClass.method.return")
     */
    public Object method (int parameter)
    {
        return null;
    }
}
