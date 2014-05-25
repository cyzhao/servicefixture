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
public class PaymentType {
	private String paymentType;
	
	private PaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	
	public String toString() {
		return paymentType;
	}
	
	public static PaymentType fromString(String paymentType) {
		if ( CREDIT_CARD.toString().equals(paymentType) ) {
			return CREDIT_CARD;
		} else if ( DEBIT_CARD.toString().equals(paymentType) ) {
			return DEBIT_CARD;
		} else {
			throw new IllegalArgumentException("Invalid payment type.");
		}
	}
	
	public static final PaymentType CREDIT_CARD = new PaymentType("Credit Card");
	public static final PaymentType DEBIT_CARD = new PaymentType("Debit Card");
}
