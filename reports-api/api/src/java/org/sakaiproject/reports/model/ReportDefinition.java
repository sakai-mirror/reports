/**********************************************************************************
* $URL:https://source.sakaiproject.org/svn/osp/trunk/reports/api/src/java/org/theospi/portfolio/reports/model/ReportDefinition.java $
* $Id:ReportDefinition.java 9134 2006-05-08 20:28:42Z chmaurer@iupui.edu $
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

import org.sakaiproject.db.api.SqlService;
import org.sakaiproject.metaobj.shared.model.Id;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ReportDefinition
{
    private static final String DEFAULT_VIEW_LINK = "/reportxsls/default.xsl";

    /** the unique identifier for the report definition */
    private Id reportDefId = null;

    /** the unique identifier for the report definition */
    private String idString = null;

    private boolean usesWizard = true;
    /** the title of the report definition */
    private String title;

    /** the sql query for the report definition */
    private List query;

    private Map vendorQuery;
    /** the keyword for the report definition */
    private String keywords;

    /** the description for the report definition */
    private String description;

    /** the defaultXsl for the report definition */
    private ReportXsl defaultXsl;

    /** the exportXsl for the report definition */
    private String exportXsl;

   /** the link to the report parameters for the report definition */
   private List reportDefinitionParams;
   
   private List<ReportDefinitionParam> reportDefinitionProcessedParams;

   /** whether or not this report uses the datawarehouse, defaults to true */
   private Boolean usesWarehouse = Boolean.TRUE;

    /** the defaultXsl for the report definition */
    private List xsls;

    /** the type of report defining who can view the report definition and derived data */
    private String siteType;

    private List userRoles;

   /** list of any special result processors this report needs.
    * These should be of type ResultProcessor
    * @see org.sakaiproject.reports.service.ResultProcessor
    */
    private List resultProcessors;
    private String role;

    private boolean dbLoaded = false;

    private String paramTitle;

    private String paramInstruction;

    private String vendor;
    /**
     * when the report is finished loading the link in the report parameters
     * needs to be set to the owning report definition
     *
     */
    public void finishLoading()
    {
        if(reportDefinitionParams == null)
            return;
        Iterator iter = reportDefinitionParams.iterator();

        while(iter.hasNext()) {
            ReportDefinitionParam rdp = (ReportDefinitionParam) iter.next();

            rdp.setReportDefinition(this);
        }
    }


    /**
     * the getter for the reportDefId property
     * @return String the unique identifier
     */
    public Id getReportDefId()
    {
        return reportDefId;
    }


    /**
     * the setter for the reportDefId property.  This is set by the bean
     * and by hibernate.
     * @param reportDefId String
     */
    public void setReportDefId(Id reportDefId)
    {
        this.reportDefId = reportDefId;
    }

    /**
     * return the id as a string.  return the actual id if there is one then
     * the configured definition id if not
     * @return String
     */
    public String getIdString()
    {
        if(reportDefId == null)
            return idString;
        return reportDefId.getValue();
    }
    public void setIdString(String idString)
    {
        this.idString = idString;
    }

    public SqlService getSqlService() {
        return  org.sakaiproject.db.cover.SqlService.getInstance();
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
     * @param reportDefId String
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getVendor() {
        return getSqlService().getVendor();
    }

    /**
     * the getter for the query property
     * @return String the query
     */
    public List getQuery() {
        if (getVendorQuery() != null) {
            for (Iterator i = getVendorQuery().entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = (Map.Entry) i.next();
                String vendor = (String) entry.getKey();
                if (vendor.equals(getVendor())) {
                    List query = (List) entry.getValue();
                    return query;
                }
            }
        }
        return this.query;
    }


    /**
     * the setter for the query property.  This is set by the bean
     * and by hibernate.
     * @param query String
     */
    public void setQuery(List query)
    {
        this.query = query;
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
     * the getter for the defaultXsl property
     * @return ReportXsl the defaultXsl
     */
    public ReportXsl getDefaultXsl()
    {
        if(defaultXsl == null) {
            defaultXsl = new ReportXsl();
            defaultXsl.setIsExport(false);
            defaultXsl.setReportDefinition(this);
            defaultXsl.setXslLink(DEFAULT_VIEW_LINK);
        }
        return defaultXsl;
    }


    /**
     * the setter for the defaultXsl property.  This is set by the bean
     * and by hibernate.
     * @param defaultXsl ReportXsl
     */
    public void setDefaultXsl(ReportXsl defaultXsl)
    {
        this.defaultXsl = defaultXsl;
    }


    /**
     * the getter for the exportXsl property
     * @return String the exportXsl
     */
    public String getExportXsl()
    {
        return exportXsl;
    }


    /**
     * the setter for the exportXsl property.  This is set by the bean
     * and by hibernate.
     * @param exportXsl List
     */
    public void setExportXsl(String exportXsl)
    {
        this.exportXsl = exportXsl;
    }


    /**
     * the getter for the reportDefinitionParams property
     * @return List of ReportDefinitionParam
     */
    public List getReportDefinitionParams()
    {
        return reportDefinitionParams;
    }


    /**
     * the setter for the reportDefinitionParams property.  This is set by the bean
     * and by hibernate.
     * @param reportDefinitionParams List
     */
    public void setReportDefinitionParams(List reportDefinitionParams)
    {
        this.reportDefinitionParams = reportDefinitionParams;
    }


    public void setReportDefinitionProcessedParams(
			List<ReportDefinitionParam> reportDefinitionProcessedParams) {
		this.reportDefinitionProcessedParams = reportDefinitionProcessedParams;
	}


	public List<ReportDefinitionParam> getReportDefinitionProcessedParams() {
		return reportDefinitionProcessedParams;
	}


	/**
     * the getter for the xsls property
     * @return List the xsls
     */
    public List getXsls()
    {
        return xsls;
    }


    /**
     * the setter for the xsl property.  This is set by the bean
     * and by hibernate.
     * @param xsl List
     */
    public void setXsls(List xsls)
    {
        this.xsls = xsls;
    }


    /**
     * the getter for the type property
     * @return String the type
     */
    public String getSiteType()
    {
        return siteType;
    }


    /**
     * the setter for the type property.  This is set by the bean
     * and by hibernate.
     * @param keywords String
     */
    public void setSiteType(String type)
    {
        this.siteType = type;
    }

   /** list of any special result processors this report needs.
    * These should be of type ResultProcessor
    * @see org.sakaiproject.reports.service.ResultProcessor
    */
   public List getResultProcessors() {
      return resultProcessors;
   }

   public void setResultProcessors(List resultProcessors) {
      this.resultProcessors = resultProcessors;
   }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
   public ReportXsl findReportXsl(String link)
   {
      Iterator iter = xsls.iterator();

      while(iter.hasNext()) {
         ReportXsl xslInfo = (ReportXsl)iter.next();
         if(xslInfo.getXslLink() != null && xslInfo.getXslLink().equals(link))
            return xslInfo;
      }
      return null;
   }

   public ReportXsl findReportXslByRuntimeId(String runtimeId)
   {
      Iterator iter = xsls.iterator();

      while(iter.hasNext()) {
         ReportXsl xslInfo = (ReportXsl)iter.next();
         if(xslInfo.getXslLink() != null && xslInfo.getRuntimeId().equals(runtimeId))
            return xslInfo;
      }
      return null;
   }

public List getUserRoles() {
    return userRoles;
}


public void setUserRoles(List userRoles) {
    this.userRoles = userRoles;
}

public boolean isUsesWizard() {
    return usesWizard;
}


public void setUsesWizard(boolean usesWizard) {
    this.usesWizard = usesWizard;
}



   /**
    * Specifies whether or not this report is keyed on the data warehouse tables
    * @return
    */
   public Boolean getUsesWarehouse() {
      return usesWarehouse;
   }


   /**
    * sets whether or not this report is keyed on the data warehouse tables
    * @param usesWarehouse
    */
   public void setUsesWarehouse(Boolean usesWarehouse) {
      this.usesWarehouse = usesWarehouse;
   }

    public boolean isDbLoaded() {
        return dbLoaded;
    }

    public void setDbLoaded(boolean dbLoaded) {
        this.dbLoaded = dbLoaded;
    }

    public String getParamTitle() {
        return paramTitle;
    }

    public void setParamTitle(String paramTitle) {
        this.paramTitle = paramTitle;
    }

    public String getParamInstruction() {
        return paramInstruction;
    }

    public void setParamInstruction(String paramInstruction) {
        this.paramInstruction = paramInstruction;
    }

    public Map getVendorQuery() {
        return vendorQuery;
    }

    public void setVendorQuery(Map vendorQuery) {
        this.vendorQuery = vendorQuery;
    }
}
