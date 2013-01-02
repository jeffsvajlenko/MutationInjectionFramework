// PUT.cs created with MonoDevelop
//
//User: eric at 18:16 04/08/2008
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
using System.Diagnostics;
using System.Threading;
using System.Globalization;

namespace MonoOBSFramework.Engine
{
/// <summary>
///
/// </summary>
public static class PUT
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
    public static StringBuilder Putit(string FuncAndArgs, string User, string Password)
    {
        try
        {
            Uri address = new Uri(VarGlobal.OpenSuseApiUrl + FuncAndArgs);
            if(!VarGlobal.LessVerbose)if(!VarGlobal.LessVerbose)Console.WriteLine("PUT {0}", address.ToString());
            VarGlobal.NetEvManager.DoSomething(string.Format("PUT {0}", address.ToString()));
            // Create the web request
            HttpWebRequest request = WebRequest.Create(address) as HttpWebRequest;

            //If proxy is not null, add it
            if (VarGlobal.Proxy != null) request.Proxy = VarGlobal.Proxy;

            // Add authentication to request
            request.Credentials = new NetworkCredential(User, Password);
            request.Timeout = VarGlobal.TimeOut;
            request.AllowAutoRedirect = true;
            request.AllowWriteStreamBuffering = true;
            request.UserAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.12) Gecko/20080201 Firefox/2.0.0.12";

            // Set type to PUT
            request.Method = "PUT";
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
    static public StringBuilder Putit(string FuncAndArgs, string User, string Password, string SourceFile)
    {
        WebClient request = new WebClient();
        try
        {
            Uri DestFile = new Uri(VarGlobal.OpenSuseApiUrl + FuncAndArgs);

            //If proxy is not null, add it
            if (VarGlobal.Proxy != null) request.Proxy = VarGlobal.Proxy;

            if(!VarGlobal.LessVerbose)Console.WriteLine("PUT {0}", DestFile.ToString());
            VarGlobal.NetEvManager.DoSomething(string.Format("PUT {0}", DestFile.ToString()));

            //parametre de connexion
            request.Credentials = new NetworkCredential(User, Password);

            /*request.Headers.Add(HttpRequestHeader.Accept, "100-continue");
            request.Headers.Add(HttpRequestHeader.ContentType, "application/octet-stream; charset=utf-8");
            request.Headers.Add(HttpRequestHeader.Connection, "open");
            request.Headers.Add(HttpRequestHeader.AcceptEncoding, "identity");
            Cookie Cooki = new Cookie("IPCZQX018ef15359", "010068027daf78608de2ffc6", "/", ".opensuse.org");
            request.Headers.Add(HttpRequestHeader.Cookie, Cooki.Name + "=" + Cooki.Value);
            request.Headers.Add(HttpRequestHeader.UserAgent, "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.12) Gecko/20080201 Firefox/2.0.0.12");*/
            LoopValue(request);
            byte[] Result = request.UploadFile(DestFile, "PUT", SourceFile);
            LoopValue(request);
            return new StringBuilder(ASCIIEncoding.ASCII.GetString(Result));
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);
            VarGlobal.NetEvManager.DoSomething(Ex.Message);
            return new StringBuilder(Ex.Message + Environment.NewLine + Ex.StackTrace);
        }

    }

