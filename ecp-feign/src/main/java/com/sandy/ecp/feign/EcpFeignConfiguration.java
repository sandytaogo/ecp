/*
 * Copyright 2024-2030 the original author or authors.
 *
 * Licensed under the company License, Version 2.0 (the "License");
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
package com.sandy.ecp.feign;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import com.sandy.ecp.feign.codec.EcpStreamDecoder;
import com.sandy.ecp.feign.hystrix.EcpHystrixCallableWrapper;
import com.sandy.ecp.feign.hystrix.EcpHystrixConcurrencyStrategy;
import com.sandy.ecp.feign.hystrix.EcpRequestAttributeAwareCallableWrapper;
import com.sandy.ecp.feign.interceptors.EcpRequestInterceptor;

import feign.Feign;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;

/**
 * 企业云平台 feign Hystrix 隔离线程池处理配置.
 * @author Sandy
 * @since 1.0.0 2024-09-12 12:12:12
 */
@Configuration
public class EcpFeignConfiguration {

	private Logger log = LoggerFactory.getLogger(getClass());

	private static String systemId;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	protected Feign.Builder builder;

	@Autowired(required = false)
	private List<EcpHystrixCallableWrapper> wrappers;
	
	@Autowired
	private ObjectFactory<HttpMessageConverters> messageConverters;

	public EcpFeignConfiguration() {
		super();
	}
	
	public static String getSystemId() {
		return systemId;
	}

	@Bean
	public EcpRequestAttributeAwareCallableWrapper requestAttributeAwareCallableWrapper() {
		return new EcpRequestAttributeAwareCallableWrapper();
	}

	@Bean
	public HystrixConcurrencyStrategy requestContextHystrixConcurrencyStrategy() {
		return new EcpHystrixConcurrencyStrategy(wrappers);
	}
	
	public EcpRequestInterceptor ecpRequestInterceptor() {
		return new EcpRequestInterceptor();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public Decoder feignDecoder() {
		return new OptionalDecoder(new ResponseEntityDecoder(new EcpStreamDecoder(this.messageConverters)));
	}

	@PostConstruct
	public void init() {
		if (environment.getProperty("system.id") != null) {
			EcpFeignConfiguration.systemId = environment.getProperty("system.id");
		}
		try {
			HystrixConcurrencyStrategy strategy = HystrixPlugins.getInstance().getConcurrencyStrategy();
			if (strategy instanceof EcpHystrixConcurrencyStrategy) {
				return;
			}
			EcpHystrixConcurrencyStrategy hystrixConcurrencyStrategy = new EcpHystrixConcurrencyStrategy(wrappers);
			// 获取原来的相关数据配置
			HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins.getInstance().getCommandExecutionHook();
			HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance().getEventNotifier();
			HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance().getMetricsPublisher();
			HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance().getPropertiesStrategy();
			// 打印日志
			if (log.isDebugEnabled()) {
				log.debug("Current Hystrix plugins configuration is [concurrencyStrategy [{}], eventNotifier [{}], metricPublisher [{}], propertiesStrategy [{}]]",
					hystrixConcurrencyStrategy, eventNotifier, metricsPublisher, propertiesStrategy);
				log.debug("Registering Muses Hystrix Concurrency Strategy.");
			}
			// 重置再重新填充
			// 直接设置会触发异常 Caused by: java.lang.IllegalStateException: Another strategy was
			// already registered.
			HystrixPlugins.reset();
			HystrixPlugins.getInstance().registerConcurrencyStrategy(hystrixConcurrencyStrategy);
			HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook);
			HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
			HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
			HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
		} catch (Exception e) {
			log.error("Failed to register Ecp Hystrix Concurrency Strategy", e);
		}
		// builder.requestInterceptor(ecpRequestInterceptor());
	}
}
