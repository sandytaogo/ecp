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
package com.sandy.ecp.rule.context;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

/**
 * 企业管理云平台 规则引擎基础服务.
 * @author Sandy
 * @date 2024-05-05 12:12:12
 */
public class EcpRuleService {
	
	private final KieServices kieServices = KieServices.Factory.get();
	
	public KieServices getKieServices() {
		return kieServices;
	}

	/**
	 * create kie session
	 * @param path rule.drl file.
	 * @return
	 */
	public KieSession createKieSession(String path) {
      KieFileSystem fileSystem = kieServices.newKieFileSystem();
      fileSystem.write(ResourceFactory.newClassPathResource(path));
      KieBuilder kb = kieServices.newKieBuilder(fileSystem);
      kb.buildAll();
      KieModule km = kb.getKieModule();
      KieContainer kieContainerBuild = kieServices.newKieContainer(km.getReleaseId());
      KieSession kieSession = kieContainerBuild.newKieSession();
      return kieSession;
	}
}
