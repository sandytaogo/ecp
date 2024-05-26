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

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * ViewList (view object)
 *
 * @author Sandy
 * @Since 2.0.0 17th 06 2018  
 */
public class ViewList implements Serializable {

	/**
	 * serial version uid.
	 */
	private static final long serialVersionUID = -7403122655320961374L;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Integer code = 200;
	private Long total;
	private Integer page;
	private Object rows;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Object footer;
	
	public ViewList() {
		super();
	}
	
	public ViewList(Integer page, Long total, Object rows) {
		super();
		this.total = total;
		this.page = page;
		this.rows = rows;
	}
	
	public static ViewList success() {
		Object [] rows = {};
		ViewList result = new ViewList(0, 0L, rows);
		return result;
	}
	
	public static ViewList success(Integer page, Integer total, Object rows) {
		ViewList result = new ViewList(page, total.longValue(), rows);
		return result;
	}
	
	public static ViewList success(Integer page, Integer total, Object rows, Object footer) {
		ViewList result = new ViewList(page, total.longValue(), rows);
		result.setFooter(footer);
		return result;
	}
	
	public static ViewList success(Integer page, Long total, Object rows) {
		ViewList result = new ViewList(page, total, rows);
		return result;
	}
	
	public static ViewList success(Integer page, Long total, Object rows, Object footer) {
		ViewList result = new ViewList(page, total, rows);
		result.setFooter(footer);
		return result;
	}
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Object getRows() {
		return rows;
	}
	public void setRows(Object rows) {
		this.rows = rows;
	}
	public Object getFooter() {
		return footer;
	}
	public void setFooter(Object footer) {
		this.footer = footer;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder(200);
		buffer.append("ViewList [");
		buffer.append("total=").append(total);
		buffer.append(",page=").append(page);
		buffer.append(",rows=").append(rows);
		buffer.append(",footer=").append(footer);
		buffer.append(']');
		return buffer.toString();
	}
}
