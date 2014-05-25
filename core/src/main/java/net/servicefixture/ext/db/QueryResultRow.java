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
/*
 * @author Chunyun Zhao
 * @since Dec 08, 2006
 */
public final class QueryResultRow {
	private Object[] columns;

	public Object[] getColumns() {
		return columns;
	}

	public void setColumns(Object[] columns) {
		this.columns = columns;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if ( columns == null || columns.length == 0 ) {
			return "No Data";
		}
		for (int i = 0; i < columns.length; i++) {
			builder.append(columns[i]);
			if ( i < columns.length - 1) {
				builder.append(";;;");
			}
		}
		return builder.toString();
	}
}