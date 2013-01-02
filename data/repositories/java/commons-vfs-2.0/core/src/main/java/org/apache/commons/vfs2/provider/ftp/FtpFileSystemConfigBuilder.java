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
package org.apache.commons.vfs2.provider.ftp;

import org.apache.commons.net.ftp.parser.FTPFileEntryParserFactory;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemConfigBuilder;
import org.apache.commons.vfs2.FileSystemOptions;

/**
 * The config BUILDER for various ftp configuration options.
 *
 * @author <a href="http://commons.apache.org/vfs/team-list.html">Commons VFS team</a>
 */
public final class FtpFileSystemConfigBuilder extends FileSystemConfigBuilder
{
    private static final FtpFileSystemConfigBuilder BUILDER = new FtpFileSystemConfigBuilder();

    private static final String FACTORY_KEY = FTPFileEntryParserFactory.class.getName() + ".KEY";
    private static final String PASSIVE_MODE = FtpFileSystemConfigBuilder.class.getName() + ".PASSIVE";
    private static final String USER_DIR_IS_ROOT = FtpFileSystemConfigBuilder.class.getName() + ".USER_DIR_IS_ROOT";
    private static final String DATA_TIMEOUT = FtpFileSystemConfigBuilder.class.getName() + ".DATA_TIMEOUT";
    private static final String SO_TIMEOUT = FtpFileSystemConfigBuilder.class.getName() + ".SO_TIMEOUT";

    private static final String SERVER_LANGUAGE_CODE =
        FtpFileSystemConfigBuilder.class.getName() + ".SERVER_LANGUAGE_CODE";
    private static final String DEFAULT_DATE_FORMAT =
        FtpFileSystemConfigBuilder.class.getName() + ".DEFAULT_DATE_FORMAT";
    private static final String RECENT_DATE_FORMAT =
        FtpFileSystemConfigBuilder.class.getName() + ".RECENT_DATE_FORMAT";
    private static final String SERVER_TIME_ZONE_ID =
        FtpFileSystemConfigBuilder.class.getName() + ".SERVER_TIME_ZONE_ID";
    private static final String SHORT_MONTH_NAMES =
        FtpFileSystemConfigBuilder.class.getName() + ".SHORT_MONTH_NAMES";
    private static final String ENCODING =
        FtpFileSystemConfigBuilder.class.getName() + ".ENCODING";

    private FtpFileSystemConfigBuilder()
    {
        super("ftp.");
    }

    public static FtpFileSystemConfigBuilder getInstance()
    {
        return BUILDER;
    }

    /**
     * FTPFileEntryParserFactory which will be used for ftp-entry parsing.
     *
     * @param opts The FileSystemOptions.
     * @param factory instance of your factory
     */
    public void setEntryParserFactory(FileSystemOptions opts, FTPFileEntryParserFactory factory)
    {
        setParam(opts, FTPFileEntryParserFactory.class.getName(), factory);
    }

    /**
     * @param opts The FlleSystemOptions.
     * @see #setEntryParserFactory
     * @return An FTPFileEntryParserFactory.
     */
    public FTPFileEntryParserFactory getEntryParserFactory(FileSystemOptions opts)
    {
        return (FTPFileEntryParserFactory) getParam(opts, FTPFileEntryParserFactory.class.getName());
    }

    /**
     * set the FQCN of your FileEntryParser used to parse the directory listing from your server.<br />
     * <br />
     * <i>If you do not use the default commons-net FTPFileEntryParserFactory e.g. by using
     * {@link #setEntryParserFactory}
     * this is the "key" parameter passed as argument into your custom factory</i>
     *
     * @param opts The FileSystemOptions.
     * @param key The key.
     */
    public void setEntryParser(FileSystemOptions opts, String key)
    {
        setParam(opts, FACTORY_KEY, key);
    }

    /**
     * @param opts The FileSystemOptions.
     * @see #setEntryParser
     * @return the key to the EntryParser.
     */
    public String getEntryParser(FileSystemOptions opts)
    {
        return getString(opts, FACTORY_KEY);
    }

    @Override
    protected Class<? extends FileSystem> getConfigClass()
    {
        return FtpFileSystem.class;
    }

    /**
     * enter into passive mode.
     *
     * @param opts The FileSystemOptions.
     * @param passiveMode true if passive mode should be used.
     */
    public void setPassiveMode(FileSystemOptions opts, boolean passiveMode)
    {
        setParam(opts, PASSIVE_MODE, passiveMode ? Boolean.TRUE : Boolean.FALSE);
    }

    /**
     * @param opts The FileSystemOptions.
     * @return true if passive mode is set.
     * @see #setPassiveMode
     */
    public Boolean getPassiveMode(FileSystemOptions opts)
    {
        return getBoolean(opts, PASSIVE_MODE);
    }

    /**
     * use user directory as root (do not change to fs root).
     *
     * @param opts The FileSystemOptions.
     * @param userDirIsRoot true if the user directory should be treated as the root.
     */
    public void setUserDirIsRoot(FileSystemOptions opts, boolean userDirIsRoot)
    {
        setParam(opts, USER_DIR_IS_ROOT, userDirIsRoot ? Boolean.TRUE : Boolean.FALSE);
    }

    /**
     * @param opts The FileSystemOptions.
     * @return true if the user directory is treated as the root.
     * @see #setUserDirIsRoot
     */
    public Boolean getUserDirIsRoot(FileSystemOptions opts)
    {
        return getBoolean(opts, USER_DIR_IS_ROOT);
    }

    /**
     * @param opts The FileSystemOptions.
     * @return The timeout as an Integer.
     * @see #setDataTimeout
     */
    public Integer getDataTimeout(FileSystemOptions opts)
    {
        return getInteger(opts, DATA_TIMEOUT);
    }

