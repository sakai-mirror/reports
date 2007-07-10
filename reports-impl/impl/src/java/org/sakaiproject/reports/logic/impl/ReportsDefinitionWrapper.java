package org.sakaiproject.reports.logic.impl;

import org.sakaiproject.reports.model.ReportDefinitionXmlFile;


/**
 * Created by IntelliJ IDEA.
 * User: John Ellis
 * Date: Jan 30, 2006
 * Time: 10:25:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReportsDefinitionWrapper extends ReportDefinitionXmlFile {

   private String idValue;
   private String definitionFileLocation;
   private String beanId;

    public String getDefinitionFileLocation() {
        return definitionFileLocation;
    }
    public void setDefinitionFileLocation(String definitiontFileLocation) {
        this.definitionFileLocation = definitiontFileLocation;
    }
   public String getIdValue() {
      return idValue;
   }
   public void setIdValue(String idValue) {
      this.idValue = idValue;
   }

    public String getBeanId() {
        return beanId;
    }

    public void setBeanId(String beanId) {
        this.beanId = beanId;
    }
}
