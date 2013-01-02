// lists.cs created with MonoDevelop
// User: shen139 at 15:21 20/06/2008
//

using System;
using System.Collections;

namespace GlobalVars
{

	
	// lista degli URL indicizzati e da indicizzare
	class urlsLists
    {
        private static volatile ArrayList l;
		private static volatile bool isInitialized = false;

        public static bool init()
        {
			try
			{
				l = new ArrayList();
				isInitialized = true;
			}
			catch
			{
				isInitialized = false;
			}
			
			return isInitialized;
        }
		
		public static void clear()
		{
			l.Clear();
		}
		
		/* addURL
		 * aggiunge un URL valido e univoco alla lista
		 */
		public static bool addURL(OpenWebSpiderCS.page p)
		{
			if( isInitialized )
			{
				// controlla che la pagina sia valida e che non violi il robots.txt
				if( p.checkAddablePage() )
				{
					
					// se è impostato un maxDepthLevel e il valore della pagina attuale lo supera: non aggiungere in lista
					// usa i limiti dal DB?
					if( GlobalVars.limits.useHostlist_Extras_limits == true )
					{
						if( GlobalVars.limits.he_maxDepth > -1 && p._depthLevel > GlobalVars.limits.he_maxDepth )
							return false;
					}
					else
					{
						// se siamo qui: usiamo i limiti passati da linea di comando o i default
						if( GlobalVars.limits.maxDepth > -1 && p._depthLevel > GlobalVars.limits.maxDepth )
							return false;
					}
					
					if( pageExists( p._hostname, p._page, p._port ) == false )
						l.Add( p );
				}
				else
					return false;
			}
			else
			{
				// lista non inizializzata!
				return false;
			}
			
			return true;
		}
		
		/* pageExists
		 * ritorna true o false se una determinata pagina (hostname+pagina+porta) esiste
		 */
		public static bool pageExists(string h, string p, uint port)
		{
			if( isInitialized && l.Count > 0 )
			{
				foreach( OpenWebSpiderCS.page __p in l )
				{
					if( __p._hostname == h && __p._page == p && __p._port == port )
						return true;
				}
			}

			return false;
		}		
		
		/* getPageByStatus
		 * ritorna un oggetto page della prima occorrenza di una pagina con stato == isIndexed o null se non esiste 
		 */
		public static OpenWebSpiderCS.page getPageByStatus(uint isIndexed)
		{
			if( isInitialized && l.Count > 0 )
			{
				foreach( OpenWebSpiderCS.page __p in l )
				{
					if( __p.isIndexed == isIndexed )
						return __p;
				}
			}

			return null;
		}
		
		/* count
		 * ritorna il numero di elementi di una lista
		 */
		public static uint count()
		{
			if( isInitialized )
				return (uint)l.Count;

			return 0;
		}
			
    }
	
	/* ********************************************************************** */
	
	// lista dei domini esterni (cache per evitare letture continue dal DB)
	class externUrlsLists
	{
		public static volatile ArrayList l;
		public static volatile bool isInitialized = false;

        public static bool init()
        {
			try
			{
				l = new ArrayList();
				isInitialized = true;
			}
			catch
			{
				isInitialized = false;
			}
			
			return isInitialized;
        }
		
		public static void clear()
		{
			l.Clear();
		}
		
		/* addURL
		 * aggiunge un URL valido e univoco alla lista
		 */
		public static bool addURL(OpenWebSpiderCS.page p)
		{
			if( isInitialized )
			{
				if( p.isValidPage )
				{
					if( pageExists( p._hostname, p._page, p._port ) == false )
						l.Add( p );
				}
				else
					return false;
			}
			else
			{
				// lista non inizializzata!
				return false;
			}
			
			return true;
		}
		
		/* pageExists
		 * ritorna true o false se una determinata pagina (hostname+pagina+porta) esiste
		 */
		public static bool pageExists(string h, string p, uint port)
		{
			if( isInitialized && l.Count > 0 )
			{
				foreach( OpenWebSpiderCS.page __p in l )
				{
					if( __p._hostname == h && __p._page == p && __p._port == port )
						return true;
				}
			}

			return false;
		}
		
		/* count
		 * ritorna il numero di elementi di una lista
		 */
		public static uint count()
		{
			if( isInitialized )
				return (uint)l.Count;

			return 0;
		}

	}
	
	/* ********************************************************************** */
	
	// lista dei Disallow trovati nel robots.txt
	class robotsTxtDisallows
	{
		public static volatile ArrayList disallowList;
		public static volatile bool isInitialized = false;

        public static bool init()
        {
			if( isInitialized )
				disallowList.Clear();
			
			try
			{
				disallowList = new ArrayList();
				isInitialized = true;
			}
			catch
			{
				isInitialized = false;
			}
			
			return isInitialized;
        }
	}
	
