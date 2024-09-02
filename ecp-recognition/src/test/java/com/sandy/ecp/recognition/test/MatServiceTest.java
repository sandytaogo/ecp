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
package com.sandy.ecp.recognition.test;

import java.time.Duration;
import java.time.Instant;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import com.sandy.ecp.recognition.Constant;
import com.sandy.ecp.recognition.util.FaceUtil;

public class MatServiceTest {

	 /**
     * 测试当前工具类
     * @param args
     */
    public static void main(String[] args) {
        Instant start = Instant.now();

        FaceUtil fu = new FaceUtil();
        Mat inMat = Imgcodecs.imread("D:/FaceDetect/train/huge/huge.png");
        fu.detectFace(inMat, Constant.DEFAULT_FACE_TEMP_DIR + "result.jpg");

        Instant end = Instant.now();
        System.err.println("总耗时：" + Duration.between(start, end).toMillis());
    }
}
