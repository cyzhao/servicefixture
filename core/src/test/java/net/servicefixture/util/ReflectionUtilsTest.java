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
package net.servicefixture.util;

import java.util.Date;

import junit.framework.TestCase;
import net.servicefixture.converter.ObjectConverter;

public class ReflectionUtilsTest extends TestCase {
	public void testGetAttributeType() {
		ClassA a = new ClassA();
		assertEquals(String.class, ReflectionUtils.getAttributeType(a, "classAStr"));
		assertEquals(Integer.class, ReflectionUtils.getAttributeType(a, "classAInt"));
		ClassB b = new ClassB();
		assertEquals(String.class, ReflectionUtils.getAttributeType(b, "classAStr"));
		assertEquals(Integer.class, ReflectionUtils.getAttributeType(b, "classAInt"));
		assertEquals(String.class, ReflectionUtils.getAttributeType(b, "classAStr"));
		assertEquals(Integer.class, ReflectionUtils.getAttributeType(b, "classAInt"));
	}

	public void testSetBooleanNull() {
		ClassA object = new ClassA();
		ReflectionUtils.setObjectAttribute(object, "classABoolean", null);
		ReflectionUtils.setObjectAttribute(object, "classAInt", null);
		ReflectionUtils.setObjectAttribute(object, "classAStr", null);
		assertNull(object.getClassAStr());
		assertNull(object.getClassAInt());
		assertNull(object.getClassABoolean());
	}
	
    public void testSetDate() {
        ClassA a = new ClassA();
        Date date = (Date)ObjectConverter.convert("2006-11-22T12:30:23.359-05:00", Date.class);
        ReflectionUtils.setObjectAttribute(a, "date", date);
    }
    
	public static class ClassA {
		private String classAStr;
		private Integer classAInt;
		private Boolean classABoolean;
        private Date date;
		public Date getDate() {
            return date;
        }
        public void setDate(Date date) {
            this.date = date;
        }
        public Integer getClassAInt() {
			return classAInt;
		}
		public void setClassAInt(Integer classAInt) {
			this.classAInt = classAInt;
		}
		public String getClassAStr() {
			return classAStr;
		}
		public void setClassAStr(String classAStr) {
			this.classAStr = classAStr;
		}
		public Boolean getClassABoolean() {
			return classABoolean;
		}
		public void setClassABoolean(Boolean classABoolean) {
			this.classABoolean = classABoolean;
		}
	}
	
	public static class ClassB extends ClassA {
		private String classBStr;
		private Integer classBInt;
		public Integer getClassBInt() {
			return classBInt;
		}
		public void setClassBInt(Integer classBInt) {
			this.classBInt = classBInt;
		}
		public String getClassBStr() {
			return classBStr;
		}
		public void setClassBStr(String classBStr) {
			this.classBStr = classBStr;
		}
	}
}
