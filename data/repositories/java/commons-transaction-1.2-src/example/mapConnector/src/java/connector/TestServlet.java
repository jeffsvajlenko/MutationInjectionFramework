/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package connector;

import java.io.*;
import java.util.Map;

import javax.resource.ResourceException;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import javax.naming.InitialContext;
import javax.naming.Context;

import org.apache.commons.transaction.memory.jca.*;

/**
 * Implementation of the test servlet.
 *
 * @version $Id: TestServlet.java 493628 2007-01-07 01:42:48Z joerg $
 */
public class TestServlet extends HttpServlet
{
    // Reference to the factory
    private MapConnectionFactory _factory;

    /**
     * <code>init()</code> stores the factory for efficiency since JNDI
     * is relatively slow.
     */
    public void init() throws ServletException
    {
        try
        {
            Context ic = new InitialContext();

            _factory = (MapConnectionFactory) ic.lookup("java:comp/env/Map");
        }
        catch (Exception e)
        {
            throw new ServletException(e);
        }
    }

    /**
     * Use the connection.  All JCA connections must use the following
     * pattern to ensure the connection is closed even when exceptions
     * occur.
     */
    public void service(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
    {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        MapConnection conn1 = null;
        MapConnection conn2 = null;

        UserTransaction tx = null;
        try
        {
            Context ic = new InitialContext();
            tx = (UserTransaction) ic.lookup("java:comp/UserTransaction");

            tx.begin();

            System.out.println("Tx: " + tx);
            out.println("Tx: " + tx + "<br>");

            System.out.println("Factory: " + _factory);
            out.println("Factory: " + _factory + "<br>");

            conn1 = (MapConnection) _factory.getConnection(new MapConnectionSpec("map1"));
            conn2 = (MapConnection) _factory.getConnection(new MapConnectionSpec("map2"));
            out.println("Connection1: " + conn1 + "<br>");
            System.out.println("Connection1: " + conn1);
            out.println("Connection2: " + conn2 + "<br>");
            System.out.println("Connection2: " + conn2);

            Map map1 = conn1.getMap();
            Map map2 = conn2.getMap();
            out.println("Map1: " + map1 + "<br>");
            System.out.println("Map1: " + map1);
            out.println("Map2: " + map2 + "<br>");
            System.out.println("Map2: " + map2);

            map1.put("Olli", "Molli");
            map1.remove("Berti");

            map2.put("Walter", "Alter");
            map2.put("Gundel", "Flunder");
            map2.remove("Hertha");

            tx.commit();
        }
        catch (Exception e)
        {
            if (tx != null)
                try
                {
                    tx.rollback();
                }
                catch (IllegalStateException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                catch (SecurityException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                catch (SystemException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            System.out.println(e);
            e.printStackTrace();
            throw new ServletException(e);
        }
        finally
        {
            if (conn1 != null)
                try
                {
                    conn1.close();
                }
                catch (ResourceException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            if (conn2 != null)
                try
                {
                    conn2.close();
                }
                catch (ResourceException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
        }
    }
}
