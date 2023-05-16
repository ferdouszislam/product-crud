package com.dbl.nsl.productcrud.product;

public abstract class PayloadWithName {

	protected String name;

	public PayloadWithName() {
		super();
	}

	public PayloadWithName(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "PayloadWithName [name=" + name + "]";
	}
	
}
