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

import net.servicefixture.sample.domain.Order;
import net.servicefixture.sample.domain.OrderRef;

/**
 * @author Chunyun Zhao
 * @since Aug 25, 2006
 */
public interface OrderService {
	public OrderRef placeOrder(Order order);
	public OrderRef[] listOrders(long customerId);
	public Order getOrderDetail(long orderId);
}
