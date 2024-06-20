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
package com.sandy.ecp.mybatis.binding;

import java.lang.reflect.Method;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.Configuration;

import com.sandy.ecp.mybatis.annotations.SqlCmd;

public class EcpMapperMethod extends MapperMethod {
	
    private final SqlCommand sqlCommand;
 
    public EcpMapperMethod(Class<?> mapperInterface, Method method, Configuration config) {
        super(mapperInterface, method, config);
        this.sqlCommand = new SqlCommand(config, mapperInterface, method);
    }
 
//    @Override
//    public MapperMethod bind(DataReader dataReader, Object parameterObject) {
//        return new CustomMapperMethod(this.mapperInterface, this.method, this.configuration, dataReader, parameterObject);
//    }
    
    public SqlCommand getSqlCommand() {
        return this.sqlCommand;
    }
 
    private class SqlCommand extends MapperMethod.SqlCommand {
    	private Method method; 
        SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
            super(configuration, mapperInterface, method);
            this.method = method;
        }
 
        @SuppressWarnings("unused")
		public boolean isSelect() {
            return this.method.isAnnotationPresent(SqlCmd.class);
        }
 
        @SuppressWarnings("unused")
		public String getSql() {
        	SqlCmd annotation = this.method.getAnnotation(SqlCmd.class);
            if (annotation != null) {
                return annotation.value();
            }
            return null;
        }
    }
}
