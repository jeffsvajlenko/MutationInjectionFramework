// html.cs created with MonoDevelop
// User: shen139 at 10:56 18/06/2008
//

using System;
using System.Collections.Generic;
using System.Text;
using System.Text.RegularExpressions;
using System.Collections;

namespace OpenWebSpiderCS
{
    public class html : http
    {
        // FLAG settato dal META "robots"->"NOINDEX"
        public bool META_ROBOTS_INDEX = true;
        // FLAG settato dal META "robots"->"NOFOLLOW"
        public bool META_ROBOTS_FOLLOW = true;

        // regex usata per estrarre gli URL dalle pagine
        Regex regexExtractURLs = new Regex(
                        "(?<base><base\\s?[^>]*\\shref\\s*=\\s*(?:(?:[\\\"\\\'](?<url>[^\\\"\\\']*)[\\\"\\\'])|(?<url>[^\\s>]*))[^>]*[>])|"
                        + "(?<links><a\\s?[^>]*\\shref\\s*=\\s*(?:(?:[\\\"\\\'](?<url>[^\\\"\\\']*)[\\\"\\\'])|(?<url>[^\\s>]*))[^>]*[>](?<anchor>(.|\\s)*?)</a>)|"
                        + "(?<frames><i?frame\\s?[^>]*\\ssrc\\s*=\\s*(?:(?:[\\\"\\\'](?<url>[^\\\"\\\']*)[\\\"\\\'])|(?<url>[^\\s>]*))[^>]*[>])"
                        , RegexOptions.IgnoreCase | RegexOptions.Singleline );

        /*
         
         (?<images><img\s?[^>]*\ssrc\s*=\s*(?:(?:[\"\'](?<url>[^\"\']*)[\"\'])|(?<url>[^\s>]*))[^>]*[>])
         
         */
        Regex regexExtractImages = new Regex("(?<images><img\\s?[^>]*\\ssrc\\s*=\\s*(?:(?:[\\\"\\\'](?<url>[^\\\"\\\']*)[\\\"\\\'])|(?<url>[^\\s>]*))[^>]*[>])", RegexOptions.IgnoreCase);
        Regex regexExtractImagesALT = new Regex("alt\\s*=\\s*(?:(?:[\\\"\\\'](?<text>[^\\\"\\\']*)[\\\"\\\'])|(?<text>[^\\s>]*))", RegexOptions.IgnoreCase);
        Regex regexExtractImagesTITLE = new Regex("title\\s*=\\s*(?:(?:[\\\"\\\'](?<text>[^\\\"\\\']*)[\\\"\\\'])|(?<text>[^\\s>]*))", RegexOptions.IgnoreCase);

        /*
         (?<meta><meta\s?[^>]*\sname\s*=\s*(?:(?:[\"\'](?<name>[^\"\']*)[\"\'])|(?<name>[^\s>]*))[^>]*[>])
         */
        Regex regexExtractMETA = new Regex("(?<meta><meta\\s?[^>]*\\sname\\s*=\\s*(?:(?:[\\\"\\\'](?<name>[^\\\"\\\']*)[\\\"\\\'])|(?<name>[^\\s>]*))[^>]*[>])", RegexOptions.IgnoreCase);

		// rimuove tutte le occorrenze di un TAG da una pagina HTML
		public string removeTAG(string HTML, string startTAG, string endTAG)
		{
			int startTAGPos, endTAGPos;
			bool bAgain;
			
			do
			{
				bAgain = false;
				startTAGPos = HTML.IndexOf(startTAG,0 ,StringComparison.CurrentCultureIgnoreCase);
				if ( startTAGPos >= 0 )
				{
					endTAGPos = HTML.IndexOf(endTAG, startTAGPos + 1, StringComparison.CurrentCultureIgnoreCase);
					
					if( endTAGPos > startTAGPos )
					{
						HTML = HTML.Remove(startTAGPos, endTAGPos - startTAGPos + endTAG.Length );
						bAgain = true;						
					}
					
				}

                System.Threading.Thread.Sleep(0);
				
			} while( bAgain );
			
			return HTML;
		}

		/* removeHtmlEntity
		 * rimuove le HTML Entity &....;
		 */
		private string removeHtmlEntity(string s)
		{
			return Regex.Replace(s, "&[^;]{,10}?;", " ");
		}
		
		public string getTitle()
		{
			Regex regexExtractTitle = new Regex("<title>\\s*(?<title>[^<]*)\\s*</title>", RegexOptions.IgnoreCase);
			MatchCollection mcTitle = regexExtractTitle.Matches( HTML );
			
			foreach (Match mMatch in mcTitle)
			{
				return mMatch.Groups["title"].ToString();
			}
			
			return string.Empty;
		}

