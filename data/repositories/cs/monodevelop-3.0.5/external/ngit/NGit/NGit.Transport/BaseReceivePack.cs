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
using System.Collections.Generic;
using System.IO;
using System.Text;
using NGit;
using NGit.Errors;
using NGit.Internal;
using NGit.Revwalk;
using NGit.Storage.File;
using NGit.Transport;
using NGit.Util.IO;
using Sharpen;

namespace NGit.Transport
{
/// <summary>Base implementation of the side of a push connection that receives objects.
/// 	</summary>
/// <remarks>
/// Base implementation of the side of a push connection that receives objects.
/// <p>
/// Contains high-level operations for initializing and closing streams,
/// advertising refs, reading commands, and receiving and applying a pack.
/// Subclasses compose these operations into full service implementations.
/// </remarks>
public abstract class BaseReceivePack
{
    /// <summary>Data in the first line of a request, the line itself plus capabilities.</summary>
    /// <remarks>Data in the first line of a request, the line itself plus capabilities.</remarks>
    public class FirstLine
    {
        private readonly string line;

        private readonly ICollection<string> capabilities;

        /// <summary>Parse the first line of a receive-pack request.</summary>
        /// <remarks>Parse the first line of a receive-pack request.</remarks>
        /// <param name="line">line from the client.</param>
        public FirstLine(string line)
        {
            HashSet<string> caps = new HashSet<string>();
            int nul = line.IndexOf('\0');
            if (nul >= 0)
            {
                foreach (string c in Sharpen.Runtime.Substring(line, nul + 1).Split(" "))
                {
                    caps.AddItem(c);
                }
                this.line = Sharpen.Runtime.Substring(line, 0, nul);
            }
            else
            {
                this.line = line;
            }
            this.capabilities = Sharpen.Collections.UnmodifiableSet(caps);
        }

        /// <returns>non-capabilities part of the line.</returns>
        public virtual string GetLine()
        {
            return line;
        }

        /// <returns>capabilities parsed from the line.</returns>
        public virtual ICollection<string> GetCapabilities()
        {
            return capabilities;
        }
    }

    /// <summary>Database we write the stored objects into.</summary>
    /// <remarks>Database we write the stored objects into.</remarks>
    protected internal readonly Repository db;

    /// <summary>
    /// Revision traversal support over
    /// <see cref="db">db</see>
    /// .
    /// </summary>
    protected internal readonly RevWalk walk;

    /// <summary>
    /// Is the client connection a bi-directional socket or pipe?
    /// <p>
    /// If true, this class assumes it can perform multiple read and write cycles
    /// with the client over the input and output streams.
    /// </summary>
    /// <remarks>
    /// Is the client connection a bi-directional socket or pipe?
    /// <p>
    /// If true, this class assumes it can perform multiple read and write cycles
    /// with the client over the input and output streams. This matches the
    /// functionality available with a standard TCP/IP connection, or a local
    /// operating system or in-memory pipe.
    /// <p>
    /// If false, this class runs in a read everything then output results mode,
    /// making it suitable for single round-trip systems RPCs such as HTTP.
    /// </remarks>
    protected internal bool biDirectionalPipe = true;

    /// <summary>Expecting data after the pack footer</summary>
    protected internal bool expectDataAfterPackFooter;

    /// <summary>Should an incoming transfer validate objects?</summary>
    protected internal bool checkReceivedObjects;

    /// <summary>Should an incoming transfer permit create requests?</summary>
    protected internal bool allowCreates;

    /// <summary>Should an incoming transfer permit delete requests?</summary>
    protected internal bool allowDeletes;

    /// <summary>Should an incoming transfer permit non-fast-forward requests?</summary>
    protected internal bool allowNonFastForwards;

    private bool allowOfsDelta;

    /// <summary>Identity to record action as within the reflog.</summary>
    /// <remarks>Identity to record action as within the reflog.</remarks>
    private PersonIdent refLogIdent;

    /// <summary>Hook used while advertising the refs to the client.</summary>
    /// <remarks>Hook used while advertising the refs to the client.</remarks>
    private AdvertiseRefsHook advertiseRefsHook;

    /// <summary>Filter used while advertising the refs to the client.</summary>
    /// <remarks>Filter used while advertising the refs to the client.</remarks>
    private RefFilter refFilter;

    /// <summary>Timeout in seconds to wait for client interaction.</summary>
    /// <remarks>Timeout in seconds to wait for client interaction.</remarks>
    private int timeout;

    /// <summary>
    /// Timer to manage
    /// <see cref="timeout">timeout</see>
    /// .
    /// </summary>
    private InterruptTimer timer;

    private TimeoutInputStream timeoutIn;

    private OutputStream origOut;

    /// <summary>Raw input stream.</summary>
    /// <remarks>Raw input stream.</remarks>
    protected internal InputStream rawIn;

    /// <summary>Raw output stream.</summary>
    /// <remarks>Raw output stream.</remarks>
    protected internal OutputStream rawOut;

    /// <summary>Optional message output stream.</summary>
    /// <remarks>Optional message output stream.</remarks>
    protected internal OutputStream msgOut;

    /// <summary>
    /// Packet line input stream around
    /// <see cref="rawIn">rawIn</see>
    /// .
    /// </summary>
    protected internal PacketLineIn pckIn;

    /// <summary>
    /// Packet line output stream around
    /// <see cref="rawOut">rawOut</see>
    /// .
    /// </summary>
    protected internal PacketLineOut pckOut;

    private readonly BaseReceivePack.MessageOutputWrapper msgOutWrapper;

    private PackParser parser;

    /// <summary>The refs we advertised as existing at the start of the connection.</summary>
    /// <remarks>The refs we advertised as existing at the start of the connection.</remarks>
    protected internal IDictionary<string, Ref> refs;

    /// <summary>All SHA-1s shown to the client, which can be possible edges.</summary>
    /// <remarks>All SHA-1s shown to the client, which can be possible edges.</remarks>
    protected internal ICollection<ObjectId> advertisedHaves;

    /// <summary>Capabilities requested by the client.</summary>
    /// <remarks>Capabilities requested by the client.</remarks>
    protected internal ICollection<string> enabledCapabilities;

    private IList<ReceiveCommand> commands;

    private StringBuilder advertiseError;

