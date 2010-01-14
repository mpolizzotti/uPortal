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

import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.channels.groupsmanager.CGroupsManagerSessionData;
import org.jasig.portal.channels.groupsmanager.GroupsManagerXML;
import org.jasig.portal.channels.groupsmanager.Utility;
import org.jasig.portal.groups.ILockableEntityGroup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class update the group with a new name. It then updates
 * all of the xml elements with the new name.
 * @author Don Fracapane
 * @version $Revision$
 * @deprecated All IChannel implementations should be migrated to portlets
 */
@Deprecated
public class UpdateGroup extends GroupsManagerCommand {

   public UpdateGroup () {
   }

   /**
    * This is the public method
    * @param sessionData
    * @throws Exception
    */
   public void execute (CGroupsManagerSessionData sessionData) throws Exception {
      ChannelRuntimeData runtimeData = sessionData.runtimeData;
      Utility.logMessage("DEBUG", "UpdateGroup::execute(): Start");
      Document model = getXmlDoc(sessionData);
      String newName = runtimeData.getParameter("grpName");
      String newDescription = runtimeData.getParameter("grpDescription");
      String updId = getCommandArg(runtimeData);
      Element updElem = GroupsManagerXML.getElementByTagNameAndId(model, GROUP_TAGNAME,
            updId);
      String retMsg;
      String curName = GroupsManagerXML.getElementValueForTagName(updElem, "dc:title");
      if (curName == null || curName.equals("")) {
         Utility.logMessage("ERROR", "UpdateGroup::execute(): Cannot find dc:title element for: "
               + updElem.getAttribute("name"));
         return;
      }
      String curDescription = GroupsManagerXML.getElementValueForTagName(updElem, "dc:description");
      boolean hasChanged = false;
      if (!Utility.areEqual(curName, newName)) {
         Utility.logMessage("DEBUG", "UpdateGroup::execute(): Group name: '" + curName
               + "' will be updated to : '" + newName + "'");
         hasChanged = true;
      }
      if (!Utility.areEqual(curDescription, newDescription)) {
         Utility.logMessage("DEBUG", "UpdateGroup::execute(): Group: '" + newDescription
               + "' will be updated to : '" + newDescription + "'");
         hasChanged = true;
      }
      // Notify user if nothing was changed
      if (!hasChanged) {
         Utility.logMessage("DEBUG", "UpdateGroup::execute(): Update was not applied because nothing has been changed.");
         retMsg = "Update was not applied. No changes were entered.";
         sessionData.feedback = retMsg;
         return;
      }
      //IEntityGroup updGroup = GroupsManagerXML.retrieveGroup(updKey);
      ILockableEntityGroup updGroup = sessionData.lockedGroup;
      if (updGroup == null) {
         retMsg = "Unable to retrieve Group!";
         sessionData.feedback = retMsg;
         return;
      }
      Utility.logMessage("DEBUG", "UpdateGroup::execute(): About to update group: " +
            curName);
      updGroup.setName(newName);
      updGroup.setDescription(newDescription);
      updGroup.updateAndRenewLock();
      Utility.logMessage("DEBUG", "UpdateGroup::execute(): About to update xml nodes for group: "
            + curName);
      // update all xml nodes for this group
      GroupsManagerXML.refreshAllNodes(sessionData.getUnrestrictedData(), updGroup);
      Utility.logMessage("DEBUG", "UpdateGroup::execute(): Finished");
   }
}
