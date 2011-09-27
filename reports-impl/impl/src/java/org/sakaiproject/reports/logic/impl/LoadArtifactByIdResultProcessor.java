/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2008 The Sakai Foundation.
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

import java.util.Iterator;

import org.jdom.Element;
import org.sakaiproject.content.cover.ContentHostingService;
import org.sakaiproject.metaobj.shared.ArtifactFinder;
import org.sakaiproject.metaobj.shared.EntityContextFinder;
import org.sakaiproject.metaobj.shared.mgt.PresentableObjectHome;
import org.sakaiproject.metaobj.shared.model.Artifact;
import org.sakaiproject.metaobj.shared.model.Id;
import org.sakaiproject.reports.model.ReportResult;

public class LoadArtifactByIdResultProcessor extends LoadArtifactResultProcessor {

	protected void loadArtifact(ReportResult results, ArtifactHolder holder) {

		ArtifactFinder finder = getArtifactFinderManager().getArtifactFinderByType(holder.artifactType);

		Artifact art;
		Id uuid = getIdManager().getId(ContentHostingService.getUuid(holder.artifactId.getValue()));
		if (finder instanceof EntityContextFinder) {
			//String uri = ContentHostingService.resolveUuid(holder.artifactId.getValue());
			String hash = getReportsManager().getReportResultKey(
					results, ContentHostingService.getReference(holder.artifactId.getValue()));
			art = ((EntityContextFinder)finder).loadInContext(uuid,
					ReportsEntityProducer.REPORTS_PRODUCER,
					uuid.getValue(), hash);
		}
		else {
			art = finder.load(uuid);
		}
		if(art != null) {
			PresentableObjectHome home = (PresentableObjectHome)art.getHome();
			Element xml = home.getArtifactAsXml(art);

			// replace the artifact uuid with the actual xml
			for (Iterator i=holder.reportElements.iterator();i.hasNext();) {
				xml = (Element)xml.clone();
				Element element = (Element) i.next();
				element.removeContent();
				element.addContent(xml);

				// each element can only have one parent
				//xml = (Element)xml.clone();
			}
		} else {
			logger.debug("Artifact '" + holder.artifactId.toString() + "' of type '" + holder.artifactType + "' was not loaded");
		}
	}
}
