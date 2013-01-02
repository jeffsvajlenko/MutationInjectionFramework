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
package org.apache.commons.betwixt.io;

import java.beans.IntrospectionException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.apache.commons.betwixt.XMLUtils;
import org.apache.commons.betwixt.strategy.MixedContentEncodingStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/** <p><code>BeanWriter</code> outputs beans as XML to an io stream.</p>
  *
  * <p>The output for each bean is an xml fragment
  * (rather than a well-formed xml-document).
  * This allows bean representations to be appended to a document
  * by writing each in turn to the stream.
  * So to create a well formed xml document,
  * you'll need to write the prolog to the stream first.
  * If you append more than one bean to the stream,
  * then you'll need to add a wrapping root element as well.
  *
  * <p> The line ending to be used is set by {@link #setEndOfLine}.
  *
  * <p> The output can be formatted (with whitespace) for easy reading
  * by calling {@link #enablePrettyPrint}.
  * The output will be indented.
  * The indent string used is set by {@link #setIndent}.
  *
  * <p> Bean graphs can sometimes contain cycles.
  * Care must be taken when serializing cyclic bean graphs
  * since this can lead to infinite recursion.
  * The approach taken by <code>BeanWriter</code> is to automatically
  * assign an <code>ID</code> attribute value to beans.
  * When a cycle is encountered,
  * an element is written that has the <code>IDREF</code> attribute set to the
  * id assigned earlier.
  *
  * <p> The names of the <code>ID</code> and <code>IDREF</code> attributes used
  * can be customized by the <code>XMLBeanInfo</code>.
  * The id's used can also be customized by the user
  * via <code>IDGenerator</code> subclasses.
  * The implementation used can be set by the <code>IdGenerator</code> property.
  * BeanWriter defaults to using <code>SequentialIDGenerator</code>
  * which supplies id values in numeric sequence.
  *
  * <p>If generated <code>ID</code> attribute values are not acceptable in the output,
  * then this can be disabled by setting the <code>WriteIDs</code> property to false.
  * If a cyclic reference is encountered in this case then a
  * <code>CyclicReferenceException</code> will be thrown.
  * When the <code>WriteIDs</code> property is set to false,
  * it is recommended that this exception is caught by the caller.
  *
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @author <a href="mailto:martin@mvdb.net">Martin van den Bemt</a>
  */
public class BeanWriter extends AbstractBeanWriter
{

    /**
     * Gets the default EOL string.
     * @return EOL string, not null
     */
    private static final String getEOL()
    {
        // just wraps call in an exception check for access restricted environments
        String result = "\n";
        try
        {
            result = System.getProperty( "line.separator", "\n" );
        }
        catch (SecurityException se)
        {
            Log log = LogFactory.getLog( BeanWriter.class );
            log.warn("Cannot load line separator property: " + se.getMessage());
            log.trace("Caused by: ", se);
        }
        return result;
    }


    /** Where the output goes */
    private Writer writer;
    /** text used for end of lines. Defaults to <code>\n</code>*/
    private static final String EOL = getEOL();
    /** text used for end of lines. Defaults to <code>\n</code>*/
    private String endOfLine = EOL;
    /** Initial level of indentation (starts at 1 with the first element by default) */
    private int initialIndentLevel = 1;
    /** indentation text */
    private String indent;

    /** should we flush after writing bean */
    private boolean autoFlush;
    /** Log used for logging (Doh!) */
    private Log log = LogFactory.getLog( BeanWriter.class );
    /** Has any content (excluding attributes) been written to the current element */
    private boolean currentElementIsEmpty = false;
    /** Has the current element written any body text */
    private boolean currentElementHasBodyText = false;
    /** Has the last start tag been closed */
    private boolean closedStartTag = true;
    /** Should an end tag be added for empty elements? */
    private boolean addEndTagForEmptyElement = false;
    /** Current level of indentation */
    private int indentLevel;
    /** USed to determine how body content should be encoded before being output*/
    private MixedContentEncodingStrategy mixedContentEncodingStrategy
        = MixedContentEncodingStrategy.DEFAULT;

    /**
     * <p> Constructor uses <code>System.out</code> for output.</p>
     */
    public BeanWriter()
    {
        this( System.out );
    }

    /**
     * <p> Constuctor uses given <code>OutputStream</code> for output.</p>
     *
     * @param out write out representations to this stream
     */
    public BeanWriter(OutputStream out)
    {
        this.writer = new BufferedWriter( new OutputStreamWriter( out ) );
        this.autoFlush = true;
    }

