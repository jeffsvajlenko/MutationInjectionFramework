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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.activation.DataHandler;
import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import junit.framework.TestCase;

import org.apache.commons.mail.settings.EmailConfiguration;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;



/**
 * Base test case for Email test classes
 *
 * @since 1.0
 * @author <a href="mailto:corey.scott@gmail.com">Corey Scott</a>
 * @author <a href="mailto:epugh@opensourceconnections.com">Eric Pugh</a>
 * @version $Id: BaseEmailTestCase.java 785799 2009-06-17 21:04:08Z sgoeschl $
 */

public abstract class BaseEmailTestCase extends TestCase
{
    /** Padding at end of body added by wiser/send */
    public static final int BODY_END_PAD = 3;
    /** Padding at start of body added by wiser/send */
    public static final int BODY_START_PAD = 2;

    /** Line separator in email messages */
    private static final String LINE_SEPARATOR = "\r\n";

    /** default port */
    private static int mailServerPort = EmailConfiguration.MAIL_SERVER_PORT;

    /** The fake Wiser email server */
    protected Wiser fakeMailServer;

    /** Mail server used for testing */
    protected String strTestMailServer = EmailConfiguration.MAIL_SERVER;
    /** From address for the test email */
    protected String strTestMailFrom = EmailConfiguration.TEST_FROM;
    /** Destination address for the test email */
    protected String strTestMailTo = EmailConfiguration.TEST_TO;
    /** Mailserver username (set if needed) */
    protected String strTestUser = EmailConfiguration.TEST_USER;
    /** Mailserver strTestPasswd (set if needed) */
    protected String strTestPasswd = EmailConfiguration.TEST_PASSWD;
    /** URL to used to test URL attachmetns (Must be valid) */
    protected String strTestURL = EmailConfiguration.TEST_URL;

    /** Test characters acceptable to email */
    protected String[] testCharsValid =
    {
        " ",
        "a",
        "A",
        "\uc5ec",
        "0123456789",
        "012345678901234567890",
        "\n"
    };

    /** Array of test strings */
    protected String[] testCharsNotValid = {"", null};

    /** Where to save email output **/
    private File emailOutputDir;

    /** counter for creating a file name */
    private static int fileNameCounter;

    /**
     * @param name name
     */
    public BaseEmailTestCase(String name)
    {
        super(name);
        emailOutputDir = new File("target/test-emails");
        if (!emailOutputDir.exists())
        {
            emailOutputDir.mkdirs();
        }
    }

    /** */
    protected void tearDown()
    {
        //stop the fake email server (if started)
        if (this.fakeMailServer != null && !isMailServerStopped(fakeMailServer))
        {
            this.fakeMailServer.stop();
            assertTrue("Mail server didn't stop", isMailServerStopped(fakeMailServer));
        }

        this.fakeMailServer = null;
    }

    /**
     * Gets the mail server port.
     * @return the port the server is running on.
     */
    protected int getMailServerPort()
    {
        return mailServerPort;
    }

    /**
     * Safe a mail to a file using a more or less unique file name.
     *
     * @param email email
     * @throws IOException writing the email failed
     * @throws MessagingException writing the email failed
     */
    protected void saveEmailToFile(WiserMessage email) throws IOException, MessagingException
    {
        int currCounter = fileNameCounter++ % 10;
        String emailFileName = "email" + new Date().getTime() + "-" + currCounter + ".eml";
        File emailFile = new File(emailOutputDir, emailFileName);
        EmailUtils.writeMimeMessage(emailFile, email.getMimeMessage() );
    }

    /**
     * @param intMsgNo the message to retrieve
     * @return message as string
     */
    public String getMessageAsString(int intMsgNo)
    {
        List receivedMessages = fakeMailServer.getMessages();
        assertTrue("mail server didn't get enough messages", receivedMessages.size() >= intMsgNo);

        WiserMessage emailMessage = (WiserMessage) receivedMessages.get(intMsgNo);

        if (emailMessage != null)
        {
            try
            {
                return serializeEmailMessage(emailMessage);
            }
            catch (Exception e)
            {
                // ignore, since the test will fail on an empty string return
            }
        }
        fail("Message not found");
        return "";
    }

