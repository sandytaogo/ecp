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
package com.sandy.ecp.framework.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.sandy.ecp.framework.model.ModelState;

/**
 * abstract value object
 * @author Sandy
 * @param <PK>
 * @version 1.0.0
 * @since 1.0.0 28th 12 2018
 */
@MappedSuperclass
public abstract class AbstractIdEntity<PK> implements Serializable {

	/**
	 * serial version uid.
	 */
	private static final long serialVersionUID = 8085067367006711063L;

	@Id
	@Column(name = "GID", length = 36, precision = 0, nullable = false)
	private PK id;
	
	@Transient
	private int modelState = ModelState.DEFAULT;

	public PK getId() {
		return id;
	}
	public void setId(PK id) {
		this.id = id;
	}
	
	public int getModelState() {
		return modelState;
	}

	public void setModelState(int modelState) {
		this.modelState = modelState;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(this.getClass() != obj.getClass()) {
			return false;
		}
		AbstractIdEntity<?> other = (AbstractIdEntity<?>) obj;
		if(null == id) {
			return null == other.id;
		}
		return id.equals(other.id);
	}
	
	@Override
	public int hashCode() {
		int prime = 31;
		return null != id ? id.hashCode() + prime : 0 ;
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder(50);
		buffer.append(getClass().getName());
		buffer.append(" [id=").append(getId());
		buffer.append("]");
		return buffer.toString();
	}
}
