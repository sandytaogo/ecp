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
package com.sandy.ecp.recognition.enums;
/**
 * 
 * @author sandy
 * @date 2020-05-13 10:10:09
 */
public enum Direction {

    VERTICAL("VERTICAL","垂直"), 
    HORIZONTAL("HORIZONTAL","水平"), 
    UNKNOWN("UNKNOWN","未知");

    public final String code;
    public final String desc;

    Direction(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(String code) {
        Direction[] enums = values();
        for (Direction type : enums) {
            if (type.code().equals(code)) {
                return type.desc();
            }
        }
        return null;
    }

    public static String getCode(String desc) {
        Direction[] enums = values();
        for (Direction type : enums) {
            if (type.desc().equals(desc)) {
                return type.code();
            }
        }
        return null;
    }
    
    
    public String code() {
        return this.code;
    }

    public String desc() {
        return this.desc;
    }
    
}