    /**
     * <p>Constuctor uses given <code>OutputStream</code> for output
     * and allows encoding to be set.</p>
     *
     * @param out write out representations to this stream
     * @param enc the name of the encoding to be used. This should be compatible
     * with the encoding types described in <code>java.io</code>
     * @throws UnsupportedEncodingException if the given encoding is not supported
     */
    public BeanWriter(OutputStream out, String enc) throws UnsupportedEncodingException
    {
        this.writer = new BufferedWriter( new OutputStreamWriter( out, enc ) );
        this.autoFlush = true;
    }

    /**
     * <p> Constructor sets writer used for output.</p>
     *
     * @param writer write out representations to this writer
     */
    public BeanWriter(Writer writer)
    {
        this.writer = writer;
    }

    /**
     * A helper method that allows you to write the XML Declaration.
     * This should only be called once before you output any beans.
     *
     * @param xmlDeclaration is the XML declaration string typically of
     *  the form "&lt;xml version='1.0' encoding='UTF-8' ?&gt;
     *
     * @throws IOException when declaration cannot be written
     */
    public void writeXmlDeclaration(String xmlDeclaration) throws IOException
    {
        writer.write( xmlDeclaration );
        printLine();
    }

    /**
     * Allows output to be flushed on the underlying output stream
     *
     * @throws IOException when the flush cannot be completed
     */
    public void flush() throws IOException
    {
        writer.flush();
    }

    /**
     * Closes the underlying output stream
     *
     * @throws IOException when writer cannot be closed
     */
    public void close() throws IOException
    {
        writer.close();
    }

    /**
     * Write the given object to the stream (and then flush).
     *
     * @param bean write this <code>Object</code> to the stream
     * @throws IOException if an IO problem causes failure
     * @throws SAXException if a SAX problem causes failure
     * @throws IntrospectionException if bean cannot be introspected
     */
    public void write(Object bean) throws IOException, SAXException, IntrospectionException
    {

        super.write(bean);

        if ( autoFlush )
        {
            writer.flush();
        }
    }


    /**
     * <p> Switch on formatted output.
     * This sets the end of line and the indent.
     * The default is adding 2 spaces and a newline
     */
    public void enablePrettyPrint()
    {
        endOfLine = EOL;
        indent = "  ";
    }

    /**
     * Gets the string used to mark end of lines.
     *
     * @return the string used for end of lines
     */
    public String getEndOfLine()
    {
        return endOfLine;
    }

    /**
     * Sets the string used for end of lines
     * Produces a warning the specified value contains an invalid whitespace character
     *
     * @param endOfLine the <code>String</code to use
     */
    public void setEndOfLine(String endOfLine)
    {
        this.endOfLine = endOfLine;
        for (int i = 0; i < endOfLine.length(); i++)
        {
            if (!Character.isWhitespace(endOfLine.charAt(i)))
            {
                log.warn("Invalid EndOfLine character(s)");
                break;
            }
        }

    }

    /**
     * Gets the initial indent level
     *
     * @return the initial level for indentation
     * @since 0.8
     */
    public int getInitialIndentLevel()
    {
        return initialIndentLevel;
    }

    /**
     * Sets the initial indent level used for pretty print indents
     * @param initialIndentLevel use this <code>int</code> to start with
     * @since 0.8
     */
    public void setInitialIndentLevel(int initialIndentLevel)
    {
        this.initialIndentLevel = initialIndentLevel;
    }


    /**
     * Gets the indent string
     *
     * @return the string used for indentation
     */
    public String getIndent()
    {
        return indent;
    }

    /**
     * Sets the string used for pretty print indents
     * @param indent use this <code>string</code> for indents
     */
    public void setIndent(String indent)
    {
        this.indent = indent;
    }

    /**
     * <p> Set the log implementation used. </p>
     *
     * @return a <code>org.apache.commons.logging.Log</code> level constant
     */
    public Log getLog()
    {
        return log;
    }

    /**
     * <p> Set the log implementation used. </p>
     *
     * @param log <code>Log</code> implementation to use
     */
    public void setLog( Log log )
    {
        this.log = log;
    }

    /**
     * Gets the encoding strategy for mixed content.
     * This is used to process body content
     * before it is written to the textual output.
     * @return the <code>MixedContentEncodingStrategy</code>, not null
     * @since 0.5
     */
    public MixedContentEncodingStrategy getMixedContentEncodingStrategy()
    {
        return mixedContentEncodingStrategy;
    }

