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
package org.apache.commons.attributes.test;

import junit.framework.TestCase;

import org.apache.commons.attributes.Attributes;
import org.apache.commons.attributes.Target;
import org.apache.commons.attributes.InvalidAttributeTargetError;

public class TargetTestCase extends TestCase
{

    /**
     * @@Target(Target.METHOD)
     */
    public static class AttributeWithTargetMethod {}

    /**
     * @@Target(Target.CLASS)
     */
    public static class AttributeWithTargetClass {}

    /**
     * @@Target(Target.FIELD)
     */
    public static class AttributeWithTargetField {}

    /**
     * @@Target(Target.CONSTRUCTOR)
     */
    public static class AttributeWithTargetConstructor {}

    /**
     * @@Target(Target.METHOD_PARAMETER)
     */
    public static class AttributeWithTargetMethodParameter {}

    /**
     * @@Target(Target.CONSTRUCTOR_PARAMETER)
     */
    public static class AttributeWithTargetConstructorParameter {}

    /**
     * @@Target(Target.RETURN)
     */
    public static class AttributeWithTargetReturn {}

    /*
     *
    -------------------------------------------------------------------
    *
    */
    private void expectSuccess (Class[] classes)
    {
        for (int i = 0; i < classes.length; i++)
        {
            Attributes.getAttributes (classes[i]);
        }
    }

    private void expectFail (Class[] classes)
    {
        for (int i = 0; i < classes.length; i++)
        {
            try
            {
                Attributes.getAttributes (classes[i]);
                fail (classes[i].getName () + " should throw an InvalidAttributeTargetError");
            }
            catch (InvalidAttributeTargetError iate)
            {
                // OK.
            }
        }
    }

    /*
     *
    -------------------------------------------------------------------
    *
    */

    /**
     * @@TargetTestCase.AttributeWithTargetMethod()
     */
    public static class ClassWithInvalidDeclarations {}

    public void testErrorMessage () throws Exception
    {
        Class clazz1 = ClassWithInvalidDeclarations.class;

        try
        {
            Attributes.getAttributes (clazz1);

            fail ();
        }
        catch (InvalidAttributeTargetError iate)
        {
            assertEquals ("Attributes of type org.apache.commons.attributes.test.TargetTestCase$AttributeWithTargetMethod " +
                          "can't be applied to org.apache.commons.attributes.test.TargetTestCase$ClassWithInvalidDeclarations. " +
                          "They can only be applied to: METHOD",
                          iate.getMessage ());
            // OK.
        }
    }

    /** @@TargetTestCase.AttributeWithTargetClass() */
    public static class ClassAttributes0 {}
    /** @@TargetTestCase.AttributeWithTargetMethod() */
    public static class ClassAttributes1 {}
    /** @@TargetTestCase.AttributeWithTargetField() */
    public static class ClassAttributes2 {}
    /** @@TargetTestCase.AttributeWithTargetMethodParameter() */
    public static class ClassAttributes3 {}
    /** @@TargetTestCase.AttributeWithTargetConstructorParameter() */
    public static class ClassAttributes4 {}
    /** @@TargetTestCase.AttributeWithTargetReturn() */
    public static class ClassAttributes5 {}
    /** @@TargetTestCase.AttributeWithTargetConstructor() */
    public static class ClassAttributes6 {}

    public void testClassAttributes () throws Exception
    {
        expectSuccess (new Class[] { ClassAttributes0.class });

        expectFail (new Class[]
                    {
                        ClassAttributes1.class,
                        ClassAttributes2.class,
                        ClassAttributes3.class,
                        ClassAttributes4.class,
                        ClassAttributes5.class,
                        ClassAttributes6.class
                    });
    }


    public static class FieldAttributes0
    {
        /** @@TargetTestCase.AttributeWithTargetClass() */
        private Object o;
    }
    public static class FieldAttributes1
    {
        /** @@TargetTestCase.AttributeWithTargetMethod() */
        private Object o;
    }
    public static class FieldAttributes2
    {
        /** @@TargetTestCase.AttributeWithTargetField() */
        private Object o;
    }
    public static class FieldAttributes3
    {
        /** @@TargetTestCase.AttributeWithTargetMethodParameter() */
        private Object o;
    }
    public static class FieldAttributes4
    {
        /** @@TargetTestCase.AttributeWithTargetConstructorParameter() */
        private Object o;
    }
    public static class FieldAttributes5
    {
        /** @@TargetTestCase.AttributeWithTargetReturn() */
        private Object o;
    }
    public static class FieldAttributes6
    {
        /** @@TargetTestCase.AttributeWithTargetConstructor() */
        private Object o;
    }

