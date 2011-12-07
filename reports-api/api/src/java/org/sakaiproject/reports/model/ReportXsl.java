/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/msub/iu.edu/oncourse/trunk/reports/reports-api/api/src/java/org/sakaiproject/reports/model/ReportXsl.java $
 * $Id: ReportXsl.java 61316 2009-04-27 20:15:31Z chmaurer@iupui.edu $
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

package org.sakaiproject.reports.model;

import org.sakaiproject.metaobj.shared.model.Id;
import org.sakaiproject.reports.service.ResultsPostProcessor;
import org.sakaiproject.reports.model.ReportDefinition;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class ReportXsl implements ResourceLoaderAware
{

   /** The primary key */
    private Id	reportXslId = null;

    /** the link to the report definition */
    private ReportDefinition reportDefinition = null;

    /** whether or not this xsl is for export or view */
    private boolean isExport = false;

    /** the xsl location */
    private String xslLink;

    /** the title */
    private String title;

    /** the contentType */
    private String contentType;

    /** the extension */
    private String extension;

   private ResultsPostProcessor resultsPostProcessor;

   private String target = "_blank";

   private Resource resource;

    /**
     * the getter for the reportId property
     */
    public ReportXsl()
    {

    }

    public Id getReportXslId()
    {
        return reportXslId;
    }

    public void setReportXslId(Id reportXslId)
    {
        this.reportXslId = reportXslId;
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
        this.reportDefinition = reportDefinition;
    }


    /**
     * the getter for the isExport property
     * @return boolean the isExport
     */
    public boolean getIsExport()
    {
        return isExport;
    }


    /**
     * the setter for the isExport property.  This is set by the bean
     * and by hibernate.
     * @param isExport boolean
     */
    public void setIsExport(boolean isExport)
    {
        this.isExport = isExport;
    }


    /**
     * the getter for the xslLink property
     * @return String the xslLink
     */
    public String getXslLink()
    {
        return xslLink;
    }


    /**
     * the setter for the xslLink property.  This is set by the bean
     * and by hibernate.
     * @param xslLink String
     */
    public void setXslLink(String xslLink)
    {
        this.xslLink = xslLink;
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
     * the getter for the contentType property
     * @return String the contentType
     */
    public String getContentType()
    {
        return contentType;
    }


    /**
     * the setter for the contentType property.  This is set by the bean
     * and by hibernate.
     * @param contentType String
     */
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }


    /**
     * the getter for the extension property
     * @return String the extension
     */
    public String getExtension()
    {
        return extension;
    }


    /**
     * the setter for the extension property.  This is set by the bean
     * and by hibernate.
     * @param extension String
     */
    public void setExtension(String extension)
    {
        this.extension = extension;
    }

   public ResultsPostProcessor getResultsPostProcessor() {
      return resultsPostProcessor;
   }

   public void setResultsPostProcessor(ResultsPostProcessor resultsPostProcessor) {
      this.resultsPostProcessor = resultsPostProcessor;
   }

   /** return the singleton's object id, this will be unique and permanent until the next restart **/
   public String getRuntimeId() {
      return this.toString().hashCode() + "";
   }

   public String getTarget() {
      return target;
   }

   public void setTarget(String target) {
      this.target = target;
   }

   public Resource getResource() {
      return resource;
   }

   public void setResource(Resource resource) {
      this.resource = resource;
   }

   public void setResourceLoader(ResourceLoader resourceLoader) {
      setResource(resourceLoader.getResource(getXslLink()));
   }

}