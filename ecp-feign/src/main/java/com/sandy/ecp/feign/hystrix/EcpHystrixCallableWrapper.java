/*
 * Copyright 2024-2030 the original author or authors.
 *
 * Licensed under the company License, Version 2.0 (the "License");
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
package com.sandy.ecp.feign.hystrix;

import java.util.concurrent.Callable;

/**
 * 用于包装hystrix中Callable实例的接口
 * @author sandy
 * @since 1.0.0 2024-09-12 12:12:12
 */
public interface EcpHystrixCallableWrapper {

    /**
     * 包装Callable实例
     * @param callable 待包装实例
     * @param <T> 返回类型
     * @return 包装后的实例
     */
    <T> Callable<T> wrap(Callable<T> callable);
}


