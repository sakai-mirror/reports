/**********************************************************************************
 * $URL: https://source.sakaiproject.org/svn/msub/iu.edu/oncourse/trunk/reports/reports-api/api/src/java/org/sakaiproject/reports/model/ReportDefinitionXmlFile.java $
 * $Id: ReportDefinitionXmlFile.java 61316 2009-04-27 20:15:31Z chmaurer@iupui.edu $
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

package org.sakaiproject.reports.model;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.sakaiproject.content.api.ContentResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;


public class ReportDefinitionXmlFile {
    private String reportDefId = null;
    private byte[] xmlFile;
    private Set reportXslFiles;
    Document xml;


    public ReportDefinitionXmlFile() {
    }

    public ReportDefinitionXmlFile(ContentResource resource) {
        SAXBuilder builder = new SAXBuilder();


        try {
            InputStream in = resource.streamContent();
            setXmlFile(readStreamToBytes(resource.streamContent()));
            setXml(builder.build(in));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getReportDefId() {
        return reportDefId;
    }

    public void setReportDefId(String reportDefId) {
        this.reportDefId = reportDefId;
    }

    public Document getXml() {
        if (xml == null) {
            ByteArrayInputStream in = new ByteArrayInputStream(getXmlFile());
            SAXBuilder builder = new SAXBuilder();
            try {
                Document doc = builder.build(in);
                setXml(doc);
            }
            catch (Exception e) {

            }


        }
         return xml;
    }
        public void setXml
        (Document
        xml) {
        this.xml = xml;
    }

        public byte[] getXmlFile
        ()
        {
            return xmlFile;
        }

        public void setXmlFile
        (
        byte[] xmlFile) {
        this.xmlFile = xmlFile;
    }
        private byte[] readStreamToBytes
        (InputStream
        inStream) throws IOException
        {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            byte data[] = new byte[10 * 1024];

            int count;
            while ((count = inStream.read(data, 0, 10 * 1024)) != -1) {
                bytes.write(data, 0, count);
            }
            byte[] tmp = bytes.toByteArray();
            bytes.close();
            return tmp;
        }

    public Set getReportXslFiles() {
        return reportXslFiles;
    }

    public void setReportXslFiles(Set reportXslFiles) {
        this.reportXslFiles = reportXslFiles;
    }

    public int hashCode() { 
        return getReportDefId().hashCode(); 
    } 
 
    public boolean equals(Object object) { 
        if (this != null && object != null && object instanceof ReportDefinitionXmlFile) { 
            ReportDefinitionXmlFile that = (ReportDefinitionXmlFile) object; 
            if (that.getReportDefId() != null && that.getReportDefId().equals(getReportDefId())) { 
                return true; 
 
            } 
        } 
        return false; 
    } 
   
}
