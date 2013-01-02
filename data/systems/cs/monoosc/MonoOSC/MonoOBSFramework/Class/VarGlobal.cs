// VarGlobal.cs created with MonoDevelop
//
//User: eric at 18:17 04/08/2008
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
using System.Net.Security;
using System.Security.Cryptography.X509Certificates;
using MonoOBSFramework.Engine;
using System.IO;

namespace MonoOBSFramework
{
/// <summary>
///
/// </summary>
public static class VarGlobal
{
    /// <summary>
    /// The user name to log at OpenSuseApiUrl
    /// </summary>
    /// <returns>
    /// A <see cref="System.Boolean"/>
    /// </returns>
    public static string User = string.Empty;
    /// <summary>
    /// The password to log at OpenSuseApiUrl
    /// </summary>
    /// <returns>
    /// A <see cref="System.Boolean"/>
    /// </returns>
    public static string Password = string.Empty;
    /// <summary>
    /// The name of a project to work on.
    /// </summary>
    /// <returns>
    /// A <see cref="System.Boolean"/>
    /// </returns>
    public static string Project = string.Empty;
    /// <summary>
    /// Will never be use.
    /// </summary>
    /// <returns>
    /// A <see cref="System.Boolean"/>
    /// </returns>
    public static bool TrustIt = AlwaysTrustCertif();
    /// <summary>
    /// The prefix to add before the user name (Default = "home:")
    /// </summary>
    /// <returns>
    /// A <see cref="System.Boolean"/>
    /// </returns>
    public static string PrefixUserName = "home:";
    /// <summary>
    /// The TimOut for all Web transaction in Millisecondes
    /// </summary>
    public static int TimeOut = 60000;
    /// <summary>
    /// If you want to change the default OpenSuseApiUrl url.
    /// </summary>
    public static string OpenSuseApiUrl = "https://api.opensuse.org/";
    /// <summary>
    /// If true, we will not display console messages
    /// </summary>
    public static bool LessVerbose = false;


    static bool AlwaysTrustCertif()
    {
        ServicePointManager.UseNagleAlgorithm = true;
        ServicePointManager.Expect100Continue = false;
        ServicePointManager.CheckCertificateRevocationList = false;
        ServicePointManager.DefaultConnectionLimit = ServicePointManager.DefaultPersistentConnectionLimit;
        //Always trust certificate in .Net 2.0 but not implemented under mono
        /*ServicePointManager.ServerCertificateValidationCallback =

        delegate(Object obj, X509Certificate certificate, X509Chain chain,

        SslPolicyErrors errors) { return true; };*/

        //Always trust certificate obsolete Net1.0
#pragma warning disable 0618
        ServicePointManager.CertificatePolicy = new ProgramCert();
        return true;

    }
    /// <summary>
    /// Provide a temp directory.
    /// </summary>
    public static string MonoOBSFrameworkTmpDir = MonoOBSFrameworkTmpDirCheck();
    /// <summary>
    /// This object is used to globaly send or intercept event of web traffic.
    /// </summary>
    public static NetEvents NetEvManager = new NetEvents();
    /// <summary>
    ///
    /// </summary>
    public static string GlobalLogFs = MonoOBSFrameworkTmpDir + "MonoOSC.log";
    /// <summary>
    ///
    /// </summary>
    public static FileStream LogFsStream = new FileStream(GlobalLogFs, FileMode.Create, FileAccess.ReadWrite, FileShare.ReadWrite);
    /// <summary>
    ///
    /// </summary>
    public static GlobalLog LogFrm;
    private static bool ShowLogWindow = false;
    /// <summary>
    ///
    /// </summary>
    public static bool _ShowLogWindow
    {
        get
        {
            return ShowLogWindow;
        }
        set
        {
            ShowLogWindow = value;
            if (ShowLogWindow == true)
            {
                if(LogFrm!= null)LogFrm.Dispose();
                LogFrm = null;
                LogFrm = new GlobalLog();
                LogFrm.Visible = true;
            }
            else LogFrm.Visible = false;
        }
    }

    private static string MonoOBSFrameworkTmpDirCheck()
    {
        DirectoryInfo DirInf = new DirectoryInfo(Path.GetTempPath() +
                Path.DirectorySeparatorChar.ToString() + "MonoOBSFrameworkTmpDir");
        if(DirInf.Exists == false)DirInf.Create();
        return DirInf.FullName + Path.DirectorySeparatorChar.ToString();
    }

    /// <summary>
    /// This the builded proxy, either is null, since you don't set it with "DefineProxy" void
    /// </summary>
    public static WebProxy Proxy = null;

    /// <summary>
    /// Define the proxy
    /// </summary>
    /// <param name="Adress">
    /// A <see cref="System.String"/>
    /// </param>
    /// <param name="Port">
    /// A <see cref="System.Int32"/>
    /// </param>
    /// <param name="ByPassOnLocal">
    /// A <see cref="System.Boolean"/>
    /// </param>
    /// <param name="UserName">
    /// A <see cref="System.String"/>
    /// </param>
    /// <param name="Password">
    /// A <see cref="System.String"/>
    /// </param>
    public static void DefineProxy(string Adress,int Port,bool ByPassOnLocal,string UserName,string Password)
    {
        Proxy = new WebProxy(Adress + ":" + Port.ToString(),ByPassOnLocal);
        if (!string.IsNullOrEmpty(UserName) && !string.IsNullOrEmpty(Password))
        {
            NetworkCredential credentials = new NetworkCredential(UserName,Password );
            Proxy.Credentials = credentials;
        }
    }

}//class VarGlobal


/// <summary>
/// Always answer yes when the Certificate of OpenSuseApiUrl need to be accepted/trusted.
/// </summary>
public class ProgramCert : ICertificatePolicy
{
    /// <summary>
    /// Always answer yes when the Certificate of OpenSuseApiUrl need to be accepted/trusted.
    /// </summary>
    /// <param name="sp">
    /// A <see cref="ServicePoint"/>
    /// </param>
    /// <param name="certificate">
    /// A <see cref="X509Certificate"/>
    /// </param>
    /// <param name="request">
    /// A <see cref="WebRequest"/>
    /// </param>
    /// <param name="error">
    /// A <see cref="System.Int32"/>
    /// </param>
    /// <returns>
    /// A <see cref="System.Boolean"/>A list of all getted value.
    /// </returns>
    public bool CheckValidationResult(ServicePoint sp,
                                      X509Certificate certificate, WebRequest request, int error)
    {
        return true;
    }

}//class ProgramCert : ICertificatePolicy

}//namespace
