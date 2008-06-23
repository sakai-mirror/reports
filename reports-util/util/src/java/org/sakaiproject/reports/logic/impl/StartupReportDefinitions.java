package org.sakaiproject.reports.logic.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.metaobj.security.impl.AllowAllSecurityAdvisor;
import org.sakaiproject.reports.service.ReportsManager;
import org.sakaiproject.tool.cover.SessionManager;

import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: johnellis
 * Date: Jul 24, 2007
 * Time: 9:46:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class StartupReportDefinitions {

   protected final Log logger = LogFactory.getLog(getClass());

   private SecurityService securityService;
   private ReportsManager reportsManager;
   private List definitions;
   private boolean autoDdl = true;
   
   protected void init() throws Exception {
       logger.info("init() StartupReportDefinitions");
       
       if (isAutoDdl()) {
          initDefinedReportDefinitions();
       }
   }

   protected void initDefinedReportDefinitions() {
       getSecurityService().pushAdvisor(new AllowAllSecurityAdvisor());

       org.sakaiproject.tool.api.Session sakaiSession = SessionManager.getCurrentSession();
       String userId = sakaiSession.getUserId();
       sakaiSession.setUserId("admin");
       sakaiSession.setUserEid("admin");

       try {
           for (Iterator i = getDefinitions().iterator(); i.hasNext();) {
               ReportsDefinitionWrapper wrapper = (ReportsDefinitionWrapper) i.next();
               wrapper.setParentClass(getClass());
               getReportsManager().addReportDefinition(wrapper);
           }

       } finally {
           getSecurityService().popAdvisor();
           sakaiSession.setUserEid(userId);
           sakaiSession.setUserId(userId);
       }

   }

   public SecurityService getSecurityService() {
      return securityService;
   }

   public void setSecurityService(SecurityService securityService) {
      this.securityService = securityService;
   }

   public ReportsManager getReportsManager() {
      return reportsManager;
   }

   public void setReportsManager(ReportsManager reportsManager) {
      this.reportsManager = reportsManager;
   }

   public List getDefinitions() {
      return definitions;
   }

   public void setDefinitions(List definitions) {
      this.definitions = definitions;
   }

   public boolean isAutoDdl() {
      return autoDdl;
   }

   public void setAutoDdl(boolean autoDdl) {
      this.autoDdl = autoDdl;
   }

}
