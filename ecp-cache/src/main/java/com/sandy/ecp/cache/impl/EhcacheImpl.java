/*
 * Copyright 2020-2030 the original author or authors.
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
package com.sandy.ecp.cache.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import com.sandy.ecp.cache.ICache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 企业云平台框架缓存基础实现.
 * @author Sandy
 * @date 2020-04-04 12:12:12
 * @since 0.0.1
 */
public class EhcacheImpl<K, V> implements ICache<K, V> {

	protected CacheManager cacheManager;
	
	protected Cache cache;
	
	public EhcacheImpl() {
		super();
	}
	
	public EhcacheImpl(String cacheName) {
		super();
	}
	
	/**
	 * 根據key獲取緩存内容.
	 * @param key
	 * @return value.
	 */
	@SuppressWarnings("unchecked")
	public V get(K key) {
		// Retrieve the data element
		Element e = cache.get(key);
		if (e == null) {
			return null;
		}
		
		return (V) e.getObjectValue();
	}
	
	public Collection<V> get(Collection<K> keys) {
		if (keys == null || keys.size() == 0) {
            return Collections.emptyList();
        }
		Map<Object, Element> dataMap = cache.getAll(keys);
		if (dataMap == null || dataMap.size() <= 0) {
			return Collections.emptyList();
		}
		final Iterator<Element> values = dataMap.values().iterator();
		ArrayList<V> result = new ArrayList<V>(dataMap.size());
        while (values.hasNext()) {
            final Element e = values.next();
            if (e != null) {
                @SuppressWarnings("unchecked")
				final V value = (V) e.getObjectValue();
                if (value == null) {
                    continue;
                }
                result.add(value);
            }
        }
		return result;
	}
	
	public void put(K key, V value) {
		// Create a data element
		cache.put(new Element(key, value));
	}
	
	/**
	 * 移除缓存cachename中key对应的value
	 * @param cachename
	 * @param key
	 */
	public  void remove(Serializable key) {
		cache.remove(key);
	}
	
	/**
	 * 清除缓存名称为cachename的缓存
	 * @param cachename
	 */
	public void clear() {
		try {
			cache.removeAll();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
}
