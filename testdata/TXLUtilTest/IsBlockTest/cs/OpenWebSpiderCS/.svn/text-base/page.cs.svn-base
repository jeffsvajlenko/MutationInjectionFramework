// page.cs created with MonoDevelop
// User: shen139 at 11:35 25/06/2008
//

using System;
using System.Text.RegularExpressions;


namespace OpenWebSpiderCS
{
	
	
	public class page
	{
		
		public string _url;
        public string _hostname;
        public uint _port;
        public string _page;
        public string _anchorText;
		public string _title;
		
		/* _depthLevel
		 * home_page(0) -> (1) -> (2) -> ...
		 */
		public uint _depthLevel;

		// ID sul DB Server del'host corrente
		public int _hostID;
		
        public bool isValidPage;
		
		/* isIndexed == 0 :: da indicizzare
		 * isIndexed == 1 :: indicizzata
		 * isIndexed == 2 :: indicizzazione in corso
		 */
		public uint isIndexed;

		/* tests
			page pg1 = new page("http://www.google.com/prova/asd/deheh/uh12345/#segnalibro", "", null);
			page pg2 = new page("/test/prova.php", "", pg1);
			page pg3 = new page("prova.php3?deheheh#ciao", "", pg2);
			nsGlobalOutput.output.write("url: " + pg1._url + " host: " + pg1._hostname + " port: " + pg1._port + " page: " + pg1._page  );
			nsGlobalOutput.output.write("url: " + pg2._url + " host: " + pg2._hostname + " port: " + pg2._port + " page: " + pg2._page  );
			nsGlobalOutput.output.write("url: " + pg3._url + " host: " + pg3._hostname + " port: " + pg3._port + " page: " + pg3._page  );
		 * 
		 */
        public page(string u, string a, page parentPage)
        {
		
            isValidPage = false;
			isIndexed = 0;
			_hostID = 0;
			
			// protocolli non supportati
            if ( u.Length == 0 ||
                u.StartsWith("https:", StringComparison.CurrentCultureIgnoreCase) ||
                u.StartsWith("mailto:", StringComparison.CurrentCultureIgnoreCase) ||
                u.StartsWith("javascript:", StringComparison.CurrentCultureIgnoreCase) ||
                u.StartsWith("ftp:", StringComparison.CurrentCultureIgnoreCase) ||
                u.StartsWith("about:", StringComparison.CurrentCultureIgnoreCase) ||
                u.StartsWith("irc:", StringComparison.CurrentCultureIgnoreCase) ||
                u.StartsWith("news:", StringComparison.CurrentCultureIgnoreCase) )
			{
                isValidPage = false;
				return;
			}

			_url = u.Replace("&amp;", "&");
            _anchorText = a;
			
			/* inizialmente il titolo viene impostato come anchor_text;
			 * se poi è una pagina HTML verrà estratto dal TAG TITLE
			 */
			_title = a;
			
			/* _depthLevel
			 * se abbiamo una parent Page: _depthLevel = parentPage._depthLevel + 1
			 * altrimenti default = 0
			 */
			if( parentPage == null )
				_depthLevel = 0;
			else
				_depthLevel = parentPage._depthLevel + 1;
				
			// cerca un #segnalibro
			{
				int tSharp = _url.IndexOf("#");

				if( tSharp >= 0 )
					_url = _url.Remove( tSharp, _url.Length - tSharp );
			}
			
			if( _url == "" )
			{
				isValidPage = false;
				return;
			}
			
			// isFullURL è true se l'URL inizia con "http://" o "//"
			bool isFullURL = false;
			
			if( _url.StartsWith("http://", StringComparison.CurrentCultureIgnoreCase) )
			{
				_url = _url.Remove(0,7);
				isFullURL = true;
			}

			if( _url.StartsWith("//") )
			{
				_url = _url.Remove(0,2);
				isFullURL = true;
			}

			// se è un URL completo:
			if( isFullURL )
			{
				// - estraiamo dominio e pagina
				{
					int tToken1 = _url.IndexOf("/");
					if( tToken1 > 0 )
					{
						_hostname = _url.Substring(0, tToken1).ToLower();
						_page = _url.Substring(tToken1, _url.Length - tToken1);
					}
					else
					{
						_hostname = _url.ToLower();
						_page = "/";
					}
						
					_port = getPortNum( ref _hostname );
					
					isValidPage = true;
					
					// se la pagina genitore è la stessa dell'URL che stiamo parsando e ha un HostID?
					// assegna l'hostID evitando di cercarlo nel DB
					if( parentPage != null && parentPage._hostID > 0 && parentPage._hostname == _hostname )
						_hostID = parentPage._hostID;
					else
					{
						// l'hostID per gli URL esterni vengono trovati a fine indicizzazione del dominio corrente
					}

				}

			}
			else
			{
				// non è un URL completo ( senza http:// )
				
				// se non abbiamo una pagina genitore non possiamo continuare!!!
				if( parentPage != null )
				{
					_hostname = parentPage._hostname.ToLower();
					_port = parentPage._port;
					_hostID = parentPage._hostID;

					string baseDir = string.Empty;
					
					// se l'url non inizia con '/' dobbiamo cercare la directory dall'URL genitore (es.: url = "test.html")
					// altrimenti abbiamo la directory esatta nell'URL (es.: url = "/dir1/dir2/test.html")
					if( _url[0] != '/' )
					{
						baseDir = GetDir( parentPage._page );
					}
					
					_page = baseDir + _url;
					
					isValidPage = true;
					
				}
			}

            // se arriviamo qui e la pagina non è valida: non serve continuare!
            if (isValidPage == false)
                return;

			// normalizza la pagina
            normalizePage();

            // controllare la validità dell'hostname e della pagina
            Regex testHostname = new Regex("^[a-zA-Z0-9\\-\\.]+$", RegexOptions.IgnoreCase);
            if (!testHostname.IsMatch( _hostname ))
            {
                isValidPage = false;
                return;
            }
			
			// controlla la validità della pagina in base alla lunghezza dei campi
			if( _hostname.Length > GlobalVars.dbLimits.maxHostnameLength ||
          _hostname.Length < 2 ||
			    _page.Length > GlobalVars.dbLimits.maxPageLength ||
			    _title.Length > GlobalVars.dbLimits.maxTitleLength ||
			    _anchorText.Length > GlobalVars.dbLimits.maxAnchorText )
				isValidPage = false;

            return;
			
        }
		
