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
 * 用来封装ip相关信息，目前只有两个字段，ip所在的国家和地区
 * @author Sandy
 */
public class IPLocation {
    public String country;
    public String area;
    public IPLocation() {
        country = area = "";
    }
    public IPLocation getCopy() {
        IPLocation ret = new IPLocation();
        ret.country = country;
        ret.area = area;
        return ret;
    }
    
    public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getArea()
	{
		return area;
	}

	public void setArea(String area)
	{
		if (area.trim().equals("CZ88.NET"))
			this.area = "本机或本网络";
		else
			this.area = area;
	}
}
