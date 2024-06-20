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
package com.sandy.ecp.cloud.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestTemplate;

import com.sandy.ecp.framework.context.SpringContextUtil;

/**
 * 框架遠程跨服務調用.
 * @author Sandy
 * @date 2023-10-05 22:22:22
 */
public class DefaultMappService implements MappService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private RestTemplate restTemplate;
	
	@Autowired(required = false)
	private DiscoveryClient discoveryClient;
	
	protected DiscoveryClient getDiscoveryClient() {
		if (discoveryClient == null) {
			synchronized(this) {
				if (discoveryClient == null) {
					this.discoveryClient = SpringContextUtil.getBean(DiscoveryClient.class);
				}
			}
		}
		return discoveryClient;
	}
	
	protected RestTemplate getRestTemplate() {
		if (restTemplate == null) {
			synchronized(this) {
				try {
					ApplicationContext context = SpringContextUtil.getApplicationContext();
					this.restTemplate = (RestTemplate) context.getBean("mappClient");
				} catch (Throwable localThrowable) {
					//ignore
					restTemplate = new RestTemplate();
		 		}
			}
		}
		return restTemplate;
	}
	
	private ServiceInstance getServiceInstance(String applicationName) {
		List<ServiceInstance> serviceInstanceList = getDiscoveryClient().getInstances(applicationName);
		ServiceInstance si = null;
		for(ServiceInstance service : serviceInstanceList) {
			si = service; 
		}
		return si;
	 }
	
	@Override
	public <T> T doGet(String paramString, Class<T> paramClass, Object... paramVarArgs) {
		ServiceInstance service = getServiceInstance("");
		Map<String, String> metadata = service.getMetadata();
		String contextPath = metadata.get("contextPath");
		if (null == contextPath) {
			contextPath = "";
		}
		String callServiceResult = getRestTemplate().getForObject(service.getUri().toString() + contextPath +"/countryItems" ,String.class);
		if (logger.isInfoEnabled()) {
			logger.info("Service id = {}, receive data: {}", service.getServiceId(), callServiceResult);
		}
		return null;
	}

	@Override
	public <T> T doGet(String appName, String paramString, Map<String, Object> paramMap, Class<T> paramClass) {
		ServiceInstance service = getServiceInstance(appName);
		Map<String, String> metadata = service.getMetadata();
		String contextPath = metadata.get("contextPath");
		if (null == contextPath) {
			contextPath = "";
		}
		return getRestTemplate().getForObject(service.getUri().toString() + contextPath + paramString , paramClass, paramMap);
	}
}
