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
package net.servicefixture.sample.domain;

/**
 *
 * @author Chunyun Zhao
 * @since Aug 22, 2006
 */
public class Availability {
	private String availability;
	
	private Availability(String availability) {
		this.availability = availability;
	}
	
	public String toString() {
		return availability;
	}
	
	public static Availability fromString(String availability) {
		if ( IN_STOCK.toString().equals(availability)) {
			return IN_STOCK;
		} else if ( OUT_OF_STOCK.toString().equals(availability) ) {
			return OUT_OF_STOCK;
		} else if ( NOT_SUPPLIED.toString().equals(availability)) {
			return NOT_SUPPLIED;
		} else {
			throw new IllegalArgumentException("Invalid availability.");
		}
	}
	
	public static final Availability IN_STOCK = new Availability("In Stock");
	public static final Availability OUT_OF_STOCK = new Availability("Out of Stock");
	public static final Availability NOT_SUPPLIED = new Availability("Not Supplied");
}
