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
import java.util.ArrayList;
import java.util.List;

import net.servicefixture.ServiceFixtureException;
import net.servicefixture.util.DbUtil;

/**
 * This fixture executes the query against the database and returns
 * the result set back in an array of array.
 * 
 * @author Chunyun Zhao
 * @since Dec 08, 2006
 */
public class SelectFixture extends DbFixture {

	@Override
	public Method getServiceOperation() {
		try {
			return this.getClass().getMethod("select", new Class[]{String.class, Object[].class});
		} catch (Exception e) {
			throw new ServiceFixtureException("Unable to find the method.", e);
		}
	}

	public QueryResult select(String sql, Object[] bindings) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = dbConnection.prepareStatement(sql);
			if ( bindings != null && bindings.length > 0 ) {
				for (int i = 0; i < bindings.length; i++) {
					statement.setObject(i+1, bindings[i]);
				}
			}
			resultSet = statement.executeQuery();
			int columnCount = resultSet.getMetaData().getColumnCount();
			List<QueryResultRow> rows = new ArrayList<QueryResultRow>();
			while (resultSet.next()) {
				Object[] columns = new Object[columnCount];
				for (int i = 0; i < columnCount; i++) {
					columns[i] = resultSet.getObject(i+1);
				}
				QueryResultRow row = new QueryResultRow();
				row.setColumns(columns);
				rows.add(row);
			}
			QueryResult result = new QueryResult();
			result.setRows(rows.toArray(new QueryResultRow[rows.size()]));
			return result;
		} catch (SQLException e) {
			throw new ServiceFixtureException("Failed to execute the query.", e);
		} finally {
			DbUtil.closeAll(null, statement, resultSet);
		}
	}
	@Override
	public Object jdbcInvoke(Object[] args) throws Exception {
		return select((String)args[0], (Object[])args[1]);
	}
}
