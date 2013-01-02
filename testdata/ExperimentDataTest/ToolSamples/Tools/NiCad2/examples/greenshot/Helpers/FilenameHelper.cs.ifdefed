using System;
using Greenshot.Configuration;
using System.Collections;

namespace Greenshot.Helpers
{
public class FilenameHelper
{
    private FilenameHelper()
    {
    }

    // a map of all placeholders and their respective regexes
    public static Hashtable Placeholders = new Hashtable();
    static FilenameHelper()
    {
        Placeholders.Add("%YYYY%",@"\d{4}"); // year
        Placeholders.Add("%MM%",@"\d{2}"); // month
        Placeholders.Add("%DD%",@"\d{2}"); // day
        Placeholders.Add("%hh%",@"\d{2}"); // hour
        Placeholders.Add("%mm%",@"\d{2}"); // minute
        Placeholders.Add("%ss%",@"\d{2}"); // second
        Placeholders.Add("%NUM%",@"\d{6}"); // second
    }

    public static string GetFilenameWithoutExtensionFromPattern(string pattern)
    {
        return FillPattern(pattern);
    }

    public static string GetFilenameFromPattern(string pattern, string imageFormat)
    {
        string ext = imageFormat.ToLower();
        if(ext.Equals("jpeg")) ext = "jpg";
        return FillPattern(pattern) + "." + ext;
    }

    private static string FillPattern(string pattern)
    {
        DateTime d = DateTime.Now;
        pattern = pattern.Replace("%YYYY%",d.Year.ToString());
        pattern = pattern.Replace("%MM%", zeroPad(d.Month.ToString(), 2));
        pattern = pattern.Replace("%DD%", zeroPad(d.Day.ToString(), 2));
        pattern = pattern.Replace("%hh%", zeroPad(d.Hour.ToString(), 2));
        pattern = pattern.Replace("%mm%", zeroPad(d.Minute.ToString(), 2));
        pattern = pattern.Replace("%ss%", zeroPad(d.Second.ToString(), 2));
        if(pattern.Contains("%NUM%"))
        {
            AppConfig conf = AppConfig.GetInstance();
            int num = conf.Output_File_IncrementingNumber++;
            conf.Store();
            pattern = pattern.Replace("%NUM%", zeroPad(num.ToString(), 6));
        }
        return pattern;
    }

    private static string zeroPad(string input, int chars)
    {
        while(input.Length < chars) input = "0" + input;
        return input;
    }
}
}
