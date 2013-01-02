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

import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.parser.FTPFileEntryParserFactory;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.util.UserAuthenticatorUtils;

/**
 * Create a FtpClient instance.
 *
 * @author <a href="http://commons.apache.org/vfs/team-list.html">Commons VFS team</a>
 */
public final class FtpClientFactory
{
    private static final int BUFSZ = 40;

    private FtpClientFactory()
    {
    }

    /**
     * Creates a new connection to the server.
     *
     * @param hostname          The host name of the server.
     * @param port              The port to connect to.
     * @param username          The name of the user for authentication.
     * @param password          The user's password.
     * @param workingDirectory  The base directory.
     * @param fileSystemOptions The FileSystemOptions.
     * @return An FTPClient.
     * @throws FileSystemException if an error occurs while connecting.
     */
    public static FTPClient createConnection(String hostname, int port, char[] username, char[] password,
            String workingDirectory, FileSystemOptions fileSystemOptions)
    throws FileSystemException
    {
        // Determine the username and password to use
        if (username == null)
        {
            username = "anonymous".toCharArray();
        }

        if (password == null)
        {
            password = "anonymous".toCharArray();
        }

        try
        {
            final FTPClient client = new FTPClient();

            configureClient(fileSystemOptions, client);

            FTPFileEntryParserFactory myFactory =
                FtpFileSystemConfigBuilder.getInstance().getEntryParserFactory(fileSystemOptions);
            if (myFactory != null)
            {
                client.setParserFactory(myFactory);
            }

            try
            {
                client.connect(hostname, port);

                int reply = client.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply))
                {
                    throw new FileSystemException("vfs.provider.ftp/connect-rejected.error", hostname);
                }

                // Login
                if (!client.login(
                            UserAuthenticatorUtils.toString(username),
                            UserAuthenticatorUtils.toString(password)))
                {
                    throw new FileSystemException("vfs.provider.ftp/login.error",
                                                  new Object[] {hostname, UserAuthenticatorUtils.toString(username)}, null);
                }

                // Set binary mode
                if (!client.setFileType(FTP.BINARY_FILE_TYPE))
                {
                    throw new FileSystemException("vfs.provider.ftp/set-binary.error", hostname);
                }

                // Set dataTimeout value
                Integer dataTimeout = FtpFileSystemConfigBuilder.getInstance().getDataTimeout(fileSystemOptions);
                if (dataTimeout != null)
                {
                    client.setDataTimeout(dataTimeout.intValue());
                }

                Integer socketTimeout = FtpFileSystemConfigBuilder.getInstance().getSoTimeout(fileSystemOptions);
                if (socketTimeout != null)
                {
                    client.setSoTimeout(socketTimeout.intValue());
                }

                // Change to root by default
                // All file operations a relative to the filesystem-root
                // String root = getRoot().getName().getPath();

                Boolean userDirIsRoot = FtpFileSystemConfigBuilder.getInstance().getUserDirIsRoot(fileSystemOptions);
                if (workingDirectory != null && (userDirIsRoot == null || !userDirIsRoot.booleanValue()))
                {
                    if (!client.changeWorkingDirectory(workingDirectory))
                    {
                        throw new FileSystemException("vfs.provider.ftp/change-work-directory.error", workingDirectory);
                    }
                }

                Boolean passiveMode = FtpFileSystemConfigBuilder.getInstance().getPassiveMode(fileSystemOptions);
                if (passiveMode != null && passiveMode.booleanValue())
                {
                    client.enterLocalPassiveMode();
                }

                String controlEncoding = FtpFileSystemConfigBuilder.getInstance().getControlEncoding(fileSystemOptions);
                if (controlEncoding != null)
                {
                    client.setControlEncoding(controlEncoding);
                }
            }
            catch (final IOException e)
            {
                if (client.isConnected())
                {
                    client.disconnect();
                }
                throw e;
            }

            return client;
        }
        catch (final Exception exc)
        {
            throw new FileSystemException("vfs.provider.ftp/connect.error", new Object[] {hostname}, exc);
        }
    }

    private static void configureClient(FileSystemOptions fileSystemOptions, FTPClient client)
    {
        String key = FtpFileSystemConfigBuilder.getInstance().getEntryParser(fileSystemOptions);
        if (key != null)
        {
            FTPClientConfig config = new FTPClientConfig(key);

            String serverLanguageCode =
                FtpFileSystemConfigBuilder.getInstance().getServerLanguageCode(fileSystemOptions);
            if (serverLanguageCode != null)
            {
                config.setServerLanguageCode(serverLanguageCode);
            }
            String defaultDateFormat =
                FtpFileSystemConfigBuilder.getInstance().getDefaultDateFormat(fileSystemOptions);
            if (defaultDateFormat != null)
            {
                config.setDefaultDateFormatStr(defaultDateFormat);
            }
            String recentDateFormat =
                FtpFileSystemConfigBuilder.getInstance().getRecentDateFormat(fileSystemOptions);
            if (recentDateFormat != null)
            {
                config.setRecentDateFormatStr(recentDateFormat);
            }
            String serverTimeZoneId =
                FtpFileSystemConfigBuilder.getInstance().getServerTimeZoneId(fileSystemOptions);
            if (serverTimeZoneId != null)
            {
                config.setServerTimeZoneId(serverTimeZoneId);
            }
            String[] shortMonthNames =
                FtpFileSystemConfigBuilder.getInstance().getShortMonthNames(fileSystemOptions);
            if (shortMonthNames != null)
            {
                StringBuilder shortMonthNamesStr = new StringBuilder(BUFSZ);
                for (int i = 0; i < shortMonthNames.length; i++)
                {
                    if (shortMonthNamesStr.length() > 0)
                    {
                        shortMonthNamesStr.append("|");
                    }
                    shortMonthNamesStr.append(shortMonthNames[i]);
                }
                config.setShortMonthNames(shortMonthNamesStr.toString());
            }

            client.configure(config);
        }
    }
}
