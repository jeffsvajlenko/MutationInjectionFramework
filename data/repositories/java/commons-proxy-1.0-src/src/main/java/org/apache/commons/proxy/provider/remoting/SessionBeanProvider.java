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

package org.apache.commons.proxy.provider.remoting;

import org.apache.commons.proxy.ObjectProvider;
import org.apache.commons.proxy.ProxyUtils;
import org.apache.commons.proxy.exception.ObjectProviderException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * Provides a reference to a session bean by looking up the home object and calling (via reflection) the no-argument
 * create() method.  This will work for both local and remote session beans.
 *
 * @author James Carman
 * @since 1.0
 */
public class SessionBeanProvider implements ObjectProvider
{
//----------------------------------------------------------------------------------------------------------------------
// Fields
//----------------------------------------------------------------------------------------------------------------------
    private final String jndiName;
    private final Class homeInterface;
    private final Properties properties;

//----------------------------------------------------------------------------------------------------------------------
// Constructors
//----------------------------------------------------------------------------------------------------------------------

    public SessionBeanProvider( String jndiName, Class homeInterface )
    {
        this.jndiName = jndiName;
        this.homeInterface = homeInterface;
        this.properties = null;
    }

    public SessionBeanProvider( String jndiName, Class homeInterface, Properties properties )
    {
        this.jndiName = jndiName;
        this.homeInterface = homeInterface;
        this.properties = properties;
    }

//----------------------------------------------------------------------------------------------------------------------
// ObjectProvider Implementation
//----------------------------------------------------------------------------------------------------------------------

    public Object getObject()
    {
        try
        {
            final InitialContext initialContext = properties == null ? new InitialContext() :
                                                  new InitialContext( properties );
            Object homeObject = PortableRemoteObject.narrow( initialContext.lookup( jndiName ), homeInterface );
            final Method createMethod = homeObject.getClass().getMethod( "create", ProxyUtils.EMPTY_ARGUMENT_TYPES );
            return createMethod.invoke( homeObject, ProxyUtils.EMPTY_ARGUMENTS );
        }
        catch( NoSuchMethodException e )
        {
            throw new ObjectProviderException(
                "Unable to find no-arg create() method on home interface " + homeInterface.getName() + ".", e );
        }
        catch( IllegalAccessException e )
        {
            throw new ObjectProviderException(
                "No-arg create() method on home interface " + homeInterface.getName() + " is not accessible.",
                e ); // Should never happen!
        }
        catch( NamingException e )
        {
            throw new ObjectProviderException( "Unable to lookup EJB home object in JNDI.", e );
        }
        catch( InvocationTargetException e )
        {
            throw new ObjectProviderException(
                "No-arg create() method on home interface " + homeInterface.getName() + " threw an exception.", e );
        }
    }
}

