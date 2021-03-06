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

using System.Collections.Generic;
using System.Text;
using NGit;
using NGit.Api;
using NGit.Api.Errors;
using NGit.Junit;
using NGit.Revwalk;
using NGit.Storage.File;
using NGit.Submodule;
using NGit.Util;
using Sharpen;

namespace NGit.Api
{
[NUnit.Framework.TestFixture]
public class CloneCommandTest : RepositoryTestCase
{
    private Git git;

    private TestRepository<Repository> tr;

    /// <exception cref="System.Exception"></exception>
    public override void SetUp()
    {
        base.SetUp();
        tr = new TestRepository<Repository>(db);
        git = new Git(db);
        // commit something
        WriteTrashFile("Test.txt", "Hello world");
        git.Add().AddFilepattern("Test.txt").Call();
        git.Commit().SetMessage("Initial commit").Call();
        // create a master branch and switch to it
        git.BranchCreate().SetName("test").Call();
        RefUpdate rup = db.UpdateRef(Constants.HEAD);
        rup.Link("refs/heads/test");
        // commit something on the test branch
        WriteTrashFile("Test.txt", "Some change");
        git.Add().AddFilepattern("Test.txt").Call();
        git.Commit().SetMessage("Second commit").Call();
        RevBlob blob = tr.Blob("blob-not-in-master-branch");
        git.Tag().SetName("tag-for-blob").SetObjectId(blob).Call();
    }

    /// <exception cref="System.IO.IOException"></exception>
    /// <exception cref="NGit.Api.Errors.JGitInternalException"></exception>
    /// <exception cref="NGit.Api.Errors.GitAPIException"></exception>
    [NUnit.Framework.Test]
    public virtual void TestCloneRepository()
    {
        FilePath directory = CreateTempDirectory("testCloneRepository");
        CloneCommand command = Git.CloneRepository();
        command.SetDirectory(directory);
        command.SetURI("file://" + git.GetRepository().WorkTree.GetPath());
        Git git2 = command.Call();
        AddRepoToClose(git2.GetRepository());
        NUnit.Framework.Assert.IsNotNull(git2);
        ObjectId id = git2.GetRepository().Resolve("tag-for-blob");
        NUnit.Framework.Assert.IsNotNull(id);
        NUnit.Framework.Assert.AreEqual(git2.GetRepository().GetFullBranch(), "refs/heads/test"
                                       );
        NUnit.Framework.Assert.AreEqual("origin", git2.GetRepository().GetConfig().GetString
                                        (ConfigConstants.CONFIG_BRANCH_SECTION, "test", ConfigConstants.CONFIG_KEY_REMOTE
                                        ));
        NUnit.Framework.Assert.AreEqual("refs/heads/test", git2.GetRepository().GetConfig
                                        ().GetString(ConfigConstants.CONFIG_BRANCH_SECTION, "test", ConfigConstants.CONFIG_KEY_MERGE
                                                    ));
        NUnit.Framework.Assert.AreEqual(2, git2.BranchList().SetListMode(ListBranchCommand.ListMode
                                        .REMOTE).Call().Count);
    }

