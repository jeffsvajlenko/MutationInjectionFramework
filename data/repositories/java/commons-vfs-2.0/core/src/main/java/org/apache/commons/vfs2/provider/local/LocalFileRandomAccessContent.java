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
package org.apache.commons.vfs2.provider.local;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.provider.AbstractRandomAccessContent;
import org.apache.commons.vfs2.util.RandomAccessMode;

/**
 * RandomAccess for local files
 *
 * @author <a href="http://commons.apache.org/vfs/team-list.html">Commons VFS team</a>
 */
class LocalFileRandomAccessContent extends AbstractRandomAccessContent
{
    // private final LocalFile localFile;
    private final RandomAccessFile raf;
    private final InputStream rafis;

    LocalFileRandomAccessContent(final File localFile, final RandomAccessMode mode) throws FileSystemException
    {
        super(mode);

        try
        {
            raf = new RandomAccessFile(localFile, mode.getModeString());
            rafis = new InputStream()
            {
                @Override
                public int read() throws IOException
                {
                    try
                    {
                        return raf.readByte();
                    }
                    catch (EOFException e)
                    {
                        return -1;
                    }
                }

                @Override
                public long skip(long n) throws IOException
                {
                    raf.seek(raf.getFilePointer() + n);
                    return n;
                }

                @Override
                public void close() throws IOException
                {
                    raf.close();
                }

                @Override
                public int read(byte[] b) throws IOException
                {
                    return raf.read(b);
                }

                @Override
                public int read(byte[] b, int off, int len) throws IOException
                {
                    return raf.read(b, off, len);
                }

                @Override
                public int available() throws IOException
                {
                    long available = raf.length() - raf.getFilePointer();
                    if (available > Integer.MAX_VALUE)
                    {
                        return Integer.MAX_VALUE;
                    }

                    return (int) available;
                }
            };
        }
        catch (FileNotFoundException e)
        {
            throw new FileSystemException("vfs.provider/random-access-open-failed.error", localFile);
        }
    }

    public long getFilePointer() throws IOException
    {
        return raf.getFilePointer();
    }

    public void seek(long pos) throws IOException
    {
        raf.seek(pos);
    }

    public long length() throws IOException
    {
        return raf.length();
    }

    public void close() throws IOException
    {
        raf.close();
    }

    public byte readByte() throws IOException
    {
        return raf.readByte();
    }

    public char readChar() throws IOException
    {
        return raf.readChar();
    }

    public double readDouble() throws IOException
    {
        return raf.readDouble();
    }

    public float readFloat() throws IOException
    {
        return raf.readFloat();
    }

    public int readInt() throws IOException
    {
        return raf.readInt();
    }

    public int readUnsignedByte() throws IOException
    {
        return raf.readUnsignedByte();
    }

    public int readUnsignedShort() throws IOException
    {
        return raf.readUnsignedShort();
    }

    public long readLong() throws IOException
    {
        return raf.readLong();
    }

    public short readShort() throws IOException
    {
        return raf.readShort();
    }

    public boolean readBoolean() throws IOException
    {
        return raf.readBoolean();
    }

    public int skipBytes(int n) throws IOException
    {
        return raf.skipBytes(n);
    }

    public void readFully(byte[] b) throws IOException
    {
        raf.readFully(b);
    }

    public void readFully(byte[] b, int off, int len) throws IOException
    {
        raf.readFully(b, off, len);
    }

    public String readUTF() throws IOException
    {
        return raf.readUTF();
    }

    @Override
    public void writeDouble(double v) throws IOException
    {
        raf.writeDouble(v);
    }

    @Override
    public void writeFloat(float v) throws IOException
    {
        raf.writeFloat(v);
    }

    @Override
    public void write(int b) throws IOException
    {
        raf.write(b);
    }

    @Override
    public void writeByte(int v) throws IOException
    {
        raf.writeByte(v);
    }

    @Override
    public void writeChar(int v) throws IOException
    {
        raf.writeChar(v);
    }

    @Override
    public void writeInt(int v) throws IOException
    {
        raf.writeInt(v);
    }

    @Override
    public void writeShort(int v) throws IOException
    {
        raf.writeShort(v);
    }

    @Override
    public void writeLong(long v) throws IOException
    {
        raf.writeLong(v);
    }

    @Override
    public void writeBoolean(boolean v) throws IOException
    {
        raf.writeBoolean(v);
    }

    @Override
    public void write(byte[] b) throws IOException
    {
        raf.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
        raf.write(b, off, len);
    }

    @Override
    public void writeBytes(String s) throws IOException
    {
        raf.writeBytes(s);
    }

    @Override
    public void writeChars(String s) throws IOException
    {
        raf.writeChars(s);
    }

    @Override
    public void writeUTF(String str) throws IOException
    {
        raf.writeUTF(str);
    }

    public InputStream getInputStream() throws IOException
    {
        return rafis;
    }
}
