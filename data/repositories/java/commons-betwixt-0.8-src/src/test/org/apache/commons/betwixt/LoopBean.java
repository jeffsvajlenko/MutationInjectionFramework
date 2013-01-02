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

package org.apache.commons.betwixt;

/** <p> This is a bean specifically designed to test cyclic references.
  * The idea is that there's a count that counts every time <code>getFriend</code>
  * gets called and throws a <code>RuntimeException</code> if the count gets too high.</p>
  *
  * @author Robert Burrell Donkin
  * @version $Revision: 438373 $
  */
public class LoopBean
{
    private static int count = 0;

    private static final int max_count = 100;

    private LoopBean friend;

    private String name;

    public static final LoopBean createNoLoopExampleBean()
    {
        LoopBean root = new LoopBean("Root");
        LoopBean levelOne = new LoopBean("level1");
        LoopBean levelTwo = new LoopBean("level2");
        LoopBean levelThree = new LoopBean("level3");
        LoopBean levelFour = new LoopBean("level4");
        LoopBean levelFive = new LoopBean("level5");

        root.setFriend(levelOne);
        levelOne.setFriend(levelTwo);
        levelTwo.setFriend(levelThree);
        levelThree.setFriend(levelFour);
        levelFour.setFriend(levelFive);

        return root;
    }

    public static final LoopBean createLoopExampleBean()
    {
        LoopBean root = new LoopBean("Root");
        LoopBean levelOne = new LoopBean("level1");
        LoopBean levelTwo = new LoopBean("level2");
        LoopBean levelThree = new LoopBean("level3");
        LoopBean levelFour = new LoopBean("level4");
        LoopBean levelFive = new LoopBean("level5");

        root.setFriend(levelOne);
        levelOne.setFriend(levelTwo);
        levelTwo.setFriend(levelThree);
        levelThree.setFriend(levelFour);
        levelFour.setFriend(levelFive);
        levelFive.setFriend(root);

        return root;
    }


    public static final LoopBean createNotEmptyNoLoopExampleBean()
    {
        LoopBean root = new LoopBean("");
        LoopBean levelOne = new LoopBean("");
        LoopBean levelTwo = new LoopBean("");
        LoopBean levelThree = new LoopBean("");
        LoopBean levelFour = new LoopBean("");
        LoopBean levelFive = new LoopBean("Not Empty");

        root.setFriend(levelOne);
        levelOne.setFriend(levelTwo);
        levelTwo.setFriend(levelThree);
        levelThree.setFriend(levelFour);
        levelFour.setFriend(levelFive);

        return root;
    }

    public static final LoopBean createEmptyLoopExampleBean()
    {
        LoopBean root = new LoopBean("");
        LoopBean levelOne = new LoopBean("");
        LoopBean levelTwo = new LoopBean("");
        LoopBean levelThree = new LoopBean("");
        LoopBean levelFour = new LoopBean("");
        LoopBean levelFive = new LoopBean("");

        root.setFriend(levelOne);
        levelOne.setFriend(levelTwo);
        levelTwo.setFriend(levelThree);
        levelThree.setFriend(levelFour);
        levelFour.setFriend(levelFive);
        levelFive.setFriend(root);

        return root;
    }

    public static final LoopBean createIdOnlyLoopExampleBean()
    {
        LoopBean root = new LoopBean("Root");
        LoopBean levelOne = new LoopBean("level1");
        LoopBean levelTwo = new LoopBean("level2");
        LoopBean levelThree = new LoopBean("level3");
        LoopBean levelFour = new LoopBean("level4");
        LoopBean levelFive = new LoopBean("level5");
        LoopBean notRoot = new LoopBean("Root");

        root.setFriend(levelOne);
        levelOne.setFriend(levelTwo);
        levelTwo.setFriend(levelThree);
        levelThree.setFriend(levelFour);
        levelFour.setFriend(levelFive);
        levelFive.setFriend(notRoot);

        return root;
    }

    public LoopBean(String name)
    {
        this.name = name;
    }

    public LoopBean getFriend()
    {
        if (++count > max_count)
        {
            throw new RuntimeException("Looping!");
        }
        return friend;
    }

    public void setFriend(LoopBean friend)
    {
        this.friend = friend;
    }

    public String getName()
    {
        return name;
    }

    public String toString()
    {
        return "[LoopBean] name=" + name;
    }
}
