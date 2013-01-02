// RSACSP.cs created with MonoDevelop
//
//User: eric at 02:54 28/08/2008
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
using System.Security.Cryptography;
using System.Text;
using System.IO;
using System.Windows.Forms;
using MonoOBSFramework;

namespace MonoOSC
{
static public class RSACSP
{

    //Create a UnicodeEncoder to convert between byte array and string.
    static private UnicodeEncoding ByteConverter = new UnicodeEncoding();
    //Create a new instance of RSACryptoServiceProvider to generate
    //public and private key data.
    static private RSACryptoServiceProvider RSA = new RSACryptoServiceProvider();
    //Store the Instance RSAKey
    static private string XmlKeyFs = Application.UserAppDataPath.Replace(Application.ProductVersion, null) + "RsaKey.bin";

    static public void Example()
    {
        try
        {
            string Str = EnCrypt("Data to Encrypt");
            if(!VarGlobal.LessVerbose)Console.WriteLine(Str);
            if(!VarGlobal.LessVerbose)Console.WriteLine(DeCrypt(Str));



        }
        catch (ArgumentNullException)
        {
            //Catch this exception in case the encryption did
            //not succeed.
            if(!VarGlobal.LessVerbose)Console.WriteLine("Encryption failed.");

        }
    }

    private static bool _CreateKey = false;
    public static bool CreateKey
    {
        get
        {
            return _CreateKey;
        }
        set
        {
            _CreateKey = value;
            if (File.Exists(XmlKeyFs) == true && _CreateKey == true)
                File.Delete(XmlKeyFs);
        }
    }


    static public string EnCrypt(string Str)
    {
        //Create byte arrays to hold original, encrypted, and decrypted data.
        byte[] dataToEncrypt = ByteConverter.GetBytes(Str);
        byte[] encryptedData;
        //Pass the data to ENCRYPT, the public key information
        //(using RSACryptoServiceProvider.ExportParameters(false),
        //and a boolean flag specifying no OAEP padding.
        if (_CreateKey == true)File.WriteAllText(XmlKeyFs, RSA.ToXmlString(true));
        encryptedData = RSAEncrypt(dataToEncrypt, RSA.ExportParameters(false), false);
        return ByteConverter.GetString(encryptedData);
    }

    static public string DeCrypt(string Str)
    {
        byte[] decryptedData;
        byte[] encryptedData = ByteConverter.GetBytes(Str);
        //Pass the data to DECRYPT, the private key information
        //(using RSACryptoServiceProvider.ExportParameters(true),
        //and a boolean flag specifying no OAEP padding.
        if (File.Exists(XmlKeyFs) == true) RSA.FromXmlString(File.ReadAllText(XmlKeyFs));
        decryptedData = RSADecrypt(encryptedData, RSA.ExportParameters(true), false);

        string DeCryptStr = string.Empty;
        if (decryptedData != null) DeCryptStr = ByteConverter.GetString(decryptedData);

        //Display the decrypted plaintext to the console.
        return DeCryptStr;

    }

    static private byte[] RSAEncrypt(byte[] DataToEncrypt, RSAParameters RSAKeyInfo, bool DoOAEPPadding)
    {
        try
        {
            //Create a new instance of RSACryptoServiceProvider.
            //RSACryptoServiceProvider RSA = new RSACryptoServiceProvider();

            //Import the RSA Key information. This only needs
            //toinclude the public key information.
            RSA.ImportParameters(RSAKeyInfo);

            //Encrypt the passed byte array and specify OAEP padding.
            //OAEP padding is only available on Microsoft Windows XP or
            //later.
            return RSA.Encrypt(DataToEncrypt, DoOAEPPadding);
        }
        //Catch and display a CryptographicException
        //to the console.
        catch (CryptographicException e)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(e.Message);

            return null;
        }

    }

    static private byte[] RSADecrypt(byte[] DataToDecrypt, RSAParameters RSAKeyInfo, bool DoOAEPPadding)
    {
        try
        {
            //Create a new instance of RSACryptoServiceProvider.
            //RSACryptoServiceProvider RSA = new RSACryptoServiceProvider();

            //Import the RSA Key information. This needs
            //to include the private key information.
            RSA.ImportParameters(RSAKeyInfo);

            //Decrypt the passed byte array and specify OAEP padding.
            //OAEP padding is only available on Microsoft Windows XP or
            //later.
            return RSA.Decrypt(DataToDecrypt, DoOAEPPadding);
        }
        //Catch and display a CryptographicException
        //to the console.
        catch (CryptographicException e)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(e.ToString());

            return null;
        }

    }

}
}
