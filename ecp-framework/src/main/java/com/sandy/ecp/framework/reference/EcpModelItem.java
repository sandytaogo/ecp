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

/**
 * 企业云平台 参考模型项.
 * @author Sandy
 * @date 2024-12-12 12:12:12
 */
public class EcpModelItem {

	private String name;
	private String caption;
	private String dataType;
	private Boolean multiValue;
	private String fixFlag;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 精度.
	 */
	private Integer scale;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public Boolean getMultiValue() {
		return multiValue;
	}
	public void setMultiValue(Boolean multiValue) {
		this.multiValue = multiValue;
	}
	public String getFixFlag() {
		return fixFlag;
	}
	public void setFixFlag(String fixFlag) {
		this.fixFlag = fixFlag;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Integer getScale() {
		return scale;
	}
	public void setScale(Integer scale) {
		this.scale = scale;
	}
	
}
