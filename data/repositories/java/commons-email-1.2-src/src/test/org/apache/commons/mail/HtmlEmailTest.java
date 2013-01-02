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
package org.apache.commons.mail;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.activation.FileDataSource;

import org.apache.commons.mail.mocks.MockHtmlEmailConcrete;
import org.apache.commons.mail.settings.EmailConfiguration;

/**
 * JUnit test case for HtmlEmail Class
 *
 * @since 1.0
 * @author <a href="mailto:corey.scott@gmail.com">Corey Scott</a>
 * @version $Id: HtmlEmailTest.java 785799 2009-06-17 21:04:08Z sgoeschl $
 */

public class HtmlEmailTest extends BaseEmailTestCase
{
    /** */
    private MockHtmlEmailConcrete email;

    /**
     * @param name name
     */
    public HtmlEmailTest(String name)
    {
        super(name);
    }

    /**
     * @throws Exception  */
    protected void setUp() throws Exception
    {
        super.setUp();
        // reusable objects to be used across multiple tests
        this.email = new MockHtmlEmailConcrete();
    }

    /**
     * @throws EmailException  */
    public void testGetSetTextMsg() throws EmailException
    {
        // ====================================================================
        // Test Success
        // ====================================================================
        for (int i = 0; i < testCharsValid.length; i++)
        {
            this.email.setTextMsg(testCharsValid[i]);
            assertEquals(testCharsValid[i], this.email.getTextMsg());
        }

        // ====================================================================
        // Test Exception
        // ====================================================================
        for (int i = 0; i < this.testCharsNotValid.length; i++)
        {
            try
            {
                this.email.setTextMsg(this.testCharsNotValid[i]);
                fail("Should have thrown an exception");
            }
            catch (EmailException e)
            {
                assertTrue(true);
            }
        }

    }

    /**
     * @throws EmailException if setting the message fails
     */
    public void testGetSetHtmlMsg() throws EmailException
    {
        // ====================================================================
        // Test Success
        // ====================================================================
        for (int i = 0; i < testCharsValid.length; i++)
        {
            this.email.setHtmlMsg(testCharsValid[i]);
            assertEquals(testCharsValid[i], this.email.getHtmlMsg());
        }

        // ====================================================================
        // Test Exception
        // ====================================================================
        for (int i = 0; i < this.testCharsNotValid.length; i++)
        {
            try
            {
                this.email.setHtmlMsg(this.testCharsNotValid[i]);
                fail("Should have thrown an exception");
            }
            catch (EmailException e)
            {
                assertTrue(true);
            }
        }

    }

    /**
     * @throws EmailException  */
    public void testGetSetMsg() throws EmailException
    {
        // ====================================================================
        // Test Success
        // ====================================================================
        for (int i = 0; i < testCharsValid.length; i++)
        {
            this.email.setMsg(testCharsValid[i]);
            assertEquals(testCharsValid[i], this.email.getTextMsg());

            assertTrue(
                this.email.getHtmlMsg().indexOf(testCharsValid[i]) != -1);
        }

        // ====================================================================
        // Test Exception
        // ====================================================================
        for (int i = 0; i < this.testCharsNotValid.length; i++)
        {
            try
            {
                this.email.setMsg(this.testCharsNotValid[i]);
                fail("Should have thrown an exception");
            }
            catch (EmailException e)
            {
                assertTrue(true);
            }
        }

    }

    /**
     *
     * @throws Exception Exception
     */
    public void testEmbedUrl() throws Exception
    {
        // ====================================================================
        // Test Success
        // ====================================================================

        String strEmbed =
            this.email.embed(new URL(this.strTestURL), "Test name");
        assertNotNull(strEmbed);
        assertEquals(HtmlEmail.CID_LENGTH, strEmbed.length());

        // if we embed the same name again, do we get the same content ID
        // back?
        String testCid =
            this.email.embed(new URL(this.strTestURL), "Test name");
        assertEquals(strEmbed, testCid);

        // if we embed the same URL under a different name, is the content ID
        // unique?
        String newCid =
            this.email.embed(new URL(this.strTestURL), "Test name 2");
        assertFalse(strEmbed.equals(newCid));

        // ====================================================================
        // Test Exceptions
        // ====================================================================

        // Does an invalid URL throw an exception?
        try
        {
            this.email.embed(new URL("http://example.invalid"), "Bad URL");
            fail("Should have thrown an exception");
        }
        catch (EmailException e)
        {
            // expected
        }

        // if we try to embed a different URL under a previously used name,
        // does it complain?
        try
        {
            this.email.embed(new URL("http://www.google.com"), "Test name");
            fail("shouldn't be able to use an existing name with a different URL!");
        }
        catch (EmailException e)
        {
            // expected
        }
    }

