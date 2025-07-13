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
package com.sandy.ecp.framework.web.listener;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * 企业云平台 扩展监听器.
 * 
 * @author Sandy
 * @date 2024-05-05 12:12:12
 */
public class EcpExtensionRegisterListener implements ServletContextListener {

	private static final String PLUGIN = "classpath*:/META-INF/config/plugin.xml";

	/** Logger available to subclasses */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = null;
		try {
			resources = resolver.getResources(PLUGIN);
			for (final Resource resource : resources) {
				if (logger.isInfoEnabled()) {
					logger.info("contextInitialized plugin={}", resource.getURL());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
