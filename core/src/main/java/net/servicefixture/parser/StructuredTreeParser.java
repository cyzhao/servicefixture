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
import java.util.Stack;

import net.servicefixture.ServiceFixtureException;
import net.servicefixture.parser.Tree.Group;
import net.servicefixture.parser.Tree.Leaf;
import net.servicefixture.parser.Tree.Node;
import net.servicefixture.util.ReflectionUtils;
import fit.Parse;

/**
 * Parses the structured request data tree.
 * <p>
 * Here is a sample test table using structured data tree:
 * <p>
 * <table cellspacing="0" border="1" bgcolor="lightgrey">
 * <tr><td colspan="5">Create contract</td></tr>
 * <tr><td>set</td><td colspan="4">OrderID</td></tr>
 * <tr><td>set</td><td></td><td>id</td><td colspan="2">18</td></tr>
 * <tr><td>set</td><td colspan="4">ContractItem</td>
 * <tr><td>set</td><td></td><td>acceptedDate</td><td colspan="2">2005-12-15</td></tr>
 * <tr><td>set</td><td></td><td>acceptedTextVersion</td><td colspan="2">1</td>
 * <tr><td>set</td><td></td><td colspan="3">contractID</td>
 * <tr><td>set</td><td></td><td></td><td>id</td><td>1001</td>
 * <tr><td>invoke</td><td colspan="4"/></tr>
 * <tr><td>check</td><td>response ne null</td><td colspan="3">true</td></tr>
 * </table>
 * <p>
 * 
 * @author Chunyun Zhao
 * @since Dec 21, 2005
 */
public class StructuredTreeParser implements TreeParser {
    private Tree tree;

    private Stack<Group> groupStack;

    public StructuredTreeParser() {
        tree = new Tree();
        groupStack = new Stack<Group>();
        groupStack.push(tree.getRoot());
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
        int depth = 0;
        List<Parse> data = new ArrayList<Parse>();
        for (int i = 0; i < dataCells.length; i++) {
            if (dataCells[i].text().trim().length() > 0) {
                data.add(dataCells[i]);
            } else {
                depth++;
            }
        }

        //Pops up nodes that is lower than depth
        while (depth < (groupStack.size() - 1)) {
            groupStack.pop();
        }
        Group parent = groupStack.peek();
        switch (data.size()) {
        case 1:
            Group group = new Group(data.get(0).text());
            parent.addChild(group);
            groupStack.push(group);
            break;
        case 2:
        case 3:
        	String value = (data.get(1)).text();
        	if ( value.startsWith(CLASS_TYPE_PREFIX) && value.endsWith(CLASS_TYPE_SUFFIX)) {
            	//It is row that contains customized object type.
            	String clazzName = value.substring(CLASS_TYPE_PREFIX.length(), value.length() - CLASS_TYPE_SUFFIX.length());
            	Node node = null;
            	if ( data.size() == 3 ) {
            		//It is the lowest level object with customized object type.
            		node = new Leaf(data.get(0).text(), data.get(2));
            	} else {
    	        	node = new Group(data.get(0).text());
            	}
            	node.setObjectType(ReflectionUtils.newClass(clazzName));
            	parent.addChild(node);        		
        	} else {
	            Leaf leaf = new Leaf(data.get(0).text(), data.get(1));
	            parent.addChild(leaf);
        	}
            break;
        default:
            throw new ServiceFixtureException(
                    "The data row must have 1 or 2 non-blank data cells.");
        }
    }
}
