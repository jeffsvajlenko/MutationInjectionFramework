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
package org.apache.commons.validator;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This contains the results of a set of validation rules processed
 * on a JavaBean.
 *
 * @version $Revision: 1227719 $ $Date: 2012-01-05 18:45:51 +0100 (Thu, 05 Jan 2012) $
 */
public class ValidatorResults implements Serializable
{

    private static final long serialVersionUID = -2709911078904924839L;

    /**
     * Map of validation results.
     */
    protected Map hResults = new HashMap();

    /**
     * Merge another ValidatorResults into mine.
     *
     * @param results ValidatorResults to merge.
     */
    public void merge(ValidatorResults results)
    {
        this.hResults.putAll(results.hResults);
    }

    /**
     * Add a the result of a validator action.
     *
     * @param field The field validated.
     * @param validatorName The name of the validator.
     * @param result The result of the validation.
     */
    public void add(Field field, String validatorName, boolean result)
    {
        this.add(field, validatorName, result, null);
    }

    /**
     * Add a the result of a validator action.
     *
     * @param field The field validated.
     * @param validatorName The name of the validator.
     * @param result The result of the validation.
     * @param value The value returned by the validator.
     */
    public void add(
        Field field,
        String validatorName,
        boolean result,
        Object value)
    {

        ValidatorResult validatorResult = this.getValidatorResult(field.getKey());

        if (validatorResult == null)
        {
            validatorResult = new ValidatorResult(field);
            this.hResults.put(field.getKey(), validatorResult);
        }

        validatorResult.add(validatorName, result, value);
    }

    /**
     * Clear all results recorded by this object.
     */
    public void clear()
    {
        this.hResults.clear();
    }

    /**
     * Return <code>true</code> if there are no messages recorded
     * in this collection, or <code>false</code> otherwise.
     *
     * @return Whether these results are empty.
     */
    public boolean isEmpty()
    {
        return this.hResults.isEmpty();
    }

    /**
     * Gets the <code>ValidatorResult</code> associated
     * with the key passed in.  The key the <code>ValidatorResult</code>
     * is stored under is the <code>Field</code>'s getKey method.
     *
     * @param key The key generated from <code>Field</code> (this is often just
     * the field name).
     *
     * @return The result of a specified key.
     */
    public ValidatorResult getValidatorResult(String key)
    {
        return (ValidatorResult) this.hResults.get(key);
    }

    /**
     * Return the set of property names for which at least one message has
     * been recorded.
     * @return An unmodifiable Set of the property names.
     */
    public Set getPropertyNames()
    {
        return Collections.unmodifiableSet(this.hResults.keySet());
    }

    /**
     * Get a <code>Map</code> of any <code>Object</code>s returned from
     * validation routines.
     *
     * @return Map of objections returned by validators.
     */
    public Map getResultValueMap()
    {
        Map results = new HashMap();

        for (Iterator i = hResults.keySet().iterator(); i.hasNext();)
        {
            String propertyKey = (String) i.next();
            ValidatorResult vr = this.getValidatorResult(propertyKey);

            for (Iterator x = vr.getActions(); x.hasNext();)
            {
                String actionKey = (String)x.next();
                Object result = vr.getResult(actionKey);

                if (result != null && !(result instanceof Boolean))
                {
                    results.put(propertyKey, result);
                }
            }
        }

        return results;
    }

}
