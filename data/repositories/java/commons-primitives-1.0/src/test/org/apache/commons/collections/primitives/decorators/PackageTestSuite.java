/*
 * $Header: /home/cvs/jakarta-commons/primitives/src/test/org/apache/commons/collections/primitives/decorators/PackageTestSuite.java,v 1.7 2003/10/29 20:07:54 rwaldhoff Exp $
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowledgement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgement may appear in the software itself,
 *    if and wherever such third-party acknowledgements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package org.apache.commons.collections.primitives.decorators;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Test this package.
 *
 * @version $Revision: 1.7 $ $Date: 2003/10/29 20:07:54 $
 * @author Rodney Waldhoff
 */
public class PackageTestSuite extends TestCase
{
    public PackageTestSuite(String testName)
    {
        super(testName);
    }

    public static void main(String args[])
    {
        String[] testCaseName = { PackageTestSuite.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite();

        suite.addTest(TestBaseProxyByteCollection.suite());
        suite.addTest(TestBaseProxyByteList.suite());
        suite.addTest(TestUnmodifiableByteList.suite());
        suite.addTest(TestUnmodifiableByteIterator.suite());
        suite.addTest(TestUnmodifiableByteListIterator.suite());

        suite.addTest(TestBaseProxyCharCollection.suite());
        suite.addTest(TestBaseProxyCharList.suite());
        suite.addTest(TestUnmodifiableCharList.suite());
        suite.addTest(TestUnmodifiableCharIterator.suite());
        suite.addTest(TestUnmodifiableCharListIterator.suite());

        suite.addTest(TestBaseProxyDoubleCollection.suite());
        suite.addTest(TestBaseProxyDoubleList.suite());
        suite.addTest(TestUnmodifiableDoubleList.suite());
        suite.addTest(TestUnmodifiableDoubleIterator.suite());
        suite.addTest(TestUnmodifiableDoubleListIterator.suite());

        suite.addTest(TestBaseProxyFloatCollection.suite());
        suite.addTest(TestBaseProxyFloatList.suite());
        suite.addTest(TestUnmodifiableFloatList.suite());
        suite.addTest(TestUnmodifiableFloatIterator.suite());
        suite.addTest(TestUnmodifiableFloatListIterator.suite());

        suite.addTest(TestBaseProxyShortCollection.suite());
        suite.addTest(TestBaseProxyShortList.suite());
        suite.addTest(TestUnmodifiableShortList.suite());
        suite.addTest(TestUnmodifiableShortIterator.suite());
        suite.addTest(TestUnmodifiableShortListIterator.suite());

        suite.addTest(TestBaseProxyIntCollection.suite());
        suite.addTest(TestBaseProxyIntList.suite());
        suite.addTest(TestUnmodifiableIntList.suite());
        suite.addTest(TestUnmodifiableIntIterator.suite());
        suite.addTest(TestUnmodifiableIntListIterator.suite());

        suite.addTest(TestBaseProxyLongCollection.suite());
        suite.addTest(TestBaseProxyLongList.suite());
        suite.addTest(TestUnmodifiableLongList.suite());
        suite.addTest(TestUnmodifiableLongIterator.suite());
        suite.addTest(TestUnmodifiableLongListIterator.suite());

        return suite;
    }
}

