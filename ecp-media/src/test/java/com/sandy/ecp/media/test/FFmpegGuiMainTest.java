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
package com.sandy.ecp.media.test;

import java.io.IOException;

import javax.swing.JFrame;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameGrabber;

/**
 * @author Sandy
 * @see https://opencv.org/releases/
 */
public class FFmpegGuiMainTest {
	

    public static void main(String[] args) throws IOException, InterruptedException {
    	
    	OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);//新建opencv抓取器，一般的电脑和移动端设备中摄像头默认序号是0，不排除其他情况
//        Socket socket = new Socket("127.0.0.1", 3000);
//        InputStream is=socket.getInputStream();
//        FFmpegFrameGrabber grabber=new FFmpegFrameGrabber(is);
    	CanvasFrame canvas = new CanvasFrame("摄像头预览");//新建一个预览窗口
        canvas.setCanvasSize(600,600);
        canvas.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
        	grabber.start();//开始获取摄像头数据
            //窗口是否关闭
            while(canvas.isDisplayable()){
                /*获取摄像头图像并在窗口中显示,这里Frame frame=grabber.grab()得到是解码后的视频图像*/
                canvas.showImage(grabber.grab());
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	System.out.println("获取摄像头失败");
			// TODO: handle exception
		} finally {
			grabber.close();//停止抓取
		}
    }
}
