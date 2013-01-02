// DELETE.cs created with MonoDevelop
//
//User: eric at 02:05 07/08/2008
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
using System.Threading;
using System.Globalization;

namespace MonoOBSFramework.Engine
{

/// <summary>
///
/// </summary>
public static class DELETE
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
    public static StringBuilder Deleteit(string FuncAndArgs, string User, string Password)
    {
        try
        {
            Uri address = new Uri(VarGlobal.OpenSuseApiUrl + FuncAndArgs);
            // Create the web request
            HttpWebRequest request = WebRequest.Create(address) as HttpWebRequest;

            //If proxy is not null, add it
            if(VarGlobal.Proxy != null) request.Proxy = VarGlobal.Proxy;

            // Add authentication to request
            request.Credentials = new NetworkCredential(User, Password);
            request.Timeout = VarGlobal.TimeOut;

            // Set type to DELETE
            request.Method = "DELETE";
            if(!VarGlobal.LessVerbose)Console.WriteLine(request.Method + " {0}",address.ToString());
            VarGlobal.NetEvManager.DoSomething(string.Format(request.Method + " {0}", address.ToString()));

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
    /// <returns></returns>
    public static StringBuilder DeleteitUnix(string FuncAndArgs, string User, string Password)
    {
        try
        {
            // Create the web request
            HttpWebRequest request
                = WebRequest.Create(VarGlobal.OpenSuseApiUrl + FuncAndArgs) as HttpWebRequest;
            if(!VarGlobal.LessVerbose)Console.WriteLine("DELETE {0}",VarGlobal.OpenSuseApiUrl + FuncAndArgs);

            //If proxy is not null, add it
            if(VarGlobal.Proxy != null) request.Proxy = VarGlobal.Proxy;

            // Set type to DELETE
            request.Method = "DELETE";

            // Add authentication to request
            request.Credentials = new NetworkCredential(User, Password);
            request.Timeout = VarGlobal.TimeOut;

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
}//NameSpace
