// rank.cs created with MonoDevelop
// User: shen139 at 10:54 22/07/2008
//

using System;

namespace OpenWebSpiderCS
{
	
	
	public class rank
	{
		
		public rank(int host_id)
		{
			try
			{
				nsGlobalOutput.output.write("\n + Calculating Host Rank...");
				// 1. ottieni l'host rank dal DB come somma di tutti i link che l'host corrente ha
				int host_rank = int.Parse(GlobalVars.mysqlConn.connHostList.getValueFromTable("rels", "count(distinct host_id,linkedhost_id)", "linkedhost_id = " + host_id ));

				nsGlobalOutput.output.write("   - HostRank: " + host_rank);

				
				nsGlobalOutput.output.write(" + Calculating Page Rank...");
				// 2. calcola il pagerank semplice => PR(px)=HostRank+(10/Level)
				GlobalVars.mysqlConn.connPageList.executeSQLQuery("UPDATE pages SET rank = " + (host_rank + 1) + " * abs( 20 / (level + 1) ) WHERE host_id = " + host_id );
				nsGlobalOutput.output.write("   - OK\n");
			}
			catch(Exception e)
			{
				nsGlobalOutput.output.write("   - Error: " + e.Message + "\n\n");
			}
		}
	}
}
