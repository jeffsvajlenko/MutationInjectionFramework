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
package org.apache.commons.attributes.validation;

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
 * Ant task that validates attributes. Usage:
 *
 * <pre><code>
 *     &lt;taskdef resource="org/apache/commons/attributes/anttasks.properties"/&gt;
 *
 *     &lt;attribute-validator jarFile="myclasses.jar"&gt;
 *         &lt;classpath&gt;
 *             ...
 *         &lt;/classpath&gt;
 *         &lt;validator class="my.Validator"/&gt;
 *         &lt;validator class="my.other.Validator"/&gt;
 *     &lt;/attribute-validator&gt;
 * </code></pre>
 *
 * The task will run the validator(s) with the classes the given jar file.
 */
public class AttributeValidatorTask extends Task
{

    private File jarFile;
    private List classes = new ArrayList ();
    private List validators = new ArrayList ();
    private Path classPath;
    private File baseName;
    private boolean inMaven = false;

    public static class Validator
    {

        private String className;

        public void setClass (String className)
        {
            this.className = className;
        }

        public String getClassName ()
        {
            return className;
        }
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

    public Validator createValidator ()
    {
        Validator validator = new Validator ();
        validators.add (validator);
        return validator;
    }

    private static final String SUFFIX = "$__attributeRepository.class";

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
            log ("Validating attributes in " + jarFile.getPath ());

            JarFile jar = new JarFile (jarFile);
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
                }
            }
            finally
            {
                jar.close ();
            }

            AntClassLoader cl = new AntClassLoader (this.getClass ().getClassLoader (), project, classPath, true);
            try
            {
                cl.addPathElement (jarFile.getPath ());

                HashSet classesToValidate = new HashSet ();
                Iterator attrs = classes.iterator ();
                while (attrs.hasNext ())
                {
                    String className = (String) attrs.next ();

                    Class clazz = cl.loadClass (className);
                    classesToValidate.add (clazz);
                }

                Iterator iter = validators.iterator ();
                while (iter.hasNext ())
                {
                    Validator validator = (Validator) iter.next ();
                    Class validatorClass = cl.loadClass (validator.getClassName ());
                    AttributeValidator attrValidator = (AttributeValidator) validatorClass.newInstance ();
                    try
                    {
                        attrValidator.validate (classesToValidate);
                    }
                    catch (ValidationException ve)
                    {
                        throw new BuildException (ve.getInvalidClass () + " failed to validate: " + ve.getMessage ());
                    }
                }
            }
            finally
            {
                cl.cleanup ();
            }
        }
        catch (BuildException be)
        {
            throw be;
        }
        catch (Exception e)
        {
            e.printStackTrace ();
            throw new BuildException (e.toString ());
        }
    }
}
