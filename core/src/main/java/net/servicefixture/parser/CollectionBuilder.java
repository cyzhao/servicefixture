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


/**
 * <codee>CollectionBuilder</code> builds a collection of objects and attaches
 * the objects to its parent when it is flushed.
 * 
 * @author Chunyun Zhao
 * @since Dec 21, 2005
 */
public interface CollectionBuilder extends Cloneable {
    /**
     * Adds a new object to the collection.
     */
    public void addObject(Object o);

    /**
     * Returns the collection of objects.
     */
    public Object getCollection();

    /**
     * Constructs the collection and attache to its parent.
     */
    public void flush();

    /**
     * Clean up code goes here.
     */
    public void clear();

    /**
     * Initializes the builder. 
     *
     * @param parent the parent of this collection.
     * @param nodeName the node name(attribute name) of this collection in its parent
     * @param nodeType the node type(attribute type) of this collection in its parent
     */
    public void initialize(Object parent, String nodeName, Class nodeType);
    /**
     * Returns true if the type and nodeName is the collection type
     * supported by this <code>CollectionBuilder</code>
     *
     * @param nodeName the node name
     * @param nodeType the node type
     * @return true if it is collection
     */
    public boolean isCollection(String nodeName, Class nodeType);
    
    /**
     * Returns the object type of the element in the collection 
     *
     * @param nodeName the node name
     * @param nodeType the node type
     * @return the object type for the collection
     */
    public Class getElementType(String nodeName, Class nodeType);
    
	/**
	 * Clones the builder.
	 *
	 * @return
	 */
	public Object clone();
	
	/**
	 * Determines if the node name indicates a collection element, for example,
	 * values[0] would indicate that it is an element of the array or list.
	 * 
	 * @param nodeName the node name
	 * @return Returns true if the node represents an element of the collection.
	 */
	public boolean isCollectionElement(String nodeName);
	
	/**
	 * Trims the suffix and returns the attribute name in its parent of the collection back.
	 * For example, if the node name is values[0], then it will return values back.
	 * 
	 * Should alway echo the nodeName back if the nodeName doesn't represent a collection element.
	 * 
	 * @param nodeName the node name
	 * @return the attibute name without suffix. 
	 */
	public String getCollectionAttributeName(String nodeName);
}