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

package org.jasig.portal.security.provider;

import java.net.HttpURLConnection;
import java.text.MessageFormat;

import org.apache.commons.codec.binary.Base64;
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.security.LocalConnectionContext;

/**
 * <p>
 * BasicLocalConnectionContext implements HTTP Basic Authentication as a 
 * LocalConnectionContext. Connections are provided as Objects:
 * they may be URL, LDAP, database connections, etc.
 * </p>
 * 
 * <p>
 * In order to use this class you should define two publish time parameters for
 * your channel: <code>remote.username</code> and <code>remote.password</code>.
 * The username and password default to "tomcat" if you don't set them. 
 * </p>
 *
 *  @author Stephen Barrett, smb1@cornell.edu
 *  @version $Revision$ $Date$ 
 */

public class BasicLocalConnectionContext extends LocalConnectionContext {
    protected static final String CHANPARAMUSERNAME = "remote.username";

    protected static final String CHANPARAMPASSWORD = "remote.password";

    protected static final String AUTHORIZATIONHDR = "Authorization";

    protected static final String AUTHORIZATIONTYPE = "Basic ";

    protected static final String USERNAMEANDPWDMASK = "{0}:{1}";

    // default to tomcat examples default
    private String usernameandpassword = "tomcat:tomcat";

    /**
     * Constructs the username/password combination from the parameters
     * set at publish time.
     * 
     * @param sd
     *            The calling channel's ChannelStaticData.
     */

    public void init(ChannelStaticData sd) {
        staticData = sd;

        /*
         * Construct the username/password combination.
         */
        if (sd.getParameter(CHANPARAMUSERNAME) != null)
            usernameandpassword = MessageFormat.format(USERNAMEANDPWDMASK,
                    (Object[])(new String[] { sd.getParameter(CHANPARAMUSERNAME),
                            sd.getParameter(CHANPARAMPASSWORD) }));

    }

    /**
     * Sets the headers so that the connection will authenticate using 
     * HTTP Basic Authentication using the username and password passed
     * set at publish time.
     * 
     * @param connection
     *            Must be an instance of HttpURLConnection
     * @param rd
     *            The calling channel's ChannelRuntimeData.
     */
    public void sendLocalData(Object connection, ChannelRuntimeData rd) {
        HttpURLConnection modified_connection = (HttpURLConnection) connection;

        // encode and set the authentication credentials
        modified_connection.setRequestProperty(AUTHORIZATIONHDR, AUTHORIZATIONTYPE
                + new String(Base64.encodeBase64(usernameandpassword.getBytes())));

        // all done. This will be sent with the request now.
    }
}