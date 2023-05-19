package com.dbl.nsl.productcrud.specificationhelper;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
public abstract class EntityFieldMapper {

	public abstract Comparable getFieldValue(String fieldPath, String value) 
			throws IllegalArgumentException;
	
	public List<Comparable> getFieldValues(String fieldPath, List<String> values) {
		return values.stream().map(v -> getFieldValue(fieldPath, v)).collect(Collectors.toList());
	}
}