		/* GetDir
         * Page -> dir <-
         * Page = "/dir1/dir2/page.htm" => dir = "/dir1/dir2/"
         */
        public string GetDir(string page)
        {
            string retDir = page;
            int lastSlash = 0 ;
            int hasQuestionMark = retDir.IndexOf('?');

            if (hasQuestionMark > 0)
                retDir = retDir.Substring(0, hasQuestionMark);

            lastSlash = retDir.LastIndexOf('/');

            retDir = (lastSlash == 0) ? "/" : retDir.Substring(0, lastSlash);

            if (retDir[retDir.Length - 1] != '/')
                retDir = retDir + "/";

            return retDir;
        }

        /* getPortNum
         * hostname -><-
         * hostname="www.auuuu.com:90" => hostname="www.auuuu.com"; return 90;
         */
        public uint getPortNum(ref string h)
        {
            int colonPos;
            uint uiPort = 80; /* porta di DEFAULT */

            colonPos = h.LastIndexOf(':');
            try
            {
                if (colonPos > 0)   /* c'è un ':' */
				{
                    if (colonPos > 1 && colonPos + 1 < h.Length)  /* c'è un ':' dopo il primo carattere e prima dell'ultimo */
                    {
                        uiPort = UInt32.Parse(h.Substring((colonPos + 1), h.Length - (colonPos + 1)));
						h = h.Remove(colonPos, h.Length - colonPos);
                    }
                    else
                    {
                        uiPort = 0;   /* URL non valido */
                    }
				}
            }
            catch
            {
                uiPort = 0; /* URL non valido */
            }

            return uiPort;  /* DEFAULT HTTP PORT */	
        }

