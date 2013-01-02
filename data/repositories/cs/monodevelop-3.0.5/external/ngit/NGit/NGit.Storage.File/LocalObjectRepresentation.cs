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

using System;
using System.IO;
using NGit;
using NGit.Storage.File;
using NGit.Storage.Pack;
using Sharpen;

namespace NGit.Storage.File
{
internal class LocalObjectRepresentation : StoredObjectRepresentation
{
    internal static LocalObjectRepresentation NewWhole(PackFile f, long p, long length
                                                      )
    {
        LocalObjectRepresentation r = new _LocalObjectRepresentation_53();
        r.pack = f;
        r.offset = p;
        r.length = length;
        return r;
    }

    private sealed class _LocalObjectRepresentation_53 : LocalObjectRepresentation
    {
        public _LocalObjectRepresentation_53()
        {
        }

        public override int GetFormat()
        {
            return StoredObjectRepresentation.PACK_WHOLE;
        }
    }

    internal static LocalObjectRepresentation NewDelta(PackFile f, long p, long n, ObjectId
            @base)
    {
        LocalObjectRepresentation r = new LocalObjectRepresentation.Delta();
        r.pack = f;
        r.offset = p;
        r.length = n;
        r.baseId = @base;
        return r;
    }

    internal static LocalObjectRepresentation NewDelta(PackFile f, long p, long n, long
            @base)
    {
        LocalObjectRepresentation r = new LocalObjectRepresentation.Delta();
        r.pack = f;
        r.offset = p;
        r.length = n;
        r.baseOffset = @base;
        return r;
    }

    internal PackFile pack;

    internal long offset;

    internal long length;

    private long baseOffset;

    private ObjectId baseId;

    public override int GetWeight()
    {
        return (int)Math.Min(length, int.MaxValue);
    }

    public override ObjectId GetDeltaBase()
    {
        if (baseId == null && GetFormat() == PACK_DELTA)
        {
            try
            {
                baseId = pack.FindObjectForOffset(baseOffset);
            }
            catch (IOException)
            {
                return null;
            }
        }
        return baseId;
    }

    private sealed class Delta : LocalObjectRepresentation
    {
        public override int GetFormat()
        {
            return PACK_DELTA;
        }
    }
}
}
