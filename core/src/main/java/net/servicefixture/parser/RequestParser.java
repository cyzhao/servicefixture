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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.servicefixture.ServiceFixtureException;
import net.servicefixture.converter.ObjectConverter;
import net.servicefixture.parser.Tree.Group;
import net.servicefixture.parser.Tree.Leaf;
import net.servicefixture.parser.Tree.Node;
import net.servicefixture.util.ReflectionUtils;
import fit.Fixture;

/**
 * 
 * Builds the request arguments from the set rows of test table.
 * 
 * @author Chunyun Zhao
 * @since Dec 21, 2005
 */
public class RequestParser {
    private Method serviceOperation;

    private List<Object> requestArgumentList = new ArrayList<Object>();
    private List<String> requestArgumentNames = new ArrayList<String>();

    /**
	 * Maintains all the CollectionBuilder(s) during the processing of a request
	 * argument. <code>CollectionBuilderKey</code> is used as the key to
	 * identify a <code>
     * CollecitonBuilder</code>. Is flushed and cleared when
	 * a new argument process begins.
	 */
    private Map<CollectionBuilderKey, CollectionBuilder> collectionBuilders = new HashMap<CollectionBuilderKey, CollectionBuilder> ();

    public RequestParser(Method serviceOperation) {
        this.serviceOperation = serviceOperation;
    }

    public Object[] getRequestArguments() {
        flushCollectionBuilders();
        
        // Fills up arguments with null values if not provided
        if ( serviceOperation.getParameterTypes().length > requestArgumentList.size() ) {
        	for (int i = 0; i < serviceOperation.getParameterTypes().length -  requestArgumentList.size(); i++) {
        		requestArgumentList.add(null);
			}
        }
        
        return requestArgumentList.toArray();
    }
    
    public String[] getRequestArgumentNames() {
    	return requestArgumentNames.toArray(new String[requestArgumentNames.size()]);
    }
    
    public void parse(Tree tree) {
//        System.out.println("Parsing request data tree: \n" + tree);
    	String argumentName = null;
        for (Iterator iter = tree.iterator(); iter.hasNext();) {
            Node node = (Node) iter.next();
            if ( !node.getName().equals(argumentName) ) {
            	argumentName = node.getName();
	            int index = argumentName.indexOf("[");
	            if ( index > 0 ) {
	            	argumentName = argumentName.substring(0, index);
	            }
	            requestArgumentNames.add(argumentName);
            }
            addRequestArgument(node);
        }
    }

    private void addRequestArgument(Node node) {
        if (!collectionBuilderExists(requestArgumentList, getCurrentArgumentType(), node.getName()) ) {
            flushCollectionBuilders();
        }
        Class argumentType = getCurrentArgumentType();
        createObject(requestArgumentList, argumentType, node);
    }

    /**
	 * This method recursively calls itself to populate nested objects.
	 */
    private void createObject(Object parent, final Class type, final Node node) {
        boolean isCollectionType = false;
        Class objectType = type;
        if (CollectionBuilderFactory.isCollectionType(node.getName(), type)) {
            objectType = CollectionBuilderFactory.getElementType(node.getName(), type);
            isCollectionType = true;
        }
        
        // If the object type is specified in test table, use this
        // type instead of the one found by reflection.
        if ( node.getObjectType() != null ) {
        	objectType = node.getObjectType();
        	System.out.println("Using specified type:" + objectType.getName() + " for " + node.getName());
        }
        
        Object object = null;
        if (node instanceof Group) {
            object = ReflectionUtils.newInstance(objectType);
            for (Iterator iter = ((Group) node).iterator(); iter.hasNext();) {
                Node childNode = (Node) iter.next();
                Class childType = ReflectionUtils.getAttributeType(object,
                        CollectionBuilderFactory.getActualAttributeName(childNode.getName()));
                // Recursive call to populate its children.
                createObject(object, childType, childNode);
            }
        } else if (node instanceof Leaf) {
        	Leaf leaf = (Leaf)node;
            String value = leaf.getValue();
            if ( isCollectionType 
            		&& !CollectionBuilderFactory.getCollectionBuilder(node.getName(), type).isCollectionElement(node.getName()) ) {
	            	// Assigns another array of the same type to the attribute.
	                object = ObjectConverter.convert(value, type);
	                isCollectionType = false;
            } else {
                object = ObjectConverter.convert(value,
                        objectType);
            }
            // Replaces the expression with real value in the test result.
            if ( ObjectConverter.isExpression(leaf.getValue()) ) {
            	String realValue = ObjectConverter.toString(object);
            	realValue = realValue != null ? Fixture.escape(realValue) : "null";
            	leaf.getValueCell().addToBody(" ==> " + realValue);
            }
        }

        if (isCollectionType) {
            // The collection type argument will be attached to its parent when
            // collectionBuilders is flushed.
            addObjectToCollection(parent, node.getName(), type, object);
        } else {
            // Attaches the object to its parent.
            ReflectionUtils.attacheObjectToParent(parent, node.getName(),
                    object);
        }
    }

