package com.sandy.ecp.framework.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * 
 * @author Sandy
 * @see https://www.cnblogs.com/daoluanxiaozi/p/3274925.html
 * @see http://ifeve.com/java-nio-scattergather/
 * @see http://tutorials.jenkov.com/java-nio/overview.html
 */
public class UdpClient {

	static byte[] ret = { 0x7C, 0x13, 0x1F, 0x47, 0x0E, 0x03, 0x06, 0x0F,
			0x13, 0x30, 0x00, 0x08, 0x00, 0x00, 0x00, 0x55,
			(byte) 0xAA, 0x70, 0x7A };
	
	public static void main(String[] args) throws IOException {
		DatagramChannel channel = DatagramChannel.open();
		channel.configureBlocking(false);
		//InputStream input = new ByteArrayInputStream(null);
		//ByteArrayOutputStream baos = new ByteArrayOutputStream(128);
		ByteBuffer buf = ByteBuffer.wrap("十分大师傅十分士大夫发生范德萨范德萨范德萨范德萨范德萨范德萨的撒范德萨范德萨发范德萨范德萨发十分十分大师傅的撒范德萨范德萨发是撒旦发射点撒范德萨范德萨犯得上发射点傻傻的发呆大师傅的方式发达撒范德萨分发的是否发放打发打发反对法撒范德萨付德萨范撒地方的萨芬的萨芬第三方的的萨芬的萨芬撒范德萨德萨范德萨付德萨范的省份的打法萨芬萨多少德萨范德萨分到手付德萨范是芬萨范德萨范德萨付德萨范多少".getBytes());
		int result = channel.send(buf, new InetSocketAddress("127.0.0.1",9999));
		System.out.println(result);
	}
}
