// ows.cs created with MonoDevelop
// User: shen139 at 13:53 29/06/2008
//

using System;

namespace GlobalVars
{
    class OpenWebSpider
    {
		public static string NAME = "OpenWebSpider";
		public static string AUTHOR = "Stefano Alimonti (shen139@openwebspider.org)";
		public static string VERSION = "0.1.4";
        public static string ID = "C001";
		public static string USERAGENT;
		
		// se impostato a true: lo spider verrà terminato in modo pulito
		public static bool stopItGracefully = false;

        // se impostato a true: lo spider verrà messo in pausa
        public static bool crawlerActPAUSE = false;
		
	}
}

namespace OpenWebSpiderCS
{
	
	public class ows
	{
		private threading threads;
		
		// init OpenWebSpider
		public ows(int outputType)
		{
			// crea un nuovo oggetto output con destinazione la Console (type == 1) con debugMode attivo
            nsGlobalOutput.output.init(outputType, true);
			
            nsGlobalOutput.output.write( GlobalVars.OpenWebSpider.NAME + "(v" + GlobalVars.OpenWebSpider.VERSION + ") [ http://www.openwebspider.org/ ]");
            nsGlobalOutput.output.write("  Copyright (c) 2004-2009 " + GlobalVars.OpenWebSpider.AUTHOR + "\n");
            nsGlobalOutput.output.write(" + Libraries:");
            nsGlobalOutput.output.write("   - Mysql/NET Connector (v5.2.5.0)");
            nsGlobalOutput.output.write("   - UltraID3Lib (v0.9.6.6)");
            nsGlobalOutput.output.write("   - PDFBox (v0.7.3)\n");
		}
		
		// legge i parametri passati dalla riga di comando
		public bool readCommandLine( string[] _args )
		{
			// legge gli argomenti dalla linea di comando
			if( GlobalVars.args.readArgs( _args ) == false )
				return false;
			

			{
				// controlla che l'URL da indicizzare sia valido
				// se manca l'http iniziale ce lo aggiunge
				if( !GlobalVars.args.startURL.StartsWith("http://", StringComparison.CurrentCultureIgnoreCase) )
					GlobalVars.args.startURL = "http://" + GlobalVars.args.startURL;
				
				page startPage = new page(GlobalVars.args.startURL, GlobalVars.args.startURL, null);
				if( startPage.isValidPage == false )
				{
					nsGlobalOutput.output.write("Invalid URL [" + GlobalVars.args.startURL + "]");
					return false;
				}
				
				// imposta il dominio corrente da indicizzare
				GlobalVars.threadsVars.currentDomain = startPage;
				
				return true;
			}
		}
		
		// legge il file di configurazione
		public bool readConfFile(string f)
		{
			GlobalVars.readConfFile.parseFile(f);
            if (GlobalVars.readConfFile.isConfFileParsed == false)
            {
                // il file di configurazione non esiste o non è stato letto correttamente
                nsGlobalOutput.output.write("Error(1) while reading configuration file.");
                return false;
            }		
			return true;
		}

        public bool isMysqlNeeded()
        {
            if (GlobalVars.args.stressTest)
                return false;

            return true;
        }
		
