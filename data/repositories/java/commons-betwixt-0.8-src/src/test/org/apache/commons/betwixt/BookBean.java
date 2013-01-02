/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.betwixt;

/**
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class BookBean
{

    private String author;
    private String title;
    private String publisher;

    public BookBean() {}

    public BookBean(String author, String title, String publisher)
    {
        setAuthor(author);
        setTitle(title);
        setPublisher(publisher);
    }

    public String getAuthor()
    {
        return author;
    }

    public String getPublisher()
    {
        return publisher;
    }

    public String getTitle()
    {
        return title;
    }


    public void setAuthor(String string)
    {
        author = string;
    }

    public void setPublisher(String string)
    {
        publisher = string;
    }

    public void setTitle(String string)
    {
        title = string;
    }

    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj instanceof BookBean)
        {
            BookBean book = (BookBean) obj;
            result = author.equals(book.author) &&
                     publisher.equals(book.publisher) &&
                     title.equals(book.title);
        }
        return result;
    }

    public String toString()
    {
        return "[BookBean title=" + title + "]";
    }

}