    static private void LoopValue(WebClient request)
    {
        if(!VarGlobal.LessVerbose)Console.WriteLine("WebHeaderCollection for {0}", Environment.OSVersion.Platform.ToString());
        // Loop through the Headers and display the header name/value pairs.
        if(!VarGlobal.LessVerbose)Console.WriteLine("Loop through the Headers and display the header name/value pairs.");
        for (int i = 0; i < request.Headers.Count; i++)
            if(!VarGlobal.LessVerbose)Console.WriteLine("\t" + request.Headers.GetKey(i) + " = " + request.Headers.Get(i));
        // Loop through the QueryString and display the header name/value pairs.
        if(!VarGlobal.LessVerbose)Console.WriteLine("Loop through the QueryString and display the header name/value pairs.");
        for (int k = 0; k < request.QueryString.Count; k++)
            if(!VarGlobal.LessVerbose)Console.WriteLine("\t" + request.QueryString.GetKey(k) + " = " + request.QueryString.Get(k));
        // Loop through the ResponseHeaders and display the header name/value pairs.
        if (request.ResponseHeaders != null)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine("Loop through the ResponseHeaders and display the header name/value pairs.");
            for (int j = 0; j < request.ResponseHeaders.Count; j++)
                if(!VarGlobal.LessVerbose)Console.WriteLine("\t" + request.ResponseHeaders.GetKey(j) + " = " + request.ResponseHeaders.Get(j));
        }
    }

    static private void LoopValue(HttpWebRequest request)
    {
        if(!VarGlobal.LessVerbose)Console.WriteLine("WebHeaderCollection for {0}", Environment.OSVersion.Platform.ToString());
        // Loop through the Headers and display the header name/value pairs.
        if(!VarGlobal.LessVerbose)Console.WriteLine("Loop through the Headers and display the header name/value pairs.");
        for (int i = 0; i < request.Headers.Count; i++)
            if(!VarGlobal.LessVerbose)Console.WriteLine("\t" + request.Headers.GetKey(i) + " = " + request.Headers.Get(i));
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
            if(!VarGlobal.LessVerbose)Console.WriteLine("PUT {0}", address.ToString());
            VarGlobal.NetEvManager.DoSomething(string.Format("PUT {0}", address.ToString()));

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
            LoopValue(request);

            // Set type to PUT
            request.Method = "PUT";
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
                    DebugCnt += nread;
                    VarGlobal.NetEvManager.DoSomething(string.Format(
                                                           "SENT {0} byte(s) of {1} total byte(s)", DebugCnt, fStream.Length));
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
    public static StringBuilder PutMeta(string FuncAndArgs, string User, string Password, string XmlBody)
    {
        if (FuncAndArgs == null)
            throw new ArgumentNullException("address");
        if (XmlBody == null)
            throw new ArgumentNullException("XmlBody");

        try
        {
            Uri address = new Uri(VarGlobal.OpenSuseApiUrl + FuncAndArgs);
            if(!VarGlobal.LessVerbose)Console.WriteLine("PUT {0}", address.ToString());
            VarGlobal.NetEvManager.DoSomething(string.Format("PUT {0}", address.ToString()));

            // Create the web request
            HttpWebRequest request = WebRequest.Create(address) as HttpWebRequest;

            //If proxy is not null, add it
            if (VarGlobal.Proxy != null) request.Proxy = VarGlobal.Proxy;

            // Add authentication to request
            request.Credentials = new NetworkCredential(User, Password);
            request.Timeout = VarGlobal.TimeOut;

            // Set type to PUT
            request.Method = "PUT";
            request.ContentType = "application/x-ssds+xml";
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
    /// <summary>
    ///
    /// </summary>
    /// <param name="FuncAndArgs"></param>
    /// <param name="filename"></param>
    /// <param name="fileBytes"></param>
    /// <param name="User"></param>
    /// <param name="Password"></param>
    /// <returns></returns>
    public static string ExecutePostCommand(string FuncAndArgs, string filename, byte[] fileBytes, string User, string Password)
    {
        HttpWebRequest request = (HttpWebRequest)HttpWebRequest.Create(VarGlobal.OpenSuseApiUrl + FuncAndArgs);

        //If proxy is not null, add it
        if (VarGlobal.Proxy != null) request.Proxy = VarGlobal.Proxy;

        request.PreAuthenticate = true;
        request.AllowWriteStreamBuffering = true;

        //parametre de connexion
        request.Credentials = new NetworkCredential(User, Password);
        string boundary = System.Guid.NewGuid().ToString();

        request.ContentType = string.Format("multipart/form-data;boundary={0}", boundary);
        request.Method = "POST";
        // not sure if this was necessary, but found forums where it was an issue not to set it.
        request.UserAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.1.12) Gecko/20080201 Firefox/2.0.0.12";

        // Build Contents for Post
        string header = string.Format("--{0}", boundary);
        string footer = header + "--";
        // string builders are for the text above and below the file - file is kept in its original format.
        StringBuilder contents = new StringBuilder();
        StringBuilder contents2 = new StringBuilder();

        // Zip File
        contents.AppendLine(header);
        contents.AppendLine(string.Format("Content-Disposition:form-data; name=\"myFile\"; filename=\"{0}\"", filename));
        contents.AppendLine("Content-Type: application/x-zip-compressed");
        contents.AppendLine();

        // file goes here... then contents 2

        contents2.AppendLine();
        // Form Field 1
        contents2.AppendLine(header);
        contents2.AppendLine("Content-Disposition:form-data; name=\"Field1\"");
        contents2.AppendLine();
        contents2.AppendLine("Field1Value");
        // Form Field 2
        contents2.AppendLine(header);
        contents2.AppendLine("Content-Disposition:form-data; name=\"Field2\"");
        contents2.AppendLine();
        contents2.AppendLine("Field2Value");
        // Footer
        contents2.AppendLine(footer);

        // This is sent to the Post
        byte[] bytes = Encoding.UTF8.GetBytes(contents.ToString());
        byte[] bytes2 = Encoding.UTF8.GetBytes(contents2.ToString());

        // now we have all of the bytes we are going to send, so we can calculate the size of the stream
        request.ContentLength = bytes.Length + fileBytes.Length + bytes2.Length;

        using (Stream requestStream = request.GetRequestStream())
        {
            requestStream.Write(bytes, 0, bytes.Length);
            requestStream.Write(fileBytes, 0, fileBytes.Length);
            requestStream.Write(bytes2, 0, bytes2.Length);
            requestStream.Flush();
            requestStream.Close();

            using (WebResponse response = request.GetResponse())
            {
                using (StreamReader reader = new StreamReader(response.GetResponseStream()))
                {
                    return reader.ReadToEnd();
                }
            }
        }
    }
}//Class
}//NameSpace
