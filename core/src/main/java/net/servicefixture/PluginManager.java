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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

import net.servicefixture.converter.Converter;
import net.servicefixture.converter.ObjectConverter;
import net.servicefixture.jexl.ExpressionTransformer;
import net.servicefixture.jexl.JexlEvaluator;
import net.servicefixture.parser.CollectionBuilder;
import net.servicefixture.parser.CollectionBuilderFactory;
import net.servicefixture.util.CompareUtils;
import net.servicefixture.util.InfoLogger;
import net.servicefixture.util.ReflectionUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is used to provide a central place to register custom plugins. 
 * 
 * @author Chunyun Zhao
 * @since Aug 02, 2006
 */
public class PluginManager {
	private static Log log = LogFactory.getLog(PluginManager.class);
	
	private static final String CONVERTERS_PROPERTY = "servicefixture.plugins.Converters";
	private static final String COMPARATORS_PROPERTY = "servicefixture.plugins.Comparators";
	private static final String COLLECITONBUILDERS_PROPERTY = "servicefixture.plugins.CollectionBuilders";
	private static final String EXPRESSIONTRANSFORMERS_PROPERTY = "servicefixture.plugins.ExpressionTransformers";
	private static final String SPECIALTOKENS_PROPERTY = "servicefixture.plugins.SpecialTokens";
	
	private static final String PLUGIN_DELIMITER = " ";
	private static final String TOKEN_DELIMITER = ":";
	
	/**
	 * Registers custom comparator.
	 * 
	 * @param comparator the comparator implementation.
	 * @param clazz the type to be compared.
	 */
	public static void registerComparator(Comparator<Object> comparator, Class clazz) {
		CompareUtils.register(comparator, clazz);
	}
	/**
	 * Registers custom converter.
	 * 
	 * @param converter the converter implementation
	 * @param clazz the type to be converted
	 */
	public static void registerConverter(Converter converter, Class clazz) {
		ObjectConverter.register(converter, clazz);
	}
	
    /**
     * Registers a special expression token.
     * 
     * @param key the key to be referred in the expression
     * @param object the token implementation
     */
    public static void registerSpecialToken(String key, Object object) {
        JexlEvaluator.registerSpecialToken(key, object);
    }
    
    /**
     * Registers transformer class which will be used to transform the expression
     * before it gets evaluated.
     * 
     * @param transformer
     */
    public static void registerExpressionTransformers(ExpressionTransformer transformer) {
    	JexlEvaluator.registerExpressionTransformer(transformer);
    }
    
	/**
	 * Registers custom collection builder.
	 * 
	 * @param builder 
	 */
	public static void registerCollectionBuilder(CollectionBuilder builder) {
		CollectionBuilderFactory.registerCollectionBuilder(builder);
	}
	
	/**
	 * Loads all the plugins defined in servicefixture_plugins.properties file.
	 */
	static void loadPlugins() {
		Configuration configuration = Configuration.getInstance();
		
		loadConverters(configuration.getProperty(CONVERTERS_PROPERTY));
		loadComparators(configuration.getProperty(COMPARATORS_PROPERTY));
		loadCollectionBuilders(configuration.getProperty(COLLECITONBUILDERS_PROPERTY));
		loadExpressionTransformers(configuration.getProperty(EXPRESSIONTRANSFORMERS_PROPERTY));
		loadSpecialTokens(configuration.getProperty(SPECIALTOKENS_PROPERTY));
	}
	
	private static void loadExpressionTransformers(String value) {
		if ( value == null ) return;
		List<String[]> list = parsePluginValue(value, 1);
		for (String[] tokens : list ) {
			registerExpressionTransformers((ExpressionTransformer)newInstance(tokens[0], ExpressionTransformer.class));
			InfoLogger.log(log, "Registered expression transformer : " + tokens[0]);
		}
	}
	private static void loadSpecialTokens(String value) {
		if ( value == null ) return;
		List<String[]> list = parsePluginValue(value, 2);
		for (String[] tokens : list ) {
			registerSpecialToken(tokens[0], newInstance(tokens[1], Object.class));
			InfoLogger.log(log, "Registered special token : " + tokens[0] + " with class : " + tokens[1]);
		}
	}
	
	private static void loadCollectionBuilders(String value) {
		if ( value == null ) return;
		
		List<String[]> list = parsePluginValue(value, 1);
		for (String[] tokens : list ) {
			registerCollectionBuilder((CollectionBuilder)newInstance(tokens[0], CollectionBuilder.class));
			InfoLogger.log(log, "Registered collection builder : " + tokens[0]);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void loadComparators(String value) {
		if ( value == null ) return;
		
		List<String[]> list = parsePluginValue(value, 2);
		for (String[] tokens : list ) {
			registerComparator((Comparator)newInstance(tokens[0], Comparator.class), newClass(tokens[1]));
			InfoLogger.log(log, "Registered comparator : " + tokens[0] + " for class : " + tokens[1]);
		}
	}
	
	private static void loadConverters(String value) {
		if ( value == null ) return;
		
		List<String[]> list = parsePluginValue(value, 2);
		for (String[] tokens : list ) {
			registerConverter((Converter)newInstance(tokens[0], Converter.class), newClass(tokens[1]));
			InfoLogger.log(log, "Registered converter : " + tokens[0] + " for class : " + tokens[1]);
		}
	}
	
	private static List<String[]> parsePluginValue(String value, int numOfTokens) {
		List<String[]> list = new ArrayList<String[]>();
		StringTokenizer pluginTokenizer = new StringTokenizer(value, PLUGIN_DELIMITER);
		while (pluginTokenizer.hasMoreTokens()) {
			String token = pluginTokenizer.nextToken();
			StringTokenizer tokenizer = new StringTokenizer(token, TOKEN_DELIMITER);
			String[] tokens = new String[tokenizer.countTokens()];
			if ( tokens.length != numOfTokens ) {
				throw new ServiceFixtureException("Invalid plugin value: " + value);
			}
			for (int i = 0; i < tokens.length; i++) {
				tokens[i] = tokenizer.nextToken().trim();
			}
			list.add(tokens);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private static Object newInstance(String classname, Class baseClazz) {
		try {
			Class clazz = ReflectionUtils.newClass(classname);
			if ( baseClazz.isAssignableFrom(clazz) ) {
				return ReflectionUtils.newInstance(clazz);
			}
		} catch (RuntimeException e) {
			throw new ServiceFixtureException("Unable to load plugins due to:" + e.getMessage());
		}
		
		throw new ServiceFixtureException("Unable to load plugins, class " + classname + " doesn't implement " + baseClazz.getName());
	}
	@SuppressWarnings("unchecked")
	private static Class newClass(String classname) {
		try {
			return ReflectionUtils.newClass(classname);
		} catch (RuntimeException e) {
			throw new ServiceFixtureException("Unable to load plugins due to:" + e.getMessage());
		}
	}
}
