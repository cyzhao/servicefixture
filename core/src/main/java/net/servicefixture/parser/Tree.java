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
import java.util.Iterator;
import java.util.List;

import fit.Parse;

/**
 * The <code>Tree</code> models the request data tree structure
 * which is then used to parse the request objects.
 * <p>
 * 
 * @author Chunyun Zhao
 * @since Dec 21, 2005
 */
public class Tree {
    private Group rootNode;

    public Tree() {
        rootNode = new Group();
    }

    /**
     * Adds the top level child.
     */
    public void addChild(Node node) {
        rootNode.addChild(node);
    }

    public Group getRoot() {
        return rootNode;
    }

    /**
     * Return iterator over the top level nodes.
     */
    public Iterator iterator() {
        return rootNode.iterator();
    }

    public static class Node {
        private String name;
        private Class objectType;
        
        protected Node(String name) {
            this.name = name;
        }

        protected Node(String name, Class objectType) {
        	this.objectType = objectType;
        }
        
        public String getName() {
        	return this.name;
        }
        
		public Class getObjectType() {
			return objectType;
		}

		public void setObjectType(Class objectType) {
			this.objectType = objectType;
		}
    }

    public static class Group extends Node {
        private List<Node> children = new ArrayList<Node>();

        public Group() {
            super(null);
        }

        public Group(String name) {
            super(name);
        }

        public void addChild(Node node) {
            children.add(node);
        }

        public Iterator iterator() {
            return children.iterator();
        }
    }

    public static class Leaf extends Node {
        private Parse valueCell;

        public Leaf(String name, Parse valueCell) {
            super(name);
            this.valueCell = valueCell;
        }

        public Parse getValueCell() {
        	return this.valueCell;
        }
        
        public String getValue() {
            return this.valueCell.text();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Iterator iter = iterator(); iter.hasNext();) {
            Node node = (Node) iter.next();
            printNode(sb, node, 0);
        }
        return sb.toString();
    }

    private void printNode(StringBuilder sb, Node node, int depth) {
        indent(sb, depth);
        sb.append(node.getName());
        if ( node.getObjectType() != null ) {
        	sb.append("{").append(node.getObjectType().getName()).append("}");
        }
        if (node instanceof Leaf) {
            indent(sb, 1);
            sb.append(((Leaf) node).getValue()).append("\n");
        } else {
            sb.append("\n");
            for (Iterator iterator = ((Group) node).iterator(); iterator
                    .hasNext();) {
                Node childNode = (Node) iterator.next();
                printNode(sb, childNode, depth + 1);
            }
        }
    }

    private String indent(StringBuilder sb, int depth) {
        for (int i = 0; i < depth; i++) {
            sb.append("    ");
        }
        return sb.toString();
    }
}
