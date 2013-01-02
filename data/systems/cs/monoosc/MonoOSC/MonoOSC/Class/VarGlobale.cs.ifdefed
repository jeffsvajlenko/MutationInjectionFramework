// VarGlobale.cs created with MonoDevelop
//
//User: eric at 03:35 08/08/2008
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
using MonoOBSFramework;
using System.Windows.Forms;
using System.Diagnostics;
using System.Reflection;
using System.Threading;

namespace MonoOSC
{
public static class VarGlobale
{
    //static private string XmlKeyFs = Application.UserAppDataPath.Replace(Application.ProductVersion, null) + "RsaKey.MonoOSC";
    //static private string XmlProxyKeyFs = Application.UserAppDataPath.Replace(Application.ProductVersion, null) + "RsaProxyKey.MonoOSC";
    public static List<string> SubProject = new List<string>();
    //Manage a proxy
    public static string PrefPath = Application.UserAppDataPath.Replace(
                                        Application.ProductVersion, null);
    public static string ProxyPassword = null;
    public static string ProxyUserName = null;
    public static string ProxyURL = null;
    public static int ProxyPort = 0;
    public static bool ProxyEnable = false;
    public static bool AutoCheckUpdate = true;
    public static string SpecWizCont = string.Empty;
    public static string SpecWizTarBz2Fs = string.Empty;
    public static string SpecWizSpecFs = string.Empty;
    public static string SpecWizXmlTemplateFs = string.Empty;
    public static string SpecWizPkgName = string.Empty;
    public static object[] PkgList = null;
    public static bool FirstUse = true;
    public static string SkinFontPath = PrefPath + "SkinFontPath.resx";
    public static string AutoCompleteSrcePath = PrefPath + "AutoCompleteSrce.resx";
    public static string BookMarks = PrefPath + "BookMarks.resx";

    public static void LoadParam()
    {
        AutoCheckUpdate = Convert.ToBoolean(ConfigReaderWriter.GetXmlValue("AutoCheckUpdate", true.ToString()));
        //RSACSP.CreateKey = FirstRun;

        //VarGlobal.Password = RSACSP.DeCrypt(ConfigReaderWriter.GetXmlValue("Password", "123456789"));
        VarGlobal.Password = RemoveEncryption(ConfigReaderWriter.GetXmlValue("Password", "123456789"));
        VarGlobal.PrefixUserName = ConfigReaderWriter.GetXmlValue("PrefixUserName", "home:UserName");
        VarGlobal.User = ConfigReaderWriter.GetXmlValue("User", "Your OBS Username");
        VarGlobal.OpenSuseApiUrl = ConfigReaderWriter.GetXmlValue("OpenSuseApiUrl", VarGlobal.OpenSuseApiUrl);
        string[] SubPrjStr = ConfigReaderWriter.GetXmlValue("SubProject", VarGlobal.PrefixUserName).Split("|".ToCharArray());
        RpmSpecMPath = ConfigReaderWriter.GetXmlValue("RpmSpecMPath", RpmSpecMPath);
        string ConfirmWarnStr = ConfigReaderWriter.GetXmlValue("ConfirmWarn", ConfirmWarn.ToString());
        if (string.IsNullOrEmpty(ConfirmWarnStr) == false) ConfirmWarn = Convert.ToBoolean(ConfirmWarnStr);
        SubProject.Clear();
        SubProject.AddRange(SubPrjStr);

        //Manage a proxy
        //ProxyPassword = RSACSP.DeCrypt(ConfigReaderWriter.GetXmlValue("ProxyPassword", "123456789"));
        ProxyEnable = Convert.ToBoolean(ConfigReaderWriter.GetXmlValue("ProxyEnable", false.ToString()));
        ProxyPassword = RemoveEncryption(ConfigReaderWriter.GetXmlValue("ProxyPassword", "123456789"));
        ProxyUserName = ConfigReaderWriter.GetXmlValue("ProxyUserName", "UserName");
        ProxyURL = ConfigReaderWriter.GetXmlValue("ProxyURL", "127.0.0.1");
        ProxyPort = Convert.ToInt32(ConfigReaderWriter.GetXmlValue("ProxyPort", 8080.ToString()));
        if (ProxyEnable == true)
        {
            VarGlobal.DefineProxy(ProxyURL, ProxyPort, true, ProxyUserName, ProxyPassword);
        }
        FirstUse = Convert.ToBoolean(ConfigReaderWriter.GetXmlValue("FirstUse", true.ToString()));
        VarGlobal.GlobalLogFs = ConfigReaderWriter.GetXmlValue("GlobalLogFs", PrefPath + "MonoOSC.log");
        VarGlobal.LessVerbose = Convert.ToBoolean(ConfigReaderWriter.GetXmlValue("LessVerbose", true.ToString()));
    }

