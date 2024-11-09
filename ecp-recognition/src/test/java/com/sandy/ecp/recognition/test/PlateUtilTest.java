package com.sandy.ecp.recognition.test;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.Vector;

import org.opencv.core.Mat;

import com.google.common.collect.Sets;
import com.sandy.ecp.recognition.config.Constant;
import com.sandy.ecp.recognition.enums.PlateColor;
import com.sandy.ecp.recognition.enums.PlateHSV;
import com.sandy.ecp.recognition.util.PlateUtil;

public class PlateUtilTest {

	public void face() {
	        Instant start = Instant.now();
        String tempPath = Constant.DEFAULT_TEMP_DIR;
        String filename = Constant.DEFAULT_DIR + "test/11.jpg";
        File f = new File(filename);
        if (!f.exists()) {
            File f1 = new File(filename.replace("jpg", "png"));
            File f2 = new File(filename.replace("png", "bmp"));
            filename = f1.exists() ? f1.getPath() : f2.getPath();
        }
        Boolean debug = true;
        Vector<Mat> dst = new Vector<Mat>();
        // 提取车牌图块
        // getPlateMat(filename, dst, debug, tempPath);
        // findPlateByContours(filename, dst, debug, tempPath);
        // findPlateByHsvFilter(filename, dst, PlateHSV.BLUE, debug, tempPath);
        PlateUtil.findPlateByHsvFilter(filename, dst, PlateHSV.GREEN, debug, tempPath);
        Set<String> result = Sets.newHashSet();
        dst.stream().forEach(inMat -> {
            // 识别车牌颜色
            PlateColor color = PlateUtil.getPlateColor(inMat, true, debug, tempPath);
            // 识别车牌字符
            String plateNo = PlateUtil.charsSegment(inMat, color, debug, tempPath);
            result.add(plateNo + "\t" + color.desc);
        });
        System.out.println(result.toString());
        Instant end = Instant.now();
        System.err.println("总耗时：" + Duration.between(start, end).toMillis());
	}
}
