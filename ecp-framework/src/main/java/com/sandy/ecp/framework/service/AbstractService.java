package com.sandy.ecp.framework.service;

import java.util.List;

import com.sandy.ecp.framework.domain.AbstractIdEntity;
import com.sandy.ecp.framework.mapper.AbstractMapper;

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
