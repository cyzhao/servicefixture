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
package net.servicefixture.sample.util;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.access.ContextSingletonBeanFactoryLocator;

/**
 * @author Chunyun Zhao
 * @since Aug 23, 2006
 */
public class SpringContextUtil {
	private static BeanFactory factory;

	public static Object getBean(String beanName) {
		if ( factory == null ) {
			factory = ContextSingletonBeanFactoryLocator.getInstance().useBeanFactory("net.servicefixture.sample.service").getFactory();
		}
		return factory.getBean(beanName);
	}
}