    /// <summary>
    /// If
    /// <see cref="BasePackPushConnection.CAPABILITY_REPORT_STATUS">BasePackPushConnection.CAPABILITY_REPORT_STATUS
    /// 	</see>
    /// is enabled.
    /// </summary>
    protected internal bool reportStatus;

    /// <summary>
    /// If
    /// <see cref="BasePackPushConnection.CAPABILITY_SIDE_BAND_64K">BasePackPushConnection.CAPABILITY_SIDE_BAND_64K
    /// 	</see>
    /// is enabled.
    /// </summary>
    protected internal bool sideBand;

    /// <summary>Lock around the received pack file, while updating refs.</summary>
    /// <remarks>Lock around the received pack file, while updating refs.</remarks>
    private PackLock packLock;

    private bool checkReferencedIsReachable;

    /// <summary>Git object size limit</summary>
    private long maxObjectSizeLimit;

    /// <summary>Create a new pack receive for an open repository.</summary>
    /// <remarks>Create a new pack receive for an open repository.</remarks>
    /// <param name="into">the destination repository.</param>
    protected internal BaseReceivePack(Repository into)
    {
        msgOutWrapper = new BaseReceivePack.MessageOutputWrapper(this);
        // Original stream passed to init(), since rawOut may be wrapped in a
        // sideband.
        db = into;
        walk = new RevWalk(db);
        BaseReceivePack.ReceiveConfig cfg = db.GetConfig().Get(BaseReceivePack.ReceiveConfig
                                            .KEY);
        checkReceivedObjects = cfg.checkReceivedObjects;
        allowCreates = cfg.allowCreates;
        allowDeletes = cfg.allowDeletes;
        allowNonFastForwards = cfg.allowNonFastForwards;
        allowOfsDelta = cfg.allowOfsDelta;
        advertiseRefsHook = AdvertiseRefsHook.DEFAULT;
        refFilter = RefFilter.DEFAULT;
        advertisedHaves = new HashSet<ObjectId>();
    }

    /// <summary>Configuration for receive operations.</summary>
    /// <remarks>Configuration for receive operations.</remarks>
    protected internal class ReceiveConfig
    {
        private sealed class _SectionParser_264 : Config.SectionParser<BaseReceivePack.ReceiveConfig
            >
        {
            public _SectionParser_264()
            {
            }

            public BaseReceivePack.ReceiveConfig Parse(Config cfg)
            {
                return new BaseReceivePack.ReceiveConfig(cfg);
            }
        }

        internal static readonly Config.SectionParser<BaseReceivePack.ReceiveConfig> KEY =
            new _SectionParser_264();

        internal readonly bool checkReceivedObjects;

        internal readonly bool allowCreates;

        internal readonly bool allowDeletes;

        internal readonly bool allowNonFastForwards;

        internal readonly bool allowOfsDelta;

        internal ReceiveConfig(Config config)
        {
            checkReceivedObjects = config.GetBoolean("receive", "fsckobjects", false);
            allowCreates = true;
            allowDeletes = !config.GetBoolean("receive", "denydeletes", false);
            allowNonFastForwards = !config.GetBoolean("receive", "denynonfastforwards", false
                                                     );
            allowOfsDelta = config.GetBoolean("repack", "usedeltabaseoffset", true);
        }
    }

    /// <summary>
    /// Output stream that wraps the current
    /// <see cref="BaseReceivePack.msgOut">BaseReceivePack.msgOut</see>
    /// .
    /// <p>
    /// We don't want to expose
    /// <see cref="BaseReceivePack.msgOut">BaseReceivePack.msgOut</see>
    /// directly because it can change
    /// several times over the course of a session.
    /// </summary>
    internal class MessageOutputWrapper : OutputStream
    {
        public override void Write(int ch)
        {
            if (this._enclosing.msgOut != null)
            {
                try
                {
                    this._enclosing.msgOut.Write(ch);
                }
                catch (IOException)
                {
                }
            }
        }

        // Ignore write failures.
        public override void Write(byte[] b, int off, int len)
        {
            if (this._enclosing.msgOut != null)
            {
                try
                {
                    this._enclosing.msgOut.Write(b, off, len);
                }
                catch (IOException)
                {
                }
            }
        }

        // Ignore write failures.
        public override void Write(byte[] b)
        {
            this.Write(b, 0, b.Length);
        }

        public override void Flush()
        {
            if (this._enclosing.msgOut != null)
            {
                try
                {
                    this._enclosing.msgOut.Flush();
                }
                catch (IOException)
                {
                }
            }
        }

        internal MessageOutputWrapper(BaseReceivePack _enclosing)
        {
            this._enclosing = _enclosing;
        }

        private readonly BaseReceivePack _enclosing;
        // Ignore write failures.
    }

    /// <returns>the process name used for pack lock messages.</returns>
    protected internal abstract string GetLockMessageProcessName();

    /// <returns>the repository this receive completes into.</returns>
    public Repository GetRepository()
    {
        return db;
    }

    /// <returns>the RevWalk instance used by this connection.</returns>
    public RevWalk GetRevWalk()
    {
        return walk;
    }

    /// <summary>Get refs which were advertised to the client.</summary>
    /// <remarks>Get refs which were advertised to the client.</remarks>
    /// <returns>
    /// all refs which were advertised to the client, or null if
    /// <see cref="SetAdvertisedRefs(System.Collections.Generic.IDictionary{K, V}, System.Collections.Generic.ICollection{E})
    /// 	">SetAdvertisedRefs(System.Collections.Generic.IDictionary&lt;K, V&gt;, System.Collections.Generic.ICollection&lt;E&gt;)
    /// 	</see>
    /// has not been called yet.
    /// </returns>
    public IDictionary<string, Ref> GetAdvertisedRefs()
    {
        return refs;
    }

