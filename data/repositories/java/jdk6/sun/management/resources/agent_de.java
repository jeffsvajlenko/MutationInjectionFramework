package sun.management.resources;

import java.util.ListResourceBundle;

public final class agent_de extends ListResourceBundle
{
    protected final Object[][] getContents()
    {
        return new Object[][]
        {
            { "agent.err.access.file.not.readable", "Zugriffsdatei kann nicht gelesen werden." },
            { "agent.err.access.file.notfound", "Zugriffsdatei konnte nicht gefunden werden." },
            { "agent.err.access.file.notset", "Es wurde keine Zugriffsdatei angegeben, obwohl com.sun.management.jmxremote.authenticate auf \"true\" gesetzt ist." },
            { "agent.err.access.file.read.failed", "Zugriffsdatei konnte nicht gelesen werden." },
            { "agent.err.acl.file.access.notrestricted", "Lesezugriff auf Passwortdatei muss eingeschr\u00E4nkt sein." },
            { "agent.err.acl.file.not.readable", "SNMP-ACL-Datei kann nicht gelesen werden." },
            { "agent.err.acl.file.notfound", "SNMP-ACL-Datei konnte nicht gefunden werden." },
            { "agent.err.acl.file.notset", "Es wurde keine SNMP-ACL-Datei angegeben, obwohl com.sun.management.snmp.acl auf \"true\" gesetzt ist." },
            { "agent.err.acl.file.read.failed", "SNMP-ACL-Datei konnte nicht gelesen werden." },
            { "agent.err.agentclass.access.denied", "Zugriff auf premain(String) wurde verweigert." },
            { "agent.err.agentclass.failed", "Verwaltungsagentenklasse fehlgeschlagen " },
            { "agent.err.agentclass.notfound", "Verwaltungsagentenklasse nicht gefunden" },
            { "agent.err.configfile.access.denied", "Zugriff auf Konfigurationsdatei wurde verweigert." },
            { "agent.err.configfile.closed.failed", "Konfigurationsdatei konnte nicht geschlossen werden." },
            { "agent.err.configfile.failed", "Konfigurationsdatei konnte nicht gelesen werden." },
            { "agent.err.configfile.notfound", "Konfigurationsdatei wurde nicht gefunden." },
            { "agent.err.connector.server.io.error", "Fehler bei der JMX-Anschlussserver-Kommunikation" },
            { "agent.err.error", "Fehler" },
            { "agent.err.exception", "Agent-Ausnahmefehler " },
            { "agent.err.exportaddress.failed", "Export der JMX-Anschlussadresse an Instrumentierungspuffer schlug fehl." },
            { "agent.err.invalid.agentclass", "Ung\u00FCltiger Eigenschaftswert f\u00FCr com.sun.management.agent.class" },
            { "agent.err.invalid.jmxremote.port", "Ung\u00FCltige Nummer f\u00FCr com.sun.management.jmxremote.port" },
            { "agent.err.invalid.option", "Ung\u00FCltige Option angegeben" },
            { "agent.err.invalid.snmp.port", "Ung\u00FCltige Nummer f\u00FCr com.sun.management.snmp.port" },
            { "agent.err.invalid.snmp.trap.port", "Ung\u00FCltige Nummer f\u00FCr com.sun.management.snmp.trap" },
            { "agent.err.password.file.access.notrestricted", "Lesezugriff auf Passwortdatei muss eingeschr\u00E4nkt sein." },
            { "agent.err.password.file.not.readable", "Passwortdatei kann nicht gelesen werden." },
            { "agent.err.password.file.notfound", "Passwortdatei konnte nicht gefunden werden." },
            { "agent.err.password.file.notset", "Es wurde keine Passwortdatei angegeben, obwohl com.sun.management.jmxremote.authenticate auf \"true\" gesetzt ist." },
            { "agent.err.password.file.read.failed", "Passwortdatei konnte nicht gelesen werden." },
            { "agent.err.premain.notfound", "premain(String) ist in Agentenklasse nicht vorhanden." },
            { "agent.err.snmp.adaptor.start.failed", "Fehler beim Starten des SNMP-Adapters mit Adresse" },
            { "agent.err.snmp.mib.init.failed", "Initialisierung von SNMP-MIB fehlgeschlagen mit Fehler" },
            { "agent.err.unknown.snmp.interface", "Unbekannte SNMP-Schnittstelle" },
            { "agent.err.warning", "Warnung" },
            { "jmxremote.AdaptorBootstrap.getTargetList.adding", "Ziel hinzuf\u00FCgen: {0}" },
            { "jmxremote.AdaptorBootstrap.getTargetList.initialize1", "Adapter bereit." },
            { "jmxremote.AdaptorBootstrap.getTargetList.initialize2", "SNMP-Adapter bereit unter: {0}:{1}" },
            { "jmxremote.AdaptorBootstrap.getTargetList.processing", "ACL verarbeiten" },
            { "jmxremote.AdaptorBootstrap.getTargetList.starting", "Adapterserver starten:" },
            { "jmxremote.AdaptorBootstrap.getTargetList.terminate", "{0} beenden" },
            { "jmxremote.ConnectorBootstrap.initialize", "JMX-Anschlussserver starten:" },
            { "jmxremote.ConnectorBootstrap.initialize.noAuthentication", "Keine Authentifizierung" },
            { "jmxremote.ConnectorBootstrap.initialize.password.readonly", "Lesezugriff auf Passwortdatei muss eingeschr\u00E4nkt sein. {0}" },
            { "jmxremote.ConnectorBootstrap.initialize.ready", "JMX-Anschluss bereit unter: {0}" },
        };
    }
}
