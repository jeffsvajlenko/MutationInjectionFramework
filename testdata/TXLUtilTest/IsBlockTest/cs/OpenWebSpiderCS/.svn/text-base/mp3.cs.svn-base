using System;
using System.Collections.Generic;
using System.Text;
using HundredMilesSoftware.UltraID3Lib;
using System.IO;

namespace OpenWebSpiderCS
{
    public class mp3
    {
        public bool isValidMp3 = false;
        public string mp3Title = "";
        public string mp3Artist = "";
        public string mp3Album = "";
        public string mp3Genre = "";
        public int mp3Length = 0;
        public string errorMessage = "";
        public int mp3Size = 0;

        public mp3(string filename)
        {
            isValidMp3 = false;
            errorMessage = "";

            // Parsing

            UltraID3 mp3 = new UltraID3();

            try
            {
                mp3.Read(filename);

                mp3Title = mp3.Title;
                mp3Artist = mp3.Artist;
                mp3Album = mp3.Album;
                mp3Genre = mp3.Genre;
                mp3Length = (int)Math.Round(mp3.Duration.TotalSeconds);

                isValidMp3 = true;

            }
            catch (Exception e)
            {
                errorMessage = e.Message;
                nsGlobalOutput.output.write("Error: " + e.Message);
                return;
            }

            return;

        }

    }
}
