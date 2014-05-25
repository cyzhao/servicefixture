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
 * @author Chunyun Zhao
 * @since Aug 24, 2006
 */
public class Customer extends CustomerRef {
	private String emailAddress;
	private PaymentMethod[] paymentMethods;
	
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public PaymentMethod[] getPaymentMethods() {
		return paymentMethods;
	}
	public void setPaymentMethods(PaymentMethod[] paymentMethods) {
		this.paymentMethods = paymentMethods;
	}
	
}