using System;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;

namespace Greenshot.Drawing
{
/// <summary>
/// Dispatches most of a DrawableContainer's public properties and methods to a list of DrawableContainers.
/// </summary>
[Serializable()]
public class DrawableContainerList : List<DrawableContainer>
{

    public DrawableContainerList()
    {
    }

    /// <summary>
    /// Gets or sets the fore color of the elements in the list.
    /// If there are several elements, the fore color of the last added is returned.
    /// </summary>
    public  Color ForeColor
    {
        get
        {
            if(Count > 0) return this[Count-1].ForeColor;
            return Color.Empty;
        }
        set
        {
            foreach(DrawableContainer dc in this) dc.ForeColor = value;
        }
    }

    /// <summary>
    /// Gets or sets the back color of the elements in the list.
    /// If there are several elements, the back color of the last added is returned.
    /// </summary>
    public  Color BackColor
    {
        get
        {
            if(Count > 0) return this[Count-1].BackColor;
            return Color.Empty;
        }
        set
        {
            foreach(DrawableContainer dc in this) dc.BackColor = value;
        }
    }

    /// <summary>
    /// Gets or sets the thickness of the elements in the list.
    /// If there are several elements, the thickness of the last added is returned.
    /// </summary>
    public  int Thickness
    {
        get
        {
            if(Count > 0) return this[Count-1].Thickness;
            return 0;
        }
        set
        {
            foreach(DrawableContainer dc in this) dc.Thickness = value;
        }
    }

    /// <summary>
    /// Gets or sets the selection status of the elements.
    /// If several elements are in the list, true is only returned when all elements are selected.
    /// </summary>
    public bool Selected
    {
        get
        {
            bool ret = true;
            foreach(DrawableContainer dc in this) ret &= dc.Selected;
            return ret;
        }
        set
        {
            foreach(DrawableContainer dc in this) dc.Selected = value;
        }
    }

    /// <summary>
    /// Gets or sets the parent control of the elements in the list.
    /// If there are several elements, the parent control of the last added is returned.
    /// </summary>
    public Control Parent
    {
        get
        {
            if(Count > 0) return this[Count-1].Parent;
            return null;
        }
        set
        {
            foreach(DrawableContainer dc in this) dc.Parent = value;
        }
    }

    /// <summary>
    /// Moves all elements in the list by the given amount of pixels.
    /// </summary>
    /// <param name="dx">pixels to move horizontally</param>
    /// <param name="dy">pixels to move vertically</param>
    public void MoveBy(int dx, int dy)
    {
        foreach(DrawableContainer dc in this)
        {
            dc.Left += dx;
            dc.Top += dy;
        }
    }

    /// <summary>
    /// Indicates whether the given property is supported by all elements in the list.
    /// </summary>
    /// <param name="prop">property to be checked</param>
    /// <returns>true if prop is supported by all elements in the list</returns>
    public bool PropertySupported(DrawableContainer.Property prop)
    {
        foreach(DrawableContainer dc in this)
        {
            if(!dc.PropertySupported(prop)) return false;
        }
        return true;
    }

    /// <summary>
    /// Indicates whether the given property is supported by at least one of the elements in the list.
    /// </summary>
    /// <param name="prop">property to be checked</param>
    /// <returns>true if prop is supported by at least one element in the list, false otherwise</returns>
    public bool PropertySupportedBySome(DrawableContainer.Property prop)
    {
        bool ret = false;
        foreach(DrawableContainer dc in this)
        {
            ret |= dc.PropertySupported(prop);
        }
        return ret;
    }

    /// <summary>
    /// Hides the grippers of all elements in the list.
    /// </summary>
    public void HideGrippers()
    {
        foreach(DrawableContainer dc in this)
        {
            dc.HideGrippers();
        }
    }

    /// <summary>
    /// Shows the grippers of all elements in the list.
    /// </summary>
    public void ShowGrippers()
    {
        foreach(DrawableContainer dc in this)
        {
            dc.ShowGrippers();
        }
    }

    /// <summary>
    /// Indicates whether on of the elements is clickable at the given location
    /// </summary>
    /// <param name="x">x coordinate to be checked</param>
    /// <param name="y">y coordinate to be checked</param>
    /// <returns>true if one of the elements in the list is clickable at the given location, false otherwise</returns>
    public bool ClickableAt(int x, int y)
    {
        bool ret = false;
        foreach(DrawableContainer dc in this)
        {
            ret |= dc.ClickableAt(x, y);
        }
        return ret;
    }

