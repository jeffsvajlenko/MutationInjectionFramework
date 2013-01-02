/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.betwixt.examples.rss;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Implementation object representing a <strong>channel</strong> in the
 * <em>Rich Site Summary</em> DTD, version 0.91.  This class may be subclassed
 * to further specialize its behavior.</p>
 *
 * <p>Based on the Jakarta Commons <code>Digester</code> implementation.</p>
 *
 * @author Craig R. McClanahan
 * @author Ted Husted
 * @version $Revision: 438373 $ $Date: 2006-08-30 06:17:21 +0100 (Wed, 30 Aug 2006) $
 */

public class Channel implements Serializable
{


    // ----------------------------------------------------- Instance Variables


    /**
     * The set of items associated with this Channel.
     */
    protected ArrayList items = new ArrayList();


    /**
     * The set of skip days for this channel.
     */
    protected ArrayList skipDays = new ArrayList();


    /**
     * The set of skip hours for this channel.
     */
    protected ArrayList skipHours = new ArrayList();


    // ------------------------------------------------------------- Properties


    /**
     * The channel copyright (1-100 characters).
     */
    protected String copyright = null;

    public String getCopyright()
    {
        if (this.copyright == null)
        {
            return "Public Domain";
        }
        else
        {
            return (this.copyright);
        }
    }

    public void setCopyright(String copyright)
    {
        this.copyright = copyright;
    }


    /**
     * The channel description (1-500 characters).
     */
    protected String description = null;

    public String getDescription()
    {
        return (this.description);
    }

    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * The channel description file URL (1-500 characters).
     */
    protected String docs = null;

    public String getDocs()
    {
        return (this.docs);
    }

    public void setDocs(String docs)
    {
        this.docs = docs;
    }


    /**
     * The image describing this channel.
     */
    protected Image image = null;

    public Image getImage()
    {
        return (this.image);
    }

    public void setImage(Image image)
    {
        this.image = image;
    }


    /**
     * The channel language (2-5 characters).
     */
    protected String language = null;

    public String getLanguage()
    {
        return (this.language);
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }


    /**
     * The channel last build date (1-100 characters).
     */
    protected String lastBuildDate = null;

    public String getLastBuildDate()
    {
        return (this.lastBuildDate);
    }

    public void setLastBuildDate(String lastBuildDate)
    {
        this.lastBuildDate = lastBuildDate;
    }


    /**
     * The channel link (1-500 characters).
     */
    protected String link = null;

    public String getLink()
    {
        return (this.link);
    }

    public void setLink(String link)
    {
        this.link = link;
    }


    /**
     * The managing editor (1-100 characters).
     */
    protected String managingEditor = null;

    public String getManagingEditor()
    {
        return (this.managingEditor);
    }

    public void setManagingEditor(String managingEditor)
    {
        this.managingEditor = managingEditor;
    }


    /**
     * The channel publication date (1-100 characters).
     */
    protected String pubDate = null;

    public String getPubDate()
    {
        return (this.pubDate);
    }

    public void setPubDate(String pubDate)
    {
        this.pubDate = pubDate;
    }


    /**
     * The channel rating (20-500 characters).
     */
    protected String rating = null;

    public String getRating()
    {
        return (this.rating);
    }

    public void setRating(String rating)
    {
        this.rating = rating;
    }


    /**
     * The text input description for this channel.
     */
    protected TextInput textInput = null;

    public TextInput getTextInput()
    {
        return (this.textInput);
    }

    public void setTextInput(TextInput textInput)
    {
        this.textInput = textInput;
    }


    /**
     * The channel title (1-100 characters).
     */
    protected String title = null;

    public String getTitle()
    {
        return (this.title);
    }

    public void setTitle(String title)
    {
        this.title = title;
    }


    /**
     * The RSS specification version number used to create this Channel.
     */
    protected double version = 0.91;

    public double getVersion()
    {
        return (this.version);
    }

    public void setVersion(double version)
    {
        this.version = version;
    }


    /**
     * The webmaster email address (1-100 characters).
     */
    protected String webMaster = null;

    public String getWebMaster()
    {
        return (this.webMaster);
    }

    public void setWebMaster(String webMaster)
    {
        this.webMaster = webMaster;
    }


    // --------------------------------------------------------- Public Methods


    /**
     * Add an additional item.
     *
     * @param item The item to be added
     */
    public void addItem(Item item)
    {
        synchronized (items)
        {
            items.add(item);
        }
    }


    /**
     * Add an additional skip day name.
     *
     * @param skipDay The skip day to be added
     */
    public void addSkipDay(String skipDay)
    {
        synchronized (skipDays)
        {
            skipDays.add(skipDay);
        }
    }


    /**
     * Add an additional skip hour name.
     *
     * @param skipHour The skip hour to be added
     */
    public void addSkipHour(String skipHour)
    {
        synchronized (skipHours)
        {
            skipHours.add(skipHour);
        }
    }

    /**
     * Return the items for this channel.
     */
    public List getItems()
    {
        return items;
    }


    /**
     * Return the skip days for this channel.
     */
    public String[] findSkipDays()
    {
        synchronized (skipDays)
        {
            String skipDays[] = new String[this.skipDays.size()];
            return ((String[]) this.skipDays.toArray(skipDays));
        }
    }


    /**
     * Return the skip hours for this channel.
     */
    public String[] getSkipHours()
    {
        return findSkipHours();
    }


    /**
     * Return the skip hours for this channel.
     */
    public String[] findSkipHours()
    {
        synchronized (skipHours)
        {
            String skipHours[] = new String[this.skipHours.size()];
            return ((String[]) this.skipHours.toArray(skipHours));
        }
    }


    /**
     * Return the skip days for this channel.
     */
    public String[] getSkipDays()
    {
        return findSkipDays();
    }


    /**
     * Remove an item for this channel.
     *
     * @param item The item to be removed
     */
    public void removeItem(Item item)
    {
        synchronized (items)
        {
            items.remove(item);
        }
    }


    /**
     * Remove a skip day for this channel.
     *
     * @param skipDay The skip day to be removed
     */
    public void removeSkipDay(String skipDay)
    {
        synchronized (skipDays)
        {
            skipDays.remove(skipDay);
        }
    }


    /**
     * Remove a skip hour for this channel.
     *
     * @param skipHour The skip hour to be removed
     */
    public void removeSkipHour(String skipHour)
    {
        synchronized (skipHours)
        {
            skipHours.remove(skipHour);
        }
    }
}
