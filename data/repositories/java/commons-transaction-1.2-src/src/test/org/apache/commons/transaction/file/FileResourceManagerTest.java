/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.transaction.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.transaction.Status;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.transaction.util.CommonsLoggingLogger;
import org.apache.commons.transaction.util.FileHelper;
import org.apache.commons.transaction.util.LoggerFacade;
import org.apache.commons.transaction.util.RendezvousBarrier;

/**
 * Tests for FileResourceManager.
 *
 * @version $Id: FileResourceManagerTest.java 493628 2007-01-07 01:42:48Z joerg $
 */
public class FileResourceManagerTest extends TestCase
{

    private static final Log log = LogFactory.getLog(FileResourceManagerTest.class.getName());
    private static final LoggerFacade sLogger = new CommonsLoggingLogger(log);

    private static final String STORE = "tmp/store";
    private static final String WORK = "tmp/work";
    private static final String ENCODING = "ISO-8859-15";
    // FIXME
    // XXX INCREASE THIS WHEN DEBUGGING OTHERWISE THE BARRIER WILL TIME OUT AFTER TWO SECONDS
    // MOST LIKELY CONFUSING YOU COMPLETELY
    private static final long BARRIER_TIMEOUT = 200000;

    private static final String[] INITIAL_FILES = new String[] { STORE + "/olli/Hubert6", STORE + "/olli/Hubert" };

    private static final String STATUS_COMMITTING_CONTEXT =
        "8\n10\n2000\n1063099404687\n";
    private static final String[] STATUS_COMMITTING_CONTEXT_CHANGE_FILES =
        new String[] { "olli/Hubert40", "olli/Hubert50" };
    private static final String[] STATUS_COMMITTING_CONTEXT_DELETE_FILES = new String[] { "/olli/Hubert" };
    private static final String[] STATUS_COMMITTING_CONTEXT_RESULT_FILES =
        new String[] { "Hubert6", "Hubert50", "Hubert40" };

    private static void initCommittingRecovery() throws Throwable
    {
        String txId = "COMMITTING";
        createTxContextFile(txId, STATUS_COMMITTING_CONTEXT);
        createTxDeleteFiles(txId, STATUS_COMMITTING_CONTEXT_DELETE_FILES);
        createTxChangeFiles(txId, STATUS_COMMITTING_CONTEXT_CHANGE_FILES);
    }

    private static final String STATUS_COMMITTED_CONTEXT =
        "3\n10\n2000\n1063099404687\n";
    private static final String[] STATUS_COMMITTED_CONTEXT_CHANGE_FILES =
        new String[] { "olli/Hubert4", "olli/Hubert5" };
    private static final String[] STATUS_COMMITTED_CONTEXT_DELETE_FILES = new String[]
    {
    };
    private static final String[] STATUS_COMMITTED_CONTEXT_RESULT_FILES = new String[] { "Hubert6", "Hubert" };

    protected static final long TIMEOUT = Long.MAX_VALUE;

    private static int deadlockCnt = 0;

    private static void initCommittedRecovery() throws Throwable
    {
        String txId = "COMMITTED";
        createTxContextFile(txId, STATUS_COMMITTED_CONTEXT);
        createTxDeleteFiles(txId, STATUS_COMMITTED_CONTEXT_DELETE_FILES);
        createTxChangeFiles(txId, STATUS_COMMITTED_CONTEXT_CHANGE_FILES);
    }

    private static final String STATUS_ROLLING_BACK_CONTEXT =
        "9\n10\n2000\n1063099404687\n";
    private static final String[] STATUS_ROLLING_BACK_CONTEXT_CHANGE_FILES =
        new String[] { "olli/Hubert4", "olli/Hubert5" };
    private static final String[] STATUS_ROLLING_BACK_CONTEXT_DELETE_FILES = new String[]
    {
    };
    private static final String[] STATUS_ROLLING_BACK_CONTEXT_RESULT_FILES = new String[] { "Hubert6", "Hubert" };

