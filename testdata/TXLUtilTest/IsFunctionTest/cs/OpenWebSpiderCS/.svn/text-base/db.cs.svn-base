// db.cs created with MonoDevelop
// User: shen139 at 15:05 29/06/2008
//

using System;
using System.Text.RegularExpressions;
using System.Text;

namespace GlobalVars
{

	public class dbLimits
	{
		// lunghezze massime dei campi sul DB
		public static int maxHostnameLength = 100;
		public static int maxPageLength = 255;
		public static int maxTitleLength = 255;
		public static int maxAnchorText = 255;
	}
}

namespace OpenWebSpiderCS
{
	
	public class db
	{
		
		public string myMySQLEscapeString(string s)
		{
            return s.Replace("\\", "\\\\").Replace("'", "\\'");
		}

		public page getFirstAvailableURL()
		{
			int hostID;
			string url;
			
			try
			{
                if(GlobalVars.readConfFile.sqlWhereHostlist == "")
                    GlobalVars.readConfFile.sqlWhereHostlist = "1=1";

				// prende l'ID del primo url libero
				hostID = int.Parse(GlobalVars.mysqlConn.connHostList.getValueFromTable("hostlist", "id", "status=0 AND ( " + GlobalVars.readConfFile.sqlWhereHostlist + " ) ORDER by priority DESC"));
				if( hostID > 0 )
				{
					// setta l'url come "in scansionamento"
                    GlobalVars.mysqlConn.connHostList.executeSQLQuery("UPDATE hostlist SET status = 2, crawler_id = '" + myMySQLEscapeString( GlobalVars.OpenWebSpider.ID ) + "' WHERE id = " + hostID);
					
					// prende l'url
					url = GlobalVars.mysqlConn.connHostList.getValueFromTable("hostlist", "CONCAT('http://', hostname, ':', port)", "id = " + hostID);

					nsGlobalOutput.output.write("\n\n\n");
					
					nsGlobalOutput.output.write(" + Indexing new domain:  " + url);
					
					nsGlobalOutput.output.write("\n");
					
					page ret = new page(url, url, null);
					ret._hostID = hostID;
					
					return ret;
				}
			}
			catch
			{
				return null;
			}
			
			return null;
		}
		
		public int GetHostId(page p)
		{
			int hostID;

            try
			{
				// prende l'ID del primo url libero
				hostID = int.Parse(GlobalVars.mysqlConn.connHostList.getValueFromTable("hostlist", "id", " hostname = '" + p._hostname + "' AND port = "+ p._port +" "));
				if( hostID > 0 )
					return hostID;
			}
			catch
			{
			}

			return 0;
		}
		
		/* startIndexThisSite
		 * inserisce il dominio corrente nel DB se non esiste altrimenti aggiorna lo stato(=2)
		 * ritorna l'hostID
		 */ 
		public int startIndexThisSite(page p)
		{

			// prova a recuperare l'host_id dal DB
			if( p._hostID == 0 )
				p._hostID = GetHostId( p );
			
			// definisce lo stato "Indicizzazione in corso"
			int status = 2;
			
			// se siamo in sola aggiunta dell'host: mettilo come "Da indicizzare"
			if( GlobalVars.args.add2Hostlist == true )
				status = 0;
			
			// se l'host non è presente nel DB: inseriscilo
			if( p._hostID == 0 )
			{
                GlobalVars.mysqlConn.connHostList.executeSQLQuery("INSERT IGNORE INTO hostlist (hostname, port, status, lastvisit, crawler_id) VALUES('" + myMySQLEscapeString(p._hostname) + "', " + p._port + ", " + status + ", curdate(), '" + myMySQLEscapeString(GlobalVars.OpenWebSpider.ID) + "')");
				p._hostID = GetHostId( p );
			}
			else
                GlobalVars.mysqlConn.connHostList.executeSQLQuery("UPDATE hostlist SET status = " + status + ", lastvisit=curdate(), crawler_id = '" + myMySQLEscapeString(GlobalVars.OpenWebSpider.ID) + "' WHERE id = " + p._hostID + " limit 1");
			
			// se siamo in sola aggiunta dell'host: non eliminare l'indice
			if( GlobalVars.args.add2Hostlist == true )
				return p._hostID;
			

			// == eliminazione record dell'indice ==
			
			// a questo punto hostID è sicuramente settato
			// lo usiamo per eliminare le vecchie pagine indicizzate e le relazioni
			if( p._hostID != 0 )
			{
				// elimina le pagine
				GlobalVars.mysqlConn.connPageList.executeSQLQuery("DELETE FROM pages where host_id = " + p._hostID);
				
				// elimina le relazioni
				GlobalVars.mysqlConn.connHostList.executeSQLQuery("DELETE FROM rels where host_id = " + p._hostID);

                // se stiamo indicizzando le immagini: le rimuove
                if( GlobalVars.args.indexImages == true )
                    GlobalVars.mysqlConn.connPageList.executeSQLQuery("DELETE FROM images where src_host_id = " + p._hostID);

                // se stiamo indicizzando gli MP3: rimuovili
                if (GlobalVars.args.indexMP3 == true)
                    GlobalVars.mysqlConn.connPageList.executeSQLQuery("DELETE FROM mp3 where host_id = " + p._hostID);

                // se stiamo indicizzando i PDF: rimuovili
                if (GlobalVars.args.indexPDF == true)
                    GlobalVars.mysqlConn.connPageList.executeSQLQuery("DELETE FROM pdf where host_id = " + p._hostID);
			}
			
			return p._hostID;
		}
		
