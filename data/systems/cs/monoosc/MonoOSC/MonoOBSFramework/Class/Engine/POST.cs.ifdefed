// POST.cs created with MonoDevelop
//
//User: eric at 03:42 07/08/2008
//
// Copyright (C) 2008 [Petit Eric, surfzoid@gmail.com]
//
//Permission is hereby granted, free of charge, to any person
//obtaining a copy of this software and associated documentation
//files (the "Software"), to deal in the Software without
//restriction, including without limitation the rights to use,
//copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the
//Software is furnished to do so, subject to the following
//conditions:
//
//The above copyright notice and this permission notice shall be
//included in all copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//OTHER DEALINGS IN THE SOFTWARE.
//

using System;
using System.Collections.Generic;
using System.Text;
// We use the HttpUtility class from the System.Web namespace
using System.Web;
using System.Net;
using System.IO;

namespace MonoOBSFramework.Engine
{

/// <summary>
///
/// </summary>
public static class POST
{
    /// <summary>
    ///
    /// </summary>
    /// <param name="FuncAndArgs">
    /// A <see cref="System.String"/>
    /// </param>
    /// <param name="User">
    /// A <see cref="System.String"/>
    /// </param>
    /// <param name="Password">
    /// A <see cref="System.String"/>
    /// </param>
    /// <returns>
    /// A <see cref="StringBuilder"/>
    /// </returns>
    public static StringBuilder Postit(string FuncAndArgs, string User, string Password)
    {
        try
        {
            Uri address = new Uri(VarGlobal.OpenSuseApiUrl + FuncAndArgs);
            if(!VarGlobal.LessVerbose)Console.WriteLine("POST {0}", address.ToString());
            VarGlobal.NetEvManager.DoSomething(string.Format("POST {0}", address.ToString()));

            // Create the web request
            HttpWebRequest request = WebRequest.Create(address) as HttpWebRequest;

            //If proxy is not null, add it
            if (VarGlobal.Proxy != null) request.Proxy = VarGlobal.Proxy;

            // Add authentication to request
            request.Credentials = new NetworkCredential(User, Password);
            request.Timeout = VarGlobal.TimeOut;

            // Set type to POST
            request.Method = "POST";
            request.ContentType = "application/x-www-form-urlencoded";

            // Create the data we want to send
            /*string appId = "YahooDemo";
            string context = "Italian sculptors and painters of the renaissance"
                                + "favored the Virgin Mary for inspiration";
            string query = "madonna";*/
            StringBuilder data = new StringBuilder();
            /*data.Append("appid=" + HttpUtility.UrlEncode(appId));
            data.Append("&context=" + HttpUtility.UrlEncode(context));
            data.Append("&query=" + HttpUtility.UrlEncode(query));*/

            // Create a byte array of the data we want to send
            byte[] byteData = UTF8Encoding.UTF8.GetBytes(data.ToString());

            // Set the content length in the request headers
            request.ContentLength = byteData.Length;

            // Write data
            using (Stream postStream = request.GetRequestStream())
            {
                postStream.Write(byteData, 0, byteData.Length);
            }

            // Get response
            using (HttpWebResponse response = request.GetResponse() as HttpWebResponse)
            {
                // Get the response stream
                StreamReader reader = new StreamReader(response.GetResponseStream());

                // Console application output
                //if(!VarGlobal.LessVerbose)Console.WriteLine(reader.ReadToEnd());
                return new StringBuilder(reader.ReadToEnd());
            }
        }
        catch (Exception Ex)
        {

            VarGlobal.NetEvManager.DoSomething(Ex.Message);
            return new StringBuilder(Ex.Message + Environment.NewLine + Ex.StackTrace);
        }
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="FuncAndArgs"></param>
    /// <param name="User"></param>
    /// <param name="Password"></param>
    /// <param name="SourceFile"></param>
    /// <returns></returns>
    static public StringBuilder Postit(string FuncAndArgs, string User, string Password, string SourceFile)
    {
        WebClient request = new WebClient();
        try
        {
            Uri DestFile = new Uri(VarGlobal.OpenSuseApiUrl + FuncAndArgs);

            //If proxy is not null, add it
            if (VarGlobal.Proxy != null) request.Proxy = VarGlobal.Proxy;

            if(!VarGlobal.LessVerbose)Console.WriteLine("POST {0}", DestFile.ToString());
            VarGlobal.NetEvManager.DoSomething(string.Format("POST {0}", DestFile.ToString()));

            //parametre de connexion
            request.Credentials = new NetworkCredential(User, Password);

            // LoopValue(request);
            byte[] Result = request.UploadFile(DestFile, "POST", SourceFile);
            //LoopValue(request);
            return new StringBuilder(ASCIIEncoding.ASCII.GetString(Result));
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);
            VarGlobal.NetEvManager.DoSomething(Ex.Message);
            return new StringBuilder(Ex.Message + Environment.NewLine + Ex.StackTrace);
        }

    }
    /// <summary>
    ///
    /// </summary>
    /// <param name="FuncAndArgs"></param>
    /// <param name="User"></param>
    /// <param name="Password"></param>
    /// <param name="SourceFile"></param>
    /// <returns></returns>
    static public StringBuilder UploadFileCore(string FuncAndArgs, string User, string Password, string SourceFile)
    {
        if(!VarGlobal.LessVerbose)Console.WriteLine("UploadFileCore");
        VarGlobal.NetEvManager.DoSomething("UploadFileCore");
        try
        {

            Uri address = new Uri(VarGlobal.OpenSuseApiUrl + FuncAndArgs);
            if(!VarGlobal.LessVerbose)Console.WriteLine("POST {0}", address.ToString());
            VarGlobal.NetEvManager.DoSomething(string.Format("POST {0}", address.ToString()));

            // Create the web request
            HttpWebRequest request = HttpWebRequest.Create(address) as HttpWebRequest;

            //If proxy is not null, add it
            if (VarGlobal.Proxy != null) request.Proxy = VarGlobal.Proxy;

            // Add authentication to request
            request.Credentials = new NetworkCredential(User, Password);

            request.Timeout = VarGlobal.TimeOut;
            request.AllowWriteStreamBuffering = true;
            request.Accept = "utf8";
            //request.Pipelined = true;
            request.ReadWriteTimeout = VarGlobal.TimeOut;
            //request.SendChunked = true;
            //request.UseDefaultCredentials = true;
            request.PreAuthenticate = true;

            request.ContentType = "application/octet-stream";
            request.Connection = "open";
            request.Accept = "100-continue";
            request.CookieContainer = new CookieContainer();
            request.UserAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.12) Gecko/20080201 Firefox/2.0.0.12";

            request.Headers.Add(HttpRequestHeader.AcceptEncoding, "identity");
            Cookie Cooki = new Cookie("IPCZQX018ef15359", "010068027daf78608de2ffc6", "/", ".opensuse.org");
            request.Headers.Add(HttpRequestHeader.Cookie, Cooki.Name + "=" + Cooki.Value);
            //LoopValue(request);

            // Set type to POST
            request.Method = "POST";
            string contentType = "application/octet-stream";
            request.ContentType = contentType;

            Stream reqStream = null;
            Stream fStream = null;
            StringBuilder result = new StringBuilder();

            SourceFile = Path.GetFullPath(SourceFile);
            //string boundary = "---------------------" + DateTime.Now.Ticks.ToString("x", NumberFormatInfo.InvariantInfo);
            //byte [] boundaryBytes = Encoding.ASCII.GetBytes("\r\n--" + boundary + "--\r\n");

            try
            {
                fStream = File.OpenRead(SourceFile);
                request.ContentLength = fStream.Length;
                reqStream = request.GetRequestStream();
                int nread;
                int DebugCnt = 0;
                int buffSize = (int)Math.Min((long)8192, fStream.Length);
                byte[] buffer = new byte[buffSize];
                if(!VarGlobal.LessVerbose)Console.WriteLine("File length {0}", fStream.Length);
                while ((nread = fStream.Read(buffer, 0, buffSize)) != 0)
                {
                    reqStream.Write(buffer, 0, nread);
                    VarGlobal.NetEvManager.DoSomething(string.Format("POST {0}", address.ToString()));
                    DebugCnt += nread;
                }
                if(!VarGlobal.LessVerbose)Console.WriteLine("Writted {0} byte(s)", DebugCnt);
                reqStream.Close();
                reqStream = null;

                // Get response
                using (HttpWebResponse response = request.GetResponse() as HttpWebResponse)
                {
                    // Get the response stream
                    StreamReader reader = new StreamReader(response.GetResponseStream());

                    result.Append(reader.ReadToEnd());
                }

            }
            catch (Exception Tex)
            {
                VarGlobal.NetEvManager.DoSomething(Tex.Message);
                if(!VarGlobal.LessVerbose)Console.WriteLine(Tex.Message + Environment.NewLine + Tex.StackTrace);
                if (request != null)
                    request.Abort();
                throw;
            }
            finally
            {
                if (fStream != null)
                    fStream.Close();

                if (reqStream != null)
                    reqStream.Close();
            }

            return result;
        }
        catch (Exception Ex)
        {
            VarGlobal.NetEvManager.DoSomething(Ex.Message);
            if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);

        }
        return null;
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="FuncAndArgs"></param>
    /// <param name="User"></param>
    /// <param name="Password"></param>
    /// <param name="XmlBody"></param>
    /// <returns></returns>
    public static StringBuilder PostMeta(string FuncAndArgs, string User, string Password, string XmlBody)
    {
        if (FuncAndArgs == null)
            throw new ArgumentNullException("address");
        if (XmlBody == null)
            throw new ArgumentNullException("XmlBody");

        try
        {
            Uri address = new Uri(VarGlobal.OpenSuseApiUrl + FuncAndArgs);
            if(!VarGlobal.LessVerbose)Console.WriteLine("POST {0}", address.ToString());
            VarGlobal.NetEvManager.DoSomething(string.Format("POST {0}", address.ToString()));

            // Create the web request
            HttpWebRequest request = WebRequest.Create(address) as HttpWebRequest;

            //If proxy is not null, add it
            if (VarGlobal.Proxy != null) request.Proxy = VarGlobal.Proxy;

            // Add authentication to request
            request.Credentials = new NetworkCredential(User, Password);
            request.Timeout = VarGlobal.TimeOut;

            // Set type to POST
            request.Method = "POST";
            //This is okay for Message, but made an err 500 for the XML of Create
            //request.ContentType = "application/x-ssds+xml";
            request.AllowAutoRedirect = true;
            request.AllowWriteStreamBuffering = true;
            request.UserAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.12) Gecko/20080201 Firefox/2.0.0.12";

            // Create the data we want to send
            /*string appId = "YahooDemo";
            string context = "Italian sculptors and painters of the renaissance"
                                + "favored the Virgin Mary for inspiration";
            string query = "madonna";*/
            StringBuilder data = new StringBuilder(XmlBody);
            /*data.Append("appid=" + HttpUtility.UrlEncode(appId));
            data.Append("&context=" + HttpUtility.UrlEncode(context));
            data.Append("&query=" + HttpUtility.UrlEncode(query));*/

            // Create a byte array of the data we want to send
            byte[] byteData = UTF8Encoding.UTF8.GetBytes(data.ToString());

            // Set the content length in the request headers
            request.ContentLength = byteData.Length;

            // Write data
            using (Stream postStream = request.GetRequestStream())
            {
                postStream.Write(byteData, 0, byteData.Length);
                if(!VarGlobal.LessVerbose)Console.WriteLine("Connection " + request.Connection);
                if(!VarGlobal.LessVerbose)Console.WriteLine("Accept " + request.Accept);
                if(!VarGlobal.LessVerbose)Console.WriteLine("RequestUri " + request.RequestUri.ToString());
            }

            // Get response
            using (HttpWebResponse response = request.GetResponse() as HttpWebResponse)
            {
                // Get the response stream
                StreamReader reader = new StreamReader(response.GetResponseStream());

                // Console application output
                //if(!VarGlobal.LessVerbose)Console.WriteLine(reader.ReadToEnd());
                return new StringBuilder(reader.ReadToEnd());
            }
        }
        catch (Exception Ex)
        {

            VarGlobal.NetEvManager.DoSomething(Ex.Message);
            return new StringBuilder(Ex.Message + Environment.NewLine + Ex.StackTrace);
        }
    }
}//class
}//namespace
