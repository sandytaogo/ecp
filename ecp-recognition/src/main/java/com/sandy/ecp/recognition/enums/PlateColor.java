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
 * 车牌颜色
 * @author Sandy
 * @date 2020-05-08 12:38
 */
public enum PlateColor {

    BLUE("BLUE","蓝牌", 100, 130), 
    GREEN("GREEN","绿牌", 38, 100), 
    YELLOW("YELLOW","黄牌", 15, 40),
    UNKNOWN("UNKNOWN","未知", 0, 0);

    public final String code;
    public final String desc;

    // opencv颜色识别的HSV中各个颜色所对应的H的范围： Orange 0-22 Yellow 22- 38 Green 38-75 Blue 75-130
    public final int minH;
    public final int maxH;

    PlateColor(String code, String desc, int minH, int maxH) {
        this.code = code;
        this.desc = desc;
        this.minH = minH;
        this.maxH = maxH;
    }

    public static String getDesc(String code) {
        PlateColor[] enums = values();
        for (PlateColor type : enums) {
            if (type.code().equals(code)) {
                return type.desc();
            }
        }
        return null;
    }

    public static String getCode(String desc) {
        PlateColor[] enums = values();
        for (PlateColor type : enums) {
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
