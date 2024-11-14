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
package com.sandy.ecp.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

import com.sandy.ecp.mqtt.callback.EcpMqttCallback;
import com.sandy.ecp.mqtt.config.EcpConfiguration;

/**
 * MQTT（Message Queuing Telemetry Transport，消息队列遥测传输协议），是一种基于发布/订阅（publish/subscribe）模式的“轻量级”通讯协议，
 * 该协议构建于 TCP/IP 协议上，由 IBM 在 1999 年发布，并于 2013 年发布为开放标准。MQTT 协议专注于在低带宽、不稳定或高延迟的网络环境中，可靠地传输小型数据包
 * 
 * @author Mr.Sandy
 * @since v1.0.0 2024/11/09 16:26:23
 */
@Component
public class EcpMqttConnect {

    private final String clientId = "DC" + (int) (Math.random() * 100000000);
    
    private EcpConfiguration configuration;
    
    private MqttClient mqttClient;

    /**
     * 客户端connect连接mqtt服务器
     *
     * @param userName     用户名
     * @param passWord     密码
     * @param mqttCallback 回调函数
     **/
    public void setMqttClient(String userName, String passWord, EcpMqttCallback mqttCallback) throws MqttException {
        MqttConnectOptions options = mqttConnectOptions(userName, passWord);
        if (mqttCallback == null) {
            mqttClient.setCallback(new EcpMqttCallback());
        } else {
            mqttClient.setCallback(mqttCallback);
        }
        mqttClient.connect(options);
    }

    /**
     * MQTT连接参数设置
     */
    private MqttConnectOptions mqttConnectOptions(String userName, String passWord) throws MqttException {
        mqttClient = new MqttClient(configuration.getHost(), clientId, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        options.setConnectionTimeout(10);///默认：30
        options.setAutomaticReconnect(true);//默认：false
        options.setCleanSession(false);//默认：true
        //options.setKeepAliveInterval(20);//默认：60
        return options;
    }

    /**
     * 关闭MQTT连接
     */
    public void close() throws MqttException {
        mqttClient.disconnect();
        mqttClient.close();
    }

    /**
     * 向某个主题发布消息 默认qos：1
     *
     * @param topic:发布的主题
     * @param msg：发布的消息
     */
    public void pub(String topic, String msg) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage();
        //mqttMessage.setQos(2);
        mqttMessage.setPayload(msg.getBytes());
        MqttTopic mqttTopic = mqttClient.getTopic(topic);
        MqttDeliveryToken token = mqttTopic.publish(mqttMessage);
        token.waitForCompletion();
    }

    /**
     * 向某个主题发布消息
     *
     * @param topic: 发布的主题
     * @param msg:   发布的消息
     * @param qos:   消息质量    Qos：0、1、2
     */
    public void pub(String topic, String msg, int qos) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(qos);
        mqttMessage.setPayload(msg.getBytes());
        MqttTopic mqttTopic = mqttClient.getTopic(topic);
        MqttDeliveryToken token = mqttTopic.publish(mqttMessage);
        token.waitForCompletion();
    }

    /**
     * 订阅某一个主题 ，此方法默认的的Qos等级为：1
     *
     * @param topic 主题
     */
    public void sub(String topic) throws MqttException {
        mqttClient.subscribe(topic);
    }

    /**
     * 订阅某一个主题，可携带Qos
     *
     * @param topic 所要订阅的主题
     * @param qos   消息质量：0、1、2
     */
    public void sub(String topic, int qos) throws MqttException {
        mqttClient.subscribe(topic, qos);
    }
    
    public void setConfiguration(EcpConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
     * main函数自己测试用
     */
    public static void main(String[] args) throws MqttException {
        EcpMqttConnect mqttConnect = new EcpMqttConnect();
        
        EcpConfiguration configuration = new EcpConfiguration();
        configuration.setUserName("admin");
        configuration.setPassword("admin");
        mqttConnect.setConfiguration(configuration);
        
        mqttConnect.setMqttClient(configuration.getUserName(), configuration.getPassword(), new EcpMqttCallback());
        mqttConnect.sub("com/iot/init");
        mqttConnect.pub("com/iot/init", "Mr.Qu" + (int) (Math.random() * 100000000));
    }
}

