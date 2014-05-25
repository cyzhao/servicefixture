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

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;

/**
 * @author Chunyun Zhao
 * @since Aug 23, 2006
 */
public class ListUserType implements UserType {
	private static final int[] SQL_TYPES = new int[] {Types.VARCHAR};
	private static final String DELIMITER = ",";

	public int[] sqlTypes() {
		return SQL_TYPES;
	}

	public Class returnedClass() {
		return List.class;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		return (x == y);
	}

	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
			throws HibernateException, SQLException {
		String value = rs.getString(names[0]);
		if ( rs.wasNull() ) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(value, DELIMITER);
		while (tokenizer.hasMoreTokens() ) {
			list.add(tokenizer.nextToken());
		}
		return list;
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index)
			throws HibernateException, SQLException {
		if (value == null) {
			st.setNull(index, Types.VARCHAR);
		} else {
			StringBuilder data = new StringBuilder();
			List list = (List)value;
			for (int i = 0; i < list.size(); i++) {
				data.append(list.get(i));
				if ( i < list.size() - 1 ) {
					data.append(DELIMITER);
				}
			}
			st.setString(index, data.toString());
		}
	}

	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public boolean isMutable() {
		return false;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	public Object assemble(Serializable cached, Object owner)
			throws HibernateException {
		return cached;
	}

	public Object replace(Object original, Object target, Object owner)
			throws HibernateException {
		return original;
	}
}