    public void testFieldAttributes () throws Exception
    {
        expectSuccess (new Class[] { FieldAttributes2.class });

        expectFail (new Class[]
                    {
                        FieldAttributes0.class,
                        FieldAttributes1.class,
                        FieldAttributes3.class,
                        FieldAttributes4.class,
                        FieldAttributes5.class,
                        FieldAttributes6.class
                    });
    }

    public static class MethodAttributes0
    {
        /** @@TargetTestCase.AttributeWithTargetClass() */
        private void method () {};
    }
    public static class MethodAttributes1
    {
        /** @@TargetTestCase.AttributeWithTargetMethod() */
        private void method () {};
    }
    public static class MethodAttributes2
    {
        /** @@TargetTestCase.AttributeWithTargetField() */
        private void method () {};
    }
    public static class MethodAttributes3
    {
        /** @@TargetTestCase.AttributeWithTargetMethodParameter() */
        private void method () {};
    }
    public static class MethodAttributes4
    {
        /** @@TargetTestCase.AttributeWithTargetConstructorParameter() */
        private void method () {};
    }
    public static class MethodAttributes5
    {
        /** @@TargetTestCase.AttributeWithTargetReturn() */
        private void method () {};
    }
    public static class MethodAttributes6
    {
        /** @@TargetTestCase.AttributeWithTargetConstructor() */
        private void method () {};
    }

    public void testMethodAttributes () throws Exception
    {
        expectSuccess (new Class[] { MethodAttributes1.class });

        expectFail (new Class[]
                    {
                        MethodAttributes0.class,
                        MethodAttributes2.class,
                        MethodAttributes3.class,
                        MethodAttributes4.class,
                        MethodAttributes5.class,
                        MethodAttributes6.class
                    });
    }

    public static class ConstructorAttributes0
    {
        /** @@TargetTestCase.AttributeWithTargetClass() */
        public ConstructorAttributes0 () {};
    }
    public static class ConstructorAttributes1
    {
        /** @@TargetTestCase.AttributeWithTargetMethod() */
        public ConstructorAttributes1 () {};
    }
    public static class ConstructorAttributes2
    {
        /** @@TargetTestCase.AttributeWithTargetField() */
        public ConstructorAttributes2 () {};
    }
    public static class ConstructorAttributes3
    {
        /** @@TargetTestCase.AttributeWithTargetMethodParameter() */
        public ConstructorAttributes3 () {};
    }
    public static class ConstructorAttributes4
    {
        /** @@TargetTestCase.AttributeWithTargetConstructorParameter() */
        public ConstructorAttributes4 () {};
    }
    public static class ConstructorAttributes5
    {
        /** @@TargetTestCase.AttributeWithTargetReturn() */
        public ConstructorAttributes5 () {};
    }
    public static class ConstructorAttributes6
    {
        /** @@TargetTestCase.AttributeWithTargetConstructor() */
        public ConstructorAttributes6 () {};
    }

    public void testConstructorAttributes () throws Exception
    {
        expectSuccess (new Class[] { ConstructorAttributes6.class });

        expectFail (new Class[]
                    {
                        ConstructorAttributes0.class,
                        ConstructorAttributes1.class,
                        ConstructorAttributes2.class,
                        ConstructorAttributes3.class,
                        ConstructorAttributes4.class,
                        ConstructorAttributes5.class
                    });
    }

    public static class ConstructorParameterAttributes0
    {
        /** @@.param TargetTestCase.AttributeWithTargetClass() */
        public ConstructorParameterAttributes0 (int param) {};
    }
    public static class ConstructorParameterAttributes1
    {
        /** @@.param TargetTestCase.AttributeWithTargetMethod() */
        public ConstructorParameterAttributes1 (int param) {};
    }
    public static class ConstructorParameterAttributes2
    {
        /** @@.param TargetTestCase.AttributeWithTargetField() */
        public ConstructorParameterAttributes2 (int param) {};
    }
    public static class ConstructorParameterAttributes3
    {
        /** @@.param TargetTestCase.AttributeWithTargetMethodParameter() */
        public ConstructorParameterAttributes3 (int param) {};
    }
    public static class ConstructorParameterAttributes4
    {
        /** @@.param TargetTestCase.AttributeWithTargetConstructorParameter() */
        public ConstructorParameterAttributes4 (int param) {};
    }
    public static class ConstructorParameterAttributes5
    {
        /** @@.param TargetTestCase.AttributeWithTargetReturn() */
        public ConstructorParameterAttributes5 (int param) {};
    }
    public static class ConstructorParameterAttributes6
    {
        /** @@.param TargetTestCase.AttributeWithTargetConstructor() */
        public ConstructorParameterAttributes6 (int param) {};
    }