    public void testEmbedFile() throws Exception
    {
        // ====================================================================
        // Test Success
        // ====================================================================

        File file = File.createTempFile("testEmbedFile", "txt");
        file.deleteOnExit();
        String strEmbed = this.email.embed(file);
        assertNotNull(strEmbed);
        assertEquals("generated CID has wrong length",
                     HtmlEmail.CID_LENGTH, strEmbed.length());

        // if we embed the same file again, do we get the same content ID
        // back?
        String testCid =
            this.email.embed(file);
        assertEquals("didn't get same CID after embedding same file twice",
                     strEmbed, testCid);

        // if we embed a new file, is the content ID unique?
        File otherFile = File.createTempFile("testEmbedFile2", "txt");
        otherFile.deleteOnExit();
        String newCid = this.email.embed(otherFile);
        assertFalse("didn't get unique CID from embedding new file",
                    strEmbed.equals(newCid));
    }

    public void testEmbedUrlAndFile() throws Exception
    {
        File tmpFile = File.createTempFile("testfile", "txt");
        tmpFile.deleteOnExit();
        String fileCid = this.email.embed(tmpFile);

        URL fileUrl = tmpFile.toURL();
        String urlCid = this.email.embed(fileUrl, "urlName");

        assertFalse("file and URL cids should be different even for same resource",
                    fileCid.equals(urlCid));
    }

    public void testEmbedDataSource() throws Exception
    {
        File tmpFile = File.createTempFile("testEmbedDataSource", "txt");
        tmpFile.deleteOnExit();
        FileDataSource dataSource = new FileDataSource(tmpFile);

        // does embedding a datasource without a name fail?
        try
        {
            this.email.embed(dataSource, "");
            fail("embedding with an empty string for a name should fail");
        }
        catch (EmailException e)
        {
            // expected
        }

        // properly embed the datasource
        String cid = this.email.embed(dataSource, "testname");

        // does embedding the same datasource under the same name return
        // the original cid?
        String sameCid = this.email.embed(dataSource, "testname");
        assertEquals("didn't get same CID for embedding same datasource twice",
                     cid, sameCid);

        // does embedding another datasource under the same name fail?
        File anotherFile = File.createTempFile("testEmbedDataSource2", "txt");
        anotherFile.deleteOnExit();
        FileDataSource anotherDS = new FileDataSource(anotherFile);
        try
        {
            this.email.embed(anotherDS, "testname");
        }
        catch (EmailException e)
        {
            // expected
        }
    }