    /// <summary>
    /// retrieves the topmost element being clickable at the given location
    /// </summary>
    /// <param name="x">x coordinate to be checked</param>
    /// <param name="y">y coordinate to be checked</param>
    /// <returns>the topmost element from the list being clickable at the given location, null if there is no clickable element</returns>
    public DrawableContainer ClickableElementAt(int x, int y)
    {
        for(int i=Count-1; i>=0; i--)
        {
            if(this[i].ClickableAt(x,y)) return this[i];
        }
        return null;
    }

    /// <summary>
    /// Dispatches OnDoubleClick to all elements in the list.
    /// </summary>
    public void OnDoubleClick()
    {
        foreach(DrawableContainer dc in this)
        {
            dc.OnDoubleClick();
        }
    }

    /// <summary>
    /// Triggers all elements in the list ot be redrawn.
    /// </summary>
    /// <param name="g">the related Graphics object</param>
    /// <param name="rm">the rendermode in which the element is to be drawn</param>
    public void Draw(Graphics g, DrawableContainer.RenderMode rm)
    {
        foreach(DrawableContainer dc in this)
        {
            dc.Draw(g, rm);
        }
    }

    /// <summary>
    /// Indicates whether the given list of elements can be pulled up,
    /// i.e. whether there is at least one unselected element higher in hierarchy
    /// </summary>
    /// <param name="elements">list of elements to pull up</param>
    /// <returns>true if the elements could be pulled up</returns>
    public bool CanPullUp(DrawableContainerList elements)
    {
        if(elements.Count == 0 || elements.Count == this.Count) return false;
        foreach(DrawableContainer element in elements)
        {
            if(this.IndexOf(element) < this.Count - elements.Count) return true;
        }
        return false;
    }

    /// <summary>
    /// Pulls one or several elements up one level in hierarchy (z-index).
    /// </summary>
    /// <param name="elements">list of elements to pull up</param>
    public void PullElementsUp(DrawableContainerList elements)
    {
        for(int i=this.Count-1; i>=0; i--)
        {
            DrawableContainer dc = this[i];
            if(elements.Contains(dc))
            {
                if(Count > (i+1) && !elements.Contains(this[i+1])) SwapElements(i,i+1);
            }
        }
    }

    /// <summary>
    /// Pulls one or several elements up to the topmost level(s) in hierarchy (z-index).
    /// </summary>
    /// <param name="elements">of elements to pull to top</param>
    public void PullElementsToTop(DrawableContainerList elements)
    {
        DrawableContainer[] dcs = this.ToArray();
        for(int i=0; i<dcs.Length; i++)
        {
            DrawableContainer dc = dcs[i];
            if(elements.Contains(dc))
            {
                this.Remove(dc);
                this.Add(dc);
            }
        }
    }

    /// <summary>
    /// Indicates whether the given list of elements can be pushed down,
    /// i.e. whether there is at least one unselected element lower in hierarchy
    /// </summary>
    /// <param name="elements">list of elements to push down</param>
    /// <returns>true if the elements could be pushed down</returns>
    public bool CanPushDown(DrawableContainerList elements)
    {
        if(elements.Count == 0 || elements.Count == this.Count) return false;
        foreach(DrawableContainer element in elements)
        {
            if(this.IndexOf(element) >= elements.Count) return true;
        }
        return false;
    }

    /// <summary>
    /// Pushes one or several elements down one level in hierarchy (z-index).
    /// </summary>
    /// <param name="elements">list of elements to push down</param>
    public void PushElementsDown(DrawableContainerList elements)
    {
        for(int i=0; i<Count; i++)
        {
            DrawableContainer dc = this[i];
            if(elements.Contains(dc))
            {
                if((i>0) && !elements.Contains(this[i-1])) SwapElements(i,i-1);
            }
        }
    }

    /// <summary>
    /// Pushes one or several elements down to the bottommost level(s) in hierarchy (z-index).
    /// </summary>
    /// <param name="elements">of elements to push to bottom</param>
    public void PushElementsToBottom(DrawableContainerList elements)
    {
        DrawableContainer[] dcs = this.ToArray();
        for(int i=dcs.Length-1; i>=0; i--)
        {
            DrawableContainer dc = dcs[i];
            if(elements.Contains(dc))
            {
                this.Remove(dc);
                this.Insert(0, dc);
            }
        }
    }

    /// <summary>
    /// swaps two elements in hierarchy (z-index),
    /// checks both indices to be in range
    /// </summary>
    /// <param name="index1">index of the 1st element</param>
    /// <param name="index2">index of the 2nd element</param>
    private void SwapElements(int index1, int index2)
    {
        if(index1 >= 0 && index1 < Count && index2 >= 0 && index2 < Count && index1 != index2)
        {
            DrawableContainer dc = this[index1];
            this[index1] = this[index2];
            this[index2] = dc;
        }
    }
}
}
