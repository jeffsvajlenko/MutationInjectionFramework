package net.azib.ipscan.core.plugins;

import net.azib.ipscan.core.ScanningSubject;
import net.azib.ipscan.fetchers.AbstractFetcher;
import org.junit.Test;
import org.picocontainer.MutablePicoContainer;

import static org.mockito.Mockito.*;

public class PluginLoaderTest
{
    @Test
    public void loadFromSystemProperty()
    {
        System.setProperty("ipscan.plugins", DummyFetcher.class.getName());
        MutablePicoContainer container = mock(MutablePicoContainer.class);
        new PluginLoader().addTo(container);
        verify(container).registerComponentImplementation(DummyFetcher.class);
    }

    public static class DummyFetcher extends AbstractFetcher
    {
        public Object scan(ScanningSubject subject)
        {
            return "dummy";
        }

        public String getId()
        {
            return "dummy";
        }
    @Test
    public void testBasic() throws IOException
    {
        Labels labels = Labels.getInstance();

        exporter.start(outputStream, "feederstuff");
        exporter.setFetchers(new String[] {"fetcher1", labels.get(IPFetcher.ID), "mega long fetcher 2", labels.get(PortsFetcher.ID)});
        exporter.nextAdressResults(new Object[] {"", "123", "", new NumericRangeList(Arrays.asList(1,23,4,5,6,78), true)});
        exporter.end();

        assertContains("123:1");
        assertContains("123:23");
        assertContains("123:4");
        assertContains("123:5");
        assertContains("123:6");
        assertContains("123:78");
    }
    }
}
