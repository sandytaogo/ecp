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

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;

/**
 * 类似 SecurityContextConcurrencyStrategy，插件式扩展实现：被执行目标方法执行前后的处理包装
 * @author sandy
 * @since 1.0.0 2024-09-12 12:12:12
 */
public class EcpHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

    private final Collection<EcpHystrixCallableWrapper> wrappers;

    public EcpHystrixConcurrencyStrategy(Collection<EcpHystrixCallableWrapper> wrappers) {
        this.wrappers = wrappers;
    }

    @Override
    public <T> Callable<T> wrapCallable(Callable<T> callable) {
        return new CallableWrapperChain<>(callable, wrappers.iterator()).wrapCallable();
    }

    private static class CallableWrapperChain<T> {

        private final Callable<T> callable;

        private final Iterator<EcpHystrixCallableWrapper> wrappers;

        CallableWrapperChain(Callable<T> callable, Iterator<EcpHystrixCallableWrapper> wrappers) {
            this.callable = callable;
            this.wrappers = wrappers;
        }

        Callable<T> wrapCallable() {
            Callable<T> delegate = callable;
            while (wrappers.hasNext()) {
                delegate = wrappers.next().wrap(delegate);
            }
            return delegate;
        }
    }
}

