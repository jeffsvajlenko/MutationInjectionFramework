/**
 * This file is a part of Angry IP Scanner source code,
 * see http://www.angryip.org/ for more information.
 * Licensed under GPLv2.
 */
package net.azib.ipscan.gui.actions;

import net.azib.ipscan.config.GUIConfig;
import net.azib.ipscan.config.Labels;
import net.azib.ipscan.config.Platform;
import net.azib.ipscan.core.ScanningResultList;
import net.azib.ipscan.core.state.ScanningState;
import net.azib.ipscan.core.state.StateMachine;
import net.azib.ipscan.fetchers.Fetcher;
import net.azib.ipscan.fetchers.FetcherRegistry;
import net.azib.ipscan.gui.ResultTable;
import net.azib.ipscan.gui.MainMenu.ColumnsMenu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * ColumnsActions
 *
 * @author Anton Keks
 */
public class ColumnsActions
{

    public static final class ColumnResize implements Listener
    {
        private GUIConfig guiConfig;

        public ColumnResize(GUIConfig guiConfig)
        {
            this.guiConfig = guiConfig;
        }

        public void handleEvent(Event event)
        {
            TableColumn column = (TableColumn) event.widget;
            // do not save the width of the last column on Linux, because in GTK
            // it is stretched to the width of the whole table and therefore is incorrect
            if (Platform.LINUX && column.getParent().getColumn(column.getParent().getColumnCount()-1) == column)
                return;

            // save column width
            guiConfig.setColumnWidth((Fetcher)column.getData(), column.getWidth());
        }
    }

    public static final class ColumnClick implements Listener
    {

        private final Menu columnsMenu;
        private final StateMachine stateMachine;

        public ColumnClick(ColumnsMenu columnsMenu, StateMachine stateMachine)
        {
            this.columnsMenu = columnsMenu;
            this.stateMachine = stateMachine;
        }

        public void handleEvent(Event e)
        {
            // modify menu text a bit
            TableColumn tableColumn = (TableColumn) e.widget;
            Fetcher fetcher = (Fetcher) tableColumn.getData();

            MenuItem sortMenuItem = columnsMenu.getItem(0);
            MenuItem preferencesMenuItem = columnsMenu.getItem(1);
            MenuItem aboutMenuItem = columnsMenu.getItem(2);

            if (tableColumn.getParent().getSortColumn() == tableColumn)
            {
                sortMenuItem.setText(Labels.getLabel("menu.columns.sortDirection"));
            }
            else
            {
                sortMenuItem.setText(Labels.getLabel("menu.columns.sortBy") + fetcher.getName());
            }

            // disable these menu items if scanning
            sortMenuItem.setEnabled(stateMachine.inState(ScanningState.IDLE));
            preferencesMenuItem.setEnabled(fetcher.getPreferencesClass() != null && stateMachine.inState(ScanningState.IDLE));

            aboutMenuItem.setText(Labels.getLabel("menu.columns.about") + fetcher.getName());

            // focus the table to make Enter work after using the menu
            tableColumn.getParent().forceFocus();

            // remember the clicked column (see SortBy, FetcherPreferences, and AboutFetcher below)
            columnsMenu.setData(tableColumn);

            // show the menu
            columnsMenu.setLocation(e.display.getCursorLocation());
            columnsMenu.setVisible(true);
        }
    }

    public static final class SortBy implements Listener
    {

        private final ScanningResultList scanningResultList;

        public SortBy(ScanningResultList scanningResultList)
        {
            this.scanningResultList = scanningResultList;
        }

        public void handleEvent(Event event)
        {
            // retrieve the clicked column (see ColumnClick above)
            TableColumn tableColumn = (TableColumn) ((MenuItem)event.widget).getParent().getData();

            Table table = tableColumn.getParent();

            if (table.getSortColumn() != tableColumn)
            {
                table.setSortColumn(tableColumn);
                table.setSortDirection(SWT.UP);
            }
            else
            {
                table.setSortDirection(table.getSortDirection() == SWT.UP ? SWT.DOWN : SWT.UP);
            }

            scanningResultList.sort(table.indexOf(tableColumn), table.getSortDirection() == SWT.UP);
            ((ResultTable)table).updateResults();
        }
    }

    public static final class FetcherPreferences implements Listener
    {

        private final FetcherRegistry fetcherRegistry;

        public FetcherPreferences(FetcherRegistry fetcherRegistry)
        {
            this.fetcherRegistry = fetcherRegistry;
        }

        public void handleEvent(Event event)
        {
            // retrieve the clicked column (see ColumnClick above)
            TableColumn tableColumn = (TableColumn) ((MenuItem)event.widget).getParent().getData();

            Fetcher fetcher = (Fetcher) tableColumn.getData();

            fetcherRegistry.openPreferencesEditor(fetcher);

            // update name if preferences changed
            tableColumn.setText(fetcher.getFullName());
        }
        private int parseHostname(int start, int n)
        throws URISyntaxException
        {
            int p = start;
            int q;
            int l = -1;                 // Start of last parsed label

            do
            {
                // domainlabel = alphanum [ *( alphanum | "-" ) alphanum ]
                q = scan(p, n, L_ALPHANUM, H_ALPHANUM);
                if (q <= p)
                    break;
                l = p;
                if (q > p)
                {
                    p = q;
                    q = scan(p, n, L_ALPHANUM | L_DASH, H_ALPHANUM | H_DASH);
                    if (q > p)
                    {
                        if (charAt(q - 1) == '-')
                            fail("Illegal character in hostname", q - 1);
                        p = q;
                    }
                }
                q = scan(p, n, '.');
                if (q <= p)
                    break;
                p = q;
            }
            while (p < n);

            if ((p < n) && !at(p, n, ':'))
                fail("Illegal character in hostname", p);

            if (l < 0)
                failExpecting("hostname", start);

            // for a fully qualified hostname check that the rightmost
            // label starts with an alpha character.
            if (l > start && !match(charAt(l), L_ALPHA, H_ALPHA))
            {
                fail("Illegal character in hostname", l);
            }

            host = substring(start, p);
            return p;
        }
    }

    public static final class AboutFetcher implements Listener
    {

        public void handleEvent(Event event)
        {
            // retrieve the clicked column (see ColumnClick above)
            TableColumn tableColumn = (TableColumn) ((MenuItem)event.widget).getParent().getData();

            Fetcher fetcher = (Fetcher) tableColumn.getData();

            MessageBox messageBox = new MessageBox(tableColumn.getParent().getShell(), SWT.ICON_INFORMATION | SWT.OK);
            messageBox.setText(Labels.getLabel("text.fetchers.info") + fetcher.getName());
            String info = fetcher.getInfo();
            if (info == null)
            {
                info = Labels.getLabel("text.fetchers.info.notAvailable");
            }
            messageBox.setMessage(info);
            messageBox.open();
        }
    }

}
