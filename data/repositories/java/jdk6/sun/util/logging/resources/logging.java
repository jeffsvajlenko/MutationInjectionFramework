package sun.util.logging.resources;

import java.util.ListResourceBundle;

public final class logging extends ListResourceBundle
{
    protected final Object[][] getContents()
    {
        return new Object[][]
        {
            { "ALL", "ALL" },
            { "CONFIG", "CONFIG" },
            { "FINE", "FINE" },
            { "FINER", "FINER" },
            { "FINEST", "FINEST" },
            { "INFO", "INFO" },
            { "OFF", "OFF" },
            { "SEVERE", "SEVERE" },
            { "WARNING", "WARNING" },
        };
    }
}
