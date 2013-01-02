// threading.cs created with MonoDevelop
// User: shen139 at 22:26 19/06/2008
//

using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.IO;
using System.Text.RegularExpressions;
using System.Collections;



namespace GlobalVars
{
	public class robotstxtVars
	{
		public static int robotstxtCrawlDelay;
	}
}

namespace OpenWebSpiderCS
{
    public class robots
    {

		public robots()
		{
			nsGlobalOutput.output.write(" + Parsing robots.txt...");
			GlobalVars.robotstxtVars.robotstxtCrawlDelay = 0;
			
			// svuota la lista dei Disallow
			GlobalVars.robotsTxtDisallows.init();
			
		}
		
        public bool parseRobotsTxt(string t)
        {
            /* robots.txt example file

	            User-agent: *
	            Disallow: /cgi-bin/
	            Disallow: /tmp/
	            Disallow: /private/

	            User-agent : OpenWebSpider
	            Crawl-Delay: 5
	            Disallow: /private/

            */

			string singleUserAgentArea = string.Empty;
			
            Regex regexExtractUserAgents = new Regex(
                            "(?<agents>User-agent[\\s\\t]*:[\\s\\t]*(?<robot>[^\\n\\r]+).*?(?=User-agent[\\s\\t]*:|$))"
                            , RegexOptions.IgnoreCase | RegexOptions.Singleline);
			
			for( int i=0; i < 2 ; i++ )
			{
	            MatchCollection mcUserAgents = regexExtractUserAgents.Matches(t);

	            /* nel primo ciclo controlla che ci siano i disallow per l'user-agent: "*"
	             * nel secondo ciclo controlla per l'user-agent specificato nel CRAWLER NAME e se c'è sovrascrive gli URL trovati in precedenza
	             */
	            foreach (Match mMatch in mcUserAgents)
	            {
					if ( i == 0 && String.Equals( mMatch.Groups["robot"].ToString().Trim(), "*", StringComparison.CurrentCultureIgnoreCase ))
	                {
	                    singleUserAgentArea = mMatch.Value;
	                }
					
	                if ( i == 1 && String.Equals( mMatch.Groups["robot"].ToString().Trim(), GlobalVars.OpenWebSpider.NAME, StringComparison.CurrentCultureIgnoreCase ))
	                {
	                    singleUserAgentArea = mMatch.Value;
	                }
	            }
			}
			
			// singleUserAgentArea qui conterrà il testo specifico per uno dei 2 user-agent trovati
			// regex per l'estrazione degli URL dei disallow:
			// disallow\s*:\s*(?<url>[^\n]*)
			
			Regex regexExtractDisallows = new Regex("disallow\\s*:\\s*(?<url>[^\\n]*)", RegexOptions.IgnoreCase);
			MatchCollection mcDisallows = regexExtractDisallows.Matches( singleUserAgentArea );
			
			foreach (Match mMatch in mcDisallows)
			{
				string __disallow = mMatch.Groups["url"].ToString().Replace("\r", "").Replace("\n", "").Trim();
                if (__disallow != "")
                {
                    GlobalVars.robotsTxtDisallows.disallowList.Add(__disallow);
                    nsGlobalOutput.output.write("   - Disallow [" + __disallow + "] ");
                }
			}
			
			// estrae il Crawl-Delay
			// Crawl-Delay\s*:\s*(?<crawldelay>[^\D]*)
			Regex regexExtractCrawlDelay = new Regex("crawl-delay\\s*:\\s*(?<crawldelay>[^\\D]*)", RegexOptions.IgnoreCase);
			MatchCollection mcCrawlDelay = regexExtractCrawlDelay.Matches( singleUserAgentArea );
			
			foreach (Match mMatch in mcCrawlDelay)
			{
				try
				{
					GlobalVars.robotstxtVars.robotstxtCrawlDelay = int.Parse( mMatch.Groups["crawldelay"].ToString() );
					nsGlobalOutput.output.write("   - Crawl-Delay: " + GlobalVars.robotstxtVars.robotstxtCrawlDelay + " seconds ");
				}
				catch
				{
					GlobalVars.robotstxtVars.robotstxtCrawlDelay = 0;
				}
			}
			
            return true;
        }
    }
}
