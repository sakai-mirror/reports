/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/msub/iu.edu/oncourse/trunk/reports/reports-tool/tool/src/java/org/sakaiproject/reports/tool/DecoratedReportDefinitionXmlFile.java $
 * $Id: DecoratedReportDefinitionXmlFile.java 61316 2009-04-27 20:15:31Z chmaurer@iupui.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2007, 2008 The Sakai Foundation
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

package org.sakaiproject.reports.tool;

import org.w3c.dom.Document;
import org.sakaiproject.reports.model.ReportDefinitionXmlFile;


/**
 * This class allows the ReportResult to interact with the view
 *
 */
public class DecoratedReportDefinitionXmlFile {


    /** The link to the main tool */
    private ReportDefinitionXmlFile	reportDefinitionXmlFile = null;
    private Document xmlFile = null;


    public Document getXmlFile() {
        return xmlFile;
    }


    public DecoratedReportDefinitionXmlFile(Document xmlFile)
    {
       this.xmlFile = xmlFile;
    }


}
