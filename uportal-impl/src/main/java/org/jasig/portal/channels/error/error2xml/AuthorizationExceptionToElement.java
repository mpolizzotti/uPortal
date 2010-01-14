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

package org.jasig.portal.channels.error.error2xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.jasig.portal.AuthorizationException;

/**
 * Translates from a uPortal AuthorizationException to an XML element.
 * @author andrew.petro@yale.edu
 * @version $Revision$ $Date$
 * @since uPortal 2.5
 * @deprecated All IChannel implementations should be migrated to portlets
 */
@Deprecated
public final class AuthorizationExceptionToElement 
    implements IThrowableToElement{

    private ThrowableToElement throwableToElement = new ThrowableToElement();
    
    /* (non-Javadoc)
     * @see org.jasig.portal.channels.error.tt.IThrowableToElement#throwableToElement(java.lang.Throwable, org.w3c.dom.Document)
     */
    public Element throwableToElement(Throwable t, Document parentDoc) 
        throws IllegalArgumentException {
        
        Element element = this.throwableToElement.throwableToElement(t, parentDoc);
        
        if (! (t instanceof AuthorizationException))
            throw new IllegalArgumentException(t.getClass().getName() + 
                    " does not extend AuthorizationException");
        
        element.setAttribute("renderedAs", AuthorizationException.class.getName());
        
        return element;
    }

    /* (non-Javadoc)
     * @see org.jasig.portal.channels.error.tt.IThrowableToElement#supports(java.lang.Class)
     */
    public boolean supports(Class c) throws IllegalArgumentException {
        if (c == null)
            throw new IllegalArgumentException("Cannot support a null class.");
        
        if (! Throwable.class.isAssignableFrom(c))
            throw new IllegalArgumentException("Supports undefined on classes " +
                    "which are not and do not extend Throwable.  Class was " 
                    + c.getName());
        
        return AuthorizationException.class.isAssignableFrom(c);
    }

}