    private static void initRollingBackRecovery() throws Throwable
    {
        String txId = "ROLLING_BACK";
        createTxContextFile(txId, STATUS_ROLLING_BACK_CONTEXT);
        createTxDeleteFiles(txId, STATUS_ROLLING_BACK_CONTEXT_DELETE_FILES);
        createTxChangeFiles(txId, STATUS_ROLLING_BACK_CONTEXT_CHANGE_FILES);
    }

    private static final String STATUS_ROLLEDBACK_CONTEXT =
        "4\n10\n2000\n1063099404687\n";
    private static final String[] STATUS_ROLLEDBACK_CONTEXT_CHANGE_FILES =
        new String[] { "olli/Hubert4", "olli/Hubert5" };
    private static final String[] STATUS_ROLLEDBACK_CONTEXT_DELETE_FILES = new String[]
    {
    };
    private static final String[] STATUS_ROLLEDBACK_CONTEXT_RESULT_FILES = new String[] { "Hubert6", "Hubert" };

    private static void initRolledBackRecovery() throws Throwable
    {
        String txId = "ROLLEDBACK";
        createTxContextFile(txId, STATUS_ROLLEDBACK_CONTEXT);
        createTxDeleteFiles(txId, STATUS_ROLLEDBACK_CONTEXT_DELETE_FILES);
        createTxChangeFiles(txId, STATUS_ROLLEDBACK_CONTEXT_CHANGE_FILES);
    }

    private static final String STATUS_ACTIVE_CONTEXT = "0\n10\n2000\n1063099404687\n";
    private static final String[] STATUS_ACTIVE_CONTEXT_CHANGE_FILES = new String[] { "olli/Hubert4", "olli/Hubert5" };
    private static final String[] STATUS_ACTIVE_CONTEXT_DELETE_FILES = new String[]
    {
    };
    private static final String[] STATUS_ACTIVE_CONTEXT_RESULT_FILES = new String[] { "Hubert6", "Hubert" };

    private static void initActiveRecovery() throws Throwable
    {
        String txId = "ACTIVE";
        createTxContextFile(txId, STATUS_ACTIVE_CONTEXT);
        createTxDeleteFiles(txId, STATUS_ACTIVE_CONTEXT_DELETE_FILES);
        createTxChangeFiles(txId, STATUS_ACTIVE_CONTEXT_CHANGE_FILES);
    }

    private static void removeRec(String dirPath)
    {
        FileHelper.removeRec(new File(dirPath));
    }

    private static final void createFiles(String[] filePaths)
    {
        createFiles(filePaths, null, null);
    }

    private static final void createFiles(String[] filePaths, String dirPath)
    {
        createFiles(filePaths, null, dirPath);
    }

    private static final void createFiles(String[] filePaths, String[] contents)
    {
        createFiles(filePaths, contents, null);
    }

    private static final void createFiles(String[] filePaths, String[] contents, String dirPath)
    {
        for (int i = 0; i < filePaths.length; i++)
        {
            String filePath = filePaths[i];
            File file;
            if (dirPath != null)
            {
                file = new File(new File(dirPath), filePath);
            }
            else
            {
                file = new File(filePath);
            }
            file.getParentFile().mkdirs();
            try
            {
                file.delete();
                file.createNewFile();
                String content = null;
                if (contents != null && contents.length > i)
                {
                    content = contents[i];
                }
                if (content != null)
                {
                    FileOutputStream stream = new FileOutputStream(file);
                    stream.write(contents[i].getBytes(ENCODING));
                    stream.close();
                }
            }
            catch (IOException e)
            {
            }
        }
    }

    private static final void checkIsEmpty(String dirPath)
    {
        checkExactlyContains(dirPath, null);
    }
    private static final void checkExactlyContains(String dirPath, String[] fileNames)
    {
        checkExactlyContains(dirPath, fileNames, null);
    }

