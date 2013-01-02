// ListViewItemComparer.cs created with MonoDevelop
// User: eric at 19:51 09/12/2007
//
//  Copyright (C) 2007 [Petit Eric, surfzoid@gmail.com]
//
// This program is free software; you can redistribute it and/or modify
//obtaining a copy of this software and associated documentation
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

using System.Collections;
using System;
using System.Windows.Forms;

namespace MonoOSC
{
// Implements the manual sorting of items by column.
class ListViewItemComparer : IComparer
{
    private int col;
    private bool AscDescInternal;
    public ListViewItemComparer()
    {
        col = 0;
    }
    public ListViewItemComparer(int column, bool AscDesc)
    {
        col = column;
        AscDescInternal = AscDesc;
    }
    public int Compare(object x, object y)
    {
        int returnVal = -1;
        if (AscDescInternal == true)
        {
            returnVal = String.Compare(((ListViewItem)x).SubItems[col].Text,
                                       ((ListViewItem)y).SubItems[col].Text);
        }

        else
        {
            returnVal = String.Compare(((ListViewItem)y).SubItems[col].Text,
                                       ((ListViewItem)x).SubItems[col].Text);
        }
        return returnVal;
    }
}
}
