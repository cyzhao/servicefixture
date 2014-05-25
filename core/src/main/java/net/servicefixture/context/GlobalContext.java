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
package net.servicefixture.context;


import net.servicefixture.jexl.JexlEvaluator;

import org.apache.commons.jexl.JexlContext;
import org.apache.commons.lang.StringUtils;


/**
 * Maintains all the request and response data of test tables that have 
 * testTableKey row defined in an global JexlContext.
 * <p>
 * The internal JexlContext is then used by <code>ObjectConverter</code> to 
 * convert an expression to an object.
 * 
 * @author Chunyun Zhao
 * @since Dec 21, 2005
 */
public final class GlobalContext {
	public static final String RESPONSE_TOKEN = "response";
    public static final String EXCEPTION_TOKEN = "exception";
    
    private static JexlContext jexlContext = JexlEvaluator.createJexlContext();

    /**
     * Stores the context of test table indentified by testTableKey in the
     * global context.
     */
    @SuppressWarnings("unchecked")
	public static void storeTableContext(String testTableKey, TableContext context) {
        if (!StringUtils.isEmpty(testTableKey)) {
            jexlContext.getVars().put(testTableKey, context);
        }
    }

    @SuppressWarnings("unchecked")
	public static void putVar(String key, Object object) {
    	if (!StringUtils.isEmpty(key)) {
    		jexlContext.getVars().put(key, object);
    	}
    }
    
    /**
     * Returns the jexlContext.
     */
    public static JexlContext getGlobalJexlContext() {
        return jexlContext;
    }

    /**
     * Clears all the vars in jexlContext.
     */
    public static void clear() {
        jexlContext.getVars().clear();
    }
    
    /**
     * Removes the response or exception of current test table.
     */
    public static void clearLocalResponse() {
    	GlobalContext.getGlobalJexlContext().getVars().remove(RESPONSE_TOKEN);
    	GlobalContext.getGlobalJexlContext().getVars().remove(EXCEPTION_TOKEN);
    }
}
