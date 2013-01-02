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


/** <p>An example of a bean that has a BeanInfo for use with introspection.</p>
  *
  * <p>
  * Three different pseudo-properties:
  * <ul>
  * <li><strong>Alpha</strong> is a standard property.
  * <li><strong>Beta</strong> follows standard naming conventions but should be ignored.
  * <li><strong>Gamma</strong> doesn't follow standard naming conventions
  * </ul>
  * </p>
  *
  * @author Robert Burrell Donkin
  * @version $Revision: 438373 $
  */
public class BeanWithBeanInfoBean
{

    private String alpha;
    private String beta;
    private String gamma;

    public BeanWithBeanInfoBean() {}

    public BeanWithBeanInfoBean(String alpha, String beta, String gamma)
    {
        setAlpha(alpha);
        setBeta(beta);
        gammaSetter(gamma);
    }

    public String getAlpha()
    {
        return alpha;
    }

    public void setAlpha(String alpha)
    {
        this.alpha = alpha;
    }

    public String getBeta()
    {
        return beta;
    }

    public void setBeta(String beta)
    {
        this.beta = beta;
    }

    public String gammaGetter()
    {
        return gamma;
    }

    public void gammaSetter(String gamma)
    {
        this.gamma = gamma;
    }
}

