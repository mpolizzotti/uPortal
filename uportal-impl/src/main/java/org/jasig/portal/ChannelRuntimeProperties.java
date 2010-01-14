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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Conveys runtime properties from channel to framework.
 * 
 * <p>In general, the IChannel API contract consists of one-way calls.  
 * The framework instantiates the channel and provisions it with state via
 * setStaticData() and setRuntimeData(); these are one-way calls from framework
 * to channel.  The channel can fail with an exception, but otherwise the method has
 * no return value.  (As of uPortal 2.6, channels can persist parameters back to the
 * StaticChannelData object).
 *   
 * Then there's the renderXML() call, by which the channel writes back to 
 * a content handler to produce output.</p>
 * 
 * <p>The getRuntimeProperties() IChannel method, and this ChannelRuntimeProperties
 * object, provides a way for the channel to communicate back to the framework
 * data other than the content it desires to write out to the end user.  
 * ChannelRuntimeProperties was originally envisioned as a mechanism whereby
 * an IChannel would convey whether it wishes to render at all, and it was 
 * envisioned that its place in the sequence of IChannel calls would be after
 * setRuntimeData() but before renderXML().  However, this was not implemented.
 * Through uPortal 2.5.0, no framework code called getRuntimeProperties() on
 * IChannels.</p>
 * 
 * <p>For uPortal 2.5.1, ChannelRuntimeProperties were recruited for use in 
 * implementing dynamic channel titles.  Channels returning ChannelRuntimeProperties
 * which implement the optional interface IChannelTitle convey a desired dynamic title
 * for rendering by the framework in place of the title defined at channel
 * publication.  In support of this change, the channel rendering framework
 * was modified to actually get the ChannelRuntimeProperties from channels for
 * inspection as to whether it implements this interface.  This method call was
 * added <b>after renderXML()</p>, not before as originally envisioned.  This
 * order was selected because many channels are not prepared to know their dynamic 
 * title until after they have rendered - particularly IPortletAdaptor, as the
 * JSR-168 mechanism for portlets to set their titles is part of their 
 * configuration of the RenderResponse.</p>
 * 
 * <p>Note that this does not foreclose the possibility of using 
 * ChannelRuntimeProperties to allow a channel to convey that it does not wish
 * to be rendered at all at this time.  Such a channel could write no content
 * to the contentHandler, and return a ChannelRuntimeProperties conveying its
 * wish not to be rendered at all.  Similarly to the way dynamic channel titles
 * are supported, dropping the channel from the output entirely could be supported.</p>
 * 
 * <p>Warning: The willRender() feature of ChannelRuntimeProperties is not 
 * implemented.  No framework code will currently check for this property. 
 * Setting willRender one way or the other currently doesn't have any effect.</p>
 * 
 * Current version gathers the following information
 * <ul>
 *  <li> willRender  - a boolean flag signaling if the channel will render at all in the current state. 
 * This flag could be checked after the ChannelRuntimeData has been passed to a channel, 
 * but prior to the renderXML() call.
 *   However, no framework code currently checks the willRender property of ChannelRuntimeData.</li>
 * </ul>
 * @version $Revision$ $Date$ 
 * @author Peter Kharchenko  {@link <a href="mailto:pkharchenko@interactivebusiness.com"">pkharchenko@interactivebusiness.com"</a>}
 * @deprecated All IChannel implementations should be migrated to portlets
 */
@Deprecated
public class ChannelRuntimeProperties
{
    protected final Log log = LogFactory.getLog(getClass());
    
  private boolean bool_willRender;

    /**
     * Default constructor.
     * willRender property is initialized to <code>true</code>.
     *
     */
    public ChannelRuntimeProperties() {
      // set the default values here
      bool_willRender = true;
    }

    /**
     * Set whether the channel will render if asked to do so (if renderXML() is called).
     *
     * Warning: no known code currently accesses this property of ChannelRuntimeProperties
     * and so setting this property will have no effect.
     *
     * @param value a <code>boolean</code> value
     */
    public void setWillRender (boolean value) {
        bool_willRender = value;
    }


    /**
     * Getter method for willRender property.
     *
     * @return a <code>boolean</code> value
     */
    public boolean willRender() {
        return bool_willRender;
    }
}
