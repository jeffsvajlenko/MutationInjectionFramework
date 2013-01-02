/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.apache.commons.betwixt.examples.rss;

import java.io.File;

import org.apache.commons.betwixt.AbstractTestCase;

/**
 * <p>Test case for example.</p>
 *
 * @author Robert Burrell Donkin
 * @version $Revision: 438373 $ $Date: 2006-08-30 06:17:21 +0100 (Wed, 30 Aug 2006) $
 */

public class TestRSS extends AbstractTestCase
{

    public TestRSS(String testName)
    {
        super(testName);
    }

    public void testPrintTextSummary() throws Exception
    {
        RSSApplication rssApplication = new RSSApplication();
        File file = new File(
            getTestFile("src/test/org/apache/commons/betwixt/examples/rss/rss-example.xml"));
        String output = rssApplication.plainTextSummary(file);
        String expected =   "channel: MozillaZine\n" +
                            "url: http://www.mozillazine.org\n" +
                            "copyright: Public Domain\n\n" +
                            "title: Java2 in Navigator 5?\n" +
                            "link: http://www.mozillazine.org/talkback.html?article=607\n" +
                            "description: Will Java2 be an integrated part of Navigator 5? " +
                            "Read more about it in this discussion...\n\n" +
                            "title: Communicator 4.61 Out\n" +
                            "link: http://www.mozillazine.org/talkback.html?article=606\n" +
                            "description: The latest version of Communicator is now " +
                            "available.  It includes security enhancements " +
                            "and various bug fixes.\n\n" +
                            "title: Mozilla Dispenses with Old, Proprietary DOM\n" +
                            "link: http://www.mozillazine.org/talkback.html?article=604\n" +
                            "description: \n\n" +
                            "title: The Animation Contest is Now Closed\n" +
                            "link: http://www.mozillazine.org/talkback.html?article=603\n" +
                            "description: \n";
        assertEquals(output, expected);
    }
}