        /* normalizePage
         * rimuove: "./" (/dir1/./././dir2/page.html) 
         * e normalizza: "/dir1/../dir2/dir3/../dir4/page.html"
         * 
         *  Reference: Remove Dot Segments [ http://tools.ietf.org/html/rfc3986#page-33 ]
         */
        /* test
         * 
                Page:     /dir1/..
                Reduced:  /


                Page:     /dir1/./../../../././../../
                Reduced:  /


                Page:     /dir1/dir2/dir3/../../
                Reduced:  /dir1/


                Page:     /../../../dir1/.././dir2/.././dir3/../..
                Reduced:  /


                Page:     /../../../dir1/.././dir2/.././dir3/../..
                Reduced:  /


                Page:     /../../../dir1/.././dir2/./dir3/./dir4/dir5.././dir6/../..
                Reduced:  /dir2/dir3/


                Page:     /dir1/dir2/dir3/dir4/dir5/../../../dir2/.././dir3/.././dir4/../..
                Reduced:  /dir1/
         */
        public bool normalizePage()
        {
            if (!isValidPage)
                return false;

            // rimuove "./" (ma non "../")
            for (int i = 1; i < _page.Length - 1; i++)
            {
                if (_page[i - 1] != '.' && _page[i] == '.' && _page[i + 1] == '/')
                {
                    _page = _page.Remove(i, 2);
                    i -= 2;
                }
                if (i < 0)
                    i = 0;
            }

            int dblPoint ;
            while( (dblPoint = _page.IndexOf("..")) > 0 )
            {
                // estrae la stringa con tutto ciò che precede i ".." - 1 (senza lo slash (se c'è uno slash))
                string tmpCurPath = _page.Substring(0, dblPoint - 1);

                // trova l'ultimo slash prima di dove ha trovato ".."
                int lastSlashBefore = tmpCurPath.LastIndexOf('/');

                if (lastSlashBefore >= 0)
                {
                    // rimuove dall'ultimo slash trovato (lastSlashBefore) + 1 (per preservare lo '/' stesso)
                    // fino a dove ha trovato i ".." + 2 ( "..".Length )
                    // (se c'è un'altro carattere dopo i ".." (presumibilmente uno '/'))) lo rimuove
                    if (_page.Length >= dblPoint + 3 )   // 3 = 2 ("..") + 1 ('/')
                        _page = _page.Remove(lastSlashBefore + 1, dblPoint - lastSlashBefore + 2);
                    else
                        _page = _page.Remove(lastSlashBefore + 1, dblPoint - lastSlashBefore + 1);
                    
                }
                else
                { 
                    // probabilmente il path è errato (es.: /../../../../../../dir1/page.html)
                    if (_page.Length > 3)
                        _page = _page.Remove(dblPoint, 3); // rimuove i ".." e il carattere successivo (probabilmente uno '/')
                    else
                        _page = _page.Remove(dblPoint, 2); // rimuove solo i ".."
                }
            }

            return true;
        }

        public string GenerateURL()
		{
			return "http://" + _hostname + ":" + _port + _page;
		}
		
		/* checkAddablePage
		 * controlla in base ai disallow e ad alcune estensioni conosciute
		 * se è possibile aggiungere la pagina alla lista di quelle da indicizzare
		 */
        public bool checkAddablePage()
		{
			if( !isValidPage )
				return false;
				
			if( GlobalVars.robotsTxtDisallows.isInitialized )
			{
				foreach( string __disallow in GlobalVars.robotsTxtDisallows.disallowList )
				{
					if( _page.StartsWith( __disallow, StringComparison.CurrentCultureIgnoreCase ) )
					{
						return false;
					}
				}
			}
			return true;
		}
		
		
	}
}
