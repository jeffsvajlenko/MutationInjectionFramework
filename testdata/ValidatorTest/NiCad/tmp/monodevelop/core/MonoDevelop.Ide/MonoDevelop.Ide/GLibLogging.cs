//
// GLibLogging.cs
//
// Author:
//   Michael Hutchinson <mhutchinson@novell.com>
//
// Copyright (C) 2008 Novell, Inc (http://www.novell.com)
//
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//

using System;

using MonoDevelop.Core;

namespace MonoDevelop.Ide.Gui
{
static class GLibLogging
{
    // If we get more than 1MB of debug info, we don't care. 99.999% of the time we're just doing
    // the same thing over and over and we really don't want log files which are 100gb in size
    static int RemainingBytes = 1 * 1024 * 1024;
    static readonly string[] domains = new string[] {"Gtk", "Gdk", "GLib", "GLib-GObject", "Pango", "GdkPixbuf" };
    static uint[] handles;

    public static bool Enabled
    {
        get
        {
            return handles != null;
        }
        set
        {
            if ((handles != null) == value)
                return;

            if (value)
            {
                handles = new uint[domains.Length];
                for (int i = 0; i < domains.Length; i++)
                    handles[i] = GLib.Log.SetLogHandler (domains[i],  GLib.LogLevelFlags.All, LogFunc);
            }
            else
            {
                for (int i = 0; i < domains.Length; i++)
                    GLib.Log.RemoveLogHandler (domains[i], handles[i]);
                handles = null;
            }
        }
    }

    static void LogFunc (string logDomain, GLib.LogLevelFlags logLevel, string message)
    {
        if (RemainingBytes < 0)
            return;

        System.Diagnostics.StackTrace trace = new System.Diagnostics.StackTrace (2, true);
        string msg = string.Format ("{0}-{1}: {2}\nStack trace: \n{3}",
                                    logDomain, logLevel, message, trace.ToString ());

        switch (logLevel)
        {
        case GLib.LogLevelFlags.Debug:
            LoggingService.LogDebug (msg);
            break;
        case GLib.LogLevelFlags.Info:
            LoggingService.LogInfo (msg);
            break;
        case GLib.LogLevelFlags.Warning:
            LoggingService.LogWarning (msg);
            break;
        case GLib.LogLevelFlags.Error:
        case GLib.LogLevelFlags.Critical:
        default:
            LoggingService.LogError (msg);
            break;
        }

        RemainingBytes -= msg.Length;
        if (RemainingBytes < 0)
            LoggingService.LogError ("Disabling glib logging for the rest of the session");
    }
}
}
