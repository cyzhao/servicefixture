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
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * The extention point where customized comparison logic can be applied
 * for custom types.
 * <p>
 * A CalendarComparator is provided to compare the time at second level.
 * 
 * @author Chunyun Zhao
 * @since Feb 10, 2006
 */
public class CompareUtils {
	private static Map<Class<?>, Comparator<Object>> registeredComparators = new HashMap<Class<?>, Comparator<Object>>();
	
	/**
	 * Compares the actual with expected object. 
	 * 
	 * @param actual the actual object
	 * @param expected the expected object
	 * @return true if two objects equals, otherwise, returns false
	 */
	public static boolean compare(Object actual, Object expected) {
		if ( actual == null || expected == null ) {
			if ( actual == expected ) {
				return true;
			} else {
				return false;
			}
		}
		for (Iterator iter = registeredComparators.keySet().iterator(); iter.hasNext(); ) {
			Class<?> clazz = (Class<?>)iter.next();
			if ( clazz.isAssignableFrom(actual.getClass()) ) {
				Comparator<Object> comparator = registeredComparators.get(clazz);
				if (comparator.compare(actual, expected) == 0) {
					return true;
				} else {
					return false;
				}
			}
		}
		
		return actual.equals(expected);
	}
	
	public static void register(Comparator<Object> comparator, Class<?> clazz) {
		registeredComparators.put(clazz, comparator);
	}
	
	static {
		register(new ToStringComparator<Object>(), Calendar.class);
		register(new ToStringComparator<Object>(), Date.class);
	}
}
