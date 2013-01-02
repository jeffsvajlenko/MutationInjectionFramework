package sun.management.resources;

import java.util.ListResourceBundle;

public final class agent_sv extends ListResourceBundle
{
    protected final Object[][] getContents()
    {
        return new Object[][]
        {
            { "agent.err.access.file.not.readable", "Access-filen \u00E4r inte l\u00E4sbar" },
            { "agent.err.access.file.notfound", "Access-filen hittades inte" },
            { "agent.err.access.file.notset", "Access-filen har inte angetts men com.sun.management.jmxremote.authenticate=true" },
            { "agent.err.access.file.read.failed", "Det g\u00E5r inte att l\u00E4sa access-filen" },
            { "agent.err.acl.file.access.notrestricted", "L\u00E4sbeh\u00F6righeten f\u00F6r filen m\u00E5ste begr\u00E4nsas" },
            { "agent.err.acl.file.not.readable", "SNMP ACL-filen \u00E4r inte l\u00E4sbar" },
            { "agent.err.acl.file.notfound", "SNMP ACL-filen hittades inte" },
            { "agent.err.acl.file.notset", "Ingen SNMP ACL-fil har angetts, men com.sun.management.snmp.acl=true" },
            { "agent.err.acl.file.read.failed", "Det g\u00E5r inte att l\u00E4sa filen SNMP ACL" },
            { "agent.err.agentclass.access.denied", "\u00C5tkomst till premain(String) nekad" },
            { "agent.err.agentclass.failed", "Administrationsagentklassen misslyckades " },
            { "agent.err.agentclass.notfound", "Administrationsagentklassen hittades inte" },
            { "agent.err.configfile.access.denied", "\u00C5tkomst till konfigurationsfilen nekad" },
            { "agent.err.configfile.closed.failed", "Det g\u00E5r inte att st\u00E4nga konfigurationsfilen" },
            { "agent.err.configfile.failed", "Det g\u00E5r inte att l\u00E4sa konfigurationsfilen" },
            { "agent.err.configfile.notfound", "Konfigurationsfilen hittades inte" },
            { "agent.err.connector.server.io.error", "Serverkommunikationsfel f\u00F6r JMX-anslutning" },
            { "agent.err.error", "Fel" },
            { "agent.err.exception", "Agenten orsakade ett undantag " },
            { "agent.err.exportaddress.failed", "Det g\u00E5r inte att exportera JMX-anslutningsadressen till instrumentbufferten" },
            { "agent.err.invalid.agentclass", "Ogiltigt egenskapsv\u00E4rde f\u00F6r com.sun.management.agent.class" },
            { "agent.err.invalid.jmxremote.port", "Ogiltigt com.sun.management.jmxremote.port-nummer" },
            { "agent.err.invalid.option", "Det angivna alternativet \u00E4r ogiltigt" },
            { "agent.err.invalid.snmp.port", "Ogiltigt com.sun.management.snmp.port-nummer" },
            { "agent.err.invalid.snmp.trap.port", "Ogiltigt com.sun.management.snmp.trap-nummer" },
            { "agent.err.password.file.access.notrestricted", "L\u00E4sbeh\u00F6righeten f\u00F6r filen m\u00E5ste begr\u00E4nsas" },
            { "agent.err.password.file.not.readable", "L\u00F6senordsfilen \u00E4r inte l\u00E4sbar" },
            { "agent.err.password.file.notfound", "Det g\u00E5r inte att hitta l\u00F6senordsfilen" },
            { "agent.err.password.file.notset", "L\u00F6senordsfilen har inte angetts men com.sun.management.jmxremote.authenticate=true" },
            { "agent.err.password.file.read.failed", "Det g\u00E5r inte att l\u00E4sa l\u00F6senordsfilen" },
            { "agent.err.premain.notfound", "premain(String) finns inte i agentklassen" },
            { "agent.err.snmp.adaptor.start.failed", "Det g\u00E5r inte att starta SNMP-adaptern med adressen" },
            { "agent.err.snmp.mib.init.failed", "Det g\u00E5r inte att initiera SNMP MIB med felet" },
            { "agent.err.unknown.snmp.interface", "Ok\u00E4nt SNMP-gr\u00E4nssnitt" },
            { "agent.err.warning", "Varning!" },
            { "jmxremote.AdaptorBootstrap.getTargetList.adding", "M\u00E5l l\u00E4ggs till: {0}" },
            { "jmxremote.AdaptorBootstrap.getTargetList.initialize1", "Adaptern klar." },
            { "jmxremote.AdaptorBootstrap.getTargetList.initialize2", "SNMP-adaptern redo p\u00E5: {0}:{1}" },
            { "jmxremote.AdaptorBootstrap.getTargetList.processing", "ACL bearbetas" },
            { "jmxremote.AdaptorBootstrap.getTargetList.starting", "Adapterservern startas:" },
            { "jmxremote.AdaptorBootstrap.getTargetList.terminate", "avsluta {0}" },
            { "jmxremote.ConnectorBootstrap.initialize", "Startar JMX Connector-servern:" },
            { "jmxremote.ConnectorBootstrap.initialize.noAuthentication", "Ingen autentisering" },
            { "jmxremote.ConnectorBootstrap.initialize.password.readonly", "L\u00E4sbeh\u00F6righeten f\u00F6r l\u00F6senordsfilen m\u00E5ste begr\u00E4nsas: {0}" },
            { "jmxremote.ConnectorBootstrap.initialize.ready", "JMX Connector redo p\u00E5: {0}" },
        };
    }
}
