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
import net.servicefixture.sample.dao.ProductDao;
import net.servicefixture.sample.domain.Category;
import net.servicefixture.sample.domain.Product;
import net.servicefixture.sample.domain.ProductRef;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Chunyun Zhao
 * @since Aug 23, 2006
 */
public class ProductDaoImpl extends HibernateDaoSupport implements ProductDao {

	public ProductRef[] listProducts(Category category) {
		String classname = "Product";
		switch(category) {
			case BOOK:
				classname = "Book";
				break;
			case COMPUTER:
				classname = "Computer";
				break;
			case SOFTWARE:
				classname = "Software";
				break;
		}
		List list = getHibernateTemplate().find("select p.productId, p.name from " + classname + " as p");
		return toProductRefs(list);
	}

	public ProductRef[] listAllProducts() {
		List list = getHibernateTemplate().find("select p.productId, p.name from Product as p");
		return toProductRefs(list);
	}

	public Product getProductDetail(long productId) {
		Product product = (Product)getHibernateTemplate().get(Product.class, new Long(productId));
		if ( product == null ) {
			throw new ApplicationException("Invalid productId:" + productId);
		}
		return product;
	}

	public ProductRef createProduct(Product product) {
		getHibernateTemplate().save(product);
		return new ProductRef(product);
	}

	private ProductRef[] toProductRefs(List list) {
		ProductRef[] products = new ProductRef[list.size()];
		for (int i = 0; i < list.size(); i++) {
			products[i] = new ProductRef();
			Object[] array = (Object[])list.get(i);
			products[i].setProductId(((Long)array[0]).longValue());
			products[i].setName((String)array[1]);
		}
		return products;
	}
}