		// connessione ai server mysql
		public bool mysqlConnect()
		{
			nsGlobalOutput.output.write("");
			nsGlobalOutput.output.write(" + Connecting to MySQL Server ["+ GlobalVars.readConfFile.mysqlServerHostList +"]; DB ["+ GlobalVars.readConfFile.databaseHostList +"]");
			
            /* connection string(s) */
            GlobalVars.mysqlConn.connHostList = new mysql("Server=" + GlobalVars.readConfFile.mysqlServerHostList + ";" +
                                    "Database=" + GlobalVars.readConfFile.databaseHostList + ";" +
                                    "User ID="+ GlobalVars.readConfFile.mysqlUserNameHostList +";" +
                                    "Password="+ GlobalVars.readConfFile.mysqlPassWordHostList +";" +
                                    "Port=" + GlobalVars.readConfFile.mysqlServerPort1 + ";" +
                                    "charset=utf8;" +
                                    "Pooling=false");

			nsGlobalOutput.output.write(" + Connecting to MySQL Server ["+ GlobalVars.readConfFile.mysqlServerPageList +"]; DB ["+ GlobalVars.readConfFile.databasePageList +"]");
            GlobalVars.mysqlConn.connPageList = new mysql("Server=" + GlobalVars.readConfFile.mysqlServerPageList + ";" +
                                    "Database=" + GlobalVars.readConfFile.databasePageList + ";" +
                                    "User ID=" + GlobalVars.readConfFile.mysqlUserNamePageList + ";" +
                                    "Password=" + GlobalVars.readConfFile.mysqlPassWordPageList + ";" +
                                    "Port=" + GlobalVars.readConfFile.mysqlServerPort2 + ";" +
                                    "charset=utf8;" +
                                    "Pooling=false");
            
            if (!GlobalVars.mysqlConn.connHostList.isConnected || !GlobalVars.mysqlConn.connPageList.isConnected)
            {
                // impossibile connettersi ad uno dei mysql server
                nsGlobalOutput.output.write("   - Error(2) while trying to connect to one or more mysql server.");
				return false;
            }
			else
				nsGlobalOutput.output.write("   - Connected to both MySQL Servers");
			
			nsGlobalOutput.output.write("");
			
			return true;			
		}

		/* showBanner
		 * mostra un cartello con le impostazioni dello spider all'avvio
		 */
		public void showBanner()
		{
			
			nsGlobalOutput.output.write("");
			nsGlobalOutput.output.write(" + Parameters:");
			nsGlobalOutput.output.write("   - Starting URL     : " + GlobalVars.threadsVars.currentDomain.GenerateURL() );
			nsGlobalOutput.output.write("   - Single Host Mode : " + ( GlobalVars.args.singleHostMode ? "On" : "Off") );
			nsGlobalOutput.output.write("   - Crawl-Delay      : " + GlobalVars.args.crawlDelay + " seconds" );
			nsGlobalOutput.output.write("");
		}
		
