package com.sandy.ecp.mybatis.binding;

import java.util.HashMap;
import java.util.Map;

import com.sandy.ecp.mybatis.plugin.MapperRefresh;

/**
 * 重写 org.apache.ibatis.session.Configuration.StrictMap 类
 * 来自 MyBatis3.4.0版本，修改 put 方法，允许反复 put更新。
 */
public class StrictMap extends HashMap {

	private static final long serialVersionUID = -4950446264854982944L;

	private String name;

	public StrictMap(String name, int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
		this.name = name;
	}

	public StrictMap(String name, int initialCapacity) {
		super(initialCapacity);
		this.name = name;
	}

	public StrictMap(String name) {
		super();
		this.name = name;
	}

	public StrictMap(String name, Map m) {
		super(m);
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	public Object put(String key, Object value) {
		//ThinkGem 如果现在状态为刷新，则刷新(先删除后添加)
		if (MapperRefresh.isRefresh()) {
			remove(key);
			MapperRefresh.log.debug("refresh key:" + key.substring(key.lastIndexOf(".") + 1));
		}
		//ThinkGem end
		if (containsKey(key)) {
			throw new IllegalArgumentException(name + " already contains value for " + key);
		}
		if (key.contains(".")) {
			final String shortKey = getShortName(key);
			if (super.get(shortKey) == null) {
				super.put(shortKey, value);
			} else {
				super.put(shortKey, new Ambiguity(shortKey));
			}
		}
		return super.put(key, value);
	}

	public Object get(Object key) {
		Object value = super.get(key);
		if (value == null) {
			throw new IllegalArgumentException(name + " does not contain value for " + key);
		}
		if (value instanceof Ambiguity) {
			throw new IllegalArgumentException(((Ambiguity) value).getSubject() + " is ambiguous in " + name
					+ " (try using the full name including the namespace, or rename one of the entries)");
		}
		return value;
	}

	private String getShortName(String key) {
		final String[] keyparts = key.split("\\.");
		return keyparts[keyparts.length - 1];
	}

	protected static class Ambiguity {
		private String subject;
		public Ambiguity(String subject) {
			this.subject = subject;
		}

		public String getSubject() {
			return subject;
		}
	}
}
