// http.cs created with MonoDevelop
// User: shen139 at 11:30 25/06/2008
//

using System;
using System.Net;
using System.IO;
using System.Text;

namespace OpenWebSpiderCS
{
	
	
	public class http
	{
		// indica lo lo status code dell'ultima richiesta HTTP
		public int statusCode;
		// contiene l'eventuale stringa di errore dell'ultima richiesta HTTP
        public string errorString;
		// content-type della pagina appena scaricata
		public string contentType;
		// il contenuto della pagina scaricata
		public string HTML;
		/*
		public http()
		{
			statusCode = 0;			
			errorString = String.Empty;
			contentType = String.Empty;			
		}
		*/

        public string binaryStream2MD5File(page p, BinaryReader binStream, string contentType, string contentLength)
        {
            string appPath = System.AppDomain.CurrentDomain.BaseDirectory.ToString();
            md5 MD5 = new md5();
            string fullPath = appPath + MD5.compute(p._page);

            nsGlobalOutput.output.write("\n + Downloading " + p.GenerateURL() + " ... ");
            nsGlobalOutput.output.write("   - Content-Type: " + contentType);
            nsGlobalOutput.output.write("   - Content-Length: " + contentLength + " bytes \n");

            try
            {
                int BUFFER_SIZE = 4096;
                byte[] buf = new byte[BUFFER_SIZE];
                System.IO.FileStream stream = new System.IO.FileStream(fullPath, System.IO.FileMode.Create);
                int n = binStream.Read(buf, 0, BUFFER_SIZE);
                while (n > 0)
                {
                    stream.Write(buf, 0, n);
                    n = binStream.Read(buf, 0, BUFFER_SIZE);
                }
                stream.Close();
            }
            catch (Exception e)
            {
                nsGlobalOutput.output.write("Error: " + e.Message);
            }
            finally
            {
                binStream.Close();
            }

            return fullPath;
        }

        public bool deleteFile(string filename)
        {
            // delete file
            try
            {
                FileInfo f = new FileInfo(filename);
                f.Delete();
            }
            catch (Exception e)
            {
                nsGlobalOutput.output.write("Error: " + e.Message);
                return false;
            }
            return true;
        }
		
        public string getURL(string URL, page p, bool followRedirects)
        {
			
			errorString = String.Empty;
			contentType = String.Empty;
			statusCode = 0;
			HTML = String.Empty;

            try
            {
                HttpWebRequest request = HttpWebRequest.Create(URL) as HttpWebRequest;

                // imposta l'user-agent
                request.UserAgent = GlobalVars.OpenWebSpider.USERAGENT;

                // imposta il timeout (default: un minuto 60.000 ms)
                request.Timeout = GlobalVars.args.reqTimeout * 1000;
                
                // segue i redirect
                if (followRedirects)
                {
                    request.AllowAutoRedirect = true;
                    request.MaximumAutomaticRedirections = 5;
                }
                else
                    request.AllowAutoRedirect = false;
                
                HttpWebResponse response = (HttpWebResponse)request.GetResponse();

                if (response.CharacterSet == null)
                    return HTML;

                // Support to Encodings
                Encoding responseEncoding;
                responseEncoding = Encoding.UTF8;   //default UTF-8
                if (response.CharacterSet.Trim() != "")
                    responseEncoding = Encoding.GetEncoding(response.CharacterSet);
                
                StreamReader sr = new StreamReader(response.GetResponseStream(), responseEncoding);
                BinaryReader binaryStream = new BinaryReader(response.GetResponseStream());

                statusCode = (int)response.StatusCode;

                contentType = response.Headers["Content-Type"];

                // il content-type di questa pagina è testo?
                if (contentType.StartsWith("text", StringComparison.CurrentCultureIgnoreCase))                    
                    HTML = sr.ReadToEnd();
                else if (GlobalVars.args.indexMP3 && contentType.ToLower() == "audio/mpeg")
                {
                    // se sappiamo chi è il padre e ha un hostID valido allora indicizza l'MP3
                    if (p != null)
                        if (p.isValidPage && p._hostID > 0)
                        {
                            string fullPath = binaryStream2MD5File(p, binaryStream, response.Headers["Content-Type"], response.Headers["Content-Length"]);

                            mp3 MP3 = new mp3(fullPath);
                            deleteFile(fullPath);

                            MP3.mp3Size = int.Parse(response.Headers["Content-Length"]);
                    
                            db __db = new db();
                            __db.indexMP3(p, MP3);
                        }

                }
                else if (GlobalVars.args.indexPDF && contentType.ToLower() == "application/pdf")
                {
                    // se sappiamo chi è il padre e ha un hostID valido allora indicizza il PDF
                    if (p != null)
                        if (p.isValidPage && p._hostID > 0)
                        {
                            string fullPath = binaryStream2MD5File(p, binaryStream, response.Headers["Content-Type"], response.Headers["Content-Length"]);

                            pdf PDF = new pdf(fullPath);
                            deleteFile(fullPath);

                            db __db = new db();
                            __db.indexPDF(p, int.Parse(response.Headers["Content-Length"]), PDF.pdfText);
                        }

                }
                else
                    HTML = string.Empty;

                // forza l'encoding corrente a UTF-8
                Encoding utf8 = Encoding.Unicode;

                byte[] responseEncodingBytes = responseEncoding.GetBytes(HTML);

                byte[] utf8Bytes = Encoding.Convert(responseEncoding,
                                                     utf8,
                                                     responseEncodingBytes);

                HTML = utf8.GetString(utf8Bytes);

                sr.Close();

            }
            catch (WebException e)
            {
                // TODO: in caso di 404 leggere ugualmente lo stream e ritornare l'HTML

                HttpWebResponse response = (HttpWebResponse)e.Response;
                if (response != null)
                {
                    // in caso di eccezione: prova a recuperare da qui lo status code
                    statusCode = (int)response.StatusCode;

                    if (response.StatusCode == HttpStatusCode.Unauthorized)
                    {
                        string challenge = null;
                        challenge = response.GetResponseHeader("WWW-Authenticate");
                        if (challenge != null)
                            errorString = "The following challenge was raised by the server:" + challenge;
                    }
                    else
                        errorString = "The following WebException was raised : " + e.Message;
                }
                else
                    errorString = "Response Received from server was null";
            }
            catch (Exception e)
            {
                errorString = "The following Exception was raised :" + e.Message;
            }

            return HTML;
        }

        public string getURL(page p, bool followRedirects)
		{
            return getURL(p.GenerateURL(), p, followRedirects);
		}
		
	}
}
