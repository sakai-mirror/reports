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
import org.sakaiproject.authz.api.Role;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.reports.model.ReportResult;
import org.sakaiproject.reports.logic.impl.BaseResultProcessor;
import org.sakaiproject.site.api.Group;
import org.sakaiproject.site.api.Site;
import org.sakaiproject.site.cover.SiteService;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.sakaiproject.user.cover.UserDirectoryService;

import java.util.Collection;
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
public class LoadSiteUserProcessor extends BaseResultProcessor {

   private String columnNamePattern = ".*_siteuser$";
   private String GROUP_LIST_DELIMITER = ";";
   private Cache userCache = null;
   private Cache siteCache = null;
   
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

			   Element userElm = new Element("user");

			   String[] fields = getColumnData(data).split(":");
			   String userId = fields[0];
			   String siteId = fields[1];
			   Element attr = new Element("id");
			   attr.setText(fields[0]);
			   userElm.addContent(attr);

			   try {
				   User user = lookupUser(userId);

				   attr = new Element("eid");
				   attr.setText(user.getEid());
				   userElm.addContent(attr);

				   attr = new Element("displayName");
				   attr.setText(user.getDisplayName());
				   userElm.addContent(attr);

				   attr = new Element("sortName");
				   attr.setText(user.getSortName());
				   userElm.addContent(attr);

				   attr = new Element("firstName");
				   attr.setText(user.getFirstName());
				   userElm.addContent(attr);

				   attr = new Element("lastName");
				   attr.setText(user.getLastName());
				   userElm.addContent(attr);

				   attr = new Element("email");
				   attr.setText(user.getEmail());
				   userElm.addContent(attr);
				   
				   Site site = lookupSite(siteId);
				   Collection<Group> groups = site.getGroupsWithMember(userId);
				   if (groups.size() > 0) {
					   String groupList = "";
					   int count=0;
					   for (Group group : groups) {
						   if (count==0)
							   groupList = group.getTitle();
						   else
							   groupList += GROUP_LIST_DELIMITER + group.getTitle();
						   count++;
					   }

					   attr = new Element("groups");
					   attr.setText(groupList);
					   userElm.addContent(attr);
				   }
				   
				   Role role = site.getUserRole(userId);
				   attr = new Element("role");
				   if (role != null) {
					   attr.setText(role.getId());
				   }
				   userElm.addContent(attr);

			   } catch (UserNotDefinedException e) {
				   logger.error("", e);
			   } catch (IdUnusedException e) {
				   logger.error("", e);
			}
			   data.removeContent();
			   data.addContent(userElm);
		   }
	   }
   }
   
	private String ListToString(String[] strArray) {
		String result = "";
		if (strArray != null) {
			for (int i = 0; i < strArray.length; i++) {
            if (i == 0) {
               result = strArray[i];
            } else {
               result = result.concat(",").concat(strArray[i]);
            }
			}
		}
		return result;
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
	
	private User lookupUser(String userId) throws UserNotDefinedException {
		User user = null;
		try {
			net.sf.ehcache.Element elem = null;
			if(userId != null)
				elem = userCache.get(userId);
			if(userCache != null && elem != null) {
				if(elem.getObjectValue() != null)
					user = (User)elem.getObjectValue();
			}
		} catch(CacheException e) {
			logger.warn("the user ehcache had an exception", e);					   
		}

		if (user == null) {
			user = UserDirectoryService.getUser(userId);
		}
		if (userCache != null) {
			userCache.put(new net.sf.ehcache.Element(userId, user));
		}
		
		return user;
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

   public Cache getUserCache() {
	   return userCache;
   }

   public void setUserCache(Cache userCache) {
	   this.userCache = userCache;
   }

   public Cache getSiteCache() {
	   return siteCache;
   }

   public void setSiteCache(Cache siteCache) {
	   this.siteCache = siteCache;
   }
}