    /// <summary>Set the refs advertised by this ReceivePack.</summary>
    /// <remarks>
    /// Set the refs advertised by this ReceivePack.
    /// <p>
    /// Intended to be called from a
    /// <see cref="PreReceiveHook">PreReceiveHook</see>
    /// .
    /// </remarks>
    /// <param name="allRefs">
    /// explicit set of references to claim as advertised by this
    /// ReceivePack instance. This overrides any references that
    /// may exist in the source repository. The map is passed
    /// to the configured
    /// <see cref="GetRefFilter()">GetRefFilter()</see>
    /// . If null, assumes
    /// all refs were advertised.
    /// </param>
    /// <param name="additionalHaves">
    /// explicit set of additional haves to claim as advertised. If
    /// null, assumes the default set of additional haves from the
    /// repository.
    /// </param>
    public virtual void SetAdvertisedRefs(IDictionary<string, Ref> allRefs, ICollection
                                          <ObjectId> additionalHaves)
    {
        refs = allRefs != null ? allRefs : db.GetAllRefs();
        refs = refFilter.Filter(refs);
        Ref head = refs.Get(Constants.HEAD);
        if (head != null && head.IsSymbolic())
        {
            Sharpen.Collections.Remove(refs, Constants.HEAD);
        }
        foreach (Ref @ref in refs.Values)
        {
            if (@ref.GetObjectId() != null)
            {
                advertisedHaves.AddItem(@ref.GetObjectId());
            }
        }
        if (additionalHaves != null)
        {
            Sharpen.Collections.AddAll(advertisedHaves, additionalHaves);
        }
        else
        {
            Sharpen.Collections.AddAll(advertisedHaves, db.GetAdditionalHaves());
        }
    }

    /// <summary>Get objects advertised to the client.</summary>
    /// <remarks>Get objects advertised to the client.</remarks>
    /// <returns>
    /// the set of objects advertised to the as present in this repository,
    /// or null if
    /// <see cref="SetAdvertisedRefs(System.Collections.Generic.IDictionary{K, V}, System.Collections.Generic.ICollection{E})
    /// 	">SetAdvertisedRefs(System.Collections.Generic.IDictionary&lt;K, V&gt;, System.Collections.Generic.ICollection&lt;E&gt;)
    /// 	</see>
    /// has not been called
    /// yet.
    /// </returns>
    public ICollection<ObjectId> GetAdvertisedObjects()
    {
        return advertisedHaves;
    }

    /// <returns>
    /// true if this instance will validate all referenced, but not
    /// supplied by the client, objects are reachable from another
    /// reference.
    /// </returns>
    public virtual bool IsCheckReferencedObjectsAreReachable()
    {
        return checkReferencedIsReachable;
    }

    /// <summary>Validate all referenced but not supplied objects are reachable.</summary>
    /// <remarks>
    /// Validate all referenced but not supplied objects are reachable.
    /// <p>
    /// If enabled, this instance will verify that references to objects not
    /// contained within the received pack are already reachable through at least
    /// one other reference displayed as part of
    /// <see cref="GetAdvertisedRefs()">GetAdvertisedRefs()</see>
    /// .
    /// <p>
    /// This feature is useful when the application doesn't trust the client to
    /// not provide a forged SHA-1 reference to an object, in an attempt to
    /// access parts of the DAG that they aren't allowed to see and which have
    /// been hidden from them via the configured
    /// <see cref="AdvertiseRefsHook">AdvertiseRefsHook</see>
    /// or
    /// <see cref="RefFilter">RefFilter</see>
    /// .
    /// <p>
    /// Enabling this feature may imply at least some, if not all, of the same
    /// functionality performed by
    /// <see cref="SetCheckReceivedObjects(bool)">SetCheckReceivedObjects(bool)</see>
    /// .
    /// Applications are encouraged to enable both features, if desired.
    /// </remarks>
    /// <param name="b">
    /// <code>true</code>
    /// to enable the additional check.
    /// </param>
    public virtual void SetCheckReferencedObjectsAreReachable(bool b)
    {
        this.checkReferencedIsReachable = b;
    }

    /// <returns>
    /// true if this class expects a bi-directional pipe opened between
    /// the client and itself. The default is true.
    /// </returns>
    public virtual bool IsBiDirectionalPipe()
    {
        return biDirectionalPipe;
    }

    /// <param name="twoWay">
    /// if true, this class will assume the socket is a fully
    /// bidirectional pipe between the two peers and takes advantage
    /// of that by first transmitting the known refs, then waiting to
    /// read commands. If false, this class assumes it must read the
    /// commands before writing output and does not perform the
    /// initial advertising.
    /// </param>
    public virtual void SetBiDirectionalPipe(bool twoWay)
    {
        biDirectionalPipe = twoWay;
    }

    /// <returns>true if there is data expected after the pack footer.</returns>
    public virtual bool IsExpectDataAfterPackFooter()
    {
        return expectDataAfterPackFooter;
    }

    /// <param name="e">true if there is additional data in InputStream after pack.</param>
    public virtual void SetExpectDataAfterPackFooter(bool e)
    {
        expectDataAfterPackFooter = e;
    }

    /// <returns>
    /// true if this instance will verify received objects are formatted
    /// correctly. Validating objects requires more CPU time on this side
    /// of the connection.
    /// </returns>
    public virtual bool IsCheckReceivedObjects()
    {
        return checkReceivedObjects;
    }

    /// <param name="check">
    /// true to enable checking received objects; false to assume all
    /// received objects are valid.
    /// </param>
    public virtual void SetCheckReceivedObjects(bool check)
    {
        checkReceivedObjects = check;
    }

    /// <returns>true if the client can request refs to be created.</returns>
    public virtual bool IsAllowCreates()
    {
        return allowCreates;
    }

    /// <param name="canCreate">true to permit create ref commands to be processed.</param>
    public virtual void SetAllowCreates(bool canCreate)
    {
        allowCreates = canCreate;
    }

    /// <returns>true if the client can request refs to be deleted.</returns>
    public virtual bool IsAllowDeletes()
    {
        return allowDeletes;
    }

    /// <param name="canDelete">true to permit delete ref commands to be processed.</param>
    public virtual void SetAllowDeletes(bool canDelete)
    {
        allowDeletes = canDelete;
    }

    /// <returns>
    /// true if the client can request non-fast-forward updates of a ref,
    /// possibly making objects unreachable.
    /// </returns>
    public virtual bool IsAllowNonFastForwards()
    {
        return allowNonFastForwards;
    }

    /// <param name="canRewind">
    /// true to permit the client to ask for non-fast-forward updates
    /// of an existing ref.
    /// </param>
    public virtual void SetAllowNonFastForwards(bool canRewind)
    {
        allowNonFastForwards = canRewind;
    }

    /// <returns>identity of the user making the changes in the reflog.</returns>
    public virtual PersonIdent GetRefLogIdent()
    {
        return refLogIdent;
    }

