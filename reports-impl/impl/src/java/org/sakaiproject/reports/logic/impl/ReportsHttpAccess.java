/**********************************************************************************
* $URL:https://source.sakaiproject.org/svn/osp/trunk/reports/api-impl/src/java/org/theospi/portfolio/reports/model/impl/ReportsHttpAccess.java $
* $Id:ReportsHttpAccess.java 9134 2006-05-08 20:28:42Z chmaurer@iupui.edu $
***********************************************************************************
*
 * Copyright (c) 2005, 2006, 2007, 2008 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.osedu.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*
**********************************************************************************/
package org.sakaiproject.reports.logic.impl;

import org.sakaiproject.entity.api.EntityAccessOverloadException;
import org.sakaiproject.entity.api.EntityCopyrightException;
import org.sakaiproject.entity.api.EntityNotDefinedException;
import org.sakaiproject.entity.api.EntityPermissionException;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.exception.CopyrightException;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.exception.ServerOverloadException;
import org.sakaiproject.metaobj.shared.mgt.ReferenceParser;
import org.sakaiproject.metaobj.shared.mgt.HttpAccessBase;
import org.sakaiproject.metaobj.security.AuthorizationFacade;
import org.sakaiproject.reports.service.ReportsManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * This class can check for access permissions on a particular reference
 * 
 * User: John Ellis
 * Date: Dec 24, 2005
 * Time: 12:03:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportsHttpAccess extends HttpAccessBase {

   private ReportsManager reportsManager;

   private AuthorizationFacade authzManager;

   public void handleAccess(HttpServletRequest req, HttpServletResponse res, Reference ref,
                            Collection copyrightAcceptedRefs)
         throws EntityPermissionException, EntityNotDefinedException, EntityAccessOverloadException, EntityCopyrightException {
      ReferenceParser parser =
            new ReferenceParser(ref.getReference(), ref.getEntityProducer());
      authzManager.pushAuthzGroups(parser.getSiteId());

      super.handleAccess(req, res, ref, copyrightAcceptedRefs);
   }


   public AuthorizationFacade getAuthzManager() {
      return authzManager;
   }

   public void setAuthzManager(AuthorizationFacade authzManager) {
      this.authzManager = authzManager;
   }

   /**
    * Given a file reference and the reference parser (fill in what these are)
    * This method asks the ReportsManager Singleton whether or not the request
    * has access to the particular file.  This method throws an exception
    * when something isn't correct about the user, the request, or the file
    * @throws PermissionException
    * @throws IdUnusedException
    * @throws ServerOverloadException
    * @throws CopyrightException
    */
   protected void checkSource(Reference ref, ReferenceParser parser)
      throws EntityPermissionException, EntityNotDefinedException, EntityAccessOverloadException, EntityCopyrightException {
      getReportsManager().checkReportAccess(parser.getId(), parser.getRef());
   }

   /** gets the class */
   public ReportsManager getReportsManager() {
      return reportsManager;
   }

   /** sets the class, from the components.xml */
   public void setReportsManager(ReportsManager reportsManager) {
      this.reportsManager = reportsManager;
   }

}
