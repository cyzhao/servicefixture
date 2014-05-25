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
package net.servicefixture.ext.db;

import java.sql.Connection;

import org.apache.commons.lang.StringUtils;

import net.servicefixture.Configuration;
import net.servicefixture.ServiceFixture;
import net.servicefixture.ServiceFixtureException;
import net.servicefixture.util.DbUtil;

/**
 * The base class for db service fixtures. It manages the db connection. For any
 * type of db service fixtures, following rows are supported to collect db
 * connection parameters, if any of those are not provided in the test table, it
 * uses the value of the corresponding system property for the parameter.
 * 
 * <ul>
 * <li>dbUrl - Jdbc url. Corresponding system property: servicefixture.db.<dbname>.url</li>
 * <li>dbUser - Db user name. Corresponding system property: servicefixture.db.<dbname>.user</li>
 * <li>dbPassword - Db password. Corresponding system property:
 * servicefixture.db.<dbname>.password</li>
 * <li>dbDriver - Jdbc driver. Corresponding system property:
 * servicefixture.db.<dbname>.driver</li>
 * </ul>
 * 
 * @author Chunyun Zhao
 * @since Dec 08, 2006
 */
public abstract class DbFixture extends ServiceFixture {
	protected Connection dbConnection;

	public final static String DEFAULT_DB_NAME = "default";

	public final static String DEFAULT_DB_URL_SYSPROP = "servicefixture.db."
			+ DEFAULT_DB_NAME + ".url";

	public final static String DEFAULT_DB_USER_SYSPROP = "servicefixture.db."
			+ DEFAULT_DB_NAME + ".user";

	public final static String DEFAULT_DB_PASSWORD_SYSPROP = "servicefixture.db."
			+ DEFAULT_DB_NAME + ".password";

	public final static String DEFAULT_DB_DRIVER_SYSPROP = "servicefixture.db."
			+ DEFAULT_DB_NAME + ".driver";

	private String dbName;

	private String dbUrl;

	private String dbUser;

	private String dbPassword;

	private String dbDriver;

	public void url(String[] data) {
		this.dbUrl = data[0];
	}

	public void driver(String[] data) {
		this.dbDriver = data[0];
	}

	public void user(String[] data) {
		this.dbUser = data[0];
	}

	public void password(String[] data) {
		this.dbPassword = data[0];
	}

	public void db(String[] data) {
		this.dbName = data[0];
	}

	@Override
	public Object invoke(Object[] args) throws Exception {
		if (dbUrl == null) {
			dbUrl = getProperty(DEFAULT_DB_URL_SYSPROP);
		}
		if (dbUser == null) {
			dbUser = getProperty(DEFAULT_DB_USER_SYSPROP);
		}
		if (dbPassword == null) {
			dbPassword = getProperty(DEFAULT_DB_PASSWORD_SYSPROP);
		}
		if (dbDriver == null) {
			dbDriver = getProperty(DEFAULT_DB_DRIVER_SYSPROP);
		}

		dbConnection = DbUtil
				.getConnection(dbDriver, dbUrl, dbUser, dbPassword);
		try {
			return jdbcInvoke(args);
		} finally {
			DbUtil.closeAll(dbConnection, null, null);
		}
	}

	public String getProperty(String defaultPropName) {
		String propName = defaultPropName;
		if (dbName != null) {
			propName = StringUtils.replace(defaultPropName, DEFAULT_DB_NAME,
					dbName);
		}
		String propValue = Configuration.getInstance().getProperty(propName);
		if (propValue == null) {
			throw new ServiceFixtureException("Please configure database:"
					+ (dbName == null ? DEFAULT_DB_NAME : dbName) + " in "
					+ Configuration.SERVICEFIXTURE_PROPERTIES + " file.");
		}
		return propValue;
	}

	public abstract Object jdbcInvoke(Object[] args) throws Exception;
}