    /// <exception cref="System.IO.IOException"></exception>
    /// <exception cref="NGit.Api.Errors.JGitInternalException"></exception>
    /// <exception cref="NGit.Api.Errors.GitAPIException"></exception>
    [NUnit.Framework.Test]
    public virtual void TestCloneRepositoryWithBranch()
    {
        FilePath directory = CreateTempDirectory("testCloneRepositoryWithBranch");
        CloneCommand command = Git.CloneRepository();
        command.SetBranch("refs/heads/master");
        command.SetDirectory(directory);
        command.SetURI("file://" + git.GetRepository().WorkTree.GetPath());
        Git git2 = command.Call();
        AddRepoToClose(git2.GetRepository());
        NUnit.Framework.Assert.IsNotNull(git2);
        NUnit.Framework.Assert.AreEqual(git2.GetRepository().GetFullBranch(), "refs/heads/master"
                                       );
        NUnit.Framework.Assert.AreEqual("refs/heads/master, refs/remotes/origin/master, refs/remotes/origin/test"
                                        , AllRefNames(git2.BranchList().SetListMode(ListBranchCommand.ListMode.ALL).Call
                                                ()));
        // Same thing, but now without checkout
        directory = CreateTempDirectory("testCloneRepositoryWithBranch_bare");
        command = Git.CloneRepository();
        command.SetBranch("refs/heads/master");
        command.SetDirectory(directory);
        command.SetURI("file://" + git.GetRepository().WorkTree.GetPath());
        command.SetNoCheckout(true);
        git2 = command.Call();
        AddRepoToClose(git2.GetRepository());
        NUnit.Framework.Assert.IsNotNull(git2);
        NUnit.Framework.Assert.AreEqual(git2.GetRepository().GetFullBranch(), "refs/heads/master"
                                       );
        NUnit.Framework.Assert.AreEqual("refs/remotes/origin/master, refs/remotes/origin/test"
                                        , AllRefNames(git2.BranchList().SetListMode(ListBranchCommand.ListMode.ALL).Call
                                                ()));
        // Same thing, but now test with bare repo
        directory = CreateTempDirectory("testCloneRepositoryWithBranch_bare");
        command = Git.CloneRepository();
        command.SetBranch("refs/heads/master");
        command.SetDirectory(directory);
        command.SetURI("file://" + git.GetRepository().WorkTree.GetPath());
        command.SetBare(true);
        git2 = command.Call();
        AddRepoToClose(git2.GetRepository());
        NUnit.Framework.Assert.IsNotNull(git2);
        NUnit.Framework.Assert.AreEqual(git2.GetRepository().GetFullBranch(), "refs/heads/master"
                                       );
        NUnit.Framework.Assert.AreEqual("refs/heads/master, refs/heads/test", AllRefNames
                                        (git2.BranchList().SetListMode(ListBranchCommand.ListMode.ALL).Call()));
    }

    /// <exception cref="System.IO.IOException"></exception>
    /// <exception cref="NGit.Api.Errors.JGitInternalException"></exception>
    /// <exception cref="NGit.Api.Errors.GitAPIException"></exception>
    [NUnit.Framework.Test]
    public virtual void TestCloneRepositoryOnlyOneBranch()
    {
        FilePath directory = CreateTempDirectory("testCloneRepositoryWithBranch");
        CloneCommand command = Git.CloneRepository();
        command.SetBranch("refs/heads/master");
        command.SetBranchesToClone(Collections.SingletonList("refs/heads/master"));
        command.SetDirectory(directory);
        command.SetURI("file://" + git.GetRepository().WorkTree.GetPath());
        Git git2 = command.Call();
        AddRepoToClose(git2.GetRepository());
        NUnit.Framework.Assert.IsNotNull(git2);
        NUnit.Framework.Assert.AreEqual(git2.GetRepository().GetFullBranch(), "refs/heads/master"
                                       );
        NUnit.Framework.Assert.AreEqual("refs/remotes/origin/master", AllRefNames(git2.BranchList
                                        ().SetListMode(ListBranchCommand.ListMode.REMOTE).Call()));
        // Same thing, but now test with bare repo
        directory = CreateTempDirectory("testCloneRepositoryWithBranch_bare");
        command = Git.CloneRepository();
        command.SetBranch("refs/heads/master");
        command.SetBranchesToClone(Collections.SingletonList("refs/heads/master"));
        command.SetDirectory(directory);
        command.SetURI("file://" + git.GetRepository().WorkTree.GetPath());
        command.SetBare(true);
        git2 = command.Call();
        AddRepoToClose(git2.GetRepository());
        NUnit.Framework.Assert.IsNotNull(git2);
        NUnit.Framework.Assert.AreEqual(git2.GetRepository().GetFullBranch(), "refs/heads/master"
                                       );
        NUnit.Framework.Assert.AreEqual("refs/heads/master", AllRefNames(git2.BranchList(
                                        ).SetListMode(ListBranchCommand.ListMode.ALL).Call()));
    }

    public static string AllRefNames(IList<Ref> refs)
    {
        StringBuilder sb = new StringBuilder();
        foreach (Ref f in refs)
        {
            if (sb.Length > 0)
            {
                sb.Append(", ");
            }
            sb.Append(f.GetName());
        }
        return sb.ToString();
    }

