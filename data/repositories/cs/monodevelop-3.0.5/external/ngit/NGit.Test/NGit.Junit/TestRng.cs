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

using Sharpen;

namespace NGit.Junit
{
/// <summary>Toy RNG to ensure we get predictable numbers during unit tests.</summary>
/// <remarks>Toy RNG to ensure we get predictable numbers during unit tests.</remarks>
public class TestRng
{
    private int next;

    /// <summary>Create a new random number generator, seeded by a string.</summary>
    /// <remarks>Create a new random number generator, seeded by a string.</remarks>
    /// <param name="seed">seed to bootstrap, usually this is the test method name.</param>
    public TestRng(string seed)
    {
        next = 0;
        for (int i = 0; i < seed.Length; i++)
        {
            next = next * 11 + seed[i];
        }
    }

    /// <summary>
    /// Get the next
    /// <code>cnt</code>
    /// bytes of random data.
    /// </summary>
    /// <param name="cnt">number of random bytes to produce.</param>
    /// <returns>
    /// array of
    /// <code>cnt</code>
    /// randomly generated bytes.
    /// </returns>
    public virtual byte[] NextBytes(int cnt)
    {
        byte[] r = new byte[cnt];
        for (int i = 0; i < cnt; i++)
        {
            r[i] = unchecked((byte)NextInt());
        }
        return r;
    }

    /// <returns>the next random integer.</returns>
    public virtual int NextInt()
    {
        next = next * 1103515245 + 12345;
        return next;
    }
}
}
