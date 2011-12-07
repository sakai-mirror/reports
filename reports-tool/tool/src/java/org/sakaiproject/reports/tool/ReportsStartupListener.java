/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/msub/iu.edu/oncourse/trunk/reports/reports-tool/tool/src/java/org/sakaiproject/reports/tool/ReportsStartupListener.java $
 * $Id: ReportsStartupListener.java 61316 2009-04-27 20:15:31Z chmaurer@iupui.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2007, 2008 The Sakai Foundation
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
