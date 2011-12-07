/**********************************************************************************
* $URL: https://source.sakaiproject.org/svn/msub/iu.edu/oncourse/trunk/reports/reports-api/api/src/java/org/sakaiproject/reports/service/ReportFunctions.java $
* $Id: ReportFunctions.java 61316 2009-04-27 20:15:31Z chmaurer@iupui.edu $
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

/**
 *   These are the permissions for reporting.  Reporting uses the sakai permission manager
 *   and not the osp permission manager.  Meta object uses the sakai permission manager as well.
 *   The labels on the page are drawn from the function string minus the prefix.
 *   
 *   Apparently, when labelling a permission with more than one word, then a period is used as 
 *   the spacer.
 *
 * User: John Ellis
 * Date: Jan 7, 2006
 * Time: 12:43:30 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ReportFunctions {
   public static final String REPORT_FUNCTION_PREFIX = "reports.";
   
   public static final String REPORT_FUNCTION_CREATE = REPORT_FUNCTION_PREFIX + "create";
   public static final String REPORT_FUNCTION_RUN = REPORT_FUNCTION_PREFIX + "run";
   public static final String REPORT_FUNCTION_VIEW = REPORT_FUNCTION_PREFIX + "view";
   public static final String REPORT_FUNCTION_EDIT = REPORT_FUNCTION_PREFIX + "edit";
   public static final String REPORT_FUNCTION_DELETE = REPORT_FUNCTION_PREFIX + "delete";
   public static final String REPORT_FUNCTION_SHARE = REPORT_FUNCTION_PREFIX + "share";
}
