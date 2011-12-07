/**********************************************************************************
* $URL: https://source.sakaiproject.org/svn/msub/iu.edu/oncourse/trunk/reports/reports-tool/tool/src/java/org/sakaiproject/reports/tool/DecoratedAbstractResult.java $
* $Id: DecoratedAbstractResult.java 61316 2009-04-27 20:15:31Z chmaurer@iupui.edu $
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

import java.util.Date;

public interface DecoratedAbstractResult
{
	public static final String REPORT = "report";
	public static final String RESULT = "result";
	
	public String getResultType();
	public String getTitle();
	public Date getCreationDate();
	public boolean getIsLive();
   public String processSelectReportResult();
   public String processDelete();
}