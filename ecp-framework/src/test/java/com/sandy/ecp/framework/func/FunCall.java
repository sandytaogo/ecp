package com.sandy.ecp.framework.func;

import java.util.function.Function;

public class FunCall {
	
	public String sign(Function<Integer, String> in) {
		int t = 23;
		System.out.println("sign");
		return in.apply(t);
//		return null;
	}
	
	public String append(int b) {
		System.out.println("append");
		return b + "";
	}
	
	public static void main(String[] args) {
		IFunction<FunCall> o = FunCall::new;
		FunCall impl = o.apply();
		String res = impl.sign(impl::append);
		System.out.println("res:"  + res);
		res = impl.sign(a -> {
			System.out.println("a");
			return a+ "哈哈";
		});
		System.out.println("res:"  + res);
	}
}



