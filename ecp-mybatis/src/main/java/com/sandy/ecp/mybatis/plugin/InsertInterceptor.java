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
package com.sandy.ecp.mybatis.plugin;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

/**
 * 数据拦截处理器
 * @author Sandy
 * @version 1.0.0
 */
@Intercepts(@Signature(method = "update", type = Executor.class, args = { MappedStatement.class, Object.class }))
public class InsertInterceptor implements Interceptor {

	protected Properties properties;
	
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		System.out.println("进入plugin intercept...");
		Object[] args = invocation.getArgs();
		MappedStatement ms = (MappedStatement) args[0];
		Object parameter = args[1];
		
		SqlCommandType commandType = ms.getSqlCommandType();
		if(commandType == SqlCommandType.INSERT) {
			System.out.println("====== insert ======");
		}else if(commandType == SqlCommandType.UPDATE) {
			System.out.println("====== update ======");
		}else if(commandType == SqlCommandType.DELETE) {
		}
		System.out.println(parameter);
		Object result = invocation.proceed();
		return result;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}