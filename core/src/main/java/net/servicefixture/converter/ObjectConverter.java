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

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import net.servicefixture.ServiceFixtureException;
import net.servicefixture.context.GlobalContext;
import net.servicefixture.jexl.JexlEvaluator;
import net.servicefixture.parser.CollectionBuilderFactory;
import net.servicefixture.util.ReflectionUtils;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;


/**
 * Provides static conversion methods to convert any object type to and from 
 * String.
 * 
 * @author Chunyun Zhao
 * @since Jul 28, 2006
 */
public class ObjectConverter {
    private final static String JEXL_TOKEN_START = "${";
    private final static String JEXL_TOKEN_END = "}";
    private final static String EMPTY_TOKEN = "${empty.collection}";
    
    private final static Map<Class, net.servicefixture.converter.Converter> converters = new HashMap<Class, net.servicefixture.converter.Converter>();

    public static Object convert(String data, Class destType) {
    	return convert(data, destType, true);
    }
    /**
     * Converts string data to java object.
     * 
     * @param data
     * @param destType
     * @return
     */
    public static Object convert(String data, Class destType, boolean checkType) {
        // Convert EMPTY_TOKEN to an empty array
        if (EMPTY_TOKEN.equals(data)
                && CollectionBuilderFactory.isCollectionType(destType)) {
            return CollectionBuilderFactory.createCollectionBuilder(null, null, destType).getCollection();
        }

        // Check if it s a jexl token
        if (isExpression(data)) {
            return evaluateJexlToken(extractExpression(data), destType, checkType);
        }
        if (Calendar.class.isAssignableFrom(destType)) {
            destType = Calendar.class;
        }
        
        // If the destType is not supported by ConvertUtils
        if (ConvertUtils.lookup(destType) == null) {
            return specialConvert(data, destType);
        }
        return ConvertUtils.convert(data, destType);
    }

    public static boolean isExpression(String data) {
        return data.startsWith(JEXL_TOKEN_START)
                && data.endsWith(JEXL_TOKEN_END);
    }

    private static String extractExpression(String data) {
        String expression = data.substring(JEXL_TOKEN_START.length(), data
                .length()
                - JEXL_TOKEN_END.length());

        return expression;
    }

    /**
     * Converts a object to string.
     */
    public static String toString(Object object) {
        return ConvertUtils.convert(object);
    }

    /**
     * Use the jexl expression to get the object from the
     * <code>GlobalContext</code>.
     * 
     * Note: the expression must follow <testTableKey>. <request[i]>|
     * <response>.* pattern.
     * 
     * Throws FixtureException if the destType is not assignable from the object
     * type.
     */
    private static Object evaluateJexlToken(String expression, Class<?> destType, boolean checkType) {
        Object result = JexlEvaluator.evaluate(expression, GlobalContext
                .getGlobalJexlContext());

        if (result != null && checkType) {
        	Class<?> resultType = result.getClass();
            if (!destType.isAssignableFrom(resultType) &&
           		!(destType.isPrimitive() && resultType.getName().equalsIgnoreCase("java.lang." + destType.getName()))) {
                throw new ServiceFixtureException("Expression " + expression
                        + " returns a object(" + resultType.getName() + ") that is not " + destType.getName()
                        + " type.");
            }
        }
        return result;
    }

    /**
     * Converts types that are supported by <code>ConvertUtils</code>.
     * 
     * @param data
     * @param destType
     * @return
     */
    public static Object specialConvert(String data, Class destType) {
        try {
        	//If it is enumeration class.
        	if ( ReflectionUtils.isEnumarationPatternClass(destType) )  {
        		try {
					Field field = destType.getField(data);
					return field.get(null);
				} catch (NoSuchFieldException e) {
					//No such field, meaning the variable name and the data
					//doesn't match, call fromString method.
					return destType.getMethod("fromString", new Class[] { String.class }).invoke(null, new Object[]{data});
				}
        	} 
        	//JDK5.0 enum type
        	if (destType.isEnum() ) {
        		return destType.getMethod("valueOf", String.class).invoke(null, data);
        	}
        } catch (Exception ignoreIt) {
            ignoreIt.printStackTrace();
        }

        throw new ServiceFixtureException("Unable to convert data:" + data
                + " to class:" + destType.getName());
    }
    
    public static void register(final net.servicefixture.converter.Converter converter, Class clazz) {
    	ConvertUtils.register(new Converter() {
			public Object convert(Class type, Object obj) {
				return converter.fromObject(obj);
			}
    		
    	}, clazz);
    	
    	converters.put(clazz, converter);
    }
    
    public static net.servicefixture.converter.Converter lookUpConverter(Class type) {
    	if ( converters.containsKey(type) ) {
    		return (net.servicefixture.converter.Converter)converters.get(type);
    	}
    	//Otherwise, let's check if any super class of type has a converter.
    	for (Iterator iter = converters.keySet().iterator(); iter.hasNext();) {
			Class<?> clazz = (Class<?>) iter.next();
			if ( clazz.isAssignableFrom(type) ) {
				return (net.servicefixture.converter.Converter)converters.get(clazz);
			}
		}
    	return null;
    }

    static {
        register(new DateConverter(), Date.class);
        register(new DateConverter(), java.sql.Date.class);
        register(new CalendarConverter(), Calendar.class);
        register(new XMLGregorianCalendarConverter(), XMLGregorianCalendar.class);
        
        ConvertUtils.register(new StringConverter(), String.class);
    }

    public static class StringConverter implements Converter {
        public Object convert(Class clazz, Object source) {
            if (source == null) {
                return null;
            }
            net.servicefixture.converter.Converter converter = lookUpConverter(source.getClass());
            
            if ( converter != null ) {
            	return converter.toString(source);
            }
            
            return source.toString();
        }
    }
}
