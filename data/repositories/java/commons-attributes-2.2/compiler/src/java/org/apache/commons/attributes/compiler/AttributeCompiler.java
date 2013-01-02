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
package org.apache.commons.attributes.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaParameter;


/**
 * Ant task to compile attributes. Usage:
 *
 * <pre><code>
 *     &lt;taskdef resource="org/apache/commons/attributes/anttasks.properties"/&gt;
 *
 *     &lt;attribute-compiler destDir="temp/"&gt; attributepackages="my.attributes;my.otherattributes"
 *         &lt;fileset dir="src/" includes="*.java"/&gt;
 *     &lt;/attribute-compiler&gt;
 * </code></pre>
 *
 * <ul>
 * <li>destDir<dd>Destination directory for generated source files
 * <li>attributepackages<dd>A set of package names that will be automatically searched for attributes.
 * </ul>
 *
 * The task should be run before compiling the Java sources, and will produce some
 * additional Java source files in the destination directory that should be compiled
 * along with the input source files. (See the overview for a diagram.)
 */
public class AttributeCompiler extends Task
{

    private final ArrayList fileSets = new ArrayList ();
    private Path src;
    private File destDir;
    private int numGenerated;
    private int numIgnored;
    private String attributePackages = "";

    public AttributeCompiler ()
    {
    }

    public void setAttributePackages (String attributePackages)
    {
        this.attributePackages = attributePackages;
    }

    public void addFileset (FileSet set)
    {
        fileSets.add (set);
    }

    public void setDestdir (File destDir)
    {
        this.destDir = destDir;
    }

    public void setSourcepathref (String pathref)
    {
        String sourcePaths = project.getReference (pathref).toString ();
        StringTokenizer tok = new StringTokenizer (sourcePaths, File.pathSeparator);
        while (tok.hasMoreTokens ())
        {
            FileSet fs = new FileSet ();
            fs.setDir (new File (tok.nextToken ()));
            fs.setIncludes ("**/*.java");
            fs.setProject (project);
            addFileset (fs);
        }
    }

    protected void copyImports (File source, PrintWriter dest) throws Exception
    {
        BufferedReader br = new BufferedReader (new FileReader (source));
        try
        {
            String line = null;
            while ((line = br.readLine ()) != null)
            {
                if (line.startsWith ("import "))
                {
                    dest.println (line);
                }
            }
        }
        finally
        {
            br.close ();
        }
    }

    protected void addExpressions (DocletTag[] tags, PrintWriter pw, String collectionName, File sourceFile)
    {
        addExpressions (tags, null, pw, collectionName, sourceFile);
    }

    protected void addExpressions (DocletTag[] tags, String selector, PrintWriter pw, String collectionName, File sourceFile)
    {
        String fileName = sourceFile != null ? sourceFile.getPath ().replace ('\\', '/') : "<unknown>";
        for (int i = 0; i < tags.length; i++)
        {
            DocletTag tag = tags[i];

            if (isAttribute (tag))
            {
                String tagName = tag.getName ();
                String tagValue = tag.getValue ();
                String expression = tagName + " " + tagValue;
                expression = expression.trim ();

                // Remove the second @-sign.
                expression = expression.substring (1);

                if (selector != null)
                {
                    if (expression.startsWith ("."))
                    {
                        // We have selector, tag does...
                        String tagSelector = expression.substring (1, expression.indexOf (" "));
                        expression = expression.substring (expression.indexOf (" ")).trim ();
                        if (!selector.equals (tagSelector))
                        {
                            // ...but they didn't match.
                            continue;
                        }
                    }
                    else
                    {
                        // We have selector, but tag doesn't
                        continue;
                    }
                }
                else
                {
                    // No selector, but tag has selector.
                    if (expression.startsWith ("."))
                    {
                        continue;
                    }
                }

                pw.println ("        {");
                outputAttributeExpression (pw, expression, fileName, tag.getLineNumber (), "_attr");
                pw.println ("        Object _oattr = _attr; // Need to erase type information");
                pw.println ("        if (_oattr instanceof org.apache.commons.attributes.Sealable) {");
                pw.println ("            ((org.apache.commons.attributes.Sealable) _oattr).seal ();");
                pw.println ("        }");
                pw.println ("        " + collectionName + ".add ( _attr );");
                pw.println ("        }");
            }
        }
    }

