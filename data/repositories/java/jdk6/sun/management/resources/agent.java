package sun.management.resources;

import java.util.ListResourceBundle;

public final class agent extends ListResourceBundle
{
    protected final Object[][] getContents()
    {
        return new Object[][]
        {
            { "agent.err.access.file.not.readable", "Access file not readable" },
            { "agent.err.access.file.notfound", "Access file not found" },
            { "agent.err.access.file.notset", "Access file is not specified but com.sun.management.jmxremote.authenticate=true" },
            { "agent.err.access.file.read.failed", "Failed in reading the access file" },
            { "agent.err.acl.file.access.notrestricted", "Password file read access must be restricted" },
            { "agent.err.acl.file.not.readable", "SNMP ACL file not readable" },
            { "agent.err.acl.file.notfound", "SNMP ACL file not found" },
            { "agent.err.acl.file.notset", "No SNMP ACL file is specified but com.sun.management.snmp.acl=true" },
            { "agent.err.acl.file.read.failed", "Failed in reading SNMP ACL file" },
            { "agent.err.agentclass.access.denied", "Access to premain(String) is denied" },
            { "agent.err.agentclass.failed", "Management agent class failed " },
            { "agent.err.agentclass.notfound", "Management agent class not found" },
            { "agent.err.configfile.access.denied", "Access to the config file is denied" },
            { "agent.err.configfile.closed.failed", "Failed in closing the config file" },
            { "agent.err.configfile.failed", "Failed in reading the config file" },
            { "agent.err.configfile.notfound", "Config file not found" },
            { "agent.err.connector.server.io.error", "JMX connector server communication error" },
            { "agent.err.error", "Error" },
            { "agent.err.exception", "Exception thrown by the agent " },
            { "agent.err.exportaddress.failed", "Export of JMX connector address to instrumentation buffer failed" },
            { "agent.err.file.access.not.restricted", "File read access must be restricted" },
            { "agent.err.file.not.found", "File not found" },
            { "agent.err.file.not.readable", "File not readable" },
            { "agent.err.file.not.set", "File not specified" },
            { "agent.err.file.read.failed", "Failed in reading the file" },
            { "agent.err.invalid.agentclass", "Invalid com.sun.management.agent.class property value" },
            { "agent.err.invalid.jmxremote.port", "Invalid com.sun.management.jmxremote.port number" },
            { "agent.err.invalid.option", "Invalid option specified" },
            { "agent.err.invalid.snmp.port", "Invalid com.sun.management.snmp.port number" },
            { "agent.err.invalid.snmp.trap.port", "Invalid com.sun.management.snmp.trap number" },
            { "agent.err.password.file.access.notrestricted", "Password file read access must be restricted" },
            { "agent.err.password.file.not.readable", "Password file not readable" },
            { "agent.err.password.file.notfound", "Password file not found" },
            { "agent.err.password.file.notset", "Password file is not specified but com.sun.management.jmxremote.authenticate=true" },
            { "agent.err.password.file.read.failed", "Failed in reading the password file" },
            { "agent.err.premain.notfound", "premain(String) does not exist in agent class" },
            { "agent.err.snmp.adaptor.start.failed", "Failed to start SNMP adaptor with address" },
            { "agent.err.snmp.mib.init.failed", "Failed to initialize SNMP MIB with error" },
            { "agent.err.unknown.snmp.interface", "Unknown SNMP interface" },
            { "agent.err.warning", "Warning" },
            { "jmxremote.AdaptorBootstrap.getTargetList.adding", "Adding target: {0}" },
            { "jmxremote.AdaptorBootstrap.getTargetList.initialize1", "Adaptor ready." },
            { "jmxremote.AdaptorBootstrap.getTargetList.initialize2", "SNMP Adaptor ready on: {0}:{1}" },
            { "jmxremote.AdaptorBootstrap.getTargetList.processing", "Processing ACL" },
            { "jmxremote.AdaptorBootstrap.getTargetList.starting", "Starting Adaptor Server:" },
            { "jmxremote.AdaptorBootstrap.getTargetList.terminate", "terminate {0}" },
            { "jmxremote.ConnectorBootstrap.initialize", "Starting JMX Connector Server:" },
            { "jmxremote.ConnectorBootstrap.initialize.file.readonly", "File read access must be restricted: {0}" },
            { "jmxremote.ConnectorBootstrap.initialize.noAuthentication", "No Authentication" },
            { "jmxremote.ConnectorBootstrap.initialize.password.readonly", "Password file read access must be restricted: {0}" },
            { "jmxremote.ConnectorBootstrap.initialize.ready", "JMX Connector ready at: {0}" },
        };
    }
}
