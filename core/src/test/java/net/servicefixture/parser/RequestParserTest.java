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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import net.servicefixture.parser.Tree.Group;
import net.servicefixture.parser.Tree.Leaf;
import fit.Parse;

public class RequestParserTest extends TestCase {
	static {
		CollectionBuilderFactory.registerCollectionBuilder(new TestCollectionBuilder());
	}
    public void testParser1() throws Exception {
        RequestParser parser = new RequestParser(this.getClass().getMethod("operation", new Class[]{TypeA.class, TypeB.class}));
        Tree dataTree = new Tree();
        Group parameterA = new Group("a");
        parameterA.addChild(new Leaf("date", new Parse("tr","2006-04-10", null, null)));
        parameterA.addChild(new Leaf("cal", new Parse("tr", "2006-04-28",null, null)));
        parameterA.addChild(new Leaf("list", new Parse("tr", "${empty.collection}",null, null)));
        parameterA.addChild(new Leaf("value1", new Parse("tr", "value1",null, null)));
        parameterA.addChild(new Leaf("value2", new Parse("tr", "value2",null, null)));
        parameterA.addChild(new Leaf("value3", new Parse("tr", "value3",null, null)));
        
        Group parameterB = new Group("b");
        parameterB.addChild(new Leaf("data", new Parse("tr", "data", null, null)));
        parameterB.addChild(new Leaf("array[0]", new Parse("tr", "array0",null, null)));
        parameterB.addChild(new Leaf("array[1]", new Parse("tr", "array1",null, null)));
        parameterB.addChild(new Leaf("array[2]", new Parse("tr", "array2",null, null)));
        parameterB.addChild(new Leaf("value", new Parse("tr", "value",null, null)));
        dataTree.addChild(parameterA);
        dataTree.addChild(parameterB);
        parser.parse(dataTree);
        Object[] args = parser.getRequestArguments();
        assertEquals(args[0].getClass(), TypeA.class);
        assertEquals(args[1].getClass(), TypeB.class);
        assertEquals(((TypeB)args[1]).getData(), "data");
        assertNotNull(((TypeA)args[0]).getList());
        assertEquals(0,((TypeA)args[0]).getList().size());
        assertNotNull(((TypeB)args[1]).getArray());
        assertEquals(3,((TypeB)args[1]).getArray().length);
        assertEquals("array0", ((TypeB)args[1]).getArray()[0]);
        assertEquals("array1", ((TypeB)args[1]).getArray()[1]);
        assertEquals("array2", ((TypeB)args[1]).getArray()[2]);
        assertEquals("value1|value2|value3", ((TypeA)args[0]).getValue());
        assertEquals("value", ((TypeB)args[1]).getValue());
        
    }
    public void testParser2() throws Exception {
    	RequestParser parser = new RequestParser(this.getClass().getMethod("operation", new Class[]{TypeA.class, TypeB.class}));
    	Tree dataTree = new Tree();
    	Group parameterA = new Group("a");
    	parameterA.addChild(new Leaf("date", new Parse("tr","2006-04-10", null, null)));
    	parameterA.addChild(new Leaf("cal", new Parse("tr", "2006-04-28",null, null)));
    	parameterA.addChild(new Leaf("list", new Parse("tr", "${empty.collection}",null, null)));
    	dataTree.addChild(parameterA);
    	parser.parse(dataTree);
    	Object[] args = parser.getRequestArguments();
    	assertEquals(2, args.length);
    	assertEquals(args[0].getClass(), TypeA.class);
    	assertNull(args[1]);
    }
    
    public void operation(TypeA a, TypeB b) {
    }
    
    public static class TypeA {
        private Date date;
        private Calendar cal;
        private List list;
        private String value;
        public Calendar getCal() {
            return cal;
        }
        public void setCal(Calendar cal) {
            this.cal = cal;
        }
        public Date getDate() {
            return date;
        }
        public void setDate(Date date) {
            this.date = date;
        }
		public List getList() {
			return list;
		}
		public void setList(List list) {
			this.list = list;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
        
    }
    
    public static class TypeB {
        private String data;
        private String[] array;
        private String value;
        public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

		public String[] getArray() {
			return array;
		}

		public void setArray(String[] array) {
			this.array = array;
		}
        
    }
}
