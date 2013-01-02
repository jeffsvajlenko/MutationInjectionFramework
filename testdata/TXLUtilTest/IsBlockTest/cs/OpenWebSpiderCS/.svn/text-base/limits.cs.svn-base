// limits.cs created with MonoDevelop
// User: shen139 at 18:43 27/06/2008
//

using System;
using System.Data;

namespace GlobalVars
{
	
	public class limits
	{
		public static bool useHostlist_Extras_limits = false;
		
		// numero massimo di livelli in cui scendere nell'indicizzazione di ogni dominio (via command-line)
		public static int maxDepth = -1;
		// numero massimo di livelli in cui scendere nell'indicizzazione di ogni dominio (via hostlist_extras)
		public static int he_maxDepth = -1;
				
		// numero massimo di pagine per dominio (via command-line)
        public static uint maxPages = 0;
		// numero massimo di pagine per dominio (via hostlist_extras)
		public static uint he_maxPages = 0;
		
		// numero di pagine indicizzate (ogni thread che indicizza una pagina fà un incremento)
		public static volatile uint curPages = 0;
		
		// numero massimo di Kb per dominio (via command-line)
		public static uint maxKb = 0;
		// numero massimo di Kb per dominio (via hostlist_extras)
		public static uint he_maxKb = 0;
		// numero di bytes indicizzate (ogni thread che indicizza una pagina fà un incremento)
		public static volatile int curBytes = 0;
		
		// numero massimo di secondi per dominio (via command-line)
		public static uint maxSeconds = 0;
		// numero massimo di secondi per dominio (via hostlist_extras)
		public static uint he_maxSeconds = 0;
		public static long startTime = 0;
		
		// numero massimo di errori nello scaricamento di una pagina (via command-line)
        public static uint maxErrorCodes = 0;
		// numero massimo di errori nello scaricamento di una pagina (via hostlist_extras)
        public static uint he_maxErrorCodes = 0;
		// numero attuale di errori per dominio
		public static volatile uint curErrorCodes = 0;
		
		public static string he_regex_include_pages;
		public static string he_regex_exclude_pages;
		
		/* checkLimits
		 * controlla che l'indicizzazione sia dentro i limiti imposti altrimenti ritorna false e interrompe l'indicizzazione del dominio
		 * 
		 * 
		 * maxDepth o he_maxDepth vengono controllati direttamente nella AddURL in lists.cs
		 * 
		 */
		public static bool checkLimits()
		{
			// usa i limiti dal DB?
			if( useHostlist_Extras_limits == true )
			{
				// se è impostato un numero massimo di pagine da indicizzare e il numero corrente di pagine lo supera: INTERROMPI INDICIZZAZIONE
				if( he_maxPages > 0 && curPages > he_maxPages)
				{
					nsGlobalOutput.output.write(" + Max number of indexed pages exceeded!");
					return false;
				}

				// se è impostato un numero massimo di Kb da indicizzare e il numero corrente lo supera: INTERROMPI INDICIZZAZIONE
				if( he_maxKb > 0 && (curBytes / 1024) > he_maxKb)
				{
					nsGlobalOutput.output.write(" + Max number of Kb downloaded exceeded!");
					return false;
				}
				
				// se è impostato un numero massimo di secondi e il numero corrente lo supera: INTERROMPI INDICIZZAZIONE
				if( he_maxSeconds > 0 && ((DateTime.Now.Ticks - startTime) / 10000000) > he_maxSeconds)
				{
					nsGlobalOutput.output.write(" + Max number of seconds exceeded!");
					return false;
				}
				
				// se è impostato un numero massimo di pagine da indicizzare e il numero corrente di pagine lo supera: INTERROMPI INDICIZZAZIONE
				if( he_maxErrorCodes > 0 && curErrorCodes > he_maxErrorCodes)
				{
					nsGlobalOutput.output.write(" + Max number of HTTP Error codes exceeded!");
					return false;
				}
			}
			else
			{
				// se è impostato un numero massimo di pagine da indicizzare e il numero corrente di pagine lo supera: INTERROMPI INDICIZZAZIONE
				if( maxPages > 0 && curPages > maxPages)
				{
					nsGlobalOutput.output.write(" + Max number of indexed pages exceeded!");
					return false;
				}

				// se è impostato un numero massimo di Kb da indicizzare e il numero corrente lo supera: INTERROMPI INDICIZZAZIONE
				if( maxKb > 0 && (curBytes / 1024) > maxKb)
				{
					nsGlobalOutput.output.write(" + Max number of Kb downloaded exceeded!");
					return false;
				}
				
				// se è impostato un numero massimo di secondi e il numero corrente lo supera: INTERROMPI INDICIZZAZIONE
				if( maxSeconds > 0 && ((DateTime.Now.Ticks - startTime) / 10000000) > maxSeconds)
				{
					nsGlobalOutput.output.write(" + Max number of seconds exceeded!");
					return false;
				}
				
				// se è impostato un numero massimo di pagine da indicizzare e il numero corrente di pagine lo supera: INTERROMPI INDICIZZAZIONE
				if( maxErrorCodes > 0 && curErrorCodes > maxErrorCodes)
				{
					nsGlobalOutput.output.write(" + Max number of HTTP Error codes exceeded!");
					return false;
				}
			}
			return true;
		}

