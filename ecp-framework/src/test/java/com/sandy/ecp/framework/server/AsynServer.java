package com.sandy.ecp.framework.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannel;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.Pipe;

public class AsynServer {

	AsynchronousChannelGroup group;
	
	AsynchronousChannel channel ;
	
	AsynchronousServerSocketChannel serverSocket ;
	
	public void start() throws IOException {
		
		serverSocket = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(5000));
		//Future<AsynchronousSocketChannel> future = serverSocket.accept();
		serverSocket.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
			public void completed(AsynchronousSocketChannel result, Object attachment) {
				System.out.println(result);
			}
			public void failed(Throwable exc, Object attachment) {
				System.out.println(attachment);
			}
		});
	}
	
	public static void main(String[] args) throws IOException {
		AsynServer server = new AsynServer();
		Pipe pipe = null;
		System.out.println(pipe);
		server.start();
	}
	
}
