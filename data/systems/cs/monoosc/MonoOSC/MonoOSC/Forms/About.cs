// About.cs created with MonoDevelop
//
//User: eric at 03:29 10/08/2008
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
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Diagnostics;
using System.Reflection;
using System.IO;

namespace MonoOSC.Forms
{
public partial class About : Form
{
    public About()
    {
        InitializeComponent();
        this.richTextBox1.Rtf = MonoOSC.Properties.Resources.RtfAbout;

        /*System.Net.WebRequest request =
        System.Net.WebRequest.Create(
        "http://mono-project.com/files/3/31/Mono-powered-big.png");
        System.Net.WebResponse response = request.GetResponse();
        System.IO.Stream responseStream =
        response.GetResponseStream();
        imageList1.Images.Add(Image.FromStream(responseStream));
        imageList1.TransparentColor = Color.Transparent;
        imageList1.Images.SetKeyName(0, "Mono-powered-big.png");
        imageList1.ImageSize = new Size(93, 45);

        this.MonoLogo.Image = this.imageList1.Images[this.imageList1.Images.Count - 1];*/
    }

    private void richTextBox1_LinkClicked(object sender, LinkClickedEventArgs e)
    {
        try
        {
            Process.Start(e.LinkText);
        }
        catch (Exception ex)
        {
            MessageBox.Show(ex.Message + Environment.NewLine + ex.StackTrace, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            Debug.WriteLine(DateTime.Now + ": " + ex.Message + Environment.NewLine + ex.StackTrace);
        }
    }

    private void About_Shown(object sender, EventArgs e)
    {
        try
        {
            this.LblVers.Text = String.Format(
                                    "Version {0} Build {1} AT {2}",
                                    Application.ProductVersion,
                                    AssemblyVersion,
                                    AssemblyBuildDate);
            this.listView1.Items.Clear();
            foreach (Assembly asm in AppDomain.CurrentDomain.GetAssemblies())
            {
                AssemblyName name = asm.GetName();
                FileVersionInfo FsVers = System.Diagnostics.FileVersionInfo.GetVersionInfo(asm.Location);
                FileInfo FsInf = new System.IO.FileInfo(asm.Location);
                //if(FsInf.Extension == ".exe")
                this.listView1.Items.Add(FsInf.Name).SubItems.AddRange(new string[]
                {
                    name.Version.ToString(), FsVers.FileVersion,
                    FsVers.LegalCopyright, asm.Location
                });
            }
        }
        catch (Exception ex)
        {
            MessageBox.Show(ex.Message + Environment.NewLine + ex.StackTrace, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            Debug.WriteLine(DateTime.Now + ": " + ex.Message + Environment.NewLine + ex.StackTrace);
        }
        this.tabControl1.SelectedIndex = 1;
        ItColor();
        this.tabControl1.SelectedIndex = 0;
    }

    #region Accesseurs d'attribut de l'assembly

    /// <summary>
    ///
    /// </summary>
    public string AssemblyTitle
    {
        get
        {
            // Obtenir tous les attributs Title de cet assembly
            object[] attributes = Assembly.GetExecutingAssembly().GetCustomAttributes(typeof(AssemblyTitleAttribute), false);
            // Si au moins un attribut Title existe
            if (attributes.Length > 0)
            {
                // S�lectionnez le premier
                AssemblyTitleAttribute titleAttribute = (AssemblyTitleAttribute)attributes[0];
                // Si ce n'est pas une cha�ne vide, le retourner
                if (titleAttribute.Title != string.Empty)
                    return titleAttribute.Title;
            }
            // Si aucun attribut Title n'existe ou si l'attribut Title �tait la cha�ne vide, retourner le nom .exe
            return System.IO.Path.GetFileNameWithoutExtension(Assembly.GetExecutingAssembly().CodeBase);
        }
    }
    /// <summary>
    ///
    /// </summary>
    public static string AssemblyVersion
    {
        get
        {
            return Assembly.GetExecutingAssembly().GetName().Version.ToString();
        }
    }
    /// <summary>
    ///
    /// </summary>
    public static string AssemblyBuildDate
    {
        get
        {
            return System.IO.File.GetLastWriteTime(Assembly.GetExecutingAssembly().Location).ToString();

            /*//Build dates start from 01/01/2000
            DateTime result = Convert.ToDateTime("1/1/2000");
            //Retrieve the version information from the assembly from which this code is being executed
            System.Version version = Assembly.GetExecutingAssembly().GetName().Version;
            //Add the number of days (build)
            result.AddDays(version.Build);
            //Add the number of seconds since midnight (revision) multiplied by 2
            result.AddSeconds(version.Revision * 2);
            //If we're currently in daylight saving time add an extra hour
            if(TimeZone.IsDaylightSavingTime(
            DateTime.Now, TimeZone.CurrentTimeZone.GetDaylightChanges(DateTime.Now.Year)) )
            result.AddHours(1);
            return result.ToString();*/
        }
    }
    /// <summary>
    ///
    /// </summary>
    public string AssemblyDescription
    {
        get
        {
            // Obtenir tous les attributs Description de cet assembly
            object[] attributes = Assembly.GetExecutingAssembly().GetCustomAttributes(typeof(AssemblyDescriptionAttribute), false);
            // Si aucun attribut Description n'existe, retourner une cha�ne vide
            if (attributes.Length == 0)
                return string.Empty;
            // Si un attribut Description existe, retourner sa valeur
            return ((AssemblyDescriptionAttribute)attributes[0]).Description;
        }
    }
    /// <summary>
    ///
    /// </summary>
    public string AssemblyProduct
    {
        get
        {
            // Obtenir tous les attributs Product de cet assembly
            object[] attributes = Assembly.GetExecutingAssembly().GetCustomAttributes(typeof(AssemblyProductAttribute), false);
            // Si aucun attribut Product n'existe, retourner une cha�ne vide
            if (attributes.Length == 0)
                return string.Empty;
            // Si un attribut Product existe, retourner sa valeur
            return ((AssemblyProductAttribute)attributes[0]).Product;
        }
    }
    /// <summary>
    ///
    /// </summary>
    public string AssemblyCopyright
    {
        get
        {
            // Obtenir tous les attributs Copyright de cet assembly
            object[] attributes = Assembly.GetExecutingAssembly().GetCustomAttributes(typeof(AssemblyCopyrightAttribute), false);
            // Si aucun attribut Copyright n'existe, retourner une cha�ne vide
            if (attributes.Length == 0)
                return string.Empty;
            // Si un attribut Copyright existe, retourner sa valeur
            return ((AssemblyCopyrightAttribute)attributes[0]).Copyright;
        }
    }
    /// <summary>
    ///
    /// </summary>
    public string AssemblyCompany
    {
        get
        {
            // Obtenir tous les attributs Company de cet assembly
            object[] attributes = Assembly.GetExecutingAssembly().GetCustomAttributes(typeof(AssemblyCompanyAttribute), false);
            // Si aucun attribut Company n'existe, retourner une cha�ne vide
            if (attributes.Length == 0)
                return string.Empty;
            // Si un attribut Company existe, retourner sa valeur
            return ((AssemblyCompanyAttribute)attributes[0]).Company;
        }
    }
    #endregion

    private void MonoLogo_Click(object sender, EventArgs e)
    {
        try
        {
            Process.Start("http://mono-project.com");
        }
        catch (Exception ex)
        {
            MessageBox.Show(ex.Message + Environment.NewLine + ex.StackTrace, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            Debug.WriteLine(DateTime.Now + ": " + ex.Message + Environment.NewLine + ex.StackTrace);
        }
    }

    bool AscDesc = true;
    private void listView1_ColumnClick(object sender, ColumnClickEventArgs e)
    {
        try
        {
            AscDesc = !AscDesc;

            // Set the ListViewItemSorter property to a new ListViewItemComparer
            // object.
            this.listView1.ListViewItemSorter = new ListViewItemComparer(e.Column, AscDesc);
            // Call the sort method to manually sort.
            this.listView1.Sort();
            ItColor();
        }
        catch (Exception ex)
        {
            Debug.WriteLine(DateTime.Now + ": " + ex.Message + Environment.NewLine + ex.StackTrace);
        }
    }

    private void ItColor()
    {
        bool ColorSwitch = false;
        foreach (ListViewItem It in this.listView1.Items)
        {
            if (ColorSwitch)
            {
                It.ForeColor = Color.Lime;
                It.BackColor = Color.Black;
                ColorSwitch = false;
            }
            else
            {
                It.ForeColor = Color.Black;
                It.BackColor = Color.Lime;
                ColorSwitch = true;
            }
        }
    }
}//Class
}//Namespace
