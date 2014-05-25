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

import net.servicefixture.sample.ValidationException;
import net.servicefixture.sample.dao.ProductDao;
import net.servicefixture.sample.domain.Category;
import net.servicefixture.sample.domain.Product;
import net.servicefixture.sample.domain.ProductRef;

/**
 * @author Chunyun Zhao
 * @since Aug 25, 2006
 */
public class ProductServiceImpl implements ProductService {
	private ProductDao productDao;

	public ProductRef[] listProducts(Category category) {
		return productDao.listProducts(category);
	}

	public Product getProductDetail(long productId) {
		return productDao.getProductDetail(productId);
	}
	public ProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public ProductRef[] listAllProducts() {
		return productDao.listAllProducts();
	}

	public ProductRef createProduct(Product product) {
		//Validation Logic
		if ( product.getPrice() < 0 ) {
			throw new ValidationException("Price can't be negative.");
		}
		return productDao.createProduct(product);
	}	
}
