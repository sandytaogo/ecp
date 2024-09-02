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

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
 
/**
 * OpenCV - Open Computer Vision Library 图像识别工具类.
 * @author sandy
 * @date 2024-05-28 15:11
 */
public  class OpencvUtil {
	
    private static final int BLACK = 0;
    private static final int WHITE = 255;
    /**
     * 灰化处理
     * @return
     */
    public static Mat gray (Mat mat){
        Mat gray = new Mat();
        Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY, 1);
        return gray;
    }
  
    /**
     * 二值化处理
     * @return
     */
    public static Mat binary (Mat mat){
        Mat binary = new Mat();
        Imgproc.adaptiveThreshold(mat, binary, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 25, 10);
        return binary;
    }
  
    /**
     * 模糊处理
     * @param mat
     * @return
     */
    public static Mat blur (Mat mat) {
        Mat blur = new Mat();
        Imgproc.blur(mat,blur,new Size(5,5));
        return blur;
    }
  
    /**
     *膨胀
     * @param mat
     * @return
     */
    public static Mat dilate (Mat mat,int size){
        Mat dilate=new Mat();
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(size,size));
        //膨胀
        Imgproc.dilate(mat, dilate, element, new Point(-1, -1), 1);
        return dilate;
    }
  
    /**
     * 腐蚀
     * @param mat
     * @return
     */
    public static Mat erode (Mat mat,int size){
        Mat erode=new Mat();
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(size,size));
        //腐蚀
        Imgproc.erode(mat, erode, element, new Point(-1, -1), 1);
        return erode;
    }
  
    /**
     * 边缘检测
     * @param mat
     * @return
     */
    public static Mat carry(Mat mat){
        Mat dst=new Mat();
        //高斯平滑滤波器卷积降噪
        Imgproc.GaussianBlur(mat, dst, new Size(3,3), 0);
        //边缘检测
        Imgproc.Canny(mat, dst, 50, 150);
        return dst;
    }
  
    /**
     * 轮廓检测
     * @param mat
     * @return
     */
    public static List<MatOfPoint> findContours(Mat mat){
        List<MatOfPoint> contours=new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mat, contours, hierarchy, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        return contours;
    }
  
    /**
     * 清除小面积轮廓
     * @param mat
     * @param size
     * @return
     */
    public static Mat drawContours(Mat mat,int size){
        List<MatOfPoint> cardContours=OpencvUtil.findContours(mat);
        for (int i = 0; i < cardContours.size(); i++)
        {
            double area=OpencvUtil.area(cardContours.get(i));
            if(area<size){
                Imgproc.drawContours(mat, cardContours, i, new Scalar( 0, 0, 0),-1 );
            }
        }
        return mat;
    }
  
    /**
     * 人脸识别
     * @param mat
     * @return
     */
    public static Mat face(Mat mat){
        CascadeClassifier faceDetector = new CascadeClassifier(
                System.getProperty("user.dir")+"\\opencv\\haarcascades\\haarcascade_frontalface_alt.xml");
        // 在图片中检测人脸
        MatOfRect faceDetections = new MatOfRect();
        //指定人脸识别的最大和最小像素范围
        Size minSize = new Size(100, 100);
        Size maxSize = new Size(500, 500);
        //参数设置为scaleFactor=1.1f, minNeighbors=4, flags=0 以此来增加识别人脸的正确率
        faceDetector.detectMultiScale(mat, faceDetections, 1.1f, 4, 0, minSize, maxSize);
        Rect[] rects = faceDetections.toArray();
        if(rects != null && rects.length == 1){
            // 在每一个识别出来的人脸周围画出一个方框
            @SuppressWarnings("unused")
			Rect rect = rects[0];
            return mat;
        }else{
            return null;
        }
    }
  
    /**
     * 循环进行人脸识别
     * */
    public static Mat faceLoop(Mat src){
        Mat face=new Mat();
        //默认人脸识别失败时图像旋转90度
        int k=90;
        while (k>0){
            for(int i=0;i<360/k;i++){
                //人脸识别
                face= OpencvUtil.face(src);
                if(face==null){
                    src = rotate3(src,k);
                }else{
                    break;
                }
            }
            if(face!=null){
                break;
            }else{
                k=k-30;
            }
        }
        return src;
    }
  
    /**
     * 剪切身份证区域
     * @param mat
     */
    public static Mat houghLinesP(Mat begin,Mat mat){
        //灰度
        mat=OpencvUtil.gray(mat);
        //二值化
        mat=OpencvUtil.binary(mat);
        //腐蚀
        mat=OpencvUtil.erode(mat,5);
        //边缘检测
        mat=OpencvUtil.carry(mat);
        //降噪
        mat=OpencvUtil.navieRemoveNoise(mat,1);
        //膨胀
        mat=OpencvUtil.dilate(mat,3);
        //轮廓检测,清除小的轮廓部分
        List<MatOfPoint> contours=OpencvUtil.findContours(mat);
        for(int i=0;i<contours.size();i++){
            double area=OpencvUtil.area(contours.get(i));
            if(area<5000){
                Imgproc.drawContours(mat, contours, i, new Scalar( 0, 0, 0), -1);
            }
        }
        Mat storage = new Mat();
        Imgproc.HoughLinesP(mat, storage, 1, Math.PI / 180, 10, 0, 10);
        double[] maxLine = new double[]{0,0,0,0};
        //获取最长的直线
        for (int x = 0; x < storage.rows(); x++)
        {
            double[] vec = storage.get(x, 0);
            double x1 = vec[0], y1 = vec[1], x2 = vec[2], y2 = vec[3];
            double newLength = Math.sqrt(Math.abs((x1 - x2)* (x1 - x2)+(y1 - y2)* (y1 - y2)));
            double oldLength = Math.sqrt(Math.abs((maxLine[0] - maxLine[2])* (maxLine[0] - maxLine[2])+(maxLine[1] - maxLine[3])* (maxLine[1] - maxLine[3])));
            if(newLength>oldLength){
                maxLine = vec;
            }
        }
        //计算最长线的角度
        double angle = getAngle(maxLine[0],maxLine[1],maxLine[2],maxLine[3]);
        //旋转角度
        mat = rotate3( mat,angle);
        begin = rotate3( begin,angle);
  
        Imgproc.HoughLinesP(mat, storage, 1, Math.PI / 180, 10, 10, 10);
        List<double[]> lines=new ArrayList<>();
        //在mat上划线
        for (int x = 0; x < storage.rows(); x++)
        {
            double[] vec = storage.get(x, 0);
            double x1 = vec[0], y1 = vec[1], x2 = vec[2], y2 = vec[3];
            Point start = new Point(x1, y1);
            Point end = new Point(x2, y2);
            //获取与图像x边缘近似平行的直线
            if(Math.abs(start.y-end.y)<5){
                if(Math.abs(x2-x1)>20){
                    lines.add(vec);
                }
            }
            //获取与图像y边缘近似平行的直线
            if(Math.abs(start.x-end.x)<5){
                if(Math.abs(y2-y1)>20){
                    lines.add(vec);
                }
            }
        }
        //获取最大的和最小的X,Y坐标
        double maxX=0.0,minX=10000,minY=10000,maxY=0.0;
        for(int i=0;i<lines.size();i++){
            double[] vec = lines.get(i);
            double x1 = vec[0], y1 = vec[1], x2 = vec[2], y2 = vec[3];
            maxX=maxX>x1?maxX:x1;
            maxX=maxX>x2?maxX:x2;
            minX=minX>x1?x1:minX;
            minX=minX>x2?x2:minX;
            maxY=maxY>y1?maxY:y1;
            maxY=maxY>y2?maxY:y2;
            minY=minY>y1?y1:minY;
            minY=minY>y2?y2:minY;
        }
        if(maxX<mat.cols()&&minX>0&&maxY<mat.rows()&&minY>0){
            List<Point> list=new ArrayList<>();
            Point point1=new Point(minX+10,minY+10);
            Point point2=new Point(minX+10,maxY-10);
            Point point3=new Point(maxX-10,minY+10);
            Point point4=new Point(maxX-10,maxY-10);
            list.add(point1);
            list.add(point2);
            list.add(point3);
            list.add(point4);
            mat=shear(begin,list);
        }else{
            mat=begin;
        }
        return mat;
    }
  
    /**
     * 计算角度
     * @param px1
     * @param py1
     * @param px2
     * @param py2
     * @return
     */
    public static double  getAngle(double px1, double py1, double px2, double py2) {
        //两点的x、y值
        double x = px2-px1;
        double y = py2-py1;
        double hypotenuse = Math.sqrt(Math.pow(x, 2)+Math.pow(y, 2));
        //斜边长度
        double cos = x/hypotenuse;
        double radian = Math.acos(cos);
        //求出弧度
        double angle = 180/(Math.PI/radian);
        //用弧度算出角度
        if (y<0) {
            angle = -angle;
        } else if ((y == 0) && (x<0)) {
            angle = 180;
        }
        while (angle<0){
            angle = angle +90;
        }
        return angle;
    }
  
    /**
     * 累计概率hough变换直线检测
     * @param mat
     */
    public static Mat houghLines(Mat mat){
        Mat storage = new Mat();
        Imgproc.HoughLines(mat, storage, 1, Math.PI / 180, 50, 0, 0, 0, 1);
        for (int x = 0; x < storage.rows(); x++) {
            double[] vec = storage.get(x, 0);
  
            double rho = vec[0];
            double theta = vec[1];
  
            Point pt1 = new Point();
            Point pt2 = new Point();
  
            double a = Math.cos(theta);
            double b = Math.sin(theta);
  
            double x0 = a * rho;
            double y0 = b * rho;
  
            pt1.x = Math.round(x0 + 1000 * (-b));
            pt1.y = Math.round(y0 + 1000 * (a));
            pt2.x = Math.round(x0 - 1000 * (-b));
            pt2.y = Math.round(y0 - 1000 * (a));
  
            if (theta >= 0)
            {
                Imgproc.line(mat, pt1, pt2, new Scalar(255), 3);
            }
        }
        return mat;
    }
  
  
    /**
     * 根据四点坐标截取模板图片
     * @param mat
     * @param pointList
     * @return
     */
    public static Mat shear (Mat mat,List<Point> pointList){
        int x=minX(pointList);
        int y=minY(pointList);
        int xl=xLength(pointList)>mat.cols()-x?mat.cols()-x:xLength(pointList);
        int yl=yLength(pointList)>mat.rows()-y?mat.rows()-y:yLength(pointList);
        Rect re=new Rect(x,y,xl,yl);
        return new Mat(mat,re);
    }
  
  
    /**
     * 图片旋转
     * @param splitImage
     * @param angle
     * @return
     */
    public static Mat rotate3(Mat splitImage, double angle){
        double thera = angle * Math.PI / 180;
        double a = Math.sin(thera);
        double b = Math.cos(thera);
  
        int wsrc = splitImage.width();
        int hsrc = splitImage.height();
  
        int wdst = (int) (hsrc * Math.abs(a) + wsrc * Math.abs(b));
        int hdst = (int) (wsrc * Math.abs(a) + hsrc * Math.abs(b));
        Mat imgDst = new Mat(hdst, wdst, splitImage.type());
  
        Point pt = new Point(splitImage.cols() / 2, splitImage.rows() / 2);
        // 获取仿射变换矩阵
        Mat affineTrans = Imgproc.getRotationMatrix2D(pt, angle, 1.0);
  
        //System.out.println(affineTrans.dump());
        // 改变变换矩阵第三列的值
        affineTrans.put(0, 2, affineTrans.get(0, 2)[0] + (wdst - wsrc) / 2);
        affineTrans.put(1, 2, affineTrans.get(1, 2)[0] + (hdst - hsrc) / 2);
  
        Imgproc.warpAffine(splitImage, imgDst, affineTrans, imgDst.size(),
                Imgproc.INTER_CUBIC | Imgproc.WARP_FILL_OUTLIERS);
        return imgDst;
    }
  
    /**
     * 图像直方图处理
     * @param mat
     * @return
     */
    public static Mat equalizeHist(Mat mat){
        Mat dst = new Mat();
        List<Mat> mv = new ArrayList<>();
        Core.split(mat, mv);
        for (int i = 0; i < mat.channels(); i++)
        {
            Imgproc.equalizeHist(mv.get(i), mv.get(i));
        }
        Core.merge(mv, dst);
        return dst;
    }
  
    /**
     * 8邻域降噪,又有点像9宫格降噪;即如果9宫格中心被异色包围，则同化
     * @param pNum 默认值为1
     */
    public static Mat navieRemoveNoise(Mat mat,int pNum) {
        int i, j, m, n, nValue, nCount;
        int nWidth = mat.cols();
        int nHeight = mat.rows();
  
        // 如果一个点的周围都是白色的，而它确是黑色的，删除它
        for (j = 1; j < nHeight - 1; ++j) {
            for (i = 1; i < nWidth - 1; ++i) {
                nValue =  (int)mat.get(j, i)[0];
                if (nValue == 0) {
                    nCount = 0;
                    // 比较以(j ,i)为中心的9宫格，如果周围都是白色的，同化
                    for (m = j - 1; m <= j + 1; ++m) {
                        for (n = i - 1; n <= i + 1; ++n) {
                            if ((int)mat.get(m, n)[0] == 0) {
                                nCount++;
                            }
                        }
                    }
                    if (nCount <= pNum) {
                        // 周围黑色点的个数小于阀值pNum,把该点设置白色
                        mat.put(j, i, WHITE);
                    }
                } else {
                    nCount = 0;
                    // 比较以(j ,i)为中心的9宫格，如果周围都是黑色的，同化
                    for (m = j - 1; m <= j + 1; ++m) {
                        for (n = i - 1; n <= i + 1; ++n) {
                            if ((int)mat.get(m, n)[0] == 0) {
                                nCount++;
                            }
                        }
                    }
                    if (nCount >= 7) {
                        // 周围黑色点的个数大于等于7,把该点设置黑色;即周围都是黑色
                        mat.put(j, i, BLACK);
                    }
                }
            }
        }
        return mat;
    }
  
    /**
     * 连通域降噪
     * @param pArea 默认值为1
     */
    public static Mat contoursRemoveNoise(Mat mat,double pArea) {
        //mat=floodFill(mat,mat.new Point(mat.cols()/2,mat.rows()/2),new Color(225,0,0));
        int i, j, color = 1;
        int nWidth =  mat.cols(), nHeight = mat.rows();
  
        for (i = 0; i < nWidth; ++i) {
            for (j = 0; j < nHeight; ++j) {
                if ((int) mat.get(j, i)[0] == BLACK) {
                    //用不同颜色填充连接区域中的每个黑色点
                    //floodFill就是把一个点x的所有相邻的点都涂上x点的颜色，一直填充下去，直到这个区域内所有的点都被填充完为止
                    Imgproc.floodFill(mat, new Mat(), new Point(i, j), new Scalar(color));
                    color++;
                }
            }
        }
        //统计不同颜色点的个数
        int[] ColorCount = new int[255];
        for (i = 0; i < nWidth; ++i) {
            for (j = 0; j < nHeight; ++j) {
                if ((int) mat.get(j, i)[0] != 255) {
                    ColorCount[(int) mat.get(j, i)[0] - 1]++;
                }
            }
        }
        //去除噪点
        for (i = 0; i < nWidth; ++i) {
            for (j = 0; j < nHeight; ++j) {
                if (ColorCount[(int) mat.get(j, i)[0] - 1] <= pArea) {
                    mat.put(j, i, WHITE);
                }
            }
        }
        for (i = 0; i < nWidth; ++i) {
            for (j = 0; j < nHeight; ++j) {
                if ((int) mat.get(j, i)[0] < WHITE) {
                    mat.put(j, i, BLACK);
                }
            }
        }
        return mat;
    }
  
    /**
     * Mat转换成BufferedImage
     * @param matrix 要转换的Mat
     * @param fileExtension 格式为 ".jpg", ".png", etc
     * @return
     */
    public static BufferedImage Mat2BufImg (Mat matrix, String fileExtension) {
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(fileExtension, matrix, mob);
        byte[] byteArray = mob.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufImage;
    }
  
    /**
     * BufferedImage转换成Mat
     * @param original 要转换的BufferedImage
     * @param imgType bufferedImage的类型 如 BufferedImage.TYPE_3BYTE_BGR
     * @param matType 转换成mat的type 如 CvType.CV_8UC3
     */
    public static Mat BufImg2Mat (BufferedImage original, int imgType, int matType) {
        if (original == null) {
            throw new IllegalArgumentException("original == null");
        }
        if (original.getType() != imgType) {
            BufferedImage image = new BufferedImage(original.getWidth(), original.getHeight(), imgType);
            Graphics2D g = image.createGraphics();
            try {
                g.setComposite(AlphaComposite.Src);
                g.drawImage(original, 0, 0, null);
            } finally {
                g.dispose();
            }
        }
        DataBufferByte dbi =(DataBufferByte)original.getRaster().getDataBuffer();
        byte[] pixels = dbi.getData();
        Mat mat = Mat.eye(original.getHeight(), original.getWidth(), matType);
        mat.put(0, 0, pixels);
        return mat;
    }
  
    /**
     * 人眼识别
     * @param mat
     * @return
     */
    public static List<Point> eye(Mat mat){
        List<Point> eyeList=new ArrayList<>();
        CascadeClassifier eyeDetector = new CascadeClassifier(System.getProperty("user.dir")+"\\opencv\\haarcascades\\haarcascade_eye.xml");
        // 在图片中检测人眼
        MatOfRect eyeDetections = new MatOfRect();
        //指定人脸识别的最大和最小像素范围
        Size minSize = new Size(20, 20);
        Size maxSize = new Size(30, 30);
  
        eyeDetector.detectMultiScale(mat, eyeDetections, 1.1f, 3, 0, minSize, maxSize);
        Rect[] rects = eyeDetections.toArray();
        if(rects != null && rects.length == 2){
            Point point1=new Point(rects[0].x,rects[0].y);
            eyeList.add(point1);
            Point point2=new Point(rects[1].x,rects[1].y);
            eyeList.add(point2);
        }else{
            return null;
        }
        return eyeList;
    }
  
    /**
     * 获取轮廓的顶点坐标
     * @param contour
     * @return
     */
    public static List<Point> getPointList(MatOfPoint contour){
        MatOfPoint2f mat2f=new MatOfPoint2f();
        contour.convertTo(mat2f,CvType.CV_32FC1);
        RotatedRect rect=Imgproc.minAreaRect(mat2f);
        Mat points=new Mat();
        Imgproc.boxPoints(rect,points);
        return getPoints(points.dump());
    }
  
    /**
     * 获取轮廓的面积
     * @param contour
     * @return
     */
    public static double area (MatOfPoint contour){
        MatOfPoint2f mat2f=new MatOfPoint2f();
        contour.convertTo(mat2f,CvType.CV_32FC1);
        RotatedRect rect=Imgproc.minAreaRect(mat2f);
        return rect.boundingRect().area();
    }
  
    /**
     * 获取点坐标集合
     * @param str
     * @return
     */
    public  static List<Point> getPoints(String str){
        List<Point> points=new ArrayList<>();
        str=str.replace("[","").replace("]","");
        String[] pointStr=str.split(";");
        for(int i=0;i<pointStr.length;i++){
            double x=Double.parseDouble(pointStr[i].split(",")[0]);
            double y=Double.parseDouble(pointStr[i].split(",")[1]);
            Point po=new Point(x,y);
            points.add(po);
        }
        return points;
    }
  
    /**
     * 获取最小的X坐标
     * @param points
     * @return
     */
    public  static int minX(List<Point> points){
        Collections.sort(points, new XComparator(false));
        return (int)(points.get(0).x>0?points.get(0).x:-points.get(0).x);
    }
  
    /**
     * 获取最小的Y坐标
     * @param points
     * @return
     */
    public  static int minY(List<Point> points){
        Collections.sort(points, new YComparator(false));
        return (int)(points.get(0).y>0?points.get(0).y:-points.get(0).y);
    }
  
    /**
     * 获取最长的X坐标距离
     * @param points
     * @return
     */
    public static int xLength(List<Point> points){
        Collections.sort(points, new XComparator(false));
        return (int)(points.get(3).x-points.get(0).x);
    }
  
    /**
     * 获取最长的Y坐标距离
     * @param points
     * @return
     */
    public  static int yLength(List<Point> points){
        Collections.sort(points, new YComparator(false));
        return (int)(points.get(3).y-points.get(0).y);
    }
  
    //集合排序规则（根据X坐标排序）
    public static class XComparator implements Comparator<Point> {
        private boolean reverseOrder; // 是否倒序
        public XComparator(boolean reverseOrder) {
            this.reverseOrder = reverseOrder;
        }
  
        public int compare(Point arg0, Point arg1) {
            if(reverseOrder)
                return (int)arg1.x - (int)arg0.x;
            else
                return (int)arg0.x - (int)arg1.x;
        }
    }
  
    //集合排序规则（根据Y坐标排序）
    public static class YComparator implements Comparator<Point> {
        private boolean reverseOrder; // 是否倒序
        public YComparator(boolean reverseOrder) {
            this.reverseOrder = reverseOrder;
        }
  
        public int compare(Point arg0, Point arg1) {
            if(reverseOrder)
                return (int)arg1.y - (int)arg0.y;
            else
                return (int)arg0.y - (int)arg1.y;
        }
    }
}
