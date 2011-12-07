/**********************************************************************************
* $URL:https://source.sakaiproject.org/svn/osp/trunk/reports/api/src/java/org/theospi/portfolio/reports/model/ReportsManager.java $
* $Id:ReportsManager.java 9134 2006-05-08 20:28:42Z chmaurer@iupui.edu $
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
package org.sakaiproject.reports.service;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.sakaiproject.exception.ImportException;
import org.sakaiproject.exception.UnsupportedFileTypeException;
import org.sakaiproject.metaobj.shared.DownloadableManager;
import org.sakaiproject.metaobj.shared.model.Id;
import org.sakaiproject.reports.model.*;
import org.sakaiproject.reports.logic.impl.ReportsDefinitionWrapper;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ReportsManager extends DownloadableManager, Job
{
   public static final String RESULTS_ID = "reportResultsId";
   public static final String EXPORT_XSL_ID = "reportExportId";
   public static final String reportGroup = "REPORTS";

   public static final String REPORTS_MESSAGE_BUNDLE = "org.sakaiproject.reports.bundle.Messages";

   public void addReportDefinition(ReportsDefinitionWrapper reportDef);
   

   /**
    * Sets the list of ReportDefinitions.  It also iterates through the list
    * and tells the report definition to complete it's loading.
    * @param reportDefinitions List of reportDefinitions
    */
    public void setReportDefinitions(List reportdefs);


   /**
    * Returns the ReportDefinitions.  The list returned is filtered
    * for the worksite type against the report types
    * @return List of ReportDefinitions
    */
    public List getReportDefinitions();


   /**
    * Creates parameters in the report linked to the parameters in the report definition
    * 
    * @param report a Collection of ReportParam
    */
    public void createReportParameters(Report report);


   /**
    * Creates a new blank Report based on a report definition
    * 
    * @param reportDefinition a Collection of ReportParam
    */
    public Report createReport(ReportDefinition reportDefinition);


   /**
    * runs a report and creates a ReportResult.  The parameters were
    * verified on the creation of this report object.
    * @return ReportResult
    */
    public ReportResult generateResults(Report report);


   /**
    * Replaces the the system value proxy with the values.
    * The list of system value proxies(without quote characters):
    * "{userid}", "{userdisplayname}", "{useremail}", 
    * "{userfirstname}", "{userlastname}", "{worksiteid}", 
    * "{toolid}", 
    * @param inString
    * @return String with replaced values
    */
    public String replaceSystemValues(String inString);


   /**
    * gathers the data for dropdown/list box.  It runs the query defined in the report param
    * ane creates a string in the format "[value1, value2, value3, ...]" or 
    * "[(value1; title1), (value2; title2), (value3; title3), ...]"
    * @return String
    */
   public String generateSQLParameterValue(ReportParam reportParam, List<ReportParam> reportParams);


   /**
    * Takes a report result and an xsl and transforms the results
    * @param reportResult ReportResult
    * @param xslFile String to xsl resource
    * @return String
    */
   public String transform(ReportResult result, ReportXsl reportXsl);


   /**
    * saves the report into the database
    * @param result
    */
   public void saveReport(Report report);


   /**
    * saves the embedded report and then saves the report result
    * @param result
    */
   public void saveReportResult(ReportResult result);


   /**
    * this gets the list of report results that a user can view.
    * If the user has permissions to run or view reports, 
    *   then this grabs the ReportResults
    * If the user has permissions to run reports,
    *   then this grabs the Live Reports
    * 
    * @return List of ReportResult objects
    */
   public List getCurrentUserResults();


   /**
    * Reloads a ReportResult.  During the loading process it loads the
    * report from which the ReportResult is derived, It links the report to
    * the report definition, and sets the report in the report result.
    * @param ReportResult result
    * @return ReportResult
    */
   public ReportResult loadResult(ReportResult result);


   /**
    * Generates a unique id for a reference
    * @param result
    * @param ref
    * @return
    */
   public String getReportResultKey(ReportResult result, String ref);


   /**
    * checks the id against the generated unique id of the reference.
    * It throws an AuthorizationFailedException if the ids don't match.
    * Otherwise it adds the AllowAllSecurityAdvisor to the securityService
    * @param id
    * @param ref
    */
   public void checkReportAccess(String id, String ref);

   /**
    * Checks for edit report permission
    *
    */
   public void checkEditAccess();


   /**
    * Puts the ReportResult into the session
    * @param result
    */
   public void setCurrentResult(ReportResult result);

   /**
    * Deletes a ReportResult. If the report that this result came from in not on display
    * then we should try to delete the report too because the user can't do anything with it
    * @param result
    */
   public void deleteReportResult(ReportResult result);


   /**
    * if we are deleting a report that is not live, then delete the result associated with it
    * because it will become invalid.  If a report is live, then we need to check how many results
    * are linked to the report.  If there are no results then we can delete it, otherwise we need to
    * just disable the report from showing in the interface given the display parameter option.  
    * aka, if a report is live and has results associated, the  display parameter decides if we should deactivated 
    * the report.
    * 
    * @param report Report
    */
   public void deleteReport(Report report, boolean deactivate);


   /**
    * 
    * @return Map
    */
   public Map getAuthorizationsMap();

   /**
    * 
    * @return boolean
    */
   public boolean isMaintaner();

   /**
    * 
    * This gets the datasource that the report manager is configured to use.
    * it may be ambiguous if the data source is set to 3 (aka. both DW reports and live reports)
    * @return
    */
   //public DataSource getDataSource();


   /**
    * returns the data source based on if the calling code is using the warehouse or not
    * @param useWarehouse boolean
    * @return
    */
   public DataSource getDataSourceUseWarehouse(boolean useWarehouse);

    public boolean importResource(Id worksite, String reference)
            throws UnsupportedFileTypeException, ImportException;

     public void deleteReportDefXmlFile(ReportDefinition reportDef);

    public List getReportsByViewer();

     public String processSaveResultsToResources(ReportResult reportResult) throws IOException;
     public JobDetail processCreateJob(Report report);

   void clientInit();
}