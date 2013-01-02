/*
 * Copyright 2003-2005 The Apache Software Foundation
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
package org.apache.commons.attributes.compiler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import org.apache.commons.attributes.AttributeRepositoryClass;
import org.apache.commons.attributes.Attributes;
import org.apache.commons.attributes.AttributeUtil;
import org.apache.commons.attributes.Indexed;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;

/**
 * Ant task to compile attribute indexes. Usage:
 *
 * <pre><code>
 *     &lt;taskdef resource="org/apache/commons/attributes/anttasks.properties"/&gt;
 *
 *     &lt;attribute-indexer jarFile="myclasses.jar"&gt;
 *         &lt;classpath&gt;
 *             ...
 *         &lt;/classpath&gt;
 *     &lt;/attribute-indexer&gt;
 * </code></pre>
 *
 * The task will inspect the classes in the given jar and add a <tt>META-INF/attrs.index</tt>
 * file to it, which contains the index information. The classpath element is required and
 * must contain all dependencies for the attributes used.
 */
public class AttributeIndexer extends Task
{

    private File jarFile;
    private List classes = new ArrayList ();
    private Path classPath;
    private File baseName;
    private boolean inMaven = false;

    private final static String INDEX_FILENAME = "META-INF/attrs.index";

    public AttributeIndexer ()
    {
    }

    public void setJarfile (File jarFile)
    {
        this.jarFile = jarFile;
    }

    public void setBaseName (File baseName)
    {
        inMaven = true;
        this.baseName = baseName;
    }

    public Path createClasspath ()
    {
        this.classPath = new Path(project);
        return classPath;
    }

    private static final String SUFFIX = "$__attributeRepository.class";

    protected void copyEntry (JarFile jar, JarEntry entry, JarOutputStream outputStream) throws Exception
    {
        outputStream.putNextEntry (entry);

        if (!entry.isDirectory ())
        {
            InputStream is = new BufferedInputStream (jar.getInputStream (entry));
            try
            {
                byte[] buffer = new byte[16384];
                while (true)
                {
                    int numRead = is.read (buffer, 0, 16384);
                    if (numRead == 0 || numRead == -1)
                    {
                        break;
                    }

                    outputStream.write (buffer, 0, numRead);
                }
            }
            finally
            {
                is.close ();
            }
        }
    }

    protected void findJarFile () throws BuildException
    {
        File[] allFiles = baseName.getParentFile ().listFiles ();
        if (allFiles == null)
        {
            throw new BuildException ("Unable to find any file with base name " + baseName.getName ()
                                      + " in " + baseName.getParentFile ().getPath ());
        }

        long newestDate = 0;
        for (int i = 0; i < allFiles.length; i++)
        {
            String name = allFiles[i].getName ();
            if (name.startsWith (baseName.getName ()) && name.endsWith (".jar") &&
                    allFiles[i].lastModified () > newestDate)
            {
                jarFile = allFiles[i];
                newestDate = allFiles[i].lastModified ();
            }
        }

        if (jarFile == null)
        {
            throw new BuildException ("Unable to find any file with base name " + baseName.getName ()
                                      + " in " + baseName.getParentFile ().getPath ());
        }
    }

    public void execute () throws BuildException
    {
        if (inMaven)
        {
            findJarFile ();
        }
        if (!jarFile.exists ())
        {
            log ("Can't find " + jarFile.getPath ());
            return;
        }
        try
        {
            log ("Creating attribute index for " + jarFile.getPath ());

            JarFile jar = new JarFile (jarFile);
            File newJarFile = new File (jarFile.getPath () + ".new");
            JarOutputStream output = new JarOutputStream (new FileOutputStream (newJarFile));
            try
            {
                Enumeration e = jar.entries ();
                while (e.hasMoreElements ())
                {
                    JarEntry entry = (JarEntry) e.nextElement ();
                    if (!entry.isDirectory ())
                    {
                        String className = entry.getName ();
                        if (className.endsWith (SUFFIX))
                        {
                            className = className.replace ('/', '.').replace ('\\', '.').substring (0, className.length () - SUFFIX.length ());
                            classes.add (className);
                        }
                    }

                    if (!entry.getName ().equals (INDEX_FILENAME))
                    {
                        copyEntry (jar, entry, output);
                    }
                }

                output.putNextEntry (new JarEntry (INDEX_FILENAME));

                Iterator attrs = classes.iterator ();
                while (attrs.hasNext ())
                {
                    String className = (String) attrs.next ();
                    output.write (("Class: " + className + "\n").getBytes ());
                }
            }
            finally
            {
                output.close ();
                jar.close ();
            }

            jarFile.delete ();
            newJarFile.renameTo (jarFile);
        }
        catch (Exception e)
        {
            e.printStackTrace ();
            throw new BuildException (e.toString ());
        }
    }
}
