package com.sandy.ecp.framework.func;

import java.util.Map;
import java.util.function.Function;

@SuppressWarnings("rawtypes")
public class FunImpl implements Function<Object, Map> {

	@Override
	public Map<?, ?> apply(Object t) {
		System.out.println("sdfsad");
		return null;
	}
}
