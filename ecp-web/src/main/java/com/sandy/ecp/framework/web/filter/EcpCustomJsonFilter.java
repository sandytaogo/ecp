/*
 * Copyright 2018-2030 the original author or authors.
 *
 * Licensed under the company, Version 2.0 (the "License");
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
package com.sandy.ecp.framework.web.filter;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

/**
 * 平台JSON 序列化字段过滤器.
 * @author Sandy
 * @date 2023-05-05 12:12:12
 */
@SuppressWarnings("deprecation")
public class EcpCustomJsonFilter extends FilterProvider {
	
	private static final ThreadLocal<Map<Class<?>, String[]>> include = new ThreadLocal<Map<Class<?>, String[]>>();

	@Override
	public BeanPropertyFilter findFilter(Object filterId) {
		throw new UnsupportedOperationException("不支持访问即将过期的过滤器");
	}
	
	/**
     * 清空规则
     */
    public static void clear() {
        include.remove();
    }
 
    /**
     * 设置过滤规则
     * @param clazz 规则
     */
    public static void add(Class<?> clazz, String ... fields) {
        Map<Class<?>, String[]> map = include.get();
        if (map == null) {
            map = new HashMap<>();
            include.set(map);
        }
        map.put(clazz, fields);
    }
    
    /**
     * 判断该字段是否需要，返回 true 序列化，返回 false 则过滤
     * @param type 实体类类型
     * @param name 字段名
     */
    public boolean apply(Class<?> type, String name) {
        Map<Class<?>, String[]> map = include.get();
        if (map == null) {
            return true;
        }
        String[] fields = map.get(type);
        for (String field : fields) {
            if (field.equals(name)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public PropertyFilter findPropertyFilter(Object filterId, Object valueToFilter) {
    	return new SimpleBeanPropertyFilter() {
    		@Override
	    	public void serializeAsField(Object pojo, JsonGenerator jg, SerializerProvider sp, PropertyWriter pw) throws Exception {
	    		if (apply(pojo.getClass(), pw.getName())) {
	                pw.serializeAsField(pojo, jg, sp);
	            } else if (!jg.canOmitFields()) {
	                pw.serializeAsOmittedField(pojo, jg, sp);
	            }
	    	}
    	};
    }
}
