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
package com.sandy.ecp.framework.vo;

import java.io.Serializable;

import com.sandy.ecp.framework.model.ModelState;

/**
 * abstract value object
 * @author Sandy
 * @param <PK>
 * @version 1.0.0
 * @since 1.0.0 28th 12 2018
 */
public abstract class AbstractVO<PK> implements Serializable, Cloneable {
	
	/**
	 * serial version uid.
	 */
	private static final long serialVersionUID = 1231294490787480738L;

	private PK id;
	
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
		if(!(obj instanceof AbstractVO)) {
			return false;
		}
		AbstractVO<?> other = (AbstractVO<?>) obj;
		if(null == id) {
			if(null != other.getId()) {
				return false;
			}
		}
		return id.equals(other.getId());
	}
	
	@Override
	public int hashCode() {
		int prime = 31, result = 1;
		return null != id ? prime * result + id.hashCode() : 0;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder(100);
		buffer.append(getClass().getName());
		buffer.append(" [id=").append(id);
		buffer.append("]");
		return buffer.toString();
	}
}