		/* stopIndexThisSite
		 * imposta il dominio corrente come: "indicizzato" (status=1)
		 */
		public bool stopIndexThisSite(page p)
		{
			if( p._hostID > 0 )
				GlobalVars.mysqlConn.connHostList.executeSQLQuery(" UPDATE hostlist SET " + 
				                                                  "  status = 1 " +
				                                                  ", indexed_pages = " + GlobalVars.limits.curPages +
				                                                  ", time_sec = " + ((DateTime.Now.Ticks - GlobalVars.limits.startTime) / 10000000) +
				                                                  ", bytes_downloaded = " + GlobalVars.limits.curBytes +
				                                                  ", error_pages = " + GlobalVars.limits.curErrorCodes +
                                                                  ", crawler_id = '' " +
				                                                  " WHERE id = " + p._hostID + " limit 1");
			else
				return false;
			
			return true;
		}
		
		public bool indexThisPage(page p, html h)
		{

            // se negli argomenti è specificato di non indicizzare: esci
            if ( GlobalVars.args.noIndex )
                return false;

			string text2Index = string.Empty;
			
			try
			{
				// controlla che le regex permettano l'indicizzazione di questa pagina
				// 1. stiamo usano hostlist_extras?
				if( GlobalVars.limits.useHostlist_Extras_limits == true )
				{
					// 2. c'è una regex valida per le inclusioni?
					if( GlobalVars.limits.he_regex_include_pages != "" )
					{
						Regex testIncludePageRegex = new Regex( GlobalVars.limits.he_regex_include_pages, RegexOptions.IgnoreCase);
						// se la pagina corrente NON VERIFICA l'espressione regolare: non indicizzare
						if( !testIncludePageRegex.IsMatch( p._page ) )
							return false;
					}

					// 3. c'è una regex valida per le esclusioni?
					if( GlobalVars.limits.he_regex_exclude_pages != "" )
					{
						Regex testExcludePageRegex = new Regex( GlobalVars.limits.he_regex_exclude_pages, RegexOptions.IgnoreCase);
						// se la pagina corrente VERIFICA l'espressione regolare: non indicizzare
						if( testExcludePageRegex.IsMatch( p._page ) )
							return false;
					}
				}
			}
			catch(Exception e)
			{
				nsGlobalOutput.output.write("\n\n + Error while parsing hostlist_extras regex: " + e.Message + "\n\n");
			}

            if (p.isValidPage && h.HTML != "")
            {
                if (h.contentType.StartsWith("text/html", StringComparison.CurrentCultureIgnoreCase))
                {
                    text2Index = h.UnHTML(h.HTML);
                }
                else if (h.contentType.StartsWith("text/", StringComparison.CurrentCultureIgnoreCase))
                {
                    // indicizza il testo (es.: *.txt; *.c; *.h)
                    text2Index = h.removeUnWantedChars(h.HTML).Trim();
                }
                else
                    return false;

                return addContent2Index(p._hostID, p._hostname, p._page, p._title, p._anchorText, p._depthLevel, text2Index, h.HTML);
            }
            return false;
		}

