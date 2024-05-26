package com.sandy.ecp.framework.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/***
 * 
 * @author Sandy
 * @since 22th 05 2022
 */
public class DynamicDatasource extends AbstractRoutingDataSource {

	@Override
	protected Object determineCurrentLookupKey() {
		return null;
	}
}