    /**
     * set the data timeout for the ftp client.<br />
     * If you set the dataTimeout to <code>null</code> no dataTimeout will be set on the
     * ftp client.
     *
     * @param opts The FileSystemOptions.
     * @param dataTimeout The timeout value.
     */
    public void setDataTimeout(FileSystemOptions opts, Integer dataTimeout)
    {
        setParam(opts, DATA_TIMEOUT, dataTimeout);
    }

    /**
     * @param opts The FileSystem options.
     * @return The timeout value.
     * @see #getDataTimeout
     * @since 2.0
     */
    public Integer getSoTimeout(FileSystemOptions opts)
    {
        return (Integer) getParam(opts, SO_TIMEOUT);
    }

    /**
     * set the socket timeout for the ftp client.<br />
     * If you set the socketTimeout to <code>null</code> no socketTimeout will be set on the
     * ftp client.
     *
     * @param opts The FileSystem options.
     * @param soTimeout The timeout value.
     * @since 2.0
     */
    public void setSoTimeout(FileSystemOptions opts, Integer soTimeout)
    {
        setParam(opts, SO_TIMEOUT, soTimeout);
    }

    /**
     * get the language code used by the server. see {@link org.apache.commons.net.ftp.FTPClientConfig}
     * for details and examples.
     * @param opts The FilesystemOptions.
     * @return The language code of the server.
     */
    public String getServerLanguageCode(FileSystemOptions opts)
    {
        return getString(opts, SERVER_LANGUAGE_CODE);
    }

    /**
     * set the language code used by the server. see {@link org.apache.commons.net.ftp.FTPClientConfig}
     * for details and examples.
     * @param opts The FileSystemOptions.
     * @param serverLanguageCode The servers language code.
     */
    public void setServerLanguageCode(FileSystemOptions opts, String serverLanguageCode)
    {
        setParam(opts, SERVER_LANGUAGE_CODE, serverLanguageCode);
    }

    /**
     * get the language code used by the server. see {@link org.apache.commons.net.ftp.FTPClientConfig}
     * for details and examples.
     * @param opts The FileSystemOptions
     * @return The default date format.
     */
    public String getDefaultDateFormat(FileSystemOptions opts)
    {
        return getString(opts, DEFAULT_DATE_FORMAT);
    }

    /**
     * set the language code used by the server. see {@link org.apache.commons.net.ftp.FTPClientConfig}
     * for details and examples.
     * @param opts The FileSystemOptions.
     * @param defaultDateFormat The default date format.
     */
    public void setDefaultDateFormat(FileSystemOptions opts, String defaultDateFormat)
    {
        setParam(opts, DEFAULT_DATE_FORMAT, defaultDateFormat);
    }

    /**
     * see {@link org.apache.commons.net.ftp.FTPClientConfig} for details and examples.
     * @param opts The FileSystemOptions.
     * @return The recent date format.
     */
    public String getRecentDateFormat(FileSystemOptions opts)
    {
        return getString(opts, RECENT_DATE_FORMAT);
    }

    /**
     * see {@link org.apache.commons.net.ftp.FTPClientConfig} for details and examples.
     * @param opts The FileSystemOptions.
     * @param recentDateFormat The recent date format.
     */
    public void setRecentDateFormat(FileSystemOptions opts, String recentDateFormat)
    {
        setParam(opts, RECENT_DATE_FORMAT, recentDateFormat);
    }

    /**
     * see {@link org.apache.commons.net.ftp.FTPClientConfig} for details and examples.
     * @param opts The FileSystemOptions.
     * @return The server timezone id.
     */
    public String getServerTimeZoneId(FileSystemOptions opts)
    {
        return getString(opts, SERVER_TIME_ZONE_ID);
    }

    /**
     * see {@link org.apache.commons.net.ftp.FTPClientConfig} for details and examples.
     * @param opts The FileSystemOptions.
     * @param serverTimeZoneId The server timezone id.
     */
    public void setServerTimeZoneId(FileSystemOptions opts, String serverTimeZoneId)
    {
        setParam(opts, SERVER_TIME_ZONE_ID, serverTimeZoneId);
    }

    /**
     * see {@link org.apache.commons.net.ftp.FTPClientConfig} for details and examples.
     * @param opts The FileSystemOptions.
     * @return An array of short month names.
     */
    public String[] getShortMonthNames(FileSystemOptions opts)
    {
        return (String[]) getParam(opts, SHORT_MONTH_NAMES);
    }

    /**
     * see {@link org.apache.commons.net.ftp.FTPClientConfig} for details and examples.
     * @param opts The FileSystemOptions.
     * @param shortMonthNames an array of short month name Strings.
     */
    public void setShortMonthNames(FileSystemOptions opts, String[] shortMonthNames)
    {
        String[] clone = null;
        if (shortMonthNames != null)
        {
            clone = new String[shortMonthNames.length];
            System.arraycopy(shortMonthNames, 0, clone, 0, shortMonthNames.length);
        }

        setParam(opts, SHORT_MONTH_NAMES, clone);
    }

    /**
     * see {@link org.apache.commons.net.ftp.FTP#setControlEncoding} for details and examples.
     * @param opts The FileSystemOptions.
     * @param encoding the encoding to use
     * @since 2.0
     */
    public void setControlEncoding(FileSystemOptions opts, String encoding)
    {
        setParam(opts, ENCODING, encoding);
    }

    /**
     * @param opts The FileSystemOptions.
     * @return The encoding.
     * @since 2.0
     * */
    public String getControlEncoding(FileSystemOptions opts)
    {
        return  (String) getParam(opts, ENCODING);
    }
}
