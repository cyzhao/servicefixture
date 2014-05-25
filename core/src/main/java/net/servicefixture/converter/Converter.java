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
package net.servicefixture.converter;

/**
 * This interface defines methods for converting an object type to string, and
 * and converting to an object type from string or any object. 
 * <p>
 * It is used to provide an extension point to support custome type conversion.  
 * 
 * @see ObjectConverter
 * @author Chunyun Zhao
 * @since Jul 28, 2006
 */
public interface Converter {
	/**
	 * Converts any object to the type supported by the converter. For instance,
	 * DateConverter converts any object to java.util.Date.
	 * 
	 * @param object 
	 */
	public Object fromObject(Object source);
	/**
	 * Converts the object to string. 
	 * 
	 * @param object The object need to converted.
	 */
	public String toString(Object source);
}
