package com.sandy.cloud.gateway;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
		if (logger.isInfoEnabled()) {
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
		logger.info("headers={}", headers.keySet());
		if (headers.getContentType() == null) {
			headers.set("contentType", MediaType.APPLICATION_JSON_UTF8_VALUE);
			logger.info("headers after={}", headers.keySet());
		}
		return chain.filter(exchange.mutate().response(response).build());
	}
}
