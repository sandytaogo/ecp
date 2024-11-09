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
 * @author Sandy
 * @date 2020-10-13 14:50
 */
public enum PlateHSV {

    BLUE(105, 125, 5, 35), 
    GREEN(60, 100, 5, 35), 
    YELLOW(15, 35, 5, 35), 
    UNKNOWN(0, 0, 0, 0);

    public final int minH;
    public final int maxH;

    public final int equalizeMinH;
    public final int equalizeMaxH;

    PlateHSV(int minH, int maxH, int equalizeMinH, int equalizeMaxH) {
        this.minH = minH;
        this.maxH = maxH;
        this.equalizeMinH = equalizeMinH;
        this.equalizeMaxH = equalizeMaxH;
    }

}
