/*
 * Copyright 2023-2030 the original author or authors.
 *
 * Licensed under the sandy License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.sandy.org/licenses/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sandy.ecp.framework.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.env.Environment;

import com.sandy.ecp.framework.util.Sm4Util;
import com.zaxxer.hikari.HikariDataSource;

/**
 * 数据源密码加密支持.
 * @author Sandy
 * 2023-03-02
 */
public class JasyptHikariDataSource implements BeanPostProcessor {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired(required = false)
	private Environment env;
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof com.zaxxer.hikari.HikariDataSource) {
			HikariDataSource dataSource = (HikariDataSource) bean;
			String pwd = null;
			try {
				if (env != null && Boolean.valueOf(env.getProperty("spring.datasource.encrypt.sm4"))) {
					pwd = Sm4Util.decryptEcb(dataSource.getPassword());
					dataSource.setPassword(pwd);
				}
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return bean;
	}
}
