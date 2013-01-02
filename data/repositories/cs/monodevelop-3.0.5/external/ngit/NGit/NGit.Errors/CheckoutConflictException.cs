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

using System.IO;
using System.Text;
using NGit.Internal;
using Sharpen;

namespace NGit.Errors
{
/// <summary>Exception thrown if a conflict occurs during a merge checkout.</summary>
/// <remarks>Exception thrown if a conflict occurs during a merge checkout.</remarks>
[System.Serializable]
public class CheckoutConflictException : IOException
{
    private const long serialVersionUID = 1L;

    /// <summary>Construct a CheckoutConflictException for the specified file</summary>
    /// <param name="file"></param>
    public CheckoutConflictException(string file) : base(MessageFormat.Format(JGitText
            .Get().checkoutConflictWithFile, file))
    {
    }

    /// <summary>Construct a CheckoutConflictException for the specified set of files</summary>
    /// <param name="files"></param>
    public CheckoutConflictException(string[] files) : base(MessageFormat.Format(JGitText
            .Get().checkoutConflictWithFiles, BuildList(files)))
    {
    }

    private static string BuildList(string[] files)
    {
        StringBuilder builder = new StringBuilder();
        foreach (string f in files)
        {
            builder.Append("\n");
            builder.Append(f);
        }
        return builder.ToString();
    }
}
}
