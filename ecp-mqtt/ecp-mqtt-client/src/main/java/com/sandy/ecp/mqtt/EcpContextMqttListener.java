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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.sandy.ecp.mqtt.callback.EcpMqttCallback;
import com.sandy.ecp.mqtt.config.EcpConfiguration;

/**
 * 项目启动 监听主题
 *
 * @author Mr.sandy
 * @since 2024/11/09 16:26:23
 */
@Component
public class EcpContextMqttListener implements ApplicationListener<ContextRefreshedEvent> {
	
	private final static Log log = LogFactory.getLog(EcpMqttCallback.class);
	
	protected EcpConfiguration configuration;

    private final EcpMqttConnect server;

    @Autowired
    public EcpContextMqttListener(EcpMqttConnect server) {
        this.server = server;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            server.setMqttClient("admin", "public", new EcpMqttCallback());
            server.sub("com/iot/init");
        } catch (MqttException e) {
            log.error(e.getMessage(), e);
        }
    }
}