    /// <summary>Set the identity of the user appearing in the affected reflogs.</summary>
    /// <remarks>
    /// Set the identity of the user appearing in the affected reflogs.
    /// <p>
    /// The timestamp portion of the identity is ignored. A new identity with the
    /// current timestamp will be created automatically when the updates occur
    /// and the log records are written.
    /// </remarks>
    /// <param name="pi">
    /// identity of the user. If null the identity will be
    /// automatically determined based on the repository
    /// configuration.
    /// </param>
    public virtual void SetRefLogIdent(PersonIdent pi)
    {
        refLogIdent = pi;
    }

    /// <returns>the hook used while advertising the refs to the client</returns>
    public virtual AdvertiseRefsHook GetAdvertiseRefsHook()
    {
        return advertiseRefsHook;
    }

    /// <returns>the filter used while advertising the refs to the client</returns>
    public virtual RefFilter GetRefFilter()
    {
        return refFilter;
    }

    /// <summary>Set the hook used while advertising the refs to the client.</summary>
    /// <remarks>
    /// Set the hook used while advertising the refs to the client.
    /// <p>
    /// If the
    /// <see cref="AdvertiseRefsHook">AdvertiseRefsHook</see>
    /// chooses to call
    /// <see cref="SetAdvertisedRefs(System.Collections.Generic.IDictionary{K, V}, System.Collections.Generic.ICollection{E})
    /// 	">SetAdvertisedRefs(System.Collections.Generic.IDictionary&lt;K, V&gt;, System.Collections.Generic.ICollection&lt;E&gt;)
    /// 	</see>
    /// , only refs set by this hook
    /// <em>and</em> selected by the
    /// <see cref="RefFilter">RefFilter</see>
    /// will be shown to the client.
    /// Clients may still attempt to create or update a reference not advertised by
    /// the configured
    /// <see cref="AdvertiseRefsHook">AdvertiseRefsHook</see>
    /// . These attempts should be rejected
    /// by a matching
    /// <see cref="PreReceiveHook">PreReceiveHook</see>
    /// .
    /// </remarks>
    /// <param name="advertiseRefsHook">the hook; may be null to show all refs.</param>
    public virtual void SetAdvertiseRefsHook(AdvertiseRefsHook advertiseRefsHook)
    {
        if (advertiseRefsHook != null)
        {
            this.advertiseRefsHook = advertiseRefsHook;
        }
        else
        {
            this.advertiseRefsHook = AdvertiseRefsHook.DEFAULT;
        }
    }

    /// <summary>Set the filter used while advertising the refs to the client.</summary>
    /// <remarks>
    /// Set the filter used while advertising the refs to the client.
    /// <p>
    /// Only refs allowed by this filter will be shown to the client.
    /// The filter is run against the refs specified by the
    /// <see cref="AdvertiseRefsHook">AdvertiseRefsHook</see>
    /// (if applicable).
    /// </remarks>
    /// <param name="refFilter">the filter; may be null to show all refs.</param>
    public virtual void SetRefFilter(RefFilter refFilter)
    {
        this.refFilter = refFilter != null ? refFilter : RefFilter.DEFAULT;
    }

    /// <returns>timeout (in seconds) before aborting an IO operation.</returns>
    public virtual int GetTimeout()
    {
        return timeout;
    }

    /// <summary>Set the timeout before willing to abort an IO call.</summary>
    /// <remarks>Set the timeout before willing to abort an IO call.</remarks>
    /// <param name="seconds">
    /// number of seconds to wait (with no data transfer occurring)
    /// before aborting an IO read or write operation with the
    /// connected client.
    /// </param>
    public virtual void SetTimeout(int seconds)
    {
        timeout = seconds;
    }

    /// <summary>Set the maximum allowed Git object size.</summary>
    /// <remarks>
    /// Set the maximum allowed Git object size.
    /// <p>
    /// If an object is larger than the given size the pack-parsing will throw an
    /// exception aborting the receive-pack operation.
    /// </remarks>
    /// <param name="limit">the Git object size limit. If zero then there is not limit.</param>
    public virtual void SetMaxObjectSizeLimit(long limit)
    {
        maxObjectSizeLimit = limit;
    }

    /// <summary>Check whether the client expects a side-band stream.</summary>
    /// <remarks>Check whether the client expects a side-band stream.</remarks>
    /// <returns>
    /// true if the client has advertised a side-band capability, false
    /// otherwise.
    /// </returns>
    /// <exception cref="RequestNotYetReadException">
    /// if the client's request has not yet been read from the wire, so
    /// we do not know if they expect side-band. Note that the client
    /// may have already written the request, it just has not been
    /// read.
    /// </exception>
    /// <exception cref="NGit.Transport.RequestNotYetReadException"></exception>
    public virtual bool IsSideBand()
    {
        if (enabledCapabilities == null)
        {
            throw new RequestNotYetReadException();
        }
        return enabledCapabilities.Contains(BasePackPushConnection.CAPABILITY_SIDE_BAND_64K
                                           );
    }

    /// <returns>all of the command received by the current request.</returns>
    public virtual IList<ReceiveCommand> GetAllCommands()
    {
        return Sharpen.Collections.UnmodifiableList(commands);
    }

    /// <summary>Send an error message to the client.</summary>
    /// <remarks>
    /// Send an error message to the client.
    /// <p>
    /// If any error messages are sent before the references are advertised to
    /// the client, the errors will be sent instead of the advertisement and the
    /// receive operation will be aborted. All clients should receive and display
    /// such early stage errors.
    /// <p>
    /// If the reference advertisements have already been sent, messages are sent
    /// in a side channel. If the client doesn't support receiving messages, the
    /// message will be discarded, with no other indication to the caller or to
    /// the client.
    /// <p>
    /// <see cref="PreReceiveHook">PreReceiveHook</see>
    /// s should always try to use
    /// <see cref="ReceiveCommand.SetResult(Result, string)">ReceiveCommand.SetResult(Result, string)
    /// 	</see>
    /// with a result status of
    /// <see cref="Result.REJECTED_OTHER_REASON">Result.REJECTED_OTHER_REASON</see>
    /// to indicate any reasons for
    /// rejecting an update. Messages attached to a command are much more likely
    /// to be returned to the client.
    /// </remarks>
    /// <param name="what">
    /// string describing the problem identified by the hook. The
    /// string must not end with an LF, and must not contain an LF.
    /// </param>
    public virtual void SendError(string what)
    {
        if (refs == null)
        {
            if (advertiseError == null)
            {
                advertiseError = new StringBuilder();
            }
            advertiseError.Append(what).Append('\n');
        }
        else
        {
            msgOutWrapper.Write(Constants.Encode("error: " + what + "\n"));
        }
    }

