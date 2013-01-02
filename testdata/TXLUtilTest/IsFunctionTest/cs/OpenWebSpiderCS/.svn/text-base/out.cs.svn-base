// out.cs created with MonoDevelop
// User: shen139 at 12:36 18/06/2008
//

using System;

namespace nsGlobalOutput
{

    public class output
    {
        private static int type;
        /*
         * type == 1 -> console
         * type == 2 -> file (TODO)
         * ... (TODO)
         */
        private static bool debugMode;

        public static bool init(int t, bool b)
        {
            type = t;
            debugMode = b;

            return true;
        }

		public static void handleCTRLC()
		{
            if (type == 1)
            {
                // rimuove l'handler di CTRL+C
                Console.TreatControlCAsInput = false;

                // assegna un nuovo handler
                Console.CancelKeyPress += new ConsoleCancelEventHandler(CTRLCHandler);

                write("\n\n == Press CTRL+C to quit gracefully ==\n\n");
            }
		}

		protected static void CTRLCHandler( object sender, ConsoleCancelEventArgs args )
		{
			// previene l'uscita
			args.Cancel = true;

			// setta il segnale di uscita
			GlobalVars.OpenWebSpider.stopItGracefully = true;
			
			write( "\n\n == Caught CTRL+C: Quitting gracefully! ==\n\n" );
			
		}
		
        public static void write(string s)
        {
            write(s, false);
        }

        public static void write(string s, bool isDebug)
        {
            /* se non è una stringa di debug o lo è e sono ammessi i messaggi di debug*/
            if (isDebug == false || (isDebug == true && debugMode == true))
                if (type == 1)
                {
                    Console.WriteLine(s);
                }
        }
    }
}
