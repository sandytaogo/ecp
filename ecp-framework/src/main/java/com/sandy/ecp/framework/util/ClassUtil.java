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
package com.sandy.ecp.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.ClassUtils;

public class ClassUtil {

	public static void setAttribute(Object obj, String attrName, Object value, Class<?>... paramTypes) {
		Method method = ClassUtils.getMethod(obj.getClass(), attrName, paramTypes);
		try {
			method.invoke(obj, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Class<?> getClass(String className) throws ClassNotFoundException {
		return Class.forName(className);
	}

	public static Object getPrimitiveTypeDefaultObject(final Class<?> cls) {
		return PrimitiveTypeDefault.getPrimitiveNullObject(cls);
	}

	public static Field getClassField(final Class<?> cls, final String name) {
		Map<String, Field> map = null;
		if (map == null) {
			map = getClassFieldMap(cls);
		}
		return map.get(name);
	}

	public static String getClassSimpleName(final String className) {
		if (className == null) {
			return null;
		}
		final int index = className.lastIndexOf(46);
		if (index >= 0) {
			return className.substring(index + 1);
		}
		return className;
	}

	public static String getClassSimpleName(final Class<?> cls) {
		final String name = cls.getName();
		final int index = name.lastIndexOf(46);
		if (index >= 0) {
			return name.substring(index + 1);
		}
		return name;
	}

	public static Map<String, Field> getClassFieldMap(final Class<?> cls) {
		final Map<String, Field> map = new ConcurrentHashMap<String, Field>();
		final Field[] fields = cls.getDeclaredFields();
		Field fld = null;
		for (int i = 0; i < fields.length; ++i) {
			fld = fields[i];
			map.put(fld.getName(), fld);
		}
		return map;
	}
}
