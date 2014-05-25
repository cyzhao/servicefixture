/*
 * Copyright 2006 (C) Chunyun Zhao(Chunyun.Zhao@gmail.com).
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *	http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package net.servicefixture.util;

import java.util.Calendar;
import java.util.TimeZone;

import junit.framework.TestCase;
import net.servicefixture.converter.ObjectConverter;

public class CompareUtilsTest extends TestCase {
	//This test is to ensure normal comparison still works.
	public void testCompareString() {
		String str1 = "test";
		String str2 = "test";
		assertTrue(CompareUtils.compare(str1, str2));
	}

	public void testCalendarComparator1() {
		String data = "2005-12-12 00:00:00";
		Calendar calendar1 = (Calendar)ObjectConverter.convert(data, Calendar.class);
		Calendar calendar2 = (Calendar)ObjectConverter.convert(data, Calendar.class);
		calendar1.add(Calendar.MILLISECOND, 100);
		assertTrue(CompareUtils.compare(calendar1, calendar2));
	}
	
	public void testCalendarComparator2() {
		String dateStr = "2006-02-12T11:24:34";
		Calendar cal1 = (Calendar)ObjectConverter.convert(dateStr, Calendar.class);

		//Sets up the GMT calendar at 2006-01-12 11:24:00.234
		Calendar cal2 = Calendar.getInstance();
		cal2.set(Calendar.YEAR, 2006);
		cal2.set(Calendar.MONTH, 1);
		cal2.set(Calendar.DATE, 12);
		cal2.set(Calendar.HOUR, 11);
        cal2.set(Calendar.AM_PM, 0);
		cal2.set(Calendar.MINUTE, 24);
		cal2.set(Calendar.SECOND, 34);
		cal2.set(Calendar.MILLISECOND, 234);
		cal2.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		assertTrue(CompareUtils.compare(cal1,cal2));
	}
}
