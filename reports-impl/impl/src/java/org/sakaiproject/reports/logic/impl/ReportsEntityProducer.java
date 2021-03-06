/**********************************************************************************
* $URL$
* $Id$
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.entity.api.Entity;
import org.sakaiproject.metaobj.shared.mgt.EntityProducerBase;

/**
 * This class is a singleton from components.xml.
 * 
 * It munges/decorates a resource url with info so as the artifact manager
 * will ask the reports code base about access to the particular artifact
 * 
 * @see ReportsHttpAccess
 * 
 * Created by IntelliJ IDEA.
 * User: John Ellis
 * Date: Dec 24, 2005
 * Time: 12:01:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportsEntityProducer extends EntityProducerBase {

   protected final Log logger = LogFactory.getLog(getClass());
   public static final String REPORTS_PRODUCER = "reports";

   public String getLabel() {
      return REPORTS_PRODUCER;
   }

   public void init() {
      logger.info("init()");
      try {
         getEntityManager().registerEntityProducer(this, Entity.SEPARATOR + REPORTS_PRODUCER);
      }
      catch (Exception e) {
         logger.warn("Error registering Reports Entity Producer", e);
      }
   }

}
