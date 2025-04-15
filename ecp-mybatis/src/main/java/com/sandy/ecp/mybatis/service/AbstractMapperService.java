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

import java.util.Date;
import java.util.List;

import com.sandy.ecp.framework.domain.AbstractIdEntity;
import com.sandy.ecp.framework.domain.Paging;
import com.sandy.ecp.framework.domain.SearchFilter;
import com.sandy.ecp.framework.model.AbstractObjectTransfer;
import com.sandy.ecp.framework.model.ModelState;
import com.sandy.ecp.framework.util.PageArrayList;
import com.sandy.ecp.framework.util.PageList;
import com.sandy.ecp.framework.vo.AbstractDateVO;
import com.sandy.ecp.framework.vo.AbstractVO;
import com.sandy.ecp.mybatis.mapper.AbstractMapper;
/**
 * 基礎mybatis 數據庫服務層.
 * @author Sandy
 * @since 1.0.0 2012-04-02 12:11:02
 * @param <T> object.
 * @param <PK> id.
 */
@SuppressWarnings("unchecked")
public abstract class AbstractMapperService<VO extends AbstractVO<PK>, PO extends AbstractIdEntity<PK>,Transfer extends AbstractObjectTransfer<VO, PO, PK>, Mapper extends AbstractMapper<PO, PK>, PK> {

	private Mapper mapper;
	
	private Transfer entityTransfer;
	
	public Mapper getMapper() {
		return mapper;
	}
	
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
	}
	
	public Transfer loadTransfer(Class<Transfer> clazz) {
		Transfer transfer = null;
		try {
			transfer = clazz.newInstance();
		} catch (Exception e) {
		
		}
		return transfer;
	}
	
	public Transfer getEntityTransfer() {
		return entityTransfer;
	}
	
	public void setEntityTransfer(Transfer entityTransfer) {
		this.entityTransfer = entityTransfer;
	}
	
	public VO queryById(PK id) {
		if(null == id) {
			return null;
		}
		return getEntityTransfer().toVO(mapper.selectById(id));
	}
	
	public PageList<VO> queryPageListByCondition(List<SearchFilter> filterList, Paging paging) {
		List<PO> data = mapper.selectPageListByFilter(filterList, paging);
		int total = 0;
		if (data != null && data.size() > 0) {
			total = mapper.selectPageCountByFilter(filterList);
		}
		return new PageArrayList<VO>(getEntityTransfer().toVO(data), paging, total);
	}
	
	public VO save(VO model) {
		if (model instanceof AbstractDateVO) {
			AbstractDateVO<PK> vo = (AbstractDateVO<PK>) model;
			if (vo.getCreatedTime() == null) {
				vo.setCreatedTime(new Date());
			}
		}
		mapper.save(getEntityTransfer().toPO(model));
		model.setModelState(ModelState.NEW);
		return model;
	}
	
	public List<VO> batchSave(List<VO> models) {
		mapper.batchSave(getEntityTransfer().toPO(models));
		return models;
	}
	
	public VO update(VO model) {
		if(null == model) {
			return null;
		}
		if (model instanceof AbstractDateVO) {
			AbstractDateVO<PK> vo = (AbstractDateVO<PK>) model;
			if (vo.getUpdatedTime() == null) {
				vo.setUpdatedTime(new Date());
			}
		}
		int result = mapper.update(getEntityTransfer().toPO(model));
		if(0 < result) {
			model.setModelState(ModelState.MODIFY);
			return model;
		}
		return null;
	}
	
	public boolean delete(VO entity) {
		if(null == entity) {
			return false;
		}
		int result = mapper.deleteById(entity.getId());
		if(0 < result) {
			return true;
		}
		return false;
	}
}
