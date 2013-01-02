// threading.cs created with MonoDevelop
// User: shen139 at 19:39 22/06/2008
//

using System;
using System.Threading;

namespace GlobalVars
{
    class threadsVars
    {
		// numero di threads (per default)
		public static int nThreads = 10;
		
		// mutex usato per rendere unico l'accesso all'elenco delle pagine da indicizzare
		public static Mutex mutexAccessURLList = new Mutex();
		
		// mutex usato per rendere unico l'accesso all'elenco degli URL esterni
		public static Mutex mutexAccessExternURLList = new Mutex();

        // mutex usato per rendere unico l'accesso all'elenco delle relazioni
        public static Mutex mutexRelsList = new Mutex();
		
		// mutex usati per rendere unico l'accesso al MySQL
		public static Mutex mutexMySQLHostList = new Mutex();
		public static Mutex mutexMySQLPageList = new Mutex();
		
		// mutex usato per serializzare e rallentare lo scaricamento di pagine secondo le regole del Crawl-Delay
		public static Mutex mutexCrawlDelay = new Mutex();
		
		public static OpenWebSpiderCS.page currentDomain;
		
	}
}

namespace OpenWebSpiderCS
{
	
	public class threading
	{

		private volatile bool _shouldStop = false;
		public Thread[] ThreadArray;
		
		public threading()
		{
			// inizializza una array di nThreads threads :-)
			ThreadArray = new Thread[ GlobalVars.threadsVars.nThreads ];
		}
		
		public void createThreads()
		{
			_shouldStop = false;
			nsGlobalOutput.output.write(" + Creating Threads...");
			try
			{
				// crea i thread
				for(int i=0; i < GlobalVars.threadsVars.nThreads ; i++ )
				{
					ThreadArray[i] = new Thread(new ParameterizedThreadStart(this.DoWork));
					ThreadArray[i].Start((object)i);
				}
				nsGlobalOutput.output.write("   - " + GlobalVars.threadsVars.nThreads + " threads created!");
			}
			catch(Exception e)
			{
				nsGlobalOutput.output.write("   - Error while creating Threads: " + e.Message);
				
			}
		}
		
		~threading()
		{
			// killThreads();
		}
		
		public void killThreads()
		{

			// invia il segnale d'uscita
			_shouldStop = true;

			nsGlobalOutput.output.write(" + Killing threads...");
			
			// aspetta un secondo e li killa forzatamente
			System.Threading.Thread.Sleep(1000);

			
			for(int i=0; i < GlobalVars.threadsVars.nThreads; i++ )
			{
				if( ThreadArray[i].IsAlive )
				{
					ThreadArray[i].Abort();
					nsGlobalOutput.output.write("   - Thread " + i + " killed");
				}
			}
			
			// aspetta un secondo
			System.Threading.Thread.Sleep(1000);

		}

        public void initMutexes()
        {
            try
            {
                // chiude e ricrea i mutex usati nei thread
                GlobalVars.threadsVars.mutexCrawlDelay.Close();
                GlobalVars.threadsVars.mutexCrawlDelay = new Mutex();

                GlobalVars.threadsVars.mutexAccessURLList.Close();
                GlobalVars.threadsVars.mutexAccessURLList = new Mutex();

                GlobalVars.threadsVars.mutexAccessExternURLList.Close();
                GlobalVars.threadsVars.mutexAccessExternURLList = new Mutex();

                GlobalVars.threadsVars.mutexRelsList.Close();
                GlobalVars.threadsVars.mutexRelsList = new Mutex();
                
                GlobalVars.threadsVars.mutexMySQLHostList.Close();
                GlobalVars.threadsVars.mutexMySQLHostList = new Mutex();

                GlobalVars.threadsVars.mutexMySQLPageList.Close();
                GlobalVars.threadsVars.mutexMySQLPageList = new Mutex();
            }
            catch (Exception e)
            {
                nsGlobalOutput.output.write("Error[initMutexes()]: " + e.Message + "\n");
            }
        }
		
