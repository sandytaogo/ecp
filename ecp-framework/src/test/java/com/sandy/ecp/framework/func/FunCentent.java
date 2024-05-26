package com.sandy.ecp.framework.func;

import java.util.Map;
import java.util.function.Function;

public class FunCentent {

	public void exec(FunImpl impl) {
		
	}

	public void exec(Object impl) {
	}
	
	public void exe(Function<Map< ?, ?> , Map<?, ?>> t,  Map<String, Object> mapper) {
		t.apply(null);
		System.out.println(t);
	}
}