    /**
     * Initializes the stub mail server. Fails if the server cannot be proven
     * to have started. If the server is already started, this method returns
     * without changing the state of the server.
     */
    public void getMailServer()
    {
        if (this.fakeMailServer == null || isMailServerStopped(fakeMailServer))
        {
            mailServerPort++;

            this.fakeMailServer = new Wiser();
            this.fakeMailServer.setPort(getMailServerPort());
            this.fakeMailServer.start();

            assertFalse("fake mail server didn't start", isMailServerStopped(fakeMailServer));

            Date dtStartWait = new Date();
            while (isMailServerStopped(fakeMailServer))
            {
                // test for connected
                if (this.fakeMailServer != null
                        && !isMailServerStopped(fakeMailServer))
                {
                    break;
                }

                // test for timeout
                if ((dtStartWait.getTime() + EmailConfiguration.TIME_OUT)
                        <= new Date().getTime())
                {
                    fail("Mail server failed to start");
                }
            }
        }
    }

    /**
     * Validate the message was sent properly
     * @param mailServer reference to the fake mail server
     * @param strSubject expected subject
     * @param fromAdd expected from address
     * @param toAdd list of expected to addresses
     * @param ccAdd list of expected cc addresses
     * @param bccAdd list of expected bcc addresses
     * @param boolSaveToFile true will output to file, false doesnt
     * @return WiserMessage email to check
     * @throws IOException Exception
     */
    protected WiserMessage validateSend(
        Wiser mailServer,
        String strSubject,
        InternetAddress fromAdd,
        List toAdd,
        List ccAdd,
        List bccAdd,
        boolean boolSaveToFile)
    throws IOException
    {
        assertTrue("mail server doesn't contain expected message",
                   mailServer.getMessages().size() == 1);
        WiserMessage emailMessage = (WiserMessage) mailServer.getMessages().get(0);

        if (boolSaveToFile)
        {
            try
            {
                this.saveEmailToFile(emailMessage);
            }
            catch(MessagingException me)
            {
                IllegalStateException ise =
                    new IllegalStateException("caught MessagingException during saving the email");
                ise.initCause(me);
                throw ise;
            }
        }

        try
        {
            // get the MimeMessage
            MimeMessage mimeMessage = emailMessage.getMimeMessage();

            // test subject
            assertEquals("got wrong subject from mail",
                         strSubject, mimeMessage.getHeader("Subject", null));

            //test from address
            assertEquals("got wrong From: address from mail",
                         fromAdd.toString(), mimeMessage.getHeader("From", null));

            //test to address
            assertTrue("got wrong To: address from mail",
                       toAdd.toString().indexOf(mimeMessage.getHeader("To", null)) != -1);

            //test cc address
            if (ccAdd.size() > 0)
            {
                assertTrue("got wrong Cc: address from mail",
                           ccAdd.toString().indexOf(mimeMessage.getHeader("Cc", null))
                           != -1);
            }

            //test bcc address
            if (bccAdd.size() > 0)
            {
                assertTrue("got wrong Bcc: address from mail",
                           bccAdd.toString().indexOf(mimeMessage.getHeader("Bcc", null))
                           != -1);
            }
        }
        catch (MessagingException me)
        {
            IllegalStateException ise =
                new IllegalStateException("caught MessagingException in validateSend()");
            ise.initCause(me);
            throw ise;
        }

        return emailMessage;
    }

    /**
     * Validate the message was sent properly
     * @param mailServer reference to the fake mail server
     * @param strSubject expected subject
     * @param content the expected message content
     * @param fromAdd expected from address
     * @param toAdd list of expected to addresses
     * @param ccAdd list of expected cc addresses
     * @param bccAdd list of expected bcc addresses
     * @param boolSaveToFile true will output to file, false doesnt
     * @throws IOException Exception
     */
    protected void validateSend(
        Wiser mailServer,
        String strSubject,
        Multipart content,
        InternetAddress fromAdd,
        List toAdd,
        List ccAdd,
        List bccAdd,
        boolean boolSaveToFile)
    throws IOException
    {
        // test other properties
        WiserMessage emailMessage = this.validateSend(
                                        mailServer,
                                        strSubject,
                                        fromAdd,
                                        toAdd,
                                        ccAdd,
                                        bccAdd,
                                        boolSaveToFile);

        // test message content

        // get sent email content
        String strSentContent =
            content.getContentType();
        // get received email content (chop off the auto-added \n
        // and -- (front and end)
        String emailMessageBody = getMessageBody(emailMessage);
        String strMessageBody =
            emailMessageBody.substring(BaseEmailTestCase.BODY_START_PAD,
                                       emailMessageBody.length()
                                       - BaseEmailTestCase.BODY_END_PAD);
        assertTrue("didn't find expected content type in message body",
                   strMessageBody.indexOf(strSentContent) != -1);
    }

