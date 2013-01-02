// args.cs created with MonoDevelop
// User: shen139 at 14:41 27/06/2008
//

using System;

namespace GlobalVars
{

	public class args
	{
		public static string startURL;
		public static bool singleHostMode;
		public static int cachingMode;
		public static int relsMode;
		public static bool addExternalHosts;
		public static string confFileName;
		public static bool add2Hostlist;
        public static bool noIndex;
        public static bool stressTest;
        public static int reqTimeout;
        public static bool indexImages;
        public static bool indexPDF;
        public static bool indexMP3;
        public static bool keepDup;

		// crawl-delay
		public static int crawlDelay;
		
		public static void showArgs()
		{
			nsGlobalOutput.output.write("");
			nsGlobalOutput.output.write("Usage:");
			nsGlobalOutput.output.write("       " + System.AppDomain.CurrentDomain + " <arguments>");
			nsGlobalOutput.output.write("\n Arguments:");
			nsGlobalOutput.output.write("    --index, -i [URL]              Indexes this website");
			nsGlobalOutput.output.write("    --add-hostlist                 Doesn't index the URL; it simply adds the hostname to the list of the Hosts (hostlist), prints its ID and and exits");
			nsGlobalOutput.output.write("    --threads, -t [1-100]          Sets number of threads");
			nsGlobalOutput.output.write("    -s                             Single Mode: On (Default: Off)");
            nsGlobalOutput.output.write("    --images                       Indexes images");
            nsGlobalOutput.output.write("    --pdf                          Indexes PDFs");
            nsGlobalOutput.output.write("    --mp3                          Indexes MP3s");
			nsGlobalOutput.output.write("    --cache                        Saves a copy of each indexed page (Default: Doesn't save cache)");
			nsGlobalOutput.output.write("    --cache-compressed             Saves a compressed copy of each indexed page");
			nsGlobalOutput.output.write("    --rels, -r [1,2]               Saves relationships between pages (Default: Doesn't save rels)");
			nsGlobalOutput.output.write("                                       1: saves only hostnames");
			nsGlobalOutput.output.write("                                       2: saves hostnames and pages");
			nsGlobalOutput.output.write("    --add-external, -e             Adds External Hosts (Default: Doesn't add external hosts)");
			nsGlobalOutput.output.write("    --conf-file [filename]         Sets a configuration file (Default: openwebspider.conf)");
            nsGlobalOutput.output.write("    --no-index                     Doesen't index crawled pages");
            nsGlobalOutput.output.write("    --keep-dup                     Doesen't delete duplicated pages");
            nsGlobalOutput.output.write("    --crawl-delay [1-20]           Seconds between the download of a page and the next one");
            nsGlobalOutput.output.write("    --stress-test [1-10000]        Simply downloads the same page x-times and exits");
            nsGlobalOutput.output.write("    --req-timeout [1-1000]         HTTP Request Timeout (in seconds) (Default: 60 seconds)");
			nsGlobalOutput.output.write("\n  Limits:");
			nsGlobalOutput.output.write("    --max-depth, -m [0-1000]       Sets Max Depth Level");
			nsGlobalOutput.output.write("    --max-pages, -l [1-1000000]    Sets Max Pages to Index (per domain)");
			nsGlobalOutput.output.write("    --max-seconds, -c [1-100000]   Sets Max Seconds (per domain)");
			nsGlobalOutput.output.write("    --max-kb, -k [1-100000]        Sets Max Kb to Download (per domain)");
			nsGlobalOutput.output.write("    --errors [1-1000]              Sets Max HTTP Errors Code (per domain)");
			nsGlobalOutput.output.write("\n  Help:");
			nsGlobalOutput.output.write("    --help, -h                     This help!");
			nsGlobalOutput.output.write("");
			
		}
		