    /**
     * @throws EmailException when bad addresses and attachments are used
     * @throws IOException if creating a temp file, URL or sending fails
     */
    public void testSend() throws EmailException, IOException
    {
        EmailAttachment attachment = new EmailAttachment();
        File testFile = null;

        /** File to used to test file attachments (Must be valid) */
        testFile = File.createTempFile("commons-email-testfile", ".txt");
        testFile.deleteOnExit();

        // ====================================================================
        // Test Success
        // ====================================================================
        this.getMailServer();

        String strSubject = "Test HTML Send #1 Subject (w charset)";

        this.email = new MockHtmlEmailConcrete();
        this.email.setHostName(this.strTestMailServer);
        this.email.setSmtpPort(this.getMailServerPort());
        this.email.setFrom(this.strTestMailFrom);
        this.email.addTo(this.strTestMailTo);

        /** File to used to test file attachmetns (Must be valid) */
        attachment.setName("Test Attachment");
        attachment.setDescription("Test Attachment Desc");
        attachment.setPath(testFile.getAbsolutePath());
        this.email.attach(attachment);

        this.email.setAuthentication(this.strTestUser, this.strTestPasswd);

        this.email.setCharset(Email.ISO_8859_1);
        this.email.setSubject(strSubject);

        URL url = new URL(EmailConfiguration.TEST_URL);
        String cid = this.email.embed(url, "Apache Logo");

        String strHtmlMsg =
            "<html>The Apache logo - <img src=\"cid:" + cid + "\"><html>";

        this.email.setHtmlMsg(strHtmlMsg);
        this.email.setTextMsg(
            "Your email client does not support HTML emails");

        this.email.send();
        this.fakeMailServer.stop();
        // validate txt message
        validateSend(
            this.fakeMailServer,
            strSubject,
            this.email.getTextMsg(),
            this.email.getFromAddress(),
            this.email.getToAddresses(),
            this.email.getCcAddresses(),
            this.email.getBccAddresses(),
            true);

        // validate html message
        validateSend(
            this.fakeMailServer,
            strSubject,
            this.email.getHtmlMsg(),
            this.email.getFromAddress(),
            this.email.getToAddresses(),
            this.email.getCcAddresses(),
            this.email.getBccAddresses(),
            false);

        // validate attachment
        validateSend(
            this.fakeMailServer,
            strSubject,
            attachment.getName(),
            this.email.getFromAddress(),
            this.email.getToAddresses(),
            this.email.getCcAddresses(),
            this.email.getBccAddresses(),
            false);

        this.getMailServer();

        this.email = new MockHtmlEmailConcrete();
        this.email.setHostName(this.strTestMailServer);
        this.email.setSmtpPort(this.getMailServerPort());
        this.email.setFrom(this.strTestMailFrom);
        this.email.addTo(this.strTestMailTo);

        if (this.strTestUser != null && this.strTestPasswd != null)
        {
            this.email.setAuthentication(
                this.strTestUser,
                this.strTestPasswd);
        }

        strSubject = "Test HTML Send #1 Subject (wo charset)";
        this.email.setSubject(strSubject);
        this.email.setTextMsg("Test message");

        this.email.send();
        this.fakeMailServer.stop();
        // validate txt message
        validateSend(
            this.fakeMailServer,
            strSubject,
            this.email.getTextMsg(),
            this.email.getFromAddress(),
            this.email.getToAddresses(),
            this.email.getCcAddresses(),
            this.email.getBccAddresses(),
            true);
    }

    /**
     *
     * @throws Exception Exception
     */
    public void testSend2() throws Exception
    {
        // ====================================================================
        // Test Success
        // ====================================================================

        this.getMailServer();

        this.email = new MockHtmlEmailConcrete();
        this.email.setHostName(this.strTestMailServer);
        this.email.setSmtpPort(this.getMailServerPort());
        this.email.setFrom(this.strTestMailFrom);
        this.email.addTo(this.strTestMailTo);

        if (this.strTestUser != null && this.strTestPasswd != null)
        {
            this.email.setAuthentication(
                this.strTestUser,
                this.strTestPasswd);
        }

        String strSubject = "Test HTML Send #2 Subject (wo charset)";
        this.email.setSubject(strSubject);
        this.email.setMsg("Test txt msg");

        this.email.send();
        this.fakeMailServer.stop();
        // validate txt message
        validateSend(
            this.fakeMailServer,
            strSubject,
            this.email.getTextMsg(),
            this.email.getFromAddress(),
            this.email.getToAddresses(),
            this.email.getCcAddresses(),
            this.email.getBccAddresses(),
            true);

        // validate html message
        validateSend(
            this.fakeMailServer,
            strSubject,
            this.email.getHtmlMsg(),
            this.email.getFromAddress(),
            this.email.getToAddresses(),
            this.email.getCcAddresses(),
            this.email.getBccAddresses(),
            false);

        this.getMailServer();

        this.email = new MockHtmlEmailConcrete();
        this.email.setHostName(this.strTestMailServer);
        this.email.setFrom(this.strTestMailFrom);
        this.email.setSmtpPort(this.getMailServerPort());
        this.email.addTo(this.strTestMailTo);

        if (this.strTestUser != null && this.strTestPasswd != null)
        {
            this.email.setAuthentication(
                this.strTestUser,
                this.strTestPasswd);
        }

        strSubject = "Test HTML Send #2 Subject (w charset)";
        this.email.setCharset(Email.ISO_8859_1);
        this.email.setSubject(strSubject);
        this.email.setMsg("Test txt msg");

        this.email.send();
        this.fakeMailServer.stop();
        // validate txt message
        validateSend(
            this.fakeMailServer,
            strSubject,
            this.email.getTextMsg(),
            this.email.getFromAddress(),
            this.email.getToAddresses(),
            this.email.getCcAddresses(),
            this.email.getBccAddresses(),
            true);

        // validate html message
        validateSend(
            this.fakeMailServer,
            strSubject,
            this.email.getHtmlMsg(),
            this.email.getFromAddress(),
            this.email.getToAddresses(),
            this.email.getCcAddresses(),
            this.email.getBccAddresses(),
            false);

    }

}
