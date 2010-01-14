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

package org.jasig.portal.layout.dlm;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portal.UserProfile;
import org.jasig.portal.events.PortalEvent;
import org.jasig.portal.events.support.UserLoggedOutPortalEvent;
import org.jasig.portal.events.support.UserSessionDestroyedPortalEvent;
import org.jasig.portal.layout.IUserLayoutStore;
import org.jasig.portal.layout.UserLayoutStoreFactory;
import org.jasig.portal.security.IPerson;
import org.jasig.portal.utils.Tuple;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.w3c.dom.Document;

/**
 * Provides API for layout caching service
 */
public class LayoutCachingService implements ApplicationListener, ILayoutCachingService {
    protected final Log logger = LogFactory.getLog(this.getClass());
    
    private Map<Serializable, Document> layoutCache;
    
    /**
     * @return the layoutCache
     */
    public Map<Serializable, Document> getLayoutCache() {
        return layoutCache;
    }
    /**
     * @param layoutCache the layoutCache to set
     */
    public void setLayoutCache(Map<Serializable, Document> layoutCache) {
        this.layoutCache = layoutCache;
    }

    /* (non-Javadoc)
     * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
     */
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof UserLoggedOutPortalEvent || event instanceof UserSessionDestroyedPortalEvent) {
            final PortalEvent portalEvent = (PortalEvent)event;
            final IPerson person = portalEvent.getPerson();
            
            //Try invalidating just the layout associated with the current user and profile
            final UserProfile currentUserProfile = (UserProfile)person.getAttribute(UserProfile.USER_PROFILE);
            if (currentUserProfile != null) {
                this.removeCachedLayout(person, currentUserProfile);
                return;
            }
            
            //No provided profile, invalidate all layouts for the user
            final IUserLayoutStore userLayoutStore = UserLayoutStoreFactory.getUserLayoutStoreImpl();
            final Hashtable<Integer, UserProfile> userProfiles;
            try {
                userProfiles = userLayoutStore.getUserProfileList(person);
            }
            catch (Exception e) {
                this.logger.warn("Failed to load all UserProfiles for '" + person.getUserName() + "'. The user's layouts will not be explicitly removed from the layout cache.", e);
                return;
            }
            
            for (final UserProfile userProfile : userProfiles.values()) {
                this.removeCachedLayout(person, userProfile);
            }
        }
    }

    public void cacheLayout(IPerson owner, UserProfile profile, Document layout) {
        final Serializable cacheKey = this.getCacheKey(owner, profile);
        this.layoutCache.put(cacheKey, layout);
    }
    
    public Document getCachedLayout(IPerson owner, UserProfile profile) {
        final Serializable cacheKey = this.getCacheKey(owner, profile);
        return this.layoutCache.get(cacheKey);
    }
    
    public void removeCachedLayout(IPerson owner, UserProfile profile) {
        final Serializable cacheKey = this.getCacheKey(owner, profile);
        this.layoutCache.remove(cacheKey);
    }
    protected Serializable getCacheKey(IPerson owner, UserProfile profile) {
        return new Tuple<String, Integer>(owner.getUserName(), profile.getLayoutId());
    }
}
