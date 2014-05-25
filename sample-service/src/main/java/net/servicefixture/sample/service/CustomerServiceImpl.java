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
package net.servicefixture.sample.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.servicefixture.sample.ValidationException;
import net.servicefixture.sample.dao.CustomerDao;
import net.servicefixture.sample.domain.Customer;
import net.servicefixture.sample.domain.CustomerRef;

/**
 * @author Chunyun Zhao
 * @since Aug 25, 2006
 */
public class CustomerServiceImpl implements CustomerService {
	private CustomerDao customerDao;
	
	public CustomerDao getCustomerDao() {
		return customerDao;
	}

	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}
	
	public CustomerRef createCustomer(Customer customer) {
		validateCustomer(customer);
		return customerDao.createCustomer(customer);
	}

	public Customer getCustomer(long customerId) {
		return customerDao.getCustomer(customerId);
	}
	
	public CustomerRef[] listCustomers() {
		return customerDao.listCustomers();
	}

	public void deleteCustomer(long customerId) {
		customerDao.deleteCustomer(customerId);
	}
	
	public CustomerRef updateCustomer(Customer customer) {
		validateCustomer(customer);
		return customerDao.updateCustomer(customer);
	}
	
	private void validateCustomer(Customer customer) {
		customer.getEmailAddress();
	      Pattern p = Pattern.compile(".+@.+\\.[a-z]+");
	      Matcher m = p.matcher(customer.getEmailAddress());
	      boolean matchFound = m.matches();
	      if (!matchFound) {
	        throw new ValidationException("Invalid Email Address:" + customer.getEmailAddress());
	      }
	}
}
