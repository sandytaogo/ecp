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
package com.sandy.ecp.framework.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;
/**
 * Enterprise Cloud Platform spring Holder 上下文工具类
 * @author Sandy
 * 060219-062193
 * @since 1.0.0 31th 08 2022
 */
@Component
public class SpringContextHolder implements BeanFactoryAware, ApplicationContextAware, ImportBeanDefinitionRegistrar, DisposableBean {

	private static ApplicationContext applicationContext;
	
	private static BeanFactory beanFactory;
	
	public SpringContextHolder() {
		super();
	}
	
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextHolder.applicationContext = applicationContext;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		SpringContextHolder.beanFactory = beanFactory;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		if (name == null) {
			return null;
		}
		if (null != beanFactory) {
			try {
				T obj = (T) beanFactory.getBean(name);
				if (obj != null) {
					return obj;
				}
			} catch (BeansException e) {
				
			}
		}
		if (applicationContext != null) {
			return (T) applicationContext.getBean(name);
		}
		return null;
	}
	
	public static <T> T getBean(Class<T> requiredType) throws BeansException {
		if (null != beanFactory) {
			try {
				T obj = (T) beanFactory.getBean(requiredType);
				if (obj != null) {
					return obj;
				}
			} catch (BeansException e) {
				
			}
		}
		if (applicationContext != null) {
			return (T) applicationContext.getBean(requiredType);
		}
		return null;
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition().getBeanDefinition();
		beanDefinition.setBeanClass(Object.class);
		registry.registerBeanDefinition("aaaaaaaa", beanDefinition);
	}
	
	@Override
	public void destroy() throws Exception {
		
	}
}