        public bool addContent2Index(int i_host_id, string i_hostname, string i_page, string i_title, string i_anchorText, uint i_depthLevel, string i_text, string i_cache)
        {
            bool ret = true;
            md5 MD5 = new md5();

            string sql = "INSERT DELAYED INTO pages SET " +
                        "  host_id = " + i_host_id +
                        ", hostname = '" + myMySQLEscapeString(i_hostname) + "'" +
                        ", page='" + myMySQLEscapeString(i_page) + "'" +
                        ", title='" + myMySQLEscapeString(i_title) + "'" +
                        ", anchor_text='" + myMySQLEscapeString(i_anchorText) + "'" +
						", date=curdate(),time=curtime()" +
                        ", level=" + i_depthLevel +
                        ",`text`= '" + myMySQLEscapeString(i_text) + "'";


            sql += ",`html_md5`= '" + MD5.compute(i_text) + "' ";


            if(  GlobalVars.args.cachingMode == 1 )
                sql += ",`cache`= '" + myMySQLEscapeString(i_cache) + "'";
            else if( GlobalVars.args.cachingMode == 2 )
                sql += ",`cache`= COMPRESS('" + myMySQLEscapeString(i_cache) + "')";

            GlobalVars.threadsVars.mutexMySQLPageList.WaitOne();
            try
            {
                ret = GlobalVars.mysqlConn.connPageList.executeSQLQuery(sql);
            }
            catch(Exception e)
            {
                nsGlobalOutput.output.write("SQL Error: " + e.Message + "\n\nSQL: -===[\n" + sql.Substring(0,1000) + "\n]===-\n\n");
                ret = false;
            }
            finally
            {
                GlobalVars.threadsVars.mutexMySQLPageList.ReleaseMutex();
            }
            return ret;
        }
		
		/* swapExternURLs2DB
		 * riversa gli URL esterni trovati nel DB
		 */
		public bool swapExternURLs2DB()
		{
            nsGlobalOutput.output.write(" + Swapping External URLs to DB [ " + GlobalVars.externUrlsLists.l.Count + " ]...");
			
			if( GlobalVars.externUrlsLists.isInitialized && GlobalVars.externUrlsLists.l.Count > 0)
			{
				foreach( page __p in GlobalVars.externUrlsLists.l )
				{
					GlobalVars.mysqlConn.connHostList.executeSQLQuery("INSERT IGNORE INTO hostlist (hostname, port, status, lastvisit) " +
					                                                  "VALUES('" + __p._hostname + "', " + __p._port +  ", 0, null)");
				}
				return true;
			}
			
			return false;
		}
		
		/* saveRels
		 * salva le relazioni su DB
		 */
		public bool saveRels()
		{

            nsGlobalOutput.output.write(" + Saving rels [ " + GlobalVars.relsList.rels.Count + " ]...");
			
			foreach( GlobalVars.relsList.node __nodeInList in GlobalVars.relsList.rels )
			{
				if( __nodeInList.__linkedPage._hostID == 0 )
					__nodeInList.__linkedPage._hostID = GetHostId( __nodeInList.__linkedPage );

				// aggiunge solo URL di cui esiste un host_id
				// può esistere nel caso non si aggiungano host esterni
				if( __nodeInList.__linkedPage._hostID > 0 )
				{
					if( GlobalVars.args.relsMode == 1 )
					{
						GlobalVars.mysqlConn.connHostList.executeSQLQuery("INSERT IGNORE INTO rels (host_id, page, linkedhost_id, linkedpage, textlink) " +
						                                                  "VALUES('" + __nodeInList.__page._hostID + "', '/', " +  __nodeInList.__linkedPage._hostID +  ", '/', '" + myMySQLEscapeString( __nodeInList.__linkedPage._anchorText ) + "')");
					}
					else if( GlobalVars.args.relsMode == 2)
					{
						GlobalVars.mysqlConn.connHostList.executeSQLQuery("INSERT IGNORE INTO rels (host_id, page, linkedhost_id, linkedpage, textlink) " +
						                                                  "VALUES('" + __nodeInList.__page._hostID + "', '" + myMySQLEscapeString( __nodeInList.__page._page ) + "', " +  __nodeInList.__linkedPage._hostID +  ", '" + myMySQLEscapeString( __nodeInList.__linkedPage._page ) + "', '" + myMySQLEscapeString( __nodeInList.__linkedPage._anchorText ) + "')");
					}
				}
			}
			
			return false;
		}

