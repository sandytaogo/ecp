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
package com.sandy.ecp.framework.ip;
/**
 * @author Sandy
 */
public class IPEntry {
	
	public String beginIp;
	public String endIp;
	public String country;
	public String area;
	/**
	 * 构造函数
	 */
	public IPEntry() {
		beginIp = endIp = country = area = "";
	}
	public String toString() {
		return this.area + "  " + this.country + "IP范围:" + this.beginIp + "-"
				+ this.endIp;
	}
}
