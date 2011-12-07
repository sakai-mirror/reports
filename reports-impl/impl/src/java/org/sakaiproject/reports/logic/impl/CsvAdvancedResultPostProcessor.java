/**********************************************************************************
 * $URL:https://source.sakaiproject.org/svn/osp/trunk/reports/api-impl/src/java/org/theospi/portfolio/reports/model/impl/CsvResultPostProcessor.java $
 * $Id:CsvResultPostProcessor.java 9134 2006-05-08 20:28:42Z chmaurer@iupui.edu $
 ***********************************************************************************
 *
 * Copyright (c) 2005, 2006 The Sakai Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.opensource.org/licenses/ecl1.php
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.Filter;
import org.sakaiproject.reports.service.ResultsPostProcessor;
import org.sakaiproject.reports.logic.impl.BaseResultPostProcessor;

/**
 * Created by IntelliJ IDEA.
 * User: John Ellis
 * Date: Dec 25, 2005
 * Time: 6:43:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class CsvAdvancedResultPostProcessor extends BaseResultPostProcessor implements ResultsPostProcessor {

	public byte[] postProcess(String fileData) {
		Document results = getDocument(fileData);

		ByteArrayOutputStream os = new ByteArrayOutputStream(fileData.length());

		try {
			Element[][] orderedHeaders = processDocumentHeaders(results);
			Element[][] orderedData = processDocumentData(results);

			createHeaderArea(orderedHeaders, os);
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

	protected void createHeaderArea(Element[][] orderedHeaders, OutputStream os) throws IOException {
		for (int i=0;i<orderedHeaders.length;i++) {
			Element[] header = orderedHeaders[i];
			createHeaderRow(header, os);
			os.write('\n');
		}
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
	}

	protected void writeHeaderValue(Element column, OutputStream os) throws IOException {
		os.write('"');
		os.write(escapeValue(column.getTextNormalize()).getBytes("UTF-8"));
		os.write('"');
	}

	protected Element[][] processDocumentHeaders(Document results) throws DataConversionException {
		Iterator<Element> i = results.getRootElement().getDescendants(new AttributeFilter("class", "exportHeaderRow")); 
		List<Element[]> retList = new ArrayList<Element[]>();

		while (i.hasNext()) {
			Element column = (Element) i.next();
			retList.add(processHeader(column));
		}

		return retList.toArray(new Element[retList.size()][]);
	}

	protected Element[][] processDocumentData(Document results)
	throws DataConversionException {
		Iterator<Element> i = results.getRootElement().getDescendants(new AttributeFilter("class", "exportDataRow")); 
		List<Element[]> retList = new ArrayList<Element[]>();

		while (i.hasNext()) {
			Element column = (Element) i.next();
			retList.add(processRow(column));
		}

		return retList.toArray(new Element[retList.size()][]);

	}

	protected Element[] processHeader(Element row) throws DataConversionException {
		Iterator<Element> i = row.getDescendants(new AttributeFilter("class", "exportHeader")); 
		List<Element> retList = new ArrayList<Element>();

		while (i.hasNext()) {
			Element column = (Element) i.next();
			int colSpan = Integer.parseInt(column.getAttributeValue("colspan", "1"));
			for (int count=1; count<=colSpan; count++) {
				retList.add(column);
			}
		}

		return retList.toArray(new Element[retList.size()]);
	}
	
	protected Element[] processRow(Element row) throws DataConversionException {
		Iterator<Element> i = row.getDescendants(new AttributeFilter("class", "exportDataCol")); 
		List<Element> retList = new ArrayList<Element>();

		while (i.hasNext()) {
			Element column = (Element) i.next();
			retList.add(column);
		}

		return retList.toArray(new Element[retList.size()]);
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
		
		excelText = excelText.replaceAll(String.valueOf((char) new Integer(160).intValue()), "");		

		return excelText;
	}


	private class AttributeFilter implements Filter {

		private String attributeName;
		private String attributeValue;

		public AttributeFilter(String attributeName, String attributeValue) {
			this.attributeName = attributeName;
			this.attributeValue= attributeValue;
		}

		public boolean matches(Object obj) {
			if (!(obj instanceof Element)) return false;
			Element elm = (Element) obj;
			String attVal = elm.getAttributeValue(attributeName);
			return (attVal != null && attVal.contains(attributeValue));
		}

		public String getAttributeName() {
			return attributeName;
		}

		public void setAttributeName(String attributeName) {
			this.attributeName = attributeName;
		}

		public String getAttributeValue() {
			return attributeValue;
		}

		public void setAttributeValue(String attributeValue) {
			this.attributeValue = attributeValue;
		}

	}

}
