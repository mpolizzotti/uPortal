/**
 * Copyright (c) 2000 The JA-SIG Collaborative.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. Redistributions of any form whatsoever must retain the following
 *    acknowledgment:
 *    "This product includes software developed by the JA-SIG Collaborative
 *    (http://www.jasig.org/)."
 *
 * THIS SOFTWARE IS PROVIDED BY THE JA-SIG COLLABORATIVE "AS IS" AND ANY
 * EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE JA-SIG COLLABORATIVE OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

/**
 * This pseudo-channel provides header XML document fragment
 * to be used in compilation of the structuredLayoutXML
 * @author Peter Kharchenko
 * @version $Revision 1.1$
 */

package org.jasig.portal.xmlchannels;

import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.servlet.jsp.*;
import javax.servlet.http.*;
import org.jasig.portal.*;



public class CHeaderChannel implements IXMLChannel
{

    private ChannelRuntimeData runtimeData;


    // most of these are empty
    public void setStaticData(ChannelStaticData sd) {
    };

    public void setRuntimeData(ChannelRuntimeData rd) {
	// need to save runtimedata so we can determine media from the
	// request object contained in it
	runtimeData=rd;
    }

    public void receiveEvent(LayoutEvent ev) {
	// we have no events to process here
    }

    // report static channel properties to the portal
    public ChannelSubscriptionProperties getSubscriptionProperties() {
	ChannelSubscriptionProperties csb=new ChannelSubscriptionProperties();
	csb.setName("HeaderChannel");
	return csb;
    }

    // report runtime channel properties to the portal
    public ChannelRuntimeProperties getRuntimeProperties() {
	// channel will always render, so the default values are ok
	return new ChannelRuntimeProperties();
    }



    public void renderXML(DocumentHandler out)
    {
	HttpSession session = (runtimeData.getHttpRequest()).getSession (false);
	String userName= (String) session.getAttribute ("userName");
	String headerXMLString= new String("<header>\n<title>"+userName+", welcome !</title>");
	headerXMLString+="<description>XML version \n   </description> \n    <image> \n    <link>http://localhost:8080/xmlportal/</link> \n    <url>images/MyIBS.gif</url> \n    <description>IBS logo</description> \n    <width>100</width> \n    <height>50</height> \n   </image> \n  </header>";

	try {
	    Parser documentParser = ParserFactory.makeParser("org.apache.xerces.parsers.SAXParser");
	    documentParser.setDocumentHandler(out);
	    documentParser.parse(new org.xml.sax.InputSource(new StringReader(headerXMLString)));
	} catch (Exception e) {};

    }

}
