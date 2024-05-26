package com.sandy.ecp.framework.util;

/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the HUIFU  License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://sandy.com
 */
import java.util.Collection;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.support.DataAccessUtils;

/**
 * Miscellaneous utility methods for DAO implementations. Useful with any data
 * access technology.
 * 
 * @author Sandy
 * @since 1.0.0 26th 04 2017
 */
public class JdbcDataAccessUtils extends DataAccessUtils {
	/**
	 * Return a single result object from the given Collection.
	 * <p>
	 * Throws an exception if 0 or more than 1 element found.
	 * 
	 * @param results
	 *            the result Collection (can be {@code null})
	 * @return the single result object
	 * @throws IncorrectResultSizeDataAccessException
	 *             if more than one element has been found in the given
	 *             Collection
	 * @throws EmptyResultDataAccessException
	 *             if no element at all has been found in the given Collection
	 */
	public static <T> T requiredSingleResult(Collection<T> results) throws IncorrectResultSizeDataAccessException {
		int size = (results != null ? results.size() : 0);
		if (size == 0) {
			return null;
		}
		if (results.size() > 1) {
			throw new IncorrectResultSizeDataAccessException(1, size);
		}
		return results.iterator().next();
	}
	
	/**
	 * Convert a name in camelCase to an underscored name in lower case. Any
	 * upper case letters are converted to lower case with a preceding underscore.
	 * @param name the string containing original name
	 * @return the converted name
	 */
	public static String underscoreName(String name) {
		StringBuilder result = new StringBuilder();
		if (name != null && name.length() > 0) {
			result.append(name.substring(0, 1).toUpperCase());
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				if (s.equals(s.toUpperCase())) {
					result.append("_");
					result.append(s);
				} else {
					result.append(s.toUpperCase());
				}
			}
		}
		return result.toString();
	}
}

