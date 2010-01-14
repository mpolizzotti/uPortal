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

package org.jasig.portal.portlet.dao;

import java.util.Set;

import org.jasig.portal.portlet.om.IPortletDefinitionId;
import org.jasig.portal.portlet.om.IPortletEntity;
import org.jasig.portal.portlet.om.IPortletEntityId;

/**
 * Provides APIs for creating, storing and retrieving {@link IPortletEntity} objects.
 * 
 * @author Eric Dalquist
 * @version $Revision$
 */
public interface IPortletEntityDao {
    /**
     * Creates, initializes and persists a new {@link IPortletEntity} based on the specified {@link IPortletDefinitionId},
     * channel subscribe id and user id. 
     * 
     * @param portletDefinitionId The id of the {@link org.jasig.portal.portlet.om.IPortletDefinition} that is the parent of the new entity
     * @param channelSubscribeId The subscription id for the channel the entity is based on in the user's layout.
     * @param userId The id of the user the entity is for.
     * @return A newly created, initialized and persisted entity.
     * @throws IllegalArgumentException if portletDefinitionId or channelSubscribeId are null.
     * @throws org.springframework.dao.DataIntegrityViolationException If an entity already exists for the channel
     *         subscribe id and userId pair
     * @throws org.springframework.dao.DataRetrievalFailureException If no {@link org.jasig.portal.portlet.om.IPortletDefinition}
     *         exists for the specified {@link IPortletDefinitionId} 
     */
    public IPortletEntity createPortletEntity(IPortletDefinitionId portletDefinitionId, String channelSubscribeId, int userId);
    
    /**
     * Persists changes to a {@link IPortletEntity}.
     * 
     * @param portletEntity The portlet entity to store the changes for
     * @throws IllegalArgumentException if portletEntity is null.
     */
    public void updatePortletEntity(IPortletEntity portletEntity);
    
    /**
     * Get a {@link IPortletEntity} for the specified {@link IPortletEntityId}.
     * 
     * @param portletEntityId The id to get the entity for.
     * @return The portlet entity for the id, null if no entity exists for the id.
     * @throws IllegalArgumentException if portletEntityId is null.
     */
    public IPortletEntity getPortletEntity(IPortletEntityId portletEntityId);
    
    /**
     * Get a {@link IPortletEntity} for the specified channel subscribe id and user id.
     * 
     * @param channelSubscribeId The channel subscription id from the user's layout to get the entity for
     * @param userId The user id to get the entity for
     * @return The portlet entity for the id, null if no entity exists for the ids.
     * @throws IllegalArgumentException if channelSubscribeId is null.
     */
    public IPortletEntity getPortletEntity(String channelSubscribeId, int userId);
    
    /**
     * Get all {@link IPortletEntity}s based on the specified {@link IPortletDefinitionId}.
     * 
     * @param portletDefinitionId The ID of the parent portlet definition
     * @return A set of all entities based on the specified portlet definition id, will be empty if no entities exist for the id, will never be null.
     * @throws IllegalArgumentException if portletDefinitionId is null.
     */
    public Set<IPortletEntity> getPortletEntities(IPortletDefinitionId portletDefinitionId);
    
    /**
     * Get all {@link IPortletEntity}s that exist for the specified user id. (From {@link org.jasig.portal.security.IPerson#getID()}.
     * 
     * @param userId The id of the user to get the entities for.
     * @return A set of all entities base on the specified user id, will be empty if no entities exist for the id, will never be null.
     */
    public Set<IPortletEntity> getPortletEntitiesForUser(int userId);
    
    /**
     * Removes the specified {@link IPortletEntity} from the persistent store.
     * 
     * @param portletEntity The entity to remove.
     * @throws IllegalArgumentException if portletEntity is null.
     */
    public void deletePortletEntity(IPortletEntity portletEntity);
}
