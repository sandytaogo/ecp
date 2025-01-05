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
package com.sandy.ecp.mqtt.callback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * 常规MQTT回调函数
 *
 * @author Mr.sandy
 * @since 2024/11/09 16:26:23
 */
public class EcpMqttCallback implements MqttCallback {

	private final static Log log = LogFactory.getLog(EcpMqttCallback.class);
	
	/**
	 * MQTT 断开连接会执行此方法
	 */
	@Override
	public void connectionLost(Throwable throwable) {
        log.info(String.format("断开了MQTT连接 ：%s", throwable.getMessage()));
        log.error(throwable.getMessage(), throwable);
	}


	/**
	 * publish发布成功后会执行到这里
	 */
	@Override
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("发布消息成功");
	}

	/**
	 * subscribe订阅后得到的消息会执行到这里
	 */
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO 此处可以将订阅得到的消息进行业务处理、数据存储
        log.info(String.format("收到来自 " + topic + " 的消息：%s", new String(message.getPayload())));
	}
}
