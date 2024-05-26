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

/**
 * abstract basic framework value object
 * @author Sandy
 * @version 1.0.0
 * @since 1.0.0 28th 12 2018
 */
public class Result implements Cloneable, Serializable {

	/**
	 * serial version uid.
	 */
	private static final long serialVersionUID = -3983771658885002684L;
	
	private boolean ispass = false;
	private Object result;
	private Integer code;
	private String msg;
	private Object data;
	
	public Boolean getIspass() {
		return ispass;
	}
	public void setIspass(Boolean ispass) {
		if (ispass) {
			code = 200;
		} else {
			code = 201;
		}
		this.ispass = ispass;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
