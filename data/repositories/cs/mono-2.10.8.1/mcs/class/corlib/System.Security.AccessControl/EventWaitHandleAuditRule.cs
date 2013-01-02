//
// System.Security.AccessControl.EventWaitHandleAuditRule implementation
//
// Authors:
//	Dick Porter  <dick@ximian.com>
//	Atsushi Enomoto  <atsushi@ximian.com>
//
// Copyright (C) 2006-2007 Novell, Inc (http://www.novell.com)
//
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//

using System.Security.Principal;

namespace System.Security.AccessControl
{
public sealed class EventWaitHandleAuditRule : AuditRule
{
    EventWaitHandleRights rights;

    public EventWaitHandleAuditRule (IdentityReference identity,
                                     EventWaitHandleRights eventRights,
                                     AuditFlags flags)
    : base (identity, 0, false, InheritanceFlags.None, PropagationFlags.None, flags)
    {
        if (eventRights < EventWaitHandleRights.Modify ||
                eventRights > EventWaitHandleRights.FullControl)
        {
            throw new ArgumentOutOfRangeException ("eventRights");
        }
        if (flags < AuditFlags.None ||
                flags > AuditFlags.Failure)
        {
            throw new ArgumentOutOfRangeException ("flags");
        }
        if (identity == null)
        {
            throw new ArgumentNullException ("identity");
        }
        if (eventRights == 0)
        {
            throw new ArgumentNullException ("eventRights");
        }
        if (flags == AuditFlags.None)
        {
            throw new ArgumentException ("flags");
        }
        if (!(identity is SecurityIdentifier))
        {
            throw new ArgumentException ("identity");
        }

        this.rights = eventRights;
    }

    public EventWaitHandleRights EventWaitHandleRights
    {
        get
        {
            return rights;
        }
    }
}
}

