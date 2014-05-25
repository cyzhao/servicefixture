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
package net.servicefixture.converter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

public class ObjectConverterTest extends TestCase {
	public void testGregorianCalendarConverter() {
		String dateStr = "2005-12-12 00:00:00";
		GregorianCalendar calendar = (GregorianCalendar)ObjectConverter.convert(dateStr, GregorianCalendar.class);
		System.out.println(calendar instanceof Calendar);
		assertEquals(2005, calendar.get(GregorianCalendar.YEAR));
	}
	
	@SuppressWarnings("deprecation")
	public void testDateConverter() throws Exception {
		String dateStr = "2005-07-01 00:00:00";
		Date date = (Date)ObjectConverter.convert(dateStr, Date.class);
		if ( date.getTimezoneOffset() > 0 ) {
			assertEquals("The date of month should be 30.", date.getDate(), 30);
		}
		//Now convert it back to GMT date string, has to be the same as original.
		assertEquals(dateStr, ObjectConverter.toString(date));
	}
	
	public void testSqlDateConverter() throws Exception {
		String dateStr = "2005-07-01 00:00:00";
		Date date = (Date)ObjectConverter.convert(dateStr, java.sql.Date.class);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if ( calendar.get(Calendar.ZONE_OFFSET) > 0 ) {
			assertEquals("The date of month should be 30.", calendar.get(Calendar.DAY_OF_MONTH), 30);
		}
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		//Now convert it back to GMT date string, has to be the same as original.
		assertEquals(dateStr, ObjectConverter.toString(sqlDate));
	}
	
	public void testInvlidDate() {
		String dateStr = "2008-22-88T08:30:47.0Z";
		Date date = (Date)ObjectConverter.convert(dateStr, Date.class);
		System.out.println(date);
	}
	
	@SuppressWarnings("deprecation")
	public void testCalendarConverter() throws Exception {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.MILLISECOND, 0);
		String dateStr = ObjectConverter.toString(today);
		SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		Date date = formatter.parse(dateStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		assertEquals(date.getTime() - today.getTimeInMillis(), date.getTimezoneOffset() * 60 * 1000);
	}
}
