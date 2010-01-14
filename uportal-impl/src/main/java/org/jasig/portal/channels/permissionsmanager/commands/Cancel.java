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

package org.jasig.portal.channels.permissionsmanager.commands;
import org.jasig.portal.channels.permissionsmanager.IPermissionCommand;
import org.jasig.portal.channels.permissionsmanager.PermissionsSessionData;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An IPermissionCommand implementation that resets CPermissionsManager
 * and sets the prmFinished flag in staticData for IServant operations
 *
 * @author Alex Vigdor
 * @version $Revision$
 * @deprecated All IChannel implementations should be migrated to portlets
 */
@Deprecated
public class Cancel implements IPermissionCommand {
    private static final Log log = LogFactory.getLog(Cancel.class);
    
    /** Creates new StartOver */
    public Cancel() {
    }

    public void execute(PermissionsSessionData session) throws Exception{
        session.XML=null;
        //sd.remove("prmViewDoc");
        session.gotOwners = false;
        //session.owners = null;
        //sd.remove("prmOwners");
        session.gotActivities = false;
        //sd.remove("prmActivities");
        // re-instate when this funcion is available
        session.principals=null;
        session.owners = null;
        //sd.remove("prmPrincipals");
        session.servant = null;
        //sd.remove("prmServant");
        session.gotTargets=false;
        //sd.remove("prmTargets");
        session.isFinished=true;
        session.view = null;
        //sd.setParameter("prmFinished","true");
        log.debug("PermissionsManager.Cancel complete");
    }

}
