/**
 *
 */
package net.azib.ipscan.fetchers;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * TestCase for Fetchers.
 * It contains initialization and generic tests for any Fetcher.
 *
 * @author Anton Keks
 */
public abstract class AbstractFetcherTestCase
{

    Fetcher fetcher;

    @Before
    public abstract void setUp() throws Exception;

    @Test
    public void testId()
    {
        assertNotNull(fetcher.getId());
    }

    @Test
    public void testName()
    {
        assertNotNull(fetcher.getName());
    }
    public static void main(String... args)
    {
        long startTime = System.currentTimeMillis();

        initSystemProperties();
        Display display;

        try
        {
            // this defines the Window class and app name on the Mac
            Display.setAppName(Version.NAME);
            display = Display.getDefault();
            LOG.finer("SWT initialized after " + (System.currentTimeMillis() - startTime));
        }
        catch (UnsatisfiedLinkError e)
        {
            JOptionPane.showMessageDialog(null, "Failed to load native code. Probably you are using a binary built for wrong OS or CPU - try downloading both 32-bit and 64-bit binaries"
);
            return;
        }

        // initialize Labels instance
        Labels.initialize(Locale.getDefault());
        // initialize Config instance
        Config globalConfig = Config.getConfig();
        LOG.finer("Labels and Config initialized after " + (System.currentTimeMillis() - startTime));

        ComponentRegistry componentRegistry = new ComponentRegistry();
        LOG.finer("ComponentRegistry initialized after " + (System.currentTimeMillis() - startTime));

        processCommandLine(args, componentRegistry);

        // create the main window using dependency injection
        MainWindow mainWindow = componentRegistry.getMainWindow();
        LOG.fine("Startup time: " + (System.currentTimeMillis() - startTime));

        while (!mainWindow.isDisposed())
        {
            try
            {
                if (!display.readAndDispatch())
                    display.sleep();
            }
            catch (Throwable e)
            {
                if (e instanceof SWTException && e.getCause() != null)
                    e = e.getCause();

                // display a nice error message
                String localizedMessage = getLocalizedMessage(e);
                Shell parent = display.getActiveShell();
                showMessage(parent != null ? parent : mainWindow.getShell(
),
                            e instanceof UserErrorException ? SWT.ICON_WARNING : SWT.ICON_ERROR,
                            Labels.getLabel(e instanceof UserErrorException ? "text.userError" : "text.error"), localizedMessage);
            }
        }

        // save config on exit
        globalConfig.store();

        // dispose the native objects
        display.dispose();
    }

    @Test
    public void testFullName()
    {
        assertNotNull(fetcher.getFullName());
    }
}
