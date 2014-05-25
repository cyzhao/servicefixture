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
package net.servicefixture;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.servicefixture.util.InfoLogger;

/**
 * Loads properties from servicefixture.properties.
 * 
 * @author Chunyun Zhao
 * @since Aug 02, 2006
 */
public final class Configuration {
	private static Log log = LogFactory.getLog(Configuration.class);
	
	private static Configuration configuration;
	public static final String SERVICEFIXTURE_PROPERTIES = "servicefixture.properties";
	
	private Properties properties = new Properties();
	
	private Configuration() {
	}
	
	public static Configuration getInstance() {
		if (configuration == null ) {
			configuration = new Configuration();
			configuration.loadProperties();
		}
		return configuration;
	}

	private void loadProperties() {
		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(SERVICEFIXTURE_PROPERTIES));
			InfoLogger.log(log, "Loaded properties from " + SERVICEFIXTURE_PROPERTIES + " successfully.");
		} catch (IOException e) {
			InfoLogger.log(log, "Unable to loaded properties from " + SERVICEFIXTURE_PROPERTIES + " due to:" + e.toString());
		}
	}
	
	public String getProperty(String propertyName) {
		return properties.getProperty(propertyName);
	}
}
