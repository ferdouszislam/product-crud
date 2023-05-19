package com.dbl.nsl.productcrud.specificationhelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QueryCriteria {
	
	private String fieldPath;

	private OPERATION queryOperation;
	
	private List<String> queryValues;
	
	public QueryCriteria() {
		super();
	}
	
	public QueryCriteria(String fieldPath, OPERATION queryOperation, List<String> queryValues) {
		super();
		this.fieldPath = fieldPath;
		this.queryOperation = queryOperation;
		this.queryValues = queryValues;
	}

	public static List<QueryCriteria> getQueryCriterias(Map<String, Object> params) {
		List<QueryCriteria> queryCriterias = new ArrayList<>();
		for (Map.Entry<String, Object> param : params.entrySet()) {
			String fieldPath = param.getKey();
			List<String> queryValues = Arrays.asList(param.getValue().toString().split(","));
			queryCriterias.add(new QueryCriteria(fieldPath, OPERATION.IN, queryValues));
		}
		return queryCriterias;
	}

	public String getFieldPath() {
		return fieldPath;
	}

	public void setFieldPath(String fieldPath) {
		this.fieldPath = fieldPath;
	}

	public OPERATION getQueryOperation() {
		return queryOperation;
	}

	public void setQueryOperation(OPERATION queryOperation) {
		this.queryOperation = queryOperation;
	}

	public List<String> getQueryValues() {
		if (Objects.isNull(queryValues)) {
			queryValues = new ArrayList<>();
		}
		return queryValues;
	}

	public void setQueryValues(List<String> queryValues) {
		this.queryValues = queryValues;
	}

	@Override
	public String toString() {
		return "QueryCriteria [fieldPath=" + fieldPath + ", queryOperation="
				+ queryOperation + ", queryValues=" + queryValues + "]";
	}


	enum OPERATION {
		EQUAL ("EQUAL"),
		NOT_EQUAL ("NOT_EQUAL"),
		LIKE ("LIKE"),
		IS_NULL ("IS_NULL"),
		IS_NOT_NULL ("IS_NOT_NULL"),
		IN ("IN"),
		GREATER_THAN ("GREATER_THAN"),
		GREATER_THAN_OR_EQUAL ("GREATER_THAN_OR_EQUAL"),
		LESS_THAN ("LESS_THAN"),
		LESS_THAN_OR_EQUAL ("LESS_THAN_OR_EQUAL"),
		BETWEEN ("BETWEEN");
		
		private final String v;
		
		private OPERATION(String v) {
			this.v = v;
		}

		public String get() {
			return v;
		}
	}
	
}
