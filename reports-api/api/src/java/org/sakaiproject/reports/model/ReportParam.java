/**********************************************************************************
* $URL:https://source.sakaiproject.org/svn/osp/trunk/reports/api/src/java/org/theospi/portfolio/reports/model/ReportParam.java $
* $Id:ReportParam.java 9134 2006-05-08 20:28:42Z chmaurer@iupui.edu $
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
package org.sakaiproject.reports.model;

import org.sakaiproject.metaobj.shared.model.Id;
import org.sakaiproject.reports.model.Report;
import org.sakaiproject.reports.model.ReportDefinitionParam;

import java.util.List;
import java.util.ArrayList;
public class ReportParam
{
	/** the identifier to the report paramater */
	private Id paramId = null;
	
	/** the identifier to the report definition for the paramater */
	private Report report = null;
	
	/** the reportDefParamId for the report definition parameter */
	private ReportDefinitionParam reportDefinitionParam = null;


	/** the type for the report definition Parameter 
	 * 	This is validation rules for fillin parameters,
	 * 	a set of strings for sets (both value and title),
	 * and the query if the value type is a sql query
	 */
	private String value = null;

	/** true when the value passes the test and false when the value changes */
	private boolean	validated = false;
	
	/** the results of the last validation */
	private boolean	valid = false;
	
	private String	reportDefParamIdMark = null;
	
    private List listValue = null;
	/**
	 * the getter for the paramId property
	 * @return String the unique identifier
	 */
	public Id getParamId()
	{
		return paramId;
	}
	
	
	/**
	 * the setter for the paramId property.  This is set by the bean 
	 * and by hibernate.
	 * @param paramId String
	 */
	public void setParamId(Id paramId)
	{
		this.paramId = paramId;
	}
	
	
	/**
	 * the getter for the report property
	 * @return String the unique identifier
	 */
	public Report getReport()
	{
		return report;
	}
	
	
	/**
	 * the setter for the report property.  This is set by the bean 
	 * and by hibernate.
	 * @param report Report
	 */
	public void setReport(Report report)
	{
		this.report = report;
	}
	
	/**
	 * the getter for the reportDefinitionParam property
	 * @return ReportDefinitionParam the unique identifier
	 */
	public ReportDefinitionParam getReportDefinitionParam()
	{
		return reportDefinitionParam;
	}
	
	
	/**
	 * the setter for the reportDefinitionParam property.  This is set by the bean 
	 * and by hibernate.
	 * @param reportDefinitionParam String
	 */
	public void setReportDefinitionParam(ReportDefinitionParam reportDefinitionParam)
	{
		this.reportDefinitionParam = reportDefinitionParam;
	}
	
	
	
	/**
	 * This is a way of separating the report definition from the report in the database
	 * this is a temp solution while the report definitions aren't being stored in the database
	 * @return String
	 */
	public String getReportDefParamIdMark()
	{
		if(reportDefinitionParam == null)
			return reportDefParamIdMark;
		return reportDefinitionParam.getIdString();
	}
	
	/**
	 * this is the link to report definition
	 * @param reportDefIdMark String
	 */
	public void setReportDefParamIdMark(String reportDefParamIdMark)
	{
		reportDefinitionParam = null;
		this.reportDefParamIdMark = reportDefParamIdMark;
	}
	
	
	/**
	 * the getter for the value property
	 * @return String the value
	 */
    public String getValue()
    {
        if (value == null && getListValue() != null){
            value = getListValue().toString();
        }
        return value;
    }
	
	
	/**
	 * the setter for the value property.  This is set by the bean or the user 
	 * and by hibernate.
	 * @param value String
	 */
	public void setValue(String value)
	{
		this.value = value;
		validated = false;
	}
	
	
	/**
	 * Checks to make sure that the value can be selected.
	 * Apply validation rules, check against set, check against the sql results
	 * @return boolean
	 */
	public boolean valid()
	{
		if(value == null)
			return false;
		if(!validated) {
			valid = true;
			
			//do the check here
			
			validated = true;
		}
		return valid;
    }

    public List getListValue() {
        if (listValue == null && this.value != null){
            initListValue(this.value);
        }
        return listValue;
    }

    public void setListValue(List listValue) {
        this.listValue = listValue;
        validated = false;
    }
    public void initListValue(String value){
    	if(value==null || ((value.indexOf("[")<0) || (value.indexOf("]")<0)))
    		return;
        String strSet = value.substring(value.indexOf("[")+1, value.indexOf("]"));
	    String[] set = strSet.split(",");
		List valueList = new ArrayList();
			for(int i = 0; i < set.length; i++) {
				valueList.add(set[i].trim());
            }
        setListValue(valueList);
    }
}