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
package org.apache.commons.attributes.javadoc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

import com.sun.javadoc.Tag;
import com.sun.tools.doclets.Taglet;

public class CATaglet implements Taglet
{

    public static class AttributeTaglet extends CATaglet
    {

        private final String name;
        private final CATaglet caTaglet;

        public AttributeTaglet(String name, CATaglet caTaglet)
        {
            this.name = name;
            this.caTaglet = caTaglet;
        }

        public String getName()
        {
            return name;
        }

        public String toString(Tag[] tags)
        {
            caTaglet.addTags(tags);
            return null;
        }
    }

    /**
     * Name of custom tag.
     */
    public static final String NAME
        = "org.apache.commons.attributes.CATaglet";

    /**
     * Fully qualified class name of the legacy taglet class of JDK1.5.
     */
    public static final String LEGACY_TAGLET_CLASS_NAME
        = "com.sun.tools.doclets.internal.toolkit.taglets.LegacyTaglet";

    /**
     * List of tags.
     */
    private List tagList = new ArrayList();

    /**
     * Jdk1.5 legacy taglet class constructor.
     */
    private Constructor legacyTagletClassConstructor = null;

    /**
     * Default constructor.
     */
    public CATaglet()
    {
        try
        {
            Class legacyTagletClass = Class.forName(LEGACY_TAGLET_CLASS_NAME);
            legacyTagletClassConstructor
                = legacyTagletClass.getConstructor(new Class[] {Taglet.class});
            //System.err.println("CATaglet will be wrapped.");
        }
        catch (Exception e)
        {
            /**
             * Legacy taglet class not available. Seams that JDK1.4 is used to
             * generate javadoc. No wrapping of taglets necessary.
             */

            //System.err.println("CATaglet will not be wrapped. See stack trace.");
            //e.printStackTrace();
        }
    }

    public void addTags(Tag[] tags)
    {
        for (int i = 0; i < tags.length; i++)
        {
            tagList.add(tags[i].name() + " " + tags[i].text());
        }
    }

    /**
     * Return the name of this custom tag.
     */
    public String getName()
    {
        return NAME;
    }

    public boolean inField()
    {
        return true;
    }

    public boolean inConstructor()
    {
        return true;
    }

    public boolean inMethod()
    {
        return true;
    }

    public boolean inOverview()
    {
        return false;
    }

    public boolean inPackage()
    {
        return false;
    }

    public boolean inType()
    {
        return true;
    }

    public boolean isInlineTag()
    {
        return false;
    }

    public static void register(Map tagletMap)
    {
        CATaglet caTaglet = new CATaglet();
        caTaglet.registerTags(tagletMap);
    }

    public void registerTags(Map tagletMap)
    {
        Set tagNames = new HashSet();

        StringTokenizer tok = new StringTokenizer(
            System.getProperty(
                "org.apache.commons.attributes.javadoc.CATaglet.sources"),
            File.pathSeparator);
        while (tok.hasMoreTokens())
        {
            try
            {
                scanFiles(new File(tok.nextToken()), tagNames);
            }
            catch (Exception e)
            {
                System.err.println("Caught " + e.toString() + " trying to scan "
                                   + "Java sources. Javadoc of attributes may be incomplete.");
            }
        }
        if (tagNames.size() > 0)
        {
            Iterator iter = tagNames.iterator();
            while (iter.hasNext())
            {
                String name = (String) iter.next();
                register(name, tagletMap);
            }

            if (tagletMap.containsKey(NAME))
            {
                tagletMap.remove(NAME);
            }
            tagletMap.put(NAME, wrapTaglet(this));
        }
    }

    private void scanFiles(File directory, Collection tagNames)
    throws Exception
    {
        File[] files = directory.listFiles();
        if (files == null)
        {
            return;
        }

        for (int i = 0; i < files.length; i++)
        {
            if (files[i].isDirectory())
            {
                scanFiles(files[i], tagNames);
            }
            else
            {
                scanFile(files[i], tagNames);
            }
        }
    }

