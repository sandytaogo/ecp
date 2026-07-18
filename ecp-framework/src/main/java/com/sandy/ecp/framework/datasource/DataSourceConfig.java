/*
 * Copyright 2024-2040 the original author or authors.
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
package com.sandy.ecp.framework.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

//@Configuration
public class DataSourceConfig {
	
//    @Bean
//    @ConfigurationProperties("spring.datasource.db1")
//    public DataSource db1() {
//        return DruidDataSourceBuilder.create().build();
//    }
//
//    @Bean
//    @ConfigurationProperties("spring.datasource.db2")
//    public DataSource db2() {
//        return DruidDataSourceBuilder.create().build();
//    }

    @Bean
    @Primary
    public DataSource multipleDataSource(
        @Qualifier("db1") DataSource db1,
        @Qualifier("db2") DataSource db2) {
        
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("db1", db1);
        targetDataSources.put("db2", db2);

        //MultipleDataSource multipleDataSource = new MultipleDataSource();
//        multipleDataSource.setTargetDataSources(targetDataSources);
//        multipleDataSource.setDefaultTargetDataSource(db1);
//        return multipleDataSource;
        return null;
    }
    
}