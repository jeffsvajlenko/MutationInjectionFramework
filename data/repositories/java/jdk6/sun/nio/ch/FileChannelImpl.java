/*
 * Copyright (c) 2000, 2006, Oracle and/or its affiliates. All rights reserved.
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

package sun.nio.ch;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.ref.WeakReference;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.misc.Cleaner;
import sun.security.action.GetPropertyAction;


public class FileChannelImpl
    extends FileChannel
{

    // Used to make native read and write calls
    private static NativeDispatcher nd;

    // Memory allocation size for mapping buffers
    private static long allocationGranularity;

    // Cached field for MappedByteBuffer.isAMappedBuffer
    private static Field isAMappedBufferField;

    // File descriptor
    private FileDescriptor fd;

    // File access mode (immutable)
    private boolean writable;
    private boolean readable;
    private boolean appending;

    // Required to prevent finalization of creating stream (immutable)
    private Object parent;

    // Thread-safe set of IDs of native threads, for signalling
    private NativeThreadSet threads = new NativeThreadSet(2);

    // Lock for operations involving position and size
    private Object positionLock = new Object();

    private FileChannelImpl(FileDescriptor fd, boolean readable,
                            boolean writable, Object parent, boolean append)
    {
        this.fd = fd;
        this.readable = readable;
        this.writable = writable;
        this.parent = parent;
        this.appending = append;
    }

    // Invoked by getChannel() methods
    // of java.io.File{Input,Output}Stream and RandomAccessFile
    //
    public static FileChannel open(FileDescriptor fd,
                                   boolean readable, boolean writable,
                                   Object parent)
    {
        return new FileChannelImpl(fd, readable, writable, parent, false);
    }

    public static FileChannel open(FileDescriptor fd,
                                   boolean readable, boolean writable,
                                   Object parent, boolean append)
    {
        return new FileChannelImpl(fd, readable, writable, parent, append);
    }

    private void ensureOpen() throws IOException
    {
        if (!isOpen())
            throw new ClosedChannelException();
    }


    // -- Standard channel operations --

    protected void implCloseChannel() throws IOException
    {

        nd.preClose(fd);
        threads.signal();

        // Invalidate and release any locks that we still hold
        if (fileLockTable != null)
        {
            fileLockTable.removeAll( new FileLockTable.Releaser()
            {
                public void release(FileLock fl) throws IOException
                {
                    ((FileLockImpl)fl).invalidate();
                    release0(fd, fl.position(), fl.size());
                }
            });
        }

        if (parent != null)
        {

            // Close the fd via the parent stream's close method.  The parent
            // will reinvoke our close method, which is defined in the
            // superclass AbstractInterruptibleChannel, but the isOpen logic in
            // that method will prevent this method from being reinvoked.
            //
            if (parent instanceof FileInputStream)
                ((FileInputStream)parent).close();
            else if (parent instanceof FileOutputStream)
                ((FileOutputStream)parent).close();
            else if (parent instanceof RandomAccessFile)
                ((RandomAccessFile)parent).close();
            else
                assert false;

        }
        else
        {
            nd.close(fd);
        }

    }

    public int read(ByteBuffer dst) throws IOException
    {
        ensureOpen();
        if (!readable)
            throw new NonReadableChannelException();
        synchronized (positionLock)
        {
            int n = 0;
            int ti = -1;
            try
            {
                begin();
                if (!isOpen())
                    return 0;
                ti = threads.add();
                do
                {
                    n = IOUtil.read(fd, dst, -1, nd, positionLock);
                }
                while ((n == IOStatus.INTERRUPTED) && isOpen());
                return IOStatus.normalize(n);
            }
            finally
            {
                threads.remove(ti);
                end(n > 0);
                assert IOStatus.check(n);
            }
        }
    }

    private long read0(ByteBuffer[] dsts) throws IOException
    {
        ensureOpen();
        if (!readable)
            throw new NonReadableChannelException();
        synchronized (positionLock)
        {
            long n = 0;
            int ti = -1;
            try
            {
                begin();
                if (!isOpen())
                    return 0;
                ti = threads.add();
                do
                {
                    n = IOUtil.read(fd, dsts, nd);
                }
                while ((n == IOStatus.INTERRUPTED) && isOpen());
                return IOStatus.normalize(n);
            }
            finally
            {
                threads.remove(ti);
                end(n > 0);
                assert IOStatus.check(n);
            }
        }
    }

    public long read(ByteBuffer[] dsts, int offset, int length)
    throws IOException
    {
        if ((offset < 0) || (length < 0) || (offset > dsts.length - length))
            throw new IndexOutOfBoundsException();
        // ## Fix IOUtil.write so that we can avoid this array copy
        return read0(Util.subsequence(dsts, offset, length));
    }

    public int write(ByteBuffer src) throws IOException
    {
        ensureOpen();
        if (!writable)
            throw new NonWritableChannelException();
        synchronized (positionLock)
        {
            int n = 0;
            int ti = -1;
            try
            {
                begin();
                if (!isOpen())
                    return 0;
                ti = threads.add();
                if (appending)
                    position(size());
                do
                {
                    n = IOUtil.write(fd, src, -1, nd, positionLock);
                }
                while ((n == IOStatus.INTERRUPTED) && isOpen());
                return IOStatus.normalize(n);
            }
            finally
            {
                threads.remove(ti);
                end(n > 0);
                assert IOStatus.check(n);
            }
        }
    }

    private long write0(ByteBuffer[] srcs) throws IOException
    {
        ensureOpen();
        if (!writable)
            throw new NonWritableChannelException();
        synchronized (positionLock)
        {
            long n = 0;
            int ti = -1;
            try
            {
                begin();
                if (!isOpen())
                    return 0;
                ti = threads.add();
                if (appending)
                    position(size());
                do
                {
                    n = IOUtil.write(fd, srcs, nd);
                }
                while ((n == IOStatus.INTERRUPTED) && isOpen());
                return IOStatus.normalize(n);
            }
            finally
            {
                threads.remove(ti);
                end(n > 0);
                assert IOStatus.check(n);
            }
        }
    }

    public long write(ByteBuffer[] srcs, int offset, int length)
    throws IOException
    {
        if ((offset < 0) || (length < 0) || (offset > srcs.length - length))
            throw new IndexOutOfBoundsException();
        // ## Fix IOUtil.write so that we can avoid this array copy
        return write0(Util.subsequence(srcs, offset, length));
    }


    // -- Other operations --

    public long position() throws IOException
    {
        ensureOpen();
        synchronized (positionLock)
        {
            long p = -1;
            int ti = -1;
            try
            {
                begin();
                if (!isOpen())
                    return 0;
                ti = threads.add();
                do
                {
                    p = position0(fd, -1);
                }
                while ((p == IOStatus.INTERRUPTED) && isOpen());
                return IOStatus.normalize(p);
            }
            finally
            {
                threads.remove(ti);
                end(p > -1);
                assert IOStatus.check(p);
            }
        }
    }

    public FileChannel position(long newPosition) throws IOException
    {
        ensureOpen();
        if (newPosition < 0)
            throw new IllegalArgumentException();
        synchronized (positionLock)
        {
            long p = -1;
            int ti = -1;
            try
            {
                begin();
                if (!isOpen())
                    return null;
                ti = threads.add();
                do
                {
                    p  = position0(fd, newPosition);
                }
                while ((p == IOStatus.INTERRUPTED) && isOpen());
                return this;
            }
            finally
            {
                threads.remove(ti);
                end(p > -1);
                assert IOStatus.check(p);
            }
        }
    }

    public long size() throws IOException
    {
        ensureOpen();
        synchronized (positionLock)
        {
            long s = -1;
            int ti = -1;
            try
            {
                begin();
                if (!isOpen())
                    return -1;
                ti = threads.add();
                do
                {
                    s = size0(fd);
                }
                while ((s == IOStatus.INTERRUPTED) && isOpen());
                return IOStatus.normalize(s);
            }
            finally
            {
                threads.remove(ti);
                end(s > -1);
                assert IOStatus.check(s);
            }
        }
    }

    public FileChannel truncate(long size) throws IOException
    {
        ensureOpen();
        if (size < 0)
            throw new IllegalArgumentException();
        if (size > size())
            return this;
        if (!writable)
            throw new NonWritableChannelException();
        synchronized (positionLock)
        {
            int rv = -1;
            long p = -1;
            int ti = -1;
            try
            {
                begin();
                if (!isOpen())
                    return null;
                ti = threads.add();

                // get current position
                do
                {
                    p = position0(fd, -1);
                }
                while ((p == IOStatus.INTERRUPTED) && isOpen());
                if (!isOpen())
                    return null;
                assert p >= 0;

                // truncate file
                do
                {
                    rv = truncate0(fd, size);
                }
                while ((rv == IOStatus.INTERRUPTED) && isOpen());
                if (!isOpen())
                    return null;

                // set position to size if greater than size
                if (p > size)
                    p = size;
                do
                {
                    rv = (int)position0(fd, p);
                }
                while ((rv == IOStatus.INTERRUPTED) && isOpen());
                return this;
            }
            finally
            {
                threads.remove(ti);
                end(rv > -1);
                assert IOStatus.check(rv);
            }
        }
    }

    public void force(boolean metaData) throws IOException
    {
        ensureOpen();
        int rv = -1;
        int ti = -1;
        try
        {
            begin();
            if (!isOpen())
                return;
            ti = threads.add();
            do
            {
                rv = force0(fd, metaData);
            }
            while ((rv == IOStatus.INTERRUPTED) && isOpen());
        }
        finally
        {
            threads.remove(ti);
            end(rv > -1);
            assert IOStatus.check(rv);
        }
    }

    // Assume at first that the underlying kernel supports sendfile();
    // set this to false if we find out later that it doesn't
    //
    private static volatile boolean transferSupported = true;

    // Assume that the underlying kernel sendfile() will work if the target
    // fd is a pipe; set this to false if we find out later that it doesn't
    //
    private static volatile boolean pipeSupported = true;

    // Assume that the underlying kernel sendfile() will work if the target
    // fd is a file; set this to false if we find out later that it doesn't
    //
    private static volatile boolean fileSupported = true;

    private long transferToDirectly(long position, int icount,
                                    WritableByteChannel target)
    throws IOException
    {
        if (!transferSupported)
            return IOStatus.UNSUPPORTED;

        FileDescriptor targetFD = null;
        if (target instanceof FileChannelImpl)
        {
            if (!fileSupported)
                return IOStatus.UNSUPPORTED_CASE;
            targetFD = ((FileChannelImpl)target).fd;
        }
        else if (target instanceof SelChImpl)
        {
            // Direct transfer to pipe causes EINVAL on some configurations
            if ((target instanceof SinkChannelImpl) && !pipeSupported)
                return IOStatus.UNSUPPORTED_CASE;
            targetFD = ((SelChImpl)target).getFD();
        }
        if (targetFD == null)
            return IOStatus.UNSUPPORTED;
        int thisFDVal = IOUtil.fdVal(fd);
        int targetFDVal = IOUtil.fdVal(targetFD);
        if (thisFDVal == targetFDVal) // Not supported on some configurations
            return IOStatus.UNSUPPORTED;

        long n = -1;
        int ti = -1;
        try
        {
            begin();
            if (!isOpen())
                return -1;
            ti = threads.add();
            do
            {
                n = transferTo0(thisFDVal, position, icount, targetFDVal);
            }
            while ((n == IOStatus.INTERRUPTED) && isOpen());
            if (n == IOStatus.UNSUPPORTED_CASE)
            {
                if (target instanceof SinkChannelImpl)
                    pipeSupported = false;
                if (target instanceof FileChannelImpl)
                    fileSupported = false;
                return IOStatus.UNSUPPORTED_CASE;
            }
            if (n == IOStatus.UNSUPPORTED)
            {
                // Don't bother trying again
                transferSupported = false;
                return IOStatus.UNSUPPORTED;
            }
            return IOStatus.normalize(n);
        }
        finally
        {
            threads.remove(ti);
            end (n > -1);
        }
    }

    private long transferToTrustedChannel(long position, int icount,
                                          WritableByteChannel target)
    throws IOException
    {
        if (  !((target instanceof FileChannelImpl)
                || (target instanceof SelChImpl)))
            return IOStatus.UNSUPPORTED;

        // Trusted target: Use a mapped buffer
        MappedByteBuffer dbb = null;
        try
        {
            dbb = map(MapMode.READ_ONLY, position, icount);
            // ## Bug: Closing this channel will not terminate the write
            return target.write(dbb);
        }
        finally
        {
            if (dbb != null)
                unmap(dbb);
        }
    }

    private long transferToArbitraryChannel(long position, int icount,
                                            WritableByteChannel target)
    throws IOException
    {
        // Untrusted target: Use a newly-erased buffer
        int c = Math.min(icount, TRANSFER_SIZE);
        ByteBuffer bb = Util.getTemporaryDirectBuffer(c);
        long tw = 0;                    // Total bytes written
        long pos = position;
        try
        {
            Util.erase(bb);
            while (tw < icount)
            {
                bb.limit(Math.min((int)(icount - tw), TRANSFER_SIZE));
                int nr = read(bb, pos);
                if (nr <= 0)
                    break;
                bb.flip();
                // ## Bug: Will block writing target if this channel
                // ##      is asynchronously closed
                int nw = target.write(bb);
                tw += nw;
                if (nw != nr)
                    break;
                pos += nw;
                bb.clear();
            }
            return tw;
        }
        catch (IOException x)
        {
            if (tw > 0)
                return tw;
            throw x;
        }
        finally
        {
            Util.releaseTemporaryDirectBuffer(bb);
        }
    }

    public long transferTo(long position, long count,
                           WritableByteChannel target)
    throws IOException
    {
        ensureOpen();
        if (!target.isOpen())
            throw new ClosedChannelException();
        if (!readable)
            throw new NonReadableChannelException();
        if (target instanceof FileChannelImpl &&
                !((FileChannelImpl)target).writable)
            throw new NonWritableChannelException();
        if ((position < 0) || (count < 0))
            throw new IllegalArgumentException();
        long sz = size();
        if (position > sz)
            return 0;
        int icount = (int)Math.min(count, Integer.MAX_VALUE);
        if ((sz - position) < icount)
            icount = (int)(sz - position);

        long n;

        // Attempt a direct transfer, if the kernel supports it
        if ((n = transferToDirectly(position, icount, target)) >= 0)
            return n;

        // Attempt a mapped transfer, but only to trusted channel types
        if ((n = transferToTrustedChannel(position, icount, target)) >= 0)
            return n;

        // Slow path for untrusted targets
        return transferToArbitraryChannel(position, icount, target);
    }

    private long transferFromFileChannel(FileChannelImpl src,
                                         long position, long count)
    throws IOException
    {
        // Note we could loop here to accumulate more at once
        synchronized (src.positionLock)
        {
            long p = src.position();
            int icount = (int)Math.min(Math.min(count, Integer.MAX_VALUE),
                                       src.size() - p);
            // ## Bug: Closing this channel will not terminate the write
            MappedByteBuffer bb = src.map(MapMode.READ_ONLY, p, icount);
            try
            {
                long n = write(bb, position);
                src.position(p + n);
                return n;
            }
            finally
            {
                unmap(bb);
            }
        }
    }

    private static final int TRANSFER_SIZE = 8192;

    private long transferFromArbitraryChannel(ReadableByteChannel src,
            long position, long count)
    throws IOException
    {
        // Untrusted target: Use a newly-erased buffer
        int c = (int)Math.min(count, TRANSFER_SIZE);
        ByteBuffer bb = Util.getTemporaryDirectBuffer(c);
        long tw = 0;                    // Total bytes written
        long pos = position;
        try
        {
            Util.erase(bb);
            while (tw < count)
            {
                bb.limit((int)Math.min((count - tw), (long)TRANSFER_SIZE));
                // ## Bug: Will block reading src if this channel
                // ##      is asynchronously closed
                int nr = src.read(bb);
                if (nr <= 0)
                    break;
                bb.flip();
                int nw = write(bb, pos);
                tw += nw;
                if (nw != nr)
                    break;
                pos += nw;
                bb.clear();
            }
            return tw;
        }
        catch (IOException x)
        {
            if (tw > 0)
                return tw;
            throw x;
        }
        finally
        {
            Util.releaseTemporaryDirectBuffer(bb);
        }
    }

    public long transferFrom(ReadableByteChannel src,
                             long position, long count)
    throws IOException
    {
        ensureOpen();
        if (!src.isOpen())
            throw new ClosedChannelException();
        if (!writable)
            throw new NonWritableChannelException();
        if ((position < 0) || (count < 0))
            throw new IllegalArgumentException();
        if (position > size())
            return 0;
        if (src instanceof FileChannelImpl)
            return transferFromFileChannel((FileChannelImpl)src,
                                           position, count);

        return transferFromArbitraryChannel(src, position, count);
    }

    public int read(ByteBuffer dst, long position) throws IOException
    {
        if (dst == null)
            throw new NullPointerException();
        if (position < 0)
            throw new IllegalArgumentException("Negative position");
        if (!readable)
            throw new NonReadableChannelException();
        ensureOpen();
        int n = 0;
        int ti = -1;
        try
        {
            begin();
            if (!isOpen())
                return -1;
            ti = threads.add();
            do
            {
                n = IOUtil.read(fd, dst, position, nd, positionLock);
            }
            while ((n == IOStatus.INTERRUPTED) && isOpen());
            return IOStatus.normalize(n);
        }
        finally
        {
            threads.remove(ti);
            end(n > 0);
            assert IOStatus.check(n);
        }
    }

    public int write(ByteBuffer src, long position) throws IOException
    {
        if (src == null)
            throw new NullPointerException();
        if (position < 0)
            throw new IllegalArgumentException("Negative position");
        if (!writable)
            throw new NonWritableChannelException();
        ensureOpen();
        int n = 0;
        int ti = -1;
        try
        {
            begin();
            if (!isOpen())
                return -1;
            ti = threads.add();
            do
            {
                n = IOUtil.write(fd, src, position, nd, positionLock);
            }
            while ((n == IOStatus.INTERRUPTED) && isOpen());
            return IOStatus.normalize(n);
        }
        finally
        {
            threads.remove(ti);
            end(n > 0);
            assert IOStatus.check(n);
        }
    }


    // -- Memory-mapped buffers --

    private static class Unmapper
        implements Runnable
    {

        private long address;
        private long size;

        private Unmapper(long address, long size)
        {
            assert (address != 0);
            this.address = address;
            this.size = size;
        }

        public void run()
        {
            if (address == 0)
                return;
            unmap0(address, size);
            address = 0;
        }

    }

    private static void unmap(MappedByteBuffer bb)
    {
        Cleaner cl = ((DirectBuffer)bb).cleaner();
        if (cl != null)
            cl.clean();
    }

    private static final int MAP_RO = 0;
    private static final int MAP_RW = 1;
    private static final int MAP_PV = 2;

    public MappedByteBuffer map(MapMode mode, long position, long size)
    throws IOException
    {
        ensureOpen();
        if (position < 0L)
            throw new IllegalArgumentException("Negative position");
        if (size < 0L)
            throw new IllegalArgumentException("Negative size");
        if (position + size < 0)
            throw new IllegalArgumentException("Position + size overflow");
        if (size > Integer.MAX_VALUE)
            throw new IllegalArgumentException("Size exceeds Integer.MAX_VALUE");
        int imode = -1;
        if (mode == MapMode.READ_ONLY)
            imode = MAP_RO;
        else if (mode == MapMode.READ_WRITE)
            imode = MAP_RW;
        else if (mode == MapMode.PRIVATE)
            imode = MAP_PV;
        assert (imode >= 0);
        if ((mode != MapMode.READ_ONLY) && !writable)
            throw new NonWritableChannelException();
        if (!readable)
            throw new NonReadableChannelException();

        long addr = -1;
        int ti = -1;
        try
        {
            begin();
            if (!isOpen())
                return null;
            ti = threads.add();
            if (size() < position + size)   // Extend file size
            {
                if (!writable)
                {
                    throw new IOException("Channel not open for writing " +
                                          "- cannot extend file to required size");
                }
                int rv;
                do
                {
                    rv = truncate0(fd, position + size);
                }
                while ((rv == IOStatus.INTERRUPTED) && isOpen());
            }
            if (size == 0)
            {
                addr = 0;
                if ((!writable) || (imode == MAP_RO))
                    return Util.newMappedByteBufferR(0, 0, null);
                else
                    return Util.newMappedByteBuffer(0, 0, null);
            }

            int pagePosition = (int)(position % allocationGranularity);
            long mapPosition = position - pagePosition;
            long mapSize = size + pagePosition;
            try
            {
                // If no exception was thrown from map0, the address is valid
                addr = map0(imode, mapPosition, mapSize);
            }
            catch (OutOfMemoryError x)
            {
                // An OutOfMemoryError may indicate that we've exhausted memory
                // so force gc and re-attempt map
                System.gc();
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException y)
                {
                    Thread.currentThread().interrupt();
                }
                try
                {
                    addr = map0(imode, mapPosition, mapSize);
                }
                catch (OutOfMemoryError y)
                {
                    // After a second OOME, fail
                    throw new IOException("Map failed", y);
                }
            }

            assert (IOStatus.checkAll(addr));
            assert (addr % allocationGranularity == 0);
            int isize = (int)size;
            Unmapper um = new Unmapper(addr, size + pagePosition);
            if ((!writable) || (imode == MAP_RO))
                return Util.newMappedByteBufferR(isize, addr + pagePosition, um);
            else
                return Util.newMappedByteBuffer(isize, addr + pagePosition, um);
        }
        finally
        {
            threads.remove(ti);
            end(IOStatus.checkAll(addr));
        }
    }


    // -- Locks --

    public static final int NO_LOCK = -1;       // Failed to lock
    public static final int LOCKED = 0;         // Obtained requested lock
    public static final int RET_EX_LOCK = 1;    // Obtained exclusive lock
    public static final int INTERRUPTED = 2;    // Request interrupted

    // keeps track of locks on this file
    private volatile FileLockTable fileLockTable;

    // indicates if file locks are maintained system-wide (as per spec)
    private static boolean isSharedFileLockTable;

    // indicates if the disableSystemWideOverlappingFileLockCheck property
    // has been checked
    private static volatile boolean propertyChecked;

    // The lock list in J2SE 1.4/5.0 was local to each FileChannel instance so
    // the overlap check wasn't system wide when there were multiple channels to
    // the same file. This property is used to get 1.4/5.0 behavior if desired.
    private static boolean isSharedFileLockTable()
    {
        if (!propertyChecked)
        {
            synchronized (FileChannelImpl.class)
            {
                if (!propertyChecked)
                {
                    String value = AccessController.doPrivileged(
                                       new GetPropertyAction(
                                           "sun.nio.ch.disableSystemWideOverlappingFileLockCheck"));
                    isSharedFileLockTable = ((value == null) || value.equals("false"));
                    propertyChecked = true;
                }
            }
        }
        return isSharedFileLockTable;
    }

    private FileLockTable fileLockTable()
    {
        if (fileLockTable == null)
        {
            synchronized (this)
            {
                if (fileLockTable == null)
                {
                    fileLockTable = isSharedFileLockTable() ?
                                    new SharedFileLockTable(this) : new SimpleFileLockTable();
                }
            }
        }
        return fileLockTable;
    }

    public FileLock lock(long position, long size, boolean shared)
    throws IOException
    {
        ensureOpen();
        if (shared && !readable)
            throw new NonReadableChannelException();
        if (!shared && !writable)
            throw new NonWritableChannelException();
        FileLockImpl fli = new FileLockImpl(this, position, size, shared);
        FileLockTable flt = fileLockTable();
        flt.add(fli);
        boolean i = true;
        int ti = -1;
        try
        {
            begin();
            if (!isOpen())
                return null;
            ti = threads.add();
            int result = lock0(fd, true, position, size, shared);
            if (result == RET_EX_LOCK)
            {
                assert shared;
                FileLockImpl fli2 = new FileLockImpl(this, position, size,
                                                     false);
                flt.replace(fli, fli2);
                return fli2;
            }
            if (result == INTERRUPTED || result == NO_LOCK)
            {
                flt.remove(fli);
                i = false;
            }
        }
        catch (IOException e)
        {
            flt.remove(fli);
            throw e;
        }
        finally
        {
            threads.remove(ti);
            try
            {
                end(i);
            }
            catch (ClosedByInterruptException e)
            {
                throw new FileLockInterruptionException();
            }
        }
        return fli;
    }

    public FileLock tryLock(long position, long size, boolean shared)
    throws IOException
    {
        ensureOpen();
        if (shared && !readable)
            throw new NonReadableChannelException();
        if (!shared && !writable)
            throw new NonWritableChannelException();
        FileLockImpl fli = new FileLockImpl(this, position, size, shared);
        FileLockTable flt = fileLockTable();
        flt.add(fli);
        int result = lock0(fd, false, position, size, shared);
        if (result == NO_LOCK)
        {
            flt.remove(fli);
            return null;
        }
        if (result == RET_EX_LOCK)
        {
            assert shared;
            FileLockImpl fli2 = new FileLockImpl(this, position, size,
                                                 false);
            flt.replace(fli, fli2);
            return fli2;
        }
        return fli;
    }

    void release(FileLockImpl fli) throws IOException
    {
        ensureOpen();
        release0(fd, fli.position(), fli.size());
        assert fileLockTable != null;
        fileLockTable.remove(fli);
    }


    // -- File lock support  --

    /**
     * A table of FileLocks.
     */
    private interface FileLockTable
    {
        /**
         * Adds a file lock to the table.
         *
         * @throws OverlappingFileLockException if the file lock overlaps
         *         with an existing file lock in the table
         */
        void add(FileLock fl) throws OverlappingFileLockException;

        /**
         * Remove an existing file lock from the table.
         */
        void remove(FileLock fl);

        /**
         * An implementation of this interface releases a given file lock.
         * Used with removeAll.
         */
        interface Releaser
        {
            void release(FileLock fl) throws IOException;
        }

        /**
         * Removes all file locks from the table.
         * <p>
         * The Releaser#release method is invoked for each file lock before
         * it is removed.
         *
         * @throws IOException if the release method throws IOException
         */
        void removeAll(Releaser r) throws IOException;

        /**
         * Replaces an existing file lock in the table.
         */
        void replace(FileLock fl1, FileLock fl2);
    }

    /**
     * A simple file lock table that maintains a list of FileLocks obtained by a
     * FileChannel. Use to get 1.4/5.0 behaviour.
     */
    private static class SimpleFileLockTable implements FileLockTable
    {
        // synchronize on list for access
        private List<FileLock> lockList = new ArrayList<FileLock>(2);

        public SimpleFileLockTable()
        {
        }

        private void checkList(long position, long size)
        throws OverlappingFileLockException
        {
            assert Thread.holdsLock(lockList);
            for (FileLock fl: lockList)
            {
                if (fl.overlaps(position, size))
                {
                    throw new OverlappingFileLockException();
                }
            }
        }

        public void add(FileLock fl) throws OverlappingFileLockException
        {
            synchronized (lockList)
            {
                checkList(fl.position(), fl.size());
                lockList.add(fl);
            }
        }

        public void remove(FileLock fl)
        {
            synchronized (lockList)
            {
                lockList.remove(fl);
            }
        }

        public void removeAll(Releaser releaser) throws IOException
        {
            synchronized(lockList)
            {
                Iterator<FileLock> i = lockList.iterator();
                while (i.hasNext())
                {
                    FileLock fl = i.next();
                    releaser.release(fl);
                    i.remove();
                }
            }
        }

        public void replace(FileLock fl1, FileLock fl2)
        {
            synchronized (lockList)
            {
                lockList.remove(fl1);
                lockList.add(fl2);
            }
        }
    }

    /**
     * A weak reference to a FileLock.
     * <p>
     * SharedFileLockTable uses a list of file lock references to avoid keeping the
     * FileLock (and FileChannel) alive.
     */
    private static class FileLockReference extends WeakReference<FileLock>
    {
        private FileKey fileKey;

        FileLockReference(FileLock referent,
                          ReferenceQueue queue,
                          FileKey key)
        {
            super(referent, queue);
            this.fileKey = key;
        }

        private FileKey fileKey()
        {
            return fileKey;
        }
    }

    /**
     * A file lock table that is over a system-wide map of all file locks.
     */
    private static class SharedFileLockTable implements FileLockTable
    {
        // The system-wide map is a ConcurrentHashMap that is keyed on the FileKey.
        // The map value is a list of file locks represented by FileLockReferences.
        // All access to the list must be synchronized on the list.
        private static ConcurrentHashMap<FileKey, ArrayList<FileLockReference>> lockMap =
            new ConcurrentHashMap<FileKey, ArrayList<FileLockReference>>();

        // reference queue for cleared refs
        private static ReferenceQueue queue = new ReferenceQueue();

        // the enclosing file channel
        private FileChannelImpl fci;

        // File key for the file that this channel is connected to
        private FileKey fileKey;

        public SharedFileLockTable(FileChannelImpl fci)
        {
            this.fci = fci;
            this.fileKey = FileKey.create(fci.fd);
        }

        public void add(FileLock fl) throws OverlappingFileLockException
        {
            ArrayList<FileLockReference> list = lockMap.get(fileKey);

            for (;;)
            {

                // The key isn't in the map so we try to create it atomically
                if (list == null)
                {
                    list = new ArrayList<FileLockReference>(2);
                    ArrayList<FileLockReference> prev;
                    synchronized (list)
                    {
                        prev = lockMap.putIfAbsent(fileKey, list);
                        if (prev == null)
                        {
                            // we successfully created the key so we add the file lock
                            list.add(new FileLockReference(fl, queue, fileKey));
                            break;
                        }
                    }
                    // someone else got there first
                    list = prev;
                }

                // There is already a key. It is possible that some other thread
                // is removing it so we re-fetch the value from the map. If it
                // hasn't changed then we check the list for overlapping locks
                // and add the new lock to the list.
                synchronized (list)
                {
                    ArrayList<FileLockReference> current = lockMap.get(fileKey);
                    if (list == current)
                    {
                        checkList(list, fl.position(), fl.size());
                        list.add(new FileLockReference(fl, queue, fileKey));
                        break;
                    }
                    list = current;
                }

            }

            // process any stale entries pending in the reference queue
            removeStaleEntries();
        }

        private void removeKeyIfEmpty(FileKey fk, ArrayList<FileLockReference> list)
        {
            assert Thread.holdsLock(list);
            assert lockMap.get(fk) == list;
            if (list.isEmpty())
            {
                lockMap.remove(fk);
            }
        }

        public void remove(FileLock fl)
        {
            assert fl != null;

            // the lock must exist so the list of locks must be present
            ArrayList<FileLockReference> list = lockMap.get(fileKey);
            assert list != null;

            synchronized (list)
            {
                int index = 0;
                while (index < list.size())
                {
                    FileLockReference ref = list.get(index);
                    FileLock lock = ref.get();
                    if (lock == fl)
                    {
                        assert (lock != null) && (lock.channel() == fci);
                        ref.clear();
                        list.remove(index);
                        break;
                    }
                    index++;
                }
            }
        }

        public void removeAll(Releaser releaser) throws IOException
        {
            ArrayList<FileLockReference> list = lockMap.get(fileKey);
            if (list != null)
            {
                synchronized (list)
                {
                    int index = 0;
                    while (index < list.size())
                    {
                        FileLockReference ref = list.get(index);
                        FileLock lock = ref.get();

                        // remove locks obtained by this channel
                        if (lock != null && lock.channel() == fci)
                        {
                            // invoke the releaser to invalidate/release the lock
                            releaser.release(lock);

                            // remove the lock from the list
                            ref.clear();
                            list.remove(index);
                        }
                        else
                        {
                            index++;
                        }
                    }

                    // once the lock list is empty we remove it from the map
                    removeKeyIfEmpty(fileKey, list);
                }
            }
        }

        public void replace(FileLock fromLock, FileLock toLock)
        {
            // the lock must exist so there must be a list
            ArrayList<FileLockReference> list = lockMap.get(fileKey);
            assert list != null;

            synchronized (list)
            {
                for (int index=0; index<list.size(); index++)
                {
                    FileLockReference ref = list.get(index);
                    FileLock lock = ref.get();
                    if (lock == fromLock)
                    {
                        ref.clear();
                        list.set(index, new FileLockReference(toLock, queue, fileKey));
                        break;
                    }
                }
            }
        }

        // Check for overlapping file locks
        private void checkList(List<FileLockReference> list, long position, long size)
        throws OverlappingFileLockException
        {
            assert Thread.holdsLock(list);
            for (FileLockReference ref: list)
            {
                FileLock fl = ref.get();
                if (fl != null && fl.overlaps(position, size))
                    throw new OverlappingFileLockException();
            }
        }

        // Process the reference queue
        private void removeStaleEntries()
        {
            FileLockReference ref;
            while ((ref = (FileLockReference)queue.poll()) != null)
            {
                FileKey fk = ref.fileKey();
                ArrayList<FileLockReference> list = lockMap.get(fk);
                if (list != null)
                {
                    synchronized (list)
                    {
                        list.remove(ref);
                        removeKeyIfEmpty(fk, list);
                    }
                }
            }
        }
    }

    // -- Native methods --

    // Grabs a file lock
    native int lock0(FileDescriptor fd, boolean blocking, long pos, long size,
                     boolean shared) throws IOException;

    // Releases a file lock
    native void release0(FileDescriptor fd, long pos, long size)
    throws IOException;

    // Creates a new mapping
    private native long map0(int prot, long position, long length)
    throws IOException;

    // Removes an existing mapping
    private static native int unmap0(long address, long length);

    // Forces output to device
    private native int force0(FileDescriptor fd, boolean metaData);

    // Truncates a file
    private native int truncate0(FileDescriptor fd, long size);

    // Transfers from src to dst, or returns -2 if kernel can't do that
    private native long transferTo0(int src, long position, long count, int dst);

    // Sets or reports this file's position
    // If offset is -1, the current position is returned
    // otherwise the position is set to offset
    private native long position0(FileDescriptor fd, long offset);

    // Reports this file's size
    private native long size0(FileDescriptor fd);

    // Caches fieldIDs
    private static native long initIDs();

    static
    {
        Util.load();
        allocationGranularity = initIDs();
        nd = new FileDispatcher();
        isAMappedBufferField = Reflect.lookupField("java.nio.MappedByteBuffer",
                               "isAMappedBuffer");
    }

}
