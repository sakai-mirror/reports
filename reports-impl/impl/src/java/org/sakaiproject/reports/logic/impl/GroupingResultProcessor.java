/**********************************************************************************
* $URL: https://source.sakaiproject.org/svn/msub/iu.edu/oncourse/trunk/reports/reports-impl/impl/src/java/org/sakaiproject/reports/logic/impl/GroupingResultProcessor.java $
* $Id: GroupingResultProcessor.java 61316 2009-04-27 20:15:31Z chmaurer@iupui.edu $
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
import org.sakaiproject.metaobj.shared.ArtifactFinderManager;
import org.sakaiproject.reports.service.ReportsManager;
import org.sakaiproject.reports.model.ReportResult;
import org.sakaiproject.reports.logic.impl.BaseResultProcessor;

import javax.sql.DataSource;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: John Ellis
 * Date: Dec 22, 2005
 * Time: 5:31:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroupingResultProcessor extends BaseResultProcessor {

   private DataSource dataSource;
   private ArtifactFinderManager artifactFinderManager;
   private SecurityService securityService;
   private ReportsManager reportsManager;
   private List grouping = null;

   public List getGrouping()
   {
      return grouping;
   }
   public void setGrouping(List grouping)
   {
      this.grouping = grouping;
   }
   
   public ReportResult process(ReportResult result) {
      Document rootDoc = getResults(result);


      Element groupings = new Element("groupings");
      for (Iterator i = grouping.iterator(); i.hasNext(); ) {
         String theGrouping = (String)i.next();
         String groups[] = theGrouping.split(",");
         List elements = processGroup(rootDoc, theGrouping);
         Element group = new Element("grouping");
         
         group.setAttribute("by", groups[0]);
         for(Iterator ii = elements.iterator(); ii.hasNext(); ) {
            Element element = (Element)ii.next();
            group.addContent((Element)element.clone());
         }
         groupings.addContent(group);
      }
      
      rootDoc.getRootElement().addContent(groupings);
      
      
      return setResult(result, rootDoc);
   }
   protected List processGroup(Document rootDoc, String inGrouping)
   {
      
      List data = rootDoc.getRootElement().getChild("data").getChildren("datarow");
      
     return groupElements(data, inGrouping);
   }
   
   protected List groupElements(List rows, String inGrouping)
   {
      String groups[] = inGrouping.split(",");
      String groupStr = groups[0].trim();
      Map  groupHash = new HashMap();
      
      // Loop through all the data rows
      for(Iterator i = rows.iterator(); i.hasNext(); ) {
         Element dataRow = (Element)i.next();
         List columns = dataRow.getChildren("element");
         
         // go through each column and find the grouping column
         for(Iterator ii = columns.iterator(); ii.hasNext(); ) {
            Element column = (Element)ii.next();
            
            if(column.getAttribute("colName").getValue().equals(groupStr)) {
               // add the grouping data
               List groupList = (List)groupHash.get(column.getTextTrim());
               if(groupList == null)
                  groupList = new ArrayList();
               groupList.add(dataRow);
               groupHash.put(column.getTextTrim(), groupList);
            }
         }
      }

      List matchingElements = new ArrayList();
      for(Iterator i = groupHash.keySet().iterator(); i.hasNext(); ) {
         String key = (String)i.next();
         Element group = new Element("group");
         group.setAttribute("value", key);
         for (Iterator itr = ((List) groupHash.get(key)).iterator(); itr.hasNext();) {

            Element element = (Element)itr.next();
            group.addContent((Element)element.clone());

         }
         matchingElements.add(group);
      }
       
      return matchingElements;
   }

}
