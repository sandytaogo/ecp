/*
 * Copyright 2024-2030 the original author or authors.
 *
 * Licensed under the company, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.company.com/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sandy.ecp.web.socket.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.Session;

/**
 * 企业云平台web socket 通信链接管理. 
 * @author Sandy
 * @since 1.0.0 2025-04-04 13:13:13
 */
public class WebSocketManager {
	
	/**
	 * web socket 通信连接管理. 
	 */
	private static Map<String, Session> sessionManger = new ConcurrentHashMap<String, Session>();
	
	public static Session get(String id) {
		return sessionManger.get(id);
	}
	
	public static Session put(String id, Session session) {
		return sessionManger.put(id, session);
	}
	
	public static Session remove(String key) {
		return sessionManger.remove(key);
	}
	
	public List<String> getPageUser(int page, int size) {
		Set<String> keys = sessionManger.keySet();
		String [] userIds = keys.toArray(new String[keys.size()]);
		List<String> list = new ArrayList<String>(size);
		int index = page - 1;
		for (int i = 0; i < size; i++) {
			if (index + i < userIds.length) {
				list.add(userIds[index + i]);
			} else {
				break;
			}
		}
		return list;
	}
	
	public int getTotal() {
		return sessionManger.size();
	}
}
