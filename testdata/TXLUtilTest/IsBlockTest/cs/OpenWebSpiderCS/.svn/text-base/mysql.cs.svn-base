// threading.cs created with MonoDevelop
// User: shen139 at 16:54 22/06/2008
//

using System;
using System.Collections.Generic;
using System.Text;
using System.Data;
using MySql.Data.MySqlClient;
/*
    http://dev.mysql.com/doc/refman/5.1/en/connector-net-ref-mysqlclient.html
 */

namespace GlobalVars
{
    class mysqlConn
    {
		public static OpenWebSpiderCS.mysql connHostList;
		public static OpenWebSpiderCS.mysql connPageList;
	}
}

namespace OpenWebSpiderCS
{
    public class mysql
    {
        IDbConnection dbcon;
        public bool isConnected = false;
        private int iMySQLMAXQueryTimeout = 120; /* seconds */

        /* costruttore della classe mysql: si connette al server */
        /*
         connectionString =
          "Server=localhost;" +
          "Database=DB;" +
          "User ID=root;" +
          "Password=password;" +
          "Pooling=false";
         */
        public mysql(string connectionString)
        {
            try
            {
                dbcon = new MySqlConnection(connectionString);
                dbcon.Open();
            }
            catch (Exception e)
            {
                nsGlobalOutput.output.write(e.Message);
                return;
            }

            /* controlla che la connessione sia effettivamente aperta */
            if( dbcon.State == System.Data.ConnectionState.Open )
                isConnected = true;

            executeSQLQuery("SET NAMES 'UTF8';");
            executeSQLQuery("SET CHARACTER SET UTF8;");

        }

        /* distruttore della classe: sconnette dal server */
        ~mysql()
        {
			disconnect();
        }
		
		public void disconnect()
		{
			if( dbcon != null )
			{
	            dbcon.Close();
	            dbcon = null;
			}
		}
		
		/* ping()
		 * controlla che ci sia ancora la connessione al DB
		 */
		public bool ping()
		{
			bool connState = true;
			
			if (isConnected == false || !(dbcon.State == System.Data.ConnectionState.Open) )
				connState = false;

			// se la connessione "sembra" aperta: prova a fare una query
			if( connState == true )
			{
				IDbCommand dbcmd = dbcon.CreateCommand();
				dbcmd.CommandText = "SELECT 1+1";

                dbcmd.CommandTimeout = iMySQLMAXQueryTimeout;

				try
				{
					dbcmd.ExecuteNonQuery();
				}
				catch (Exception e)
				{
					connState = false;
					nsGlobalOutput.output.write("   - Error: " + e.Message);
				}
				finally
	            {
	                dbcmd.Dispose();
	                dbcmd = null;
	            }
			}
			
			return connState;
				
		}

        /* Usage:
         *   string test = conn1.getValueFromTable("table", "field", "id = 139"); 
         */
        public string getValueFromTable(string table, string field, string where)
        {
            string strReturn = string.Empty;

            /* se la connessione è chiusa: stringa vuota */
            if (isConnected == false || !(dbcon.State == System.Data.ConnectionState.Open) )
                return strReturn;

            string sql =
                "SELECT CAST( " + field + " as CHAR )" +
                " FROM " + table + 
                " WHERE 1=1 AND " + where +
                " LIMIT 1";

            IDbCommand dbcmd = dbcon.CreateCommand();
            dbcmd.CommandText = sql;

            dbcmd.CommandTimeout = iMySQLMAXQueryTimeout;

			try
            {
                IDataReader reader = dbcmd.ExecuteReader();
                if (reader.Read())
                {
                    strReturn = (string)reader[0].ToString();
                }
                // clean up
                reader.Close();
                reader = null;
            }
            catch (Exception e)
            {
                /* se c'è un errore nell'SQL o la connessione è caduta: stampa a video l'eccezione */
                nsGlobalOutput.output.write("Unable to execute SQL Query: " + sql + "\nError [getValueFromTable()]: " + e.Message + "\n\n\n");
            }
            finally
            {
                dbcmd.Dispose();
                dbcmd = null;
            }

            return strReturn;
        }

		public bool executeSQLQuery(string SQL)
        {
			
            /* se la connessione è chiusa: stringa vuota */
            if (isConnected == false || !(dbcon.State == System.Data.ConnectionState.Open) )
                return false;

            IDbCommand dbcmd = dbcon.CreateCommand();
            dbcmd.CommandType = CommandType.Text;
            dbcmd.CommandText = SQL;

            dbcmd.CommandTimeout = iMySQLMAXQueryTimeout;

			bool ret = true;
            try
            {
                dbcmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
				ret = false;
				
				if( GlobalVars.mysqlConn.connHostList == null )
					nsGlobalOutput.output.write("GlobalVars.mysqlConn.connHostList = null");
				
				if( GlobalVars.mysqlConn.connPageList == null )
					nsGlobalOutput.output.write("GlobalVars.mysqlConn.connPageList = null");
				
                /* se c'è un errore nell'SQL o la connessione è caduta: stampa a video l'eccezione */
                nsGlobalOutput.output.write("Unable to execute SQL Query: " + SQL.Substring(0, 1000) +
				                            "\nMysql1 Connected: " + GlobalVars.mysqlConn.connHostList.isConnected +
				                            "\nMysql2 Connected: " + GlobalVars.mysqlConn.connPageList.isConnected +
				                            "\nError [executeSQLQuery()]: " + e.Message + "\n\n\n");
            }
            finally
            {
                dbcmd.Dispose();
                dbcmd = null;
            }

            return ret;
        }

		public IDataReader getSQLResult(string sql)
        {
			
            /* se la connessione è chiusa: stringa vuota */
            if (isConnected == false || !(dbcon.State == System.Data.ConnectionState.Open) )
                return null;

            IDbCommand dbcmd = dbcon.CreateCommand();
            dbcmd.CommandText = sql;

            dbcmd.CommandTimeout = iMySQLMAXQueryTimeout;

			IDataReader reader = null;
			
			try
            {
                reader = dbcmd.ExecuteReader();
            }
            catch (Exception e)
            {
				reader = null;
				
                /* se c'è un errore nell'SQL o la connessione è caduta: stampa a video l'eccezione */
                nsGlobalOutput.output.write("Unable to execute SQL Query: " + sql + "\nError [getSQLResult()]: " + e.Message + "\n\n\n");
            }
            finally
            {
                dbcmd.Dispose();
                dbcmd = null;
            }

            return reader;
        }
    }
}


