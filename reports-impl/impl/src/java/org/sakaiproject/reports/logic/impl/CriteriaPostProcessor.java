/**********************************************************************************
 * $URL:https://source.sakaiproject.org/svn/osp/trunk/reports/api-impl/src/java/org/theospi/portfolio/reports/model/impl/LoadArtifactResultProcessor.java $
 * $Id:LoadArtifactResultProcessor.java 9134 2006-05-08 20:28:42Z chmaurer@iupui.edu $
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

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.sakaiproject.entity.api.Reference;
import org.sakaiproject.entity.cover.EntityManager;
import org.sakaiproject.metaobj.shared.mgt.IdManager;
import org.sakaiproject.reports.model.ReportResult;
import org.theospi.portfolio.matrix.MatrixManager;
import org.theospi.portfolio.matrix.model.ScaffoldingCell;

/**
 * To use this class you need to place the resource UUID into a column of the report query.
 * The column needs to be then labeled with the ending "_SORTNAME".  Then include this class
 * as a post processor for a report.  The result of this post processing class is to place 
 * the display name into the column.
 *                
 * Keep in mind the case of the column labels.  the default behavior is to uppercase the 
 * labels to standardize labels between various databases.  Thus if you turn off the forcing
 * of upper case then the "_artifact" label ending needs to be either all lowercase or 
 * all uppercase.
 * 
 * Created by IntelliJ IDEA.
 * User: John Ellis
 * Date: Dec 22, 2005
 * Time: 5:31:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class CriteriaPostProcessor extends BaseResultProcessor {

	private String columnNamePattern = ".*_CRITERIAREF";

	private MatrixManager matrixManager = null;
	private IdManager idManager = null;


	/**
	 * Post Processor method
	 */
	public ReportResult process(ReportResult result) {
		Document rootDoc = getResults(result);
		List<Element> data = rootDoc.getRootElement().getChild("data").getChildren("datarow");

		for (Element dataRow : data) {
			processRow(dataRow);
		}

		return setResult(result, rootDoc);
	}

	/**
	 * find the artifact columns, stores them into a map (artifactsToLoad)
	 * also puts the column into a list so as the artifact is loaded into it.
	 * @param dataRow
	 * @param artifactsToLoad
	 */
	protected void processRow(Element dataRow) {
		List<Element> columns = dataRow.getChildren("element");

		for (Element data : columns) {
			if (isArtifactColumn(data) && !isColumnNull(data)) {

				Element criteriaRefElm = new Element("criteriaRef");

				String criteriaRef = getColumnData(data);
				Element attr = new Element("ref");
				attr.setText(criteriaRef);
				criteriaRefElm.addContent(attr);

				Reference ref = EntityManager.newReference(criteriaRef);
				ScaffoldingCell sc = getMatrixManager().getScaffoldingCellByWizardPageDef(getIdManager().getId(ref.getId()));

				attr = new Element("row");
				attr.setText(sc.getRootCriterion().getId().getValue());
				criteriaRefElm.addContent(attr);

				attr = new Element("col");
				attr.setText(sc.getLevel().getId().getValue());
				criteriaRefElm.addContent(attr);



				data.removeContent();
				data.addContent(criteriaRefElm);
			}
		}
	}


	protected String getColumnData(Element data) {
		return data.getTextNormalize();
	}

	protected boolean isArtifactColumn(Element data) {
		String columnName = data.getAttributeValue("colName");
		return columnName.matches(getColumnNamePattern());
	}

	public String getColumnNamePattern() {
		return columnNamePattern;
	}

	public void setColumnNamePattern(String columnNamePattern) {
		this.columnNamePattern = columnNamePattern;
	}


	public void setMatrixManager(MatrixManager matrixManager) {
		this.matrixManager = matrixManager;
	}

	public MatrixManager getMatrixManager() {
		return matrixManager;
	}

	public void setIdManager(IdManager idManager) {
		this.idManager = idManager;
	}

	public IdManager getIdManager() {
		return idManager;
	}
}