    /**
     * Sets the encoding strategy for mixed content.
     * This is used to process body content
     * before it is written to the textual output.
     * @param strategy the <code>MixedContentEncodingStrategy</code>
     * used to process body content, not null
     * @since 0.5
     */
    public void setMixedContentEncodingStrategy(MixedContentEncodingStrategy strategy)
    {
        mixedContentEncodingStrategy = strategy;
    }

    /**
     * <p>Should an end tag be added for each empty element?
     * </p><p>
     * When this property is false then empty elements will
     * be written as <code>&lt;<em>element-name</em>/gt;</code>.
     * When this property is true then empty elements will
     * be written as <code>&lt;<em>element-name</em>gt;
     * &lt;/<em>element-name</em>gt;</code>.
     * </p>
     * @return true if an end tag should be added
     */
    public boolean isEndTagForEmptyElement()
    {
        return addEndTagForEmptyElement;
    }

    /**
     * Sets when an an end tag be added for each empty element.
     * When this property is false then empty elements will
     * be written as <code>&lt;<em>element-name</em>/gt;</code>.
     * When this property is true then empty elements will
     * be written as <code>&lt;<em>element-name</em>gt;
     * &lt;/<em>element-name</em>gt;</code>.
     * @param addEndTagForEmptyElement true if an end tag should be
     * written for each empty element, false otherwise
     */
    public void setEndTagForEmptyElement(boolean addEndTagForEmptyElement)
    {
        this.addEndTagForEmptyElement = addEndTagForEmptyElement;
    }



    // New API
    //------------------------------------------------------------------------------


    /**
     * Writes the start tag for an element.
     *
     * @param uri the element's namespace uri
     * @param localName the element's local name
     * @param qualifiedName the element's qualified name
     * @param attr the element's attributes
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @since 0.5
     */
    protected void startElement(
        WriteContext context,
        String uri,
        String localName,
        String qualifiedName,
        Attributes attr)
    throws
        IOException,
        SAXException
    {
        if ( !closedStartTag )
        {
            writer.write( '>' );
            printLine();
        }

        indentLevel++;

        indent();
        writer.write( '<' );
        writer.write( qualifiedName );

        for ( int i=0; i< attr.getLength(); i++ )
        {
            writer.write( ' ' );
            writer.write( attr.getQName(i) );
            writer.write( "=\"" );
            writer.write( XMLUtils.escapeAttributeValue( attr.getValue(i) ) );
            writer.write( '\"' );
        }
        closedStartTag = false;
        currentElementIsEmpty = true;
        currentElementHasBodyText = false;
    }

    /**
     * Writes the end tag for an element
     *
     * @param uri the element's namespace uri
     * @param localName the element's local name
     * @param qualifiedName the element's qualified name
     *
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @since 0.5
     */
    protected void endElement(
        WriteContext context,
        String uri,
        String localName,
        String qualifiedName)
    throws
        IOException,
        SAXException
    {
        if (
            !addEndTagForEmptyElement
            && !closedStartTag
            && currentElementIsEmpty )
        {

            writer.write( "/>" );
            closedStartTag = true;

        }
        else
        {

            if (
                addEndTagForEmptyElement
                && !closedStartTag )
            {
                writer.write( ">" );
                closedStartTag = true;
            }
            else if (!currentElementHasBodyText)
            {
                indent();
            }
            writer.write( "</" );
            writer.write( qualifiedName );
            writer.write( '>' );

        }

        indentLevel--;
        printLine();

        currentElementHasBodyText = false;
    }

    /**
     * Write element body text
     *
     * @param text write out this body text
     * @throws IOException when the stream write fails
     * @since 0.5
     */
    protected void bodyText(WriteContext context, String text) throws IOException
    {
        if ( text == null )
        {
            // XXX This is probably a programming error
            log.error( "[expressBodyText]Body text is null" );

        }
        else
        {
            if ( !closedStartTag )
            {
                writer.write( '>' );
                closedStartTag = true;
            }
            writer.write(
                mixedContentEncodingStrategy.encode(
                    text,
                    context.getCurrentDescriptor()) );
            currentElementIsEmpty = false;
            currentElementHasBodyText = true;
        }
    }

    /** Writes out an empty line.
     * Uses current <code>endOfLine</code>.
     *
     * @throws IOException when stream write fails
     */
    private void printLine() throws IOException
    {
        if ( endOfLine != null )
        {
            writer.write( endOfLine );
        }
    }

