/*
 * Copyright 2020-2040 the original author or authors.
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
package com.sandy.ecp.ehcache;

import java.util.concurrent.TimeUnit;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;

import com.sandy.ecp.cache.config.EcpClusteredCacheConfigurations;
import com.sandy.ecp.cache.spi.EcpTerracottaClusteredStoreClientServiceConfiguration;

public class EhcacheDistributedExample {

    public static void main(String[] args) {
        // 构建缓存配置
        CacheConfigurationBuilder<String, String> configBuilder =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class,
                        ResourcePoolsBuilder.heap(100))
                        .withExpiry(Expirations.timeToLiveExpiration(Duration.of(1, TimeUnit.MINUTES)))
                        .withService(new EcpClusteredCacheConfigurations());

        // 构建并初始化CacheManager
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("myDistributedCache", configBuilder)
                .using(new EcpTerracottaClusteredStoreClientServiceConfiguration())
                .build(true);

        // 获取缓存实例
        Cache<String, String> myCache = cacheManager.getCache("myDistributedCache", String.class, String.class);

        // 使用缓存
        myCache.put("key", "value");
        String value = myCache.get("key");
        System.out.println("缓存中的值: " + value);

        // 关闭CacheManager
        cacheManager.close();
    }
}
