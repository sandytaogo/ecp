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
package com.sandy.ecp.mqtt.server;

import java.io.IOException;
import java.net.ServerSocket;

public class EcpMqttServer {
	
	int port = 1883;
	
	private ServerSocket serverSocket;
	
	public void start() throws IOException {
		serverSocket = new ServerSocket(port);
		EcpMqttServerThread serverThread = new EcpMqttServerThread(serverSocket);
		serverThread.start();
	    System.out.println("MQTT Broker started on port " + port);
	}
	
	public void close() throws IOException {
		serverSocket.close();
	}
}