		/* deleteDuplicatedPages
		 * 
		 * elimina tutte le pagine per l'host_id corrente che hanno un MD5 duplicato lasciando solo la pagina con id minore
		 * 
		 * es.: se indicizza www.example.com/ e www.example.com/index.html -> rimuoverà: www.example.com/index.html 
		 *
		 * 
		 * delete from pages 
		 * where host_id = 2104 and id not in 
		 * (
		 *   select id from view_unique_pages where host_id = 2104
		 * )
		 * 
		 * 
		 * =======================
		 * 
		 * CREATE
		 * VIEW `ows_index`.`view_unique_pages` 
		 * AS
		 * (
		 *		select min(id) as id, host_id
		 *		from pages
		 *		group by html_md5
		 * )
		 * 
		 */
		public void deleteDuplicatedPages( int host_id )
		{
			nsGlobalOutput.output.write(" + Deleting duplicated pages...");
			
			GlobalVars.mysqlConn.connPageList.executeSQLQuery(" DELETE FROM pages " + 
			                                                  " WHERE host_id = " + host_id + " AND id NOT IN " +
			                                                  " ( " +
			                                                  "   SELECT id FROM view_unique_pages WHERE host_id = " + host_id +
			                                                  " )");
			return;
		}

        /* saveImages
		 * riversa le immagini trovati nel DB
		 */
        public bool saveImages()
        {
            if (GlobalVars.args.indexImages == false)
                return false;

            nsGlobalOutput.output.write(" + Saving images [ " + GlobalVars.imagesLists.l.Count + " ]...");

            if (GlobalVars.imagesLists.isInitialized && GlobalVars.imagesLists.l.Count > 0)
            {
                foreach (GlobalVars.imagesLists.imageStruct __is in GlobalVars.imagesLists.l)
                {
                    // l'host id dell'immagine non è settato?
                    if (__is.imagePage._hostID == 0)
                    {
                        __is.imagePage._hostID = GetHostId(__is.imagePage);

                        // se l'host_id è ancora 0 allora dobbiamo inserire l'host in hostlist
                        if (__is.imagePage._hostID == 0)
                        {
                            GlobalVars.mysqlConn.connHostList.executeSQLQuery("INSERT IGNORE INTO hostlist (hostname, port, status, lastvisit) VALUES('" + myMySQLEscapeString(__is.imagePage._hostname) + "', " + __is.imagePage._port + ", 0, curdate())");
                            __is.imagePage._hostID = GetHostId( __is.imagePage );
                        }
                    }

                    // qui l'host_id deve per forza essere impostato! se non lo è c'è stato un errore!
                    if (__is.imagePage._hostID > 0)
                    {
                        GlobalVars.mysqlConn.connPageList.executeSQLQuery("INSERT INTO images (src_host_id, src_page, image_host_id, image, alt_text, title_text) " +
                                                                         "VALUES(" + __is.srcPage._hostID + ", " +
                                                                         "'" + myMySQLEscapeString(__is.srcPage._page) + "', " +
                                                                         __is.imagePage._hostID + ", " +
                                                                         "'" + myMySQLEscapeString(__is.imagePage._page) + "', " +
                                                                         "'" + myMySQLEscapeString(__is.alt_text) + "', " +
                                                                         "'" + myMySQLEscapeString(__is.title_text) + "')");
                    }
                }
                return true;
            }

            return false;
        }

        /* indexPDF
         * indicizza le informazioni estratte dal file PDF
         */
        public bool indexPDF(page p, int pdfSize, string pdf2text)
        {
            string outStr;
            string sql;
            bool ret;

            outStr = "\n";
            outStr += " + Indexing PDF [ " + p.GenerateURL() + " ]\n";

            nsGlobalOutput.output.write(outStr);

            html htmp = new html();

            pdf2text = htmp.removeUnWantedChars(pdf2text);

            addContent2Index(p._hostID, p._hostname, p._page, p._title, p._anchorText, p._depthLevel, pdf2text, pdf2text);

            sql = "INSERT INTO pdf (host_id, filename, pdf_size, pdf_text) " +
                                     "VALUES(" + p._hostID + ", " +
                                     "'" + myMySQLEscapeString(p._page) + "', " +
                                     "'" + pdfSize + "', " +
                                     "'" + myMySQLEscapeString(pdf2text) + "') ";


            GlobalVars.threadsVars.mutexMySQLPageList.WaitOne();
            try
            {
                ret = GlobalVars.mysqlConn.connPageList.executeSQLQuery(sql);
            }
            catch (Exception e)
            {
                nsGlobalOutput.output.write("SQL Error: " + e.Message + "\n\nSQL: -===[\n" + sql.Substring(0, 1000) + "\n]===-\n\n");
                ret = false;
            }
            finally
            {
                GlobalVars.threadsVars.mutexMySQLPageList.ReleaseMutex();
            }

            return ret;
        }