    public static void SaveParam()
    {
        //ConfigReaderWriter.SetXmlValue("Password", RSACSP.EnCrypt(VarGlobal.Password));
        ConfigReaderWriter.SetXmlValue("Password", AddEncryption(VarGlobal.Password));
        ConfigReaderWriter.SetXmlValue("PrefixUserName", VarGlobal.PrefixUserName);
        ConfigReaderWriter.SetXmlValue("User", VarGlobal.User);
        ConfigReaderWriter.SetXmlValue("OpenSuseApiUrl", VarGlobal.OpenSuseApiUrl);
        string SubPrjStr = string.Empty;
        foreach (string item in SubProject)
        {
            SubPrjStr += item + "|";
        }
        if (SubPrjStr.Length > 2) SubPrjStr = SubPrjStr.Remove(SubPrjStr.Length - 1, 1);
        ConfigReaderWriter.SetXmlValue("SubProject", SubPrjStr);
        ConfigReaderWriter.SetXmlValue("RpmSpecMPath", RpmSpecMPath);
        ConfigReaderWriter.SetXmlValue("ConfirmWarn", ConfirmWarn.ToString());
        ConfigReaderWriter.SetXmlValue("ConfirmWarn", ConfirmWarn.ToString());

        //Manage Proxy
        ConfigReaderWriter.SetXmlValue("ProxyEnable", ProxyEnable.ToString());
        if (ProxyEnable == true)
        {
            //ConfigReaderWriter.SetXmlValue("ProxyPassword", RSACSP.EnCrypt(ProxyPassword));
            ConfigReaderWriter.SetXmlValue("ProxyPassword", AddEncryption(ProxyPassword));
            ConfigReaderWriter.SetXmlValue("ProxyUserName", ProxyUserName);
            ConfigReaderWriter.SetXmlValue("ProxyURL", ProxyURL);
            ConfigReaderWriter.SetXmlValue("ProxyPort", ProxyPort.ToString());
        }

        ConfigReaderWriter.SetXmlValue("AutoCheckUpdate", AutoCheckUpdate.ToString());
        ConfigReaderWriter.SetXmlValue("FirstUse", false.ToString());
        ConfigReaderWriter.SetXmlValue("GlobalLogFs", VarGlobal.GlobalLogFs);
        ConfigReaderWriter.SetXmlValue("LessVerbose", VarGlobal.LessVerbose.ToString());
    }


    /// <summary>
    /// Replacement for VB InputBox, returns user input string.
    /// </summary>
    /// <param name="prompt"></param>
    /// <param name="title"></param>
    /// <param name="defaultValue"></param>
    /// <returns></returns>
    public static string InputBox(string prompt,
                                  string title, string defaultValue)
    {

        string s = string.Empty;
        try
        {
            InputBoxDialog ib = new InputBoxDialog();
            ib.FormPrompt = prompt;
            ib.FormCaption = title;
            ib.DefaultValue = defaultValue;
            ib.ShowDialog();
            s = ib.InputResponse;
            ib.Close();
            return s;
        }
        catch (Exception ex)
        {
            MessageBox.Show(ex.Message + Environment.NewLine + ex.StackTrace, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            Debug.WriteLine(DateTime.Now + ": " + ex.Message + Environment.NewLine + ex.StackTrace);
        }
        return s;
    } // method: InputBox

    public static string MetaPrjXmlFs = VarGlobal.MonoOBSFrameworkTmpDir + "MonoOSCPrjMeta.xml";
    public static string RpmSpecMPath = "Select the Binary full path";
    public static bool ConfirmWarn = true;

    // Encrypt a file.
    public static string AddEncryption(string Str2Encrypt)
    {
        string Result = string.Empty;
        byte[] Data = System.Text.Encoding.BigEndianUnicode.GetBytes(Str2Encrypt);
        for (int i = 0; i < Data.Length; i++)
        {
            Result += Base10ToN(2, Data[i]).ToString() + "$*�^#|@�~�&lkn,hftexwjlgv";
        }
        return Result;

    }

    // Decrypt a file.
    public static string RemoveEncryption(string Str2Decrypt)
    {
        string Result = Str2Decrypt.Replace("$*�^#|@�~�&lkn,hftexwjlgv", "\r");
        string[] Collect = Result.Split("\r".ToCharArray());
        byte[] Data = new byte[Collect.Length - 1];
        for (int i = 0; i < Data.Length; i++)
        {
            if (!string.IsNullOrEmpty(Collect[i])) Data[i] = Convert.ToByte(BaseNTo10(2, Collect[i]));
        }
        return System.Text.Encoding.BigEndianUnicode.GetString(Data);
    }

    private const string CHAR = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static int BaseNTo10(int baseFrom, string value)
    {
        if (baseFrom < 2 || baseFrom > 36) return -1;
        int v2 = 0;
        int base10 = 0;
        for (int i = value.Length - 1; i > -1; i--)
        {
            v2 = CHAR.IndexOf(value[i]);
            if (v2 < 0 || v2 >= baseFrom) return -1;
            for (int j = 1; j < value.Length - i; j++) v2 *= baseFrom;
            base10 += v2;
        }
        return base10;
    }

    public static string Base10ToN(int baseTo, int value)
    {
        if (baseTo < 2 || baseTo > 36) return null;
        System.Text.StringBuilder baseN = new System.Text.StringBuilder();

        do
        {
            baseN.Insert(0, CHAR[value % baseTo]);
            value = (int)(value / baseTo);
        }
        while (value > 0);

        return baseN.ToString();
    }

    public static string CurCulture()
    {
        string Cur =
            Thread.CurrentThread.CurrentUICulture.Name.ToLowerInvariant().Substring(0,2);
        switch (Cur)
        {
        case "fr":
        case "de":
        case "en":
        case "it":
            return Cur;
        default:
            MessageBox.Show("There isn't help in your language, i will start the English version, also help is really welcome if you want to be a translator :-)",
                            "Info",MessageBoxButtons.OK,MessageBoxIcon.Information);
            return "en";
        }
    }

}//Class
}//Namespace