    public void testConstructorParameterAttributes () throws Exception
    {
        expectSuccess (new Class[] { ConstructorParameterAttributes4.class });

        expectFail (new Class[]
                    {
                        ConstructorParameterAttributes0.class,
                        ConstructorParameterAttributes1.class,
                        ConstructorParameterAttributes2.class,
                        ConstructorParameterAttributes3.class,
                        ConstructorParameterAttributes5.class,
                        ConstructorParameterAttributes6.class
                    });
    }

    public static class MethodParameterAttributes0
    {
        /** @@.param TargetTestCase.AttributeWithTargetClass() */
        private void method (int param) {};
    }
    public static class MethodParameterAttributes1
    {
        /** @@.param TargetTestCase.AttributeWithTargetMethod() */
        private void method (int param) {};
    }
    public static class MethodParameterAttributes2
    {
        /** @@.param TargetTestCase.AttributeWithTargetField() */
        private void method (int param) {};
    }
    public static class MethodParameterAttributes3
    {
        /** @@.param TargetTestCase.AttributeWithTargetMethodParameter() */
        private void method (int param) {};
    }
    public static class MethodParameterAttributes4
    {
        /** @@.param TargetTestCase.AttributeWithTargetConstructorParameter() */
        private void method (int param) {};
    }
    public static class MethodParameterAttributes5
    {
        /** @@.param TargetTestCase.AttributeWithTargetReturn() */
        private void method (int param) {};
    }
    public static class MethodParameterAttributes6
    {
        /** @@.param TargetTestCase.AttributeWithTargetConstructor() */
        private void method (int param) {};
    }

    public void testMethodParameterAttributes () throws Exception
    {
        expectSuccess (new Class[] { MethodParameterAttributes3.class });

        expectFail (new Class[]
                    {
                        MethodParameterAttributes0.class,
                        MethodParameterAttributes1.class,
                        MethodParameterAttributes2.class,
                        MethodParameterAttributes4.class,
                        MethodParameterAttributes5.class,
                        MethodParameterAttributes6.class
                    });
    }

    public static class ReturnAttributes0
    {
        /** @@.return TargetTestCase.AttributeWithTargetClass() */
        private int method (int param)
        {
            return 0;
        };
    }
    public static class ReturnAttributes1
    {
        /** @@.return TargetTestCase.AttributeWithTargetMethod() */
        private int method (int param)
        {
            return 0;
        };
    }
    public static class ReturnAttributes2
    {
        /** @@.return TargetTestCase.AttributeWithTargetField() */
        private int method (int param)
        {
            return 0;
        };
    }
    public static class ReturnAttributes3
    {
        /** @@.return TargetTestCase.AttributeWithTargetMethodParameter() */
        private int method (int param)
        {
            return 0;
        };
    }
    public static class ReturnAttributes4
    {
        /** @@.return TargetTestCase.AttributeWithTargetConstructorParameter() */
        private int method (int param)
        {
            return 0;
        };
    }
    public static class ReturnAttributes5
    {
        /** @@.return TargetTestCase.AttributeWithTargetReturn() */
        private int method (int param)
        {
            return 0;
        };
    }
    public static class ReturnAttributes6
    {
        /** @@.return TargetTestCase.AttributeWithTargetConstructor() */
        private int method (int param)
        {
            return 0;
        };
    }

    public void testReturnAttributes () throws Exception
    {
        expectSuccess (new Class[] { ReturnAttributes5.class });

        expectFail (new Class[]
                    {
                        ReturnAttributes0.class,
                        ReturnAttributes1.class,
                        ReturnAttributes2.class,
                        ReturnAttributes3.class,
                        ReturnAttributes4.class,
                        ReturnAttributes6.class
                    });
    }
}
