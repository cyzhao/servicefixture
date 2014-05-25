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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.servicefixture.sample.ApplicationException;
import net.servicefixture.sample.domain.Address;
import net.servicefixture.sample.domain.Availability;
import net.servicefixture.sample.domain.Book;
import net.servicefixture.sample.domain.Category;
import net.servicefixture.sample.domain.Computer;
import net.servicefixture.sample.domain.Customer;
import net.servicefixture.sample.domain.CustomerRef;
import net.servicefixture.sample.domain.Order;
import net.servicefixture.sample.domain.OrderItem;
import net.servicefixture.sample.domain.PaymentMethod;
import net.servicefixture.sample.domain.PaymentType;
import net.servicefixture.sample.domain.Product;
import net.servicefixture.sample.domain.ProductRef;
import net.servicefixture.sample.domain.Software;
import net.servicefixture.sample.util.SpringContextUtil;
/**
 * @author Chunyun Zhao
 * @since Aug 23, 2006
 */
public class ServicesTest extends TestCase {
	CustomerService customerService = (CustomerService)SpringContextUtil.getBean("customerService");
	OrderService orderService = (OrderService)SpringContextUtil.getBean("orderService");
	ProductService productService = (ProductService)SpringContextUtil.getBean("productService");
	
	public ServicesTest(String name) {
		super(name);
	}

	public void testGetUsingInvalidId() {
		try {
			productService.getProductDetail(1000);
			fail("Should throw ApplicationException.");
		} catch (ApplicationException e) {
			assertEquals("Invalid productId:1000", e.getMessage());
		}
	}
	
	public void testCreateAndGetBook() {
		Book book = new Book();
		book.setName("Pragmatic Programmer");
		book.setPrice(29.99);
		book.setDescription("Pragmatic Programmer by Dave Thomas");
		book.setAvailability(Availability.IN_STOCK);
		book.setIsbn("0BNDA0001");
		book.setNumberOfPages(423);
		book.setNumberOfPages(312);
		book.setPublishDate(new Date());
		
		productService.createProduct(book);
		
		Product result = productService.getProductDetail(book.getProductId());
		assertEquals("Pragmatic Programmer", result.getName());
		assertTrue(29.99 == result.getPrice());
		assertTrue(result instanceof Book);
	}
	public void testCreateAndGetComputer() {
		Computer computer = new Computer();
		computer.setName("Dell 8100");
		computer.setPrice(1229.99);
		computer.setDescription("Intel 2G");
		computer.setAvailability(Availability.IN_STOCK);
		computer.setMemory(1024 * 1024 * 1024);
		computer.setMonitorSize(19);
		
		productService.createProduct(computer);
		
		Product result = productService.getProductDetail(computer.getProductId());
		assertEquals("Dell 8100", result.getName());
		assertTrue(1229.99 == result.getPrice());
		assertTrue(result instanceof Computer);
	}
	public void testCreateAndGetSoftware() {
		Software software = new Software();
		software.setName("Adobe PhotoShop");
		software.setPrice(129.99);
		software.setDescription("Adobe PhotoShop Enterprise Edition");
		software.setAvailability(Availability.IN_STOCK);
		software.setMedia("CD-ROM");
		software.setItemQuantity(2);
		List<String> platforms = new ArrayList<String>();
		platforms.add("Windows XP");
		platforms.add("Linux");
		software.setPlatforms(platforms);
		
		productService.createProduct(software);
		
		Product result = productService.getProductDetail(software.getProductId());
		assertEquals("Adobe PhotoShop", result.getName());
		assertTrue(129.99 == result.getPrice());
		assertTrue(result instanceof Software);
		List<String> platformsInDb = ((Software)result).getPlatforms();
		assertEquals(2, platforms.size());
		assertEquals("Windows XP", platformsInDb.get(0));
		assertEquals("Linux", platformsInDb.get(1));
	}
	
	public void testListProducts() {
		ProductRef[] products = productService.listProducts(Category.SOFTWARE);
		assertTrue(products.length > 0);
	}

	public void testListAllProducts() {
		ProductRef[] products = productService.listAllProducts();
		for (int i = 0; i < products.length; i++) {
			System.out.println(products[i].getName());
		}
		assertTrue(products.length > 2);
	}
	
