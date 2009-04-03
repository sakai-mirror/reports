/**********************************************************************************
* $URL:https://source.sakaiproject.org/svn/osp/trunk/reports/api/src/java/org/theospi/portfolio/reports/model/Report.java $
* $Id:Report.java 9134 2006-05-08 20:28:42Z chmaurer@iupui.edu $
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

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.sakaiproject.metaobj.shared.model.Id;

/**
 * After loading a report from the database you must call connectToDefinition 
 * to connect to one of a list of report definitions or setReportDefinition for a specific
 * Report Definition.  These are good for setting report definitions from the config file and
 * from the database, respectively.
 * @author andersjb
 *
 */
public class Report
{
	/** the unique identifier for the report */
	private Id reportId;
	
	/** the link to the report definition */
	private ReportDefinition reportDefinition = null;


    private ReportDefinitionXmlFile reportDefitionXmlFile = null;
    /** the database link to the report definition */
	private String	reportDefIdMark = null;

	/** the owner of the report */
	private String userId;

	/** the title of the report */
	private String title;

	/** the keyword for the report */
	private String keywords;

	/** the description for the report */
	private String description;

	/** the parameters for the query in the report */
	private boolean isLive;
	
	private ReportResult liveResult = null;

	/** the defaultXsl for the report */
	private Date creationDate;

	/** the type of report */
	private String type;
	
	/** the list of report parameters for the report */
	private List reportParams;
	
	/** when the report is live it matters if the report is saved or not */
	private boolean isSaved = false;
   
   /** when the report is active can show up in the list of live reports */
   private boolean display = true;

	/**
	 * the getter for the reportId property
	 */
	public Report()
	{
		
	}
	
	
	/**
	 * the getter for the reportId property
	 */
	
	public Report(ReportDefinition reportDefinition)
	{
		setReportDefinition(reportDefinition);
	}

    public ReportDefinitionXmlFile getReportDefitionXmlFile() {
        return reportDefitionXmlFile;
    }

    public void setReportDefitionXmlFile(ReportDefinitionXmlFile reportDefitionXmlFile) {
        this.reportDefitionXmlFile = reportDefitionXmlFile;
    }

    /**
	 * the getter for the reportId property
	 * @return String the unique identifier
	 */
	public Id getReportId()
	{
		return reportId;
	}
	
	
	/**
	 * the setter for the reportId property.  This is set by the bean 
	 * and by hibernate.
	 * @param reportId String
	 */
	public void setReportId(Id reportId)
	{
		this.reportId = reportId;
	}
	/**
	 * the getter for the reportDefinition property
	 * @return ReportDefinition the unique identifier
	 */
	public ReportDefinition getReportDefinition()
	{
		return reportDefinition;
	}
	
	
	/**
	 * the setter for the reportDefinition property.  This is set by the bean 
	 * and by hibernate.
	 * @param reportDefinition String
	 */
	public void setReportDefinition(ReportDefinition reportDefinition)
	{
		if(this.reportDefinition != null && reportDefinition != this.reportDefinition)
			throw new RuntimeException("A report cannot change it's report definition");
		
		this.reportDefinition = reportDefinition;
		type = reportDefinition.getSiteType();
	}
	
	
	
	/**
	 * This is a way of separating the report definition from the report in the database
	 * this is a temp solution while the report definitions aren't being stored in the database
	 * @return String
	 */
	public String getReportDefIdMark()
	{
		if(reportDefinition == null)
			return reportDefIdMark;
		return reportDefinition.getIdString();
	}
	
	/**
	 * this is the link to report definition
	 * @param reportDefIdMark String
	 */
	public void setReportDefIdMark(String reportDefIdMark)
	{
		if(reportDefinition != null)
			if(!reportDefIdMark.equals(reportDefinition.getIdString()))
				reportDefinition = null;
		this.reportDefIdMark = reportDefIdMark;
	}
	
