/**********************************************************************************
* $URL: https://source.sakaiproject.org/svn/msub/iu.edu/oncourse/trunk/reports/reports-api/api/src/java/org/sakaiproject/reports/service/ResultsPostProcessor.java $
* $Id: ResultsPostProcessor.java 61316 2009-04-27 20:15:31Z chmaurer@iupui.edu $
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
package org.sakaiproject.reports.service;

/**
 * Created by IntelliJ IDEA.
 * User: John Ellis
 * Date: Dec 25, 2005
 * Time: 5:58:54 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ResultsPostProcessor {

   public byte[] postProcess(String fileData);

}
