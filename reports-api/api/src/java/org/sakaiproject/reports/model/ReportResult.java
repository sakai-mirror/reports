/**********************************************************************************
* $URL:https://source.sakaiproject.org/svn/osp/trunk/reports/api/src/java/org/theospi/portfolio/reports/model/ReportResult.java $
* $Id:ReportResult.java 9134 2006-05-08 20:28:42Z chmaurer@iupui.edu $
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

import org.sakaiproject.metaobj.shared.model.Id;
import org.sakaiproject.reports.model.Report;

public class ReportResult
{
   /** the unique identifier for the report definition */
   private Id resultId;

   /** the unique identifier for the report definition */
   private Report report;

   /** the owner of the report */
   private String userId;

   /** the title of the report definition */
   private String title;

   /** the keyword for the report definition */
   private String keywords;

   /** the description for the report definition */
   private String description;

   /** the parameters for the query in the report definition */
   private Date creationDate;

   /** the defaultXsl for the report definition */
   private String xml;

   /** tells whether or not the result has been saved to the database */
   private boolean isSaved = false;

    private boolean isOwner = false;

   /**
    * the getter for the resultId property
    * @return String the unique identifier
    */
   public Id getResultId()
   {
      return resultId;
   }


   /**
    * the setter for the resultId property.  This is set by the bean 
    * and by hibernate.
    * @param resultId String
    */
   public void setResultId(Id resultId)
   {
      this.resultId = resultId;
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
    * @param report String
    */
   public void setReport(Report report)
   {
      this.report = report;
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
    * @param reportDefId String
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
    * the getter for the creationDate property
    * @return List the creationDate
    */
   public Date getCreationDate()
   {
      return creationDate;
   }


   /**
    * the setter for the creationDate property.  This is set by the bean 
    * and by hibernate.
    * @param params List
    */
   public void setCreationDate(Date creationDate)
   {
      this.creationDate = creationDate;
   }


   /**
    * the getter for the xml property
    * @return String the xml
    */
   public String getXml()
   {
      return xml;
   }


   /**
    * the setter for the xml property.  This is set by the bean 
    * and by hibernate.
    * @param xml List
    */
   public void setXml(String xml)
   {
      this.xml = xml;
   }


   /**
    * the getter for the isSaved property
    * @return String the isSaved
    */
   public boolean getIsSaved()
   {
      return isSaved;
   }


   /**
    * the setter for the isSaved property.  This is set by the bean 
    * and by hibernate.
    * @param isSaved List
    */
   public void setIsSaved(boolean isSaved)
   {
      this.isSaved = isSaved;
   }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }
}