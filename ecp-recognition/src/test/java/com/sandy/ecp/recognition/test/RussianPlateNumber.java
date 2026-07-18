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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;

import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.FFmpegLogCallback;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.bytedeco.javacv.FrameGrabber.Exception;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Point;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class RussianPlateNumber {

	private final static Logger log = LoggerFactory.getLogger(CameraFaceDetectionExample.class);
	 /**
    * 输出帧率
    */
   private static final double frameRate = 30;
   /**
    * 摄像头视频的宽
    */
   private static final int cameraImageWidth = 1280;

   /**
    * 摄像头视频的高
    */
   private static final int cameraImageHeight = 720;

   /**
    * 转换器
    */
   private static final OpenCVFrameConverter.ToIplImage openCVConverter = new OpenCVFrameConverter.ToIplImage();

   public static void main(String[] args) throws IOException {
       long startTime = System.currentTimeMillis();
       Loader.load(org.bytedeco.opencv.opencv_java.class);
       // 设置ffmepg日志级别
       avutil.av_log_set_level(avutil.AV_LOG_INFO);
       FFmpegLogCallback.set();
       //获取本地摄像头，一般是0
       OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
       // 摄像头有可能有多个分辨率，这里指定
       // 可以指定宽高，也可以不指定反而调用grabber.getImageWidth去获取，
       grabber.setImageWidth(1280);
       grabber.setImageHeight(720);

       // 开启抓取器
       grabber.start();

       //创建一个Frame窗口
       CanvasFrame previewCanvas = new CanvasFrame("摄像头预览", CanvasFrame.getDefaultGamma() / grabber.getGamma());
       previewCanvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       previewCanvas.setAlwaysOnTop(true);

       log.info("初始化完成，耗时[{}]毫秒，帧率[{}]，图像宽度[{}]，图像高度[{}]",
               System.currentTimeMillis()-startTime,
               frameRate,
               cameraImageWidth,
               cameraImageHeight);
       // 添加水印时用到的时间工具
       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       // 水印在图片上的位置
       org.bytedeco.opencv.opencv_core.Point point = new org.bytedeco.opencv.opencv_core.Point(15, 35);
       org.bytedeco.javacv.Frame captureFrame;
       Resource resource = new ClassPathResource("model/haarcascade_russian_plate_number.xml");
       // 下载模型文件
       @SuppressWarnings("resource")
       CascadeClassifier cascadeClassifier = new CascadeClassifier(resource.getFile().getPath());
       // 遍历每一帧
       while ((captureFrame = grabber.grab()) != null) {
           // 将帧对象转为IplImage对象
           IplImage img = openCVConverter.convert(captureFrame);
           // 镜像翻转
           //cvFlip(img, img, 1);
           // IplImage转mat
           Mat mat1 = new Mat(img);
           // 在图片上添加水印，水印内容是当前时间，位置是左上角
           opencv_imgproc.putText(mat1,simpleDateFormat.format(new Date()),point, opencv_imgproc.CV_FONT_VECTOR0, 1,
               new Scalar(0, 0, 255, 0),
               1,
               0,
               false);

           // 将图像转为灰度图像
           Mat grayMat = new Mat();
           // 当前图片转为灰度图片
           //opencv_imgproc.cvtColor(mat1, grayMat,  opencv_core.COLOR_BGR2GRAY);
           opencv_imgproc.cvtColor(mat1, grayMat,  opencv_imgproc.COLOR_BGR2GRAY);
           // 存放检测结果的容器
           RectVector objects = new RectVector();
           // 开始检测
           if (cascadeClassifier.empty()) {
               System.out.println("Failed to load cascade classifier.");
           }
           cascadeClassifier.detectMultiScale(grayMat, objects);
           Mat tagetMat = new Mat();
           // 如果有检测结果，就根据结果的数据构造矩形框，画在原图上
           for (long i = 0; i < objects.size(); i++) {
               Rect r = objects.get(i);
               int x = r.x(), y = r.y(), w = r.width(), h = r.height();
               opencv_imgproc.rectangle(mat1, new Point(x, y-30), new Point(x + w, y), Scalar.GREEN, -1, opencv_imgproc.LINE_AA, 0);
               opencv_imgproc.putText(mat1,"Car Number:",new Point(x, y-15), opencv_imgproc.CV_FONT_VECTOR0, 0.8,
                       new Scalar(255, 255, 255, 0),
                       1,
                       0,
                       false);
               opencv_imgproc.rectangle(mat1, new Point(x, y), new Point(x + w, y + h), Scalar.RED, 1, opencv_imgproc.LINE_AA, 0);
               opencv_imgproc.threshold(mat1, tagetMat, 127, 255, opencv_imgproc.THRESH_BINARY);
           }
           // 释放检测结果资源
           objects.close();
           // 将标注过的图片转为帧，返回
           org.bytedeco.javacv.Frame convert = openCVConverter.convert(mat1);
           // 显示图像
           previewCanvas.showImage(convert);

       }
       log.info("输出结束");
       if (null!= previewCanvas) {
           previewCanvas.dispose();
       }
       if (null!=grabber) {
           try {
               grabber.close();
           } catch (Exception exception) {
               log.error("close grabber error", exception);
           }
       }
   }
	
}