    private static final void checkExactlyContains(String dirPath, String[] fileNames,
            String[] contents)
    {
        File dir = new File(dirPath);

        if (dir.isDirectory())
        {
            File[] files = dir.listFiles();
            if (fileNames == null)
            {
                if (files.length != 0)
                {
                    fail(dirPath + " must be empty");
                }
                else
                {
                    return;
                }
            }

            if (files.length != fileNames.length)
            {
                fail(dirPath + " contains " + files.length + " instead of " + fileNames.length
                     + " files");
            }

            for (int i = 0; i < fileNames.length; i++)
            {
                String fileName = fileNames[i];
                boolean match = false;
                File file = null;
                for (int j = 0; j < files.length; j++)
                {
                    file = files[j];
                    if (file.getName().equals(fileName))
                    {
                        match = true;
                        break;
                    }
                }
                if (!match)
                {
                    fail(dirPath + " does not contain required " + fileName);
                }

                String content = null;
                if (contents != null && i < contents.length)
                {
                    content = contents[i];
                }
                if (content != null && !compare(file, content))
                {
                    fail("Contents of " + fileName + " in " + dirPath
                         + " does not contain required content '" + content + "'");
                }
            }

        }
        else
        {
            fail(dirPath + " is not directoy");
        }
    }

    private static boolean compare(FileInputStream stream, byte[] bytes)
    {
        int read;
        int count = 0;
        try
        {
            while ((read = stream.read()) != -1)
            {
                if (bytes[count++] != read)
                {
                    return false;
                }
            }
        }
        catch (IOException e)
        {
            return false;
        }
        return true;
    }

    private static boolean compare(File file, String content)
    {
        FileInputStream stream = null;
        try
        {
            byte[] bytes = content.getBytes(ENCODING);
            stream = new FileInputStream(file);
            return compare(stream, bytes);
        }
        catch (Throwable t)
        {
            return false;
        }
        finally
        {
            if (stream != null)
            {
                try
                {
                    stream.close();
                }
                catch (IOException e)
                {
                }
            }
        }
    }

    private static String workForTx(Object txId)
    {
        return WORK + "/" + txId;
    }

    private static String changeForTx(Object txId)
    {
        return workForTx(txId) + "/change";
    }

    private static String deleteForTx(Object txId)
    {
        return workForTx(txId) + "/delete";
    }

    private static String logForTx(Object txId)
    {
        return workForTx(txId) + "/transaction.log";
    }

    private static void reset()
    {
        removeRec(STORE);
        removeRec(WORK);
        new File(STORE).mkdirs();
        new File(WORK).mkdirs();
    }

    private static void createInitialFiles()
    {
        createFiles(INITIAL_FILES);
    }

    private static void createTxContextFile(Object txId, String content)
    {
        createFiles(new String[] { logForTx(txId)}, new String[] { txId + "\n" + content });
    }

    private static void createTxDeleteFiles(Object txId, String[] files)
    {
        createFiles(files, deleteForTx(txId));
    }

    private static void createTxChangeFiles(Object txId, String[] files)
    {
        createFiles(files, changeForTx(txId));
    }

    // XXX need this, as JUnit seems to print only part of these strings
    private static void report(String should, String is)
    {
        if (!is.equals(should))
        {
            fail("\nWrong output:\n'" + is + "'\nShould be:\n'" + should + "'\n");
        }
    }

    public static FileResourceManager createFRM()
    {
        return new FileResourceManager(STORE, WORK, false, sLogger, true);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite(FileResourceManagerTest.class);
        return suite;
    }

    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public FileResourceManagerTest(String testName)
    {
        super(testName);
    }

