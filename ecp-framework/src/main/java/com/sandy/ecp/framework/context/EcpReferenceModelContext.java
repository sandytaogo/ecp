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
package com.sandy.ecp.framework.context;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.sandy.ecp.framework.reference.EcpReferenceModel;

/**
 * 企业云平台 参考模型上下对象
 * 
 * @author Sandy
 * @since 1.0.0
 * @date 2024-12:23 12:12:12
 */
@Component
public class EcpReferenceModelContext extends AbstractReferenceModel implements InitializingBean, IEcpReferenceModelContext {

	private static final Logger logger = LoggerFactory.getLogger(EcpReferenceModelContext.class);

	public EcpReferenceModelContext() {
		super();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info("begin init ecp load reference model ...");
		}
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		URL[] urls = ((URLClassLoader) classLoader).getURLs();
		String ignorePath = "target/classes/";
		for (URL url : urls) {
			if (url.getFile().endsWith(ignorePath)) {
				continue;
			}
			if (logger.isInfoEnabled()) {
				logger.info(url.getFile().substring(url.getFile().lastIndexOf("/"), url.getFile().length()));
			}
			try (URLClassLoader urlClassLoader = new URLClassLoader(urls, null)) {
				loadConfigClasses(urlClassLoader, url.getFile());
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("the end ecp load reference model ...");
		}
	}

	protected List<URL> getJarUrls(String configDirPath) {
		File configDir = new File(configDirPath);
		File[] jarFiles = configDir.listFiles((dir, name) -> name.endsWith(".jar"));
		List<URL> jarUrls = new ArrayList<>();
		if (jarFiles != null) {
			for (File jarFile : jarFiles) {
				try {
					jarUrls.add(jarFile.toURI().toURL());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return jarUrls;
	}

	private void loadConfigClasses(URLClassLoader classLoader, String jarPath) throws Exception {
		// 假设配置类都在包名config下
		String packageName = "config";
		JarFile jarFile = new JarFile(jarPath.replaceAll("%20", " "));
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String name = entry.getName();
			if (name.startsWith(packageName.replace('.', '/')) && name.endsWith(".class")) {
				String className = name.substring(0, name.length() - 6).replace('/', '.');
				Class<?> clazz = classLoader.loadClass(className);
				// 这里可以根据需要检查clazz是否是配置类的实例
				// 如果是，则进行相应的处理
			} else if (name.startsWith(packageName.replace('.', '/'))) {
				logger.info(name);
			}
		}
	}

	@Override
	public EcpReferenceModel getModel(String name, String version) {
		// TODO Auto-generated method stub
		return null;
	}
}
