package com.sandy.ecp.framework.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * 
 * @author Sandy
 * @see https://www.cnblogs.com/daoluanxiaozi/p/3274925.html
 * @see http://ifeve.com/java-nio-scattergather/
 * @see http://tutorials.jenkov.com/java-nio/overview.html
 */
public class UdpServer {

	static ByteBuffer buf = ByteBuffer.allocate(1024);

	public static void main(String[] args) throws IOException {
		DatagramChannel channel = DatagramChannel.open();
		channel.configureBlocking(false);
		// ByteArrayOutputStream baos = new ByteArrayOutputStream(128);
		channel.socket().bind(new InetSocketAddress(9999));
		buf.clear();
		//打开一个选择器
		Selector selector = Selector.open();
		channel.register(selector, SelectionKey.OP_READ);
		while (true) {
			int n = selector.select();
			if (n > 0) {
				// 获取以选择的键的集合
				Iterator<?> iterator = selector.selectedKeys().iterator();
				while (iterator.hasNext()) {
					buf.clear();
					SelectionKey key = (SelectionKey) iterator.next();
					if (key.isReadable()) {
						channel = (DatagramChannel) key.channel();
						SocketAddress address = channel.receive(buf);
						System.out.println(address);
						System.out.println(String.format("receive:%s", new String(buf.array(), "UTF-8")));
					}
					// 必须手动删除
					iterator.remove();
				}
			}
		}
	}
}
