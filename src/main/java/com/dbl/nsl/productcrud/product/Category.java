package com.dbl.nsl.productcrud.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Category extends PayloadWithName {
	
	private List<ProductName> products;

	public Category() {
		super();
	}

	public Category(String name, List<ProductName> products) {
		super(name);
		this.products = products;
	}

	public List<ProductName> getProducts() {
		if (Objects.isNull(products)) {
			products = new ArrayList<>();
		}
		return products;
	}

	public void setProducts(List<ProductName> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return super.toString() + " Category [products=" + products + "]";
	}
	
}
