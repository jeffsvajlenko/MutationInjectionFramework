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

package org.apache.commons.betwixt.introspection;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;


/** <p>Bean info for exmaple bean used in introspection.</p>
  *
  * @author Robert Burrell Donkin
  * @version $Revision: 438373 $
  */
public class BeanWithBeanInfoBeanBeanInfo extends SimpleBeanInfo
{

    public PropertyDescriptor[] getPropertyDescriptors()
    {
        PropertyDescriptor[] descriptors = new PropertyDescriptor[2];
        try
        {

            descriptors[0]
                = new PropertyDescriptor("alpha", BeanWithBeanInfoBean.class, "getAlpha", "setAlpha");
            descriptors[1]
                = new PropertyDescriptor("gamma", BeanWithBeanInfoBean.class, "gammaGetter", "gammaSetter");

        }
        catch (IntrospectionException e)
        {
            throw new RuntimeException(e.getMessage());
        }
        return descriptors;
    }
}

