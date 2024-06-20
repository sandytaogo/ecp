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
package com.sandy.ecp.mybatis.factory;

import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.session.SqlSession;

public class EcpMapperProxyFactory<T> extends MapperProxyFactory<T> {

	public EcpMapperProxyFactory(Class<T> mapperInterface) {
        super(mapperInterface);
    }
 
    @Override
    public T newInstance(SqlSession sqlSession) {
//        final MapperProxy<T> mapperProxy = new CustomMapperProxy<>(sqlSession, this.getMapperInterface(), this.getMethodCache());
//        return this.newInstance(null);
    	
    	
    	return null;
    }
 
//    private class CustomMapperProxy<T> extends MapperProxy<T> {
//
//		private static final long serialVersionUID = 4477491592857590608L;
//
//		 private final SqlSession sqlSession;
//		  private final Class<T> mapperInterface;
//		  private final Map<Method, MapperMethodInvoker> methodCache;
//
//		  public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map<Method, MapperMethodInvoker> methodCache) {
//		    this.sqlSession = sqlSession;
//		    this.mapperInterface = mapperInterface;
//		    this.methodCache = methodCache;
//		  }
//    }
}
