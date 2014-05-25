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
package net.servicefixture.parser;


/**
 * @author Chunyun Zhao
 */
public class TestCollectionBuilder extends AbstractCollectionBuilder {
	private static final String VALUE = "value";
	
	public Object getCollection() {
		String[] values = (String[])objectList.toArray(new String[objectList.size()]);
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			result.append(values[i]);
			if ( i < values.length - 1) {
				result.append("|"); 
			}
		}
		return result.toString(); 
	}

	public boolean isCollection(String nodeName, Class nodeType) {
		return String.class.equals(nodeType) && isValuePattern(nodeName);
	}

	public Class getElementType(String nodeName, Class nodeType) {
		return String.class;
	}
	
	public String getCollectionAttributeName(String nodeName) {
		if ( isValuePattern(nodeName) ) {
			return VALUE;
		}
		return nodeName;
	}

	public boolean isCollectionElement(String nodeName) {
		if ( isValuePattern(nodeName) ) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true if the node name is value1, value2, ...
	 * 
	 * @param nodeName the node name
	 */
	public static boolean isValuePattern(String nodeName) {
		if ( nodeName != null && nodeName.startsWith(VALUE) && nodeName.length() > VALUE.length()) {
			String digits = nodeName.substring(VALUE.length());
			try {
				Integer.parseInt(digits);
			} catch (NumberFormatException e) {
				return false;
			}
			return true;
		}
		return false;
	}
}

