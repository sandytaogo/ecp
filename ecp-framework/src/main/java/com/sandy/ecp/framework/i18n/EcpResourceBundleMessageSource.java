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
package com.sandy.ecp.framework.i18n;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * 企业云平台 国际化语言支持
 * @author Sandy
 * @since 1.0.0
 * @date 2024-12:23 12:12:12
 */
public class EcpResourceBundleMessageSource extends ResourceBundleMessageSource {
	
	private String locationPattern = "classpath*:/META-INF/ecp-i18n/*.properties";
	private String location;

	public EcpResourceBundleMessageSource() {
		super();
		this.locationPattern = "classpath*:/ecp-i18n/*.properties";
		super.setBasenames("ecp-i18n/messages");//"classpath*:ecp-i18n/messages"
	}
	
	@Override
	public void setBasename(String basename) {
		super.setBasename(basename);
	}
	
	/**
	 * 确保路径正确指向jar内的资源文件
	 */
	protected Resource getResourceBySuffix(String basename) {
        return new ClassPathResource("/" + basename + ".properties");
    }

	@Override
	public String toString() {
		return "EcpResourceBundleMessageSource [locationPattern=" + locationPattern + ", location=" + location + "]";
	}
}
