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
package com.sandy.ecp.recognition.train;

import java.util.Set;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.ml.ANN_MLP;
import org.opencv.ml.Ml;
import org.opencv.ml.TrainData;

import com.google.common.collect.Sets;
import com.sandy.ecp.recognition.config.Constant;
import com.sandy.ecp.recognition.util.FileUtil;
import com.sandy.ecp.recognition.util.PlateUtil;

/**
 * 基于org.opencv包实现的训练
 * 图片文字识别训练
 * 训练出来的库文件，用于识别图片中的数字及字母
 * @author sandy
 * @date 2020-05-14 22:16
 */
public class ANNTrain {

    private ANN_MLP ann = ANN_MLP.create();

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    // 默认的训练操作的根目录
    private static final String DEFAULT_PATH = "D:/PlateDetect/train/chars_sample/";

    // 训练模型文件保存位置
    private static final String MODEL_PATH = DEFAULT_PATH + "ann_blue.xml";
    // private static final String MODEL_PATH = DEFAULT_PATH + "ann_green.xml";
    // private static final String MODEL_PATH = DEFAULT_PATH + "ann_all.xml";
    
    
    
    /**
     * 加小编微信，可获取更多车牌字符样本文件，还可以获取更多帮助哟
     * 小编整理样本文件，差点没把眼睛看瞎了，整理相关算法整理更是不易，请小编喝杯18块咖啡提提神即可
     * 微信号：localhost18888
     * 样本文件包含：
     *  1、从停车场图片提取的【蓝牌】字符样本，包含数字、字母、汉字； 19226个样本文件；可用于识别蓝牌、黄牌字符等
     *  2、从停车场图片提取的【绿牌】字符样本，包含数字、字母、汉字；    3998个样本文件；可用于识别绿牌字符； // 绿牌的部分字符跟蓝牌字体不一样，导致模型要添加蓝牌、绿牌样本
     *  3、旧项目上整理过的【蓝牌】字符样本，包含数字、字母、汉字；             16013个样本文件；可用于识别蓝牌、黄牌字符等
     *  4、旋转、错切、膨胀、腐蚀处理的【蓝牌、绿牌】样本，包含数字、字母、汉字；     101400个样本文件；可用于识别蓝牌、黄牌、绿字符等
     * @param _predictsize
     * @param _neurons
     */
    public void train(int _predictsize, int _neurons) {
        Mat samples = new Mat(); // 使用push_back，行数列数不能赋初始值
        Vector<Integer> trainingLabels = new Vector<Integer>();
        
        Set<String> sampleDir = Sets.newHashSet();
        
        // 加载数字及字母字符
        for (int i = 0; i < Constant.numCharacter; i++) {
            sampleDir.clear();
            // sampleDir.add(DEFAULT_PATH + "chars_blue_old/" + Constant.strCharacters[i]);
            // sampleDir.add(DEFAULT_PATH + "chars_blue_new/" + Constant.strCharacters[i]);
            sampleDir.add(DEFAULT_PATH + "chars_green/" + Constant.strCharacters[i]);
            // sampleDir.add(DEFAULT_PATH + "chars_shear/blue/" + Constant.strCharacters[i]);
            sampleDir.add(DEFAULT_PATH + "chars_shear/green/" + Constant.strCharacters[i]);
            
            Vector<String> files = new Vector<String>();
            for (String str : sampleDir) {
                FileUtil.getFiles(str, files);
            }
            for (String filename : files) {
                Mat img = Imgcodecs.imread(filename, 0);
                Mat f = PlateUtil.features(img, _predictsize);
                samples.push_back(f);
                trainingLabels.add(i); // 每一幅字符图片所对应的字符类别索引下标
            }
        }

        
        samples.convertTo(samples, CvType.CV_32F);

        //440   vhist.length + hhist.length + lowData.cols() * lowData.rows();
        // CV_32FC1 CV_32SC1 CV_32F
        Mat classes = Mat.zeros(trainingLabels.size(), Constant.strCharacters.length, CvType.CV_32F);

        float[] labels = new float[trainingLabels.size()];
        for (int i = 0; i < labels.length; ++i) {
            classes.put(i, trainingLabels.get(i), 1.f);
        }

        // samples.type() == CV_32F || samples.type() == CV_32S 
        TrainData train_data = TrainData.create(samples, Ml.ROW_SAMPLE, classes);

        ann.clear();
        Mat layers = new Mat(1, 3, CvType.CV_32F);
        layers.put(0, 0, samples.cols());   // 样本特征数 140  10*10 + 20+20
        layers.put(0, 1, _neurons); // 神经元个数
        layers.put(0, 2, classes.cols());   // 字符数

        ann.setLayerSizes(layers);
        ann.setActivationFunction(ANN_MLP.SIGMOID_SYM, 1, 1);
        ann.setTrainMethod(ANN_MLP.BACKPROP);
        TermCriteria criteria = new TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER, 30000, 0.0001);
        ann.setTermCriteria(criteria);
        ann.setBackpropWeightScale(0.1);
        ann.setBackpropMomentumScale(0.1);
        ann.train(train_data);