    protected void outputAttributeExpression (PrintWriter pw, String expression, String filename, int line, String tempVariableName)
    {
        AttributeExpressionParser.ParseResult result = AttributeExpressionParser.parse (expression, filename, line);
        pw.print ("            " + result.className + " " + tempVariableName + " = new " + result.className + "(");

        boolean first = true;
        Iterator iter = result.arguments.iterator ();
        while (iter.hasNext ())
        {
            AttributeExpressionParser.Argument arg = (AttributeExpressionParser.Argument) iter.next ();
            if (arg.field == null)
            {
                if (!first)
                {
                    pw.print (", ");
                }
                first = false;
                pw.print (arg.text);
            }
        }
        pw.println ("  // " + filename + ":" + line);
        pw.println (");");

        iter = result.arguments.iterator ();
        while (iter.hasNext ())
        {
            AttributeExpressionParser.Argument arg = (AttributeExpressionParser.Argument) iter.next ();
            if (arg.field != null)
            {
                String methodName = "set" + arg.field.substring (0, 1).toUpperCase () + arg.field.substring (1);
                pw.println ("            " + tempVariableName + "." + methodName + "(\n" +
                            arg.text + "  // " + filename + ":" + line + "\n" +
                            ");");
            }
        }
    }

    protected boolean elementHasAttributes (JavaField[] fields)
    {
        for (int i = 0; i < fields.length; i++)
        {
            if (tagHasAttributes (fields[i].getTags ()))
            {
                return true;
            }
        }
        return false;
    }

