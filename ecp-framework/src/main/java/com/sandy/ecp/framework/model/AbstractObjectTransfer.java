/*
 * Copyright 2018-2030 the original author or authors.
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
package com.sandy.ecp.framework.model;

import java.util.ArrayList;
import java.util.List;

import com.sandy.ecp.framework.domain.AbstractIdEntity;
import com.sandy.ecp.framework.vo.AbstractVO;

/**
 * Abstract Object Transfer
 * @author Sandy
 * @param <PK>
 * @version 1.0.0
 * @since 1.0.0 04th 12 2018
 */
public abstract class AbstractObjectTransfer<VO extends AbstractVO<PK>, PO extends AbstractIdEntity<PK>, PK> {

	public abstract PO toPO(VO vo);	
	
	public abstract VO toVO(PO po);	
	
	public List<PO> toPO(List<VO> vos) {
		List<PO> pos = new ArrayList<PO>();
		if(null == vos) {
			return pos;
		}
		for (VO vo : vos) {
			pos.add(toPO(vo));
		}
		return pos;
	}
	
	public List<VO> toVO(List<PO> pos) {
		List<VO> vos = new ArrayList<VO>();
		if(null == pos) {
			return vos;
		}
		for (PO po : pos) {
			vos.add(toVO(po));
		}
		return vos;
	}
	
}
