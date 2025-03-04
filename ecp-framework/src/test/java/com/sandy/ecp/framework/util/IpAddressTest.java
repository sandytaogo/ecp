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
package com.sandy.ecp.framework.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

import com.sandy.ecp.framework.context.SpringContextHolder;
import com.sandy.ecp.framework.ip.IPSeeker;

/**  
 * Description: 
 * @author Sandy
 * @Date 2024年4月3日 下午6:44:23
 * @since 1.0.0
 */
public class IpAddressTest {
	
	AnnotationConfigApplicationContext axt = null;
	
	@Before
	public void testBefore() {
		axt = new AnnotationConfigApplicationContext(SpringContextHolder.class);
		BeanDefinitionRegistry registry = (BeanDefinitionRegistry) axt.getAutowireCapableBeanFactory();
		 // 创建 bean 信息
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SpringContextHolder.class);
        // 动态注册 bean
        registry.registerBeanDefinition("springContextUtil", beanDefinitionBuilder.getBeanDefinition());
        StandardEnvironment env = axt.getBean(StandardEnvironment.class);
        // 获取可变属性源集合
        MutablePropertySources propertySources = env.getPropertySources();
        // 创建一个新的属性源
        Map<String, Object> map = new HashMap<>();
        map.put("ip.address.path", "h:/qqwry.dat");
        PropertySource<?> myPropertySource = new MapPropertySource("MyPropertySource", map);
        // 将新的属性源添加到环境中
        propertySources.addLast(myPropertySource);
        
	}

	@Test
	public void ipAddressTest() {
		
		System.out.println(IPSeeker.getInstance().getAddress("120.79.95.13"));
	}
	
	@After
	public void after() {
		axt.close();
	}
}