    /**
	 * Returns current argument type that is about to be constructed.
	 */
    private Class getCurrentArgumentType() {
        Class[] parameterTypes = serviceOperation.getParameterTypes();
        if (requestArgumentList.size() > (parameterTypes.length - 1)) {
            throw new ServiceFixtureException(
                    "Passing more parameters than it is needed.");
        }
        return parameterTypes[requestArgumentList.size()];
    }
    
    /**
	 * Adds the object to a CollectionBuilder, locate the CollectionBuilder in
	 * the map first, if not found, create a new one.
	 * 
	 * @param parent
	 *            the parent object to which the array belongs
	 * @param attributeName
	 *            the attribute name of the array, unique under the same parent
	 * @param nodeType
	 *            the node type
	 * @param element
	 *            the object need to be added to the array.
	 */
    private void addObjectToCollection(Object parent, String nodeName,
            Class nodeType, Object element) {

        String attributeName = CollectionBuilderFactory.getCollectionBuilder(nodeName, nodeType).getCollectionAttributeName(nodeName);

        CollectionBuilderKey key = new CollectionBuilderKey(parent,
                attributeName);

        CollectionBuilder builder = collectionBuilders.get(key);
        if (builder == null) {
            builder = CollectionBuilderFactory.createCollectionBuilder(parent, nodeName, nodeType);
            collectionBuilders.put(key, builder);
        }

        builder.addObject(element);
    }

    /**
	 * Constructs all the collections and attach them to their parents. Also
	 * cleans up the collectionBuilders.
	 */
    private void flushCollectionBuilders() {
        for (Iterator iter = collectionBuilders.values().iterator(); iter
                .hasNext();) {
            CollectionBuilder builder = (CollectionBuilder) iter.next();
            builder.flush();
        }
        collectionBuilders.clear();
    }

    /**
	 * Returns true if a CollectionBuilder already being used for current node,
	 * for example, when current node is the second element in the collection.
	 */
    private boolean collectionBuilderExists(Object parent, Class nodeType, String nodeName) {
    	if ( CollectionBuilderFactory.isCollectionType(getCurrentArgumentType()) ) { 
    		String attributeName = CollectionBuilderFactory.getCollectionBuilder(nodeName, nodeType).getCollectionAttributeName(nodeName); 
	        return collectionBuilders.containsKey(new CollectionBuilderKey(parent,
	                attributeName));
    	}
    	return false;
    }

    /**
	 * Identifies <code>CollectionBuilder</code> objects in the map.
	 */
    private class CollectionBuilderKey {
        private Object parent;

        private String name;

        public CollectionBuilderKey(Object parent, String attributeName) {
            this.parent = parent;
            this.name = attributeName;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof CollectionBuilderKey)) {
                return false;
            }
            CollectionBuilderKey source = (CollectionBuilderKey) obj;

            return System.identityHashCode(source.parent) == System
                    .identityHashCode(this.parent)
                    && source.name.equals(this.name);
        }

        public int hashCode() {
            int i = 17;
            i = 37 * i + System.identityHashCode(this.parent);
            i = 37 * i + this.name.hashCode();
            return i;
        }

        public String getAttributeName() {
            return name;
        }

        public Object getParent() {
            return parent;
        }
    }
}