    /// <summary>Send a message to the client, if it supports receiving them.</summary>
    /// <remarks>
    /// Send a message to the client, if it supports receiving them.
    /// <p>
    /// If the client doesn't support receiving messages, the message will be
    /// discarded, with no other indication to the caller or to the client.
    /// </remarks>
    /// <param name="what">
    /// string describing the problem identified by the hook. The
    /// string must not end with an LF, and must not contain an LF.
    /// </param>
    public virtual void SendMessage(string what)
    {
        msgOutWrapper.Write(Constants.Encode(what + "\n"));
    }

    /// <returns>an underlying stream for sending messages to the client.</returns>
    public virtual OutputStream GetMessageOutputStream()
    {
        return msgOutWrapper;
    }

    /// <returns>true if any commands to be executed have been read.</returns>
    protected internal virtual bool HasCommands()
    {
        return !commands.IsEmpty();
    }

    /// <returns>true if an error occurred that should be advertised.</returns>
    protected internal virtual bool HasError()
    {
        return advertiseError != null;
    }

    /// <summary>Initialize the instance with the given streams.</summary>
    /// <remarks>Initialize the instance with the given streams.</remarks>
    /// <param name="input">
    /// raw input to read client commands and pack data from. Caller
    /// must ensure the input is buffered, otherwise read performance
    /// may suffer.
    /// </param>
    /// <param name="output">
    /// response back to the Git network client. Caller must ensure
    /// the output is buffered, otherwise write performance may
    /// suffer.
    /// </param>
    /// <param name="messages">
    /// secondary "notice" channel to send additional messages out
    /// through. When run over SSH this should be tied back to the
    /// standard error channel of the command execution. For most
    /// other network connections this should be null.
    /// </param>
    protected internal virtual void Init(InputStream input, OutputStream output, OutputStream
                                         messages)
    {
        origOut = output;
        rawIn = input;
        rawOut = output;
        msgOut = messages;
        if (timeout > 0)
        {
            Sharpen.Thread caller = Sharpen.Thread.CurrentThread();
            timer = new InterruptTimer(caller.GetName() + "-Timer");
            timeoutIn = new TimeoutInputStream(rawIn, timer);
            TimeoutOutputStream o = new TimeoutOutputStream(rawOut, timer);
            timeoutIn.SetTimeout(timeout * 1000);
            o.SetTimeout(timeout * 1000);
            rawIn = timeoutIn;
            rawOut = o;
        }
        pckIn = new PacketLineIn(rawIn);
        pckOut = new PacketLineOut(rawOut);
        pckOut.SetFlushOnEnd(false);
        enabledCapabilities = new HashSet<string>();
        commands = new AList<ReceiveCommand>();
    }

    /// <returns>advertised refs, or the default if not explicitly advertised.</returns>
    protected internal virtual IDictionary<string, Ref> GetAdvertisedOrDefaultRefs()
    {
        if (refs == null)
        {
            SetAdvertisedRefs(null, null);
        }
        return refs;
    }

    /// <summary>Receive a pack from the stream and check connectivity if necessary.</summary>
    /// <remarks>Receive a pack from the stream and check connectivity if necessary.</remarks>
    /// <exception cref="System.IO.IOException">an error occurred during unpacking or connectivity checking.
    /// 	</exception>
    protected internal virtual void ReceivePackAndCheckConnectivity()
    {
        ReceivePack();
        if (NeedCheckConnectivity())
        {
            CheckConnectivity();
        }
        parser = null;
    }

    /// <summary>Unlock the pack written by this object.</summary>
    /// <remarks>Unlock the pack written by this object.</remarks>
    /// <exception cref="System.IO.IOException">the pack could not be unlocked.</exception>
    protected internal virtual void UnlockPack()
    {
        if (packLock != null)
        {
            packLock.Unlock();
            packLock = null;
        }
    }

    /// <summary>Generate an advertisement of available refs and capabilities.</summary>
    /// <remarks>Generate an advertisement of available refs and capabilities.</remarks>
    /// <param name="adv">the advertisement formatter.</param>
    /// <exception cref="System.IO.IOException">the formatter failed to write an advertisement.
    /// 	</exception>
    /// <exception cref="ServiceMayNotContinueException">the hook denied advertisement.</exception>
    /// <exception cref="NGit.Transport.ServiceMayNotContinueException"></exception>
    public virtual void SendAdvertisedRefs(RefAdvertiser adv)
    {
        if (advertiseError != null)
        {
            adv.WriteOne("ERR " + advertiseError);
            return;
        }
        try
        {
            advertiseRefsHook.AdvertiseRefs(this);
        }
        catch (ServiceMayNotContinueException fail)
        {
            if (fail.Message != null)
            {
                adv.WriteOne("ERR " + fail.Message);
                fail.SetOutput();
            }
            throw;
        }
        adv.Init(db);
        adv.AdvertiseCapability(BasePackPushConnection.CAPABILITY_SIDE_BAND_64K);
        adv.AdvertiseCapability(BasePackPushConnection.CAPABILITY_DELETE_REFS);
        adv.AdvertiseCapability(BasePackPushConnection.CAPABILITY_REPORT_STATUS);
        if (allowOfsDelta)
        {
            adv.AdvertiseCapability(BasePackPushConnection.CAPABILITY_OFS_DELTA);
        }
        adv.Send(GetAdvertisedOrDefaultRefs());
        foreach (ObjectId obj in advertisedHaves)
        {
            adv.AdvertiseHave(obj);
        }
        if (adv.IsEmpty())
        {
            adv.AdvertiseId(ObjectId.ZeroId, "capabilities^{}");
        }
        adv.End();
    }

