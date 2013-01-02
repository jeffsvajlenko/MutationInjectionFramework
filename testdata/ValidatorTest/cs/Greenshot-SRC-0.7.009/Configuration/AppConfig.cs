using System;
using System.Diagnostics;
using System.Drawing;
using System.Drawing.Imaging;
using System.IO;
using System.Reflection;
using System.Runtime.Serialization.Formatters.Binary;
using System.Runtime.Serialization;
using System.Windows.Forms;
using System.Collections;

namespace Greenshot.Configuration
{

public enum ScreenshotDestinations {Editor=1, FileDefault=2, FileWithDialog=4, Clipboard=8, Printer=16}

/// <summary>
/// AppConfig is used for loading and saving the configuration. All public fields
/// in this class are serialized with the BinaryFormatter and then saved to the
/// config file. After loading the values from file, SetDefaults iterates over
/// all public fields an sets fields set to null to the default value.
/// </summary>
[Serializable]
public class AppConfig
{
    //private static string loc = Assembly.GetExecutingAssembly().Location;
    //private static string oldFilename = Path.Combine(loc.Substring(0,loc.LastIndexOf(@"\")),"config.dat");
    private static string filename = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.ApplicationData),@"Greenshot\config.dat");
    private static AppConfig instance = null;

    // the configuration part - all public vars are stored in the config file
    // don't use "null" and "0" as default value!

    #region general application config
    public bool? General_RegisterHotkeys = true;
    public bool? General_IsFirstLaunch = true;
    #endregion

    #region user interface config
    public string Ui_Language = "";
    public bool? Ui_Effects_Flashlight = false;
    public bool? Ui_Effects_CameraSound = true;
    #endregion

    #region output config
    public ScreenshotDestinations Output_Destinations = ScreenshotDestinations.Editor;


    public string Output_File_Path = Environment.GetFolderPath(Environment.SpecialFolder.Desktop);
    public string Output_File_FilenamePattern = "greenshot_%YYYY%-%MM%-%DD%_%hh%-%mm%-%ss%";
    public string Output_File_Format = ImageFormat.Png.ToString();
    public bool? Output_File_CopyPathToClipboard = false;
    public int Output_File_JpegQuality = 80;
    public bool? Output_File_PromptJpegQuality = false;
    public int Output_File_IncrementingNumber = 1;

    public string Output_FileAs_Fullpath = Path.Combine(Environment.GetFolderPath(Environment.SpecialFolder.Desktop),"dummy.png");

    public bool? Output_Print_PromptOptions = true;
    public bool? Output_Print_AllowRotate = true;
    public bool? Output_Print_AllowEnlarge = true;
    public bool? Output_Print_AllowShrink = true;
    public bool? Output_Print_Center = true;
    #endregion

    #region editor config
    public Size? Editor_WindowSize = new Size(640, 480);
    public Color Editor_ForeColor = Color.FromArgb(255, 255, 0, 0);
    public Color Editor_BackColor = Color.Transparent;
    public int Editor_Thickness = 1;
    public Color[] Editor_RecentColors = new Color[12];
    public Font Editor_Font = null;
    #endregion

    /// <summary>
    /// a private constructor because this is a singleton
    /// </summary>
    private AppConfig()
    {
    }

    /// <summary>
    /// get an instance of AppConfig
    /// </summary>
    /// <returns></returns>
    public static AppConfig GetInstance()
    {
        if (instance == null)
        {
            instance = Load();
        }
        return instance;
    }

    /// <summary>
    /// loads the configuration from the config file
    /// </summary>
    /// <returns>an instance of AppConfig with all values set from the config file</returns>
    private static AppConfig Load()
    {
        AppConfig conf;
        CheckConfigFile();
        Stream s = null;
        try
        {
            s = File.Open(filename, FileMode.Open);
            BinaryFormatter b = new BinaryFormatter();
            conf = (AppConfig) b.Deserialize(s);
            s.Close();
            conf.SetDefaults();
            return conf;
        }
        catch (SerializationException)
        {
            if (s != null)
            {
                s.Close();
            }
            AppConfig config = new AppConfig();
            config.Store();
            return config;
        }
        catch (Exception)
        {
            MessageBox.Show("Could not load Greenshot's configuration file. Please check access permissions for '"+filename+"'.\n","Error");
            Process.GetCurrentProcess().Kill();
        }
        return null;

    }

    /// <summary>
    /// Checks for the existence of a configuration file.
    /// First in greenshot's Applicationdata folder (where it is stored since 0.6),
    /// then (if it cannot be found there) in greenshot's program directory (where older
    /// versions might have stored it).
    /// If the latter is the case, the file is moved to the new location, so that a user does not lose
    /// their configuration after upgrading.
    /// If there is no file in both locations, a virgin config file is created.
    /// </summary>
    private static void CheckConfigFile()
    {
        if(!File.Exists(filename))
        {
            Directory.CreateDirectory(filename.Substring(0,filename.LastIndexOf(@"\")));
            new AppConfig().Store();
        }
    }

    /// <summary>
    /// saves the configuration values to the config file
    /// </summary>
    public void Store()
    {
        Stream s = File.Open(filename, FileMode.Create);
        BinaryFormatter formatter = new BinaryFormatter();
        formatter.Serialize(s, this);
        s.Close();
    }

    /// <summary>
    /// when new fields are added to this class, they are instanced
    /// with null by default. this method iterates over all public
    /// fields and uses reflection to set them to the proper default value.
    /// </summary>
    public void SetDefaults()
    {
        Type type = this.GetType();
        FieldInfo[] fieldInfos = type.GetFields();
        foreach (FieldInfo fi in fieldInfos)
        {
            object o = fi.GetValue(this);
            int i;
            if (o == null || (int.TryParse(o.ToString(), out i) && i == 0))
            {
                // found field with value null. setting to default.
                AppConfig tmpConf = new AppConfig();
                Type tmpType = tmpConf.GetType();
                FieldInfo defaultField = tmpType.GetField(fi.Name);
                fi.SetValue(this, defaultField.GetValue(tmpConf));
            }
        }
    }

}
}
