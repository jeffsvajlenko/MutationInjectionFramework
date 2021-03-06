package com.sun.corba.se.PortableActivationIDL;


/**
* com/sun/corba/se/PortableActivationIDL/ServerNotActive.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ../../../../src/share/classes/com/sun/corba/se/PortableActivationIDL/activation.idl
* Wednesday, October 24, 2012 4:15:46 PM UTC
*/

public final class ServerNotActive extends org.omg.CORBA.UserException
{
    public String serverId = null;

    public ServerNotActive ()
    {
        super(ServerNotActiveHelper.id());
    } // ctor

    public ServerNotActive (String _serverId)
    {
        super(ServerNotActiveHelper.id());
        serverId = _serverId;
    } // ctor


    public ServerNotActive (String $reason, String _serverId)
    {
        super(ServerNotActiveHelper.id() + "  " + $reason);
        serverId = _serverId;
    } // ctor

} // class ServerNotActive