    /// <summary>Receive a list of commands from the input.</summary>
    /// <remarks>Receive a list of commands from the input.</remarks>
    /// <exception cref="System.IO.IOException">System.IO.IOException</exception>
    protected internal virtual void RecvCommands()
    {
        for (; ; )
        {
            string line;
            try
            {
                line = pckIn.ReadStringRaw();
            }
            catch (EOFException eof)
            {
                if (commands.IsEmpty())
                {
                    return;
                }
                throw;
            }
            if (line == PacketLineIn.END)
            {
                break;
            }
            if (commands.IsEmpty())
            {
                BaseReceivePack.FirstLine firstLine = new BaseReceivePack.FirstLine(line);
                enabledCapabilities = firstLine.GetCapabilities();
                line = firstLine.GetLine();
            }
            if (line.Length < 83)
            {
                string m = JGitText.Get().errorInvalidProtocolWantedOldNewRef;
                SendError(m);
                throw new PackProtocolException(m);
            }
            ObjectId oldId = ObjectId.FromString(Sharpen.Runtime.Substring(line, 0, 40));
            ObjectId newId = ObjectId.FromString(Sharpen.Runtime.Substring(line, 41, 81));
            string name = Sharpen.Runtime.Substring(line, 82);
            ReceiveCommand cmd = new ReceiveCommand(oldId, newId, name);
            if (name.Equals(Constants.HEAD))
            {
                cmd.SetResult(ReceiveCommand.Result.REJECTED_CURRENT_BRANCH);
            }
            else
            {
                cmd.SetRef(refs.Get(cmd.GetRefName()));
            }
            commands.AddItem(cmd);
        }
    }

    /// <summary>Enable capabilities based on a previously read capabilities line.</summary>
    /// <remarks>Enable capabilities based on a previously read capabilities line.</remarks>
    protected internal virtual void EnableCapabilities()
    {
        reportStatus = enabledCapabilities.Contains(BasePackPushConnection.CAPABILITY_REPORT_STATUS
                                                   );
        sideBand = enabledCapabilities.Contains(BasePackPushConnection.CAPABILITY_SIDE_BAND_64K
                                               );
        if (sideBand)
        {
            OutputStream @out = rawOut;
            rawOut = new SideBandOutputStream(SideBandOutputStream.CH_DATA, SideBandOutputStream
                                              .MAX_BUF, @out);
            msgOut = new SideBandOutputStream(SideBandOutputStream.CH_PROGRESS, SideBandOutputStream
                                              .MAX_BUF, @out);
            pckOut = new PacketLineOut(rawOut);
            pckOut.SetFlushOnEnd(false);
        }
    }

    /// <returns>true if a pack is expected based on the list of commands.</returns>
    protected internal virtual bool NeedPack()
    {
        foreach (ReceiveCommand cmd in commands)
        {
            if (cmd.GetType() != ReceiveCommand.Type.DELETE)
            {
                return true;
            }
        }
        return false;
    }

    /// <summary>Receive a pack from the input and store it in the repository.</summary>
    /// <remarks>Receive a pack from the input and store it in the repository.</remarks>
    /// <exception cref="System.IO.IOException">an error occurred reading or indexing the pack.
    /// 	</exception>
    private void ReceivePack()
    {
        // It might take the client a while to pack the objects it needs
        // to send to us.  We should increase our timeout so we don't
        // abort while the client is computing.
        //
        if (timeoutIn != null)
        {
            timeoutIn.SetTimeout(10 * timeout * 1000);
        }
        ProgressMonitor receiving = NullProgressMonitor.INSTANCE;
        ProgressMonitor resolving = NullProgressMonitor.INSTANCE;
        if (sideBand)
        {
            resolving = new SideBandProgressMonitor(msgOut);
        }
        ObjectInserter ins = db.NewObjectInserter();
        try
        {
            string lockMsg = "jgit receive-pack";
            if (GetRefLogIdent() != null)
            {
                lockMsg += " from " + GetRefLogIdent().ToExternalString();
            }
            parser = ins.NewPackParser(rawIn);
            parser.SetAllowThin(true);
            parser.SetNeedNewObjectIds(checkReferencedIsReachable);
            parser.SetNeedBaseObjectIds(checkReferencedIsReachable);
            parser.SetCheckEofAfterPackFooter(!biDirectionalPipe && !IsExpectDataAfterPackFooter
                                              ());
            parser.SetExpectDataAfterPackFooter(IsExpectDataAfterPackFooter());
            parser.SetObjectChecking(IsCheckReceivedObjects());
            parser.SetLockMessage(lockMsg);
            parser.SetMaxObjectSizeLimit(maxObjectSizeLimit);
            packLock = parser.Parse(receiving, resolving);
            ins.Flush();
        }
        finally
        {
            ins.Release();
        }
        if (timeoutIn != null)
        {
            timeoutIn.SetTimeout(timeout * 1000);
        }
    }

    private bool NeedCheckConnectivity()
    {
        return IsCheckReceivedObjects() || IsCheckReferencedObjectsAreReachable();
    }

    /// <exception cref="System.IO.IOException"></exception>
    private void CheckConnectivity()
    {
        ObjectIdSubclassMap<ObjectId> baseObjects = null;
        ObjectIdSubclassMap<ObjectId> providedObjects = null;
        if (checkReferencedIsReachable)
        {
            baseObjects = parser.GetBaseObjectIds();
            providedObjects = parser.GetNewObjectIds();
        }
        parser = null;
        ObjectWalk ow = new ObjectWalk(db);
        ow.SetRetainBody(false);
        if (checkReferencedIsReachable)
        {
            ow.Sort(RevSort.TOPO);
            if (!baseObjects.IsEmpty())
            {
                ow.Sort(RevSort.BOUNDARY, true);
            }
        }
        foreach (ReceiveCommand cmd in commands)
        {
            if (cmd.GetResult() != ReceiveCommand.Result.NOT_ATTEMPTED)
            {
                continue;
            }
            if (cmd.GetType() == ReceiveCommand.Type.DELETE)
            {
                continue;
            }
            ow.MarkStart(ow.ParseAny(cmd.GetNewId()));
        }
        foreach (ObjectId have in advertisedHaves)
        {
            RevObject o = ow.ParseAny(have);
            ow.MarkUninteresting(o);
            if (checkReferencedIsReachable && !baseObjects.IsEmpty())
            {
                o = ow.Peel(o);
                if (o is RevCommit)
                {
                    o = ((RevCommit)o).Tree;
                }
                if (o is RevTree)
                {
                    ow.MarkUninteresting(o);
                }
            }
        }
        RevCommit c;
        while ((c = ow.Next()) != null)
        {
            if (checkReferencedIsReachable && !c.Has(RevFlag.UNINTERESTING) && !providedObjects
                    .Contains(c))
            {
                //
                //
                throw new MissingObjectException(c, Constants.TYPE_COMMIT);
            }
        }
        RevObject o_1;
        while ((o_1 = ow.NextObject()) != null)
        {
            if (o_1.Has(RevFlag.UNINTERESTING))
            {
                continue;
            }
            if (checkReferencedIsReachable)
            {
                if (providedObjects.Contains(o_1))
                {
                    continue;
                }
                else
                {
                    throw new MissingObjectException(o_1, o_1.Type);
                }
            }
            if (o_1 is RevBlob && !db.HasObject(o_1))
            {
                throw new MissingObjectException(o_1, Constants.TYPE_BLOB);
            }
        }
        if (checkReferencedIsReachable)
        {
            foreach (ObjectId id in baseObjects)
            {
                o_1 = ow.ParseAny(id);
                if (!o_1.Has(RevFlag.UNINTERESTING))
                {
                    throw new MissingObjectException(o_1, o_1.Type);
                }
            }
        }
    }

