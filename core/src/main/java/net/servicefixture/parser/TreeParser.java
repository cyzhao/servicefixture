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

import fit.Parse;

/**
 * Parses the data from test table and creates the <code>Tree</code> object.
 * 
 * @author Chunyun Zhao
 * @since Dec 21, 2005
 */
public interface TreeParser {
    public static final String CLASS_TYPE_PREFIX = "${type:";
    public static final String CLASS_TYPE_SUFFIX = "}";
    /**
     * Is called everytime when a data row is encountered during the parsing
     * process.
     */
    public void addDataRow(Parse[] dataCells);

    /**
     * Returns the request data tree.
     */
    public Tree getTree();
}