    /**
     * Writes out <code>indent</code>'s to the current <code>indentLevel</code>
     *
     * @throws IOException when stream write fails
     */
    private void indent() throws IOException
    {
        if ( indent != null )
        {
            for ( int i = 1 - initialIndentLevel; i < indentLevel; i++ )
            {
                writer.write( getIndent() );
            }
        }
    }

    // OLD API (DEPRECATED)
    //----------------------------------------------------------------------------


    /** Writes out an empty line.
     * Uses current <code>endOfLine</code>.
     *
     * @throws IOException when stream write fails
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void writePrintln() throws IOException
    {
        if ( endOfLine != null )
        {
            writer.write( endOfLine );
        }
    }

    /**
     * Writes out <code>indent</code>'s to the current <code>indentLevel</code>
     *
     * @throws IOException when stream write fails
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void writeIndent() throws IOException
    {
        if ( indent != null )
        {
            for ( int i = 0; i < indentLevel; i++ )
            {
                writer.write( getIndent() );
            }
        }
    }

    /**
     * <p>Escape the <code>toString</code> of the given object.
     * For use as body text.</p>
     *
     * @param value escape <code>value.toString()</code>
     * @return text with escaped delimiters
     * @deprecated 0.5 moved into utility class {@link XMLUtils#escapeBodyValue}
     */
    protected String escapeBodyValue(Object value)
    {
        return XMLUtils.escapeBodyValue(value);
    }

    /**
     * <p>Escape the <code>toString</code> of the given object.
     * For use in an attribute value.</p>
     *
     * @param value escape <code>value.toString()</code>
     * @return text with characters restricted (for use in attributes) escaped
     *
     * @deprecated 0.5 moved into utility class {@link XMLUtils#escapeAttributeValue}
     */
    protected String escapeAttributeValue(Object value)
    {
        return XMLUtils.escapeAttributeValue(value);
    }

    /**
     * Express an element tag start using given qualified name
     *
     * @param qualifiedName the fully qualified name of the element to write
     * @throws IOException when stream write fails
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void expressElementStart(String qualifiedName) throws IOException
    {
        if ( qualifiedName == null )
        {
            // XXX this indicates a programming error
            log.fatal( "[expressElementStart]Qualified name is null." );
            throw new RuntimeException( "Qualified name is null." );
        }

        writePrintln();
        writeIndent();
        writer.write( '<' );
        writer.write( qualifiedName );
    }

    /**
     * Write a tag close to the stream
     *
     * @throws IOException when stream write fails
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void expressTagClose() throws IOException
    {
        writer.write( '>' );
    }

    /**
     * Write an element end tag to the stream
     *
     * @param qualifiedName the name of the element
     * @throws IOException when stream write fails
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void expressElementEnd(String qualifiedName) throws IOException
    {
        if (qualifiedName == null)
        {
            // XXX this indicates a programming error
            log.fatal( "[expressElementEnd]Qualified name is null." );
            throw new RuntimeException( "Qualified name is null." );
        }

        writer.write( "</" );
        writer.write( qualifiedName );
        writer.write( '>' );
    }

    /**
     * Write an empty element end to the stream
     *
     * @throws IOException when stream write fails
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void expressElementEnd() throws IOException
    {
        writer.write( "/>" );
    }

    /**
     * Write element body text
     *
     * @param text write out this body text
     * @throws IOException when the stream write fails
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void expressBodyText(String text) throws IOException
    {
        if ( text == null )
        {
            // XXX This is probably a programming error
            log.error( "[expressBodyText]Body text is null" );

        }
        else
        {
            writer.write( XMLUtils.escapeBodyValue(text) );
        }
    }

    /**
     * Writes an attribute to the stream.
     *
     * @param qualifiedName fully qualified attribute name
     * @param value attribute value
     * @throws IOException when the stream write fails
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void expressAttribute(
        String qualifiedName,
        String value)
    throws
        IOException
    {
        if ( value == null )
        {
            // XXX probably a programming error
            log.error( "Null attribute value." );
            return;
        }

        if ( qualifiedName == null )
        {
            // XXX probably a programming error
            log.error( "Null attribute value." );
            return;
        }

        writer.write( ' ' );
        writer.write( qualifiedName );
        writer.write( "=\"" );
        writer.write( XMLUtils.escapeAttributeValue(value) );
        writer.write( '\"' );
    }


}
