/**********************************************************************************
* $URL:https://source.sakaiproject.org/svn/osp/trunk/reports/tool/src/java/org/theospi/portfolio/reports/tool/DecoratedReportParam.java $
* $Id:DecoratedReportParam.java 9134 2006-05-08 20:28:42Z chmaurer@iupui.edu $
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
package org.sakaiproject.reports.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sakaiproject.reports.model.ReportDefinitionParam;
import org.sakaiproject.reports.model.ReportParam;


/**
 * This class allows the ReportResult to interact with the view
 *
 */
public class DecoratedReportParam {

   protected final transient Log logger = LogFactory.getLog(getClass());

   /** The link to the main tool */
   private ReportsTool	reportsTool = null;

   /** the report to decorate */
   private ReportParam reportParam;

   private boolean		isValid = false;

   /** the index in the list of params which contains this class */
   private int index;

   private static SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");


   public DecoratedReportParam(ReportParam reportParam, ReportsTool reportsTool)
   {
      setReportParam(reportParam);
      this.reportsTool = reportsTool;
   }
   public ReportParam getReportParam()
   {
      return reportParam;
   }
   public void setReportParam(ReportParam reportParam)
   {
      this.reportParam = reportParam;
   }
   public ReportDefinitionParam getReportDefinitionParam()
   {
      return reportParam.getReportDefinitionParam();
   }


   public int getIndex()
   {
      return index;
   }
   public void setIndex(int index)
   {
      this.index = index;
   }

   public String getStaticValue()
   {
      return reportParam.getValue();
   }

   public String getTextValue()
   {
      return reportParam.getValue();
   }
   public void setTextValue(String value)
   {
      isValid = false;
      if(getIsFillIn() && !getIsDate()) {
         if(getIsInteger()) {
            try {
               value =  Integer.toString(Integer.parseInt(value));
               isValid = true;
            } catch(NumberFormatException pe) {
               //if it fails to parse then it won't set isValid to true
            }
         }
         if(getIsFloat()) {
            try {
               value =  Float.toString(Float.parseFloat(value));
               isValid = true;
            } catch(NumberFormatException pe) {
               //if it fails to parse then it won't set isValid to true
            }
         }
         if(getIsString()) {
            isValid = value.length() > 0;
         }
         reportParam.setValue(value);
      }
   }

   public String getDateValue()
   {
      return reportParam.getValue();
   }
   public void setDateValue(String value)
   {
      isValid = false;
      if(getIsFillIn() && getIsDate()) {

         try {
            reportParam.setValue( dateFormatter.format( dateFormatter.parse(value)));
            isValid = true;
         } catch(ParseException pe) {
            //if it fails to parse then it won't set isValid to true
         }
      }
   }

   public String getMenuValue()
   {
      return reportParam.getValue();
   }
   public void setMenuValue(String value)
   {
      if(getIsSet() && !getIsMultiSelectable()) {
         reportParam.setValue(value);
         isValid = true;
      }
   }

   /**
    * defunc - we don't do lists now, it will output the list of 
    * selected values
    * @return List
    */
   public List getListValue()
   {
      if (reportParam.getListValue() == null ) {
            return new ArrayList();
        }
        else {
            return reportParam.getListValue();
        }
   }

   /**
    * defunc - We don't do lists now, the list value should 
    * iterate through and build a string of values
    * @param List value
    */
   public void setListValue(List value)
   {
      if(getIsSet() && getIsMultiSelectable()) {
         reportParam.setListValue(value);
            if (value.size() < 1){
                isValid = false;
            } else {
                isValid = true;
            }
        }
   }
   
   protected String unescapeCommas(String value) { 
      return value.replaceAll("<<comma>>",","); 
   }  
   
