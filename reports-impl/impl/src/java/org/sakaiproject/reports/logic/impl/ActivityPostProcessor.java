/**********************************************************************************
* $URL:https://source.sakaiproject.org/svn/osp/trunk/reports/api-impl/src/java/org/theospi/portfolio/reports/model/impl/LoadArtifactResultProcessor.java $
* $Id:LoadArtifactResultProcessor.java 9134 2006-05-08 20:28:42Z chmaurer@iupui.edu $
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
package org.sakaiproject.reports.logic.impl;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;

import org.jdom.Document;
import org.jdom.Element;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.reports.model.ReportResult;
import org.sakaiproject.reports.logic.impl.BaseResultProcessor;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.cover.SiteService;
import org.sakaiproject.taggable.api.TaggableActivity;
import org.sakaiproject.taggable.api.TaggingManager;
import org.sakaiproject.taggable.api.TaggingProvider;

import java.util.List;

/**
 * To use this class you need to place the resource UUID into a column of the report query.
 * The column needs to be then labeled with the ending "_SORTNAME".  Then include this class
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
public class ActivityPostProcessor extends BaseResultProcessor {

   private String columnNamePattern = ".*_ACTIVITY$";
   
   private Cache siteCache = null;
   
   TaggingManager taggingManager = null;
   TaggingProvider provider = null;
   
   
   /**
    * Post Processor method
    */
   public ReportResult process(ReportResult result) {
      Document rootDoc = getResults(result);
      List<Element> data = rootDoc.getRootElement().getChild("data").getChildren("datarow");
      
      for (Element dataRow : data) {
         processRow(dataRow);
      }

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

	   for (Element data : columns) {
		   if (isArtifactColumn(data) && !isColumnNull(data)) {

			   Element activityElm = new Element("activity");

			   String activityRef = getColumnData(data);
			   Element attr = new Element("ref");
			   attr.setText(activityRef);
			   activityElm.addContent(attr);

			   TaggableActivity activity = getTaggingManager().getActivity(activityRef, getProvider());
			   
			   attr = new Element("title");
			   attr.setText(activity.getTitle());
			   activityElm.addContent(attr);
			   
			   attr = new Element("type");
			   attr.setText(activity.getTypeName());
			   activityElm.addContent(attr);
			   
			   String siteId = activity.getContext();
			   attr = new Element("siteId");
			   attr.setText(siteId);
			   activityElm.addContent(attr);			   
			   
			   try {
				   Site site = lookupSite(siteId);

				   attr = new Element("siteTitle");
				   attr.setText(site.getTitle());
				   activityElm.addContent(attr);
			   } catch (IdUnusedException e) {
				   logger.error("", e);
			   }
			   
			   data.removeContent();
			   data.addContent(activityElm);
		   }
	   }
   }
	
	private Site lookupSite(String siteId) throws IdUnusedException {
		Site site = null;
		try {
			net.sf.ehcache.Element elem = null;
			if(siteId != null)
				elem = siteCache.get(siteId);
			if(siteCache != null && elem != null) {
				if(elem.getValue() != null)
					site = (Site)elem.getValue();
			}
		} catch(CacheException e) {
			logger.warn("the site ehcache had an exception", e);					   
		}

		if (site == null) {
			site = SiteService.getSite(siteId);			
		}

		if(siteCache != null)
			siteCache.put(new net.sf.ehcache.Element(siteId, site));

		return site;
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

   public Cache getSiteCache() {
	   return siteCache;
   }

   public void setSiteCache(Cache siteCache) {
	   this.siteCache = siteCache;
   }

public TaggingManager getTaggingManager() {
	return taggingManager;
}

public void setTaggingManager(TaggingManager taggingManager) {
	this.taggingManager = taggingManager;
}

public TaggingProvider getProvider() {
	return provider;
}

public void setProvider(TaggingProvider provider) {
	this.provider = provider;
}
}
