/*
This code is derived from jgit (http://eclipse.org/jgit).
Copyright owners are documented in jgit's IP log.

This program and the accompanying materials are made available
under the terms of the Eclipse Distribution License v1.0 which
accompanies this distribution, is reproduced below, and is
available at http://www.eclipse.org/org/documents/edl-v10.php

All rights reserved.

Redistribution and use in source and binary forms, with or
without modification, are permitted provided that the following
conditions are met:

- Redistributions of source code must retain the above copyright
  notice, this list of conditions and the following disclaimer.

- Redistributions in binary form must reproduce the above
  copyright notice, this list of conditions and the following
  disclaimer in the documentation and/or other materials provided
  with the distribution.

- Neither the name of the Eclipse Foundation, Inc. nor the
  names of its contributors may be used to endorse or promote
  products derived from this software without specific prior
  written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

using NGit.Storage.File;
using Sharpen;

namespace NGit.Storage.File
{
internal class DeltaBaseCache
{
    private const int CACHE_SZ = 1024;

    private static readonly SoftReference<DeltaBaseCache.Entry> DEAD;

    private static int Hash(long position)
    {
        return (int)(((uint)(((int)position) << 22)) >> 22);
    }

    private static volatile int defaultMaxByteCount;

    private readonly int maxByteCount;

    private readonly DeltaBaseCache.Slot[] cache;

    private DeltaBaseCache.Slot lruHead;

    private DeltaBaseCache.Slot lruTail;

    private int openByteCount;

    static DeltaBaseCache()
    {
        DEAD = new SoftReference<DeltaBaseCache.Entry>(null);
        Reconfigure(new WindowCacheConfig());
    }

    internal static void Reconfigure(WindowCacheConfig cfg)
    {
        defaultMaxByteCount = cfg.GetDeltaBaseCacheLimit();
    }

    public DeltaBaseCache()
    {
        maxByteCount = defaultMaxByteCount;
        cache = new DeltaBaseCache.Slot[CACHE_SZ];
    }

    internal virtual DeltaBaseCache.Entry Get(PackFile pack, long position)
    {
        DeltaBaseCache.Slot e = cache[Hash(position)];
        if (e == null)
        {
            return null;
        }
        if (e.provider == pack && e.position == position)
        {
            DeltaBaseCache.Entry buf = e.data.Get();
            if (buf != null)
            {
                MoveToHead(e);
                return buf;
            }
        }
        return null;
    }

    internal virtual void Store(PackFile pack, long position, byte[] data, int objectType
                               )
    {
        if (data.Length > maxByteCount)
        {
            return;
        }
        // Too large to cache.
        DeltaBaseCache.Slot e = cache[Hash(position)];
        if (e == null)
        {
            e = new DeltaBaseCache.Slot();
            cache[Hash(position)] = e;
        }
        else
        {
            ClearEntry(e);
        }
        openByteCount += data.Length;
        ReleaseMemory();
        e.provider = pack;
        e.position = position;
        e.sz = data.Length;
        e.data = new SoftReference<DeltaBaseCache.Entry>(new DeltaBaseCache.Entry(data, objectType
                                                                                 ));
        MoveToHead(e);
    }

    private void ReleaseMemory()
    {
        while (openByteCount > maxByteCount && lruTail != null)
        {
            DeltaBaseCache.Slot currOldest = lruTail;
            DeltaBaseCache.Slot nextOldest = currOldest.lruPrev;
            ClearEntry(currOldest);
            currOldest.lruPrev = null;
            currOldest.lruNext = null;
            if (nextOldest == null)
            {
                lruHead = null;
            }
            else
            {
                nextOldest.lruNext = null;
            }
            lruTail = nextOldest;
        }
    }

    private void MoveToHead(DeltaBaseCache.Slot e)
    {
        Unlink(e);
        e.lruPrev = null;
        e.lruNext = lruHead;
        if (lruHead != null)
        {
            lruHead.lruPrev = e;
        }
        else
        {
            lruTail = e;
        }
        lruHead = e;
    }

    private void Unlink(DeltaBaseCache.Slot e)
    {
        DeltaBaseCache.Slot prev = e.lruPrev;
        DeltaBaseCache.Slot next = e.lruNext;
        if (prev != null)
        {
            prev.lruNext = next;
        }
        if (next != null)
        {
            next.lruPrev = prev;
        }
    }

    private void ClearEntry(DeltaBaseCache.Slot e)
    {
        openByteCount -= e.sz;
        e.provider = null;
        e.data = DEAD;
        e.sz = 0;
    }

    internal class Entry
    {
        internal readonly byte[] data;

        internal readonly int type;

        internal Entry(byte[] aData, int aType)
        {
            data = aData;
            type = aType;
        }
    }

    private class Slot
    {
        internal DeltaBaseCache.Slot lruPrev;

        internal DeltaBaseCache.Slot lruNext;

        internal PackFile provider;

        internal long position;

        internal int sz;

        internal SoftReference<DeltaBaseCache.Entry> data = DEAD;
    }
}
}
