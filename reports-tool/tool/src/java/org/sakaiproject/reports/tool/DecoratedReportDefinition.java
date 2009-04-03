/**********************************************************************************
* $URL:https://source.sakaiproject.org/svn/osp/trunk/reports/tool/src/java/org/theospi/portfolio/reports/tool/DecoratedReportDefinition.java $
* $Id:DecoratedReportDefinition.java 9134 2006-05-08 20:28:42Z chmaurer@iupui.edu $
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
package org.sakaiproject.reports.tool;

import org.sakaiproject.reports.model.Report;
import org.sakaiproject.reports.model.ReportDefinition;
import org.sakaiproject.reports.model.ReportResult;
import org.sakaiproject.reports.tool.DecoratedReport;

/**
 * This class allows the ReportDefinition to interact with
 *
 */
public class DecoratedReportDefinition {

   /** The link to the main tool */
   private ReportsTool	reportsTool = null;

   /** The report definition to decorate */
   private ReportDefinition	reportDefinition = null;

   public DecoratedReportDefinition(ReportDefinition reportDefinition, ReportsTool reportsTool)
   {
      this.reportDefinition = reportDefinition;
      this.reportsTool = reportsTool;
   }

   public ReportDefinition getReportDefinition()
   {
      return reportDefinition;
   }

   /**
    * An action called from the JSP through the JSF framework.
    * @return String the next page
    */
   public String selectReportDefinition()
   {
      reportsTool.setWorkingReportDefinition(this);
      Report report = reportsTool.getReportsManager().createReport(reportDefinition);

      reportsTool.setWorkingReport(new DecoratedReport( reportsTool.getReportsManager().createReport(reportDefinition), reportsTool ));

      if(!reportDefinition.isUsesWizard()){

         //run the report and then send to results page
         reportsTool.getWorkingReport();

         report.setTitle(reportDefinition.getTitle());
         report.setDescription(reportDefinition.getDescription());
         report.setIsLive(true);

         ReportResult result = reportsTool.getReportsManager().generateResults(report);
         reportsTool.setWorkingResult(new DecoratedReportResult(result, reportsTool));

         //result.setResultId();
         //result = reportsTool.getReportsManager().loadResult(result);

         //result.setDescription(reportDefinition.getDescription());
         //result.setTitle(reportDefinition.getTitle());
         result.setIsSaved(false);

         //	make it the working result


         return ReportsTool.reportResultsPage;
      }
      return ReportsTool.createReportPage;
   }

   public String processDelete()
   {
      return reportsTool.processDeleteReportDef(this.getReportDefinition());
   }
}
