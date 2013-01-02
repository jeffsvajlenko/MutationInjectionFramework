/*
 * Copyright (c) 2000, 2004, Oracle and/or its affiliates. All rights reserved.
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

// -- This file was mechanically generated: Do not edit! -- //

package java.nio;


class ByteBufferAsIntBufferB                  // package-private
    extends IntBuffer
{



    protected final ByteBuffer bb;
    protected final int offset;



    ByteBufferAsIntBufferB(ByteBuffer bb)     // package-private
    {

        super(-1, 0,
              bb.remaining() >> 2,
              bb.remaining() >> 2);
        this.bb = bb;
        // enforce limit == capacity
        int cap = this.capacity();
        this.limit(cap);
        int pos = this.position();
        assert (pos <= cap);
        offset = pos;



    }

    ByteBufferAsIntBufferB(ByteBuffer bb,
                           int mark, int pos, int lim, int cap,
                           int off)
    {

        super(mark, pos, lim, cap);
        this.bb = bb;
        offset = off;



    }

    public IntBuffer slice()
    {
        int pos = this.position();
        int lim = this.limit();
        assert (pos <= lim);
        int rem = (pos <= lim ? lim - pos : 0);
        int off = (pos << 2) + offset;
        assert (off >= 0);
        return new ByteBufferAsIntBufferB(bb, -1, 0, rem, rem, off);
    }

    public IntBuffer duplicate()
    {
        return new ByteBufferAsIntBufferB(bb,
                                          this.markValue(),
                                          this.position(),
                                          this.limit(),
                                          this.capacity(),
                                          offset);
    }

    public IntBuffer asReadOnlyBuffer()
    {

        return new ByteBufferAsIntBufferRB(bb,
                                           this.markValue(),
                                           this.position(),
                                           this.limit(),
                                           this.capacity(),
                                           offset);



    }



    protected int ix(int i)
    {
        return (i << 2) + offset;
    }

    public int get()
    {
        return Bits.getIntB(bb, ix(nextGetIndex()));
    }

    public int get(int i)
    {
        return Bits.getIntB(bb, ix(checkIndex(i)));
    }



    public IntBuffer put(int x)
    {

        Bits.putIntB(bb, ix(nextPutIndex()), x);
        return this;



    }

    public IntBuffer put(int i, int x)
    {

        Bits.putIntB(bb, ix(checkIndex(i)), x);
        return this;



    }

    public IntBuffer compact()
    {

        int pos = position();
        int lim = limit();
        assert (pos <= lim);
        int rem = (pos <= lim ? lim - pos : 0);

        ByteBuffer db = bb.duplicate();
        db.limit(ix(lim));
        db.position(ix(0));
        ByteBuffer sb = db.slice();
        sb.position(pos << 2);
        sb.compact();
        position(rem);
        limit(capacity());
        discardMark();
        return this;



    }

    public boolean isDirect()
    {
        return bb.isDirect();
    }

    public boolean isReadOnly()
    {
        return false;
    }









































    public ByteOrder order()
    {

        return ByteOrder.BIG_ENDIAN;




    }

}