    /**
     * Validate the message was sent properly
     * @param mailServer reference to the fake mail server
     * @param strSubject expected subject
     * @param strMessage the expected message as a string
     * @param fromAdd expected from address
     * @param toAdd list of expected to addresses
     * @param ccAdd list of expected cc addresses
     * @param bccAdd list of expected bcc addresses
     * @param boolSaveToFile true will output to file, false doesnt
     * @throws IOException Exception
     */
    protected void validateSend(
        Wiser mailServer,
        String strSubject,
        String strMessage,
        InternetAddress fromAdd,
        List toAdd,
        List ccAdd,
        List bccAdd,
        boolean boolSaveToFile)
    throws IOException
    {
        // test other properties
        WiserMessage emailMessage = this.validateSend(
                                        mailServer,
                                        strSubject,
                                        fromAdd,
                                        toAdd,
                                        ccAdd,
                                        bccAdd,
                                        true);

        // test message content
        assertTrue("didn't find expected message content in message body",
                   getMessageBody(emailMessage).indexOf(strMessage) != -1);
    }

    /**
     * Serializes the {@link MimeMessage} from the <code>WiserMessage</code>
     * passed in. The headers are serialized first followed by the message
     * body.
     *
     * @param wiserMessage The <code>WiserMessage</code> to serialize.
     * @return The string format of the message.
     * @throws MessagingException
     * @throws IOException
     *             Thrown while serializing the body from
     *             {@link DataHandler#writeTo(java.io.OutputStream)}.
     * @throws MessagingException
     *             Thrown while getting the body content from
     *             {@link MimeMessage#getDataHandler()}
     * @since 1.1
     */
    private String serializeEmailMessage(WiserMessage wiserMessage)
    throws MessagingException, IOException
    {
        if (wiserMessage == null)
        {
            return "";
        }

        StringBuffer serializedEmail = new StringBuffer();
        MimeMessage message = wiserMessage.getMimeMessage();

        // Serialize the headers
        for (Enumeration headers = message.getAllHeaders(); headers
                .hasMoreElements();)
        {
            Header header = (Header) headers.nextElement();
            serializedEmail.append(header.getName());
            serializedEmail.append(": ");
            serializedEmail.append(header.getValue());
            serializedEmail.append(LINE_SEPARATOR);
        }

        // Serialize the body
        byte[] messageBody = getMessageBodyBytes(message);

        serializedEmail.append(LINE_SEPARATOR);
        serializedEmail.append(messageBody);
        serializedEmail.append(LINE_SEPARATOR);

        return serializedEmail.toString();
    }

    /**
     * Returns a string representation of the message body. If the message body
     * cannot be read, an empty string is returned.
     *
     * @param wiserMessage The wiser message from which to extract the message body
     * @return The string representation of the message body
     * @throws IOException
     *             Thrown while serializing the body from
     *             {@link DataHandler#writeTo(java.io.OutputStream)}.
     * @since 1.1
     */
    private String getMessageBody(WiserMessage wiserMessage)
    throws IOException
    {
        if (wiserMessage == null)
        {
            return "";
        }

        byte[] messageBody = null;

        try
        {
            MimeMessage message = wiserMessage.getMimeMessage();
            messageBody = getMessageBodyBytes(message);
        }
        catch (MessagingException me)
        {
            // Thrown while getting the body content from
            // {@link MimeMessage#getDataHandler()}
            IllegalStateException ise =
                new IllegalStateException("couldn't process MimeMessage from WiserMessage in getMessageBody()");
            ise.initCause(me);
            throw ise;
        }

        return (messageBody != null) ? (new String(messageBody).intern()) : "";
    }

    /**
     * Gets the byte making up the body of the message.
     *
     * @param mimeMessage
     *            The mime message from which to extract the body.
     * @return A byte array representing the message body
     * @throws IOException
     *             Thrown while serializing the body from
     *             {@link DataHandler#writeTo(java.io.OutputStream)}.
     * @throws MessagingException
     *             Thrown while getting the body content from
     *             {@link MimeMessage#getDataHandler()}
     * @since 1.1
     */
    private byte[] getMessageBodyBytes(MimeMessage mimeMessage)
    throws IOException, MessagingException
    {
        DataHandler dataHandler = mimeMessage.getDataHandler();
        ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();
        BufferedOutputStream buffOs = new BufferedOutputStream(
            byteArrayOutStream);
        dataHandler.writeTo(buffOs);
        buffOs.flush();

        return byteArrayOutStream.toByteArray();
    }

    /**
     * Checks if an email server is running at the address stored in the
     * <code>fakeMailServer</code>.
     *
     * @param fakeMailServer
     *            The server from which the address is picked up.
     * @return <code>true</code> if the server claims to be running
     * @since 1.1
     */
    protected boolean isMailServerStopped(Wiser fakeMailServer)
    {
        return !fakeMailServer.getServer().isRunning();
    }
}
