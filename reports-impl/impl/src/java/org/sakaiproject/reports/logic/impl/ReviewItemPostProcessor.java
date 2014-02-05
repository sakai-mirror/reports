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
import org.sakaiproject.authz.api.SecurityAdvisor;
import org.sakaiproject.authz.api.SecurityService;
import org.sakaiproject.content.api.ContentHostingService;
import org.sakaiproject.content.api.ContentResource;
import org.sakaiproject.entity.api.EntityManager;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.exception.IdUnusedException;
import org.sakaiproject.exception.PermissionException;
import org.sakaiproject.exception.TypeException;
import org.sakaiproject.metaobj.shared.mgt.IdManager;
import org.sakaiproject.reports.model.ReportResult;
import org.sakaiproject.reports.logic.impl.BaseResultProcessor;

import org.sakaiproject.taggable.api.Link;
import org.sakaiproject.taggable.api.LinkManager;
import org.sakaiproject.taggable.api.TaggableActivity;
import org.sakaiproject.taggable.api.TaggableActivityProducer;
import org.sakaiproject.taggable.api.TaggableItem;
import org.sakaiproject.taggable.api.TaggingManager;
import org.sakaiproject.taggable.api.TaggingProvider;
import org.sakaiproject.tool.cover.SessionManager;

import org.theospi.portfolio.matrix.MatrixManager;
import org.theospi.portfolio.matrix.model.WizardPageDefinition;

import java.util.ArrayList;
import java.util.List;

public class ReviewItemPostProcessor extends BaseResultProcessor {

   private String columnNamePattern = ".*_REVIEWITEMREF$";
   
   private TaggingManager taggingManager = null;
   private TaggingProvider provider = null;
   private EntityManager entityManager = null;
   private MatrixManager matrixManager = null;
   private IdManager idManager = null;
   private LinkManager linkManager = null;
   private SecurityService securityService = null;
   private ContentHostingService contentHostingService = null;
   
   private Cache taggableItemCache = null;
   
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

			   Element activityElm = new Element("reviewItem");

			   //String reviewItemRef = getColumnData(data);
			   
			   String[] fields = getColumnData(data).split(":");
			   String wizPageDefId = fields[0];
			   String cellOwner = fields[1];
			   String reviewItemRef = fields[2];
			   
			   
			   List<TaggableItem> taggableItems = getTaggableItems(wizPageDefId, cellOwner);
			   
			   DecoratedReviewItem item = findTaggableItemByRef(taggableItems, reviewItemRef);
			   
			   // must be a resource item?
			   if (item == null) {
				   String resourceId = contentHostingService.resolveUuid(reviewItemRef);
				   try {
					   ContentResource resource = contentHostingService.getResource(resourceId);
					   item = new DecoratedReviewItem();
					   String itemName = resource.getProperties().getProperty(resource.getProperties().getNamePropDisplayName());
					   String contentRef = resource.getReference();
					   Reference ref = getEntityManager().newReference(contentRef);
					   
					   item.setTitle(itemName);
				   } catch (PermissionException e) {
					   logger.error("error getting resource item", e);
				   } catch (IdUnusedException e) {
					   logger.error("error getting resource item", e);
				   } catch (TypeException e) {
					   logger.error("error getting resource item", e);
				   }
			   }
			   
			   Element attr = new Element("ref");
			   attr.setText(reviewItemRef);
			   activityElm.addContent(attr);

			   /*
			    * Currently, there are 4 types of things that will have reviews:
			    * 
			    *  /assignment/s/SP11-IN-UITS-PRAC-24723/705429a8-1321-4ccd-94b3-e244419a0432/e6602519-87b3-4a44-b29e-fd2b5e25727b@b5289ce1-f738-45b1-8096-d00b5f56dac3
			    *  /wizard/page/18D108A1FAB5CB883CAD28E3FD85AC4F
			    *  asnn2/s/FA11-BL-CITL-PRAC-34062/838130@demo40
			    *  0093c562-80c8-4a80-aab5-8f75391cbdcc
			    * 
			    */
			   
