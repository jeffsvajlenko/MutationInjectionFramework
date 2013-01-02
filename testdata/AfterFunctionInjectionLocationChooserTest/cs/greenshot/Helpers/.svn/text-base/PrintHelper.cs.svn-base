/*
 * Erstellt mit SharpDevelop.
 * Benutzer: Administrator
 * Datum: 11.11.2007
 * Zeit: 15:07
 *
 * Sie k�nnen diese Vorlage unter Extras > Optionen > Codeerstellung > Standardheader �ndern.
 */

using System;
using System.Windows.Forms;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Drawing.Printing;

using Greenshot.Configuration;
using Greenshot.Forms;

namespace Greenshot.Helpers
{
/// <summary>
/// Description of PrintHelper.
/// </summary>
public class PrintHelper
{
    private Image image;
    private PrintDocument printDocument = new PrintDocument();
    private PrintDialog printDialog = new PrintDialog();
    private AppConfig conf = AppConfig.GetInstance();

    public PrintHelper(Image image)
    {
        this.image = image;
        printDialog.UseEXDialog = true;
        printDocument.DocumentName = FilenameHelper.GetFilenameWithoutExtensionFromPattern(AppConfig.GetInstance().Output_File_FilenamePattern);
        printDocument.PrintPage += GetImageForPrint;
        printDialog.Document = printDocument;
    }

    public PrinterSettings PrintWithDialog()
    {
        if (printDialog.ShowDialog() == DialogResult.OK)
        {
            printDocument.Print();
            return printDialog.PrinterSettings;
        }
        else
        {
            return null;
        }
    }

    void GetImageForPrint(object sender, PrintPageEventArgs e)
    {
        PrintOptionsDialog pod = new PrintOptionsDialog();
        pod.ShowDialog();

        ContentAlignment alignment = pod.AllowPrintCenter ? ContentAlignment.MiddleCenter : ContentAlignment.TopLeft;

        RectangleF pageRect = e.PageSettings.PrintableArea;
        GraphicsUnit gu = GraphicsUnit.Pixel;
        RectangleF imageRect = image.GetBounds(ref gu);
        // rotate the image if it fits the page better
        if(pod.AllowPrintRotate)
        {
            if((pageRect.Width > pageRect.Height && imageRect.Width < imageRect.Height) ||
                    (pageRect.Width < pageRect.Height && imageRect.Width > imageRect.Height))
            {
                image.RotateFlip(RotateFlipType.Rotate90FlipNone);
                imageRect = image.GetBounds(ref gu);
                if(alignment.Equals(ContentAlignment.TopLeft)) alignment = ContentAlignment.TopRight;
            }
        }
        RectangleF printRect = new RectangleF(0,0,imageRect.Width, imageRect.Height);;
        // scale the image to fit the page better
        if(pod.AllowPrintEnlarge || pod.AllowPrintShrink)
        {
            SizeF resizedRect = ScaleHelper.GetScaledSize(imageRect.Size,pageRect.Size,false);
            if((pod.AllowPrintShrink && resizedRect.Width < printRect.Width) ||
                    pod.AllowPrintEnlarge && resizedRect.Width > printRect.Width)
            {
                printRect.Size = resizedRect;
            }

        }
        // align the image
        printRect = ScaleHelper.GetAlignedRectangle(printRect, new RectangleF(0,0, pageRect.Width, pageRect.Height), alignment);

        e.Graphics.DrawImage(image,printRect,imageRect,GraphicsUnit.Pixel);
    }
}
}
