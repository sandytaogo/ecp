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
package com.sandy.ecp.recognition.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 用于生成ID的工具类
 * @author sandy
 * @date 2024-10-05 11:16:23
 */
public class GenerateId {
    
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmssSSS");
    
    public static synchronized Long getId() {
        try {
            Thread.sleep(1);
        } catch (Exception e) {
        	//ignore
        }
        return System.currentTimeMillis();
    }

    public static synchronized String getStrId() {
    	
        return formatter.format(LocalDateTime.now());
    }
}
