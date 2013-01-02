/*
 * Copyright (c) 2002, 2003, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package javax.swing.plaf.synth;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import javax.swing.border.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import java.awt.*;
import java.awt.event.ContainerListener;
import java.awt.event.ContainerEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

import sun.swing.plaf.synth.SynthUI;


/**
 * Synth's ScrollPaneUI.
 *
 * @author Scott Violet
 */
class SynthScrollPaneUI extends BasicScrollPaneUI implements
    PropertyChangeListener, SynthUI
{
    private SynthStyle style;
    private boolean viewportViewHasFocus = false;
    private ViewportViewFocusHandler viewportViewFocusHandler;

    public static ComponentUI createUI(JComponent x)
    {
        return new SynthScrollPaneUI();
    }

    @Override
    public void update(Graphics g, JComponent c)
    {
        SynthContext context = getContext(c);

        SynthLookAndFeel.update(context, g);
        context.getPainter().paintScrollPaneBackground(context,
                g, 0, 0, c.getWidth(), c.getHeight());
        paint(context, g);
        context.dispose();
    }

    @Override
    public void paint(Graphics g, JComponent c)
    {
        SynthContext context = getContext(c);

        paint(context, g);
        context.dispose();
    }

    protected void paint(SynthContext context, Graphics g)
    {
        Border vpBorder = scrollpane.getViewportBorder();
        if (vpBorder != null)
        {
            Rectangle r = scrollpane.getViewportBorderBounds();
            vpBorder.paintBorder(scrollpane, g, r.x, r.y, r.width, r.height);
        }
    }


    public void paintBorder(SynthContext context, Graphics g, int x,
                            int y, int w, int h)
    {
        context.getPainter().paintScrollPaneBorder(context, g, x, y, w, h);
    }

    @Override
    protected void installDefaults(JScrollPane scrollpane)
    {
        updateStyle(scrollpane);
    }

    private void updateStyle(JScrollPane c)
    {
        SynthContext context = getContext(c, ENABLED);
        SynthStyle oldStyle = style;

        style = SynthLookAndFeel.updateStyle(context, this);
        if (style != oldStyle)
        {
            Border vpBorder = scrollpane.getViewportBorder();
            if ((vpBorder == null) ||( vpBorder instanceof UIResource))
            {
                scrollpane.setViewportBorder(new ViewportBorder(context));
            }
            if (oldStyle != null)
            {
                uninstallKeyboardActions(c);
                installKeyboardActions(c);
            }
        }
        context.dispose();
    }


    @Override
    protected void installListeners(JScrollPane c)
    {
        super.installListeners(c);
        c.addPropertyChangeListener(this);
        if (UIManager.getBoolean("ScrollPane.useChildTextComponentFocus"))
        {
            viewportViewFocusHandler = new ViewportViewFocusHandler();
            c.getViewport().addContainerListener(viewportViewFocusHandler);
            Component view = c.getViewport().getView();
            if (view instanceof JTextComponent)
            {
                view.addFocusListener(viewportViewFocusHandler);
            }
        }
    }

    @Override
    protected void uninstallDefaults(JScrollPane c)
    {
        SynthContext context = getContext(c, ENABLED);

        style.uninstallDefaults(context);
        context.dispose();

        if (scrollpane.getViewportBorder() instanceof UIResource)
        {
            scrollpane.setViewportBorder(null);
        }
    }


    @Override
    protected void uninstallListeners(JComponent c)
    {
        super.uninstallListeners(c);
        c.removePropertyChangeListener(this);
        if (viewportViewFocusHandler != null)
        {
            JViewport viewport = ((JScrollPane) c).getViewport();
            viewport.removeContainerListener(viewportViewFocusHandler);
            if (viewport.getView()!= null)
            {
                viewport.getView().removeFocusListener(viewportViewFocusHandler);
            }
            viewportViewFocusHandler = null;
        }
    }


    public SynthContext getContext(JComponent c)
    {
        return getContext(c, getComponentState(c));
    }

    private SynthContext getContext(JComponent c, int state)
    {
        return SynthContext.getContext(SynthContext.class, c,
                                       SynthLookAndFeel.getRegion(c), style, state);
    }


    private Region getRegion(JComponent c)
    {
        return SynthLookAndFeel.getRegion(c);
    }


    private int getComponentState(JComponent c)
    {
        int baseState = SynthLookAndFeel.getComponentState(c);
        if (viewportViewFocusHandler!=null && viewportViewHasFocus)
        {
            baseState = baseState | FOCUSED;
        }
        return baseState;
    }

    public void propertyChange(PropertyChangeEvent e)
    {
        if (SynthLookAndFeel.shouldUpdateStyle(e))
        {
            updateStyle(scrollpane);
        }
    }



    private class ViewportBorder extends AbstractBorder implements UIResource
    {
        private Insets insets;

        ViewportBorder(SynthContext context)
        {
            this.insets = (Insets)context.getStyle().get(context,
                          "ScrollPane.viewportBorderInsets");
            if (this.insets == null)
            {
                this.insets = SynthLookAndFeel.EMPTY_UIRESOURCE_INSETS;
            }
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y,
                                int width, int height)
        {
            JComponent jc = (JComponent)c;
            SynthContext context = getContext(jc);
            SynthStyle style = context.getStyle();
            if (style == null)
            {
                assert false: "SynthBorder is being used outside after the " +
                " UI has been uninstalled";
                return;
            }
            context.getPainter().paintViewportBorder(context, g, x, y, width,
                    height);
            context.dispose();
        }

        public Insets getBorderInsets(Component c)
        {
            return getBorderInsets(c, null);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets)
        {
            if (insets == null)
            {
                return new Insets(this.insets.top, this.insets.left,
                                  this.insets.bottom, this.insets.right);
            }
            insets.top = this.insets.top;
            insets.bottom = this.insets.bottom;
            insets.left = this.insets.left;
            insets.right = this.insets.left;
            return insets;
        }

        @Override
        public boolean isBorderOpaque()
        {
            return false;
        }
    }

    /**
     * Handle keeping track of the viewport's view's focus
     */
    private class ViewportViewFocusHandler implements ContainerListener,
        FocusListener
    {
        public void componentAdded(ContainerEvent e)
        {
            if (e.getChild() instanceof JTextComponent)
            {
                e.getChild().addFocusListener(this);
                viewportViewHasFocus = e.getChild().isFocusOwner();
                scrollpane.repaint();
            }
        }

        public void componentRemoved(ContainerEvent e)
        {
            if (e.getChild() instanceof JTextComponent)
            {
                e.getChild().removeFocusListener(this);
            }
        }

        public void focusGained(FocusEvent e)
        {
            viewportViewHasFocus = true;
            scrollpane.repaint();
        }

        public void focusLost(FocusEvent e)
        {
            viewportViewHasFocus = false;
            scrollpane.repaint();
        }
    }
}