   /**
    * gets the list of possible titles and values
    * This will return a List of drop down SelectItem.
    * The list is generated from any of the following:<br>
    * 		[value1, value2, value3, ...]<br>
    * 		[(value1), (value2), (value3), ...]<br>
    * 		[(value1; item title1), (value2; item title2), (value3; item title3), ...]<br>
    * These values would be in the value field of the report definition parameter.
    * If the parameter is created from a sql query, then
    * it is run:
    * 		select value from table where ...<br>
    * 		select value, itemTitle from table where ...
    * 
    * @return List of SelectItem
    */
   public List getSelectableValues()
   {
      ArrayList array = new ArrayList();
      if(getIsSet()) {
         String strSet = null;
         if(getIsDynamic()) {
            //	run the sql in the report definition parameter value
            strSet = reportsTool.getReportsManager().generateSQLParameterValue(reportParam, new ArrayList<ReportParam>());
         } else {
            strSet = reportParam.getReportDefinitionParam().getValue();
         }

         strSet = strSet.substring(strSet.indexOf("[")+1, strSet.indexOf("]"));
         String[] set = strSet.split(",");

         for(int i = 0; i < set.length; i++) {
            String element = set[i].trim();

            //	replace any system values for display in the interface
            element = reportsTool.getReportsManager().replaceSystemValues(element);

            if(element.indexOf("(") != -1) {
               element = element.substring(element.indexOf("(")+1, element.indexOf(")"));

               String[] elementData = element.split(";");
               if(elementData.length == 0)
                  array.add(new SelectItem());
               if(elementData.length == 1)
                  array.add(new SelectItem(unescapeCommas(elementData[0].trim())));
               if(elementData.length > 1)
                  array.add(new SelectItem(unescapeCommas(elementData[0].trim()), unescapeCommas(elementData[1].trim())));
            } else {
               array.add(new SelectItem(unescapeCommas(element)));
            }
         }

      }

      return array;
   }


   /**
    * tells whether this parameter is a set
    * @return boolean
    */
   public boolean getIsSet()
   {
      String type = reportParam.getReportDefinitionParam().getValueType();
      return type.equals(ReportDefinitionParam.VALUE_TYPE_ONE_OF_SET) ||
            type.equals(ReportDefinitionParam.VALUE_TYPE_ONE_OF_QUERY)||
            type.equals(ReportDefinitionParam.VALUE_TYPE_MULTI_OF_SET) ||
            type.equals(ReportDefinitionParam.VALUE_TYPE_MULTI_OF_QUERY);
   }


   /**
    * tells whether this parameter is the result of a sql query
    * @return boolean
    */
   public boolean getIsDynamic()
   {
      String type = reportParam.getReportDefinitionParam().getValueType();
      return type.equals(ReportDefinitionParam.VALUE_TYPE_ONE_OF_QUERY)||
            type.equals(ReportDefinitionParam.VALUE_TYPE_MULTI_OF_QUERY);
   }


   /**
    * tells whether this parameter can have multiple values selected
    * @return boolean
    */
   public boolean getIsMultiSelectable()
   {
      String type = reportParam.getReportDefinitionParam().getValueType();
      return type.equals(ReportDefinitionParam.VALUE_TYPE_MULTI_OF_SET) ||
            type.equals(ReportDefinitionParam.VALUE_TYPE_MULTI_OF_QUERY);
   }


   /**
    * tells whether this parameter is a fill in value
    * @return boolean
    */
   public boolean getIsFillIn()
   {
      return reportParam.getReportDefinitionParam().getValueType().equals(
                  ReportDefinitionParam.VALUE_TYPE_FILLIN);
   }


   /**
    * tells whether this parameter is a static value
    * @return boolean
    */
   public boolean getIsStatic()
   {
      return reportParam.getReportDefinitionParam().getValueType().equals(
                  ReportDefinitionParam.VALUE_TYPE_STATIC);
   }

   public boolean getIsDate()
   {
      return reportParam.getReportDefinitionParam().getType().equals(ReportDefinitionParam.TYPE_DATE);
   }

   public boolean getIsFloat()
   {
      return reportParam.getReportDefinitionParam().getType().equals(ReportDefinitionParam.TYPE_INT);
   }

   public boolean getIsInteger()
   {
      return reportParam.getReportDefinitionParam().getType().equals(ReportDefinitionParam.TYPE_FLOAT);
   }

   public boolean getIsString()
   {
      return reportParam.getReportDefinitionParam().getType().equals(ReportDefinitionParam.TYPE_STRING);
   }

   public boolean getIsValid()
   {
      return isValid
         || getIsStatic();
   }
}