/*
 * Copyright 1999-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.launcher.types;

import java.util.ArrayList;
import java.util.Stack;
import org.apache.commons.launcher.Launcher;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

/**
 * A class that represents a set of nested elements of
 * {@link ConditionalArgument} objects.
 *
 * @author Patrick Luby
 */
public class ConditionalArgumentSet extends DataType
{

    //------------------------------------------------------------------ Fields

    /**
     * Cached arguments and nested ConditionalArgumentSet objects
     */
    private ArrayList list = new ArrayList();

    //----------------------------------------------------------------- Methods

    /**
     * Add a {@link ConditionalArgument}.
     *
     * @param argument the {@link ConditionalArgument} to be
     *  added
     */
    protected void addConditionalargument(ConditionalArgument argument)
    {

        if (isReference())
            throw noChildrenAllowed();
        list.add(argument);

    }

    /**
     * Add a {@link ConditionalArgumentSet}.
     *
     * @param set the {@link ConditionalArgumentSet} to be added
     */
    protected void addConditionalargumentset(ConditionalArgumentSet set)
    {

        if (isReference())
            throw noChildrenAllowed();
        list.add(set);

    }

    /**
     * Get {@link ConditionalArgument} instances.
     *
     * @return the {@link ConditionalArgument} instances
     */
    public ArrayList getList()
    {

        // Make sure we don't have a circular reference to this instance
        if (!checked)
        {
            Stack stk = new Stack();
            stk.push(this);
            dieOnCircularReference(stk, project);
        }

        // Recursively work through the tree of ConditionalArgumentSet objects
        // and accumulate the list of ConditionalArgument objects.
        ArrayList mergedList = new ArrayList(list.size());
        for (int i = 0; i < list.size(); i++)
        {
            Object o = list.get(i);
            ConditionalArgumentSet nestedSet = null;
            if (o instanceof Reference)
            {
                o = ((Reference)o).getReferencedObject(project);
                // Only references to this class are allowed
                if (!o.getClass().isInstance(this))
                    throw new BuildException(Launcher.getLocalizedString("cannot.reference", this.getClass().getName()));
                nestedSet = (ConditionalArgumentSet)o;
            }
            else if (o.getClass().isInstance(this))
            {
                nestedSet = (ConditionalArgumentSet)o;
            }
            else if (o instanceof ConditionalArgument)
            {
                mergedList.add(o);
            }
            else
            {
                throw new BuildException(Launcher.getLocalizedString("cannot.nest", this.getClass().getName()));
            }
            if (nestedSet != null)
                mergedList.addAll(nestedSet.getList());
        }

        return mergedList;

    }

    /**
     * Makes this instance a reference to another instance. You must not
     * set another attribute or nest elements inside this element if you
     * make it a reference.
     *
     * @param r the reference to another {@link ConditionalArgumentSet}
     *  instance
     */
    public void setRefid(Reference r) throws BuildException
    {

        if (!list.isEmpty())
            throw tooManyAttributes();
        list.add(r);
        super.setRefid(r);

    }

}
