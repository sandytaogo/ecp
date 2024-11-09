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

import java.time.Duration;
import java.time.Instant;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.util.StringUtils;

import com.sandy.ecp.recognition.config.Constant;

/**
 * 人脸检测、识别工具类
 * @author sandy
 * @date 2024-05-28 15:11:34
 */
public class FaceUtil {
	
    private static final String TEMP_PATH = "D:/FaceDetect/temp/";

    private CascadeClassifier faceDetector;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * 构造函数，加载默认模型文件
     */
    public FaceUtil(){
        faceDetector = new CascadeClassifier(Constant.DEFAULT_FACE_MODEL_PATH);
    }

    /**
     * 加载自定义模型文件
     * @param modelPath
     */
    public void loadModel(String modelPath){
        if(!StringUtils.isEmpty(modelPath)) {
            faceDetector = new CascadeClassifier(modelPath);
        }
    }

    /**
     * 检测图片中的人脸
     * @param inMat
     * @param targetPath
     * @return
     */
    public MatOfRect detectFace(Mat inMat, String targetPath) {
        if(null == faceDetector || faceDetector.empty()) {
            System.out.println("加载模型文件失败: " + Constant.DEFAULT_FACE_MODEL_PATH);
            return null;
        }
        Boolean debug = false;
        Mat grey = new Mat();
        ImageUtil.gray(inMat, grey, debug, TEMP_PATH);

        Mat gsBlur = new Mat();
        ImageUtil.gaussianBlur(grey, gsBlur, debug, TEMP_PATH);

        MatOfRect faceDetections = new MatOfRect(); // 识别结果存储对象 // Rect矩形类
        faceDetector.detectMultiScale(gsBlur, faceDetections); // 识别人脸

        // 在识别到的人脸部位，描框
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(inMat, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
            Imgcodecs.imwrite(targetPath, inMat);
        }
        return faceDetections;
    }

    /**
     * 识别图片中的人脸是谁
     * 未完待续
     * @return
     */
    public Object recogniseFace() {
        return null;
    }
    
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

