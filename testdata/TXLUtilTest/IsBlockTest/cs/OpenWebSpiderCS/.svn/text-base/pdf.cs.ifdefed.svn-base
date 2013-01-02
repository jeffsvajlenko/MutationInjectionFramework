using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using org.pdfbox.pdmodel;
using org.pdfbox.util;

/*
 * TEST PDFs:
        http://lab.openwebspider.org/ext/test_ext.htm
 *
 */

/*
 * Using PDFBox in .NET requires adding references to:
 * 
 *   - PDFBox-0.7.3.dll
 *   - IKVM.GNU.Classpath
 *  and copying IKVM.Runtime.dll to the bin directory.
 */

namespace OpenWebSpiderCS
{
    public class pdf
    {
        public bool isValidPdf = false;
        public string pdfText = "";
        public string errorMessage = "";

        public pdf(string filename)
        {
            /* PDFBox-0.7.3 */
            PDDocument doc = PDDocument.load(filename);
            PDFTextStripper stripper = new PDFTextStripper();
            pdfText = stripper.getText(doc);

            return;
        }
    }
}
