/*
 * Copyright 2020-2030 the original author or authors.
 *
 * Licensed under the company, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.company.com/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sandy.ecp.jpa.dao.support;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 企业云平台JPA数据操作支持.
 * @author Sandy
 * @param <PK>
 * @version 1.0.0
 * @since 1.0.0 04th 12 2018
 */
public abstract class AbstractDaoSupport<T> {

	@Autowired
	protected EntityManager em;
	
	/**
	 * 根据条件获取原生SQL查询
	 * @param sql a native SQL query string
	 * @param paramMap query  parameter key value.
	 * @param offset startPosition position of the first result, numbered from 0
	 * @param maxResult maxResult maximum number of results to retrieve
	 * @param resultClass resultClass the class of the resulting instance(s)
	 * @return a list of the results
	 */
	@SuppressWarnings("unchecked")
	public List<T> queryNativeByCondition(String sql, Map<String, Object> paramMap, int offset, int maxResult, Class<T> resultClass) {
		
		Query query = em.createNativeQuery(sql, resultClass);
		
		paramMap.entrySet().stream().forEach((entry) -> {
			query.setParameter(entry.getKey(), entry.getValue());
		});
		query.setFirstResult(offset);
		query.setMaxResults(maxResult);
		List<?> data = query.getResultList();
		return (List<T>) data;
	}
}
