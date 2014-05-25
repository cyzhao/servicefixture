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
package net.servicefixture.fitnesse;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import net.servicefixture.ServiceFixture;
import net.servicefixture.parser.CollectionBuilderFactory;
import net.servicefixture.parser.TreeParser;
import net.servicefixture.util.ReflectionUtils;

import org.apache.commons.lang.StringUtils;

/**
 * Fitnesse extension that generates ServiceFixture template in Fitnesse edit page
 * if the selected fixture class extends from <code>ServiceFixture</code>. Default 
 * fitnesse template creator will be used otherwise.
 * 
 * @author Chunyun Zhao
 * @since Mar 20, 2006
 * 
 */
public class FixtureTemplateCreator extends fitnesse.FixtureTemplateCreator {
	static final String TEMPLATE_CREATOR_FLAG = "TEMPLATE_CREATOR_FLAG";
	
	public static boolean isGeneratingFixtureTemplate() {
		return System.getProperty(TEMPLATE_CREATOR_FLAG) != null;
	}
	
	public static void main(String args[]) throws Exception {
		if (args.length < 1) {
			return;
		} else {
			new FixtureTemplateCreator().generateTemplate(args[0]);
			return;
		}
	}

	public void generateTemplate(String fixtureClass) throws Exception {
		try {
			Class clazz = ClassLoader.getSystemClassLoader().loadClass(fixtureClass);
			if (ServiceFixture.class.isAssignableFrom(clazz)) {
				writeLine("!|" + fixtureClass + "|");
				generateServiceFixtureTemplate(clazz);
			} else {
				// Support fitnesse built in fixtures.
				super.run(fixtureClass);
			}
		} catch (ClassNotFoundException e) {
			writeLine("# Could not find " + fixtureClass + " in the classpath. #");
		}
	}
	
	/**
	 * Generates the test table template for the ServiceFixture and prints out
	 * to standard console.
	 *
	 * @param clazz
	 */
	private void generateServiceFixtureTemplate(Class clazz) throws Exception {
		ServiceFixture fixture = (ServiceFixture)clazz.newInstance();
		Method operation = fixture.getServiceOperation();
		if ( operation == null ) {
			writeLine("Unable to generate template for fixture:" + fixture.getClass().getName() + " because service operation is unknown.");
			return;
		}
		Class[] parameterTypes = operation.getParameterTypes();
		Class returnType = operation.getReturnType();
		for (int i = 0; i < parameterTypes.length; i++) {
			String prefix = "|set|" + getParameterName(parameterTypes[i]);
            Stack<Class> ancestors = new Stack<Class>();
            ancestors.push(CollectionBuilderFactory.getElementType(parameterTypes[i]));
			addTableRowsForType(ancestors, prefix, CollectionBuilderFactory.getElementType(parameterTypes[i]), true);
		}
		
		writeLine("|invoke||");
		
		if ( "void".equals(returnType.getName())) {
			writeLine("|check|response == null|true|");
		} else {
			writeLine("|check|response != null|true|");
			
			String prefix = "|check|response";
			if ( CollectionBuilderFactory.isCollectionType(returnType) ) {
				writeLine("|check|response.size() > 0|true|");
				writeLine("|check|response.size()||");
				prefix = prefix + "[0]";
			}
			Stack<Class> ancestors = new Stack<Class>();
			ancestors.push(CollectionBuilderFactory.getElementType(returnType));
			addTableRowsForType(ancestors, prefix, CollectionBuilderFactory.getElementType(returnType), false);
		}
	}

	/**
	 * Generates the parameter name based on the type. If the type is an array
	 * type, return the array syntaxed parameter name.
	 *
	 * @param class1
	 * @return
	 */
	private String getParameterName(Class type) {
		//Get the object type if it is the collection type.
		Class objectType = CollectionBuilderFactory.getElementType(type);
		int index = objectType.getName().lastIndexOf(".");
		String varName = objectType.getName().substring(index+1);
		varName = StringUtils.uncapitalize(varName);
		if ( CollectionBuilderFactory.isCollectionType(type) ) {
			varName = varName + "[0]" + ReflectionUtils.getExtraMultiDimensionalArraySuffix(type);
		}
		return varName;
	}

	/**
	 * Adds the tablerow to the test table.
	 * @param ancestors 
	 *
	 * @param string
	 * @param returnType
	 */
	private void addTableRowsForType(Stack<Class> ancestors, String prefix, Class type, boolean isSetRow) {
		if ( ReflectionUtils.isLowestLevelType(type) ) {
			writeLine(prefix + "|" + (isSetRow?ReflectionUtils.getDefaultValue(type):"") + "|");
			return;
		}
		//If the type is an abstract type, generate rows for all of its subclasses.
		if ( ReflectionUtils.isAbstractType(type) ) {
			Class[] subClasses = ReflectionUtils.findSubClasses(type);
			for (int i = 0; i < subClasses.length; i++) {
				if ( isSetRow ) {
					writeLine(prefix + "|" + TreeParser.CLASS_TYPE_PREFIX + subClasses[i].getName() + TreeParser.CLASS_TYPE_SUFFIX + "|");
				} else {
					writeLine(prefix + ".class.name|" + subClasses[i].getName() + "|");
				}
				addTableRowsForType(ancestors, prefix, subClasses[i], isSetRow);
			}
			return;
		}
		
		//Handles regular type
		Map<String, Class> attributes = ReflectionUtils.getAttributes(type);
		for (Iterator iter = attributes.keySet().iterator(); iter.hasNext();) {
			String attributeName = (String)iter.next();
			Class attributeType = (Class)attributes.get(attributeName);
			if ( ReflectionUtils.isLowestLevelType(attributeType) ) {
				//size is the reserved word for common jexl library, call the getter method instead.
				if ( "size".equals(attributeName) && !isSetRow ) {
					attributeName = "getSize()";
				}
				writeLine(prefix + "." + attributeName + "|" + (isSetRow?ReflectionUtils.getDefaultValue(attributeType):"") + "|");
			} else {
                if ( ancestors.contains(attributeType) ) {
                	writeLine(prefix + "." + attributeName + "|" + (isSetRow?"${nil.object}":"") + "|");
                } else {
                    ancestors.push(attributeType);
                    addTableRowsForType(ancestors, prefix + "." + attributeName, attributeType, isSetRow);
                    ancestors.pop();
                }
			}
		}
	}

	private void writeLine(String message) {
		System.out.println(message);
	}
}