        /* indexMP3
         * indicizza le informazioni estratte dal file MP3
         */
        public bool indexMP3( page p, mp3 MP3Info)
        {
            string outStr;
            string sql;
            bool ret;

            outStr = "\n";
            outStr += " + Indexing MP3 [ " + p.GenerateURL() + " ]\n";
            outStr += "   - Title   : " + MP3Info.mp3Title + "\n";
            outStr += "   - Artist  : " + MP3Info.mp3Artist + "\n";
            outStr += "   - Album   : " + MP3Info.mp3Album + "\n";
            outStr += "   - Genre   : " + MP3Info.mp3Genre + "\n";
            outStr += "   - Duration: " + MP3Info.mp3Length + " seconds\n";
            outStr += "\n";

            nsGlobalOutput.output.write(outStr);

            sql = "INSERT INTO mp3 (host_id, filename, mp3_size, mp3_artist, mp3_title, mp3_album, mp3_genre, mp3_duration) " +
                                     "VALUES(" + p._hostID + ", " +
                                     "'" + myMySQLEscapeString(p._page) + "', " +
                                     "'" + MP3Info.mp3Size + "', " +
                                     "'" + myMySQLEscapeString(MP3Info.mp3Artist) + "', " +
                                     "'" + myMySQLEscapeString(MP3Info.mp3Title) + "', " +
                                     "'" + myMySQLEscapeString(MP3Info.mp3Album) + "', " +
                                     "'" + myMySQLEscapeString(MP3Info.mp3Genre) + "', " +
                                     MP3Info.mp3Length + ")";


            GlobalVars.threadsVars.mutexMySQLPageList.WaitOne();
            try
            {
                ret = GlobalVars.mysqlConn.connPageList.executeSQLQuery(sql);
            }
            catch (Exception e)
            {
                nsGlobalOutput.output.write("SQL Error: " + e.Message + "\n\nSQL: -===[\n" + sql.Substring(0, 1000) + "\n]===-\n\n");
                ret = false;
            }
            finally
            {
                GlobalVars.threadsVars.mutexMySQLPageList.ReleaseMutex();
            }

            return ret;
        }


        /* checkCrawlerAct
         * controlla se c'è da fare qualcosa
         * 
         * GlobalVars.mysqlConn.connHostList non è protetto da mutex perchè non ci sono altri thread che lo usano
         * 
         */
        public void checkCrawlerAct()
        {
            int act = 0;
            try
            {
                act = int.Parse(GlobalVars.mysqlConn.connHostList.getValueFromTable("crawler_act", "act", " crawler_id = '" + myMySQLEscapeString(GlobalVars.OpenWebSpider.ID) + "' "));
            }
            catch
            {
                act = 0;
            }
            finally
            {

                if( act == 1 ) /* exit */
                {
                    // setta il segnale di uscita
			        GlobalVars.OpenWebSpider.stopItGracefully = true;

                    //aspetta un secondo per permettere a altri crawler con lo stesso crawler_id di leggere l'info
                    System.Threading.Thread.Sleep(1000);

                    //mette uno stato neutro
                    GlobalVars.mysqlConn.connHostList.executeSQLQuery("UPDATE crawler_act SET act = 0 WHERE crawler_id = '" + myMySQLEscapeString(GlobalVars.OpenWebSpider.ID) + "' LIMIT 1 ");
                }
                else if (act == 2) /* pause */
                {
                    GlobalVars.OpenWebSpider.crawlerActPAUSE = true;
                }
                else
                { 
                    // 0 o riga inesistente
                    // togli azioni in sospeso
                    GlobalVars.OpenWebSpider.crawlerActPAUSE = false;
                }

            }
            return;
        }


	}
}

