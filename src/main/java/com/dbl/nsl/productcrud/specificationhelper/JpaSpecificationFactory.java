package com.dbl.nsl.productcrud.specificationhelper;

import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

public abstract class JpaSpecificationFactory <T> {

	private Logger Log = LoggerFactory.getLogger(this.getClass());
	
	public Specification<T> getSpecification(List<QueryCriteria> criterias) {
		Specification<T> spec = null;
		for (QueryCriteria c : criterias) {
			Specification<T> s = getSingleSpecification(c);
			spec = Objects.isNull(spec) ? s : spec.and(s);
		}
		return spec;
	}
	
	@SuppressWarnings("unchecked")
	public Specification<T> getSingleSpecification(QueryCriteria qc) throws IllegalArgumentException {
		Objects.requireNonNull(qc);
		Objects.requireNonNull(qc.getFieldPath());
		Objects.requireNonNull(qc.getQueryOperation());
		Objects.requireNonNull(qc.getQueryValues());
		
		QueryCriteria.OPERATION operation = qc.getQueryOperation();
		String fieldPath = qc.getFieldPath();
		String fieldName = getFieldName(qc);
		boolean doJoin = doJoin(qc);
		List<String> queryValues = qc.getQueryValues();
		EntityFieldMapper efm = getEntityFieldsMapper();
		
		return (root, query, cb) -> {
			try {
				query.distinct(true);
				switch (operation) {
					case EQUAL :
						return cb.equal((doJoin ? getJoin(qc, root) : root).get(fieldName),
								efm.getFieldValue(fieldPath, queryValues.get(0)));
					case IN :
						return cb.in((doJoin ? getJoin(qc, root) : root).get(fieldName))
								.value(efm.getFieldValues(fieldPath, queryValues));
					
					case NOT_EQUAL :
						return cb.notEqual((doJoin ? getJoin(qc, root) : root).get(fieldName),
								efm.getFieldValue(fieldPath, queryValues.get(0)));
						
					case LIKE :
						return cb.like((doJoin ? getJoin(qc, root) : root).get(fieldName),
								"%" + efm.getFieldValue(fieldPath, queryValues.get(0)) + "%");
					
					case IS_NULL :
						return cb.isNull((doJoin ? getJoin(qc, root) : root).get(fieldName));
						
					case IS_NOT_NULL :
						return cb.isNotNull((doJoin ? getJoin(qc, root) : root).get(fieldName));
					
					case GREATER_THAN :
					case GREATER_THAN_OR_EQUAL :
					case LESS_THAN :
					case LESS_THAN_OR_EQUAL :
					case BETWEEN :
						return getCompareQueryPredicate(cb, root, qc, efm.getFieldValues(fieldPath, queryValues));
						
					default :
						throw new IllegalArgumentException("invalid operation: " + operation);
				}
			} catch (Exception e) {
				Log.info("exception: {} occurred for: {}, {}", e, qc);
			}
			return null;
		};
	}

	protected abstract EntityFieldMapper getEntityFieldsMapper();
	
	private boolean doJoin(QueryCriteria qc) {
		return qc.getFieldPath().split("\\.").length > 1;
	}
	
	@SuppressWarnings("rawtypes")
	private Join getJoin(QueryCriteria qc, Root root) throws IllegalArgumentException {
		if (!doJoin(qc)) {
			throw new IllegalArgumentException("can't convert: [" + qc.getFieldPath() + "] to join object");
		}
		String [] joinFields = qc.getFieldPath().split("\\.");
		Join join = root.join(joinFields[0]);
		for (int i = 1; i < joinFields.length - 1; i++) {
			join = join.join(joinFields[i]);
		}
		return join;
	}
	
	private String getFieldName(QueryCriteria qc) {
		String [] joinFields = qc.getFieldPath().split("\\.");
		if (joinFields.length == 0) return "";
		return joinFields[joinFields.length - 1];
	}
	
	@SuppressWarnings("unchecked")
	private <T1 extends Comparable<T1>> Predicate getCompareQueryPredicate(CriteriaBuilder cb, Root<T> root, QueryCriteria qc, List<T1> queryValues) 
			throws IllegalArgumentException {
		QueryCriteria.OPERATION operation = qc.getQueryOperation();
		String fieldName = getFieldName(qc);
		boolean doJoin = doJoin(qc);
		switch (operation) {
			case GREATER_THAN :
				return cb.greaterThan((doJoin ? getJoin(qc, root) : root).<T1>get(fieldName), queryValues.get(0));
			case GREATER_THAN_OR_EQUAL :
				return cb.greaterThanOrEqualTo((doJoin ? getJoin(qc, root) : root).<T1>get(fieldName), queryValues.get(0));
			case LESS_THAN :
				return cb.lessThan((doJoin ? getJoin(qc, root) : root).<T1>get(fieldName), queryValues.get(0));
			case LESS_THAN_OR_EQUAL :
				return cb.lessThanOrEqualTo((doJoin ? getJoin(qc, root) : root).<T1>get(fieldName), queryValues.get(0));
			case BETWEEN:
				return cb.between((doJoin ? getJoin(qc, root) : root).<T1>get(fieldName), queryValues.get(0), queryValues.get(1));
			default :
				throw new IllegalArgumentException("invalid operation: " + operation);
		}
	}
	
}
