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

import org.apache.commons.beanutils.ConvertUtils;

/**
 * Provides conversion logic for <code>java.util.Calendar</code>
 * 
 * @author Chunyun Zhao
 * @since Jul 28, 2006
 */
public class CalendarConverter implements Converter {

	public Object fromObject(Object source) {
        if (source == null) {
            return null;
        }
        if (source instanceof Calendar) {
            return source;
        }

        Date date = (Date) ConvertUtils.convert(source.toString(),
                Date.class);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());

        return calendar;
	}

	public String toString(Object source) {
        SimpleDateFormat formatter = new SimpleDateFormat(DateConverter.DATE_FORMAT);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return formatter.format(((Calendar)source).getTime());
	}
}
