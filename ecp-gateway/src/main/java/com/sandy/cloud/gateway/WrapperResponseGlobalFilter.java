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
package com.sandy.cloud.gateway;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
/**
 * @author Sandy
 * @since 1.0.0 2023-01-01 12:12:12 
 */
@Component
public class WrapperResponseGlobalFilter implements GlobalFilter, Ordered {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		ServerHttpResponse originalResponse = exchange.getResponse();
		if (logger.isDebugEnabled()) {
			logger.info("url={}", request.getURI());
		}
//		DataBufferFactory bufferFactory = originalResponse.bufferFactory();
		ServerHttpResponseDecorator response = new ServerHttpResponseDecorator(originalResponse) {
			@Override
			public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
				return super.writeWith(body);
	        }
			@Override
	        public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
				Flux.from(body).flatMapSequential(a -> a);
	            return writeWith(Flux.from(body).flatMapSequential(p -> p));
	        }
		};
		HttpHeaders headers = response.getHeaders();
		if (headers.getContentType() == null) {
			if (logger.isDebugEnabled()) {
				logger.info("headers after={}", headers.keySet());
				logger.info("headers={}", headers.keySet());
			}
		}
//		headers.set("contentType", MediaType.APPLICATION_JSON_UTF8_VALUE);
		return chain.filter(exchange.mutate().response(response).build());
	}
}
