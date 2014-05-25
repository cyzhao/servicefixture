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

import java.util.Date;

/**
 * 
 * @author Chunyun Zhao
 * @since Aug 22, 2006
 */
public class OrderRef {
	private long orderId;
	private Date orderDate;
	private double totalPrice;

	public OrderRef() {
	}
	
	public OrderRef(OrderRef order) {
		this.orderId = order.getOrderId();
		this.orderDate = order.getOrderDate();
		this.totalPrice = order.getTotalPrice();
	}
	
	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long id) {
		this.orderId = id;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public double getTotalPrice() {

		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
}
