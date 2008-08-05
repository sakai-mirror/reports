/**********************************************************************************
* $URL:https://source.sakaiproject.org/svn/osp/trunk/reports/api-impl/src/java/org/theospi/portfolio/reports/model/impl/BaseResultProcessor.java $
* $Id:BaseResultProcessor.java 9134 2006-05-08 20:28:42Z chmaurer@iupui.edu $
***********************************************************************************
*
 * Copyright 2006 Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
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

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.sakaiproject.reports.model.ReportResult;
import org.sakaiproject.reports.service.ResultProcessor;

/**
 * Created by IntelliJ IDEA.
 * User: John Ellis
 * Date: Dec 22, 2005
 * Time: 5:32:24 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseResultProcessor implements ResultProcessor {

   protected final transient Log logger = LogFactory.getLog(getClass());
   private SAXBuilder builder = new SAXBuilder();

   protected Document getResults(ReportResult result) {
      Document rootElement = null;
      try {
         rootElement = builder.build(new StringReader(result
                     .getXml()));
      }
      catch (JDOMException e) {
         logger.error("", e);
         throw new RuntimeException(e);
      }
      catch (IOException e) {
         logger.error("", e);
         throw new RuntimeException(e);
      }
      return rootElement;
   }

   protected ReportResult setResult(ReportResult result, Document doc) {
      result.setXml((new XMLOutputter()).outputString(doc));
      return result;
   }

   protected boolean isColumnNull(Element data) {
      return new Boolean(data.getAttributeValue("isNull", "false")).booleanValue();
   }

}
