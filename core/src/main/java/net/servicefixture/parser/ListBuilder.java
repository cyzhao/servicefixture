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

import java.util.List;

/**
 * Builds a List of objects.
 *
 * @author Chunyun Zhao
 * @since Jul 2, 2006
 */
public class ListBuilder extends AbstractCollectionBuilder {
    public Object getCollection() {
        return objectList;
    }

	public boolean isCollection(String nodeName, Class nodeType) {
		return List.class.equals(nodeType);
	}

	public Class getElementType(String nodeName, Class nodeType) {
		return Object.class;
	}
}
