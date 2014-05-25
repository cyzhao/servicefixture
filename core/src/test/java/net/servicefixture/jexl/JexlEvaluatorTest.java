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
package net.servicefixture.jexl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import net.servicefixture.util.TestData;
import net.servicefixture.util.TestExpressionTransformer;
import net.servicefixture.util.TestUtil;

import org.apache.commons.jexl.JexlContext;

public class JexlEvaluatorTest extends TestCase {
	static {
		JexlEvaluator.registerSpecialToken("util", new TestUtil());
		JexlEvaluator.registerExpressionTransformer(new TestExpressionTransformer());
	}
	
	public void testRandomString() {
		JexlContext context = JexlEvaluator.createJexlContext();
		String value = (String)JexlEvaluator.evaluate("random.string", context);
		assertNotNull(value);
	}
	public void testCurrentDate() {
		JexlContext context = JexlEvaluator.createJexlContext();
		Object o = JexlEvaluator.evaluate("calendar.now", context);
		assertTrue(o instanceof Calendar);
	}
	public void testCurrentDateAfter() {
		JexlContext context = JexlEvaluator.createJexlContext();
		Object o = JexlEvaluator.evaluate("calendar.after(2)", context);
		assertTrue(o instanceof Calendar);
	}
	public void testRandomNumber() {
		JexlContext context = JexlEvaluator.createJexlContext();
		Object o = JexlEvaluator.evaluate("random.digits(5)", context);
		assertNotNull(o);
	}
	@SuppressWarnings("unchecked")
	public void testStaticCallInExpression() {
		JexlContext context = JexlEvaluator.createJexlContext();
		context.getVars().put("value", "data1|data2");
		Object o1 = JexlEvaluator.evaluate("util.decode(value).0", context);
		Object o2 = JexlEvaluator.evaluate("util.decode(value).1", context);
		assertEquals("data1", o1);
		assertEquals("data2", o2);
	}
	
	@SuppressWarnings("unchecked")
	public void testExpressionTranformer() {
		JexlContext context = JexlEvaluator.createJexlContext();
		context.getVars().put("test", new TestData("data1|data2"));
		Object o1 = JexlEvaluator.evaluate("util.decode(test.value).0", context);
		Object o2 = JexlEvaluator.evaluate("util.decode(test.value).1", context);
		assertEquals("data1", o1);
		assertEquals("data2", o2);
		Object o3 = JexlEvaluator.evaluate("test.value1", context);
		Object o4 = JexlEvaluator.evaluate("test.value2", context);
		assertEquals("data1", o3);
		assertEquals("data2", o4);
	}
	
	@SuppressWarnings("unchecked")
	public void testEvaluateList() {
		List<String> list = new ArrayList<String>();
		list.add("data1");
		JexlContext context = JexlEvaluator.createJexlContext();
		context.getVars().put("list", list);
		assertEquals("data1", JexlEvaluator.evaluate("list[0]", context));
		
		Set<String> set = new HashSet<String>();
		set.add("data1");
		context.getVars().put("set", set);
		assertEquals("data1", JexlEvaluator.evaluate("set.toArray().0", context));
	}
}
