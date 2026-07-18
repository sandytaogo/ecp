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
package com.sandy.ecp.recognition.test;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.bytedeco.opencv.global.opencv_imgproc.*;

/**
 * @author sandy
 * @since 1.0.0
 */
public class FaceRecognitionDemo {

    public static void main(String[] args) throws IOException {
        faceDetection("G:\\bus.jpg");
    }

    /**
     * 人脸检测
     *
     * @param filePath 图片路径
     */
    public static void faceDetection(String filePath) throws IOException {
    	 Resource resource = new ClassPathResource("model/lbpcascade_frontalface.xml");
        // 读取opencv人脸检测器
        CascadeClassifier cascade = new CascadeClassifier(resource.getFile().getPath());
        File file=new File(filePath);
        BufferedImage image = ImageIO.read(file);
        Java2DFrameConverter imageConverter = new Java2DFrameConverter();
        Frame frame = imageConverter.convert(image);
        //类型转换
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
        Mat original = converter.convertToMat(frame);
        //存放灰度图
        Mat grayImg = new Mat();
        //模式设置成ImageMode.Gray下不需要再做灰度 摄像头获取的是彩色图像，所以先灰度化下
        cvtColor(original, grayImg, COLOR_BGRA2GRAY);
        // 均衡化直方图
        equalizeHist(grayImg, grayImg);
        // 检测到的人脸
        RectVector faces = new RectVector();
        //多人脸检测
        cascade.detectMultiScale(grayImg, faces);
        // 遍历人脸
        for (int i = 0; i < faces.size(); i++) {
            Rect face_i = faces.get(i);
            //绘制人脸矩形区域，scalar色彩顺序：BGR(蓝绿红)
            rectangle(original, face_i, new Scalar(0, 255, 0, 1));
            int pos_x = Math.max(face_i.tl().x() - 10, 0);
            int pos_y = Math.max(face_i.tl().y() - 10, 0);
            // 在人脸矩形上方绘制提示文字（中文会乱码）
            putText(original, "people face", new Point(pos_x, pos_y), FONT_HERSHEY_COMPLEX, 1.0, new Scalar(0, 0, 255, 2.0));
        }
        frame = converter.convert(original);
        image = imageConverter.convert(frame);
        String fileName=file.getName();
        String extension=fileName.substring(fileName.lastIndexOf(".")+1);
        String newFileName=fileName.substring(0,fileName.lastIndexOf("."))+"_result."+extension;
        ImageIO.write(image, extension, new File(file.getParent() + File.separator + newFileName));
    }
}