    public void testGlobal() throws Throwable
    {
        reset();
        createInitialFiles();

        final FileResourceManager rm = createFRM();

        rm.start();

        final RendezvousBarrier shutdownBarrier = new RendezvousBarrier("Shutdown", 3, BARRIER_TIMEOUT, sLogger);
        final RendezvousBarrier start2Barrier = new RendezvousBarrier("Start2", BARRIER_TIMEOUT, sLogger);
        final RendezvousBarrier commit1Barrier = new RendezvousBarrier("Commit1", BARRIER_TIMEOUT, sLogger);

        final Object txId1 = "Create";

        Thread create = new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    rm.startTransaction(txId1);

                    shutdownBarrier.call();
                    start2Barrier.call();

                    rm.createResource(txId1, "/olli/Hubert4");
                    rm.createResource(txId1, "/olli/Hubert5");
                    String msg = "Greetings from " + txId1 + "\n";
                    OutputStream out = rm.writeResource(txId1, "/olli/Hubert6");
                    out.write(msg.getBytes(ENCODING));

                    commit1Barrier.meet();

                    checkExactlyContains(
                        changeForTx(txId1) + "/olli",
                        new String[] { "Hubert4", "Hubert5", "Hubert6" },
                        new String[] { "", "", "Greetings from " + txId1 + "\n" });

                    rm.commitTransaction(txId1);

                    checkExactlyContains(
                        STORE + "/olli",
                        new String[] { "Hubert", "Hubert4", "Hubert5", "Hubert6" },
                        new String[] { "", "", "", "Greetings from " + txId1 + "\n" });

                }
                catch (Throwable e)
                {
                    System.err.println("Error: " + e);
                    e.printStackTrace();
                }
            }
        }, "Create Thread");

        Thread modify = new Thread(new Runnable()
        {
            public void run()
            {
                Object txId = null;
                try
                {

                    {
                        InputStream in = rm.readResource("/olli/Hubert6");
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, ENCODING));
                        String line = reader.readLine();
                        assertEquals(line, null);
                        in.close();
                    }

                    txId = "Modify";
                    rm.startTransaction(txId);
                    rm.setIsolationLevel(txId, ResourceManager.ISOLATION_LEVEL_READ_COMMITTED);

                    {
                        InputStream in = rm.readResource(txId, "/olli/Hubert6");
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, ENCODING));
                        String line = reader.readLine();
                        assertEquals(line, null);
                        in.close();
                    }

                    shutdownBarrier.call();

                    rm.createResource(txId, "/olli/Hubert1");
                    rm.createResource(txId, "/olli/Hubert2");
                    rm.createResource(txId, "/olli/Hubert3");

                    // wait until tx commits, so there already are Hubert4 and Hubert5 and
                    // Hubert6 changes
                    commit1Barrier.meet();

                    rm.createResource(txId, "/olli/Hubert4");
                    rm.createResource(txId, "/olli/Hubert5");

                    rm.createResource(txId, "/olli/Hubert6");
                    InputStream in = rm.readResource(txId, "/olli/Hubert6");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, ENCODING));
                    String line = reader.readLine();
                    // allow for update while in tx as this is READ_COMMITED
                    report("Greetings from " + txId1, line);
                    in.close();

                    rm.deleteResource(txId, "/olli/Hubert");
                    rm.deleteResource(txId, "/olli/Hubert2");
                    rm.deleteResource(txId, "/olli/Hubert3");
                    rm.deleteResource(txId, "/olli/Hubert4");
                    rm.deleteResource(txId, "/olli/Hubert5");

                    checkExactlyContains(deleteForTx(txId) + "/olli", new String[] { "Hubert", "Hubert4", "Hubert5" });

                    checkExactlyContains(changeForTx(txId) + "/olli", new String[] { "Hubert1" });

                    rm.commitTransaction(txId);
                }
                catch (Throwable e)
                {
                    System.err.println("Error: " + e);
                    e.printStackTrace();
                }
            }
        }, "Modify Thread");

        create.start();
        // be sure first thread is started before trying next
        start2Barrier.meet();
        modify.start();

        // let both transaction start before trying to shut down
        shutdownBarrier.meet();

        assertTrue(rm.stop(ResourceManager.SHUTDOWN_MODE_NORMAL, 5000));

        checkExactlyContains(
            STORE + "/olli",
            new String[] { "Hubert1", "Hubert6" },
            new String[] { "", "Greetings from " + txId1 + "\n" });
        checkIsEmpty(WORK);
    }

    public void testCombinedRecovery() throws Throwable
    {
        reset();
        createInitialFiles();
        initCommittingRecovery();
        initCommittedRecovery();
        initActiveRecovery();
        initRolledBackRecovery();
        initRollingBackRecovery();

        FileResourceManager rm =createFRM();

        // do nothing, just start and stop to check recovery of tx
        rm.start();
        assertTrue(rm.stop(ResourceManager.SHUTDOWN_MODE_NORMAL, 5000));

        // all but committing should be rolled back
        checkExactlyContains(STORE + "/olli", STATUS_COMMITTING_CONTEXT_RESULT_FILES);
        checkIsEmpty(WORK);
    }

    public void testCommittingRecovery() throws Throwable
    {
        reset();
        createInitialFiles();
        initCommittingRecovery();

        FileResourceManager rm = createFRM();

        // do nothing, just start and stop to check recovery of tx
        rm.start();
        assertTrue(rm.stop(ResourceManager.SHUTDOWN_MODE_NORMAL, 5000));

        checkExactlyContains(STORE + "/olli", STATUS_COMMITTING_CONTEXT_RESULT_FILES);
        checkIsEmpty(WORK);
    }

    public void testActiveRecovery() throws Throwable
    {
        reset();
        createInitialFiles();
        initActiveRecovery();

        FileResourceManager rm = createFRM();

        // do nothing, just start and stop to check recovery of tx
        rm.start();
        assertTrue(rm.stop(ResourceManager.SHUTDOWN_MODE_NORMAL, 5000));

        checkExactlyContains(STORE + "/olli", STATUS_ACTIVE_CONTEXT_RESULT_FILES);
        checkIsEmpty(WORK);
    }

    public void testRolledbackRecovery() throws Throwable
    {
        reset();
        createInitialFiles();
        initRolledBackRecovery();

        FileResourceManager rm = createFRM();

        // do nothing, just start and stop to check recovery of tx
        rm.start();
        assertTrue(rm.stop(ResourceManager.SHUTDOWN_MODE_NORMAL, 5000));

        checkExactlyContains(STORE + "/olli", STATUS_ROLLEDBACK_CONTEXT_RESULT_FILES);
        checkIsEmpty(WORK);
    }

    public void testRollingBackRecovery() throws Throwable
    {
        reset();
        createInitialFiles();
        initRollingBackRecovery();

        FileResourceManager rm = createFRM();

        // do nothing, just start and stop to check recovery of tx
        rm.start();
        assertTrue(rm.stop(ResourceManager.SHUTDOWN_MODE_NORMAL, 5000));

        checkExactlyContains(STORE + "/olli", STATUS_ROLLING_BACK_CONTEXT_RESULT_FILES);
        checkIsEmpty(WORK);
    }

    public void testCommittedRecovery() throws Throwable
    {
        reset();
        createInitialFiles();
        initCommittedRecovery();

        FileResourceManager rm = createFRM();

        // do nothing, just start and stop to check recovery of tx
        rm.start();
        assertTrue(rm.stop(ResourceManager.SHUTDOWN_MODE_NORMAL, 5000));

        checkExactlyContains(STORE + "/olli", STATUS_COMMITTED_CONTEXT_RESULT_FILES);
        checkIsEmpty(WORK);
    }

    public void testInteractiveDirtyRecovery() throws Throwable
    {
        reset();
        createInitialFiles();

        FileResourceManager rm = createFRM();

        rm.start();

        String txId = "DIRTY";
        rm.startTransaction(txId);
        rm.createResource(txId, "/olli/Hubert100");

        // fake a failed commit
        FileResourceManager.TransactionContext context = rm.getContext(txId);
        // needing synchronization in order not to interfer with shutdown thread
        synchronized (context)
        {
            sLogger.logFine("Committing Tx " + txId);

            context.status = Status.STATUS_COMMITTING;
            context.saveState();
            rm.dirty = true;
            context.finalCleanUp();
            context.notifyFinish();
        }

        // should be allowed
        rm.readResource(txId, "/olli/Hubert");

        // should be disallowed
        boolean writeDeniedByDirty = false;
        try
        {
            rm.createResource(txId, "/olli/Hubert10");
        }
        catch (ResourceManagerSystemException rmse)
        {
            writeDeniedByDirty = true;
        }
        assertTrue(writeDeniedByDirty);

        // on success (expected) resets dirty flag
        rm.recover();

        // should all be allowed again
        txId = "DIRTYTEST";
        rm.startTransaction(txId);
        rm.readResource(txId, "/olli/Hubert");
        rm.createResource(txId, "/olli/Hubert10");
        rm.commitTransaction(txId);

        assertTrue(rm.stop(ResourceManager.SHUTDOWN_MODE_NORMAL, 5000));

        // tx rolled forward created "/olli/Hubert100", so it should be here as well
        checkExactlyContains(STORE + "/olli", new String[] { "Hubert", "Hubert100", "Hubert6", "Hubert10" });
        checkIsEmpty(WORK);
    }

    public void testConflict() throws Throwable
    {
        sLogger.logInfo("Checking concurrent transaction features");

        reset();
        createInitialFiles();

        final FileResourceManager rm = createFRM();

        rm.start();

        final RendezvousBarrier restart = new RendezvousBarrier("restart",
                TIMEOUT, sLogger);

        for (int i = 0; i < 25; i++)
        {

            final RendezvousBarrier deadlockBarrier1 = new RendezvousBarrier("deadlock" + i,
                    TIMEOUT, sLogger);

            Thread thread1 = new Thread(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        rm.startTransaction("tx1");
                        // first both threads get a lock, this one on res2
                        rm.createResource("tx1", "key2");
                        synchronized (deadlockBarrier1)
                        {
                            deadlockBarrier1.meet();
                            deadlockBarrier1.reset();
                        }
                        // if I am first, the other thread will be dead, i.e.
                        // exactly one
                        rm.createResource("tx1", "key1");
                        rm.commitTransaction("tx1");
                    }
                    catch (InterruptedException ie)
                    {
                    }
                    catch (ResourceManagerException e)
                    {
                        assertEquals(e.getStatus(), ResourceManagerErrorCodes.ERR_DEAD_LOCK);
                        deadlockCnt++;
                        try
                        {
                            rm.rollbackTransaction("tx1");
                        }
                        catch (ResourceManagerException e1)
                        {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                    finally
                    {
                        try
                        {
                            synchronized (restart)
                            {
                                restart.meet();
                                restart.reset();
                            }
                        }
                        catch (InterruptedException ie) {}

                    }
                }
            }, "Thread1");

            thread1.start();

            rm.startTransaction("tx2");
            try
            {
                // first both threads get a lock, this one on res2
                rm.deleteResource("tx2", "key1");
                synchronized (deadlockBarrier1)
                {
                    deadlockBarrier1.meet();
                    deadlockBarrier1.reset();
                }
                //          if I am first, the other thread will be dead, i.e. exactly
                // one
                rm.deleteResource("tx2", "key2");
                rm.commitTransaction("tx2");
            }
            catch (ResourceManagerException e)
            {
                assertEquals(e.getStatus(), ResourceManagerErrorCodes.ERR_DEAD_LOCK);
                deadlockCnt++;
                try
                {
                    rm.rollbackTransaction("tx2");
                }
                catch (ResourceManagerException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            finally
            {
                try
                {
                    synchronized (restart)
                    {
                        restart.meet();
                        restart.reset();
                    }
                }
                catch (InterruptedException ie) {}

            }

            // XXX in special scenarios the current implementation might cause both
            // owners to be deadlock victims
            if (deadlockCnt != 1)
            {
                sLogger.logWarning("More than one thread was deadlock victim!");
            }
            assertTrue(deadlockCnt >= 1);
            deadlockCnt = 0;
        }
    }

    public void testCopyRec() throws Throwable
    {
        sLogger.logInfo("Checking file copy");
        reset();
        createInitialFiles();
        FileHelper.copyRec(new File(INITIAL_FILES[0]), new File(STORE + "/olli/NewFile"));
    }

}
