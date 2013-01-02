package sun.tools.jar.resources;

import java.util.ListResourceBundle;

public final class jar_zh_CN extends ListResourceBundle
{
    protected final Object[][] getContents()
    {
        return new Object[][]
        {
            { "error.bad.cflag", "'c' \u6807\u5FD7\u8981\u6C42\u6307\u5B9A\u6E05\u5355\u6216\u8F93\u5165\u6587\u4EF6\uFF01" },
            { "error.bad.eflag", "\u4E0D\u80FD\u540C\u65F6\u6307\u5B9A 'e' \u6807\u5FD7\u548C\u5177\u6709 'Main-Class' \u5C5E\u6027\u7684\n\u6E05\u5355\uFF01" },
            { "error.bad.option", "\u5FC5\u987B\u6307\u5B9A {ctxu} \u4E2D\u7684\u4EFB\u4E00\u9009\u9879\u3002" },
            { "error.bad.uflag", "'u' \u6807\u5FD7\u8981\u6C42\u6307\u5B9A\u6E05\u5355\u3001'e' \u6807\u5FD7\u6216\u8F93\u5165\u6587\u4EF6\uFF01" },
            { "error.cant.open", "\u4E0D\u80FD\u6253\u5F00\uFF1A{0} " },
            { "error.create.dir", "\u4E0D\u80FD\u521B\u5EFA\u76EE\u5F55\uFF1A{0}" },
            { "error.illegal.option", "\u975E\u6CD5\u9009\u9879\uFF1A{0}" },
            { "error.incorrect.length", "\u5904\u7406\u65F6\u9047\u5230\u4E0D\u6B63\u786E\u7684\u957F\u5EA6\uFF1A{0}" },
            { "error.nosuch.fileordir", "\u6CA1\u6709\u8FD9\u4E2A\u6587\u4EF6\u6216\u76EE\u5F55\uFF1A{0}" },
            { "error.write.file", "\u5199\u5B58\u5728\u7684jar\u6587\u4EF6\u65F6\u9519\u8BEF" },
            { "out.added.manifest", "\u6807\u660E\u6E05\u5355(manifest)" },
            { "out.adding", "\u589E\u52A0\uFF1A{0}" },
            { "out.create", "  \u521B\u5EFA\uFF1A{0}" },
            { "out.deflated", "(\u538B\u7F29\u4E86 {0}%)" },
            { "out.extracted", "\u5C55\u5F00\uFF1A{0}" },
            { "out.ignore.entry", "\u5FFD\u7565\u9879 {0}" },
            { "out.inflated", "  \u89E3\u538B {0}" },
            { "out.size", "(\u8BFB\u5165= {0}) (\u5199\u51FA= {1})" },
            { "out.stored", "(\u5B58\u50A8\u4E86 0%)" },
            { "out.update.manifest", "\u66F4\u65B0\u6E05\u5355(manifest)" },
            { "usage", "\u7528\u6CD5: jar {ctxui}[vfm0Me] [jar-file] [manifest-file] [entry-point] [-C dir] files ...\n\u9009\u9879\u5305\u62EC\uFF1A\n    -c  \u521B\u5EFA\u65B0\u7684\u5F52\u6863\u6587\u4EF6\n    -t  \u5217\u51FA\u5F52\u6863\u76EE\u5F55\n    -x  \u89E3\u538B\u7F29\u5DF2\u5F52\u6863\u7684\u6307\u5B9A\uFF08\u6216\u6240\u6709\uFF09\u6587\u4EF6\n    -u  \u66F4\u65B0\u73B0\u6709\u7684\u5F52\u6863\u6587\u4EF6\n    -v  \u5728\u6807\u51C6\u8F93\u51FA\u4E2D\u751F\u6210\u8BE6\u7EC6\u8F93\u51FA\n    -f  \u6307\u5B9A\u5F52\u6863\u6587\u4EF6\u540D\n    -m  \u5305\u542B\u6307\u5B9A\u6E05\u5355\u6587\u4EF6\u4E2D\u7684\u6E05\u5355\u4FE1\u606F\n    -e  \u4E3A\u6346\u7ED1\u5230\u53EF\u6267\u884C jar \u6587\u4EF6\u7684\u72EC\u7ACB\u5E94\u7528\u7A0B\u5E8F\n        \u6307\u5B9A\u5E94\u7528\u7A0B\u5E8F\u5165\u53E3\u70B9\n    -0  \u4EC5\u5B58\u50A8\uFF1B\u4E0D\u4F7F\u7528\u4EFB\u4F55 ZIP \u538B\u7F29\n    -M  \u4E0D\u521B\u5EFA\u6761\u76EE\u7684\u6E05\u5355\u6587\u4EF6\n    -i  \u4E3A\u6307\u5B9A\u7684 jar \u6587\u4EF6\u751F\u6210\u7D22\u5F15\u4FE1\u606F\n    -C  \u66F4\u6539\u4E3A\u6307\u5B9A\u7684\u76EE\u5F55\u5E76\u5305\u542B\u5176\u4E2D\u7684\u6587\u4EF6\n\u5982\u679C\u6709\u4EFB\u4F55\u76EE\u5F55\u6587\u4EF6\uFF0C\u5219\u5BF9\u5176\u8FDB\u884C\u9012\u5F52\u5904\u7406\u3002\n\u6E05\u5355\u6587\u4EF6\u540D\u3001\u5F52\u6863\u6587\u4EF6\u540D\u548C\u5165\u53E3\u70B9\u540D\u7684\u6307\u5B9A\u987A\u5E8F\n\u4E0E \"m\"\u3001\"f\" \u548C \"e\" \u6807\u5FD7\u7684\u6307\u5B9A\u987A\u5E8F\u76F8\u540C\u3002\n\n\u793A\u4F8B 1\uFF1A\u5C06\u4E24\u4E2A\u7C7B\u6587\u4EF6\u5F52\u6863\u5230\u4E00\u4E2A\u540D\u4E3A classes.jar \u7684\u5F52\u6863\u6587\u4EF6\u4E2D\uFF1A\n       jar cvf classes.jar Foo.class Bar.class \n\u793A\u4F8B 2\uFF1A\u4F7F\u7528\u73B0\u6709\u7684\u6E05\u5355\u6587\u4EF6 \"mymanifest\" \u5E76\n           \u5C06 foo/ \u76EE\u5F55\u4E2D\u7684\u6240\u6709\u6587\u4EF6\u5F52\u6863\u5230 \"classes.jar\" \u4E2D\uFF1A\n       jar cvfm classes.jar mymanifest -C foo/ .\n" },
        };
    }
}
