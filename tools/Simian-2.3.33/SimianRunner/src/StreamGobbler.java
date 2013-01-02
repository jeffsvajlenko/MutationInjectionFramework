
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Class for "gobbling" an input stream.  Used to clear a stream without regards of contents.
 * @author Jeff
 * 
 * 	This is a modification of the StreamGobbler class created by Michael C. Daconta.
 *
 *	Code retrieved on Jan29, 2012 from http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html?page=4.
 *  The author is Michael C. Daconta who posted the article on December 29, 2000.
 *	
 *
 */
public class StreamGobbler extends Thread
{
    InputStream is;
    String type;
    
    public StreamGobbler(InputStream is, String type)
    {
        this.is = is;
        this.type = type;
    }
    
    public void run()
    {
    	try {
    		InputStreamReader isr = new InputStreamReader(is);
    		BufferedReader br = new BufferedReader(isr);
			while ( br.readLine() != null);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}