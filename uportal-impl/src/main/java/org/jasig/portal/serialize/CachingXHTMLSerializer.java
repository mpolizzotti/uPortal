/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jasig.portal.serialize;


import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;


/**
 * Caching version of the XHTML serializer
 * @author Peter Kharchenko
 * @author <a href="mailto:arkin@exoffice.com">Assaf Arkin</a>
 * @see Serializer
 */
public final class CachingXHTMLSerializer
    extends XHTMLSerializer implements CachingSerializer
{

    CharacterCachingWriter cacher;

    /**
     * Constructs a new serializer. The serializer cannot be used without
     * calling {@link #setOutputCharStream} or {@link #setOutputByteStream}
     * first.
     */
    public CachingXHTMLSerializer()
    {
        super(new OutputFormat( Method.XHTML, null, false ) );
    }


    /**
     * Constructs a new serializer. The serializer cannot be used without
     * calling {@link #setOutputCharStream} or {@link #setOutputByteStream}
     * first.
     */
    public CachingXHTMLSerializer( OutputFormat format )
    {
        super(format);
    }


    /**
     * Constructs a new serializer that writes to the specified writer
     * using the specified output format. If <tt>format</tt> is null,
     * will use a default output format.
     *
     * @param writer The writer to use
     * @param format The output format to use, null for the default
     */
    public CachingXHTMLSerializer( Writer writer, OutputFormat format )
    {
       this(format);
       setOutputCharStream(writer);
    }

    @Override
    public void setOutputCharStream( Writer writer ) {
        CachingWriter cw=new CachingWriter(writer);
        this.cacher=cw;
        super.setOutputCharStream(cw);
    }

    /**
     * Constructs a new serializer that writes to the specified output
     * stream using the specified output format. If <tt>format</tt>
     * is null, will use a default output format.
     *
     * @param output The output stream to use
     * @param format The output format to use, null for the default
     */
    public CachingXHTMLSerializer( OutputStream output, OutputFormat format )
    {
        this(format);
        setOutputByteStream( output );
    }

    @Override
    public void setOutputByteStream( OutputStream output ) {
        CachingOutputStream cos=new CachingOutputStream(output);
        this.cacher=cos;
        super.setOutputByteStream(cos);
    }

    // caching methods
    
    /**
     * When starting caching if we are inside an opening tag the ">" will
     * be written in order for the ">" to be included with the correct cache.
     * 
     * Normally the serializer doesn't know if a ">" or "/>" should be written
     * until some content is received or the tag is closed. When starting
     * caching after an opening tag the tag will be assumed to have some content
     * and will write out the ">" before starting the cache.
     */    
    public boolean startCaching() throws IOException{
    	content();
        _printer.flush();
        return cacher.startCaching();
    }
    
    /**
     * When stopping caching if we are inside an opening tag the ">" will
     * be written in order for the ">" to be included with the correct cache.
     * 
     * Normally the serializer doesn't know if a ">" or "/>" should be written
     * until some content is received or the tag is closed. When starting
     * caching after an opening tag the tag will be assumed to have some content
     * and will write out the ">" before starting the cache.
     */    
    public boolean stopCaching() throws IOException {
    	content();
        _printer.flush();
        return cacher.stopCaching(); 
    }

    public String getCache() throws UnsupportedEncodingException, IOException {
        _printer.flush();
        return cacher.getCache(_format.getEncoding());
    }
    
    /**
     * Allows one to print a <code>String</code> of characters directly to the output stream.
     *
     * @param text a <code>String</code> value
     */
    public void printRawCharacters(String text) throws IOException{
        content();
        _printer.printText(text);
        //        _printer.flush();
    }

    /**
     * Let the serializer know if the document has already been started.
     *
     * @param setting a <code>boolean</code> value
     */
    public void setDocumentStarted(boolean setting) {
        _started=setting;
    }

    public void flush() throws IOException {
        _printer.flush();
        cacher.flush();
    }
        
}
