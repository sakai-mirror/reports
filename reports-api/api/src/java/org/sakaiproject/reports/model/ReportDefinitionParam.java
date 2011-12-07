/**********************************************************************************
* $URL:https://source.sakaiproject.org/svn/osp/trunk/reports/api/src/java/org/theospi/portfolio/reports/model/ReportDefinitionParam.java $
* $Id:ReportDefinitionParam.java 9134 2006-05-08 20:28:42Z chmaurer@iupui.edu $
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
package org.sakaiproject.reports.model;

import org.sakaiproject.metaobj.shared.model.Id;
import org.sakaiproject.reports.model.ReportDefinition;
import org.sakaiproject.reports.service.ParameterResultsPostProcessor;

public class ReportDefinitionParam
{
   private String idString = null;

   /** the reportDefParamId for the report definition parameter */
   private Id reportDefParamId;

   /** the identifier to the report definition for the paramater */
   private ReportDefinition reportDefinition;

   /** the name of the param as presented to the user */
   private String title;

   /** the parameter name of the report definition parameter */
   private String paramName;

   /** the description for the report definition parameter */
   private String description;

   /** the type for the report definition Parameter parameter
    *  This is defined as "int", "float", "String", "date"
    */
   private String type;

   public static final String TYPE_STRING = "string";
   public static final String TYPE_INT = "int";
   public static final String TYPE_FLOAT = "float";
   public static final String TYPE_DATE = "date";

   /** the valueType for the report definition Parameter 
    *	fillin, set, sql, static (multiset and multisql are to be supported later)
    */
   private String valueType;

   public static final String VALUE_TYPE_FILLIN = "fillin";
   public static final String VALUE_TYPE_ONE_OF_SET = "set";
   public static final String VALUE_TYPE_MULTI_OF_SET = "multiset";
   public static final String VALUE_TYPE_ONE_OF_QUERY = "sql";
   public static final String VALUE_TYPE_MULTI_OF_QUERY = "multisql";
   public static final String VALUE_TYPE_STATIC = "static";

   /** the type for the report definition Parameter 
    * 	This is validation rules for fillin parameters,
    * 	a set of strings for sets (both value and title),
    *  the query if the value type is a sql query,
    *  or the static value
    */
   private String value;
   
   
   private ParameterResultsPostProcessor resultProcessor;



   /**
    * the getter for the reportDefParamId property
    * @return String the unique identifier
    */
   public Id getReportDefParamId()
   {
      return reportDefParamId;
   }


   /**
    * the setter for the reportDefParamId property.  This is set by the bean 
    * and by hibernate.
    * @param reportDefId String
    */
   public void setReportDefParamId(Id reportDefParamId)
   {
      this.reportDefParamId = reportDefParamId;
   }

   /**
    * return the id as a string.  return the actual id if there is one then
    * the configured definition id if not
    * @return String
    */
   public String getIdString()
   {
      if(reportDefParamId == null) {
         if(idString == null)
            generateIdString();
         return idString;
      }
      return reportDefParamId.getValue();
   }
   public void setIdString(String idString)
   {
      this.idString = idString;
   }
   protected void generateIdString()
   {
      idString = String.valueOf(reportDefinition.getReportDefinitionParams().indexOf(this));
   }


   /**
    * the getter for the ReportDefinition property
    * @return String the unique identifier
    */
   public ReportDefinition getReportDefinition()
   {
      return reportDefinition;
   }


   /**
    * the setter for the ReportDefinition property.  This is set by the bean 
    * and by hibernate.
    * @param ReportDefinition String
    */
   public void setReportDefinition(ReportDefinition reportDefinition)
   {
      this.reportDefinition = reportDefinition;
   }


   /**
    * the getter for the title property
    * @return String the title
    */
   public String getTitle()
   {
      return title;
   }


   /**
    * the setter for the title property.  This is set by the bean 
    * and by hibernate.
    * @param title String
    */
   public void setTitle(String title)
   {
      this.title = title;
   }


   /**
    * the getter for the paramName property
    * @return String the paramName
    */
   public String getParamName()
   {
      return paramName;
   }


   /**
    * the setter for the paramName property.  This is set by the bean 
    * and by hibernate.
    * @param paramName String
    */
   public void setParamName(String paramName)
   {
      this.paramName = paramName;
   }


   /**
    * the getter for the description property
    * @return String the description
    */
   public String getDescription()
   {
      return description;
   }


   /**
    * the setter for the description property.  This is set by the bean 
    * and by hibernate.
    * @param description String
    */
   public void setDescription(String description)
   {
      this.description = description;
   }


   /**
    * the getter for the type property
    * @return String the type
    */
   public String getType()
   {
      return type;
   }


   /**
    * the setter for the type property.  This is set by the bean 
    * and by hibernate.
    * @param type String
    */
   public void setType(String type)
   {
      this.type = type.trim();
   }


   /**
    * the getter for the valueType property
    * @return String the valueType
    */
   public String getValueType()
   {
      return valueType;
   }


   /**
    * the setter for the valueType property.  This is set by the bean 
    * and by hibernate.
    * @param valueType String
    */
   public void setValueType(String valueType)
   {
      this.valueType = valueType.trim();
   }


   /**
    * the getter for the value property
    * @return String the value
    */
   public String getValue()
   {
      return value;
   }


   /**
    * the setter for the value property.  This is set by the bean 
    * and by hibernate.
    * @param value String
    */
   public void setValue(String value)
   {
      this.value = value.trim();
   }


public void setResultProcessor(ParameterResultsPostProcessor resultProcessor) {
	this.resultProcessor = resultProcessor;
}


public ParameterResultsPostProcessor getResultProcessor() {
	return resultProcessor;
}
}