/**
 * This file is a part of Angry IP Scanner source code,
 * see http://www.angryip.org/ for more information.
 * Licensed under GPLv2.
 */
package net.azib.ipscan.config;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * This is a generic named list config.
 * Can be used for storing favorites, openers, and other
 * user-defined configurations.
 *
 * @author Anton Keks
 */
public class NamedListConfig implements Iterable<String>
{

    protected String preferenceName;
    protected Preferences preferences;
    protected Map<String, Object> namedList = new LinkedHashMap<String, Object>();

    // package local constructor
    NamedListConfig(Preferences preferences, String preferenceName)
    {
        this.preferenceName = preferenceName;
        this.preferences = preferences;
        load();
    }

    /**
     * Loads preferences
     */
    public void load()
    {
        if (preferences == null)
        {
            return;
        }

        String[] namedListPrefs = preferences.get(preferenceName, "").split("###");
        for (int i = 0; i < namedListPrefs.length; i += 2)
        {
            if (namedListPrefs[i].length() > 0)
            {
                namedList.put(namedListPrefs[i], serializeValue(namedListPrefs[i+1]));
            }
        }
    }

    Object serializeValue(String value)
    {
        return value;
    }

    /**
     * Stores the currently available named list
     */
    public void store()
    {
        StringBuffer sb = new StringBuffer(32);
        for (Map.Entry<String, Object> e : namedList.entrySet())
        {
            sb.append(e.getKey()).append("###").append(e.getValue()).append("###");
        }
        if (sb.length() > 3)
        {
            sb.delete(sb.length() - 3, sb.length());
        }
        preferences.put(preferenceName, sb.toString());
    }

    /**
     * @param key displayed to the user
     * @param value to store according to the name
     */
    public void add(String key, Object value)
    {
        namedList.put(key, value);
    }

    /**
     * @param key key
     * @return stored value
     */
    public String get(String key)
    {
        Object value = namedList.get(key);
        return value != null ? value.toString() : null;
    }

    /**
     * @param key name
     * @return stored value
     */
    public String remove(String key)
    {
        return namedList.remove(key).toString();
    }

    /**
     * @return an Iterator for iterating names of available items
     */
    public Iterator<String> iterator()
    {
        return namedList.keySet().iterator();
    }

    public int size()
    {
        return namedList.size();
    }

    /**
     * Updates the list, retaining only items that are passed in the array.
     * The order of elements will be the same as in the array.
     *
     * @param keys
     */
    public void update(String[] keys)
    {
        // rebuild the map (to recreate the new order of elements)
        Map<String, Object> newList = new LinkedHashMap<String, Object>();
        for (int i = 0; i < keys.length; i++)
        {
            newList.put(keys[i], namedList.get(keys[i]));
        }
        namedList = newList;
    }

}
