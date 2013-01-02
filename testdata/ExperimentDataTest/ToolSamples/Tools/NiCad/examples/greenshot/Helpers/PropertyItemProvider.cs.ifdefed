using System;
using System.Drawing;
using System.IO;
using System.Drawing.Imaging;

namespace Greenshot.Helpers
{
/// <summary>
/// PropertyItemProvider is a helper class to provide instances of PropertyItem
/// Be sure to have the PropertyItemProvider.resx too, since it contains the
/// image we will take the PropertyItem from.
/// </summary>
public class PropertyItemProvider
{
    private static PropertyItem propertyItem;
    private PropertyItemProvider()
    {
    }

    public static PropertyItem GetPropertyItem(int id, string value)
    {
        if(propertyItem == null)
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(PropertyItemProvider));
            Bitmap bmp = (Bitmap)resources.GetObject("propertyitemcontainer");
            propertyItem = bmp.GetPropertyItem(bmp.PropertyIdList[0]);
            propertyItem.Type =2; // string

        }
        propertyItem.Id = id;
        System.Text.ASCIIEncoding  encoding=new System.Text.ASCIIEncoding();
        propertyItem.Value = encoding.GetBytes(value + " ");
        propertyItem.Len = value.Length + 1;
        return propertyItem;
    }
}
}
