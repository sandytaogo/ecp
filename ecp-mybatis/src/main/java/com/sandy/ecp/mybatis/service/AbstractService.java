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
package com.sandy.ecp.mybatis.service;

import java.util.List;

import com.sandy.ecp.framework.domain.AbstractIdEntity;
import com.sandy.ecp.mybatis.mapper.AbstractMapper;
/**
 * 基礎mybatis 數據庫服務層.
 * @author Sandy
 * @since 1.0.0 2012-04-02 12:11:02
 * @param <T> object.
 * @param <PK> id.
 */
public abstract class AbstractService<Mapper extends AbstractMapper<T, PK>, T extends AbstractIdEntity<?>, PK> {

	private Mapper mapper;
	
	public Mapper getMapper() {
		return mapper;
	}
	
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}
	
	public T queryById(PK id) {
		if(null == id) {
			return null;
		}
		return mapper.selectById(id);
	}
	
	public T save(T entity) {
		mapper.save(entity);
		return entity;
	}
	
	
	public List<T> save(List<T> entitys) {
		
		return entitys;
	}
	
	public T update(T entity) {
		if(null == entity) {
			return null;
		}
		int result = mapper.update(entity);
		if(0 < result) {
			return entity;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public boolean delete(T entity) {
		if(null == entity) {
			return false;
		}
		int result = mapper.deleteById((PK)entity.getId());
		if(0 < result) {
			return true;
		}
		return false;
	}
}
