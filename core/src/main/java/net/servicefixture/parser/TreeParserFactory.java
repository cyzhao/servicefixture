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

import net.servicefixture.ServiceFixtureException;

import org.apache.commons.lang.StringUtils;

/**
 * Creates the concrete <code>TreeParser</code> object.
 * <p>
 * 
 * @author Chunyun Zhao
 * @since Dec 21, 2005
 */
public class TreeParserFactory {
    private static final String TREE_PARSER_SYSPROP = "net.servicefixture.parser.TreeParser";

    /**
     * Creates an <code>TreeParser</code> instance. Looks up system property
     * TreeParserFactory.TREE_PARSER_SYSPROP for the class name first. If not 
     * found, an <code>ExpressionTreeParser</code> instance will be created.
     *  
     */
    public static TreeParser createTreeParser(
            String parserClazzName) {
        if (StringUtils.isEmpty(parserClazzName)) {
            parserClazzName = System
                    .getProperty(TREE_PARSER_SYSPROP);
        }

        try {
            if (!StringUtils.isEmpty(parserClazzName)) {
                Class parserClazz = Thread.currentThread()
                        .getContextClassLoader().loadClass(parserClazzName);
                return (TreeParser) parserClazz.newInstance();
            }

            return new ExpressionTreeParser();
        } catch (Exception e) {
            throw new ServiceFixtureException(
                    "Unable to create TreeParser using class:"
                            + parserClazzName + ".", e);
        }
    }
}
