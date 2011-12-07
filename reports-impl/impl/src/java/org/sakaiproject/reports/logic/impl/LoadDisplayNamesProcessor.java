/**********************************************************************************
* $URL:https://source.sakaiproject.org/svn/osp/trunk/reports/api-impl/src/java/org/theospi/portfolio/reports/model/impl/LoadArtifactResultProcessor.java $
* $Id:LoadArtifactResultProcessor.java 9134 2006-05-08 20:28:42Z chmaurer@iupui.edu $
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
package org.sakaiproject.reports.logic.impl;

import org.jdom.Document;
import org.jdom.Element;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.exception.TypeException;
import org.sakaiproject.reports.model.ReportResult;
import org.sakaiproject.reports.logic.impl.BaseResultProcessor;
import org.sakaiproject.metaobj.security.impl.AllowAllSecurityAdvisor;

import java.util.Iterator;
import java.util.List;

/**
 * To use this class you need to place the resource UUID into a column of the report query.
 * The column needs to be then labeled with the ending "_DISPLAYNAME".  Then include this class
 * as a post processor for a report.  The result of this post processing class is to place 
 * the display name into the column.
 *                
 * Keep in mind the case of the column labels.  the default behavior is to uppercase the 
 * labels to standardize labels between various databases.  Thus if you turn off the forcing
 * of upper case then the "_artifact" label ending needs to be either all lowercase or 
 * all uppercase.
 * 
 * Created by IntelliJ IDEA.
 * User: John Ellis
 * Date: Dec 22, 2005
 * Time: 5:31:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoadDisplayNamesProcessor extends BaseResultProcessor {

   private String columnNamePattern = ".*_displayname$";
   private SecurityService securityService;
   private ContentHostingService contentHosting;

   /**
    * Post Processor method
    */
   public ReportResult process(ReportResult result) {
      Document rootDoc = getResults(result);
      
      List<Element> data = rootDoc.getRootElement().getChild("data").getChildren("datarow");
      
      getSecurityService().pushAdvisor(new AllowAllSecurityAdvisor());
      
      for (Iterator<Element> i=data.iterator();i.hasNext();) {
         Element dataRow = (Element)i.next();
         processRow(dataRow);
      }

      getSecurityService().popAdvisor();

      return setResult(result, rootDoc);
   }

   /**
    * find the artifact columns, stores them into a map (artifactsToLoad)
    * also puts the column into a list so as the artifact is loaded into it.
    * @param dataRow
    * @param artifactsToLoad
    */
   protected void processRow(Element dataRow) {
      List<Element> columns = dataRow.getChildren("element");

      for (Iterator<Element> i=columns.iterator();i.hasNext();) {
         Element data = (Element) i.next();
         if (isArtifactColumn(data) && !isColumnNull(data)) {

            String displayName = "";
            try {
               String id = getContentHosting().resolveUuid(getColumnData(data));
               ContentResource resource = getContentHosting().getResource(id);
               
               // this code comes from the ResourceTypePropertyAccess.getPropertyValue
               String propName = resource.getProperties().getNamePropDisplayName();
               displayName = resource.getProperties().getProperty(propName);
               
            } catch (PermissionException e) {
               logger.error("", e);
            } catch (IdUnusedException e) {
               logger.error("", e);
            } catch (TypeException e) {
               logger.error("", e);
            }
            
            data.setText(displayName);
         }
      }
   }


   protected String getColumnData(Element data) {
      return data.getTextNormalize();
   }

   protected boolean isArtifactColumn(Element data) {
      String columnName = data.getAttributeValue("colName");
      return columnName.matches(getColumnNamePattern());
   }

   public String getColumnNamePattern() {
      return columnNamePattern;
   }

   public void setColumnNamePattern(String columnNamePattern) {
      this.columnNamePattern = columnNamePattern;
   }

   public SecurityService getSecurityService() {
      return securityService;
   }

   public void setSecurityService(SecurityService securityService) {
      this.securityService = securityService;
   }

   public ContentHostingService getContentHosting() {
	   return contentHosting;
   }

   public void setContentHosting(ContentHostingService contentHosting) {
	   this.contentHosting = contentHosting;
   }

}