        public string checkMETA()
        {
            Regex regexExtractContent = new Regex("content\\s*=\\s*(?:(?:[\\\"\\\'](?<text>[^\\\"\\\']*)[\\\"\\\'])|(?<text>[^\\s>]*))", RegexOptions.IgnoreCase);

            MatchCollection mcMETA = regexExtractMETA.Matches(HTML);

            string META_CONTENT;

            foreach (Match mMatch in mcMETA)
            {
                META_CONTENT = string.Empty;

                MatchCollection mcMETA_CONTENT = regexExtractContent.Matches(mMatch.Groups["meta"].ToString());
                foreach (Match mMatchContent in mcMETA_CONTENT)
                {
                    META_CONTENT = mMatchContent.Groups["text"].ToString();
                }

                if (mMatch.Groups["name"].ToString().Equals("robots",StringComparison.CurrentCultureIgnoreCase)==true )
                { 
                    // Gestisci NOINDEX e NOFOLLOW
                    handleMETA_ROBOTS(META_CONTENT);
                }
                else if (mMatch.Groups["name"].ToString().Equals("description", StringComparison.CurrentCultureIgnoreCase) == true)
                {

                }
                else if (mMatch.Groups["name"].ToString().Equals("keywords", StringComparison.CurrentCultureIgnoreCase) == true)
                {
                
                }
            }

            return string.Empty;
        }

        public void handleMETA_ROBOTS(string meta_content)
        {
            string[] meta_robots_dir = meta_content.Split(',');

            for (int i = 0; i < meta_robots_dir.Length; i++)
            {
                if (meta_robots_dir[i].Trim().ToLower() == "noindex")
                    META_ROBOTS_INDEX = false;
                if (meta_robots_dir[i].Trim().ToLower() == "nofollow")
                    META_ROBOTS_FOLLOW = false;
            }

            return;
        }

        public string removeUnWantedChars(string HTML)
        {
            // sostituisce tutti i caratteri non parola con uno spazio
            HTML = Regex.Replace(HTML, "[^\\w;&#@\\.:/\\?]", " ");

            //sostituisce tutti gli spazi consecutivi con uno spazio singolo
            return Regex.Replace(HTML, "\\s+", " ");
        }

        // prende una pagina HTML e ne estrae il testo
        public string UnHTML(string HTML)
        {

			HTML = removeTAG(HTML,"<!--","-->");
			HTML = removeTAG(HTML,"<script","</script>");
			HTML = removeTAG(HTML,"<style","</style>");

            HTML = Regex.Replace(HTML, "<[^>]+?>", " ", RegexOptions.Multiline);
			
			// rimuove le entità HTML
			HTML = removeHtmlEntity( HTML );

            // remove i caratteri non validi e sostituisce tutti gli spazi consecutivi con uno spazio singolo
            HTML = removeUnWantedChars( HTML );

			HTML = HTML.Trim();
			
            return HTML;
        }