    /// <exception cref="System.IO.IOException"></exception>
    /// <exception cref="NGit.Api.Errors.JGitInternalException"></exception>
    /// <exception cref="NGit.Api.Errors.GitAPIException"></exception>
    [NUnit.Framework.Test]
    public virtual void TestCloneRepositoryWhenDestinationDirectoryExistsAndIsNotEmpty
    ()
    {
        string dirName = "testCloneTargetDirectoryNotEmpty";
        FilePath directory = CreateTempDirectory(dirName);
        CloneCommand command = Git.CloneRepository();
        command.SetDirectory(directory);
        command.SetURI("file://" + git.GetRepository().WorkTree.GetPath());
        Git git2 = command.Call();
        AddRepoToClose(git2.GetRepository());
        NUnit.Framework.Assert.IsNotNull(git2);
        // clone again
        command = Git.CloneRepository();
        command.SetDirectory(directory);
        command.SetURI("file://" + git.GetRepository().WorkTree.GetPath());
        try
        {
            git2 = command.Call();
            // we shouldn't get here
            NUnit.Framework.Assert.Fail("destination directory already exists and is not an empty folder, cloning should fail"
                                       );
        }
        catch (JGitInternalException e)
        {
            NUnit.Framework.Assert.IsTrue(e.Message.Contains("not an empty directory"));
            NUnit.Framework.Assert.IsTrue(e.Message.Contains(dirName));
        }
    }

    /// <exception cref="System.Exception"></exception>
    [NUnit.Framework.Test]
    public virtual void TestCloneRepositoryWithMultipleHeadBranches()
    {
        git.Checkout().SetName(Constants.MASTER).Call();
        git.BranchCreate().SetName("a").Call();
        FilePath directory = CreateTempDirectory("testCloneRepositoryWithMultipleHeadBranches"
                                                );
        CloneCommand clone = Git.CloneRepository();
        clone.SetDirectory(directory);
        clone.SetURI("file://" + git.GetRepository().WorkTree.GetPath());
        Git git2 = clone.Call();
        AddRepoToClose(git2.GetRepository());
        NUnit.Framework.Assert.IsNotNull(git2);
        NUnit.Framework.Assert.AreEqual(Constants.MASTER, git2.GetRepository().GetBranch(
                                        ));
    }

    /// <exception cref="System.Exception"></exception>
    [NUnit.Framework.Test]
    public virtual void TestCloneRepositoryWithSubmodules()
    {
        git.Checkout().SetName(Constants.MASTER).Call();
        string file = "file.txt";
        WriteTrashFile(file, "content");
        git.Add().AddFilepattern(file).Call();
        RevCommit commit = git.Commit().SetMessage("create file").Call();
        SubmoduleAddCommand command = new SubmoduleAddCommand(db);
        string path = "sub";
        command.SetPath(path);
        string uri = db.Directory.ToURI().ToString();
        command.SetURI(uri);
        Repository repo = command.Call();
        NUnit.Framework.Assert.IsNotNull(repo);
        AddRepoToClose(repo);
        git.Add().AddFilepattern(path).AddFilepattern(Constants.DOT_GIT_MODULES).Call();
        git.Commit().SetMessage("adding submodule").Call();
        FilePath directory = CreateTempDirectory("testCloneRepositoryWithSubmodules");
        CloneCommand clone = Git.CloneRepository();
        clone.SetDirectory(directory);
        clone.SetCloneSubmodules(true);
        clone.SetURI("file://" + git.GetRepository().WorkTree.GetPath());
        Git git2 = clone.Call();
        AddRepoToClose(git2.GetRepository());
        NUnit.Framework.Assert.IsNotNull(git2);
        NUnit.Framework.Assert.AreEqual(Constants.MASTER, git2.GetRepository().GetBranch(
                                        ));
        NUnit.Framework.Assert.IsTrue(new FilePath(git2.GetRepository().WorkTree, path +
                                      FilePath.separatorChar + file).Exists());
        SubmoduleStatusCommand status = new SubmoduleStatusCommand(git2.GetRepository());
        IDictionary<string, SubmoduleStatus> statuses = status.Call();
        SubmoduleStatus pathStatus = statuses.Get(path);
        NUnit.Framework.Assert.IsNotNull(pathStatus);
        NUnit.Framework.Assert.AreEqual(SubmoduleStatusType.INITIALIZED, pathStatus.GetType
                                        ());
        NUnit.Framework.Assert.AreEqual(commit, pathStatus.GetHeadId());
        NUnit.Framework.Assert.AreEqual(commit, pathStatus.GetIndexId());
    }

