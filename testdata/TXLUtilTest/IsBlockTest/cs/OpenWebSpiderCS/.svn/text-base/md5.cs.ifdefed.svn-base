using System;
using System.Collections.Generic;
using System.Text;

namespace OpenWebSpiderCS
{
    public class md5
    {
        public string compute(string text)
        {
            // calcola l'md5 della pagina (text2Index)
            Byte[] originalBytes;
            Byte[] encodedBytes;
            System.Security.Cryptography.MD5 md5;

            md5 = new System.Security.Cryptography.MD5CryptoServiceProvider();
            originalBytes = System.Text.ASCIIEncoding.Default.GetBytes(text);
            encodedBytes = md5.ComputeHash(originalBytes);

            System.Text.StringBuilder s = new System.Text.StringBuilder();
            foreach (byte b in encodedBytes)
            {
                s.Append(b.ToString("x2").ToLower());
            }

            return s.ToString();
        }
    }
}
