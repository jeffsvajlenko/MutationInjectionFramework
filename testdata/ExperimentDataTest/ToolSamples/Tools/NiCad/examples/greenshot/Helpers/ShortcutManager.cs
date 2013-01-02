using System;
using System.IO;
using IWshRuntimeLibrary;
using System.Diagnostics;

namespace Greenshot.Helpers
{
/// <summary>
/// ShortcutManager offers some static methods for handling shortcut files (*.lnk)
/// </summary>
public class ShortcutManager
{
    private ShortcutManager()
    {
    }

    /// <summary>
    /// Creates a shortcut file to the specified target file.
    /// </summary>
    /// <param name="linkFilePath">Full path where the link is to be created. If a directory is given, the lnk file will be created with the executing assembly's name.</param>
    /// <param name="targetFilePath">Full path to the file the shortcut is to refer to.</param>
    /// <param name="targetWorkDirPath">Full path to the working directory of the file the shortcut is to refer to.</param>
    public static void createShortcut(string linkFilePath, string targetFilePath, string targetWorkDirPath)
    {
        // if linkFilePath is a directory, use the name of the target file for the shortcut
        DirectoryInfo di = new DirectoryInfo(linkFilePath);
        if(di.Exists)
        {
            linkFilePath += extractFilenameWithoutExtension(targetFilePath) + ".lnk";
        }
        // create the shortcut
        WshShell shell = new WshShell();
        WshShortcut link = (WshShortcut)shell.CreateShortcut(linkFilePath);
        link.TargetPath = targetFilePath;
        link.WorkingDirectory = targetWorkDirPath;
        link.Save();
    }
    /// <summary>
    /// Creates a shortcut file to the specified target file.
    /// </summary>
    /// <param name="specialFolder">An item from the Environment.Specialfolder enumeration, specifying where the link is to be created. The lnk file will be created with the executing assembly's name.</param>
    /// <param name="targetFilePath">Full path to the file the shortcut is to refer to.</param>
    /// <param name="targetWorkDirPath">Full path to the working directory of the file the shortcut is to refer to.</param>
    public static void createShortcut(Environment.SpecialFolder specialFolder, string targetFilePath, string targetWorkDirPath)
    {
        createShortcut(Environment.GetFolderPath(specialFolder), targetFilePath, targetWorkDirPath);
    }

    /// <summary>
    /// Creates a shortcut file to the specified target file.
    /// </summary>
    /// <param name="linkFilePath">Full path where the link is to be created. If a directory is given, the lnk file will be created with the executing assembly's name.</param>
    /// <param name="targetFilePath">Full path to the file the shortcut is to refer to. The working directory will be in the same directory.</param>
    public static void createShortcut(string linkFilePath, string targetFilePath)
    {
        createShortcut(linkFilePath, targetFilePath, extractPath(targetFilePath));
    }
    /// <summary>
    /// Creates a shortcut file to the specified target file.
    /// </summary>
    /// <param name="specialFolder">An item from the Environment.Specialfolder enumeration, specifying where the link is to be created. The lnk file will be created with the executing assembly's name.</param>
    /// <param name="targetFilePath">Full path to the file the shortcut is to refer to. The working directory will be in the same directory.</param>

    public static void createShortcut(Environment.SpecialFolder specialFolder, string targetFilePath)
    {
        createShortcut(Environment.GetFolderPath(specialFolder), targetFilePath);
    }

    /// <summary>
    /// Creates a shortcut file referring to the currently executing assembly.
    /// </summary>
    /// <param name="linkFilePath">Full path where the link is to be created. If a directory is given, the lnk file will be created with the executing assembly's name.</param>
    public static void createShortcut(string linkFilePath)
    {
        createShortcut(linkFilePath, getAssemblyLocation());
    }
    /// <summary>
    /// Creates a shortcut file referring to the currently executing assembly.
    /// </summary>
    /// <param name="specialFolder">An item from the Environment.Specialfolder enumeration, specifying where the link is to be created. The lnk file will be created with the executing assembly's name.</param>
    public static void createShortcut(Environment.SpecialFolder specialFolder)
    {
        createShortcut(Environment.GetFolderPath(specialFolder));
    }

    /// <summary>
    /// Removes the shortcut at the specified path (if exists).
    /// </summary>
    /// <param name="linkFilePath">Full path of the link to be removed.</param>
    public static void removeShortcut(string linkFilePath)
    {
        FileInfo fi = new FileInfo(linkFilePath);
        if(fi.Exists)  fi.Delete();
    }
    /// <summary>
    /// Removes the shortcut at the specified path (if exists).
    /// </summary>
    /// <param name="specialFolder">An item from the Environment.Specialfolder enumeration, specifying where the location of the shortcut to be removed.</param>
    public static void removeShortcut(Environment.SpecialFolder specialFolder)
    {
        removeShortcut(Environment.GetFolderPath(specialFolder) + extractFilenameWithoutExtension(getAssemblyLocation())+".lnk");
    }

    /// <summary>
    /// Checks whether the specified shortcut file exists.
    /// </summary>
    /// <param name="linkFilePath">Full path to the shortcut file in question</param>
    /// <returns>true if the specified shortcut file exists</returns>
    public static bool shortcutExists(string linkFilePath)
    {
        return new FileInfo(linkFilePath).Exists;
    }
    /// <summary>
    /// Checks whether the specified shortcut file exists.
    /// </summary>
    /// <param name="specialFolder">An item from the Environment.Specialfolder enumeration, specifying the location of the shortcut file in question</param>
    /// <returns>true if the specified shortcut file exists</returns>
    public static bool shortcutExists(Environment.SpecialFolder specialFolder)
    {
        return shortcutExists(Environment.GetFolderPath(specialFolder) + extractFilenameWithoutExtension(getAssemblyLocation())+".lnk");
    }

    #region helper functions
    private static string getAssemblyLocation()
    {
        return System.Reflection.Assembly.GetExecutingAssembly().Location;
    }
    private static string extractPath(string location)
    {
        return location.Substring(0, location.LastIndexOf(@"\"));
    }
    private static string extractFilenameWithoutExtension(string location)
    {
        int lastSlashIndex = location.LastIndexOf(@"\");
        int lastDotIndex = location.LastIndexOf(".");
        return location.Substring(lastSlashIndex,lastDotIndex-lastSlashIndex);
    }
    #endregion

}
}
