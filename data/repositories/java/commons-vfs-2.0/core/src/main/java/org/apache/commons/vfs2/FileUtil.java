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
package org.apache.commons.vfs2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility methods for dealng with FileObjects.
 *
 * @author <a href="http://commons.apache.org/vfs/team-list.html">Commons VFS team</a>
 */
public final class FileUtil
{

    /** The buffer size */
    private static final int BUFFER_SIZE = 1024;

    private FileUtil()
    {
    }

    /**
     * Returns the content of a file, as a byte array.
     *
     * @param file The file to get the content of.
     * @return The content as a byte array.
     * @throws IOException if the file content cannot be accessed.
     */
    public static byte[] getContent(final FileObject file)
    throws IOException
    {
        final FileContent content = file.getContent();
        final int size = (int) content.getSize();
        final byte[] buf = new byte[size];

        final InputStream in = content.getInputStream();
        try
        {
            int read = 0;
            for (int pos = 0; pos < size && read >= 0; pos += read)
            {
                read = in.read(buf, pos, size - pos);
            }
        }
        finally
        {
            in.close();
        }

        return buf;
    }

    /**
     * Writes the content of a file to an OutputStream.
     * @param file The FileObject to write.
     * @param outstr The OutputStream to write to.
     * @throws IOException if an error occurs writing the file.
     */
    public static void writeContent(final FileObject file,
                                    final OutputStream outstr)
    throws IOException
    {
        final InputStream instr = file.getContent().getInputStream();
        try
        {
            final byte[] buffer = new byte[BUFFER_SIZE];
            while (true)
            {
                final int nread = instr.read(buffer);
                if (nread < 0)
                {
                    break;
                }
                outstr.write(buffer, 0, nread);
            }
        }
        finally
        {
            instr.close();
        }
    }

    /**
     * Copies the content from a source file to a destination file.
     * @param srcFile The source FileObject.
     * @param destFile The target FileObject
     * @throws IOException If an error occurs copying the file.
     */
    public static void copyContent(final FileObject srcFile,
                                   final FileObject destFile)
    throws IOException
    {
        // Create the output stream via getContent(), to pick up the
        // validation it does
        final OutputStream outstr = destFile.getContent().getOutputStream();
        try
        {
            writeContent(srcFile, outstr);
        }
        finally
        {
            outstr.close();
        }
    }

}
