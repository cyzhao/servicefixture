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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import net.servicefixture.ServiceFixtureException;

/**
 * Provides conversion logic for <code>java.util.Date</code>
 * 
 * @author Chunyun Zhao
 * @since Jul 28, 2006
 */
public class DateConverter implements Converter {
    /* 2005-12-15T09:35:40.359-05:00 */
    final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    final static String DATE_XSD_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    final static String DATE_ONLY_FORMAT = "yyyy-MM-dd";
    final static String DATE_TOSTRING_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
    final static String[] SUPPORTED_DATE_FORMAT = new String[] {
            DATE_FORMAT, DATE_XSD_FORMAT, DATE_TOSTRING_FORMAT, DATE_ONLY_FORMAT};
    
	public Object fromObject(Object source) {
        if (source == null) {
            return null;
        } else if (source instanceof Date) {
            return source;
        } else if (source instanceof Calendar) {
            return ((Calendar)source).getTime();
        }

        Date date = null;
        for (int i = 0; i < SUPPORTED_DATE_FORMAT.length; i++) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(SUPPORTED_DATE_FORMAT[i]);
                // Only formats the date in GMT.
                formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
                date = formatter.parse((String)source);
                break;
            } catch (ParseException ignoreIt) {
            }
        }
        if ( date == null ) {
            throw new ServiceFixtureException(
                    "Unable to parse the date:" + source);
        }
        
        return date;
	}

	public String toString(Object source) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String result = formatter.format((Date)source);
        return result;
	}

}
