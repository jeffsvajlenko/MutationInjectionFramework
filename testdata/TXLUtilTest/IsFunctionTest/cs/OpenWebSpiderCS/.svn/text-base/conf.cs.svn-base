// conf.cs created with MonoDevelop
// User: shen139 at 14:32 21/06/2008
//

using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace GlobalVars
{
    class readConfFile
    {
        public static bool isConfFileParsed = false;
        public static string databaseHostList = String.Empty;
        public static string databasePageList = String.Empty;
        public static string mysqlServerHostList = String.Empty;
        public static string mysqlServerPageList = String.Empty;
        public static string mysqlServerPort1 = String.Empty;
        public static string mysqlServerPort2 = String.Empty;
        public static string mysqlUserNameHostList = String.Empty;
        public static string mysqlUserNamePageList = String.Empty;
        public static string mysqlPassWordHostList = String.Empty;
        public static string mysqlPassWordPageList = String.Empty;
        public static string owsServerPassWord = String.Empty;
        public static string sqlWhereHostlist = String.Empty;

        public static void parseFile(String filename)
        {

			nsGlobalOutput.output.write(" + Parsing Configuration File  [ " + filename +  " ]");

            /* se il file specificato non esiste, esce */
            if (!File.Exists(filename))
            {
                nsGlobalOutput.output.write( "   - " + filename + " not found!");
                return;
            }

            String line;
            String[] arguments;
            int iLine = 0;
            int iErrorNumber = 0;
            int sep;

            try
            {
                //apre il file in lettura
                StreamReader sr = new StreamReader(filename);

                //continua fino alla fine del file (EOF)
                do
                {
                    line = sr.ReadLine();

                    if (line == null)
                        continue;

                    iLine++;

                    /* rimuove gli spazi a inizio e fine */
                    line = line.Trim();

                    /* controlla che la riga non sia un commento o una riga vuota */
                    if (line.Length == 0)
                        continue;

                    if (line[0] == '#' || line[0] == '\r' || line[0] == '\n' || line[0] == 0)
                        continue;

                    sep = line.IndexOf('=');

                    if (sep < 0)
                    {
                        iErrorNumber++;
                        nsGlobalOutput.output.write("   - Error while parsing " + filename + " (Line: " + iLine + ")");
                        continue;
                    }

                    arguments = new String[2];

                    arguments[0] = line.Substring(0, sep);
                    arguments[1] = line.Substring(sep + 1, line.Length - sep - 1);

                    /* abbiamo 2 argomenti: chiave e valore */

                    /* togli spazi a inizio e fine */
                    arguments[0] = arguments[0].Trim();
                    arguments[1] = arguments[1].Trim();

                    /* legge il parametro */
                    if (arguments[0] == "mysqlserver1")
                    {
                        mysqlServerHostList = arguments[1];
                        nsGlobalOutput.output.write("   - Server[1]: " + arguments[1]);
                    }
                    else if (arguments[0] == "mysqlserver2")
                    {
                        mysqlServerPageList = arguments[1];
                        nsGlobalOutput.output.write("   - Server[2]: " + arguments[1]);
                    }
                    else if (arguments[0] == "port1")
                    {
                        mysqlServerPort1 = arguments[1];
                        nsGlobalOutput.output.write("   - Server[1] port: " + arguments[1]);
                    }
                    else if (arguments[0] == "port2")
                    {
                        mysqlServerPort2 = arguments[1];
                        nsGlobalOutput.output.write("   - Server[2] port: " + arguments[1]);
                    }
                    else if (arguments[0] == "db1")
                    {
                        databaseHostList = arguments[1];
                        nsGlobalOutput.output.write("   - Database[1]: " + arguments[1]);
                    }
                    else if (arguments[0] == "db2")
                    {
                        databasePageList = arguments[1];
                        nsGlobalOutput.output.write("   - Database[2]: " + arguments[1]);
                    }
                    else if (arguments[0] == "userdb1")
                    {
                        mysqlUserNameHostList = arguments[1];
                        nsGlobalOutput.output.write("   - Username[1]: " + arguments[1]);
                    }
                    else if (arguments[0] == "userdb2")
                    {
                        mysqlUserNamePageList = arguments[1];
                        nsGlobalOutput.output.write("   - Username[2]: " + arguments[1]);
                    }
                    else if (arguments[0] == "passdb1")
                    {
                        mysqlPassWordHostList = arguments[1];
                        nsGlobalOutput.output.write("   - Password[1]: ******");
                    }
                    else if (arguments[0] == "passdb2")
                    {
                        mysqlPassWordPageList = arguments[1];
                        nsGlobalOutput.output.write("   - Password[2]: ******");
                    }
                    else if (arguments[0] == "crawler_name")
                    {
                        GlobalVars.OpenWebSpider.NAME = arguments[1];
                        nsGlobalOutput.output.write("   - Crawler Name: " + GlobalVars.OpenWebSpider.NAME);
                    }
                    else if (arguments[0] == "crawler_version")
                    {
                        GlobalVars.OpenWebSpider.VERSION = arguments[1];
                        nsGlobalOutput.output.write("   - Crawler Version: " + GlobalVars.OpenWebSpider.VERSION);
                    }
                    else if (arguments[0] == "crawler_id")
                    {
                        GlobalVars.OpenWebSpider.ID = arguments[1];
                        nsGlobalOutput.output.write("   - Crawler ID: " + GlobalVars.OpenWebSpider.ID);
                    }
                    else if (arguments[0] == "sql_hostlist_where")
                    {
                        sqlWhereHostlist = arguments[1];
                        nsGlobalOutput.output.write("   - sql_hostlist_where: " + sqlWhereHostlist);
                    }
                    else
                    {
                        iErrorNumber++;
                        nsGlobalOutput.output.write("\n   - Error while parsing " + filename + " (Line: " + iLine + ")");
                    }

                } while (line != null);

                //chiude il file
                sr.Close();

            }
            catch (Exception e)
            {
                nsGlobalOutput.output.write("   - Exception: " + e.Message);
                return;
            }
            finally
            {
                /* se non sono stati trovati errori: preocedi col parsing! */
                if (iErrorNumber == 0)
                {
                    /* controlla che tutti i campi richiesti siano stati passati */
                    if ( databaseHostList.Length > 0 &&
                         databasePageList.Length > 0 &&
                         mysqlServerHostList.Length > 0 &&
                         mysqlServerPageList.Length > 0 &&
                         mysqlUserNameHostList.Length > 0 &&
                         mysqlUserNamePageList.Length > 0)
                    {
                        /* se siamo qui: il parsing è avvenuto correttamente */
                        isConfFileParsed = true;

                        /* imposta l'USERAGENT */
                        GlobalVars.OpenWebSpider.USERAGENT = GlobalVars.OpenWebSpider.NAME + " v" + GlobalVars.OpenWebSpider.VERSION + " (http://www.openwebspider.org/)";
                    }
                    else
                    {
                        nsGlobalOutput.output.write("   - Needed field(s) in " + filename + " missing! ");
                    }

                }
                
            }

        }

    }
}
