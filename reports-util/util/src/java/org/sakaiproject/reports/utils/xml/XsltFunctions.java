/*******************************************************************************
 * $URL: https://source.sakaiproject.org/svn/oncourse/branches/metaobj_2-5-x/metaobj-util/tool-lib/src/java/org/sakaiproject/metaobj/utils/xml/XsltFunctions.java $
 * $Id: XsltFunctions.java 22432 2007-03-12 16:32:07Z john.ellis@rsmart.com $
 * **********************************************************************************
 *
 *  Copyright (c) 2004, 2005, 2006 The Sakai Foundation.
 *
 *  Licensed under the Educational Community License, Version 1.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.opensource.org/licenses/ecl1.php
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/

package org.sakaiproject.reports.utils.xml;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: johnellis
 * Date: Nov 20, 2006
 * Time: 8:27:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class XsltFunctions {

	private static final String DATE_TIME_FORMAT = "EEE MMM d HH:mm:ss z yyyy";
	private static final Format dateTimeFormat = new SimpleDateFormat(DATE_TIME_FORMAT);


	public static double sqrt(double num) {
		return Math.sqrt(num);
	}


	/**
	 * <modified>Tue Nov 11 12:37:41 EST 2008</modified>
	 * @param date
	 * @return
	 */
	public static long getSortableDate(String date) {
		Date dateObject = null;
		if (date == null)
			return -1;
		
		try {
			dateObject = (Date) dateTimeFormat.parseObject(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(dateObject);

		return cal.getTimeInMillis();

	}
}