	/* *********************************************************************** */
	// lista delle relazioni: usata per evitare continue scritture sul DB
	class relsList
	{
		public static volatile ArrayList rels;
		public static volatile bool isInitialized = false;
		
		public class node
		{
			public OpenWebSpiderCS.page __page;
			public OpenWebSpiderCS.page __linkedPage;
			
			public node( OpenWebSpiderCS.page _p, OpenWebSpiderCS.page _linkedPage)
			{
				__page = _p;
				__linkedPage = _linkedPage;
			}
		}
		
        public static bool init()
        {
			if( isInitialized )
				rels.Clear();
			
			try
			{
				rels = new ArrayList();
				isInitialized = true;
			}
			catch
			{
				isInitialized = false;
			}
			
			return isInitialized;
        }
		
		public static bool addRel( OpenWebSpiderCS.page _p, OpenWebSpiderCS.page _linkedPage )
		{
			if( GlobalVars.args.relsMode == 1 || GlobalVars.args.relsMode == 2 )
			{
				node n = new node( _p, _linkedPage ); 
				if( relExists( n ) == false )
				{
                    if (n != null )
                    {
                        relsList.rels.Add(n);
                        return true;
                    }
				}
			}
			return false;
		}
		
		public static bool relExists(node _newNode)
		{
			if( GlobalVars.args.relsMode == 1 )
			{
				foreach( node __nodeInList in rels )
				{
					// se l'hostname linkato e linkante del nuovo nodo e di quello in lista corrispondono: non aggiungere
					if( __nodeInList.__linkedPage._hostname == _newNode.__linkedPage._hostname && 
					    __nodeInList.__page._hostname == _newNode.__page._hostname )
						return true;
				}
			}
			else if( GlobalVars.args.relsMode == 2 )
			{
				foreach( node __nodeInList in rels )
				{
					// se l'hostname e la pagina linkato e linkante del nuovo nodo e di quello in lista corrispondono: non aggiungere
					if( __nodeInList.__linkedPage._hostname == _newNode.__linkedPage._hostname &&
					    __nodeInList.__linkedPage._page == _newNode.__linkedPage._page && 
					    __nodeInList.__page._hostname == _newNode.__page._hostname &&
					    __nodeInList.__page._page == _newNode.__page._page )
						return true;
				}
			}
			else
				return true;  // ritorna sempre true se non c'è da salvare relazioni
			
			// il nodo non esiste: AGGIUNGILO
			return false;
		}
		
		/*public static void printRels()
		{
			nsGlobalOutput.output.write("Rels:", true);
			foreach( node __nodeInList in rels )
			{
				nsGlobalOutput.output.write( __nodeInList.__page.GenerateURL() + " links: " + __nodeInList.__linkedPage.GenerateURL() + " [ " + __nodeInList.__linkedPage._anchorText + " ]", true );
			}
		}*/
	}


    /* ********************************************************************** */

    // lista delle immagini (cache per evitare letture continue dal DB)
    class imagesLists
    {
        public static volatile ArrayList l;
        public static volatile bool isInitialized = false;

        public class imageStruct
        {
            public OpenWebSpiderCS.page srcPage;
            public OpenWebSpiderCS.page imagePage;
            public string alt_text;
            public string title_text;

            public imageStruct(OpenWebSpiderCS.page srcP, OpenWebSpiderCS.page imgP, string imgAlt, string imgTitle)
            { 
                srcPage = srcP; 
                imagePage = imgP;
                alt_text = imgAlt;
                title_text = imgTitle;
            }

        }

        public static bool init()
        {
            try
            {
                l = new ArrayList();
                isInitialized = true;
            }
            catch
            {
                isInitialized = false;
            }

            return isInitialized;
        }

        public static void clear()
        {
            l.Clear();
        }

        /* addURL
         * aggiunge un URL valido e univoco alla lista
         */
        public static bool addURL(OpenWebSpiderCS.page srcPage, OpenWebSpiderCS.page image, string imgAlt, string imgTitle)
        {
            if (isInitialized)
            {
                if (image.isValidPage)
                {
                    if (imageExists(image._hostname, image._page, image._port) == false)
                    {
                        l.Add(new imageStruct(srcPage, image, imgAlt, imgTitle));
                    }
                }
                else
                    return false;
            }
            else
            {
                // lista non inizializzata!
                return false;
            }

            return true;
        }

        /* pageExists
         * ritorna true o false se una determinata pagina (hostname+pagina+porta) esiste
         */
        public static bool imageExists(string h, string p, uint port)
        {
            if (isInitialized && l.Count > 0)
            {
                foreach (imageStruct __is in l)
                {
                    if (__is.imagePage._hostname == h && __is.imagePage._page == p && __is.imagePage._port == port)
                        return true;
                }
            }

            return false;
        }

    }
		
}
