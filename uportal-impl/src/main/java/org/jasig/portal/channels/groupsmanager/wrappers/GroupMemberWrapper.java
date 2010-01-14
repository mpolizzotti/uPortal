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

package  org.jasig.portal.channels.groupsmanager.wrappers;

import org.jasig.portal.channels.groupsmanager.CGroupsManagerUnrestrictedSessionData;
import org.jasig.portal.channels.groupsmanager.GroupsManagerConstants;
import org.jasig.portal.channels.groupsmanager.GroupsManagerXML;
import org.jasig.portal.channels.groupsmanager.IGroupsManagerPermissions;
import org.jasig.portal.channels.groupsmanager.IGroupsManagerWrapper;
import org.jasig.portal.channels.groupsmanager.Utility;
import org.jasig.portal.groups.IGroupMember;
import org.jasig.portal.security.IAuthorizationPrincipal;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Returns an xml element for an IGroupMember.
 * @author Don Fracapane
 * @version $Revision$
 * @deprecated All IChannel implementations should be migrated to portlets
 */
@Deprecated
public abstract class GroupMemberWrapper
      implements IGroupsManagerWrapper, GroupsManagerConstants {
   protected String ELEMENT_TAGNAME ;

   /** Creates new GroupMemberWrapper */
   public GroupMemberWrapper () {
   }

   /**
    * Returns an xml element for a given IGroupMember key. The element that is returned
    * could be the same one that is passed in (usually with the expanded attribute
    * set to "true" or a new element (all attributes have to be set after the
    * GroupMember is retrieved).
    * @param aKey String
    * @param aType String
    * @param anElem Element
    * @param sessionData CGroupsManagerUnrestrictedSessionData
    * @return Element
    */
   public Element getXml (String aKey, String aType, Element anElem, CGroupsManagerUnrestrictedSessionData sessionData) {
      Document aDoc = sessionData.model;
      Utility.logMessage("DEBUG", "GroupMemberWrapper::getXml(" + aKey + "): START");
      Element rootElem = (anElem != null ? anElem : GroupsManagerXML.createElement(ELEMENT_TAGNAME, aDoc, false));
      Utility.logMessage("DEBUG", "GroupMemberWrapper::getXml(" + aKey + "): rootElem: " + rootElem);
      IGroupMember gm = retrieveGroupMember(aKey, aType);
      Utility.logMessage("DEBUG", "GroupMemberWrapper::getXml(" + aKey + "): grp: " + gm);
      getXml(gm, rootElem, sessionData);
      return  rootElem;
   }

   /**
    * Adds permission attributes to the group member element.
    * @param rootElem Element
    * @param gm IGroupMember
    * @param gmp IGroupsManagerPermissions
    * @param ap IAuthorizationPrincipal
    */
   public void applyPermissions (Element rootElem, IGroupMember gm, IGroupsManagerPermissions gmp, IAuthorizationPrincipal ap) {
      // set user permissions for group
      rootElem.setAttribute("canAssignPermissions", String.valueOf(gmp.canAssignPermissions(ap, gm)));
      rootElem.setAttribute("canCreateGroup", String.valueOf(gmp.canCreateGroup(ap, gm)));
      rootElem.setAttribute("canManageMembers", String.valueOf(gmp.canManageMembers(ap, gm)));
      rootElem.setAttribute("canDelete", String.valueOf(gmp.canDelete(ap, gm)));
      rootElem.setAttribute("canSelect", String.valueOf(gmp.canSelect(ap, gm)));
      rootElem.setAttribute("canUpdate", String.valueOf(gmp.canUpdate(ap, gm)));
      rootElem.setAttribute("canView", String.valueOf(gmp.canView(ap, gm)));
      rootElem.setAttribute("canViewProperties", String.valueOf(gmp.canViewProperties(ap, gm)));
      return;
   }

   /**
    * Returns an xml element for a given IGroupMember.
    * @param gm IGroupMember
    * @param anElem Element
    * @param sessionData CGroupsManagerUnrestrictedSessionData
    * @return Element
    */
   public abstract Element getXml (IGroupMember gm, Element anElem, CGroupsManagerUnrestrictedSessionData sessionData) ;

    /**
    * Returns a GroupMember for a key.
    * @param aKey
    * @param aType
    * @return IGroupMember
    */
   protected abstract IGroupMember retrieveGroupMember (String aKey, String aType) ;
}