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
package net.servicefixture.sample.dao.hibernate;

import java.util.List;

import net.servicefixture.sample.ApplicationException;
import net.servicefixture.sample.dao.CustomerDao;
import net.servicefixture.sample.domain.Customer;
import net.servicefixture.sample.domain.CustomerRef;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Chunyun Zhao
 * @since Aug 24, 2006
 */
public class CustomerDaoImpl extends HibernateDaoSupport implements CustomerDao {

	public CustomerRef createCustomer(Customer customer) {
		getHibernateTemplate().save(customer);
		return new CustomerRef(customer);
	}

	public Customer getCustomer(long customerId) {
		Customer customer = (Customer)getHibernateTemplate().get(Customer.class, new Long(customerId));
		if ( customer == null ) {
			throw new ApplicationException("Invalid customerId:" + customerId);
		}
		return customer;		
	}

	public CustomerRef[] listCustomers() {
		List list = getHibernateTemplate().find("select c.customerId, c.firstName, c.lastName from Customer c");
		return toCustomerRefs(list);
	}

	public CustomerRef updateCustomer(Customer customer) {
		getHibernateTemplate().update(customer);
		return new CustomerRef(customer);
	}

	public void deleteCustomer(long customerId) {
		Customer customer = (Customer)getHibernateTemplate().get(Customer.class, new Long(customerId));
		if ( customer == null ) {
			throw new ApplicationException("Invalid customerId:" + customerId);
		}
		getHibernateTemplate().delete(customer);
	}

	private CustomerRef[] toCustomerRefs(List list) {
		CustomerRef[] result = new CustomerRef[list.size()];
		for (int i = 0; i < list.size(); i++) {
			Object[] dataArray = (Object[])list.get(i);
			result[i] = new CustomerRef();
			result[i].setCustomerId(((Long)dataArray[0]).longValue());
			result[i].setFirstName((String)dataArray[1]);
			result[i].setLastName((String)dataArray[2]);
		}
		return result;
	}

}