	public void testCrudCustomer() {
		//Creates the customer
		Customer customer = new Customer();
		customer.setFirstName("Chunyun");
		customer.setLastName("Zhao");
		customer.setEmailAddress("chunyun.zhao@gmail.com");
		
		PaymentMethod creditCard = new PaymentMethod();
		creditCard.setCardNumber("1111111111111111");
		creditCard.setDescription("Citi Diamond Preferred");
		creditCard.setPaymentType(PaymentType.CREDIT_CARD);
		creditCard.setVerificationCode("123");
		creditCard.setExpirationDate(Calendar.getInstance());
		
		PaymentMethod debitCard = new PaymentMethod();
		debitCard.setCardNumber("2222222222222222");
		debitCard.setDescription("Bank Of America");
		debitCard.setPaymentType(PaymentType.DEBIT_CARD);
		debitCard.setVerificationCode("456");
		debitCard.setExpirationDate(Calendar.getInstance());
		
		customer.setPaymentMethods(new PaymentMethod[] {creditCard, debitCard});
		
		CustomerRef ref = customerService.createCustomer(customer);
		
		//Gets the customer
		Customer result = customerService.getCustomer(ref.getCustomerId());
		
		assertEquals("Chunyun", result.getFirstName());
		assertEquals("Zhao", result.getLastName());
		assertEquals("chunyun.zhao@gmail.com", result.getEmailAddress());
		
		PaymentMethod[] paymentMethodsInDb = result.getPaymentMethods();
		assertNotNull(paymentMethodsInDb);
		assertEquals(2, paymentMethodsInDb.length);
		
		assertEquals("Citi Diamond Preferred", paymentMethodsInDb[0].getDescription());
		assertEquals("Bank Of America", paymentMethodsInDb[1].getDescription());
		assertEquals(PaymentType.CREDIT_CARD, paymentMethodsInDb[0].getPaymentType());
		assertEquals(PaymentType.DEBIT_CARD, paymentMethodsInDb[1].getPaymentType());
		
		//Updates the customer
		result.setEmailAddress("cyzhao@hotmail.com");
		result.getPaymentMethods()[0].setDescription("Citi Diamond Preferred - Updated");
		result.getPaymentMethods()[1].setDescription("Bank Of America - Updated");

		customerService.updateCustomer(result);
		
		//Now gets the just updated customer
		Customer updatedCustomerInDb = customerService.getCustomer(customer.getCustomerId());
		assertEquals("Citi Diamond Preferred - Updated",updatedCustomerInDb.getPaymentMethods()[0].getDescription());
		assertEquals("Bank Of America - Updated",updatedCustomerInDb.getPaymentMethods()[1].getDescription());
	}
	
	public void testPlaceOrderAndGet() {
		CustomerRef[] customers = customerService.listCustomers();
		assertTrue(customers.length > 0);
		Customer customer = customerService.getCustomer(customers[0].getCustomerId());
		
		ProductRef[] products = productService.listAllProducts();
		assertTrue(products.length > 1);
		Product productToOrder = new Book();
		productToOrder.setProductId(products[0].getProductId());
		
		Order order = new Order();
		order.setOrderDate(new Date());
		order.setCustomer(customer);
		order.setPaymentMethod(customer.getPaymentMethods()[0]);
		order.setTotalPrice(120.99);
		Address address = new Address();
		address.setStreet("123 Chain Bridge");
		address.setCity("Sterling");
		address.setState("VA");
		order.setShippingAddress(address);
		order.setBillingAddress(address);
		order.setFreeShipping(true);
		
		OrderItem orderItem = new OrderItem();
		orderItem.setProduct(productToOrder);
		orderItem.setQuantity(2);
		
		Set<OrderItem> orderItems = new HashSet<OrderItem>();
		orderItems.add(orderItem);
		
		order.setOrderItems(orderItems);
		
		orderService.placeOrder(order);
		
		Order orderInDb = orderService.getOrderDetail(order.getOrderId());
		
		assertEquals("123 Chain Bridge", orderInDb.getBillingAddress().getStreet());
		assertEquals("Sterling", orderInDb.getBillingAddress().getCity());
		assertEquals("VA", orderInDb.getBillingAddress().getState());
		assertTrue(orderInDb.getFreeShipping());

		assertEquals("123 Chain Bridge", orderInDb.getShippingAddress().getStreet());
		assertEquals("Sterling", orderInDb.getShippingAddress().getCity());
		assertEquals("VA", orderInDb.getShippingAddress().getState());
		
		assertNotNull(orderInDb.getPaymentMethod());
		System.out.println(orderInDb.getPaymentMethod().getDescription());
		assertNotNull(orderInDb.getCustomer());
		System.out.println(orderInDb.getCustomer().getFirstName());
		OrderItem[] items = (OrderItem[])orderInDb.getOrderItems().toArray(new OrderItem[orderInDb.getOrderItems().size()]);
		assertEquals(1, items.length);
		assertEquals(productToOrder.getProductId(), items[0].getProduct().getProductId());
	}

	public static Test suite() {
		 TestSuite suite = new TestSuite();		 
	      suite.addTest(new ServicesTest("testGetUsingInvalidId"));
	      suite.addTest(new ServicesTest("testCreateAndGetBook"));
	      suite.addTest(new ServicesTest("testCreateAndGetComputer"));
	      suite.addTest(new ServicesTest("testCreateAndGetSoftware"));
	      suite.addTest(new ServicesTest("testListProducts"));
	      suite.addTest(new ServicesTest("testListAllProducts"));
	      suite.addTest(new ServicesTest("testCrudCustomer"));
	      suite.addTest(new ServicesTest("testPlaceOrderAndGet"));
	      return suite;
	  }	
}
