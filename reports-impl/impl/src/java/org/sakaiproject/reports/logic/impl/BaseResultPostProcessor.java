/**********************************************************************************
* $URL:https://source.sakaiproject.org/svn/osp/trunk/reports/api-impl/src/java/org/theospi/portfolio/reports/model/impl/BaseResultPostProcessor.java $
* $Id:BaseResultPostProcessor.java 9134 2006-05-08 20:28:42Z chmaurer@iupui.edu $
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

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Created by IntelliJ IDEA.
 * User: John Ellis
 * Date: Dec 25, 2005
 * Time: 6:39:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseResultPostProcessor {

   protected final transient Log logger = LogFactory.getLog(getClass());
   private SAXBuilder builder = new SAXBuilder();

   public Document getDocument(String document) {
      Document rootElement = null;
      try {
         rootElement = builder.build(new StringReader(document));
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

}
