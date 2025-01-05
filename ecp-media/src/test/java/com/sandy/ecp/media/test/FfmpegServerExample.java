/*
 * Copyright 2024-2030 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sandy.ecp.media.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.sandy.ecp.media.util.IOUtils;

/**
 * @author Sandy
 */
public class FfmpegServerExample extends Thread {
	
	boolean isrun = false;
	
	ServerSocket serverSocket;
	
	public FfmpegServerExample() {
		super.setDaemon(false);
		super.setPriority(NORM_PRIORITY);
		setName("SERVER-THREAD");
	}
	
	public void init () throws IOException {
		serverSocket = new ServerSocket(3000);
		System.out.println("init...");
	}
	
	@Override
	public void run() {
		System.out.println("start...");
		isrun = true;
		while(isrun) {
			FileInputStream fileInputStream = null;
			try {
				Socket socket = serverSocket.accept();
				fileInputStream = new FileInputStream("H:\\Program Files\\nginx-1.13.3\\html\\media\\jrgn\\chunkfile_15s_000.ts");
				byte[] b = new byte[90000000];
				fileInputStream.read(b);
				OutputStream outputStream = socket.getOutputStream();
				outputStream.write(b);
				outputStream.close();
			} catch (IOException e) {
				// TODO: handle exception
			} finally {
				IOUtils.close(fileInputStream);
			}
		}
	}
	
	public void shutdown() throws IOException {
		isrun = false;
		System.err.println("over");
		serverSocket.close();
	}
	
	public static void main(String[] args) throws IOException {
		
		final FfmpegServerExample server = new FfmpegServerExample();
		server.init();
		server.start();
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				server.isrun = false;
				try {
					server.shutdown();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		synchronized(server) {
			try {
				server.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