    /// <summary>Validate the command list.</summary>
    /// <remarks>Validate the command list.</remarks>
    protected internal virtual void ValidateCommands()
    {
        foreach (ReceiveCommand cmd in commands)
        {
            Ref @ref = cmd.GetRef();
            if (cmd.GetResult() != ReceiveCommand.Result.NOT_ATTEMPTED)
            {
                continue;
            }
            if (cmd.GetType() == ReceiveCommand.Type.DELETE && !IsAllowDeletes())
            {
                // Deletes are not supported on this repository.
                //
                cmd.SetResult(ReceiveCommand.Result.REJECTED_NODELETE);
                continue;
            }
            if (cmd.GetType() == ReceiveCommand.Type.CREATE)
            {
                if (!IsAllowCreates())
                {
                    cmd.SetResult(ReceiveCommand.Result.REJECTED_NOCREATE);
                    continue;
                }
                if (@ref != null && !IsAllowNonFastForwards())
                {
                    // Creation over an existing ref is certainly not going
                    // to be a fast-forward update. We can reject it early.
                    //
                    cmd.SetResult(ReceiveCommand.Result.REJECTED_NONFASTFORWARD);
                    continue;
                }
                if (@ref != null)
                {
                    // A well behaved client shouldn't have sent us a
                    // create command for a ref we advertised to it.
                    //
                    cmd.SetResult(ReceiveCommand.Result.REJECTED_OTHER_REASON, MessageFormat.Format(JGitText
                                  .Get().refAlreadyExists, @ref));
                    continue;
                }
            }
            if (cmd.GetType() == ReceiveCommand.Type.DELETE && @ref != null && !ObjectId.ZeroId
                    .Equals(cmd.GetOldId()) && !@ref.GetObjectId().Equals(cmd.GetOldId()))
            {
                // Delete commands can be sent with the old id matching our
                // advertised value, *OR* with the old id being 0{40}. Any
                // other requested old id is invalid.
                //
                cmd.SetResult(ReceiveCommand.Result.REJECTED_OTHER_REASON, JGitText.Get().invalidOldIdSent
                             );
                continue;
            }
            if (cmd.GetType() == ReceiveCommand.Type.UPDATE)
            {
                if (@ref == null)
                {
                    // The ref must have been advertised in order to be updated.
                    //
                    cmd.SetResult(ReceiveCommand.Result.REJECTED_OTHER_REASON, JGitText.Get().noSuchRef
                                 );
                    continue;
                }
                if (!@ref.GetObjectId().Equals(cmd.GetOldId()))
                {
                    // A properly functioning client will send the same
                    // object id we advertised.
                    //
                    cmd.SetResult(ReceiveCommand.Result.REJECTED_OTHER_REASON, JGitText.Get().invalidOldIdSent
                                 );
                    continue;
                }
                // Is this possibly a non-fast-forward style update?
                //
                RevObject oldObj;
                RevObject newObj;
                try
                {
                    oldObj = walk.ParseAny(cmd.GetOldId());
                }
                catch (IOException)
                {
                    cmd.SetResult(ReceiveCommand.Result.REJECTED_MISSING_OBJECT, cmd.GetOldId().Name);
                    continue;
                }
                try
                {
                    newObj = walk.ParseAny(cmd.GetNewId());
                }
                catch (IOException)
                {
                    cmd.SetResult(ReceiveCommand.Result.REJECTED_MISSING_OBJECT, cmd.GetNewId().Name);
                    continue;
                }
                if (oldObj is RevCommit && newObj is RevCommit)
                {
                    try
                    {
                        if (walk.IsMergedInto((RevCommit)oldObj, (RevCommit)newObj))
                        {
                            cmd.SetTypeFastForwardUpdate();
                        }
                        else
                        {
                            cmd.SetType(ReceiveCommand.Type.UPDATE_NONFASTFORWARD);
                        }
                    }
                    catch (MissingObjectException e)
                    {
                        cmd.SetResult(ReceiveCommand.Result.REJECTED_MISSING_OBJECT, e.Message);
                    }
                    catch (IOException)
                    {
                        cmd.SetResult(ReceiveCommand.Result.REJECTED_OTHER_REASON);
                    }
                }
                else
                {
                    cmd.SetType(ReceiveCommand.Type.UPDATE_NONFASTFORWARD);
                }
                if (cmd.GetType() == ReceiveCommand.Type.UPDATE_NONFASTFORWARD && !IsAllowNonFastForwards
                        ())
                {
                    cmd.SetResult(ReceiveCommand.Result.REJECTED_NONFASTFORWARD);
                    continue;
                }
            }
            if (!cmd.GetRefName().StartsWith(Constants.R_REFS) || !Repository.IsValidRefName(
                        cmd.GetRefName()))
            {
                cmd.SetResult(ReceiveCommand.Result.REJECTED_OTHER_REASON, JGitText.Get().funnyRefname
                             );
            }
        }
    }

    /// <summary>Filter the list of commands according to result.</summary>
    /// <remarks>Filter the list of commands according to result.</remarks>
    /// <param name="want">desired status to filter by.</param>
    /// <returns>
    /// a copy of the command list containing only those commands with the
    /// desired status.
    /// </returns>
    protected internal virtual IList<ReceiveCommand> FilterCommands(ReceiveCommand.Result
            want)
    {
        return ReceiveCommand.Filter(commands, want);
    }

