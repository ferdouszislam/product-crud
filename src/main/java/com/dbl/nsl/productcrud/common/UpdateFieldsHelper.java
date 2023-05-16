package com.dbl.nsl.productcrud.common;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class UpdateFieldsHelper {

	private static Logger Log = LoggerFactory.getLogger(UpdateFieldsHelper.class);
	
	/**
	 * set fields listed in {@code updateAllowedFields} from {@code request} to {@code existing}
	 * allow setting null value for fields if {@code allowNull} = {@code true}
	 */
	public static <T> T updateFields(T existing, T requested, List<String> updateAllowedFields, boolean allowNull) {
		Class<?> c = existing.getClass();
		while(Objects.nonNull(c)) {
			for (Field field : c.getDeclaredFields()) {
				field.setAccessible(true);
				final String fieldName = field.getName();
				final Object requestedFieldValue;
				try {
					requestedFieldValue = field.get(requested);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					Log.info("exception: {} occurred in get field: {}", e, fieldName);
					continue;
				}
				if (!updateAllowedFields.contains(fieldName) 
						|| (!allowNull && Objects.isNull(requestedFieldValue)) ) {
					continue;
				}
				try {
					field.set(existing, requestedFieldValue);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					Log.info("exception: {} occurred in set field: {}", e, fieldName);
				}
			}
			c = c.getSuperclass();
		}
		return existing;
	}
	
}
