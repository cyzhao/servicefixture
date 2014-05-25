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

import java.util.Date;
import java.util.List;

import net.servicefixture.sample.ApplicationException;
import net.servicefixture.sample.dao.OrderDao;
import net.servicefixture.sample.domain.Customer;
import net.servicefixture.sample.domain.Order;
import net.servicefixture.sample.domain.OrderRef;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Chunyun Zhao
 * @since Aug 23, 2006
 */
public class OrderDaoImpl extends HibernateDaoSupport implements OrderDao {
	public OrderRef placeOrder(Order order) {
		getHibernateTemplate().save(order);
		return new OrderRef(order);
	}

	public Order getOrderDetail(long orderId) {
		Order order = (Order)getHibernateTemplate().get(Order.class, new Long(orderId));
		if ( order == null ) {
			throw new ApplicationException("Invalid orderId:" + orderId);
		}
		return order;
	}

	public OrderRef[] listOrders(long customerId) {
		Customer customer = new Customer();
		customer.setCustomerId(customerId);
		List orders = getHibernateTemplate().find("select o.orderId, o.orderDate, o.totalPrice from Order as o where o.customer = ?", customer);
		return toOrderRefs(orders); 
	}

	private OrderRef[] toOrderRefs(List list) {
		OrderRef[] orders = new OrderRef[list.size()];
		for (int i = 0; i < list.size(); i++) {
			Object[] array = (Object[])list.get(i);
			orders[i] = new OrderRef();
			orders[i].setOrderId(((Long)array[0]).longValue());
			orders[i].setOrderDate((Date)array[1]);
			orders[i].setTotalPrice(((Double)array[2]).doubleValue());
		}
		return orders;		
	}
}
