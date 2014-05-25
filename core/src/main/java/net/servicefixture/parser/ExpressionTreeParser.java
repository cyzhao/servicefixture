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

import java.util.Iterator;
import java.util.StringTokenizer;

import net.servicefixture.ServiceFixtureException;
import net.servicefixture.parser.Tree.Group;
import net.servicefixture.parser.Tree.Leaf;
import net.servicefixture.parser.Tree.Node;
import net.servicefixture.util.ReflectionUtils;

import org.apache.commons.lang.StringUtils;

import fit.Parse;

/**
 * Parses expression request data tree.
 *
 * @author Chunyun Zhao
 * @since Dec 21, 2005
 */
public class ExpressionTreeParser implements TreeParser {
    private static final String EXPRESSION_DELIMITER = ".";

    private Tree tree;

    public ExpressionTreeParser() {
        tree = new Tree();
    }

    /*
     * @see com.aol.bps.fixtures.RequestDataTreeParser#getTree()
     */
    public Tree getTree() {
        return tree;
    }

    /*
     * @see com.aol.bps.fixtures.RequestDataTreeParser#addDataRow(fit.Parse)
     */
    public void addDataRow(Parse[] dataCells) {
        if ( (dataCells.length != 2 && dataCells.length != 3) || StringUtils.isEmpty(dataCells[0].text())
                || StringUtils.isEmpty(dataCells[1].text())) {
            throw new ServiceFixtureException(
                    "The data row must and only have 2 or 3 non-blank data cells.");
        }
        String expression = dataCells[0].text();

        StringTokenizer tokenizer = new StringTokenizer(expression,
                EXPRESSION_DELIMITER);

        Group group = tree.getRoot();
        //Creates group nodes
        while ( tokenizer.countTokens() > 1) {
            group = getOrCreateGroupByName(group, tokenizer.nextToken());
        }
        //Now let's handle the last token which represents the leaf with value
        //or object type.
        String value = dataCells[1].text();
        
        if ( value.startsWith(CLASS_TYPE_PREFIX) && value.endsWith(CLASS_TYPE_SUFFIX) ) {
        	//It is row that contains customized object type.
        	String clazzName = value.substring(CLASS_TYPE_PREFIX.length(), value.length() - CLASS_TYPE_SUFFIX.length());
        	Node node = null;
        	if ( dataCells.length == 3 ) {
        		//It is the lowest level object with customized object type.
        		node = new Leaf(tokenizer.nextToken(), dataCells[2]);
        	} else {
	        	node = new Group(tokenizer.nextToken());
        	}
        	node.setObjectType(ReflectionUtils.newClass(clazzName));
        	group.addChild(node);
        } else {
        	Leaf leaf = new Leaf(tokenizer.nextToken(), dataCells[1]);
        	group.addChild(leaf);
        }
    }

    /**
     * Gets the child group node by its name from its parent group node. If it
     * is not found, create the new one and attache it to its parent.
     * 
     * @param group
     * @param childName
     * @return
     */
    private Group getOrCreateGroupByName(Group group, String childName) {
        for (Iterator iter = group.iterator(); iter.hasNext();) {
            Node node = (Node) iter.next();
            if (node.getName().equals(childName) && node instanceof Group) {
                return (Group) node;
            }
        }
        //Let's create the new group since it is not found
        Group childGroup = new Group(childName);
        group.addChild(childGroup);
        return childGroup;
    }

}
