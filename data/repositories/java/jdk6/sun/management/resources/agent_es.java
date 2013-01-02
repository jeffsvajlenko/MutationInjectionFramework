package sun.management.resources;

import java.util.ListResourceBundle;

public final class agent_es extends ListResourceBundle
{
    protected final Object[][] getContents()
    {
        return new Object[][]
        {
            { "agent.err.access.file.not.readable", "No se puede leer el archivo de acceso" },
            { "agent.err.access.file.notfound", "Archivo de acceso no encontrado" },
            { "agent.err.access.file.notset", "El archivo de acceso no se ha especificado, pero com.sun.management.jmxremote.authenticate=true" },
            { "agent.err.access.file.read.failed", "Error al leer el archivo de acceso" },
            { "agent.err.acl.file.access.notrestricted", "Se debe restringir el acceso de lectura al archivo de contrase\u00F1as" },
            { "agent.err.acl.file.not.readable", "No se puede leer el archivo ACL de SNMP" },
            { "agent.err.acl.file.notfound", "Archivo ACL de SNMP no encontrado" },
            { "agent.err.acl.file.notset", "No se ha especificado ning\u00FAn archivo ACL de SNMP, pero com.sun.management.snmp.acl=true" },
            { "agent.err.acl.file.read.failed", "Error al leer el archivo ACL de SNMP" },
            { "agent.err.agentclass.access.denied", "Acceso denegado a premain(String)" },
            { "agent.err.agentclass.failed", "Error de clase de agente de administraci\u00F3n " },
            { "agent.err.agentclass.notfound", "Clase de agente de administraci\u00F3n no encontrada" },
            { "agent.err.configfile.access.denied", "Acceso denegado al archivo de configuraci\u00F3n" },
            { "agent.err.configfile.closed.failed", "Error al cerrar el archivo de configuraci\u00F3n" },
            { "agent.err.configfile.failed", "Error al leer el archivo de configuraci\u00F3n" },
            { "agent.err.configfile.notfound", "No se ha encontrado el archivo de configuraci\u00F3n" },
            { "agent.err.connector.server.io.error", "Error de comunicaci\u00F3n con el servidor de conector JMX" },
            { "agent.err.error", "Error" },
            { "agent.err.exception", "Excepci\u00F3n generada por el agente " },
            { "agent.err.exportaddress.failed", "Error de exportaci\u00F3n de la direcci\u00F3n del conector JMX al b\u00FAfer de instrumentaci\u00F3n" },
            { "agent.err.invalid.agentclass", "Valor de propiedad com.sun.management.agent.class no v\u00E1lido" },
            { "agent.err.invalid.jmxremote.port", "N\u00FAmero com.sun.management.jmxremote.port no v\u00E1lido" },
            { "agent.err.invalid.option", "Opci\u00F3n especificada no v\u00E1lida" },
            { "agent.err.invalid.snmp.port", "N\u00FAmero com.sun.management.snmp.port no v\u00E1lido" },
            { "agent.err.invalid.snmp.trap.port", "N\u00FAmero com.sun.management.snmp.trap no v\u00E1lido" },
            { "agent.err.password.file.access.notrestricted", "Se debe restringir el acceso de lectura al archivo de contrase\u00F1as" },
            { "agent.err.password.file.not.readable", "No se puede leer el archivo de contrase\u00F1as" },
            { "agent.err.password.file.notfound", "Archivo de contrase\u00F1as no encontrado" },
            { "agent.err.password.file.notset", "El archivo de contrase\u00F1as no se ha especificado, pero com.sun.management.jmxremote.authenticate=true" },
            { "agent.err.password.file.read.failed", "Error al leer el archivo de contrase\u00F1as" },
            { "agent.err.premain.notfound", "premain(String) no existe en la clase del agente" },
            { "agent.err.snmp.adaptor.start.failed", "No se ha podido iniciar el adaptador de SNMP con la direcci\u00F3n" },
            { "agent.err.snmp.mib.init.failed", "No se ha podido inicializar el MIB de SNMP con error" },
            { "agent.err.unknown.snmp.interface", "Interfaz SNMP desconocido" },
            { "agent.err.warning", "Advertencia" },
            { "jmxremote.AdaptorBootstrap.getTargetList.adding", "Agregando destino: {0}" },
            { "jmxremote.AdaptorBootstrap.getTargetList.initialize1", "Adaptador listo." },
            { "jmxremote.AdaptorBootstrap.getTargetList.initialize2", "Adaptador SNMP listo en: {0}:{1}" },
            { "jmxremote.AdaptorBootstrap.getTargetList.processing", "Procesando ACL" },
            { "jmxremote.AdaptorBootstrap.getTargetList.starting", "Iniciar servidor adaptador:" },
            { "jmxremote.AdaptorBootstrap.getTargetList.terminate", "finalizar {0}" },
            { "jmxremote.ConnectorBootstrap.initialize", "Iniciando servidor de conector JMX:" },
            { "jmxremote.ConnectorBootstrap.initialize.noAuthentication", "Sin autenticaci\u00F3n" },
            { "jmxremote.ConnectorBootstrap.initialize.password.readonly", "Se debe restringir el acceso de lectura al archivo de contrase\u00F1as: {0}" },
            { "jmxremote.ConnectorBootstrap.initialize.ready", "Conector JMX listo en: {0}" },
        };
    }
}
