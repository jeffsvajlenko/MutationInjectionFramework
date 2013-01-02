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
package org.apache.commons.mail.mocks;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.mail.HtmlEmail;

/**
 * Extension of the HtmlEmail Class
 * (used to allow testing only)
 *
 * @since 1.0
 * @author <a href="mailto:corey.scott@gmail.com">Corey Scott</a>
 * @version $Id: MockHtmlEmailConcrete.java 747572 2009-02-24 22:07:26Z sgoeschl $
 */
public class MockHtmlEmailConcrete extends HtmlEmail
{
    /**
     * Retrieve the message content
     * @return Message Content
     */
    public String getMsg()
    {
        try
        {
            return this.getPrimaryBodyPart().getContent().toString();
        }
        catch (IOException ioE)
        {
            return null;
        }
        catch (MessagingException msgE)
        {
            return null;
        }
    }

    /**
     * Retrieve the text msg
     * @return Message Content
     */
    public String getTextMsg()
    {
        return this.text;
    }

    /**
     * Retrieve the html msg
     * @return Message Content
     */
    public String getHtmlMsg()
    {
        return this.html;
    }

    /**
     * @deprecated as of commons-email 1.1, replaced by {@link #getInlineEmbeds}.
     */
    public List getInlineImages()
    {
        return inlineImages;
    }

    /**
     * @return inlineEmbeds
     */
    public Map getInlineEmbeds()
    {
        return inlineEmbeds;
    }

    /**
     * @return fromAddress
     */
    public InternetAddress getFromAddress()
    {
        return this.fromAddress;
    }

}
