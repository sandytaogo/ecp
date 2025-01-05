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
package com.sandy.ecp.framework.context;

import com.sandy.ecp.framework.reference.EcpReferenceModel;

/**
 * 企业云平台 基础设施参考模型
 * @author Sandy
 * @since 1.0.0 
 * @date 2024-12-12 12:12:12
 */
public interface IEcpReferenceModelContext {
	
	/**
	 * 获取参考模型.
	 * @param name 模型名称.
	 * @param version 版本号.
	 * @return EcpReferenceModel
	 */
	public EcpReferenceModel getModel(String name, String version);
	
}