		// inizia l'indicizzazione
		public bool startCrawling()
		{
			// inizializza le liste degli URL da indicizzare e esterni(cache) + la lista delle relazioni
			GlobalVars.urlsLists.init();
			GlobalVars.externUrlsLists.init();
			GlobalVars.relsList.init();
            GlobalVars.imagesLists.init();
			
			// inizializza i thread
			threads = new threading();
			
			bool haveToIndexSomething = true;

			// funzioni per l'accesso al DBs
			db __db = new db();
			
			// attiva l'handler per la pressione di CTRL+C
			nsGlobalOutput.output.handleCTRLC();

			while( haveToIndexSomething )
			{
				// svuota le liste
				nsGlobalOutput.output.write(" + Clearing structures...");
				GlobalVars.urlsLists.clear();
				GlobalVars.externUrlsLists.l.Clear();
				GlobalVars.relsList.rels.Clear();
                GlobalVars.imagesLists.clear();
				
				nsGlobalOutput.output.write("");

                // init mutexes
                threads.initMutexes();

                if (isMysqlNeeded())
                {
                    // *******************************************************************
                    nsGlobalOutput.output.write(" + Checking connection to MySQL servers...");
                    if (GlobalVars.mysqlConn.connHostList.ping() == false || GlobalVars.mysqlConn.connPageList.ping() == false)
                    {
                        nsGlobalOutput.output.write("   - One or both MySQL server disconnected, trying to reconnect!");
                        if (mysqlConnect() == false)
                        {
                            nsGlobalOutput.output.write("   - Unable to connect to one or both MySQL server!");
                            break;
                        }

                    }
                    // *******************************************************************

                    // inserisce/aggiorna nel DB il sito corrente e ritorna l'hostID
                    GlobalVars.threadsVars.currentDomain._hostID = __db.startIndexThisSite(GlobalVars.threadsVars.currentDomain);
                }

				// a questo punto ha aggiunto l'HOST al DB: possiamo uscire
				if( GlobalVars.args.add2Hostlist == true )
				{
					nsGlobalOutput.output.write("\n\n");
					nsGlobalOutput.output.write(" + Host added to the table of the hosts!");
					nsGlobalOutput.output.write("   - Hostname : " + GlobalVars.threadsVars.currentDomain._hostname );
					nsGlobalOutput.output.write("   - Host ID  : " + GlobalVars.threadsVars.currentDomain._hostID );
					nsGlobalOutput.output.write("\n\n");
					break;
				}

                if (isMysqlNeeded())
                {
                    // carica i limiti dal DB [ hostlist_extras ] se presenti
                    GlobalVars.limits.loadHostlistExtraLimits(GlobalVars.threadsVars.currentDomain._hostID);
                }
				
				GlobalVars.limits.showLimits();
				
				// setta il tempo esatto di inizio dell'indicizzazione del dominio corrente
				GlobalVars.limits.startTime = DateTime.Now.Ticks;

				// azzera le pagine, i bytes indicizzati e gli errori HTTP
				GlobalVars.limits.curPages = 0;
				GlobalVars.limits.curBytes = 0;
				GlobalVars.limits.curErrorCodes = 0;
				
                // se siamo in stress test non controllare il file robots.txt
                if (GlobalVars.args.stressTest == false)
                {
                    // recupera e analizza il file: robots.txt
                    {
                        robots prb = new robots();
                        http tmpHttpRobotsTxt = new http();
                        string tmpsRobotsTxt;
                        tmpsRobotsTxt = tmpHttpRobotsTxt.getURL("http://" + GlobalVars.threadsVars.currentDomain._hostname + ":" + GlobalVars.threadsVars.currentDomain._port + "/robots.txt", null, false);
                        if (tmpHttpRobotsTxt.statusCode == 200)
                            prb.parseRobotsTxt(tmpsRobotsTxt);
                        else
                            nsGlobalOutput.output.write("   - robots.txt not found");
                    }
                }
				
				nsGlobalOutput.output.write("");
				
				// aggiunge al primo posto della lista la prima pagina da indicizzare
				GlobalVars.urlsLists.addURL( GlobalVars.threadsVars.currentDomain );
				
				// crea i threads che indicizzeranno le pagine
				try
				{
					threads.createThreads();
				}
				catch(Exception e)
				{
					nsGlobalOutput.output.write("   - Error while creating threads: " + e.Message);
					threads.killThreads();
					return false;
				}
				
				try
				{
					bool waitThreads = true;
					
					while( waitThreads )
					{
						System.Threading.Thread.Sleep(250);

                        __db.checkCrawlerAct();

						// se abbiamo premuto CTRL+C : esci dal ciclo di indicizzazione
						if( GlobalVars.OpenWebSpider.stopItGracefully == true )
							waitThreads = false;
						
						// controlla ogni tot millisecondi se ci sono altre pagine da indicizzare
						// se no: passa al prossimo dominio
                        
                        GlobalVars.threadsVars.mutexAccessURLList.WaitOne();
                        try
                        {
                            if (GlobalVars.urlsLists.getPageByStatus(0) == null && GlobalVars.urlsLists.getPageByStatus(2) == null)
                                waitThreads = false;
                        }
                        catch (Exception e)
                        {
                            nsGlobalOutput.output.write("   - Error 1 [Wait Threads]: " + e.Message);
                        }
                        finally
                        {
                            GlobalVars.threadsVars.mutexAccessURLList.ReleaseMutex();
                        }
						
						// controlla i limiti
						if( GlobalVars.limits.checkLimits() == false )
							waitThreads = false;
							
					}
					
				}
				catch(Exception e)
				{
					nsGlobalOutput.output.write("   - Error 2 [Wait Threads]: " + e.Message);
				}
				
				// killa i threads
				threads.killThreads();


                // *******************************************************************
                // ri-controlla la connessione al mysql e se down prova a ristabilirla
                if (isMysqlNeeded())
                {
                    nsGlobalOutput.output.write(" + Checking connection to MySQL servers...");
                    if (GlobalVars.mysqlConn.connHostList.ping() == false || GlobalVars.mysqlConn.connPageList.ping() == false)
                    {
                        nsGlobalOutput.output.write("   - One or both MySQL server disconnected, trying to reconnect!");
                        if (mysqlConnect() == false)
                        {
                            nsGlobalOutput.output.write("   - Unable to connect to one or both MySQL server!");
                            break;
                        }

                    }
                }
                
                // *******************************************************************
				
				// aggiorna nel DB il sito corrente ("indicizzato"; status = 1)
				__db.stopIndexThisSite( GlobalVars.threadsVars.currentDomain );
				
				// stampa le statistiche di indicizzazione
				printCurDomainStats();

                if (isMysqlNeeded())
                {
                    // lascia intatti i duplicati?
                    if (!GlobalVars.args.keepDup)
                    {
                        // elimina le pagine con MD5 duplicato lasciando solo quella con id minore
                        __db.deleteDuplicatedPages(GlobalVars.threadsVars.currentDomain._hostID);
                    }

                    // riversa gli URL esterni trovati nel DB
                    __db.swapExternURLs2DB();

                    // salva le relazioni nel DB
                    __db.saveRels();

                    //salva le immagini
                    __db.saveImages();

                    // calcolo del rank delle pagine
                    new rank(GlobalVars.threadsVars.currentDomain._hostID);
                }

				// c'è stata una pressione di CTRL+C : esci
				if( GlobalVars.OpenWebSpider.stopItGracefully == true )
					break;
					
				// abbiamo indicizzato il primo sito! dobbiamo continuare?
				if( GlobalVars.args.singleHostMode == true )
					haveToIndexSomething = false;  // siamo in single host mode; non indicizzare nient'altro
				else
				{
					// recupera il primo host libero dal DB
					page p = __db.getFirstAvailableURL();
					
					if( p == null)
					{
						// impossibile trovare un URL: esci!!!
						haveToIndexSomething = false;
						
						nsGlobalOutput.output.write(" + Nothing to do: no available website to index!!! (Try to add: --add-external to the command line arguments) ");
					}
					else
					{
						// imposta il dominio corrente da indicizzare
						GlobalVars.threadsVars.currentDomain = p;
						haveToIndexSomething = true;
					}
					
				}
					
			}
			
			return true;
		}
		
		// spegne lo spider
		public void shutDown()
		{
            if (isMysqlNeeded())
            {
                // sconnette i mysql server
                GlobalVars.mysqlConn.connHostList.disconnect();
                GlobalVars.mysqlConn.connPageList.disconnect();
                nsGlobalOutput.output.write(" + MySQL Servers Disconnected! ");
            }
			
			nsGlobalOutput.output.write("");
			
			nsGlobalOutput.output.write("Bye bye");
			
			nsGlobalOutput.output.write("");
		}
		
		public void printCurDomainStats()
		{
			nsGlobalOutput.output.write("\n + Stats ");
			nsGlobalOutput.output.write("   - Hostname: " + GlobalVars.threadsVars.currentDomain._hostname + "  [Port: " + GlobalVars.threadsVars.currentDomain._port + "]");
			nsGlobalOutput.output.write("   - Pages Indexed: " + GlobalVars.limits.curPages + " [" + GlobalVars.limits.curBytes / 1024 + " Kb] in " + ((DateTime.Now.Ticks - GlobalVars.limits.startTime) / 10000000) + " seconds");
			nsGlobalOutput.output.write("\n");
			
		}
	}
}
