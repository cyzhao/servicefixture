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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.servicefixture.ServiceFixtureException;

import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;

/**
 * Encapsulates Jexl integration code.
 * <p>
 * 
 * @author Chunyun Zhao
 * @since Dec 21, 2005
 */
public final class JexlEvaluator {
    private JexlEvaluator() {
    }

    /**
     * Creates a Jexl context.
     */
    @SuppressWarnings("unchecked")
	public static JexlContext createJexlContext() {
        JexlContext context = JexlHelper.createContext();
        context.getVars().putAll(registeredVars);
        return context;
    }

    /**
     * Evaluates the expression against the context.
     */
    public static Object evaluate(String expression, JexlContext jexlContext) {
    	String transformedExpression = expression;
    	for (Iterator iter = registeredExpressionTransforms.iterator(); iter.hasNext();) {
			ExpressionTransformer transformer = (ExpressionTransformer) iter.next();
			transformedExpression = transformer.transform(transformedExpression);
		}
    	
        try {
            Expression jexlExpression = ExpressionFactory
                    .createExpression(transformedExpression);
            return jexlExpression.evaluate(jexlContext);
        } catch (Exception e) {
            throw new ServiceFixtureException("Unable to evaluate expression:"
                    + transformedExpression, e);
        }
    }
    
    /**
     * This allows user to register special variable to Jexl context to
     * handle special tokens. For instance: nil.object, current.date,
     * random.string etc.
     *
     * @param key
     * @param object
     */
    public static void registerSpecialToken(String key, Object object) {
        registeredVars.put(key, object);
    }
    
    /**
     * This allows user to register expression transformers in case expression
     * need to tranformed before it gets evaluated.
     * 
     * @param transformer
     */
    public static void registerExpressionTransformer(ExpressionTransformer transformer) {
    	registeredExpressionTransforms.add(transformer);
    }
    
    private static Map<String, Object> registeredVars = new HashMap<String, Object>();
    private static List<ExpressionTransformer> registeredExpressionTransforms = new ArrayList<ExpressionTransformer>();
    
    //Register built in special variables
    static {
        registerSpecialToken("nil", new NilToken());
        registerSpecialToken("calendar", new CalendarToken());
        registerSpecialToken("date", new DateToken());
        registerSpecialToken("random", new RandomToken());
        registerSpecialToken("con", new ConditionToken());
    }
    
}
