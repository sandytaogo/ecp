/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sandy.ecp.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sandy.ecp.framework.domain.AbstractIdEntity;
import com.sandy.ecp.framework.domain.Paging;
import com.sandy.ecp.framework.domain.SearchFilter;

/**
 * 基礎mybatis 數據庫服務層.
 * @author Sandy
 * @since 1.0.0 2012-04-02 12:11:02
 * @param <T> object.
 * @param <PK> id.
 */
public abstract interface AbstractMapper<T extends AbstractIdEntity<?>, PK> {

	/**
	 * select by id
	 * @param id Primary key number identity. 
	 * @return 
	 */
	T selectById(PK id);
	
	/**
	 * Select Page List By Filter
	 * @param filterList
	 * @param paging
	 * @return PagingList <T>
	 */
	List<T> selectPageListByFilter(@Param("filterList") List<SearchFilter> filterList, @Param("pageRequest") Paging pageRequest);
	
	/**
	 *  Select Page Count By Filter
	 * @param filterList
	 * @return count.
	 */
	int selectPageCountByFilter(@Param("filterList") List<SearchFilter> filterList);
	
	/**
	 * 保存数据.
	 * @param entity
	 */
	PK save(T entity);
	
	/**
	 * 批量保存.
	 * @param entitys
	 */
	int batchSave(List<T> entitys);
	
	/**
	 * update
	 * 
	 * @param entity
	 * @return update count
	 */
	int update(T entity);
	
	/**
	 * 批量删除.
	 * @param entitys
	 */
	 int batchUpdate(List<T> entitys);
	
	/**
	 * 
	 * @param id
	 * @return delete success
	 */
	 int deleteById(PK id);
	
	/**
	 * 批量删除.
	 * @param entitys
	 */
	 int batchDelete(List<T> entitys);
}
