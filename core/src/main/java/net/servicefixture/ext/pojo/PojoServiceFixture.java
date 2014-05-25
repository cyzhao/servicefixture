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
package net.servicefixture.ext.pojo;

import java.lang.reflect.Method;

import net.servicefixture.ServiceFixture;
import net.servicefixture.ServiceFixtureException;
import net.servicefixture.util.ReflectionUtils;

/**
 * The base class for pojo service fixtures. If test script template needs to
 * be generated, a concrete pojo service fixture class needs to be created for
 * each operation, these concrete fixtures could be generated automatically.
 * 
 * @author Chunyun Zhao
 * @since Aug 25, 2006
 */
public abstract class PojoServiceFixture extends ServiceFixture {
	private Method operation;
	private Object pojo;

	/**
	 * Returns the pojo instance, which will be invoked upon.
	 */
	protected abstract Object getPojo();
	/**
	 * Returns the operation name.
	 */
	protected abstract String getOperationName();
	
	/**
	 * Override this method to provide pojo class or its interface type directly if it is 
	 * expensive to create the pojo. This is for FixtureTemplateCreator as it only needs pojo
	 * type and operation name to generate the test table template. Furthermore, it sends 
	 * standard/error output to FitNesse editor, so if constructing the pojo logs information 
	 * to the output, override this method too.
	 */
	protected Class getPojoClazzOrInterface() {
		pojo = getPojo();
		return pojo.getClass();
	}
	
	/**
	 * Override this method to provide paramter types for a method if multiple
	 * methods have the same method name. No need to impelement this method if 
	 * method names are unique in the class.
	 */
	protected Class[] getParameterTypes() {
		return null;
	}
	
	public Method getServiceOperation() {
		try {
			operation = getPojoClazzOrInterface().getMethod(getOperationName(), getParameterTypes());
		} catch (Exception e) {
			//Couldn't find the method based on operation name and parameter types, now find the first 
			//method matches the operation name.
			operation = ReflectionUtils.findMethodByName(getPojoClazzOrInterface(), getOperationName());
			if ( operation == null ) {
				throw new ServiceFixtureException("Unable to find a valid method " + getOperationName() + " in class " + getPojo().getClass().getName(), e);
			}
		}		
		return operation;
	}

	public Object invoke(Object[] args) throws Exception {
		if ( pojo == null ) {
			pojo = getPojo();
		}
		return operation.invoke(pojo, args);
	}
	
}
