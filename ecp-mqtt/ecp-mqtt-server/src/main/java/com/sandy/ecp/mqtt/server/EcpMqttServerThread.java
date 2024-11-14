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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 */
public class EcpMqttServerThread extends Thread {
	
	private ServerSocket serverSocket;
	
	public EcpMqttServerThread(ServerSocket serverSocket) {
		super();
		this.serverSocket = serverSocket;
		this.setDaemon(true);
		this.setName("ECP-MQTT-SERVER-Thread");
	}

	@Override
	public void run() {
		
		Socket clientSocket = null;
		
		while (true) {
			try {
				clientSocket = serverSocket.accept();
				System.out.println("New client connected: " + clientSocket.getInetAddress());
	            InputStream is = clientSocket.getInputStream();
	            OutputStream os = clientSocket.getOutputStream();
	 
	            byte[] buffer = new byte[1024];
	            int read = is.read(buffer);
	 
	            byte[] connectAck = { 0x20, 0x02, 0x00, 0x00 };
	            os.write(connectAck);
	 
	            while (true) {
	                read = is.read(buffer);
	                if (read == -1) {
	                    break;
	                }
	                int messageType = (buffer[0] >> 4) & 0x0f;
	                if (messageType == 0x03) { // PUBLISH message
	                    String topic = readMqttString(buffer, 2);
	                    byte[] messageData = new byte[read - topic.length() - 4];
	                    System.arraycopy(buffer, topic.length() + 4, messageData, 0, messageData.length);
	                    int qosLevel = (buffer[0] >> 1) & 0x03;
	                    boolean retained = ((buffer[0] >> 0) & 0x01) == 1;
	                    System.out.println("Received message on topic " + topic + ": " + new String(messageData));
//	                    forwardMessageToSubscribers(topic, messageData, qosLevel, retained);
	                }
	            }
	            
	            System.out.println("Client disconnected");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					clientSocket.close();
				} catch (IOException e) {
					// ignore
				}
			}
        }
	}

    private static String readMqttString(byte[] buffer, int offset) {
        int stringLength = ((buffer[offset] & 0xff) << 8) | (buffer[offset + 1] & 0xff);
        byte[] stringData = new byte[stringLength];
        System.arraycopy(buffer, offset + 2, stringData, 0, stringLength);
        return new String(stringData);
    }
}
