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

package org.jasig.portal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portal.serialize.CachingSerializer;
import org.jasig.portal.serialize.MarkupSerializer;
import org.jasig.portal.utils.SAX2FilterImpl;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;


/**
 * A filter that incorporates channel content into the main SAX stream.
 * Unlike a regular {@link ChannelIncorporationFilter}, this class can
 * feed cache character buffers to the {@link CachingSerializer}.
 * 
 * <p>Replaces
 * &lt;channel ID="channelSubcribeId"/&gt;
 * elements with channel output from the IChannelRenderer for that 
 * channelSubscribeId.</p>
 * 
 * <p><Replaces 
 * &lt;channel-title 
 *     channelSubcribeId="channelSubcribeId" 
 *     defaultValue="defaultTitle" /&gt;
 *
 * elements with dynamic channel title from the IChannelRenderer for that 
 * channelSubcribeId, or the provided defaultValue in the case where there is 
 * no dynamic channel title.</p>
 *
 * @author Peter Kharchenko  {@link <a href="mailto:pkharchenko@interactivebusiness.com"">pkharchenko@interactivebusiness.com"</a>}
 * @version $Revision$ $Date$
 */
public class CharacterCachingChannelIncorporationFilter extends SAX2FilterImpl {
    
    protected final Log logger = LogFactory.getLog(this.getClass());
    
    /** 
     * Track what, if any, incorporation element we are currently processing.
     * "channel" if we are "in" the <channel> element.
     * "channel-title" if we are "in" the <channel-title> element.
     * Null if we are not in one of these elements.  Other elements are not
     * processed by this filter (are not "incorporation elements").
     */
    private String insideElement = null;
    
    ChannelManager cm;

    /**
     * ChannelSubscribeId of the currently-being-incorporated channel, if any.
     * Null if not currently incorporating a channel (not in an incorporation
     * element.)
     */ 
    private String channelSubscribeId = null;
    
    private String channelTitle = null;
    private String defaultChannelTitle = null;
    
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    
    private final boolean ccaching;
    private final CachingSerializer ser;

    private List<CacheEntry> cacheEntries;

    // constructors

    /**
     * Downward chaining constructor.
     */
    public CharacterCachingChannelIncorporationFilter(ContentHandler handler, ChannelManager chanm, boolean ccaching, HttpServletRequest request, HttpServletResponse response) {
        super(handler);

        if (handler instanceof CachingSerializer) {
            this.ccaching = ccaching;
            this.ser = (CachingSerializer) handler;
            this.cacheEntries = new LinkedList<CacheEntry>();
        }
        else {
            this.ccaching = false;
            this.ser = null;
            this.cacheEntries = null;
        }

        this.cm = chanm;
        this.request = request;
        this.response = response;
    }


    /**
     * Obtain system cache blocks.
     *
     * @return a <code>List</code> of <code>CacheEntry</code> blocks.
     */
    public List<CacheEntry> getCacheBlocks() {
        if(ccaching) {
            return cacheEntries;
        }
        return null;
    }

    @Override
    public void startDocument () throws SAXException {
        if(ccaching) {
            // start caching
            try {
                if(!ser.startCaching()) {
                    log.error("CharacterCachingChannelIncorporationFilter::startDocument() " +
                    		": unable to start caching!");
                }
            } catch (IOException ioe) {
                log.error("CharacterCachingChannelIncorporationFilter::startDocument() " +
                		": unable to start caching!",ioe);
            }
        }
        super.startDocument();
    }

    @Override
    public void endDocument () throws SAXException {
        super.endDocument();
        if(ccaching) {
            // stop caching
            try {
                if(ser.stopCaching()) {
                    try {
                        cacheEntries.add(new StringCacheEntry(ser.getCache()));
                    } catch (UnsupportedEncodingException e) {
                        log.error("CharacterCachingChannelIncorporationFilter::endDocument() " +
                        		": unable to obtain character cache, invalid encoding specified ! ",e);
                    } catch (IOException ioe) {
                        log.error("CharacterCachingChannelIncorporationFilter::endDocument() " +
                        		": IO exception occurred while retreiving character cache ! ",ioe);
                    }
                } else {
                    log.error("CharacterCachingChannelIncorporationFilter::endDocument() " +
                    		": unable to stop caching!");
                }
            } catch (IOException ioe) {
                log.error("CharacterCachingChannelIncorporationFilter::endDocument() " +
                		": unable to stop caching!", ioe);
            }

        }
    }

    private void startCaching() {
        // start caching again
        try {
            if (!ser.startCaching()) {
                log
                    .error("CharacterCachingChannelIncorporationFilter::endElement() : unable to restart cache after a channel end!");
            }
        } catch (IOException ioe) {
            log
                .error(
                    "CharacterCachingChannelIncorporationFilter::endElement() : unable to start caching!",
                    ioe);
        }
    }

