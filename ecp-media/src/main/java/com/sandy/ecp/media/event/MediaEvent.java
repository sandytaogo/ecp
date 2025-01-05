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
package com.sandy.ecp.media.event;

import java.util.EventObject;

/**
 * 多媒体转换事件触发执行接口. 
 * @author Sandy
 * @since 1.0.0 2024-07-06 12:12:12
 */
public class MediaEvent extends EventObject {
	
	private static final long serialVersionUID = 6245472945298489041L;
	
	/** System time when the event happened. */
    private final long timestamp;

    /**
     * Constructs a prototypical Event.
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public MediaEvent(Object source) {
        super(source);
        this.timestamp = System.currentTimeMillis();
    }
    
    public long getTimestamp() {
		return timestamp;
	}
}
