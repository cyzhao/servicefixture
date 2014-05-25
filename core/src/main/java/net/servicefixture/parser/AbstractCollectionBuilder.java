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

import net.servicefixture.ServiceFixtureException;
import net.servicefixture.util.ReflectionUtils;

/**
 * This class provides a skeletal implementation of the <code>CollectionBuilder</code>
 * interface to minimize the effort required to implement this interface.
 * <p>
 * 
 * @author Chunyun Zhao
 * @since Dec 21, 2005
 */
public abstract class AbstractCollectionBuilder implements CollectionBuilder {
    protected Object parent;
    protected String attributeName;
    protected Class<?> elementType;

    protected List<Object> objectList;

    /* 
     * @see net.servicefixture.parser.CollectionBuilder#initialize(java.lang.Object, java.lang.String, java.lang.Class)
     */
    public void initialize(Object parent, String nodeName, Class nodeType) {
        this.parent = parent;
        if ( nodeName != null ) {
        	this.attributeName = getCollectionAttributeName(nodeName);
        }
        this.elementType = getElementType(nodeName, nodeType);
        objectList = new ArrayList<Object>();
    }

    /*
     * @see com.aol.bps.fixtures.CollectionBuilder#flush()
     */
    public void flush() {
        Object collection = getCollection();
        ReflectionUtils.attacheObjectToParent(parent, attributeName, collection);
        clear();
    }

    /**
     * Adds a new object to the array. If object type is different from the
     * elementType, throws FixtureException.
     */
    public void addObject(Object o) {
        if (o != null) {
            if (elementType == null) {
                elementType = o.getClass();
            } else if (!elementType.isAssignableFrom(o.getClass())) {
                throw new ServiceFixtureException(
                "Can't add a type:" + o.getClass().getName() + " to the collection of type:" + elementType + ".");
            }
        }
        objectList.add(o);
    }

    /**
     * Clears the list.
     */
    public void clear() {
        objectList.clear();
    }

	/* 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
        try {
        	return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new ServiceFixtureException("This should not occur since we implement Cloneable");
        }
	}

	/* 
	 * @see net.servicefixture.parser.CollectionBuilder#getCollectionAttributeName(java.lang.String)
	 */
	public String getCollectionAttributeName(String nodeName) {
        int index = nodeName.lastIndexOf('[');
        if (index >= 0) {
        	return nodeName.substring(0, index).trim();
        }
        return nodeName;		
	}

	/* 
	 * @see net.servicefixture.parser.CollectionBuilder#isCollectionElement(java.lang.String)
	 */
	public boolean isCollectionElement(String nodeName) {
		return nodeName.indexOf('[') >= 0;
	}   
}
