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
package net.servicefixture;

import net.servicefixture.context.GlobalContext;
import net.servicefixture.converter.ObjectConverter;
import net.servicefixture.jexl.JexlEvaluator;
import net.servicefixture.util.CompareUtils;

import org.apache.commons.jexl.JexlContext;
import org.apache.commons.lang.StringUtils;

import fit.Fixture;
import fit.Parse;

/**
 * Checks the response with expected data in the test table.
 * 
 * @author Chunyun Zhao
 * @since Dec 21, 2005
 */
public class ResponseChecker {
	private JexlContext jexlContext;

	private Fixture fixture;

	public ResponseChecker(Fixture fixture) {
		jexlContext = GlobalContext.getGlobalJexlContext();
		this.fixture = fixture;
	}

	@SuppressWarnings("unchecked")
	public void setResponse(Object response) {
		// This will overwrite previous response in the global context.
		jexlContext.getVars().put(GlobalContext.RESPONSE_TOKEN, response);
	}

	@SuppressWarnings("unchecked")
	public void setException(Throwable e) {
		jexlContext.getVars().put(GlobalContext.EXCEPTION_TOKEN, e);
	}

	/**
	 * Evaluates the jexl expression and checks the result against the expected
	 * data.
	 * 
	 * @param cells
	 */
	public void check(String[] dataCells, Parse outputCell) {
        String expression = dataCells[0];
        String expected = dataCells[1];

        Object responseData = JexlEvaluator.evaluate(expression, jexlContext);
        String responseString = toEscapedString(responseData);

        if (StringUtils.isEmpty(expected)) {
            outputCell.addToBody(responseString);
            return;
        } 
        
        boolean equals = false;
        if (ObjectConverter.isExpression(expected)) {
    		Class targetClass;
    		if (responseData != null) {
    			targetClass = responseData.getClass();
    		} else {
    			targetClass = String.class;
    		}
    		Object expectedObject = ObjectConverter.convert(expected,
    				targetClass, false);
    		String expectedString = toEscapedString(expectedObject);
    		outputCell.addToBody(" ==> " + expectedString);
    		equals = CompareUtils.compare(responseData, expectedObject);
    	} else {
    		equals = responseString.equals(expected);
    	}
        		
        if (equals) {
        	fixture.right(outputCell);
        } else {
            fixture.wrong(outputCell);
            outputCell.addToBody(Fixture.label("expected") + "<hr>" + responseString + Fixture.label("actual"));
        }
    }
	
	private String toEscapedString(Object object) {
		String result = ObjectConverter.toString(object);
		if ( result == null ) {
			return "";
		}
		return Fixture.escape(result);
	}
}
