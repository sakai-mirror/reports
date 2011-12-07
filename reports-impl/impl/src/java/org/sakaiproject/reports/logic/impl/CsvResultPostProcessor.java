/**********************************************************************************
* $URL:https://source.sakaiproject.org/svn/osp/trunk/reports/api-impl/src/java/org/theospi/portfolio/reports/model/impl/CsvResultPostProcessor.java $
* $Id:CsvResultPostProcessor.java 9134 2006-05-08 20:28:42Z chmaurer@iupui.edu $
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.sakaiproject.reports.service.ResultsPostProcessor;
import org.sakaiproject.reports.logic.impl.BaseResultPostProcessor;

/**
 * Created by IntelliJ IDEA.
 * User: John Ellis
 * Date: Dec 25, 2005
 * Time: 6:43:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class CsvResultPostProcessor extends BaseResultPostProcessor implements ResultsPostProcessor {

   public byte[] postProcess(String fileData) {
      Document results = getDocument(fileData);

      ByteArrayOutputStream os = new ByteArrayOutputStream(fileData.length());

      try {
         Element[] orderedHeaders = processDocumentHeaders(results);
         Element[][] orderedData = processDocumentData(results);

         createHeaderRow(orderedHeaders, os);
         createDataArea(orderedData, os);
      }
      catch (DataConversionException e) {
         throw new RuntimeException(e);
      }
      catch (IOException e) {
         throw new RuntimeException(e);
      }

      return os.toByteArray();
   }

   protected void createDataArea(Element[][] orderedData, OutputStream os) throws IOException {
      for (int i=0;i<orderedData.length;i++) {
         Element[] data = orderedData[i];
         createDataRow(data, os);
         os.write('\n');
      }
   }

   protected void createDataRow(Element[] data, OutputStream os) throws IOException {
      for (int i=0;i<data.length;i++) {
         Element column = data[i];
         if (i > 0) {
            os.write(',');
         }
         writeDataValue(column, os);
      }
   }

   protected void writeDataValue(Element column, OutputStream os) throws IOException {
      os.write('"');
      os.write(escapeValue(column.getTextNormalize()).getBytes("UTF-8"));
      os.write('"');
   }

   protected void createHeaderRow(Element[] orderedHeaders, OutputStream os) throws IOException {

      for (int i=0;i<orderedHeaders.length;i++) {
         Element column = orderedHeaders[i];
         if (i > 0) {
            os.write(',');
         }
         writeHeaderValue(column, os);
      }
      os.write('\n');
   }

   protected void writeHeaderValue(Element column, OutputStream os) throws IOException {
      os.write('"');
      os.write(escapeValue(column.getAttributeValue("title")).getBytes("UTF-8"));
      os.write('"');
   }

   protected Element[] processDocumentHeaders(Document results) throws DataConversionException {
      List headers = results.getRootElement().getChild("columns").getChildren("column");
      Element[] returned = new Element[headers.size()];
      for (Iterator i=headers.iterator();i.hasNext();) {
         Element column = (Element) i.next();
         int order = column.getAttribute("colIndex").getIntValue();
         returned[order] = column;
      }
      return returned;
   }

   protected Element[][] processDocumentData(Document results)
      throws DataConversionException {
      List data = results.getRootElement().getChild("data").getChildren("datarow");
      Element[][] returned = new Element[data.size()][];
      for (Iterator i=data.iterator();i.hasNext();) {
         Element row = (Element) i.next();
         int order = row.getAttribute("index").getIntValue();
         returned[order] = processRow(row);
      }
      return returned;
   }

   protected Element[] processRow(Element row) throws DataConversionException {
      List columns = row.getChildren("element");
      Element[] returned = new Element[columns.size()];

      for (Iterator i=columns.iterator();i.hasNext();) {
         Element column = (Element) i.next();
         int order = column.getAttribute("colIndex").getIntValue();
         returned[order] = column;
      }

      return returned;
   }

   /**
    * formats a string so that excell will interpret it as data for a single cell.
    */
   protected String escapeValue(String text) {
      String excelText = new String(text);

      // remove any trailing newline character
      boolean endsWithNewline = excelText.endsWith("\r\n");
      if (endsWithNewline)
         excelText = excelText.substring(0, excelText.length()-2);

      endsWithNewline = excelText.endsWith("\n");
      if (endsWithNewline)
         excelText = excelText.substring(0, excelText.length()-1);

      // remove any leading newline character
      boolean startsWithNewline = excelText.startsWith("\r\n");
      if (startsWithNewline)
         excelText = excelText.substring(2);

      // escape double quotes
      excelText = excelText.replaceAll("\"", "\"\"");

      // strip html formatting tags from text
      excelText = excelText.replaceAll("<.*?>", "");

      return excelText;
   }

}
