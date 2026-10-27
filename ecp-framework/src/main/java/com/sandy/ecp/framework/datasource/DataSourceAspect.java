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

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sandy.ecp.framework.annotation.EnableEncrypt;

@Aspect
//@Component
@Order(-1)
public class DataSourceAspect {
    
	@Before("@annotation(com.example.DataSource)")
    public void beforeSwitchDS(JoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        EnableEncrypt ds = method.getAnnotation(EnableEncrypt.class);
        if (ds != null) {
            DataSourceContextHolder.setDataSource(ds.inName());
        }
    }

    @After("@annotation(com.example.DataSource)")
    public void afterSwitchDS(JoinPoint point) {
        DataSourceContextHolder.clear();
    }
}
