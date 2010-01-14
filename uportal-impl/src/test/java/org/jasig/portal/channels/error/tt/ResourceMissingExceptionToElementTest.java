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

package org.jasig.portal.channels.error.tt;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.jasig.portal.ResourceMissingException;
import org.jasig.portal.channels.error.error2xml.IThrowableToElement;
import org.jasig.portal.channels.error.error2xml.ResourceMissingExceptionToElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Testcase for ResourceMissingExceptionToElement.
 * @author andrew.petro@yale.edu
 * @version $Revision$ $Date$
 */
public final class ResourceMissingExceptionToElementTest extends
        AbstractThrowableToElementTest {

    private ResourceMissingExceptionToElement rmeToElement = 
        new ResourceMissingExceptionToElement();

    /**
     * Test the aspects of the XML production for RMEs
     * that differ from basic Throwable representation.
     * @throws FactoryConfigurationError
     * @throws ParserConfigurationException
     */
    public void testRMEToElement() throws ParserConfigurationException, FactoryConfigurationError {
        ResourceMissingException rme = 
            new ResourceMissingException("http://www.somewhere.com", 
                "A description", "A message");
        Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        
        Element elem = this.rmeToElement.throwableToElement(rme, dom);
        
        assertEquals("throwable", elem.getNodeName());
        NodeList resourceList = elem.getElementsByTagName("resource");
        assertEquals(1, resourceList.getLength());
        Node resourceNode = resourceList.item(0);
        
        NodeList uriList = ((Element) resourceNode).getElementsByTagName("uri");
        assertEquals(1, uriList.getLength());
        Node uri = uriList.item(0);
        assertEquals("http://www.somewhere.com", uri.getFirstChild().getNodeValue());
        
        NodeList descriptionList = ((Element) resourceNode).getElementsByTagName("description");
        assertEquals(1, descriptionList.getLength());
        Node description = descriptionList.item(0);
        assertEquals("A description", description.getFirstChild().getNodeValue());
        
    }

    /* (non-Javadoc)
     * @see org.jasig.portal.channels.error.tt.AbstractThrowableToElementTest#getThrowableToElementInstance()
     */
    protected IThrowableToElement getThrowableToElementInstance() {
        return this.rmeToElement;
    }

    /* (non-Javadoc)
     * @see org.jasig.portal.channels.error.tt.AbstractThrowableToElementTest#supportedThrowable()
     */
    protected Throwable supportedThrowable() {
        return new ResourceMissingException("http://www.somewhere.com", 
                "A description", "A message");
    }

    /* (non-Javadoc)
     * @see org.jasig.portal.channels.error.tt.AbstractThrowableToElementTest#unsupportedThrowable()
     */
    protected Throwable unsupportedThrowable() {
        return new IllegalStateException("Just a test exception.");
    }

}