    /// <summary>Execute commands to update references.</summary>
    /// <remarks>Execute commands to update references.</remarks>
    protected internal virtual void ExecuteCommands()
    {
        IList<ReceiveCommand> toApply = FilterCommands(ReceiveCommand.Result.NOT_ATTEMPTED
                                                      );
        if (toApply.IsEmpty())
        {
            return;
        }
        ProgressMonitor updating = NullProgressMonitor.INSTANCE;
        if (sideBand)
        {
            SideBandProgressMonitor pm = new SideBandProgressMonitor(msgOut);
            pm.SetDelayStart(250, TimeUnit.MILLISECONDS);
            updating = pm;
        }
        BatchRefUpdate batch = db.RefDatabase.NewBatchUpdate();
        batch.SetAllowNonFastForwards(IsAllowNonFastForwards());
        batch.SetRefLogIdent(GetRefLogIdent());
        batch.SetRefLogMessage("push", true);
        batch.AddCommand(toApply);
        try
        {
            batch.Execute(walk, updating);
        }
        catch (IOException err)
        {
            foreach (ReceiveCommand cmd in toApply)
            {
                if (cmd.GetResult() == ReceiveCommand.Result.NOT_ATTEMPTED)
                {
                    cmd.Reject(err);
                }
            }
        }
    }

    /// <summary>Send a status report.</summary>
    /// <remarks>Send a status report.</remarks>
    /// <param name="forClient">
    /// true if this report is for a Git client, false if it is for an
    /// end-user.
    /// </param>
    /// <param name="unpackError">
    /// an error that occurred during unpacking, or
    /// <code>null</code>
    /// </param>
    /// <param name="out">the reporter for sending the status strings.</param>
    /// <exception cref="System.IO.IOException">an error occurred writing the status report.
    /// 	</exception>
    internal virtual void SendStatusReport(bool forClient, Exception unpackError
                                           , BaseReceivePack.Reporter @out)
    {
        if (unpackError != null)
        {
            @out.SendString("unpack error " + unpackError.Message);
            if (forClient)
            {
                foreach (ReceiveCommand cmd in commands)
                {
                    @out.SendString("ng " + cmd.GetRefName() + " n/a (unpacker error)");
                }
            }
            return;
        }
        if (forClient)
        {
            @out.SendString("unpack ok");
        }
        foreach (ReceiveCommand cmd_1 in commands)
        {
            if (cmd_1.GetResult() == ReceiveCommand.Result.OK)
            {
                if (forClient)
                {
                    @out.SendString("ok " + cmd_1.GetRefName());
                }
                continue;
            }
            StringBuilder r = new StringBuilder();
            r.Append("ng ");
            r.Append(cmd_1.GetRefName());
            r.Append(" ");
            switch (cmd_1.GetResult())
            {
            case ReceiveCommand.Result.NOT_ATTEMPTED:
            {
                r.Append("server bug; ref not processed");
                break;
            }

            case ReceiveCommand.Result.REJECTED_NOCREATE:
            {
                r.Append("creation prohibited");
                break;
            }

            case ReceiveCommand.Result.REJECTED_NODELETE:
            {
                r.Append("deletion prohibited");
                break;
            }

            case ReceiveCommand.Result.REJECTED_NONFASTFORWARD:
            {
                r.Append("non-fast forward");
                break;
            }

            case ReceiveCommand.Result.REJECTED_CURRENT_BRANCH:
            {
                r.Append("branch is currently checked out");
                break;
            }

            case ReceiveCommand.Result.REJECTED_MISSING_OBJECT:
            {
                if (cmd_1.GetMessage() == null)
                {
                    r.Append("missing object(s)");
                }
                else
                {
                    if (cmd_1.GetMessage().Length == Constants.OBJECT_ID_STRING_LENGTH)
                    {
                        r.Append("object " + cmd_1.GetMessage() + " missing");
                    }
                    else
                    {
                        r.Append(cmd_1.GetMessage());
                    }
                }
                break;
            }

            case ReceiveCommand.Result.REJECTED_OTHER_REASON:
            {
                if (cmd_1.GetMessage() == null)
                {
                    r.Append("unspecified reason");
                }
                else
                {
                    r.Append(cmd_1.GetMessage());
                }
                break;
            }

            case ReceiveCommand.Result.LOCK_FAILURE:
            {
                r.Append("failed to lock");
                break;
            }

            case ReceiveCommand.Result.OK:
            {
                // We shouldn't have reached this case (see 'ok' case above).
                continue;
            }
            }
            @out.SendString(r.ToString());
        }
    }

    /// <summary>Close and flush (if necessary) the underlying streams.</summary>
    /// <remarks>Close and flush (if necessary) the underlying streams.</remarks>
    /// <exception cref="System.IO.IOException">System.IO.IOException</exception>
    protected internal virtual void Close()
    {
        if (sideBand)
        {
            // If we are using side band, we need to send a final
            // flush-pkt to tell the remote peer the side band is
            // complete and it should stop decoding. We need to
            // use the original output stream as rawOut is now the
            // side band data channel.
            //
            ((SideBandOutputStream)msgOut).FlushBuffer();
            ((SideBandOutputStream)rawOut).FlushBuffer();
            PacketLineOut plo = new PacketLineOut(origOut);
            plo.SetFlushOnEnd(false);
            plo.End();
        }
        if (biDirectionalPipe)
        {
            // If this was a native git connection, flush the pipe for
            // the caller. For smart HTTP we don't do this flush and
            // instead let the higher level HTTP servlet code do it.
            //
            if (!sideBand && msgOut != null)
            {
                msgOut.Flush();
            }
            rawOut.Flush();
        }
    }

    /// <summary>Release any resources used by this object.</summary>
    /// <remarks>Release any resources used by this object.</remarks>
    /// <exception cref="System.IO.IOException">the pack could not be unlocked.</exception>
    protected internal virtual void Release()
    {
        walk.Release();
        UnlockPack();
        timeoutIn = null;
        rawIn = null;
        rawOut = null;
        msgOut = null;
        pckIn = null;
        pckOut = null;
        refs = null;
        enabledCapabilities = null;
        commands = null;
        if (timer != null)
        {
            try
            {
                timer.Terminate();
            }
            finally
            {
                timer = null;
            }
        }
    }

    /// <summary>Interface for reporting status messages.</summary>
    /// <remarks>Interface for reporting status messages.</remarks>
    internal abstract class Reporter
    {
        /// <exception cref="System.IO.IOException"></exception>
        internal abstract void SendString(string s);
    }
}
}
