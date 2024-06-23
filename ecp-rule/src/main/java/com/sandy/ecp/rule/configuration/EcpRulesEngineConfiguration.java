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
package com.sandy.ecp.rule.configuration;

import java.io.IOException;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.sandy.ecp.rule.context.EcpRuleService;

/**
 * 规则引擎配置类
 * @author Sandy
 * @date 2024-05-05 12:12:12
 */
public class EcpRulesEngineConfiguration {

	// 指定规则文件存放的目录
	private static final String RULES_PATH = "rules/";
	
	@Autowired
	private EcpRuleService ecpRuleService;

	public KieFileSystem kieFileSystem() throws IOException {
		KieServices kieServices = ecpRuleService.getKieServices();
		System.setProperty("drools.dateformat", "yyyy-MM-dd");
		KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		Resource[] files = resourcePatternResolver.getResources("classpath*:" + RULES_PATH + "*.*");
		String path = null;
		for (Resource file : files) {
			path = RULES_PATH + file.getFilename();
			kieFileSystem.write(ResourceFactory.newClassPathResource(path, "UTF-8"));
		}
		return kieFileSystem;
	}

	public KieContainer kieContainer() throws IOException {
		KieServices kieServices = ecpRuleService.getKieServices();
		KieRepository kieRepository = kieServices.getRepository();
		kieRepository.addKieModule(kieRepository::getDefaultReleaseId);
		KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem());
		kieBuilder.buildAll();
		return kieServices.newKieContainer(kieRepository.getDefaultReleaseId());
	}

	public KieBase kieBase() throws IOException {
		return kieContainer().getKieBase();
	}

//	public KModuleBeanFactoryPostProcessor kiePostProcessor() {
//		return new KModuleBeanFactoryPostProcessor();
//	}
}
