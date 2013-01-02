// This file is an automatically generated file, please do not edit this file, modify the WrapperGenerator.java file instead !

package sun.awt.X11;

import sun.misc.*;

import java.util.logging.*;
public class XModifierKeymap extends XWrapperBase
{
    private Unsafe unsafe = XlibWrapper.unsafe;
    private final boolean should_free_memory;
    public static int getSize()
    {
        return 8;
    }
    public int getDataSize()
    {
        return getSize();
    }

    long pData;

    public long getPData()
    {
        return pData;
    }


    public XModifierKeymap(long addr)
    {
        log.finest("Creating");
        pData=addr;
        should_free_memory = false;
    }


    public XModifierKeymap()
    {
        log.finest("Creating");
        pData = unsafe.allocateMemory(getSize());
        should_free_memory = true;
    }


    public void dispose()
    {
        log.finest("Disposing");
        if (should_free_memory)
        {
            log.finest("freeing memory");
            unsafe.freeMemory(pData);
        }
    }
    public int get_max_keypermod()
    {
        log.finest("");
        return (Native.getInt(pData+0));
    }
    public void set_max_keypermod(int v)
    {
        log.finest("");
        Native.putInt(pData+0, v);
    }
    public long get_modifiermap(int index)
    {
        log.finest("");
        return Native.getLong(pData+4)+index*Native.getLongSize();
    }
    public long get_modifiermap()
    {
        log.finest("");
        return Native.getLong(pData+4);
    }
    public void set_modifiermap(long v)
    {
        log.finest("");
        Native.putLong(pData + 4, v);
    }


    String getName()
    {
        return "XModifierKeymap";
    }


    String getFieldsAsString()
    {
        String ret="";

        ret += ""+"max_keypermod = " + get_max_keypermod() +", ";
        ret += ""+"modifiermap = " + get_modifiermap() +", ";
        return ret;
    }


}


