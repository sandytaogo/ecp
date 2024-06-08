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
package com.sandy.ecp.framework.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 
 * @author Sandy
 * @2023-10-11 16:12:34
 */
public class ApplicationBuilder extends SpringApplicationBuilder {
	
	ApplicationBuilder(Class<?>... sources) {
		super(sources);
	}

	@Override
	public SpringApplicationBuilder sources(Class<?>... sources) {
		return super.sources(sources);
	}
	
	@Override
	protected SpringApplication createSpringApplication(Class<?>... sources) {
		String  systemProperty = System.getProperty("spring.main.allow-bean-definition-overriding");
        if (systemProperty == null) {
            System.setProperty("spring.main.allow-bean-definition-overriding", "true");
        }
		return super.createSpringApplication(sources);
	}
	
	@Override
	public ConfigurableApplicationContext run(String... args) {
		ConfigurableApplicationContext context = super.run(args);
		return context;
	}
}
