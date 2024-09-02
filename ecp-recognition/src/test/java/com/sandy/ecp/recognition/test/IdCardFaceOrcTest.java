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

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import com.sandy.ecp.recognition.util.OpencvUtil;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.LoadLibs;

import static com.sandy.ecp.recognition.util.OpencvUtil.rotate3;
import static com.sandy.ecp.recognition.util.OpencvUtil.shear;
  
public class IdCardFaceOrcTest {
	
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        //注意程序运行的时候需要在VM option添加该行 指明opencv的dll文件所在路径
        //-Djava.library.path=$PROJECT_DIR$\opencv\x64
    }
    public static void main(String[] args){
        String path="/mnt/credential/idcard/c.jpg";
        Mat mat= Imgcodecs.imread(path);
        cardUp(mat);
    }
  
    /**
     * 身份证反面识别
     */
    public static void cardDown(Mat mat){
        //灰度
        mat=OpencvUtil.gray(mat);
        //二值化
        mat=OpencvUtil.binary(mat);
        //腐蚀
        mat=OpencvUtil.erode(mat,3);
        //膨胀
        mat=OpencvUtil.dilate(mat,3);
  
        //检测是否有居民身份证字体，若有为正向，若没有则旋转图片
        for(int i=0;i<4;i++){
            String temp=temp(mat);
            if(!temp.contains("居")&&!temp.contains("民")){
                mat= rotate3(mat,90);
            }else{
                break;
            }
        }
  
        Imgcodecs.imwrite("/mnt/credential/idcard/result.jpg", mat);
        String organization=organization (mat);
        System.out.print("签发机关是："+organization);
  
        String time=time (mat);
        System.out.print("有效期限是："+time);
    }
    
    /**
     * 识别图片信息
     * @param img
     * @return
     */
    public static String getImageMessage(BufferedImage img,String language) {
        String result="end";
        try{
            ITesseract instance = new Tesseract();
            File tessDataFolder = LoadLibs.extractTessResources("tessdata");
            instance.setLanguage(language);
            instance.setDatapath(tessDataFolder.getAbsolutePath());
            result = instance.doOCR(img);
            //System.out.println(result);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return result;
    }
  
    public static String temp (Mat mat){
        Point point1=new Point(mat.cols()*0.30,mat.rows()*0.25);
        Point point2=new Point(mat.cols()*0.30,mat.rows()*0.25);
        Point point3=new Point(mat.cols()*0.90,mat.rows()*0.45);
        Point point4=new Point(mat.cols()*0.90,mat.rows()*0.45);
        List<Point> list=new ArrayList<>();
        list.add(point1);
        list.add(point2);
        list.add(point3);
        list.add(point4);
        Mat temp= shear(mat,list);
  
        List<MatOfPoint> nameContours=OpencvUtil.findContours(temp);
        for (int i = 0; i < nameContours.size(); i++)
        {
            double area=OpencvUtil.area(nameContours.get(i));
            if(area<100){
                Imgproc.drawContours(temp, nameContours, i, new Scalar( 0, 0, 0), -1);
            }
        }
        Imgcodecs.imwrite("/mnt/credential/idcard/temp.jpg", temp);
        BufferedImage nameBuffer=OpencvUtil.Mat2BufImg(temp,".jpg");
        String nameStr=getImageMessage(nameBuffer,"chi_sim");
        nameStr=nameStr.replace("\n","");
        return nameStr;
    }
  
    public static String organization (Mat mat){
        Point point1=new Point(mat.cols()*0.36,mat.rows()*0.68);
        Point point2=new Point(mat.cols()*0.36,mat.rows()*0.68);
        Point point3=new Point(mat.cols()*0.80,mat.rows()*0.80);
        Point point4=new Point(mat.cols()*0.80,mat.rows()*0.80);
        List<Point> list=new ArrayList<>();
        list.add(point1);
        list.add(point2);
        list.add(point3);
        list.add(point4);
        Mat name= shear(mat,list);
  
        List<MatOfPoint> nameContours=OpencvUtil.findContours(name);
        for (int i = 0; i < nameContours.size(); i++)
        {
            double area=OpencvUtil.area(nameContours.get(i));
            if(area<100){
                Imgproc.drawContours(name, nameContours, i, new Scalar( 0, 0, 0), -1);
            }
        }
        Imgcodecs.imwrite("/mnt/credential/idcard/organization.jpg", name);
        BufferedImage nameBuffer=OpencvUtil.Mat2BufImg(name,".jpg");
        String nameStr=getImageMessage(nameBuffer,"chi_sim");
        nameStr=nameStr.replace("\n","");
        return nameStr+"\n";
    }
  
    public static String time (Mat mat){
        Point point1=new Point(mat.cols()*0.38,mat.rows()*0.82);
        Point point2=new Point(mat.cols()*0.38,mat.rows()*0.82);
        Point point3=new Point(mat.cols()*0.85,mat.rows()*0.92);
        Point point4=new Point(mat.cols()*0.85,mat.rows()*0.92);
        List<Point> list=new ArrayList<>();
        list.add(point1);
        list.add(point2);
        list.add(point3);
        list.add(point4);
        Mat time= shear(mat,list);
  
        List<MatOfPoint> timeContours=OpencvUtil.findContours(time);
        for (int i = 0; i < timeContours.size(); i++)
        {
            double area=OpencvUtil.area(timeContours.get(i));
            if(area<100){
                Imgproc.drawContours(time, timeContours, i, new Scalar( 0, 0, 0), -1);
            }
        }
        Imgcodecs.imwrite("/mnt/credential/idcard/time.jpg", time);
  
        //起始日期
        Point startPoint1=new Point(0,0);
        Point startPoint2=new Point(0,time.rows());
        Point startPoint3=new Point(time.cols()*0.47,0);
        Point startPoint4=new Point(time.cols()*0.47,time.rows());
        List<Point> startList=new ArrayList<>();
        startList.add(startPoint1);
        startList.add(startPoint2);
        startList.add(startPoint3);
        startList.add(startPoint4);
        Mat start= shear(time,startList);
        Imgcodecs.imwrite("/mnt/credential/idcard/start.jpg", start);
        BufferedImage yearBuffer=OpencvUtil.Mat2BufImg(start,".jpg");
        String startStr=getImageMessage(yearBuffer,"eng");
        startStr=startStr.replace("-","");
        startStr=startStr.replace(" ","");
        startStr=startStr.replace("\n","");
  
        //截止日期
        Point endPoint1=new Point(time.cols()*0.47,0);
        Point endPoint2=new Point(time.cols()*0.47,time.rows());
        Point endPoint3=new Point(time.cols(),0);
        Point endPoint4=new Point(time.cols(),time.rows());
        List<Point> endList=new ArrayList<>();
        endList.add(endPoint1);
        endList.add(endPoint2);
        endList.add(endPoint3);
        endList.add(endPoint4);
        Mat end= shear(time,endList);
        Imgcodecs.imwrite("/mnt/credential/idcard/end.jpg", end);
        BufferedImage endBuffer=OpencvUtil.Mat2BufImg(end,".jpg");
        String endStr=getImageMessage(endBuffer,"chi_sim");
        if(!endStr.contains("长")&&!endStr.contains("期")){
            endStr=getImageMessage(endBuffer,"eng");
            endStr=endStr.replace("-","");
            endStr=endStr.replace(" ","");
        }
  
        return startStr+"-"+endStr;
    }
  
    /**
     * 身份证正面识别
     */
    public static void cardUp (Mat mat){
        Mat begin=mat.clone();
        //截取身份证区域，并校正旋转角度
        mat = OpencvUtil.houghLinesP(begin,mat);
        Imgcodecs.imwrite("/mnt/credential/idcard/houghLinesP.jpg", mat);
        //循环进行人脸识别,校正图片方向
        mat=OpencvUtil.faceLoop(mat);
        Imgcodecs.imwrite("/mnt/credential/idcard/face.jpg", mat);
        //灰度
        mat=OpencvUtil.gray(mat);
        //二值化
        mat=OpencvUtil.binary(mat);
        //腐蚀
        mat=OpencvUtil.erode(mat,1);
        //膨胀
        mat=OpencvUtil.dilate(mat,1);
        Imgcodecs.imwrite("/mnt/credential/idcard/bbb.jpg", mat);
        //获取名称
        String name=name(mat);
        System.out.print("姓名是："+name);
        //获取性别
        String sex=sex(mat);
        System.out.print("性别是："+sex);
  
        //获取民族
        String nation=nation(mat);
        System.out.print("民族是："+nation);
  
        //获取出生日期
        String birthday=birthday(mat);
        System.out.print("出生日期是："+birthday);
  
        //获取住址
        String address=address(mat);
        System.out.print("住址是："+address);
  
        //获取身份证
        String card=card(mat);
        System.out.print("身份证号是："+card);
    }
  
    public static String name(Mat mat){
  
        Point point1=new Point(mat.cols()*0.18,mat.rows()*0.11);
        Point point2=new Point(mat.cols()*0.18,mat.rows()*0.24);
        Point point3=new Point(mat.cols()*0.4,mat.rows()*0.11);
        Point point4=new Point(mat.cols()*0.4,mat.rows()*0.24);
        List<Point> list=new ArrayList<>();
        list.add(point1);
        list.add(point2);
        list.add(point3);
        list.add(point4);
        Mat name= shear(mat,list);
        name=OpencvUtil.drawContours(name,50);
        Imgcodecs.imwrite("/mnt/credential/idcard/name.jpg", name);
        BufferedImage nameBuffer=OpencvUtil.Mat2BufImg(name,".jpg");
        String nameStr=getImageMessage(nameBuffer,"chi_sim");
        nameStr=nameStr.replace("\n","");
        return nameStr+"\n";
    }
  
    public static String sex(Mat mat){
        Point point1=new Point(mat.cols()*0.18,mat.rows()*0.25);
        Point point2=new Point(mat.cols()*0.18,mat.rows()*0.35);
        Point point3=new Point(mat.cols()*0.25,mat.rows()*0.25);
        Point point4=new Point(mat.cols()*0.25,mat.rows()*0.35);
        List<Point> list=new ArrayList<>();
        list.add(point1);
        list.add(point2);
        list.add(point3);
        list.add(point4);
        Mat sex= shear(mat,list);
        sex=OpencvUtil.drawContours(sex,50);
        Imgcodecs.imwrite("/mnt/credential/idcard/sex.jpg", sex);
        BufferedImage sexBuffer=OpencvUtil.Mat2BufImg(sex,".jpg");
        String sexStr=getImageMessage(sexBuffer,"chi_sim");
        sexStr=sexStr.replace("\n","");
        return sexStr+"\n";
    }
  
    public static String nation(Mat mat){
        Point point1=new Point(mat.cols()*0.39,mat.rows()*0.25);
        Point point2=new Point(mat.cols()*0.39,mat.rows()*0.36);
        Point point3=new Point(mat.cols()*0.55,mat.rows()*0.25);
        Point point4=new Point(mat.cols()*0.55,mat.rows()*0.36);
        List<Point> list=new ArrayList<>();
        list.add(point1);
        list.add(point2);
        list.add(point3);
        list.add(point4);
        Mat nation= shear(mat,list);
        Imgcodecs.imwrite("/mnt/credential/idcard/nation.jpg", nation);
        BufferedImage nationBuffer=OpencvUtil.Mat2BufImg(nation,".jpg");
        String nationStr=getImageMessage(nationBuffer,"chi_sim");
        nationStr=nationStr.replace("\n","");
        return nationStr+"\n";
    }
  
    public static String birthday(Mat mat){
        Point point1=new Point(mat.cols()*0.18,mat.rows()*0.35);
        Point point2=new Point(mat.cols()*0.18,mat.rows()*0.35);
        Point point3=new Point(mat.cols()*0.55,mat.rows()*0.48);
        Point point4=new Point(mat.cols()*0.55,mat.rows()*0.48);
        List<Point> list=new ArrayList<>();
        list.add(point1);
        list.add(point2);
        list.add(point3);
        list.add(point4);
        Mat birthday= shear(mat,list);
        birthday=OpencvUtil.drawContours(birthday,50);
        Imgcodecs.imwrite("/mnt/credential/idcard/birthday.jpg", birthday);
        //年份
        Point yearPoint1=new Point(0,0);
        Point yearPoint2=new Point(0,birthday.rows());
        Point yearPoint3=new Point(birthday.cols()*0.29,0);
        Point yearPoint4=new Point(birthday.cols()*0.29,birthday.rows());
        List<Point> yearList=new ArrayList<>();
        yearList.add(yearPoint1);
        yearList.add(yearPoint2);
        yearList.add(yearPoint3);
        yearList.add(yearPoint4);
        Mat year= shear(birthday,yearList);
        Imgcodecs.imwrite("/mnt/credential/idcard/year.jpg", year);
        BufferedImage yearBuffer=OpencvUtil.Mat2BufImg(year,".jpg");
        String yearStr=getImageMessage(yearBuffer,"eng");
  
        //月份
        Point monthPoint1=new Point(birthday.cols()*0.44,0);
        Point monthPoint2=new Point(birthday.cols()*0.44,birthday.rows());
        Point monthPoint3=new Point(birthday.cols()*0.55,0);
        Point monthPoint4=new Point(birthday.cols()*0.55,birthday.rows());
        List<Point> monthList=new ArrayList<>();
        monthList.add(monthPoint1);
        monthList.add(monthPoint2);
        monthList.add(monthPoint3);
        monthList.add(monthPoint4);
        Mat month= shear(birthday,monthList);
        Imgcodecs.imwrite("/mnt/credential/idcard/month.jpg", month);
        BufferedImage monthBuffer=OpencvUtil.Mat2BufImg(month,".jpg");
        String monthStr=getImageMessage(monthBuffer,"eng");
  
        //日期
        Point dayPoint1=new Point(birthday.cols()*0.69,0);
        Point dayPoint2=new Point(birthday.cols()*0.69,birthday.rows());
        Point dayPoint3=new Point(birthday.cols()*0.80,0);
        Point dayPoint4=new Point(birthday.cols()*0.80,birthday.rows());
        List<Point> dayList=new ArrayList<>();
        dayList.add(dayPoint1);
        dayList.add(dayPoint2);
        dayList.add(dayPoint3);
        dayList.add(dayPoint4);
        Mat day= shear(birthday,dayList);
        Imgcodecs.imwrite("/mnt/credential/idcard/day.jpg", day);
        BufferedImage dayBuffer=OpencvUtil.Mat2BufImg(day,".jpg");
        String dayStr=getImageMessage(dayBuffer,"eng");
  
        String birthdayStr=yearStr+"年"+monthStr+"月"+dayStr+"日";
        birthdayStr=birthdayStr.replace("\n","");
        return birthdayStr+"\n";
    }
  
    public static String address(Mat mat){
        Point point1=new Point(mat.cols()*0.17,mat.rows()*0.47);
        Point point2=new Point(mat.cols()*0.17,mat.rows()*0.47);
        Point point3=new Point(mat.cols()*0.61,mat.rows()*0.76);
        Point point4=new Point(mat.cols()*0.61,mat.rows()*0.76);
        List<Point> list=new ArrayList<>();
        list.add(point1);
        list.add(point2);
        list.add(point3);
        list.add(point4);
        Mat address= shear(mat,list);
        address=OpencvUtil.drawContours(address,50);
        Imgcodecs.imwrite("/mnt/credential/idcard/address.jpg", address);
        BufferedImage addressBuffer=OpencvUtil.Mat2BufImg(address,".jpg");
        return getImageMessage(addressBuffer,"chi_sim")+"\n";
    }
  
    public static String card(Mat mat){
        Point point1=new Point(mat.cols()*0.34,mat.rows()*0.75);
        Point point2=new Point(mat.cols()*0.34,mat.rows()*0.75);
        Point point3=new Point(mat.cols()*0.89,mat.rows()*0.91);
        Point point4=new Point(mat.cols()*0.89,mat.rows()*0.91);
        List<Point> list=new ArrayList<>();
        list.add(point1);
        list.add(point2);
        list.add(point3);
        list.add(point4);
        Mat card= shear(mat,list);
        card=OpencvUtil.drawContours(card,50);
        Imgcodecs.imwrite("/mnt/credential/idcard/card.jpg", card);
        BufferedImage cardBuffer=OpencvUtil.Mat2BufImg(card,".jpg");
        return getImageMessage(cardBuffer,"eng")+"\n";
    }
}