		// legge gli argomenti da linea di comando
		public static bool readArgs(String[] a)
		{

			nsGlobalOutput.output.write(" + Parsing Command Line Arguments...");
			
			startURL = string.Empty;
			
			// per default indicizza tutti gli URL a partire da quello passato da riga di comando e tutti 
			singleHostMode = false;
			
			// per default non usare la cache
			cachingMode = 0;
			
			// per default non salvare relazioni ( lento )
			relsMode = 0;
			
			// per default non aggiunge host esterni
			addExternalHosts = false;
			
			// per defaul il Crawl Delay è asente
			crawlDelay = 0;
			
			// configuration file di default (path corrente)
			confFileName = "openwebspider.conf";
			
			// per default aggiunge il sito alla tabella hostlist e lo indicizza
			add2Hostlist = false;

            // per default: noIndex = false (INDICIZZA LE PAGINE)
            noIndex = false;

            // per default: niente stress-test
            stressTest = false;

            // per default: timeout richiesta HTTP è di 60 secondi
            reqTimeout = 60;

            // per default: non indicizzare le immagini e gli MP3
            indexImages = false;
            indexPDF = false;
            indexMP3 = false;

            // elimina le pagine duplicate
            keepDup = false;
			
			for( int i = 0 ; i < a.Length ; i++ )
			{
				switch( a[i] )
				{
				// -i <URL da indicizzare>
				case "--index":
				case "-i":
					//dopo -i ci aspettiamo un URL valido
					if( i+1 >= a.Length )
					{
						nsGlobalOutput.output.write("   - (-i <URL>) No enough arguments");
						return false;
					}
					else
					{
						i++;
						startURL = a[i];						
					}
					
				break;
				// imposta NUMERO THREADS
				case "--threads":
				case "-t":
					//dopo -t ci aspettiamo un numero intero valido compreso tra 1 e 100
					if( i+1 >= a.Length )
					{
						nsGlobalOutput.output.write("   - (-t <n. threads[1-100]>) No enough arguments");
						return false;
					}
					else
					{
						i++;
						try
						{
							GlobalVars.threadsVars.nThreads = int.Parse( a[i] );
						}
						catch
						{
							nsGlobalOutput.output.write("   - (-t <n. threads[1-100]>) Invalid number of threads");
							return false;
						}
						
						if( GlobalVars.threadsVars.nThreads < 1 || GlobalVars.threadsVars.nThreads > 100 )
						{
							nsGlobalOutput.output.write("   - (-t <n. threads[1-100]>) Invalid number of threads");
							return false;
						}
					}
				break;
				// singleHostMode: ON
				case "-s":
					singleHostMode = true;
				break;
                // indicizza immagini?
                case "--images":
                    indexImages = true;
                break;
                // indicizza i PDF
                case "--pdf":
                    indexPDF = true;
                break;
                // indicizza gli MP3
                case "--mp3":
                    indexMP3 = true;
                break;
                // lascia intatti i duplicati
                case "--keep-dup":
                    keepDup = true;
                break;
				// imposta MAX DEPTH LEVEL
				case "-m":
				case "--max-depth":
					//dopo -m ci aspettiamo un numero intero valido compreso tra 0 e 1000
					if( i+1 >= a.Length )
					{
						nsGlobalOutput.output.write("   - (-m <Max depth level[0-1000]>) No enough arguments");
						return false;
					}
					else
					{
						i++;
						try
						{
							GlobalVars.limits.maxDepth = int.Parse( a[i] );
						}
						catch
						{
							nsGlobalOutput.output.write("   - (-m <Max depth level[0-1000]>) Invalid number");
							return false;
						}
						
						if( GlobalVars.limits.maxDepth < 0 || GlobalVars.limits.maxDepth > 1000 )
						{
							nsGlobalOutput.output.write("   - (-m <Max depth level[0-1000]>) Invalid number");
							return false;
						}
					}
				break;
				// imposta MAX numero PAGINE
				case "-l":
				case "--max-pages":
					//dopo -l ci aspettiamo un numero intero valido compreso tra 1 e 1000000
					if( i+1 >= a.Length )
					{
						nsGlobalOutput.output.write("   - (-l <Max Pages[1-1000000]>) No enough arguments");
						return false;
					}
					else
					{
						i++;
						try
						{
							GlobalVars.limits.maxPages = uint.Parse( a[i] );
						}
						catch
						{
							nsGlobalOutput.output.write("   - (-l <Max Pages[1-1000000]>) Invalid number");
							return false;
						}
						
						if( GlobalVars.limits.maxPages < 1 || GlobalVars.limits.maxPages > 1000000 )
						{
							nsGlobalOutput.output.write("   - (-l <Max Pages[1-1000000]>) Invalid number");
							return false;
						}
					}
				break;
				// imposta MAX SECONDI
				case "-c":
				case "--max-seconds":
					//dopo -c ci aspettiamo un numero intero valido compreso tra 1 e 100000
					if( i+1 >= a.Length )
					{
						nsGlobalOutput.output.write("   - (-c <Max seconds[1-100000]>) No enough arguments");
						return false;
					}
					else
					{
						i++;
						try
						{
							GlobalVars.limits.maxSeconds = uint.Parse( a[i] );
						}
						catch
						{
							nsGlobalOutput.output.write("   - (-c <Max seconds[1-100000]>) Invalid number");
							return false;
						}
						
						if( GlobalVars.limits.maxSeconds < 1 || GlobalVars.limits.maxSeconds > 1000 )
						{
							nsGlobalOutput.output.write("   - (-c <Max seconds[1-100000]>) Invalid number");
							return false;
						}
					}
				break;
				// imposta MAX Kb
				case "-k":
				case "--max-kb":
					//dopo -k ci aspettiamo un numero intero valido compreso tra 1 e 100000
					if( i+1 >= a.Length )
					{
						nsGlobalOutput.output.write("   - (-k <Max Kb[1-100000]>) No enough arguments");
						return false;
					}
					else
					{
						i++;
						try
						{
							GlobalVars.limits.maxKb = uint.Parse( a[i] );
						}
						catch
						{
							nsGlobalOutput.output.write("   - (-k <Max Kb[1-100000]>) Invalid number");
							return false;
						}
						
						if( GlobalVars.limits.maxKb < 1 || GlobalVars.limits.maxKb > 100000 )
						{
							nsGlobalOutput.output.write("   - (-k <Max Kb[1-100000]>) Invalid number");
							return false;
						}
					}
				break;
				// imposta MAX HTTP Error codes
				case "--errors":
					//dopo --errors ci aspettiamo un numero intero valido compreso tra 1 e 1000
					if( i+1 >= a.Length )
					{
						nsGlobalOutput.output.write("   - (--errors <Max HTTP Errors[1-1000]>) No enough arguments");
						return false;
					}
					else
					{
						i++;
						try
						{
							GlobalVars.limits.maxErrorCodes = uint.Parse( a[i] );
						}
						catch
						{
							nsGlobalOutput.output.write("   - (--errors <Max HTTP Errors[1-1000]>) Invalid number");
							return false;
						}
						
						if( GlobalVars.limits.maxErrorCodes < 1 || GlobalVars.limits.maxErrorCodes > 1000 )
						{
							nsGlobalOutput.output.write("   - (--errors <Max HTTP Errors[1-1000]>) Invalid number");
							return false;
						}
					}
				break;
				case "--cache":
					cachingMode = 1;
				break;
				case "--cache-compressed":
					cachingMode = 2;
				break;
				case "--conf-file":
					//dopo --conf-file ci aspettiamo un filename
					if( i+1 >= a.Length )
					{
						nsGlobalOutput.output.write("   - (--conf-file <filename>) No enough arguments");
						return false;
					}
					else
					{
						i++;
						confFileName = a[i];
					}
				break;
				case "-r":
				case "--rels":
					//dopo -r ci aspettiamo un numero intero valido compreso tra 1 e 2
					if( i+1 >= a.Length )
					{
						nsGlobalOutput.output.write("   - (-r <1: host rels; 2: page rels>) No enough arguments");
						return false;
					}
					else
					{
						i++;
						try
						{
							relsMode = int.Parse( a[i] );
						}
						catch
						{
							nsGlobalOutput.output.write("   - (-r <1: host rels; 2: page rels>) Invalid value");
							return false;
						}
						
						if( relsMode != 1 && relsMode != 2 )
						{
							nsGlobalOutput.output.write("   - (-r <1: host rels; 2: page rels>) Invalid value");
							return false;
						}
					}
				break;
				case "--crawl-delay":
					//dopo --crawl-delay ci aspettiamo un numero intero valido compreso tra 1 e 20 (secondi)
					if( i+1 >= a.Length )
					{
						nsGlobalOutput.output.write("   - (--crawl-delay <seconds[1-20]>) No enough arguments");
						return false;
					}
					else
					{
						i++;
						try
						{
							crawlDelay = int.Parse( a[i] );
						}
						catch
						{
							nsGlobalOutput.output.write("   - (--crawl-delay <seconds[1-20]>) Invalid value");
							return false;
						}
						
						if( crawlDelay < 1 || crawlDelay > 20 )
						{
							nsGlobalOutput.output.write("   - (--crawl-delay <seconds[1-20]>) Invalid value");
							return false;
						}
					}
				break;
				case "--add-external":
				case "-e":
					addExternalHosts = true;
				break;
				case "--add-hostlist":
					add2Hostlist = true;
					
					// forza lo spider in Single Mode
					//singleHostMode = true;
				break;
                case "--no-index":
					noIndex = true;
				break;
                case "--stress-test":
                    //dopo --stress-test ci aspettiamo un numero intero valido compreso tra 1 e 10000
                    if (i + 1 >= a.Length)
                    {
                        nsGlobalOutput.output.write("   - (--stress-test <times[1-10000]>) No enough arguments");
                        return false;
                    }
                    else
                    {
                        i++;
                        try
                        {
                            GlobalVars.limits.maxPages = uint.Parse(a[i]);
                        }
                        catch
                        {
                            nsGlobalOutput.output.write("   - (--stress-test <times[1-10000]>) Invalid value");
                            return false;
                        }

                        if (GlobalVars.limits.maxPages < 1 || GlobalVars.limits.maxPages > 10000)
                        {
                            nsGlobalOutput.output.write("   - (--stress-test <times[1-10000]>) Invalid value");
                            return false;
                        }

                        stressTest = true;

                        // se siamo in stress test mode: forza single host mode
                        singleHostMode = true;
                    }
                break;
                case "--req-timeout":
                    //dopo --req-timeout ci aspettiamo un numero intero valido compreso tra 1 e 1000 (secondi)
                    if (i + 1 >= a.Length)
                    {
                        nsGlobalOutput.output.write("   - (--req-timeout <seconds[1-1000]>) No enough arguments");
                        return false;
                    }
                    else
                    {
                        i++;
                        try
                        {
                            reqTimeout = int.Parse(a[i]);
                        }
                        catch
                        {
                            nsGlobalOutput.output.write("   - (--req-timeout <seconds[1-1000]>) Invalid value");
                            return false;
                        }

                        if (reqTimeout < 1 || reqTimeout > 1000)
                        {
                            nsGlobalOutput.output.write("   - (--req-timeout <seconds[1-1000]>) Invalid value");
                            return false;
                        }
                    }
                break;
				case "-h":
				case "--help":
					showArgs();
				break;
				default:
					nsGlobalOutput.output.write("   - Unknown option argument [" + a[i] + "]\n\n");
					return false;
				}
			}

			if( startURL == string.Empty )
			{
				nsGlobalOutput.output.write("   - (--index) Start URL needed!");
				return false;
			}
			else
				return true;
		}
	}
}

