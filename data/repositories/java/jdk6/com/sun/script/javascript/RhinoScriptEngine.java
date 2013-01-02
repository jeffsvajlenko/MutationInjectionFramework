/*
 * Copyright (c) 2005, 2012, Oracle and/or its affiliates. All rights reserved.
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

package com.sun.script.javascript;
import com.sun.script.util.*;
import javax.script.*;
import sun.org.mozilla.javascript.*;
import java.lang.reflect.Method;
import java.io.*;
import java.security.*;
import java.util.*;


/**
 * Implementation of <code>ScriptEngine</code> using the Mozilla Rhino
 * interpreter.
 *
 * @author Mike Grogan
 * @author A. Sundararajan
 * @since 1.6
 */
public final class RhinoScriptEngine extends AbstractScriptEngine
    implements  Invocable, Compilable
{

    private static final boolean DEBUG = false;

    private AccessControlContext accCtxt;

    /* Scope where standard JavaScript objects and our
     * extensions to it are stored. Note that these are not
     * user defined engine level global variables. These are
     * variables have to be there on all compliant ECMAScript
     * scopes. We put these standard objects in this top level.
     */
    private RhinoTopLevel topLevel;

    /* map used to store indexed properties in engine scope
     * refer to comment on 'indexedProps' in ExternalScriptable.java.
     */
    private Map<Object, Object> indexedProps;

    private ScriptEngineFactory factory;
    private InterfaceImplementor implementor;

    static
    {
        ContextFactory.initGlobal(new ContextFactory()
        {
            /**
             * Create new Context instance to be associated with the current thread.
             */
            @Override
            protected Context makeContext()
            {
                Context cx = super.makeContext();
                cx.setClassShutter(RhinoClassShutter.getInstance());
                cx.setWrapFactory(RhinoWrapFactory.getInstance());
                return cx;
            }


            /**
             * Execute top call to script or function. When the runtime is about to
             * execute a script or function that will create the first stack frame
             * with scriptable code, it calls this method to perform the real call.
             * In this way execution of any script happens inside this function.
             */
            @Override
            protected Object doTopCall(final Callable callable,
                                       final Context cx, final Scriptable scope,
                                       final Scriptable thisObj, final Object[] args)
            {
                AccessControlContext accCtxt = null;
                Scriptable global = ScriptableObject.getTopLevelScope(scope);
                Scriptable globalProto = global.getPrototype();
                if (globalProto instanceof RhinoTopLevel)
                {
                    accCtxt = ((RhinoTopLevel)globalProto).getAccessContext();
                }

                if (accCtxt != null)
                {
                    return AccessController.doPrivileged(new PrivilegedAction<Object>()
                    {
                        public Object run()
                        {
                            return superDoTopCall(callable, cx, scope, thisObj, args);
                        }
                    }, accCtxt);
                }
                else
                {
                    return superDoTopCall(callable, cx, scope, thisObj, args);
                }
            }

            private  Object superDoTopCall(Callable callable,
                                           Context cx, Scriptable scope,
                                           Scriptable thisObj, Object[] args)
            {
                return super.doTopCall(callable, cx, scope, thisObj, args);
            }

            public boolean hasFeature(Context cx, int feature)
            {
                // we do not support E4X (ECMAScript for XML)!
                if (feature == Context.FEATURE_E4X)
                {
                    return false;
                }
                else
                {
                    return super.hasFeature(cx, feature);
                }
            }
        });
    }


    /**
     * Creates a new instance of RhinoScriptEngine
     */
    public RhinoScriptEngine()
    {

        if (System.getSecurityManager() != null)
        {
            try
            {
                AccessController.checkPermission(new AllPermission());
            }
            catch (AccessControlException ace)
            {
                accCtxt = AccessController.getContext();
            }
        }

        Context cx = enterContext();
        try
        {
            topLevel = new RhinoTopLevel(cx, this);
        }
        finally
        {
            cx.exit();
        }

        indexedProps = new HashMap<Object, Object>();

        //construct object used to implement getInterface
        implementor = new InterfaceImplementor(this)
        {
            protected Object convertResult(Method method, Object res)
            throws ScriptException
            {
                Class desiredType = method.getReturnType();
                if (desiredType == Void.TYPE)
                {
                    return null;
                }
                else
                {
                    return Context.jsToJava(res, desiredType);
                }
            }
        };
    }

    public Object eval(Reader reader, ScriptContext ctxt)
    throws ScriptException
    {
        Object ret;

        Context cx = enterContext();
        try
        {
            Scriptable scope = getRuntimeScope(ctxt);
            String filename = (String) get(ScriptEngine.FILENAME);
            filename = filename == null ? "<Unknown source>" : filename;

            ret = cx.evaluateReader(scope, reader, filename , 1,  null);
        }
        catch (RhinoException re)
        {
            if (DEBUG) re.printStackTrace();
            int line = (line = re.lineNumber()) == 0 ? -1 : line;
            String msg;
            if (re instanceof JavaScriptException)
            {
                msg = String.valueOf(((JavaScriptException)re).getValue());
            }
            else
            {
                msg = re.toString();
            }
            ScriptException se = new ScriptException(msg, re.sourceName(), line);
            se.initCause(re);
            throw se;
        }
        catch (IOException ee)
        {
            throw new ScriptException(ee);
        }
        finally
        {
            cx.exit();
        }

        return unwrapReturnValue(ret);
    }

    public Object eval(String script, ScriptContext ctxt) throws ScriptException
    {
        if (script == null)
        {
            throw new NullPointerException("null script");
        }
        return eval(new StringReader(script) , ctxt);
    }

    public ScriptEngineFactory getFactory()
    {
        if (factory != null)
        {
            return factory;
        }
        else
        {
            return new RhinoScriptEngineFactory();
        }
    }

    public Bindings createBindings()
    {
        return new SimpleBindings();
    }

    //Invocable methods
    public Object invokeFunction(String name, Object... args)
    throws ScriptException, NoSuchMethodException
    {
        return invoke(null, name, args);
    }

    public Object invokeMethod(Object thiz, String name, Object... args)
    throws ScriptException, NoSuchMethodException
    {
        if (thiz == null)
        {
            throw new IllegalArgumentException("script object can not be null");
        }
        return invoke(thiz, name, args);
    }

    private Object invoke(Object thiz, String name, Object... args)
    throws ScriptException, NoSuchMethodException
    {
        Context cx = enterContext();
        try
        {
            if (name == null)
            {
                throw new NullPointerException("method name is null");
            }

            if (thiz != null && !(thiz instanceof Scriptable))
            {
                thiz = cx.toObject(thiz, topLevel);
            }

            Scriptable engineScope = getRuntimeScope(context);
            Scriptable localScope = (thiz != null)? (Scriptable) thiz :
                                    engineScope;
            Object obj = ScriptableObject.getProperty(localScope, name);
            if (! (obj instanceof Function))
            {
                throw new NoSuchMethodException("no such method: " + name);
            }

            Function func = (Function) obj;
            Scriptable scope = func.getParentScope();
            if (scope == null)
            {
                scope = engineScope;
            }
            Object result = func.call(cx, scope, localScope,
                                      wrapArguments(args));
            return unwrapReturnValue(result);
        }
        catch (RhinoException re)
        {
            if (DEBUG) re.printStackTrace();
            int line = (line = re.lineNumber()) == 0 ? -1 : line;
            throw new ScriptException(re.toString(), re.sourceName(), line);
        }
        finally
        {
            cx.exit();
        }
    }

    public <T> T getInterface(Class<T> clasz)
    {
        try
        {
            return implementor.getInterface(null, clasz);
        }
        catch (ScriptException e)
        {
            return null;
        }
    }

    public <T> T getInterface(Object thiz, Class<T> clasz)
    {
        if (thiz == null)
        {
            throw new IllegalArgumentException("script object can not be null");
        }

        try
        {
            return implementor.getInterface(thiz, clasz);
        }
        catch (ScriptException e)
        {
            return null;
        }
    }

    private static final String printSource =
        "function print(str, newline) {                \n" +
        "    if (typeof(str) == 'undefined') {         \n" +
        "        str = 'undefined';                    \n" +
        "    } else if (str == null) {                 \n" +
        "        str = 'null';                         \n" +
        "    }                                         \n" +
        "    var out = context.getWriter();            \n" +
        "    out.print(String(str));                   \n" +
        "    if (newline) out.print('\\n');            \n" +
        "    out.flush();                              \n" +
        "}\n" +
        "function println(str) {                       \n" +
        "    print(str, true);                         \n" +
        "}";

    Scriptable getRuntimeScope(ScriptContext ctxt)
    {
        if (ctxt == null)
        {
            throw new NullPointerException("null script context");
        }

        // we create a scope for the given ScriptContext
        Scriptable newScope = new ExternalScriptable(ctxt, indexedProps);

        // Set the prototype of newScope to be 'topLevel' so that
        // JavaScript standard objects are visible from the scope.
        newScope.setPrototype(topLevel);

        // define "context" variable in the new scope
        newScope.put("context", newScope, ctxt);

        // define "print", "println" functions in the new scope
        Context cx = enterContext();
        try
        {
            cx.evaluateString(newScope, printSource, "print", 1, null);
        }
        finally
        {
            cx.exit();
        }
        return newScope;
    }


    //Compilable methods
    public CompiledScript compile(String script) throws ScriptException
    {
        return compile(new StringReader(script));
    }

    public CompiledScript compile(java.io.Reader script) throws ScriptException
    {
        CompiledScript ret = null;
        Context cx = enterContext();

        try
        {
            String fileName = (String) get(ScriptEngine.FILENAME);
            if (fileName == null)
            {
                fileName = "<Unknown Source>";
            }

            Scriptable scope = getRuntimeScope(context);
            Script scr = cx.compileReader(scope, script, fileName, 1, null);
            ret = new RhinoCompiledScript(this, scr);
        }
        catch (Exception e)
        {
            if (DEBUG) e.printStackTrace();
            throw new ScriptException(e);
        }
        finally
        {
            cx.exit();
        }
        return ret;
    }


    //package-private helpers

    static Context enterContext()
    {
        // call this always so that initializer of this class runs
        // and initializes custom wrap factory and class shutter.
        return Context.enter();
    }

    void setEngineFactory(ScriptEngineFactory fac)
    {
        factory = fac;
    }

    AccessControlContext getAccessContext()
    {
        return accCtxt;
    }

    Object[] wrapArguments(Object[] args)
    {
        if (args == null)
        {
            return Context.emptyArgs;
        }
        Object[] res = new Object[args.length];
        for (int i = 0; i < res.length; i++)
        {
            res[i] = Context.javaToJS(args[i], topLevel);
        }
        return res;
    }

    Object unwrapReturnValue(Object result)
    {
        if (result instanceof Wrapper)
        {
            result = ( (Wrapper) result).unwrap();
        }

        return result instanceof Undefined ? null : result;
    }

    public static void main(String[] args) throws Exception
    {
        if (args.length == 0)
        {
            System.out.println("No file specified");
            return;
        }

        InputStreamReader r = new InputStreamReader(new FileInputStream(args[0]));
        ScriptEngine engine = new RhinoScriptEngine();

        engine.put("x", "y");
        engine.put(ScriptEngine.FILENAME, args[0]);
        engine.eval(r);
        System.out.println(engine.get("x"));
    }
}
