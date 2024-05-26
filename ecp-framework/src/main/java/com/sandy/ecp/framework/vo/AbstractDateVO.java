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
package com.sandy.ecp.framework.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Abstract Date Value Object
 * @author Sandy
 * @param <PK>
 * @version 1.0.0
 * @since 1.0.0 04th 12 2018
 */
public abstract class AbstractDateVO<PK> extends AbstractVO<PK> {

	/**
	 * serial version uid.
	 */
	private static final long serialVersionUID = 5328039121267156624L;

	private Long createdId;
	private Date createdTime;
	private Long updatedId;
	private Date updatedTime;
	
	public Long getCreatedId() {
		return createdId;
	}
	public void setCreatedId(Long createdId) {
		this.createdId = createdId;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	@JsonInclude(Include.NON_NULL)
	public Long getUpdatedId() {
		return updatedId;
	}
	public void setUpdatedId(Long updatedId) {
		this.updatedId = updatedId;
	}
	@JsonInclude(Include.NON_NULL)
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder(100);
		buffer.append(getClass().getName());
		buffer.append(" [id=").append(getId());
		buffer.append(", createdId=").append(createdId);
		buffer.append(", createdTime=").append(createdTime);
		buffer.append(", updatedId=").append(updatedId);
		buffer.append(", updatedTime=").append(updatedTime);
		buffer.append("]");
		return buffer.toString();
	}
}

