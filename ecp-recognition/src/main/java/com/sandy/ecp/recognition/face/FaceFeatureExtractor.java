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

import org.bytedeco.opencv.opencv_core.MatVector;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgproc.Imgproc;

/**
 * @see https://cloud.baidu.com/article/3746097
 */
public class FaceFeatureExtractor {
	
    private FaceRecognizer faceRecognizer;
    
    public FaceFeatureExtractor(String modelPath) {
        // 使用LBPH算法（其他可选：EigenFace, FisherFace）
        this.faceRecognizer = LBPHFaceRecognizer.create();
        if (modelPath != null) {
            this.faceRecognizer.read(modelPath);
        }
    }
    
    public Mat extractFeatures(Mat faceImage) {
        // 预处理：灰度化、直方图均衡化
        Mat gray = new Mat();
        Imgproc.cvtColor(faceImage, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(gray, gray);
        // 特征提取（实际由recognizer内部完成）
        Mat features = new Mat();
        // 此处简化，实际LBPH不需要显式提取特征
        return gray; // 实际应用中应直接使用recognizer的predict方法
    }
    
    public void trainModel(List<Mat> images, List<Integer> labels) {
        // 准备训练数据
        //MatVector imagesMat = new MatVector(images.toArray(new Mat[0]));
    	MatVector imagesMat = new MatVector(labels.size());
        Mat labelsMat = new Mat(labels.size(), 1, CvType.CV_32SC1);
        for (int i = 0; i < labels.size(); i++) {
            labelsMat.put(i, 0, labels.get(i));
        }
        // 训练模型（迭代次数100，阈值Double.MAX_VALUE）
        faceRecognizer.train(images, labelsMat);
        faceRecognizer.save("face_model.yml");
    }
}
