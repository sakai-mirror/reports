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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.jdom.Document;
import org.jdom.Element;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.exception.TypeException;
import org.sakaiproject.metaobj.shared.ArtifactFinder;
import org.sakaiproject.metaobj.shared.ArtifactFinderManager;
import org.sakaiproject.metaobj.shared.EntityContextFinder;
import org.sakaiproject.metaobj.shared.mgt.IdManager;
import org.sakaiproject.metaobj.shared.mgt.PresentableObjectHome;
import org.sakaiproject.metaobj.shared.model.Artifact;
import org.sakaiproject.metaobj.shared.model.Id;
import org.sakaiproject.reports.model.ReportResult;
import org.sakaiproject.reports.service.ReportsManager;
import org.sakaiproject.reports.logic.impl.BaseResultProcessor;
import org.sakaiproject.metaobj.security.impl.AllowAllSecurityAdvisor;

/**
 * To use this class you need to place the resource UUID into a column of the report query.
 * The column needs to be then labeled with the ending "_ARTIFACT".  Then include this class
 * as a post processor for a report.  The result of this post processing class is to place 
 * the xml for the structured artifact (form) data instance into the internal xml format 
 * element that contains the uuid of the resource thus replacing the uuid with the xml data.  
 * For regular files it will put the file info into the node.
 * 
 * The type of report (warehouse or direct db) is placed into the internal xml.  This reads 
 * that and will load resources based on this.
 * 
 * For warehouse reports you need to get this data in this example column label
 *                select id `SOMELABEL_artifact` from dw_resource
 *                
 * For direct db reports you need to get this data in this example column label
 *                select RESOURCE_UUID `UUID_ARTIFACT` from content_resource
 * 
 * (These examples are for you to figure out which columns you can pull to make this class work)
 * (When linking to resources in other tools these are the fields you need)
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
public class LoadArtifactResultProcessor extends BaseResultProcessor {

   private IdManager idManager;
   private String columnNamePattern = ".*_artifact$";
   private ArtifactFinderManager artifactFinderManager;
   private SecurityService securityService;
   private ReportsManager reportsManager;
   private ContentHostingService contentHosting;

   /**
    * Post Processor method
    */
   public ReportResult process(ReportResult result) {
      Document rootDoc = getResults(result);
      Map<String, ArtifactHolder> artifactsToLoad = new Hashtable<String, ArtifactHolder>();

      List<Element> data = rootDoc.getRootElement().getChild("data").getChildren("datarow");

      Element isWarehouseReportElement = rootDoc.getRootElement().getChild("isWarehouseReport");

      boolean isWarehouseReport = Boolean.valueOf(isWarehouseReportElement.getText());

      for (Iterator<Element> i=data.iterator();i.hasNext();) {
         Element dataRow = (Element)i.next();
         processRow(dataRow, artifactsToLoad);
      }

      // open the security to access all resources so we can get the types and the data
      getSecurityService().pushAdvisor(new AllowAllSecurityAdvisor());

      loadArtifactTypes(artifactsToLoad, isWarehouseReport);

      for (Iterator<ArtifactHolder> i=artifactsToLoad.values().iterator();i.hasNext();) {
         ArtifactHolder holder = (ArtifactHolder) i.next();
         loadArtifact(result, holder);
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
   protected void processRow(Element dataRow, Map<String, ArtifactHolder> artifactsToLoad) {
      List<Element> columns = dataRow.getChildren("element");

      for (Iterator<Element> i=columns.iterator();i.hasNext();) {
         Element data = (Element) i.next();
         if (isArtifactColumn(data) && !isColumnNull(data)) {
            Id artifactId = getIdManager().getId(getColumnData(data));
            String type = getColumnType(data, dataRow);
            ArtifactHolder holder =
                  (ArtifactHolder) artifactsToLoad.get(artifactId.getValue());
            if (holder == null) {
               holder = new ArtifactHolder();
               holder.artifactId = artifactId;
               holder.artifactType = type;
               artifactsToLoad.put(artifactId.getValue(), holder);
            }
            holder.reportElements.add(data);
         }
      }
   }

   /**
    * This loads the artifact type into the Map of ArtifactHolders if the type
    * does not exist.  This method packs a bunch of artifact Ids together before
    * going out and getting their associated types.  Where there are 100 types to
    * be pulled, it loads the types.  It does this so as to not have the list of ids
    * be larger than what the database can handle.  Some limit the size of a IN (...)
    * to 1000 elements.  Others may have smaller or larger limits.
    * 
    * ERROR: the list of artifact Ids can only be 1000 elements in size, restriction of Oracle
    * @param artifactsToLoad
    */
   protected void loadArtifactTypes(Map<String, ArtifactHolder> artifactsToLoad, boolean useWarehouse) {
      String artifactIds = "";
      int numFound = 0;
      if(useWarehouse) {
         for (Iterator<ArtifactHolder> i=artifactsToLoad.values().iterator();i.hasNext();) {
            ArtifactHolder holder = (ArtifactHolder) i.next();
            if (holder.artifactType == null) {
               if (numFound != 0) {
                  artifactIds += ",";
               }
               numFound++;
               artifactIds += "'" + holder.artifactId.getValue() + "'";

               if(numFound >= 100) {
                  loadArtifactTypes(artifactIds, artifactsToLoad);
                  numFound = 0;
                  artifactIds = "";
               }
            }
         }

         if (numFound > 0) {
            loadArtifactTypes(artifactIds, artifactsToLoad);
         }
      } else {
         for (Iterator<ArtifactHolder> i=artifactsToLoad.values().iterator();i.hasNext();) {
            ArtifactHolder holder = (ArtifactHolder) i.next();
            if (holder.artifactType == null) {

               //String uri = ContentHostingService.resolveUuid(holder.artifactId.getValue());
               try {
                  ContentResource resource = getContentHosting().getResource(holder.artifactId.getValue());

                  // this code comes from the ResourceTypePropertyAccess.getPropertyValue
                  String propName = resource.getProperties().getNamePropStructObjType();
                  String saType = resource.getProperties().getProperty(propName);
                  if (saType != null) {
                     holder.artifactType = saType;
                  }else {
                     holder.artifactType = "fileArtifact";
                  }

               } catch (PermissionException e) {
                  logger.error("", e);
               } catch (IdUnusedException e) {
                  logger.error("", e);
               } catch (TypeException e) {
                  logger.error("", e);
               }


            }
         }


      }


   }


   protected void loadArtifact(ReportResult results, ArtifactHolder holder) {

      ArtifactFinder finder = getArtifactFinderManager().getArtifactFinderByType(holder.artifactType);

      Artifact art;

      if (finder instanceof EntityContextFinder) {
         String uri = getContentHosting().resolveUuid(holder.artifactId.getValue());

         String hash = getReportsManager().getReportResultKey(
               results, getContentHosting().getReference(uri));
         art = ((EntityContextFinder)finder).loadInContext(holder.artifactId,
            ReportsEntityProducer.REPORTS_PRODUCER,
            holder.artifactId.getValue(), hash);
      }
      else {
         art = finder.load(holder.artifactId);
      }
      if(art != null) {
         PresentableObjectHome home = (PresentableObjectHome)art.getHome();
         Element xml = home.getArtifactAsXml(art);

         // replace the artifact uuid with the actual xml
         for (Iterator<Element> i=holder.reportElements.iterator();i.hasNext();) {
            xml = (Element)xml.clone();
            Element element = (Element) i.next();
            element.removeContent();
            element.addContent(xml);

            // each element can only have one parent
            //xml = (Element)xml.clone();
         }
      } else {
         logger.debug("Artifact '" + holder.artifactId.toString() + "' of type '" + holder.artifactType + "' was not loaded");
      }
   }

   /**
    * The calling function has the responsibility of limiting the string of artifactIds to less than 1000
    * Oracle can only do 1000.  it should be limited to maybe 100 at a time!
    * @param artifactIds  String
    * @param artifactsToLoad Map
    */
   protected void loadArtifactTypes(String artifactIds, Map<String, ArtifactHolder> artifactsToLoad) {
      Connection conn = null;
      ResultSet rs = null;
      try {
         conn = getDataSource().getConnection();
         rs = conn.createStatement().executeQuery(
               "select id, sub_type from dw_resource where id in (" + artifactIds + ")");
         while (rs.next()) {
            String id = rs.getString(1);
            String type = rs.getString(2);
            ArtifactHolder holder = (ArtifactHolder) artifactsToLoad.get(id);
            if (holder != null) {
               holder.artifactType = type;
            }
         }
      }
      catch (SQLException e) {
         logger.error("", e);
         throw new RuntimeException(e);
      }
      finally {
         //ensure that the results set is clsoed
         if (rs != null) {
            try {
               rs.close();
            } catch (SQLException e) {
               if (logger.isDebugEnabled()) {
                  logger.debug("loadArtifactTypes(String, Map) caught " + e);
               }
            }
         }
         //ensure that the connection is closed
         if (conn != null) {
            try {
               conn.close();
            }
            catch (SQLException e) {
               if (logger.isDebugEnabled()) {
                  logger.debug("loadArtifactTypes(String, Map) caught " + e);
            }
            }
         }
      }
   }

   protected String getColumnType(Element data, Element dataRow) {
      return null; // null return will look up type
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

   public IdManager getIdManager() {
      return idManager;
   }

   public void setIdManager(IdManager idManager) {
      this.idManager = idManager;
   }

   /**
    * Because the resources are not stored in the data warehouse, we want to
    * use the non-data warehouse dataSource.
    * @return
    */
   public DataSource getDataSource() {
      return reportsManager.getDataSourceUseWarehouse(false);
   }

   public ArtifactFinderManager getArtifactFinderManager() {
      return artifactFinderManager;
   }

   public void setArtifactFinderManager(ArtifactFinderManager artifactFinderManager) {
      this.artifactFinderManager = artifactFinderManager;
   }

   public SecurityService getSecurityService() {
      return securityService;
   }

   public void setSecurityService(SecurityService securityService) {
      this.securityService = securityService;
   }

   public ReportsManager getReportsManager() {
      return reportsManager;
   }

   public void setReportsManager(ReportsManager reportsManager) {
      this.reportsManager = reportsManager;
   }

   public ContentHostingService getContentHosting() {
	   return contentHosting;
   }

   public void setContentHosting(ContentHostingService contentHosting) {
	   this.contentHosting = contentHosting;
   }

   protected class ArtifactHolder {
      public Id artifactId;
      public String artifactType;
      public List<Element> reportElements = new ArrayList<Element>();
   }

}
