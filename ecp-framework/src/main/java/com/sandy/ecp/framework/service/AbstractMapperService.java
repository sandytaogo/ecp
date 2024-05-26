package com.sandy.ecp.framework.service;

import java.util.List;

import com.sandy.ecp.framework.domain.AbstractIdEntity;
import com.sandy.ecp.framework.mapper.AbstractMapper;
import com.sandy.ecp.framework.model.AbstractObjectTransfer;
import com.sandy.ecp.framework.vo.AbstractVO;

public abstract class AbstractMapperService<VO extends AbstractVO<PK>, PO extends AbstractIdEntity<PK>,Transfer extends AbstractObjectTransfer<VO, PO, PK>, Mapper extends AbstractMapper<PO, PK>, PK> {

	private Mapper mapper;
	
	private Transfer entityTransfer;
	
	public Mapper getMapper() {
		return mapper;
	}
	
	public void setMapper(Mapper mapper) {
		this.mapper = mapper;
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
	
	public VO save(VO model) {
		mapper.save(getEntityTransfer().toPO(model));
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
		int result = mapper.update(getEntityTransfer().toPO(model));
		if(0 < result) {
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
