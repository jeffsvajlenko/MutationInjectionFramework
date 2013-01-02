package sun.management.resources;

import java.util.ListResourceBundle;

public final class agent_fr extends ListResourceBundle
{
    protected final Object[][] getContents()
    {
        return new Object[][]
        {
            { "agent.err.access.file.not.readable", "Fichier d'acc\u00E8s illisible" },
            { "agent.err.access.file.notfound", "Fichier d'acc\u00E8s introuvable" },
            { "agent.err.access.file.notset", "Le fichier d'acc\u00E8s n'est pas sp\u00E9cifi\u00E9 mais com.sun.management.jmxremote.authenticate=true" },
            { "agent.err.access.file.read.failed", "Impossible de lire le fichier d'acc\u00E8s" },
            { "agent.err.acl.file.access.notrestricted", "L'acc\u00E8s \u00E0 la lecture du fichier de mots de passe doit \u00EAtre limit\u00E9" },
            { "agent.err.acl.file.not.readable", "Fichier SNMP ACL illisible" },
            { "agent.err.acl.file.notfound", "Fichier SNMP ACL introuvable" },
            { "agent.err.acl.file.notset", "Aucun fichier SNMP ACL n'est sp\u00E9cifi\u00E9 mais com.sun.management.snmp.acl=true" },
            { "agent.err.acl.file.read.failed", "Impossible de lire le fichier SNMP ACL" },
            { "agent.err.agentclass.access.denied", "Acc\u00E8s \u00E0 premain(String) refus\u00E9" },
            { "agent.err.agentclass.failed", "Une erreur s'est produite au niveau de la classe d'agents de gestion " },
            { "agent.err.agentclass.notfound", "Classe d'agents de gestion introuvable" },
            { "agent.err.configfile.access.denied", "Acc\u00E8s au fichier de configuration refus\u00E9" },
            { "agent.err.configfile.closed.failed", "Impossible de fermer le fichier de configuration" },
            { "agent.err.configfile.failed", "Impossible de lire le fichier de configuration" },
            { "agent.err.configfile.notfound", "Fichier de configuration introuvable" },
            { "agent.err.connector.server.io.error", "Erreur de communication avec le serveur du connecteur JMX" },
            { "agent.err.error", "Erreur" },
            { "agent.err.exception", "Exception envoy\u00E9e par l'agent " },
            { "agent.err.exportaddress.failed", "Impossible d'exporter l'adresse du connecteur JMX dans le tampon d'instrumentation" },
            { "agent.err.invalid.agentclass", "Valeur de propri\u00E9t\u00E9 com.sun.management.agent.class incorrecte" },
            { "agent.err.invalid.jmxremote.port", "Num\u00E9ro com.sun.management.jmxremote.port incorrect" },
            { "agent.err.invalid.option", "Option sp\u00E9cifi\u00E9e non valide" },
            { "agent.err.invalid.snmp.port", "Num\u00E9ro com.sun.management.snmp.port incorrect" },
            { "agent.err.invalid.snmp.trap.port", "Num\u00E9ro com.sun.management.snmp.trap incorrect" },
            { "agent.err.password.file.access.notrestricted", "L'acc\u00E8s \u00E0 la lecture du fichier de mots de passe doit \u00EAtre limit\u00E9" },
            { "agent.err.password.file.not.readable", "Fichier de mots de passe illisible" },
            { "agent.err.password.file.notfound", "Fichier de mots de passe introuvable" },
            { "agent.err.password.file.notset", "Le fichier de mots de passe n'est pas sp\u00E9cifi\u00E9 mais com.sun.management.jmxremote.authenticate=true" },
            { "agent.err.password.file.read.failed", "Impossible de lire le fichier de mots de passe" },
            { "agent.err.premain.notfound", "premain(String) n'existe pas dans la classe d'agents" },
            { "agent.err.snmp.adaptor.start.failed", "Impossible de d\u00E9marrer l'adaptateur SNMP avec l'adresse" },
            { "agent.err.snmp.mib.init.failed", "Impossible d'initialiser SNMP MIB avec l'erreur" },
            { "agent.err.unknown.snmp.interface", "Interface SNMP inconnue" },
            { "agent.err.warning", "Avertissement" },
            { "jmxremote.AdaptorBootstrap.getTargetList.adding", "Ajout de la cible : {0}" },
            { "jmxremote.AdaptorBootstrap.getTargetList.initialize1", "Adaptateur pr\u00EAt." },
            { "jmxremote.AdaptorBootstrap.getTargetList.initialize2", "Adaptateur SNMP pr\u00EAt sur : {0}:{1}" },
            { "jmxremote.AdaptorBootstrap.getTargetList.processing", "Traitement d'ACL" },
            { "jmxremote.AdaptorBootstrap.getTargetList.starting", "D\u00E9marrage du serveur de l'adaptateur :" },
            { "jmxremote.AdaptorBootstrap.getTargetList.terminate", "terminer {0}" },
            { "jmxremote.ConnectorBootstrap.initialize", "D\u00E9marrage du serveur du connecteur JMX :" },
            { "jmxremote.ConnectorBootstrap.initialize.noAuthentication", "Pas d'authentification" },
            { "jmxremote.ConnectorBootstrap.initialize.password.readonly", "L''acc\u00E8s \u00E0 la lecture du fichier de mots de passe doit \u00EAtre limit\u00E9 : {0}" },
            { "jmxremote.ConnectorBootstrap.initialize.ready", "Connecteur JMX pr\u00EAt \u00E0 : {0}" },
        };
    }
}
