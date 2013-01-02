/*
*
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
*
*/
import org.apache.commons.attributes.Attributes;

class MyAttribute
{
    private final String ctorArg;
    private String namedArg = null;

    public MyAttribute (String ctorArg)
    {
        this.ctorArg = ctorArg;
    }

    public void setNamedArgument (String namedArg)
    {
        this.namedArg = namedArg;
    }

    public String toString ()
    {
        return "[MyAttribute  constructor argument: \"" +
               ctorArg + "\" named argument: \"" + namedArg + "\"]";
    }
}

/**
 * @@MyAttribute ("This string is passed to the constructor.",
 *                namedArgument="This argument will be passed to the setNamedArgument method")
 */
public class AttributeDemo
{
    public static void main (String args[])
    {
        System.out.println (Attributes.getAttributes (AttributeDemo.class));
    }
}
