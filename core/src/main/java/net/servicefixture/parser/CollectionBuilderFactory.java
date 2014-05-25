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

import java.util.ArrayList;
import java.util.List;

/**
 * Creates <code>CollectionBuilder</code> instance for specific type and node. Also 
 * provides helper method to register custom CollectionBuilder, and method to check
 * if a type and node is a collection type. 
 * <p>
 * 
 * @author Chunyun Zhao
 * @since  Jul 31, 2006
 *
 */
public class CollectionBuilderFactory {
	private static List<CollectionBuilder> registeredCollectionBuilders = new ArrayList<CollectionBuilder>();
	
	static {
		registerCollectionBuilder(new ArrayBuilder());
		registerCollectionBuilder(new ListBuilder());
		registerCollectionBuilder(new SetBuilder());
	}
	
	/**
	 * Creates a new <code>CollectionBuilder</code> instance, it is clone of the prototype instance
	 * initialized with all the parameters.
	 * 
	 * @param parent the parent of the collection.
	 * @param nodeName the node name
	 * @param nodeType the node type
	 * @return
	 */
	public static CollectionBuilder createCollectionBuilder(Object parent, String nodeName, Class nodeType) {
		CollectionBuilder prototype = getCollectionBuilder(nodeName, nodeType);
		if (prototype != null ) {
				CollectionBuilder builder = (CollectionBuilder)prototype.clone();
				builder.initialize(parent, nodeName, nodeType);
				return builder;
		}
		return null;
	}
	
	/**
	 * Gets the prototype instance of the collection builder. This call doesn't create a new 
	 * <code>CollectionBuilder</code> instance. Use createCollectionBuilder instead if a new
	 * builder needs to be created.
	 * 
	 * @param nodeName the node name
	 * @param nodeType the node type
	 * @return the prototype instance
	 */
	public static CollectionBuilder getCollectionBuilder(String nodeName, Class nodeType) {
		CollectionBuilder prototype = null;
		for (int i = 0; i < registeredCollectionBuilders.size(); i++) {
			prototype = registeredCollectionBuilders.get(i); 
			if ( prototype.isCollection(nodeName, nodeType) ) {
				return prototype;
			}
		}
		return null;
	}
	
	/**
	 * Returns true if the node name and type is a supported collection type.
	 *
	 * @param nodeName the node name, this parameter is there for extensibility purpose.
	 * @param nodeType the node type
	 * @return true if it is a supported collection type.
	 */
	public static boolean isCollectionType(String nodeName, Class nodeType) {
		CollectionBuilder prototype = getCollectionBuilder(nodeName, nodeType);
		return prototype != null;
	}
	
	/**
	 * Returns the element type if the node and type is supported collection. 
	 * Otherwise, orginal node type will be returned.
	 *
	 * @param nodeName the node name, this parameter is there for extensibility purpose.
	 * @param nodeType the node type
	 * @return
	 */
	public static Class getElementType(String nodeName, Class nodeType) {
		CollectionBuilder prototype = getCollectionBuilder(nodeName, nodeType);
		if ( prototype != null ) {
			return prototype.getElementType(nodeName, nodeType);
		}
		//Otherwise, echo the node type.
		return nodeType;
		
	}
	/**
	 * For all the built in collection builders, the node name is not required 
	 * to determine if the type is a collection. This method only takes nodeType 
	 * as the parameter.
	 *
	 * @param nodeType the node type
	 * @return the element type
	 */
	public static Class getElementType(Class nodeType) {
		return getElementType(null, nodeType);
	}
	/**
	 * For all the built in collection builders, the node name is not required 
	 * to determine if the type is a collection. This method only takes nodeType 
	 * as the parameter.
	 *
	 * @param nodeType the node type
	 * @return true if the node type is a collection type.
	 */
	
	public static boolean isCollectionType(Class nodeType) {
		return isCollectionType(null, nodeType);
	}
	
	/**
	 * Calls all the registered builders to get collection attribute name. Returns
	 * the actual attribute name if nodeName represents any of the collection supported.
	 * 
	 * Echos nodeName back if it is not a collection element node.
	 * 
	 * @param nodeName
	 * @return
	 */
	public static String getActualAttributeName(String nodeName) {
		CollectionBuilder prototype = null;
		for (int i = 0; i < registeredCollectionBuilders.size(); i++) {
			prototype = registeredCollectionBuilders.get(i); 
			nodeName = prototype.getCollectionAttributeName(nodeName);
		}
		return nodeName;
	}
	
	/**
	 * Registers supported collection builders.
	 * 
	 * @param builder
	 */
	public static void registerCollectionBuilder(CollectionBuilder builder) {
		registeredCollectionBuilders.add(builder);
	}
}
