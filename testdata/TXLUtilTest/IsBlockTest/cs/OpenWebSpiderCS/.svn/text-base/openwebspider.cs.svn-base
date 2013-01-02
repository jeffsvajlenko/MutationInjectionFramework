// openwebspider.cs created with MonoDevelop
// User: shen139 at 10:13 18/06/2008
//

using System;
using System.Net;
using System.IO;


namespace OpenWebSpiderCS
{
    // OpenWebSpiderCS è un web robot/crawler scritto in C#
    class mainClass
    {
		
		// Il punto di partenza del crawler
        // (lettura degli argomenti dalla linea di comando, inizializzazioni, lettura conf)
        static int Main(String[] args)
        {

			// inizializza lo spider
			ows spider = new ows( 1 );

            // legge gli argomenti dalla linea di comando
			if( spider.readCommandLine( args ) == false )
				return 0;
			
			// legge il file di configurazione
            if( spider.readConfFile( GlobalVars.args.confFileName ) == false )
				return 0;
			
            // si connette ai server mysql usando i valori presi dal conf
            if( spider.isMysqlNeeded() )
			    if( spider.mysqlConnect() == false )
				    return 0;
		
			// mostra un riepilogo dei parametri con cui lo spider indicizzerà
			spider.showBanner();

            spider.startCrawling();

			spider.shutDown();

            /*
            nsGlobalOutput.output.write("\n\nPress enter to continue...\n\n");

            Console.ReadLine();
            */

			return 1;
		}

		
    }
}

