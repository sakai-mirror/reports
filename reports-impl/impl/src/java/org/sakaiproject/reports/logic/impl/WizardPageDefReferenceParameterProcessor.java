/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2009 The Sakai Foundation.
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

import org.sakaiproject.metaobj.shared.mgt.IdManager;
import org.sakaiproject.reports.service.ParameterResultsPostProcessor;
import org.theospi.portfolio.matrix.MatrixManager;
import org.theospi.portfolio.matrix.model.WizardPageDefinition;

public class WizardPageDefReferenceParameterProcessor implements
		ParameterResultsPostProcessor {

	
	private MatrixManager matrixManager = null;
	private IdManager idManager = null;
	
	/**
	 * Turn the passed id into a reference
	 */
	public String process(String input) {
		String ref = null;
		
		WizardPageDefinition wpd = getMatrixManager().getWizardPageDefinition(getIdManager().getId(input));
		ref = wpd.getReference();
		
		return ref;
	}

	public void setIdManager(IdManager idManager) {
		this.idManager = idManager;
	}

	public IdManager getIdManager() {
		return idManager;
	}

	public void setMatrixManager(MatrixManager matrixManager) {
		this.matrixManager = matrixManager;
	}

	public MatrixManager getMatrixManager() {
		return matrixManager;
	}

}
