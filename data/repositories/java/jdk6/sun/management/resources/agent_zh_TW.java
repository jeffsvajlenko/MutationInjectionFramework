package sun.management.resources;

import java.util.ListResourceBundle;

public final class agent_zh_TW extends ListResourceBundle
{
    protected final Object[][] getContents()
    {
        return new Object[][]
        {
            { "agent.err.access.file.not.readable", "\u5B58\u53D6\u6A94\u6848\u4E0D\u53EF\u8B80" },
            { "agent.err.access.file.notfound", "\u627E\u4E0D\u5230\u5B58\u53D6\u6A94\u6848" },
            { "agent.err.access.file.notset", "\u672A\u6307\u5B9A\u5B58\u53D6\u6A94\u6848\uFF0C\u4F46 com.sun.management.jmxremote.authenticate=true" },
            { "agent.err.access.file.read.failed", "\u7121\u6CD5\u8B80\u53D6\u5B58\u53D6\u6A94\u6848" },
            { "agent.err.acl.file.access.notrestricted", "\u5FC5\u9808\u9650\u5236\u5BC6\u78BC\u6A94\u6848\u8B80\u53D6\u5B58\u53D6" },
            { "agent.err.acl.file.not.readable", "SNMP ACL \u6A94\u6848\u4E0D\u53EF\u8B80" },
            { "agent.err.acl.file.notfound", "\u627E\u4E0D\u5230 SNMP ACL \u6A94\u6848" },
            { "agent.err.acl.file.notset", "\u672A\u6307\u5B9A SNMP ACL \u6A94\u6848\uFF0C\u4F46 com.sun.management.snmp.acl=true" },
            { "agent.err.acl.file.read.failed", "\u7121\u6CD5\u8B80\u53D6 SNMP ACL \u6A94\u6848" },
            { "agent.err.agentclass.access.denied", "\u5B58\u53D6 premain(String) \u906D\u5230\u62D2\u7D55" },
            { "agent.err.agentclass.failed", "\u7BA1\u7406\u4EE3\u7406\u985E\u5225\u5931\u6557 " },
            { "agent.err.agentclass.notfound", "\u627E\u4E0D\u5230\u7BA1\u7406\u4EE3\u7406\u7A0B\u5F0F\u985E\u5225" },
            { "agent.err.configfile.access.denied", "\u5B58\u53D6\u914D\u7F6E\u6A94\u6848\u906D\u5230\u62D2\u7D55" },
            { "agent.err.configfile.closed.failed", "\u7121\u6CD5\u95DC\u9589\u914D\u7F6E\u6A94\u6848" },
            { "agent.err.configfile.failed", "\u7121\u6CD5\u8B80\u53D6\u914D\u7F6E\u6A94\u6848" },
            { "agent.err.configfile.notfound", "\u627E\u4E0D\u5230\u914D\u7F6E\u6A94\u6848" },
            { "agent.err.connector.server.io.error", "JMX \u9023\u63A5\u5668\u4F3A\u670D\u5668\u901A\u8A0A\u932F\u8AA4" },
            { "agent.err.error", "\u932F\u8AA4" },
            { "agent.err.exception", "\u4EE3\u7406\u7A0B\u5F0F\u4E1F\u51FA\u7570\u5E38 " },
            { "agent.err.exportaddress.failed", "\u5C07 JMX \u9023\u63A5\u5668\u4F4D\u5740\u532F\u51FA\u81F3\u8A2D\u5099\u7DE9\u885D\u5340\u5931\u6557" },
            { "agent.err.invalid.agentclass", "com.sun.management.agent.class \u7279\u6027\u503C\u7121\u6548" },
            { "agent.err.invalid.jmxremote.port", "com.sun.management.jmxremote.port \u7DE8\u865F\u7121\u6548" },
            { "agent.err.invalid.option", "\u6307\u5B9A\u7684\u9078\u9805\u7121\u6548" },
            { "agent.err.invalid.snmp.port", "com.sun.management.snmp.port \u7DE8\u865F\u7121\u6548" },
            { "agent.err.invalid.snmp.trap.port", "com.sun.management.snmp.trap \u7DE8\u865F\u7121\u6548" },
            { "agent.err.password.file.access.notrestricted", "\u5FC5\u9808\u9650\u5236\u5BC6\u78BC\u6A94\u6848\u8B80\u53D6\u5B58\u53D6" },
            { "agent.err.password.file.not.readable", "\u5BC6\u78BC\u6A94\u6848\u4E0D\u53EF\u8B80" },
            { "agent.err.password.file.notfound", "\u627E\u4E0D\u5230\u5BC6\u78BC\u6A94\u6848" },
            { "agent.err.password.file.notset", "\u672A\u6307\u5B9A\u5BC6\u78BC\u6A94\u6848\uFF0C\u4F46 com.sun.management.jmxremote.authenticate=true" },
            { "agent.err.password.file.read.failed", "\u7121\u6CD5\u8B80\u53D6\u5BC6\u78BC\u6A94\u6848" },
            { "agent.err.premain.notfound", "\u4EE3\u7406\u7A0B\u5F0F\u985E\u5225\u4E2D\u4E0D\u5B58\u5728 premain(String)" },
            { "agent.err.snmp.adaptor.start.failed", "\u7121\u6CD5\u4F7F\u7528\u4F4D\u5740\u555F\u52D5 SNMP \u914D\u63A5\u5361" },
            { "agent.err.snmp.mib.init.failed", "\u7121\u6CD5\u521D\u59CB\u5316 SNMP MIB\uFF0C\u51FA\u73FE\u932F\u8AA4" },
            { "agent.err.unknown.snmp.interface", "\u4E0D\u660E\u7684 SNMP \u4ECB\u9762" },
            { "agent.err.warning", "\u8B66\u544A" },
            { "jmxremote.AdaptorBootstrap.getTargetList.adding", "\u6B63\u5728\u589E\u52A0\u76EE\u6A19\uFE30{0}" },
            { "jmxremote.AdaptorBootstrap.getTargetList.initialize1", "\u914D\u63A5\u5361\u5C31\u7DD2\u3002" },
            { "jmxremote.AdaptorBootstrap.getTargetList.initialize2", "SNMP \u914D\u63A5\u5361\u5C31\u7DD2\uFF0C\u4F4D\u65BC\uFE30{0}:{1}" },
            { "jmxremote.AdaptorBootstrap.getTargetList.processing", "\u6B63\u5728\u8655\u7406 ACL" },
            { "jmxremote.AdaptorBootstrap.getTargetList.starting", "\u6B63\u5728\u555F\u52D5\u914D\u63A5\u5361\u4F3A\u670D\u5668\uFE30" },
            { "jmxremote.AdaptorBootstrap.getTargetList.terminate", "\u7D42\u6B62 {0}" },
            { "jmxremote.ConnectorBootstrap.initialize", "\u6B63\u5728\u555F\u52D5 JMX \u9023\u63A5\u5668\u4F3A\u670D\u5668\uFE30" },
            { "jmxremote.ConnectorBootstrap.initialize.noAuthentication", "\u7121\u8A8D\u8B49" },
            { "jmxremote.ConnectorBootstrap.initialize.password.readonly", "\u5FC5\u9808\u9650\u5236\u5BC6\u78BC\u6A94\u6848\u8B80\u53D6\u5B58\u53D6\uFE30{0}" },
            { "jmxremote.ConnectorBootstrap.initialize.ready", "JMX \u9023\u63A5\u5668\u5C31\u7DD2\uFF0C\u4F4D\u65BC\uFE30{0}" },
        };
    }
}
