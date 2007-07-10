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
