package sun.management.resources;

import java.util.ListResourceBundle;

public final class agent_it extends ListResourceBundle
{
    protected final Object[][] getContents()
    {
        return new Object[][]
        {
            { "agent.err.access.file.not.readable", "File di accesso non leggibile" },
            { "agent.err.access.file.notfound", "File di accesso non trovato" },
            { "agent.err.access.file.notset", "Il file di accesso non \u00E8 specificato ma com.sun.management.jmxremote.authenticate=true" },
            { "agent.err.access.file.read.failed", "Errore di lettura file di accesso" },
            { "agent.err.acl.file.access.notrestricted", "Limitare l'accesso in lettura al file password" },
            { "agent.err.acl.file.not.readable", "File SNMP ACL non leggibile" },
            { "agent.err.acl.file.notfound", "File SNMP ACL non trovato" },
            { "agent.err.acl.file.notset", "Nessun file SNMP ACL specificato ma com.sun.management.snmp.acl=true" },
            { "agent.err.acl.file.read.failed", "Errore di lettura file SNMP ACL" },
            { "agent.err.agentclass.access.denied", "Accesso negato a premain(String)" },
            { "agent.err.agentclass.failed", "Errore classe agente gestione " },
            { "agent.err.agentclass.notfound", "Classe agente gestione non trovata" },
            { "agent.err.configfile.access.denied", "Accesso negato al file di configurazione" },
            { "agent.err.configfile.closed.failed", "Errore di chiusura file di configurazione" },
            { "agent.err.configfile.failed", "Errore di lettura file di configurazione" },
            { "agent.err.configfile.notfound", "File di configurazione non trovato" },
            { "agent.err.connector.server.io.error", "Errore di comunicazione server del connettore JMX" },
            { "agent.err.error", "Errore" },
            { "agent.err.exception", "Eccezione dell'agente " },
            { "agent.err.exportaddress.failed", "Errore di esportazione dell'indirizzo connettore JMX nel buffer strumenti" },
            { "agent.err.invalid.agentclass", "Valore propriet\u00E0 com.sun.management.agent.class non valido" },
            { "agent.err.invalid.jmxremote.port", "Numero com.sun.management.jmxremote.port non valido" },
            { "agent.err.invalid.option", "Specificata opzione non valida" },
            { "agent.err.invalid.snmp.port", "Numero com.sun.management.snmp.port non valido" },
            { "agent.err.invalid.snmp.trap.port", "Numero com.sun.management.snmp.trap non valido" },
            { "agent.err.password.file.access.notrestricted", "Limitare l'accesso in lettura al file password" },
            { "agent.err.password.file.not.readable", "File password non leggibile" },
            { "agent.err.password.file.notfound", "File password non trovato" },
            { "agent.err.password.file.notset", "Il file password non \u00E8 specificato ma com.sun.management.jmxremote.authenticate=true" },
            { "agent.err.password.file.read.failed", "Errore di lettura file password" },
            { "agent.err.premain.notfound", "premain(String) non esiste nella classe agente" },
            { "agent.err.snmp.adaptor.start.failed", "Impossibile avviare l'adattatore SNMP con indirizzo" },
            { "agent.err.snmp.mib.init.failed", "Impossibile inizializzare MIB SNMP, errore" },
            { "agent.err.unknown.snmp.interface", "Interfaccia SNMP sconosciuta" },
            { "agent.err.warning", "Avviso" },
            { "jmxremote.AdaptorBootstrap.getTargetList.adding", "Aggiunta della destinazione: {0}" },
            { "jmxremote.AdaptorBootstrap.getTargetList.initialize1", "Adattatore pronto." },
            { "jmxremote.AdaptorBootstrap.getTargetList.initialize2", "Adattatore SNMP pronto in: {0}:{1}" },
            { "jmxremote.AdaptorBootstrap.getTargetList.processing", "Elaborazione ACL" },
            { "jmxremote.AdaptorBootstrap.getTargetList.starting", "Avvio del server adattatore:" },
            { "jmxremote.AdaptorBootstrap.getTargetList.terminate", "interrompere {0}" },
            { "jmxremote.ConnectorBootstrap.initialize", "Avvio del server connettore JMX:" },
            { "jmxremote.ConnectorBootstrap.initialize.noAuthentication", "Nessuna autenticazione" },
            { "jmxremote.ConnectorBootstrap.initialize.password.readonly", "Limitare l''accesso in lettura al file password: {0}" },
            { "jmxremote.ConnectorBootstrap.initialize.ready", "Connettore JMX pronto in: {0}" },
        };
    }
}
