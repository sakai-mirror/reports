/**********************************************************************************
* $URL$
* $Id$
***********************************************************************************
*
* Copyright (c) 2005, 2006 The Sakai Foundation.
*
* Licensed under the Educational Community License, Version 1.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.opensource.org/licenses/ecl1.php
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