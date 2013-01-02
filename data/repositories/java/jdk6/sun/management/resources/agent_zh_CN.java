package sun.management.resources;

import java.util.ListResourceBundle;

public final class agent_zh_CN extends ListResourceBundle
{
    protected final Object[][] getContents()
    {
        return new Object[][]
        {
            { "agent.err.access.file.not.readable", "\u65E0\u6CD5\u8BFB\u53D6\u8BBF\u95EE\u6587\u4EF6" },
            { "agent.err.access.file.notfound", "\u627E\u4E0D\u5230\u8BBF\u95EE\u6587\u4EF6" },
            { "agent.err.access.file.notset", "\u672A\u6307\u5B9A\u8BBF\u95EE\u6587\u4EF6\uFF0C\u4F46 com.sun.management.jmxremote.authenticate=true" },
            { "agent.err.access.file.read.failed", "\u8BFB\u53D6\u8BBF\u95EE\u6587\u4EF6\u5931\u8D25" },
            { "agent.err.acl.file.access.notrestricted", "\u5FC5\u987B\u9650\u5236\u53E3\u4EE4\u6587\u4EF6\u8BFB\u53D6\u8BBF\u95EE" },
            { "agent.err.acl.file.not.readable", "\u65E0\u6CD5\u8BFB\u53D6 SNMP ACL \u6587\u4EF6" },
            { "agent.err.acl.file.notfound", "\u627E\u4E0D\u5230 SNMP ACL \u6587\u4EF6" },
            { "agent.err.acl.file.notset", "\u672A\u6307\u5B9A SNMP ACL \u6587\u4EF6\uFF0C\u4F46 com.sun.management.snmp.acl=true" },
            { "agent.err.acl.file.read.failed", "\u8BFB\u53D6 SNMP ACL \u6587\u4EF6\u5931\u8D25" },
            { "agent.err.agentclass.access.denied", "\u62D2\u7EDD\u8BBF\u95EE premain(String)" },
            { "agent.err.agentclass.failed", "\u7BA1\u7406\u4EE3\u7406\u7C7B\u5931\u8D25 " },
            { "agent.err.agentclass.notfound", "\u627E\u4E0D\u5230\u7BA1\u7406\u4EE3\u7406\u7C7B" },
            { "agent.err.configfile.access.denied", "\u62D2\u7EDD\u8BBF\u95EE\u914D\u7F6E\u6587\u4EF6" },
            { "agent.err.configfile.closed.failed", "\u5173\u95ED\u914D\u7F6E\u6587\u4EF6\u5931\u8D25" },
            { "agent.err.configfile.failed", "\u8BFB\u53D6\u914D\u7F6E\u6587\u4EF6\u5931\u8D25" },
            { "agent.err.configfile.notfound", "\u627E\u4E0D\u5230\u914D\u7F6E\u6587\u4EF6" },
            { "agent.err.connector.server.io.error", "JMX \u8FDE\u63A5\u5668\u670D\u52A1\u5668\u901A\u4FE1\u9519\u8BEF" },
            { "agent.err.error", "\u9519\u8BEF" },
            { "agent.err.exception", "\u4EE3\u7406\u629B\u51FA\u5F02\u5E38 " },
            { "agent.err.exportaddress.failed", "\u5C06 JMX \u8FDE\u63A5\u5668\u5730\u5740\u5BFC\u51FA\u5230\u6D4B\u8BD5\u8BBE\u5907\u7F13\u51B2\u533A\u5931\u8D25" },
            { "agent.err.invalid.agentclass", "com.sun.management.agent.class \u5C5E\u6027\u503C\u65E0\u6548" },
            { "agent.err.invalid.jmxremote.port", "com.sun.management.jmxremote.port \u7F16\u53F7\u65E0\u6548" },
            { "agent.err.invalid.option", "\u6307\u5B9A\u7684\u9009\u9879\u65E0\u6548" },
            { "agent.err.invalid.snmp.port", "com.sun.management.snmp.port \u7F16\u53F7\u65E0\u6548" },
            { "agent.err.invalid.snmp.trap.port", "com.sun.management.snmp.trap \u7F16\u53F7\u65E0\u6548" },
            { "agent.err.password.file.access.notrestricted", "\u5FC5\u987B\u9650\u5236\u53E3\u4EE4\u6587\u4EF6\u8BFB\u53D6\u8BBF\u95EE" },
            { "agent.err.password.file.not.readable", "\u65E0\u6CD5\u8BFB\u53D6\u53E3\u4EE4\u6587\u4EF6" },
            { "agent.err.password.file.notfound", "\u627E\u4E0D\u5230\u53E3\u4EE4\u6587\u4EF6" },
            { "agent.err.password.file.notset", "\u672A\u6307\u5B9A\u53E3\u4EE4\u6587\u4EF6\uFF0C\u4F46 com.sun.management.jmxremote.authenticate=true" },
            { "agent.err.password.file.read.failed", "\u8BFB\u53D6\u53E3\u4EE4\u6587\u4EF6\u5931\u8D25" },
            { "agent.err.premain.notfound", "\u4EE3\u7406\u7C7B\u4E2D\u4E0D\u5B58\u5728 premain(String)" },
            { "agent.err.snmp.adaptor.start.failed", "\u65E0\u6CD5\u542F\u52A8\u5E26\u6709\u5730\u5740\u7684 SNMP \u9002\u914D\u5668" },
            { "agent.err.snmp.mib.init.failed", "\u65E0\u6CD5\u521D\u59CB\u5316\u5E26\u6709\u9519\u8BEF\u7684 SNMP MIB" },
            { "agent.err.unknown.snmp.interface", "\u672A\u77E5 SNMP \u63A5\u53E3" },
            { "agent.err.warning", "\u8B66\u544A" },
            { "jmxremote.AdaptorBootstrap.getTargetList.adding", "\u6B63\u5728\u6DFB\u52A0\u76EE\u6807\uFF1A{0}" },
            { "jmxremote.AdaptorBootstrap.getTargetList.initialize1", "\u9002\u914D\u5668\u5C31\u7EEA\u3002" },
            { "jmxremote.AdaptorBootstrap.getTargetList.initialize2", "\u4F4D\u4E8E {0}:{1} \u7684 SNMP \u9002\u914D\u5668\u5C31\u7EEA" },
            { "jmxremote.AdaptorBootstrap.getTargetList.processing", "\u6B63\u5728\u5904\u7406 ACL" },
            { "jmxremote.AdaptorBootstrap.getTargetList.starting", "\u6B63\u5728\u542F\u52A8\u9002\u914D\u5668\u670D\u52A1\u5668\uFF1A" },
            { "jmxremote.AdaptorBootstrap.getTargetList.terminate", "\u7EC8\u6B62 {0}" },
            { "jmxremote.ConnectorBootstrap.initialize", "\u6B63\u5728\u542F\u52A8 JMX \u8FDE\u63A5\u5668\u670D\u52A1\u5668\uFF1A" },
            { "jmxremote.ConnectorBootstrap.initialize.noAuthentication", "\u65E0\u9A8C\u8BC1" },
            { "jmxremote.ConnectorBootstrap.initialize.password.readonly", "\u5FC5\u987B\u9650\u5236\u53E3\u4EE4\u6587\u4EF6\u8BFB\u53D6\u8BBF\u95EE\uFF1A{0}" },
            { "jmxremote.ConnectorBootstrap.initialize.ready", "\u4F4D\u4E8E {0} \u7684 JMX \u8FDE\u63A5\u5668\u5C31\u7EEA" },
        };
    }
}
