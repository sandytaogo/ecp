/*
 * Copyright 2024-2040 the original author or authors.
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
package com.sandy.ecp.recognition.face;

import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;

/**
 * 
 */
public class FaceDetector {
	
    private CascadeClassifier faceDetector;
    
    public FaceDetector(String modelPath) {
        // 加载预训练的人脸检测模型
        this.faceDetector = new CascadeClassifier(modelPath);
    }
    
    public List<Rect> detectFaces(Mat image) {
        MatOfRect faceDetections = new MatOfRect();
        // 执行人脸检测（缩放因子1.1，最小邻居数5）
        faceDetector.detectMultiScale(image, faceDetections, 1.1, 5);
        List<Rect> faces = faceDetections.toList();
        System.out.println("检测到 " + faces.size() + " 张人脸");
        return faces;
    }
    
}