    private void scanFile(File file, Collection tagNames) throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader(file));
        try
        {
            String line = null;
            while ((line = br.readLine()) != null)
            {
                scanLine(line, tagNames);
            }
        }
        finally
        {
            br.close();
        }
    }

    private void scanLine(String line, Collection tagNames) throws Exception
    {
        int start = line.indexOf("@@");
        while (start != -1)
        {
            int end = line.indexOf(" ", start);
            if (end == -1)
            {
                end = line.length();
            }
            tagNames.add(line.substring(start + 1, end));
            start = line.indexOf("@@", end);
        }
    }

    private void register(String name, Map tagletMap)
    {
        Taglet tag = new AttributeTaglet("@" + name, this);
        if (tagletMap.containsKey(name))
        {
            tagletMap.remove(name);
        }
        tagletMap.put(name, wrapTaglet(tag));
    }

    /**
     * Wraps a JDK1.4 taglet with a JDK1.5 legacy taglet. This is only done if
     * class <code>com.sun.tools.doclets.internal.toolkit.taglets.LegacyTaglet</code>
     * exists, what means that JDK1.5 is used to generate Javadoc.
     *
     * @param tag Is the taglet to wrap.
     * @return Returns the wrapped taglet.
     */
    private Object wrapTaglet(Taglet taglet)
    {
        Object wrappedTaglet = taglet;
        if (legacyTagletClassConstructor != null)
        {
            try
            {
                wrappedTaglet = legacyTagletClassConstructor.newInstance(
                                    new Object[] {taglet});
            }
            catch (Exception e)
            {
                System.err.println("Wrapping of CATaglet failed.");
                e.printStackTrace();
            }
        }
        return wrappedTaglet;
    }

    public String toString(Tag tag)
    {
        return null;
    }

    public String toString(Tag[] _t)
    {
        String[] tags = (String[]) tagList.toArray(new String[0]);

        if (tags.length == 0)
        {
            return null;
        }

        // Sort by target
        Map targets = new TreeMap();
        for (int i = 0; i < tags.length; i++)
        {
            String target = "";
            String attribute = tags[i];
            if (tags[i].startsWith("@@."))
            {
                int targetEnd = tags[i].indexOf(" ", 3);
                if (targetEnd != -1)
                {
                    target = tags[i].substring(3, targetEnd);
                    attribute = "@@" + tags[i].substring(targetEnd).trim();
                }
            }

            if (!targets.containsKey(target))
            {
                targets.put(target, new ArrayList());
            }

            List tagsForTarget = (List) targets.get(target);
            tagsForTarget.add(attribute);
        }

        StringBuffer result = new StringBuffer();
        result.append("<DT><B>Attributes:</B>");
        List attrs = (List) targets.remove("");
        if (attrs != null)
        {
            result.append("<DD><CODE>");
            Iterator iter = attrs.iterator();
            while (iter.hasNext())
            {
                result.append(iter.next());
                if (iter.hasNext())
                {
                    result.append("<BR>");
                }
            }
            result.append("</CODE>");
        }

        List returnAttrs = (List) targets.remove("return");
        if (targets.size() > 0)
        {
            result.append("<DT><B>Parameter Attributes:</B>");
            Iterator parameterTargets = targets.keySet().iterator();
            while (parameterTargets.hasNext())
            {
                String target = (String) parameterTargets.next();
                attrs = (List) targets.remove(target);
                result.append("<DD><CODE>" + target + "</CODE> - <BR><CODE>");
                Iterator iter = attrs.iterator();
                while (iter.hasNext())
                {
                    result.append("&#160;&#160;&#160;&#160;" + iter.next());
                    if (iter.hasNext())
                    {
                        result.append("<BR>");
                    }
                }
                result.append("</CODE>");
            }
        }

        if (returnAttrs != null)
        {
            result.append("<DT><B>Return Value Attributes:</B>");
            result.append("<DD><CODE>");
            Iterator iter = returnAttrs.iterator();
            while (iter.hasNext())
            {
                result.append(iter.next());
                if (iter.hasNext())
                {
                    result.append("<BR>");
                }
            }
            result.append("</CODE>");
        }

        tagList.clear();

        return result.toString();
    }
}