        // FileStorage fsto = new FileStorage(MODEL_PATH, FileStorage.WRITE);
        // ann.write(fsto, "ann");
        ann.save(MODEL_PATH);
    }


    public void predict() {
        ann.clear();
        ann = ANN_MLP.load(MODEL_PATH);

        int total = 0;
        int correct = 0;

        Set<String> sampleDir = Sets.newHashSet();
        // 遍历测试样本下的所有文件，计算预测准确率
        for (int i = 0; i < Constant.strCharacters.length; i++) {
            char c = Constant.strCharacters[i];
            sampleDir.clear();
            sampleDir.add(DEFAULT_PATH + "chars_blue_old/" + c);
            // sampleDir.add(DEFAULT_PATH + "chars_blue_new/" + c);
            // sampleDir.add(DEFAULT_PATH + "chars_green/" + c);
            // sampleDir.add(DEFAULT_PATH + "chars_shear/blue/" + c);
            // sampleDir.add(DEFAULT_PATH + "chars_shear/green/" + c);
            Vector<String> files = new Vector<String>();
            for (String str : sampleDir) {
                FileUtil.getFiles(str, files);
            }
            for (String filePath : files) {
                Mat img = Imgcodecs.imread(filePath, 0);
                Mat f = PlateUtil.features(img, Constant.predictSize);
                int index = 0;
                double maxVal = -2;
                Mat output = new Mat(1, Constant.strCharacters.length, CvType.CV_32F);
                ann.predict(f, output);  // 预测结果
                for (int j = 0; j < Constant.strCharacters.length; j++) {
                    double val = output.get(0, j)[0];
                    if (val > maxVal) {
                        maxVal = val;
                        index = j;
                    }
                }
                // 膨胀
                /*f = PlateUtil.features(ImageUtil.dilate(img, false, null, 2, 2, false), Constant.predictSize);
                ann.predict(f, output);  // 预测结果
                for (int j = 0; j < Constant.strCharacters.length; j++) {
                    double val = output.get(0, j)[0];
                    if (val > maxVal) {
                        maxVal = val;
                        index = j;
                    }
                }*/
                String result = String.valueOf(Constant.strCharacters[index]);
                if(result.equals(String.valueOf(c))) {
                    correct++;
                } else {
                    /*for (int j = 0; j < Constant.strCharacters.length; j++) {
                        double val = output.get(0, j)[0];
                        if(val > 0.1) {
                            System.out.println( j + "===>" + val);
                        }
                    }*/
                    System.err.print("原样本：" + String.valueOf(c));
                    System.err.print("\t预测结果：" + result);
                    System.err.println("\t" +filePath);
                }
                total++;
            }
        }

        System.out.print("total:" + total);
        System.out.print("\tcorrect:" + correct);
        System.out.print("\terror:" + (total - correct));
        System.out.println("\t计算准确率为：" + correct / (total * 1.0));
        
        // 旧蓝 识别  旧蓝
        // total:13079  correct:13037   error:42    计算准确率为：0.9967887453169203
        // total:13057 correct:12937   error:120   计算准确率为：0.9908095274565367
        // total:13057  correct:13046   error:11    计算准确率为：0.9991575400168492
        // total:13044  correct:13026   error:18    计算准确率为：0.9986200551977921
        // total:13032  correct:13017   error:15    计算准确率为：0.9988489871086557
        
        // 旧蓝 识别  新蓝
        //total:16952   correct:15400   error:1552  计算准确率为：0.9084473808400189
        
        // -- 使用错切、旋转、膨胀、腐蚀，蓝牌、绿牌样本训练，识别旧蓝牌样本
        // total:13079 correct:11631   error:1448  计算准确率为：0.889288171878584
        // total:3692   correct:3209    error:483   计算准确率为：0.8691765980498375
        
        // 新蓝 识别  新蓝
        // total:16952  correct:16857   error:95    计算准确率为：0.9943959414818311
        // 新蓝 识别  旧蓝
        // total:13079  correct:12528   error:551   计算准确率为：0.9578713968957872
        
        // 旧蓝 + 新蓝 + 绿  识别 绿 
        // total:3702   correct:3675    error:27    计算准确率为：0.9927066450567261
        
        // 旧蓝 + 新蓝 + 绿 + 错切等 
        
        return;
    }

    public static void main(String[] args) {
        ANNTrain annT = new ANNTrain();
        // 这里只训练model文件夹下的ann.xml，此模型是一个predictSize=10,neurons=40的ANN模型
        // 可根据需要训练不同的predictSize或者neurons的ANN模型
        // 根据机器的不同，训练时间不一样，但一般需要10分钟左右，所以慢慢等一会吧
        // 可以考虑中文，数字字母分开训练跟识别，提高准确性
        annT.train(Constant.predictSize, Constant.neurons);
        // annT.predict();
        System.err.println("The end.");
        return;
    }
}