    /// <exception cref="System.Exception"></exception>
    [NUnit.Framework.Test]
    public virtual void TestCloneRepositoryWithNestedSubmodules()
    {
        git.Checkout().SetName(Constants.MASTER).Call();
        // Create submodule 1
        FilePath submodule1 = CreateTempDirectory("testCloneRepositoryWithNestedSubmodules1"
                                                 );
        Git sub1Git = Git.Init().SetDirectory(submodule1).Call();
        NUnit.Framework.Assert.IsNotNull(sub1Git);
        Repository sub1 = sub1Git.GetRepository();
        NUnit.Framework.Assert.IsNotNull(sub1);
        AddRepoToClose(sub1);
        string file = "file.txt";
        string path = "sub";
        Write(new FilePath(sub1.WorkTree, file), "content");
        sub1Git.Add().AddFilepattern(file).Call();
        RevCommit commit = sub1Git.Commit().SetMessage("create file").Call();
        NUnit.Framework.Assert.IsNotNull(commit);
        // Create submodule 2
        FilePath submodule2 = CreateTempDirectory("testCloneRepositoryWithNestedSubmodules2"
                                                 );
        Git sub2Git = Git.Init().SetDirectory(submodule2).Call();
        NUnit.Framework.Assert.IsNotNull(sub2Git);
        Repository sub2 = sub2Git.GetRepository();
        NUnit.Framework.Assert.IsNotNull(sub2);
        AddRepoToClose(sub2);
        Write(new FilePath(sub2.WorkTree, file), "content");
        sub2Git.Add().AddFilepattern(file).Call();
        RevCommit sub2Head = sub2Git.Commit().SetMessage("create file").Call();
        NUnit.Framework.Assert.IsNotNull(sub2Head);
        // Add submodule 2 to submodule 1
        Repository r = sub1Git.SubmoduleAdd().SetPath(path).SetURI(sub2.Directory.ToURI()
                       .ToString()).Call();
        NUnit.Framework.Assert.IsNotNull(r);
        AddRepoToClose(r);
        RevCommit sub1Head = sub1Git.Commit().SetAll(true).SetMessage("Adding submodule")
                             .Call();
        NUnit.Framework.Assert.IsNotNull(sub1Head);
        // Add submodule 1 to default repository
        r = git.SubmoduleAdd().SetPath(path).SetURI(sub1.Directory.ToURI().ToString()).Call
            ();
        NUnit.Framework.Assert.IsNotNull(r);
        AddRepoToClose(r);
        NUnit.Framework.Assert.IsNotNull(git.Commit().SetAll(true).SetMessage("Adding submodule"
                                                                             ).Call());
        // Clone default repository and include submodules
        FilePath directory = CreateTempDirectory("testCloneRepositoryWithNestedSubmodules"
                                                );
        CloneCommand clone = Git.CloneRepository();
        clone.SetDirectory(directory);
        clone.SetCloneSubmodules(true);
        clone.SetURI(git.GetRepository().Directory.ToURI().ToString());
        Git git2 = clone.Call();
        AddRepoToClose(git2.GetRepository());
        NUnit.Framework.Assert.IsNotNull(git2);
        NUnit.Framework.Assert.AreEqual(Constants.MASTER, git2.GetRepository().GetBranch(
                                        ));
        NUnit.Framework.Assert.IsTrue(new FilePath(git2.GetRepository().WorkTree, path +
                                      FilePath.separatorChar + file).Exists());
        NUnit.Framework.Assert.IsTrue(new FilePath(git2.GetRepository().WorkTree, path +
                                      FilePath.separatorChar + path + FilePath.separatorChar + file).Exists());
        SubmoduleStatusCommand status = new SubmoduleStatusCommand(git2.GetRepository());
        IDictionary<string, SubmoduleStatus> statuses = status.Call();
        SubmoduleStatus pathStatus = statuses.Get(path);
        NUnit.Framework.Assert.IsNotNull(pathStatus);
        NUnit.Framework.Assert.AreEqual(SubmoduleStatusType.INITIALIZED, pathStatus.GetType
                                        ());
        NUnit.Framework.Assert.AreEqual(sub1Head, pathStatus.GetHeadId());
        NUnit.Framework.Assert.AreEqual(sub1Head, pathStatus.GetIndexId());
        SubmoduleWalk walk = SubmoduleWalk.ForIndex(git2.GetRepository());
        NUnit.Framework.Assert.IsTrue(walk.Next());
        Repository clonedSub1 = walk.GetRepository();
        AddRepoToClose(clonedSub1);
        NUnit.Framework.Assert.IsNotNull(clonedSub1);
        status = new SubmoduleStatusCommand(clonedSub1);
        statuses = status.Call();
        pathStatus = statuses.Get(path);
        NUnit.Framework.Assert.IsNotNull(pathStatus);
        NUnit.Framework.Assert.AreEqual(SubmoduleStatusType.INITIALIZED, pathStatus.GetType
                                        ());
        NUnit.Framework.Assert.AreEqual(sub2Head, pathStatus.GetHeadId());
        NUnit.Framework.Assert.AreEqual(sub2Head, pathStatus.GetIndexId());
        NUnit.Framework.Assert.IsFalse(walk.Next());
    }

