using System;
using System.Collections.Generic;
using System.Text;
using System.Windows.Forms;
using System.IO;
using System.Net;
using System.Diagnostics;
using System.Drawing;
using MonoOBSFramework;

namespace MonoOSC
{
public static class CheckUpdate
{
    static string CurVers = String.Format("Version {0}", Application.ProductVersion);// + String.Format("  Build {0}", Forms.About.AssemblyVersion);
    //static string VerFsPath = Application.UserAppDataPath.Replace(Application.ProductVersion, null) + "Vers";
    //static string VerFsPath = Path.GetDirectoryName(Application.ExecutablePath) + Path.DirectorySeparatorChar.ToString() + "Vers";

    public static Bitmap CheckIt()
    {
        try
        {
            try
            {
                //TODO : Need to find a better solution , to write the version file when i develop
                //File.WriteAllText(VerFsPath, CurVers);
            }
            catch (Exception)
            {
                // if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message);
            }

            WebClient list = new WebClient();
            string NewVers = list.DownloadString("http://surfzoid.free.fr/freevbsoft/MonoOSC/Vers");
            NewVers = NewVers.Replace("\r", string.Empty);
            NewVers = NewVers.Replace("\n", string.Empty);
            NewVers = NewVers.Replace(Environment.NewLine, string.Empty);
            _IcoUpdate = Properties.Resources.softwareupdateavailable;
            /*if(!VarGlobal.LessVerbose)Console.WriteLine(NewVers);
            if(!VarGlobal.LessVerbose)Console.WriteLine(CurVers);*/
            if (NewVers != CurVers)
            {
                _IcoUpdate = Properties.Resources.softwareupdateurgent;
                if (MessageBox.Show(string.Format(
                                        "New version ({0}) is aviable, would you like to update now ?",NewVers),
                                    "MonoOSC Update", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes)
                {
                    //Process.Start("https://sourceforge.net/project/platformdownload.php?group_id=236037");
                    Process.Start("http://monoosc.svn.sourceforge.net/viewvc/monoosc.tar.gz?view=tar");
                }
            }
            else if
            (_ReturnIfSame == true)MessageBox.Show("You have the last version!","MonoOSC Update", MessageBoxButtons.OK, MessageBoxIcon.Information);

        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message);
        }
        return IcoUpdate;
    }

    private static bool _ReturnIfSame = false;
    public static bool ReturnIfSame
    {
        get
        {
            return _ReturnIfSame;
        }
        set
        {
            _ReturnIfSame= value;
        }
    }

    private static Bitmap _IcoUpdate = Properties.Resources.softwareupdateavailable;
    private static Bitmap IcoUpdate
    {
        get
        {
            return _IcoUpdate;
        }
        set
        {
            _IcoUpdate = value;
        }
    }

}//class CheckUpdate
}//Namespace MonoOSC
