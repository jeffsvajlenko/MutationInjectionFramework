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
package org.apache.commons.math3.analysis;

import org.apache.commons.math3.util.FastMath;

/**
 * Auxillary class for testing solvers.
 *
 * The function is extraordinarily well behaved around zero roots: it
 * has an inflection point there (second order derivative is zero),
 * which means linear approximation (Regula Falsi) will converge
 * quadratically.
 *
 * @version $Id: SinFunction.java 1244107 2012-02-14 16:17:55Z erans $
 */
public class SinFunction implements DifferentiableUnivariateFunction
{

    /* Evaluate sinus fuction.
     * @see org.apache.commons.math3.UnivariateFunction#value(double)
     */
    public double value(double x)
    {
        return FastMath.sin(x);
    }

    /* First derivative of sinus function
     */
    public UnivariateFunction derivative()
    {
        return new UnivariateFunction()
        {
            public double value(double x)
            {
                return FastMath.cos(x);
            }
        };
    }

}
