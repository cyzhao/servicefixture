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

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.servicefixture.ServiceFixtureException;
import net.servicefixture.util.DbUtil;
/**
 * This fixture executes the update against the database and returns
 * the number of rows affected back.
 * 
 * @author Chunyun Zhao
 * @since Dec 08, 2006
 */
public class UpdateFixture extends DbFixture {
	@Override
	public Method getServiceOperation() {
		try {
			return this.getClass().getMethod("update", new Class[]{String.class, Object[].class});
		} catch (Exception e) {
			throw new ServiceFixtureException("Unable to find the method.", e);
		}
	}

	public int update(String sql, Object[] bindings) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = dbConnection.prepareStatement(sql);
			if ( bindings != null && bindings.length > 0 ) {
				for (int i = 0; i < bindings.length; i++) {
					statement.setObject(i+1, bindings[i]);
				}
			}
			return statement.executeUpdate();
		} catch (SQLException e) {
			throw new ServiceFixtureException("Failed to execute the update.", e);
		} finally {
			DbUtil.closeAll(null, statement, resultSet);
		}
	}
	@Override
	public Object jdbcInvoke(Object[] args) throws Exception {
		return update((String)args[0], (Object[])args[1]);
	}
}