        /* GetURLs
         * estrae tutti gli URL e gli ANCHOR TEXT(dal TAG: A) dai seguenti TAG:
         *  - BASE ... HREF
         *  - A ... HREF
         *  - FRAME ... SRC
         *  - IFRAME ... SRC
         
            (?<base><base\s?[^>]*\shref\s*=\s*(?:(?:[\"\'](?<url>[^\"\']*)[\"\'])|(?<url>[^\s>]*))[^>]*[>])|(?<links><a\s?[^>]*\shref\s*=\s*(?:(?:[\"\'](?<url>[^\"\']*)[\"\'])|(?<url>[^\s>]*))[^>]*[>](?<anchor>(.|\s)*?)</a>)|(?<frames><i?frame\s?[^>]*\ssrc\s*=\s*(?:(?:[\"\'](?<url>[^\"\']*)[\"\'])|(?<url>[^\s>]*))[^>]*[>])
         
         * 
         *  Testo di prova:
         
            <base href="baseURL1"> <a href="url1"> an1 </a><a href=u2> a2 </a> <a href = u3 > a3 </a> <frame src="frame1">  <iframe src="iframe1">
         
         */
        public int GetURLs(string HTML,page parentPage)
        {
			int nRetURLs = 0;
			
			// gestisce il tag: BASE
			page baseURL = null;
			
            MatchCollection mcURLs = regexExtractURLs.Matches(HTML);
            try
            {
                foreach (Match mMatch in mcURLs)
                {
                    string strURL = mMatch.Groups["url"].ToString().Trim();

                    // toglie TAG HTML dall'anchor text (es.: <a href = test> <img src=abc> </a> )
                    string strAnchorText = UnHTML(mMatch.Groups["anchor"].ToString().Trim());

                    if (mMatch.Groups["base"].ToString().Length > 0 && strURL.Length > 0)
                    {
                        /* abbiamo un URL di un TAG <BASE> */
                        baseURL = new page(strURL, "base", null);
                        // se non è un URL valido lo ignora
                        if (!baseURL.isValidPage)
                        {
                            nsGlobalOutput.output.write("INVALID BASE URL FOUND: " + strURL);
                            baseURL = null;
                        }

                        //nsGlobalOutput.output.write("Base: " + strURL, true );
                    }

                    page pageNewPage;
                    if (baseURL == null)
                        pageNewPage = new page(strURL, strAnchorText, parentPage);
                    else
                    {
                        // se abbiamo trovato un tag BASE: consideralo la parentPage
                        pageNewPage = new page(strURL, strAnchorText, baseURL);
                    }

                    if (pageNewPage.isValidPage)
                    {

                        // incrementa il numero di URLs validi
                        nRetURLs++;

                        // salva la relazione corrente
                        GlobalVars.threadsVars.mutexRelsList.WaitOne();
                        try
                        {
                            GlobalVars.relsList.addRel(parentPage, pageNewPage);
                        }
                        catch (Exception e)
                        {
                            nsGlobalOutput.output.write("Error Adding Rel: " + e.Message);
                        }
                        finally
                        {
                            GlobalVars.threadsVars.mutexRelsList.ReleaseMutex();
                        }

                        // la pagina è del dominio corrente?
                        if (pageNewPage._hostname == GlobalVars.threadsVars.currentDomain._hostname &&
                            pageNewPage._port == GlobalVars.threadsVars.currentDomain._port)
                        {
                            // blocca il mutex associato alla lista, aggiunge l'URL e rilascia il mutex
                            GlobalVars.threadsVars.mutexAccessURLList.WaitOne();
                            try
                            {
                                GlobalVars.urlsLists.addURL(pageNewPage);
                            }
                            catch (Exception e)
                            {
                                nsGlobalOutput.output.write("Error Adding URL: " + e.Message);
                            }
                            finally
                            {
                                GlobalVars.threadsVars.mutexAccessURLList.ReleaseMutex();
                            }
                        }
                        else
                        {
                            // aggiunge host esterni SOLO SE specificato da riga di comando
                            if (GlobalVars.args.addExternalHosts == true)
                            {
                                // aggiunge l'URL alla cache degli host esterni
                                GlobalVars.threadsVars.mutexAccessExternURLList.WaitOne();
                                try
                                {
                                    GlobalVars.externUrlsLists.addURL(pageNewPage);
                                }
                                catch (Exception e)
                                {
                                    nsGlobalOutput.output.write("Error Adding External URL: " + e.Message);
                                }
                                finally
                                {
                                    GlobalVars.threadsVars.mutexAccessExternURLList.ReleaseMutex();
                                }
                            }

                        }

                    }

                }
            }
            catch(Exception e)
            {
                nsGlobalOutput.output.write("Error Adding External URL: " + e.Message);
            }

            // dobbiamo indicizzare le immagini?
            if (GlobalVars.args.indexImages == true)
            {
                MatchCollection mcImages = regexExtractImages.Matches(HTML);
                foreach (Match mMatch in mcImages)
                {
                    string strURL = mMatch.Groups["url"].ToString().Trim();

                    page pageNewPage;
				    if( baseURL == null )
					    pageNewPage = new page( strURL, "", parentPage );
				    else
				    {
					    // se abbiamo trovato un tag BASE: consideralo la parentPage
					    pageNewPage = new page( strURL, "", baseURL );
				    }

                    if (pageNewPage.isValidPage)
                    {

                        //estrai ALT e TITLE
                        string imgAlt = regexExtractImagesALT.Match( mMatch.Value ).Groups["text"].ToString();
                        string imgTitle = regexExtractImagesTITLE.Match(mMatch.Value).Groups["text"].ToString();

                        // blocca il mutex associato alla lista, aggiunge l'URL e rilascia il mutex
                        GlobalVars.threadsVars.mutexAccessURLList.WaitOne();
                        try
                        {
                            GlobalVars.imagesLists.addURL(parentPage, pageNewPage, imgAlt, imgTitle);
                        }
                        catch (Exception e)
                        {
                            nsGlobalOutput.output.write("Error Adding URL: " + e.Message);
                        }
                        finally
                        {
                            GlobalVars.threadsVars.mutexAccessURLList.ReleaseMutex();
                        }
                    }

                }
            }
			
			// ritorna il numero di URLs validi
			return nRetURLs;

        }

    }
}
