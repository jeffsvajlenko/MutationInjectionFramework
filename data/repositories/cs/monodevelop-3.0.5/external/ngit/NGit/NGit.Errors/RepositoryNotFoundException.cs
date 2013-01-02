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
using NGit.Errors;
using NGit.Internal;
using Sharpen;

namespace NGit.Errors
{
/// <summary>Indicates a local repository does not exist.</summary>
/// <remarks>Indicates a local repository does not exist.</remarks>
[System.Serializable]
public class RepositoryNotFoundException : TransportException
{
    private const long serialVersionUID = 1L;

    /// <summary>Constructs an exception indicating a local repository does not exist.</summary>
    /// <remarks>Constructs an exception indicating a local repository does not exist.</remarks>
    /// <param name="location">description of the repository not found, usually file path.
    /// 	</param>
    public RepositoryNotFoundException(FilePath location) : this(location.GetPath())
    {
    }

    /// <summary>Constructs an exception indicating a local repository does not exist.</summary>
    /// <remarks>Constructs an exception indicating a local repository does not exist.</remarks>
    /// <param name="location">description of the repository not found, usually file path.
    /// 	</param>
    /// <param name="why">why the repository does not exist.</param>
    public RepositoryNotFoundException(FilePath location, Exception why) : this(location
            .GetPath(), why)
    {
    }

    /// <summary>Constructs an exception indicating a local repository does not exist.</summary>
    /// <remarks>Constructs an exception indicating a local repository does not exist.</remarks>
    /// <param name="location">description of the repository not found, usually file path.
    /// 	</param>
    public RepositoryNotFoundException(string location) : base(Message(location))
    {
    }

    /// <summary>Constructs an exception indicating a local repository does not exist.</summary>
    /// <remarks>Constructs an exception indicating a local repository does not exist.</remarks>
    /// <param name="location">description of the repository not found, usually file path.
    /// 	</param>
    /// <param name="why">why the repository does not exist.</param>
    public RepositoryNotFoundException(string location, Exception why) : base(Message
            (location), why)
    {
    }

    private static string Message(string location)
    {
        return MessageFormat.Format(JGitText.Get().repositoryNotFound, location);
    }
}
}
