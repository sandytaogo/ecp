/*
 * Copyright 2024-2030 the original author or authors.
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
package com.sandy.ecp.framework.reference;

import com.sandy.ecp.framework.domain.AbstractDateEntity;

/**
 * 企业云平台 参考模型
 * @author Sandy
 * @date 2024-12-12 12:12:12
 */
public abstract class AbstractReferenceModel extends AbstractDateEntity<Long> {

	private static final long serialVersionUID = -1106566217880462880L;

	private String name;
	
	private String versoin;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersoin() {
		return versoin;
	}

	public void setVersoin(String versoin) {
		this.versoin = versoin;
	}
	
}