	/**
	 * this is links this report to the report definition.
	 * It searches for the definition and if found it then links the
	 * report parameters to the report definition parameters
	 * @param reportDefIdMark String
	 */
	public void connectToDefinition(List reportDefs)
	{
        List paramList = new ArrayList();

        reportParams.size();
		if(reportDefIdMark != null && reportDefinition == null) {
			Iterator iter = reportDefs.iterator();
			
			while(iter.hasNext()) {
				ReportDefinition rd = (ReportDefinition)iter.next();
				if(rd.getIdString().equals(reportDefIdMark)){
					reportDefinition = rd;
					break;
				}
			}
			if(reportDefinition != null) {
				iter = reportDefinition.getReportDefinitionParams().iterator();
				while(iter.hasNext()) {
                    ReportDefinitionParam rdp = (ReportDefinitionParam)iter.next();

					Iterator paramIter = this.getReportParams().iterator();
					while(paramIter.hasNext()) {
                        ReportParam rp = (ReportParam)paramIter.next();
						if(rp.getReportDefParamIdMark().equals(rdp.getIdString())) {
							rp.setReportDefinitionParam(rdp);
							rp.setReport(this);
                            paramList.add(rp);
						}
					}
				}// end while(looping through report defs)
			}
            // re-ordering params to match the order of the report def
            reportParams = paramList;
        }
	}
	
	
	/**
	 * the getter for the userId property
	 * @return String the userId
	 */
	public String getUserId()
	{
		return userId;
	}
	
	
	/**
	 * the setter for the userId property.  This is set by the bean 
	 * and by hibernate.
	 * @param userId String
	 */
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	
	
	/**
	 * the getter for the title property
	 * @return String the title
	 */
	public String getTitle()
	{
		return title;
	}
	
	
	/**
	 * the setter for the title property.  This is set by the bean 
	 * and by hibernate.
	 * @param title String
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	
	/**
	 * the getter for the keywords property
	 * @return String the keywords
	 */
	public String getKeywords()
	{
		return keywords;
	}
	
	
	/**
	 * the setter for the keywords property.  This is set by the bean 
	 * and by hibernate.
	 * @param keywords String
	 */
	public void setKeywords(String keywords)
	{
		this.keywords = keywords;
	}
	
	
	/**
	 * the getter for the description property
	 * @return String the description
	 */
	public String getDescription()
	{
		return description;
	}
	
	
	/**
	 * the setter for the description property.  This is set by the bean 
	 * and by hibernate.
	 * @param description String
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	
	/**
	 * the getter for the isLive property
	 * @return String the isLive
	 */
	public boolean getIsLive()
	{
		return isLive;
	}
	
	
	/**
	 * the setter for the isLive property.  This is set by the bean 
	 * and by hibernate.
	 * @param isLive List
	 */
	public void setIsLive(boolean isLive)
	{
		this.isLive = isLive;
	}
	
	
	/**
	 * the getter for the creationDate property
	 * @return Date the creationDate
	 */
	public Date getCreationDate()
	{
		return creationDate;
	}
	
	
	/**
	 * the setter for the creationDate property.  This is set by the bean 
	 * and by hibernate.
	 * @param params Date
	 */
	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}
	
	
	/**
	 * the getter for the type property
	 * @return String the type
	 */
	public String getType()
	{
		return type;
	}
	
	
	/**
	 * the setter for the type property.  This is set by the bean 
	 * and by hibernate.
	 * @param keywords String
	 */
	public void setType(String type)
	{
		this.type = type;
	}
	
	
	/**
	 * the getter for the reportParams property
	 * @return List the reportParams
	 */
	public List getReportParams()
	{
		return reportParams;
	}
	
	
	/**
	 * the setter for the reportParams property.  This is set by hibernate.
	 * @param reportParams List
	 */
	public void setReportParams(List reportParams)
	{
		this.reportParams = reportParams;
    }
	
	
	/**
	 * the getter for the isSaved property
	 * @return boolean the isSaved
	 */
	public boolean getIsSaved()
	{
		return isSaved;
	}
	
	
	/**
	 * the setter for the isSaved property.  This is set by the bean 
	 * and by hibernate.
	 * @param isSaved boolean
	 */
	public void setIsSaved(boolean isSaved)
	{
		this.isSaved = isSaved;
	}
   
   
   /**
    * the getter for the active property
    * @return String the active
    */
   public boolean getDisplay()
   {
      return display;
   }
   
    
   /**
    * the setter for the active property.  This is set by hibernate.
    * @param active boolean
    */
   public void setDisplay(boolean display)
   {
      this.display = display;
   }
}