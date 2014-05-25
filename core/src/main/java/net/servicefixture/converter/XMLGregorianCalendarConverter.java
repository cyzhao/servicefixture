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
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.beanutils.ConvertUtils;

/**
 * Converts XMLGregorianCalendar.
 *
 * @author Chunyun Zhao
 * @author Niklas Gustavsson
 * @since Oct 2, 2006
 */
public class XMLGregorianCalendarConverter implements Converter {
	public Object fromObject(Object source) {
        if (source == null) {
            return null;
        }
        if ( source instanceof XMLGregorianCalendar ) {
        	return source;
        } else if (source instanceof Calendar) {
        	return calendarToXMLGregorianCalendar((Calendar)source);
        } else if ( source instanceof Date) {
        	return dateToXMLGregorianCalendar((Date)source);
        } else {
        	Date date = (Date) ConvertUtils.convert(source.toString(), Date.class);
        	return dateToXMLGregorianCalendar(date);
        }
	}

	private Object dateToXMLGregorianCalendar(Date source) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(source);
		return calendarToXMLGregorianCalendar(cal);
	}

	private XMLGregorianCalendar calendarToXMLGregorianCalendar(Calendar calendar) {
		XMLGregorianCalendar xmlCal;
		try {
			xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar();
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException("Failed to create XMLGregorianCalendar", e);
		}

		xmlCal.setYear(calendar.get(Calendar.YEAR));
		xmlCal.setMonth(calendar.get(Calendar.MONTH) + 1);
		xmlCal.setDay(calendar.get(Calendar.DAY_OF_MONTH));
		xmlCal.setHour(calendar.get(Calendar.HOUR));
		xmlCal.setMinute(calendar.get(Calendar.MINUTE));
		xmlCal.setSecond(calendar.get(Calendar.SECOND));
		xmlCal.setMillisecond(calendar.get(Calendar.MILLISECOND));
		return xmlCal;
	}

	public String toString(Object source) {
		XMLGregorianCalendar src = (XMLGregorianCalendar)source;
        SimpleDateFormat formatter = new SimpleDateFormat(DateConverter.DATE_FORMAT);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, src.getYear());
        cal.set(Calendar.MONTH, src.getMonth() - 1);
        cal.set(Calendar.DAY_OF_MONTH, src.getDay());
        cal.set(Calendar.HOUR, src.getHour());
        cal.set(Calendar.MINUTE, src.getMinute());
        cal.set(Calendar.SECOND, src.getSecond());
        cal.set(Calendar.MILLISECOND, src.getMillisecond());
        return formatter.format(cal.getTime());
    }
}
