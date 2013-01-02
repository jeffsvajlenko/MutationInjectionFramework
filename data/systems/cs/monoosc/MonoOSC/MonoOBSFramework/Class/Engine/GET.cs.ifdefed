// GET.cs created with MonoDevelop
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
using System.Net;
using System.IO;
using System.Security.Cryptography.X509Certificates;

namespace MonoOBSFramework.Engine
{
/// <summary>
///
/// </summary>
public static class GET
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
    public static StringBuilder Getit(string FuncAndArgs, string User, string Password)
    {
        try
        {
            // Create the web request
            HttpWebRequest request
                = WebRequest.Create(VarGlobal.OpenSuseApiUrl + FuncAndArgs) as HttpWebRequest;
            if(!VarGlobal.LessVerbose)Console.WriteLine("GET {0}", request.Address.ToString());
            VarGlobal.NetEvManager.DoSomething(string.Format("GET {0}", request.Address.ToString()));

            //If proxy is not null, add it
            if(VarGlobal.Proxy != null) request.Proxy = VarGlobal.Proxy;

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
}//Class
}//NameSpace
