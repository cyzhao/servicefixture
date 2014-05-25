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

import java.lang.reflect.Array;

/**
 * <code>ArrayBuilder</code> builds an array of objects.
 * 
 * @author Chunyun Zhao
 * @since Dec 21, 2005
 */
public class ArrayBuilder extends AbstractCollectionBuilder {
    public Object getCollection() {
        Object array = Array.newInstance(elementType, objectList.size());
        for (int i = 0; i < objectList.size(); i++) {
            Object element = objectList.get(i);
            Array.set(array, i, element);
        }
        return array;
    }

	public boolean isCollection(String nodeName, Class nodeType) {
		return nodeType.isArray();
	}

	public Class getElementType(String nodeName, Class nodeType) {
		return nodeType.getComponentType();
	}
}