    protected boolean elementHasAttributes (JavaMethod[] methods)
    {
        for (int i = 0; i < methods.length; i++)
        {
            if (tagHasAttributes (methods[i].getTags ()))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Encodes a class name to the internal Java name.
     * For example, an inner class Outer.Inner will be
     * encoded as Outer$Inner.
     */
    private void getTransformedQualifiedName (JavaClass type, StringBuffer sb)
    {
        sb.append (type.getFullyQualifiedName ());
    }

    protected String getParameterTypes (JavaParameter[] parameters)
    {
        StringBuffer sb = new StringBuffer ();
        for (int i = 0; i < parameters.length; i++)
        {
            if (i > 0)
            {
                sb.append (",");
            }

            getTransformedQualifiedName (parameters[i].getType ().getJavaClass (), sb);
            for (int j = 0; j < parameters[i].getType ().getDimensions (); j++)
            {
                sb.append ("[]");
            }
        }
        return sb.toString ();
    }

    protected void generateClass (JavaClass javaClass) throws Exception
    {
        String name = null;
        File sourceFile = null;
        File destFile = null;
        String packageName = null;
        String className = null;

        packageName = javaClass.getPackage ();
        if (packageName == null)
        {
            packageName = "";
        }

        if (javaClass.isInner ())
        {
            sourceFile = getSourceFile (javaClass);

            String nonPackagePrefix = javaClass.getParent ().getClassNamePrefix ();
            if (packageName.length () > 0)
            {
                nonPackagePrefix = nonPackagePrefix.substring (packageName.length () + 1);
            }

            className = nonPackagePrefix + javaClass.getName ();
            name = javaClass.getParent ().getClassNamePrefix () + javaClass.getName ();
        }
        else
        {
            name = javaClass.getFullyQualifiedName ();
            sourceFile = getSourceFile (javaClass);
            className = javaClass.getName ();
        }

        if (sourceFile == null)
        {
            log ("Unable to find source file for: " + name);
        }

        destFile = new File (destDir, name.replace ('.', '/') + "$__attributeRepository.java");

        /*if (javaClass.isAnonymous ()) {
        log (javaClass.getName () + " is anonymous - ignoring.", Project.MSG_VERBOSE);
        numIgnored++;
        return;
        }*/

        if (!hasAttributes (javaClass))
        {
            if (destFile.exists ())
            {
                destFile.delete ();
            }
            return;
        }

        if (destFile.exists () && sourceFile != null && destFile.lastModified () >= sourceFile.lastModified ())
        {
            return;
        }

        numGenerated++;


        destFile.getParentFile ().mkdirs ();
        PrintWriter pw = new PrintWriter (new FileWriter (destFile));
        try
        {
            if (packageName != null && !packageName.equals (""))
            {
                pw.println ("package " + packageName + ";");
            }

            if (sourceFile != null)
            {
                copyImports (sourceFile, pw);
            }

            StringTokenizer tok = new StringTokenizer (attributePackages, ";");
            while (tok.hasMoreTokens ())
            {
                pw.println ("import " + tok.nextToken () + ".*;");
            }

            pw.println ("public class " + className + "$__attributeRepository implements org.apache.commons.attributes.AttributeRepositoryClass {");
            {
                pw.println ("    private final java.util.Set classAttributes = new java.util.HashSet ();");
                pw.println ("    private final java.util.Map fieldAttributes = new java.util.HashMap ();");
                pw.println ("    private final java.util.Map methodAttributes = new java.util.HashMap ();");
                pw.println ("    private final java.util.Map constructorAttributes = new java.util.HashMap ();");
                pw.println ();

                pw.println ("    public " + className + "$__attributeRepository " + "() {");
                pw.println ("        initClassAttributes ();");
                pw.println ("        initMethodAttributes ();");
                pw.println ("        initFieldAttributes ();");
                pw.println ("        initConstructorAttributes ();");
                pw.println ("    }");
                pw.println ();

                pw.println ("    public java.util.Set getClassAttributes () { return classAttributes; }");
                pw.println ("    public java.util.Map getFieldAttributes () { return fieldAttributes; }");
                pw.println ("    public java.util.Map getConstructorAttributes () { return constructorAttributes; }");
                pw.println ("    public java.util.Map getMethodAttributes () { return methodAttributes; }");
                pw.println ();

                pw.println ("    private void initClassAttributes () {");
                addExpressions (javaClass.getTags (), pw, "classAttributes", sourceFile);
                pw.println ("    }");
                pw.println ();

                // ---- Field Attributes

                pw.println ("    private void initFieldAttributes () {");
                pw.println ("        java.util.Set attrs = null;");
                JavaField[] fields = javaClass.getFields ();
                for (int i = 0; i < fields.length; i++)
                {
                    JavaField member = (JavaField) fields[i];
                    if (member.getTags ().length > 0)
                    {
                        String key = member.getName ();

                        pw.println ("        attrs = new java.util.HashSet ();");
                        addExpressions (member.getTags (), pw, "attrs", sourceFile);
                        pw.println ("        fieldAttributes.put (\"" + key + "\", attrs);");
                        pw.println ("        attrs = null;");
                        pw.println ();
                    }
                }
                pw.println ("    }");

                // ---- Method Attributes

                pw.println ("    private void initMethodAttributes () {");
                pw.println ("        java.util.Set attrs = null;");
                pw.println ("        java.util.List bundle = null;");
                JavaMethod[] methods = javaClass.getMethods ();

                for (int i = 0; i < methods.length; i++)
                {
                    JavaMethod member = (JavaMethod) methods[i];
                    if (!member.isConstructor () && member.getTags ().length > 0)
                    {
                        StringBuffer sb = new StringBuffer ();
                        sb.append (member.getName ()).append ("(");
                        sb.append (getParameterTypes (member.getParameters ()));
                        sb.append (")");
                        String key = sb.toString ();

                        pw.println ("        bundle = new java.util.ArrayList ();");
                        pw.println ("        attrs = new java.util.HashSet ();");
                        addExpressions (member.getTags (), null, pw, "attrs", sourceFile);
                        pw.println ("        bundle.add (attrs);");
                        pw.println ("        attrs = null;");

                        pw.println ("        attrs = new java.util.HashSet ();");
                        addExpressions (member.getTags (), "return", pw, "attrs", sourceFile);
                        pw.println ("        bundle.add (attrs);");
                        pw.println ("        attrs = null;");

                        JavaParameter[] parameters = member.getParameters ();
                        for (int j = 0; j < parameters.length; j++)
                        {
                            JavaParameter parameter = (JavaParameter) parameters[j];
                            pw.println ("        attrs = new java.util.HashSet ();");
                            addExpressions (member.getTags (), parameter.getName (), pw, "attrs", sourceFile);
                            pw.println ("        bundle.add (attrs);");
                            pw.println ("        attrs = null;");
                        }

                        pw.println ("        methodAttributes.put (\"" + key + "\", bundle);");
                        pw.println ("        bundle = null;");
                        pw.println ();
                    }
                }
                pw.println ("    }");


                // ---- Constructor Attributes

                pw.println ("    private void initConstructorAttributes () {");
                pw.println ("        java.util.Set attrs = null;");
                pw.println ("        java.util.List bundle = null;");

                JavaMethod[] constructors = javaClass.getMethods ();
                for (int i = 0; i < constructors.length; i++)
                {
                    JavaMethod member = (JavaMethod) constructors[i];
                    if (member.isConstructor () && member.getTags ().length > 0)
                    {
                        StringBuffer sb = new StringBuffer ();
                        sb.append ("(");
                        sb.append (getParameterTypes (member.getParameters ()));
                        sb.append (")");
                        String key = sb.toString ();

                        pw.println ("        bundle = new java.util.ArrayList ();");
                        pw.println ("        attrs = new java.util.HashSet ();");
                        addExpressions (member.getTags (), null, pw, "attrs", sourceFile);
                        pw.println ("        bundle.add (attrs);");
                        pw.println ("        attrs = null;");

                        JavaParameter[] parameters = member.getParameters ();
                        for (int j = 0; j < parameters.length; j++)
                        {
                            JavaParameter parameter = (JavaParameter) parameters[j];
                            pw.println ("        attrs = new java.util.HashSet ();");
                            addExpressions (member.getTags (), parameter.getName (), pw, "attrs", sourceFile);
                            pw.println ("        bundle.add (attrs);");
                            pw.println ("        attrs = null;");
                        }

                        pw.println ("        constructorAttributes.put (\"" + key + "\", bundle);");
                        pw.println ("        bundle = null;");
                        pw.println ();
                    }
                }
                pw.println ("    }");
            }
            pw.println ("}");

            pw.close ();
        }
        catch (Exception e)
        {
            pw.close ();
            destFile.delete ();
            throw e;
        }
    }

    /**
     * Finds the source file of a class.
     *
     * @param qualifiedName the fully qualified class name
     * @return the file the class is defined in.
     * @throws BuildException if the file could not be found.
     */
    protected File getSourceFile (JavaClass javaClass) throws BuildException
    {
        return javaClass.getSource ().getFile ();
    }

    protected boolean hasAttributes (JavaClass javaClass)
    {
        if (tagHasAttributes (javaClass.getTags ()) ||
                elementHasAttributes (javaClass.getFields ()) ||
                elementHasAttributes (javaClass.getMethods ()) )
        {
            return true;
        }
        return false;
    }

    /**
     * Tests if a tag is an attribute. Currently the test is
     * only "check if it is defined with two @-signs".
     */
    protected boolean isAttribute (DocletTag tag)
    {
        return tag.getName ().length () > 0 && tag.getName ().charAt (0) == '@';
    }

    public void execute () throws BuildException
    {
        destDir.mkdirs ();
        numGenerated = 0;
        try
        {
            JavaDocBuilder builder = new JavaDocBuilder ();
            for (int i = 0; i < fileSets.size (); i++)
            {
                FileSet fs = (FileSet) fileSets.get (i);
                DirectoryScanner ds = fs.getDirectoryScanner(project);
                File fromDir = fs.getDir(project);

                String[] srcFiles = ds.getIncludedFiles();

                for (int j = 0; j < srcFiles.length; j++)
                {
                    String srcName = srcFiles[j];

                    File sourceFile = new File (fromDir, srcName);
                    builder.addSource (sourceFile);
                }
            }

            JavaClass[] classes = builder.getClasses ();
            for (int i = 0; i < classes.length; i++)
            {
                generateClassAndInners (classes[i]);
            }

            log ("Generated attribute information for " + numGenerated + " classes. Ignored " + numIgnored + " classes.");
        }
        catch (BuildException be)
        {
            throw be;
        }
        catch (Exception e)
        {
            e.printStackTrace ();
            throw new BuildException (e.toString (), e);
        }
    }

    public void generateClassAndInners (JavaClass clazz) throws Exception
    {
        generateClass (clazz);
        JavaClass[] classes = clazz.getInnerClasses ();
        for (int i = 0; i < classes.length; i++)
        {
            generateClassAndInners (classes[i]);
        }
    }

    /**
     * Checks if a collection of XTags contain any tags specifying attributes.
     */
    protected boolean tagHasAttributes (DocletTag[] tags)
    {
        for (int i = 0; i < tags.length; i++)
        {
            if (isAttribute (tags[i]))
            {
                return true;
            }
        }
        return false;
    }
}
