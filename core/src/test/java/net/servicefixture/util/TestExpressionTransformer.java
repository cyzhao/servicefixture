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

import net.servicefixture.jexl.ExpressionTransformer;

public class TestExpressionTransformer implements ExpressionTransformer {
	private static final String VALUE = "value";
	
	public String transform(String expression) {
		int lastIndex = expression.lastIndexOf(".");
		String lastNode = null;
		if ( lastIndex > 0 ) {
			lastNode = expression.substring(lastIndex+1);
			if ( isValuePattern(lastNode) ) {
				String start = expression.substring(0, lastIndex);
				int index = Integer.parseInt(lastNode.substring(VALUE.length()));
				return "util.decode(" + start + "." + VALUE + ")." + (index - 1);
			}
		} 
		return expression;
	}
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
