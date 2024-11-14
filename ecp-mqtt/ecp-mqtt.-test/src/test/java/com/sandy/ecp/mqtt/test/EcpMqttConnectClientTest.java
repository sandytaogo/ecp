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
package com.sandy.ecp.mqtt.test;

import java.io.IOException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sandy.ecp.mqtt.callback.EcpMqttCallback;
import com.sandy.ecp.mqtt.config.EcpConfiguration;
import com.sandy.ecp.mqtt.exception.EcpMqttException;
import com.sandy.ecp.mqtt.server.EcpMqttServer;

/**
 * 物联网 lot 客户端链接单元测试
 * 
 * @author Sandy
 * @since v1.0.0 2024/11/09 16:26:23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public class EcpMqttConnectClientTest {
	
	private EcpConfiguration configuration = new EcpConfiguration();
	
	private EcpMqttServer ecpMqttServer = null;
	
	@Before
	public void initBefore() {
		configuration.setUserName("admin");
		configuration.setPassword("admin");
		ecpMqttServer = new EcpMqttServer();
		try {
			ecpMqttServer.start();
			Thread.sleep(3000);
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
//	@Ignore
	public void testSub() throws MqttException, InterruptedException {
		String clientId = "client-testSub2";
		MqttConnectOptions options = new MqttConnectOptions();
		options.setServerURIs(new String[]{configuration.getHost()});
		options.setUserName(configuration.getUserName());
		options.setPassword(configuration.getPassword().toCharArray());
		options.setConnectionTimeout(10);
		options.setAutomaticReconnect(true);
		options.setCleanSession(false);
	 
		MqttClient mqttClient = new MqttClient(options.getServerURIs()[0], clientId, new MemoryPersistence());
		mqttClient.connect(options);
		mqttClient.setCallback(new EcpMqttCallback() {
			@Override
			public void connectionLost(Throwable cause) {
	 
			}
			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				System.out.println(topic + "收到messageId=" + message.getId() + ",payload=" + new String(message.getPayload()));
				String msg = new String(message.getPayload());
				if ("1".equals(msg)) {
					throw new EcpMqttException("模拟消息处理失败");
				}
			}
			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
			}
		});
//		mqttClient.subscribe(new String[]{"topic/testPublish"}, new int[] {1});
	}
	
	
	@After
	public void testAgter() throws IOException {
		
		ecpMqttServer.close();
	}
}