			   attr = new Element("title");
			   attr.setText(item.getTitle());
			   activityElm.addContent(attr);
			   attr = new Element("siteTitle");
			   attr.setText(item.getSiteTitle());
			   activityElm.addContent(attr);
			   data.removeContent();
			   data.addContent(activityElm);
		   }
	   }
   }
   
   private DecoratedReviewItem findTaggableItemByRef(List<TaggableItem> items, String reference) {
	   for (TaggableItem item : items) {
		   if (item.getReference().equals(reference))
			   return new DecoratedReviewItem(item.getTitle(), item.getSiteTitle());
	   }
	   return null;
   }
   
   private List<TaggableItem> getTaggableItems(String wizPageDefId, String cellOwner) {
	   List<TaggableItem> items = new ArrayList<TaggableItem>();
	   String cacheKey = wizPageDefId + ":" + cellOwner;
	   //look in the cache
	   try {
			net.sf.ehcache.Element elem = null;
			if(cacheKey != null)
				elem = taggableItemCache.get(cacheKey);
			if(taggableItemCache != null && elem != null) {
				if(elem.getValue() != null) {
					items = (List<TaggableItem>)elem.getValue();
					return items;
				}
			}
		} catch(CacheException e) {
			logger.warn("the site ehcache had an exception", e);					   
		}


	   WizardPageDefinition wpd = getMatrixManager().getWizardPageDefinition(getIdManager().getId(wizPageDefId));
	   String wpdRef = wpd.getReference();
	   List<Link> links = new ArrayList<Link>();
	try {
		links = getLinkManager().getLinks(wpdRef, true);
	} catch (PermissionException e) {
		logger.error("Error getting links for ref: " + wpdRef, e);
	}
	   List<TaggingProvider> providers = getTaggingManager().getProviders();
	   for (TaggingProvider provider : providers) {
			for (Link link : links) {
				TaggableActivity activity = getTaggingManager().getActivity(link.getActivityRef(), provider, null);
				if (activity != null) {
					TaggableActivityProducer producer = getTaggingManager().findProducerByRef(activity.getReference());
					if (producer.getItemPermissionOverride() != null) {
						getSecurityService().pushAdvisor(new SimpleSecurityAdvisor(
								SessionManager.getCurrentSessionUserId(), 
								producer.getItemPermissionOverride()));
					}
					items.addAll(producer.getItems(activity, cellOwner, provider, true, wpdRef));
					
					if (producer.getItemPermissionOverride() != null) {
						getSecurityService().popAdvisor();
					}
				}
				else {
					logger.warn("Link with ref " + link.getActivityRef() + " no longer exists.  Removing link.");
					getLinkManager().removeLink(link);
					links.remove(link);
				}
			}
	   }
	   
	   if(taggableItemCache != null)
		   taggableItemCache.put(new net.sf.ehcache.Element(cacheKey, items));

	   return items;
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

   public EntityManager getEntityManager() {
      return entityManager;
   }

   public void setEntityManager(EntityManager entityManager) {
	   this.entityManager = entityManager;
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

	public MatrixManager getMatrixManager() {
		return matrixManager;
	}
	
	public void setMatrixManager(MatrixManager matrixManager) {
		this.matrixManager = matrixManager;
	}
	
	public IdManager getIdManager() {
		return idManager;
	}
	
	public void setIdManager(IdManager idManager) {
		this.idManager = idManager;
	}
	
	public LinkManager getLinkManager() {
		return linkManager;
	}
	
	public void setLinkManager(LinkManager linkManager) {
		this.linkManager = linkManager;
	}
	
	public void setSecurityService(SecurityService securityService) {
		this.securityService = securityService;
	}

	public SecurityService getSecurityService() {
		return securityService;
	}

	public void setContentHostingService(ContentHostingService contentHostingService) {
		this.contentHostingService = contentHostingService;
	}

	public ContentHostingService getContentHostingService() {
		return contentHostingService;
	}

	public void setTaggableItemCache(Cache taggableItemCache) {
		this.taggableItemCache = taggableItemCache;
	}

	public Cache getTaggableItemCache() {
		return taggableItemCache;
	}

	/**
	 * A simple SecurityAdviser that can be used to override permissions for one user for one function.
	 */
	protected class SimpleSecurityAdvisor implements SecurityAdvisor
	{
		protected String m_userId;
		protected String m_function;

		public SimpleSecurityAdvisor(String userId, String function)
		{
			m_userId = userId;
			m_function = function;
		}

		public SecurityAdvice isAllowed(String userId, String function, String reference)
		{
			SecurityAdvice rv = SecurityAdvice.PASS;
			if (m_userId.equals(userId) && m_function.equals(function))
			{
				rv = SecurityAdvice.ALLOWED;
			}
			return rv;
		}
	}
	
	protected class DecoratedReviewItem {
		private String title;
		private String siteTitle;
		
		public DecoratedReviewItem() {
			
		}
		
		public DecoratedReviewItem(String title, String siteTitle) {
			this.title = title;
			this.siteTitle = siteTitle;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getSiteTitle() {
			return siteTitle;
		}

		public void setSiteTitle(String siteTitle) {
			this.siteTitle = siteTitle;
		}
	}
}