    /// <exception cref="System.Exception"></exception>
    [NUnit.Framework.Test]
    public virtual void TestCloneWithAutoSetupRebase()
    {
        FilePath directory = CreateTempDirectory("testCloneRepository1");
        CloneCommand command = Git.CloneRepository();
        command.SetDirectory(directory);
        command.SetURI("file://" + git.GetRepository().WorkTree.GetPath());
        Git git2 = command.Call();
        AddRepoToClose(git2.GetRepository());
        NUnit.Framework.Assert.IsFalse(git2.GetRepository().GetConfig().GetBoolean(ConfigConstants
                                       .CONFIG_BRANCH_SECTION, "test", ConfigConstants.CONFIG_KEY_REBASE, false));
        FileBasedConfig userConfig = SystemReader.GetInstance().OpenUserConfig(null, git.
                                     GetRepository().FileSystem);
        userConfig.SetString(ConfigConstants.CONFIG_BRANCH_SECTION, null, ConfigConstants
                             .CONFIG_KEY_AUTOSETUPREBASE, ConfigConstants.CONFIG_KEY_ALWAYS);
        userConfig.Save();
        directory = CreateTempDirectory("testCloneRepository2");
        command = Git.CloneRepository();
        command.SetDirectory(directory);
        command.SetURI("file://" + git.GetRepository().WorkTree.GetPath());
        git2 = command.Call();
        AddRepoToClose(git2.GetRepository());
        NUnit.Framework.Assert.IsTrue(git2.GetRepository().GetConfig().GetBoolean(ConfigConstants
                                      .CONFIG_BRANCH_SECTION, "test", ConfigConstants.CONFIG_KEY_REBASE, false));
        userConfig.SetString(ConfigConstants.CONFIG_BRANCH_SECTION, null, ConfigConstants
                             .CONFIG_KEY_AUTOSETUPREBASE, ConfigConstants.CONFIG_KEY_REMOTE);
        userConfig.Save();
        directory = CreateTempDirectory("testCloneRepository2");
        command = Git.CloneRepository();
        command.SetDirectory(directory);
        command.SetURI("file://" + git.GetRepository().WorkTree.GetPath());
        git2 = command.Call();
        AddRepoToClose(git2.GetRepository());
        NUnit.Framework.Assert.IsTrue(git2.GetRepository().GetConfig().GetBoolean(ConfigConstants
                                      .CONFIG_BRANCH_SECTION, "test", ConfigConstants.CONFIG_KEY_REBASE, false));
    }
}
}
