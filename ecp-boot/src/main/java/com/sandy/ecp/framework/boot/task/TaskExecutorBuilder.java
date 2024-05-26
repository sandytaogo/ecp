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
package com.sandy.ecp.framework.boot.task;

import java.math.BigDecimal;
import java.time.Duration;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * spring boot TaskExecutorBuilder
 * @author Sandy
 * @2023-10-11 16:12:34
 */
public class TaskExecutorBuilder {
	
	private String threadNamePrefix;
	private int queueCapacity;
	private int corePoolSize;
	private int maxPoolSize;
	private boolean allowCoreThreadTimeOut;
	private Duration keepAlive;

	public TaskExecutorBuilder threadNamePrefix(String threadNamePrefix) {
		this.threadNamePrefix = threadNamePrefix;
		return this;
	}
	
	public TaskExecutorBuilder queueCapacity (int queueCapacity) {
		this.queueCapacity = queueCapacity;
		return this;
	}
	public TaskExecutorBuilder corePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
		return this;
	}
	public TaskExecutorBuilder maxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
		return this;
	}
	public TaskExecutorBuilder allowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
		this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
		return this;
	}
	public TaskExecutorBuilder keepAlive(Duration keepAlive) {
		this.keepAlive = keepAlive;
		return this;
	}
	
	public TaskExecutorBuilder taskDecorator(Object taskDecorator) {
		return this;
	}
	
	public TaskExecutorBuilder awaitTermination(Boolean awaitTermination) {
		return this;
	}
	public TaskExecutorBuilder awaitTerminationPeriod(String awaitTermination) {
		return this;
	}
	
	public ThreadPoolTaskExecutor build() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setDaemon(true);
		executor.setKeepAliveSeconds(new BigDecimal(keepAlive.getSeconds()).intValue());
		executor.setAllowCoreThreadTimeOut(allowCoreThreadTimeOut);
		executor.setAwaitTerminationSeconds(15);
		executor.setThreadNamePrefix(threadNamePrefix);
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.initialize();
		return executor;
	}
}