    private void stopCaching() {
        // save the old cache state
        try {
            if (ser.stopCaching()) {
                if (log.isDebugEnabled()) {
                    log
                            .debug("CharacterCachingChannelIncorporationFilter::endElement() "
                                    + ": obtained the following system character entry: \n"
                                    + ser.getCache());
                }
                cacheEntries.add(new StringCacheEntry(ser.getCache()));
            } else {
                log
                        .error("CharacterCachingChannelIncorporationFilter::startElement() "
                                + ": unable to reset cache state ! Serializer was not caching when it should've been !");
            }
        } catch (UnsupportedEncodingException e) {
            log
                    .error(
                            "CharacterCachingChannelIncorporationFilter::startElement() "
                                    + ": unable to obtain character cache, invalid encoding specified ! ",
                            e);
        } catch (IOException ioe) {
            log
                    .error(
                            "CharacterCachingChannelIncorporationFilter::startElement() "
                                    + ": IO exception occurred while retreiving character cache ! ",
                            ioe);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {

		if (log.isTraceEnabled()) {
			log
					.trace("CharacterCachingChannelIncorporationFilter is filtering element with "
							+ "uri="
							+ uri
							+ " localName="
							+ localName
							+ " qName="
							+ qName
							+ "atts="
							+ atts
							+ " .  Current channelSubscribeId="
							+ this.channelSubscribeId
							+ " and in element "
							+ this.insideElement);
		}

		if (!isInIncorporationElement()) {
			// recognizing "channel"
			if (qName.equals("channel")) {
				this.insideElement = "channel";
				this.channelSubscribeId = atts.getValue("ID");
                if (this.channelSubscribeId == null) // fname access used
                {
                    String fname = atts.getValue("fname");
                    if (fname.equals("")) // can't obtain subscribe id
                        log.error("Incurred a channel with no subscribe id " +
                                "in attribute 'ID' and no functional name " +
                                "in attribute 'fname'.");
                    else
                    {
                        // get the channel from layout if there or instantiate 
                        // in transient layout manager if not
                        try
                        {
                            this.channelSubscribeId = cm.getSubscribeId(fname);
                        } catch (PortalException e)
                        {
                            log.error("Unable to obtain subscribe id for " +
                                    "channel with functional name '" + fname +
                                "'.", e);
                        }
                    }
                }
				if (ccaching) {
                    stopCaching();
				}
			} else if (qName.equals("channel-title")) {
				this.insideElement = "channel-title";
				this.channelSubscribeId = atts.getValue("channelSubscribeId");
                this.defaultChannelTitle = atts.getValue("defaultValue");
                this.channelTitle = this.defaultChannelTitle;
                if (ccaching) {
                    stopCaching();
                }
			} else {
				// not in an incorporation element and not starting one this class
				// handles specially, so pass the element through this filter.
				super.startElement(uri, localName, qName, atts);

			}
		} else {
			// inside an incorporation element, do nothing.
			if (log.isTraceEnabled()) {
				log.trace("Ignoring element " + qName);
			}
		}
	}

    @Override
    public void endElement (String uri, String localName, String qName) throws SAXException  {
    	
    	if (log.isTraceEnabled()) {
    		log.trace("CharacterCachingChannelIncorporationFilter is filtering end element with "
    				+ "url=" + uri + " localName=" + localName 
					+ " qName=" + qName + " .  Current channelSubscribeId=" 
					+ this.channelSubscribeId + " and in element " + this.insideElement);
    	}
    	
        // if inside an element this filter handles (incorporates content in place of)
    	// then this endElement call may be the time to end one of these special elements.
    	if (isInIncorporationElement()) {
            if (qName.equals ("channel") && this.insideElement.equals("channel")) {

            	try {
            	    ContentHandler contentHandler = getContentHandler();
					if (contentHandler != null) {
						if (ccaching) {
							cacheEntries.add(new ChannelContentCacheEntry(channelSubscribeId));
						}
						this.flush();
						cm.outputChannel(this.request, this.response, this.channelSubscribeId, contentHandler);
						this.flush();
						if (ccaching) {
                            startCaching();
						}
					} else {
						// contentHandler was null. This is a serious problem,
						// since
						// filtering is pointless if it's not writing back to a
						// contentHandler
						log.error("null ContentHandler prevents outputting channel with subscribe id = " + channelSubscribeId);
					}
            	} finally {
                    endIncorporationElement();
            	}
            }
        } else {
        	// pass non-incorporation elements through untouched.
            super.endElement (uri,localName,qName);
        }
    }
    
    public void flush() {
        final ContentHandler contentHandler = this.getContentHandler();
        if (contentHandler instanceof MarkupSerializer) {
            try {
                ((MarkupSerializer) contentHandler).flush();
            }
            catch (IOException e) {
                logger.warn("IOException while flushing serializer output", e);
            }
        }
    }
    
    @Override
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	sb.append(getClass());
    	sb.append(" caching: ").append(this.ccaching);
    	sb.append(" currently processing: subscribeId=").append(this.channelSubscribeId);
    	sb.append(" in incorporation element: ").append(this.insideElement);
    	
    	return sb.toString();
    }
    
    /**
     * Returns true if this filter is currently processing an incorporation element.
     * The purpose of this method is to allow ignoring of elements starting within elements 
     * this filter is already incorporating, and allow ignoring of end elements except when
     * those end elements end incorporation elements.
     * 
     * @return true if in an incorporation element, false otherwise
     */
    private boolean isInIncorporationElement() {
    	// in an incorporation element when the stored name of that element is not null.
    	return this.insideElement != null;
    }
    
    /**
     * Reset filter state to not being "inside" an incorporation element (and
     * therefore instead being available to process the next incorporation 
     * element encountered).  
     *
     * @throws IllegalStateException if not currently inside an element.
     */
    private void endIncorporationElement() {
    	if (this.channelSubscribeId == null || this.insideElement == null) {
    		throw new IllegalStateException("Cannot end element when not in element:" + this);
    	}
    	this.channelSubscribeId = null;
    	this.insideElement = null;
    }
    
    
    
}

