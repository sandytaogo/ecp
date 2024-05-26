package com.sandy.ecp.framework;import java.util.Locale;

public class UpperTest {

	public static void main(String[] args) {
		//System.out.println(buff.toString()); //00100000
		//01100001
		//01111010
		//01100000
		//0100 0001
		//0101 1010
		//01011111
		String ch = "abcdefghijklmnopqrstuvwxyz-------AAAAA2343232撒撒飞洒发生旦范德萨";
		StringBuilder buff = new StringBuilder();
		char c ; 
		for (int i = 0; i < ch.length(); i++ ) {
			c = ch.charAt(i);
			if (96 == (c & 96)) {
				//c = (char) (c & 223);
				c = (char) (c & 95);
			} 
			buff.append(c);
		}
		int i = 1,j = 0x2;
		System.out.println(i ^ j);
		System.out.println(Locale.getDefault());
		ch.toUpperCase();
		System.out.println(buff);
	}
}