		// indexer vero e proprio
		public void DoWork(object threadID)
		{
			page pageToIndex;
			string outStr;
			
			// funzioni per l'accesso al DB
			db __db = new db();
			
		    while ( !_shouldStop )
		    {
				System.Threading.Thread.Sleep(50);

                System.Threading.Thread.Sleep(0);

                if (GlobalVars.OpenWebSpider.crawlerActPAUSE)
                {
                    // aspetta ulteriore tempo
                    System.Threading.Thread.Sleep(300);

                    continue;
                }
				
				outStr = string.Empty;
				
				// nsGlobalOutput.output.write("Thread["+ (int)threadID +"]: working.");
				
				// blocca l'accesso alla lista degli URL da indicizzare e ne estrae uno ( se disponibile )
				GlobalVars.threadsVars.mutexAccessURLList.WaitOne();
				
				// cerca una pagina da indicizzare
                // se siamo in stress test mode: usa sempre la prima pagina
                if (GlobalVars.args.stressTest == true)
                    pageToIndex = GlobalVars.threadsVars.currentDomain;
                else
                    pageToIndex = GlobalVars.urlsLists.getPageByStatus(0);
				
				if( pageToIndex == null )
				{
					// rilascia il mutex
					GlobalVars.threadsVars.mutexAccessURLList.ReleaseMutex();
					// riparte dall'inizio
					continue;
				}
				else
				{
					try
					{
						// imposta la pagina come "indicizzazione in corso"
						pageToIndex.isIndexed = 2;
						
						// rilascia il mutex
						GlobalVars.threadsVars.mutexAccessURLList.ReleaseMutex();
						
						// indicizza questa pagina!!!
						html htmlPage = new html();
						
						outStr = outStr + "\nT[" + (int)threadID + "] \t Downloading... [ " + pageToIndex.GenerateURL() + " ] [Depth Level: " + pageToIndex._depthLevel + "]\n";
						
						// siamo in presenza di un Crawl-Delay ( da robots.txt o da riga di comando)
						if( GlobalVars.robotstxtVars.robotstxtCrawlDelay > 0 || GlobalVars.args.crawlDelay > 0 )
						{
							// locka il mutex
							GlobalVars.threadsVars.mutexCrawlDelay.WaitOne();
							
							// aspetta il tempo del Crawl-Delay
							// se c'è un crawl-delay da robots.txt usa quello; altrimenti quello da riga di comando
							int crawlDelay2Use = ( GlobalVars.robotstxtVars.robotstxtCrawlDelay > 0 ) ? GlobalVars.robotstxtVars.robotstxtCrawlDelay * 1000 : GlobalVars.args.crawlDelay * 1000 ;
							System.Threading.Thread.Sleep( crawlDelay2Use );
						}

						long curTick = DateTime.Now.Ticks;
						
						// scarica la pagina
						htmlPage.getURL( pageToIndex, true );
						
						// siamo in presenza di un Crawl-Delay ( da robots.txt o da riga di comando)
						if( GlobalVars.robotstxtVars.robotstxtCrawlDelay > 0 || GlobalVars.args.crawlDelay > 0 )
						{
							// un-locka il mutex
							GlobalVars.threadsVars.mutexCrawlDelay.ReleaseMutex();
						}

                        outStr = outStr + "T[" + (int)threadID + "] \t Downloaded ";
                        if (htmlPage.HTML.Length > 0)
						     outStr = outStr + htmlPage.HTML.Length / 1024 + " Kb (" + htmlPage.HTML.Length + " bytes)";
                        outStr = outStr + " in " + ((DateTime.Now.Ticks - curTick)/10000) + " ms\n";
						
						outStr = outStr + "T[" + (int)threadID + "] \t HTTP Status Code: " + htmlPage.statusCode + " -][- Content-Type: " + htmlPage.contentType + "\n";
						
						if( htmlPage.errorString != "" )
							outStr = outStr + "T[" + (int)threadID + "] \t Error: " + htmlPage.errorString + "\n";

                        // se non stiamo in stress test mode: allora parsa la pagina e indicizza
                        if (GlobalVars.args.stressTest == false)
                        {
                            // se lo status code (HTTP) è 200 (OK) e il content-type è "text/html" :: indicizza!
                            if (htmlPage.statusCode == 200)
                            {
                                if (htmlPage.contentType.StartsWith("text/html"))
                                {
                                    // siamo in una pagina HTML: controlla i META TAG
                                    htmlPage.checkMETA();

                                    // siamo in una pagina HTML: estrai il titolo <title>...</title>
                                    string __title = htmlPage.getTitle();
                                    if (__title != string.Empty)
                                    {
                                        pageToIndex._title = __title;

                                        // controlla che il titolo rientri nel limite del campo sul DB
                                        if (pageToIndex._title.Length > GlobalVars.dbLimits.maxTitleLength)
                                            pageToIndex._title = pageToIndex._title.Substring(0, 255);

                                        outStr = outStr + "T[" + (int)threadID + "] \t Page Title: " + __title + "\n";
                                    }

                                    curTick = DateTime.Now.Ticks;
                                    outStr = outStr + "T[" + (int)threadID + "] \t Getting URLs...";
                                    if (htmlPage.META_ROBOTS_FOLLOW)
                                    {
                                        int nValidURLs = htmlPage.GetURLs(htmlPage.HTML, pageToIndex);
                                        outStr = outStr + "OK [" + ((DateTime.Now.Ticks - curTick) / 10000) + " ms]  ( " + nValidURLs + " valid URLs found )\n";
                                    }
                                    else
                                    { 
                                        // NOFOLLOW
                                        outStr = outStr + "NOFOLLOW (META ROBOTS)\n";
                                    }
                                }

                                curTick = DateTime.Now.Ticks;
                                outStr = outStr + "T[" + (int)threadID + "] \t Indexing...";
                                if (htmlPage.META_ROBOTS_INDEX)
                                {
                                    if (__db.indexThisPage(pageToIndex, htmlPage) == true)
                                        outStr = outStr + "OK [" + ((DateTime.Now.Ticks - curTick) / 10000) + " ms]\n";
                                    else
                                        outStr = outStr + "NOT INDEXED [" + ((DateTime.Now.Ticks - curTick) / 10000) + " ms ]\n";
                                }
                                else
                                { 
                                    // NOINDEX
                                    outStr = outStr + "NOINDEX (META ROBOTS)\n";
                                }
                            }
                            else
                            {
                                GlobalVars.limits.curErrorCodes++;		// TODO: da testare gli error code che arrivano qui
                                Console.WriteLine(" ##  Error code: " + htmlPage.statusCode + "  ## ");
                            }
                        }

						// incrementa il numero di pagine indicizzate
						GlobalVars.limits.curPages++;
						
						// incrementa i bytes indicizzati
						GlobalVars.limits.curBytes += htmlPage.HTML.Length;
					}
					catch(Exception e)
					{
						outStr += "Error: " + e.Message + "\n";
					}
					finally
					{
						// imposta la pagina come "indicizzata"
                        if (GlobalVars.args.stressTest == false)
						    pageToIndex.isIndexed = 1;
                        else
                            // se siamo in stress-test: la mette come da indicizzare
                            pageToIndex.isIndexed = 0;
						
						nsGlobalOutput.output.write( outStr );
					}
				}

		    }
			
		    nsGlobalOutput.output.write("T[" + (int)threadID + "] \t Worker thread: terminating gracefully.");
		}

		
	}
}
