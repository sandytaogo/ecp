package com.sandy.cloud.gateway;

import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

public class AppGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

	@Override
	public org.springframework.cloud.gateway.filter.GatewayFilter apply(Object config) {
		return null;
	}
}
