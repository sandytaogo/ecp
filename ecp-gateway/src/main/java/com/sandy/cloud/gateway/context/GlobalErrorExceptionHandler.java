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
package com.sandy.cloud.gateway.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;
/**
 * @author Sandy
 * @since 1.0.0 2023-01-01 12:12:12 
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalErrorExceptionHandler implements ErrorWebExceptionHandler {
	
	@Autowired(required = false)
	protected ObjectMapper objectMapper;

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		ServerHttpResponse response = exchange.getResponse();
		if (response.isCommitted()) {
			return Mono.error(ex);
		}
		// JOSN格式返回
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		if (ex instanceof ResponseStatusException) {
			response.setStatusCode(((ResponseStatusException) ex).getStatus());
		}
//		return response.writeWith(Mono.fromSupplier(() -> {
//			DataBufferFactory bufferFactory = response.bufferFactory();
//			try {
//				// todo 返回响应结果，根据业务需求，自己定制
//				CommonResponse resultMsg = new CommonResponse("500", ex.getMessage(), null);
//				return bufferFactory.wrap(objectMapper.writeValueAsBytes(resultMsg));
//			} catch (JsonProcessingException e) {
//				log.error("Error writing response", ex);
//				return bufferFactory.wrap(new byte[0]);
//			}
//		}));
		return null;
	}
}
