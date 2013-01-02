/*
 * Copyright (c) 1996, 2006, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package java.beans;

import com.sun.beans.editors.*;

/**
 * The PropertyEditorManager can be used to locate a property editor for
 * any given type name.  This property editor must support the
 * java.beans.PropertyEditor interface for editing a given object.
 * <P>
 * The PropertyEditorManager uses three techniques for locating an editor
 * for a given type.  First, it provides a registerEditor method to allow
 * an editor to be specifically registered for a given type.  Second it
 * tries to locate a suitable class by adding "Editor" to the full
 * qualified classname of the given type (e.g. "foo.bah.FozEditor").
 * Finally it takes the simple classname (without the package name) adds
 * "Editor" to it and looks in a search-path of packages for a matching
 * class.
 * <P>
 * So for an input class foo.bah.Fred, the PropertyEditorManager would
 * first look in its tables to see if an editor had been registered for
 * foo.bah.Fred and if so use that.  Then it will look for a
 * foo.bah.FredEditor class.  Then it will look for (say)
 * standardEditorsPackage.FredEditor class.
 * <p>
 * Default PropertyEditors will be provided for the Java primitive types
 * "boolean", "byte", "short", "int", "long", "float", and "double"; and
 * for the classes java.lang.String. java.awt.Color, and java.awt.Font.
 */

public class PropertyEditorManager
{

    /**
     * Register an editor class to be used to edit values of
     * a given target class.
     *
     * <p>First, if there is a security manager, its <code>checkPropertiesAccess</code>
     * method is called. This could result in a SecurityException.
     *
     * @param targetType the Class object of the type to be edited
     * @param editorClass the Class object of the editor class.  If
     *     this is null, then any existing definition will be removed.
     * @exception  SecurityException  if a security manager exists and its
     *             <code>checkPropertiesAccess</code> method doesn't allow setting
     *              of system properties.
     * @see SecurityManager#checkPropertiesAccess
     */

    public static void registerEditor(Class<?> targetType, Class<?> editorClass)
    {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null)
        {
            sm.checkPropertiesAccess();
        }
        initialize();
        if (editorClass == null)
        {
            registry.remove(targetType);
        }
        else
        {
            registry.put(targetType, editorClass);
        }
    }

    /**
     * Locate a value editor for a given target type.
     *
     * @param targetType  The Class object for the type to be edited
     * @return An editor object for the given target class.
     * The result is null if no suitable editor can be found.
     */

    public static synchronized PropertyEditor findEditor(Class<?> targetType)
    {
        initialize();
        Class editorClass = (Class)registry.get(targetType);
        if (editorClass != null)
        {
            try
            {
                Object o = editorClass.newInstance();
                return (PropertyEditor)o;
            }
            catch (Exception ex)
            {
                System.err.println("Couldn't instantiate type editor \"" +
                                   editorClass.getName() + "\" : " + ex);
            }
        }

        // Now try adding "Editor" to the class name.

        String editorName = targetType.getName() + "Editor";
        try
        {
            return (PropertyEditor) Introspector.instantiate(targetType, editorName);
        }
        catch (Exception ex)
        {
            // Silently ignore any errors.
        }

        // Now try looking for <searchPath>.fooEditor
        int index = editorName.lastIndexOf('.') + 1;
        if (index > 0)
        {
            editorName = editorName.substring(index);
        }
        for (String path : searchPath)
        {
            if (path.startsWith(DEFAULT))
                path = path.replace(DEFAULT, DEFAULT_NEW);
            String name = path + '.' + editorName;
            try
            {
                return (PropertyEditor) Introspector.instantiate(targetType, name);
            }
            catch (Exception ex)
            {
                // Silently ignore any errors.
            }
        }

        if (null != targetType.getEnumConstants())
        {
            return new EnumEditor(targetType);
        }
        // We couldn't find a suitable Editor.
        return null;
    }

    /**
     * Gets the package names that will be searched for property editors.
     *
     * @return  The array of package names that will be searched in
     *          order to find property editors.
     * <p>     The default value for this array is implementation-dependent,
     *         e.g. Sun implementation initially sets to  {"com.sun.beans.editors"}.
     */
    public static synchronized String[] getEditorSearchPath()
    {
        // Return a copy of the searchPath.
        String result[] = new String[searchPath.length];
        System.arraycopy(searchPath, 0, result, 0, searchPath.length);
        return result;
    }

    /**
     * Change the list of package names that will be used for
     *          finding property editors.
     *
     * <p>First, if there is a security manager, its <code>checkPropertiesAccess</code>
     * method is called. This could result in a SecurityException.
     *
     * @param path  Array of package names.
     * @exception  SecurityException  if a security manager exists and its
     *             <code>checkPropertiesAccess</code> method doesn't allow setting
     *              of system properties.
     * @see SecurityManager#checkPropertiesAccess
     */

    public static synchronized void setEditorSearchPath(String path[])
    {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null)
        {
            sm.checkPropertiesAccess();
        }
        if (path == null)
        {
            path = new String[0];
        }
        searchPath = path;
    }

    private static synchronized void initialize()
    {
        if (registry != null)
        {
            return;
        }
        registry = new java.util.Hashtable();
        registry.put(Byte.TYPE, ByteEditor.class);
        registry.put(Short.TYPE, ShortEditor.class);
        registry.put(Integer.TYPE, IntegerEditor.class);
        registry.put(Long.TYPE, LongEditor.class);
        registry.put(Boolean.TYPE, BooleanEditor.class);
        registry.put(Float.TYPE, FloatEditor.class);
        registry.put(Double.TYPE, DoubleEditor.class);
    }

    private static final String DEFAULT = "sun.beans.editors";
    private static final String DEFAULT_NEW = "com.sun.beans.editors";
    private static String[] searchPath = { DEFAULT_NEW };
    private static java.util.Hashtable registry;
}
