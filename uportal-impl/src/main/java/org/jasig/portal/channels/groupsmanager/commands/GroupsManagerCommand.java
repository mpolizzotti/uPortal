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

package  org.jasig.portal.channels.groupsmanager.commands;

import org.jasig.portal.ChannelStaticData;
import org.jasig.portal.channels.groupsmanager.CGroupsManagerSessionData;
import org.jasig.portal.channels.groupsmanager.GroupsManagerConstants;
import org.jasig.portal.channels.groupsmanager.IGroupsManagerCommand;
import org.jasig.portal.channels.groupsmanager.Utility;
import org.jasig.portal.groups.IGroupMember;
import org.jasig.portal.security.IAuthorizationPrincipal;
import org.jasig.portal.security.IPermission;
import org.jasig.portal.security.IUpdatingPermissionManager;
import org.jasig.portal.services.AuthorizationService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This class is the parent class of all other command classes.
 * @author Don Fracapane
 * @version $Revision$
 * @deprecated All IChannel implementations should be migrated to portlets
 */
@Deprecated
public abstract class GroupsManagerCommand
      implements IGroupsManagerCommand, GroupsManagerConstants {

   /**
    * GroupsManagerCommand is the parent of all Groups Manager commands. It
    * hold the commone functionality of all commands.
    */
   public GroupsManagerCommand () {
   }

   /**
    * This is the public method
    * @param sessionData
    * @throws Exception
    */
   public void execute (CGroupsManagerSessionData sessionData) throws Exception{}

   /**
    * clear out the selection list
    * @param sessionData
    */
   protected void clearSelected (CGroupsManagerSessionData sessionData) {
      Element rootElem = getXmlDoc(sessionData).getDocumentElement();
      NodeList nGroupList = rootElem.getElementsByTagName(GROUP_TAGNAME);
      NodeList nPersonList = rootElem.getElementsByTagName(ENTITY_TAGNAME);
      NodeList nList = nGroupList;
      for (int i = 0; i < nList.getLength(); i++) {
         Element elem = (org.w3c.dom.Element)nList.item(i);
         elem.setAttribute("selected", "false");
      }
      nList = nPersonList;
      for (int i = 0; i < nList.getLength(); i++) {
         Element elem = (org.w3c.dom.Element)nList.item(i);
         elem.setAttribute("selected", "false");
      }
      return;
   }

   /**
    * Removes all of the permissions for a GroupMember. We need to get permissions
    * for the group as a principal and as a target. I am merging the 2 arrays into a
    * single array in order to use the transaction management in the RDBMPermissionsImpl.
    * If an exception is generated, I do not delete the group or anything else.
    * Possible Exceptions: AuthorizationException and GroupsException
    * @param grpMbr
    * @throws Exception
    */
   protected void deletePermissions (IGroupMember grpMbr) throws Exception{
      try {
         String grpKey = grpMbr.getKey();
         // first we retrieve all permissions for which the group is the principal
         IAuthorizationPrincipal iap = AuthorizationService.instance().newPrincipal(grpMbr);
         IPermission[] perms1 = iap.getPermissions();

         // next we retrieve all permissions for which the group is the target
         IUpdatingPermissionManager upm = AuthorizationService.instance().newUpdatingPermissionManager(OWNER);
         IPermission[] perms2 = upm.getPermissions(null, grpKey);

         // merge the permissions
         IPermission[] allPerms = new IPermission[perms1.length + perms2.length];
         System.arraycopy(perms1,0,allPerms,0,perms1.length);
         System.arraycopy(perms2,0,allPerms,perms1.length,perms2.length);

         upm.removePermissions(allPerms);
      }
      catch (Exception e) {
         String errMsg = "DeleteGroup::deletePermissions(): Error removing permissions for " + grpMbr;
         Utility.logMessage("ERROR", errMsg, e);
         throw new Exception(errMsg);
      }
   }

   /**
    * Answers if the parentGroupId has been set. If it has not been set, this
    * would indicate that Groups Manager is in Servant mode.
    * @param staticData
    * @return boolean
    */
   protected boolean hasParentId (ChannelStaticData staticData) {
      String pk = getParentId(staticData);
      if (pk == null) {
         return  false;
      }
      if (pk.equals("")) {
         return  false;
      }
      Utility.logMessage("Debug", "GroupsManagerCommand::hasParentId: Value is set to default: "
            + pk);
      return  true;
   }

   /**
    * Returns the grpCommand parameter from runtimeData
    * @param runtimeData
    * @return String
    */
   protected String getCommand (org.jasig.portal.ChannelRuntimeData runtimeData) {
      return  (String)runtimeData.getParameter("grpCommand");
   }

   /**
    * Returns the grpCommandIds parameter from runtimeData. The string usually
    * hold one element ID but could contain a string of delimited ids. (See
    * RemoveMember command).
    * @param runtimeData
    * @return String
    */
   protected String getCommandArg (org.jasig.portal.ChannelRuntimeData runtimeData) {
      return  (String)runtimeData.getParameter("grpCommandArg");
   }

   /**
    * Returns the groupParentId parameter from staticData
    * @param staticData
    * @return String
    */
   protected String getParentId (ChannelStaticData staticData) {
      return  staticData.getParameter("groupParentId");
   }

   /**
    * Returns the userID from the user object
    * @param sessionData
    * @return String
    */
   protected String getUserID (CGroupsManagerSessionData sessionData) {
      return  String.valueOf(sessionData.user.getID());
   }

   /**
    * Returns the cached xml document from staticData
    * @param sessionData
    * @return Document
    */
   protected Document getXmlDoc (CGroupsManagerSessionData sessionData) {
      //return  (Document)staticData.get("xmlDoc");sessionData.model
      return  sessionData.model;
   }

   /**
    * Set the CommandArg value, useful for commands which would like to chain
    * other commands
    * @param runtimeData
    * @param arg String
    */
   protected void setCommandArg (org.jasig.portal.ChannelRuntimeData runtimeData, String arg) {
      runtimeData.setParameter("grpCommandArg",arg);
   }

}