		/* loadHostlistExtraLimits
		 * carica i limiti per l'host corrente dal DB
		 */
		public static void loadHostlistExtraLimits( int host_id )
		{
			IDataReader reader;

			// non usare i limiti da DB
			useHostlist_Extras_limits = false;
			
			// inizializza con i default
			he_maxPages = 0;
			he_maxDepth = -1;
			he_maxSeconds = 0;
			he_maxKb = 0;
			he_maxErrorCodes = 0;
			GlobalVars.limits.he_regex_include_pages = string.Empty;
			GlobalVars.limits.he_regex_exclude_pages = string.Empty;

			reader = GlobalVars.mysqlConn.connHostList.getSQLResult("SELECT " +
			                                                        "  `max_pages`" +
			                                                        ", `max_level`" +
			                                                        ", `max_seconds`" +
			                                                        ", `max_bytes`" +
			                                                        ", `max_HTTP_errors`"+
			                                                        ", `include_pages_regex`" +
			                                                        ", `exclude_pages_regex`" +
			                                                        "  FROM hostlist_extras" +
			                                                        "  WHERE host_id = " + host_id + 
			                                                        "  LIMIT 1");
			if( reader == null )
				return;
			
			try
			{
				if (reader.Read())
				{

					// nel DB ci sono dei limiti: usa questi valori
					useHostlist_Extras_limits = true;
				
					he_maxPages = uint.Parse( (string)reader[0].ToString() );
					he_maxDepth = int.Parse( (string)reader[1].ToString() );
					he_maxSeconds = uint.Parse( (string)reader[2].ToString() );
					he_maxKb = uint.Parse( (string)reader[3].ToString() );
					he_maxErrorCodes = uint.Parse( (string)reader[4].ToString() );
					
					he_regex_include_pages = (string)reader[5].ToString().Trim();
					he_regex_exclude_pages = (string)reader[6].ToString().Trim();
					
				}
			}
			catch(Exception e)
			{
				nsGlobalOutput.output.write("\n   - Error [loadHostlistExtraLimits()]: " + e.Message + "\n\n\n");
			}
			finally
			{
				// clean up
				reader.Close();
				reader = null;
			}

			return;
		}
		
		public static void showLimits()
		{
			nsGlobalOutput.output.write("");
			
			if( useHostlist_Extras_limits == false )
			{
				nsGlobalOutput.output.write("\n + Limits [via command line or default]");
				nsGlobalOutput.output.write("   - Max Pages        : " + GlobalVars.limits.maxPages );
				nsGlobalOutput.output.write("   - Max Depth Level  : " + GlobalVars.limits.maxDepth);
				nsGlobalOutput.output.write("   - Max Seconds      : " + GlobalVars.limits.maxSeconds);
				nsGlobalOutput.output.write("   - Max Kb           : " + GlobalVars.limits.maxKb);
				nsGlobalOutput.output.write("   - Max HTTP Errors  : " + GlobalVars.limits.maxErrorCodes);
			}
			else
			{
				nsGlobalOutput.output.write("\n + Limits [via table hostlist_extras]");
				nsGlobalOutput.output.write("   - Max Pages        : " + GlobalVars.limits.he_maxPages );
				nsGlobalOutput.output.write("   - Max Depth Level  : " + GlobalVars.limits.he_maxDepth);
				nsGlobalOutput.output.write("   - Max Seconds      : " + GlobalVars.limits.he_maxSeconds);
				nsGlobalOutput.output.write("   - Max Kb           : " + GlobalVars.limits.he_maxKb);
				nsGlobalOutput.output.write("   - Max HTTP Errors  : " + GlobalVars.limits.he_maxErrorCodes);

				if( GlobalVars.limits.he_regex_include_pages != "" )
					nsGlobalOutput.output.write("   - Include pages regex  : " + GlobalVars.limits.he_regex_include_pages );
				
				if( GlobalVars.limits.he_regex_exclude_pages != "" )
					nsGlobalOutput.output.write("   - Exclude pages regex  : " + GlobalVars.limits.he_regex_exclude_pages );
				
			}
			
			nsGlobalOutput.output.write("");
			
		}
		
	}
}
