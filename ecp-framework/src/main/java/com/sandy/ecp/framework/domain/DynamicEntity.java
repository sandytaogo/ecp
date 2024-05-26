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

import java.util.HashMap;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.sandy.ecp.framework.model.ModelState;

/**
 * 框架動態模型.
 * @author Sandy
 * @since 1.0.0 2023-01-01 12:12:12 
 */
@MappedSuperclass
public abstract class DynamicEntity extends HashMap<String, Object> {

	/**
	 * serial version uid.
	 */
	private static final long serialVersionUID = -3483390975354813668L;
	
	@Transient
	private int modelState = ModelState.DEFAULT;

	public int getModelState() {
		return modelState;
	}

	public void setModelState(int modelState) {
		this.modelState = modelState;
	}
}
