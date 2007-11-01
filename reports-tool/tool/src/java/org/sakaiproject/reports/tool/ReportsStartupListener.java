package org.sakaiproject.reports.tool;

import org.sakaiproject.reports.service.ReportsManager;
import org.sakaiproject.component.cover.ComponentManager;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

/**
 * Created by IntelliJ IDEA.
 * User: johnellis
 * Date: Oct 30, 2007
 * Time: 9:16:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReportsStartupListener implements ServletContextListener {

   public void contextInitialized(ServletContextEvent servletContextEvent) {
      ReportsManager reportsManager = (ReportsManager) ComponentManager.get(ReportsManager.class);
      reportsManager.clientInit();
   }

   public void contextDestroyed(ServletContextEvent servletContextEvent) {

   }
}
