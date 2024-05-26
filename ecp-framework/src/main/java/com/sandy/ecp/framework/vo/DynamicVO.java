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
import java.util.HashMap;

import com.sandy.ecp.framework.model.ModelState;

/**
 * Dynamic Framework Value Object
 * @author Sandy
 * @version 1.0.0
 * @since 1.0.0 28th 12 2018
 */
public class DynamicVO extends HashMap<String, Object> implements Serializable, Cloneable {
	
	/**
	 * serial version uid.
	 */
	private static final long serialVersionUID = 7629935782395290113L;

	private int modelState = ModelState.DEFAULT;

	public int getModelState() {
		return modelState;
	}

	public void setModelState(int modelState) {
		super.put(ModelState.NAME, modelState);
		this.modelState = modelState;
	}
	
}
