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
package com.sandy.ecp.recognition;


import org.opencv.core.Mat;
import org.opencv.core.Point;

import com.sandy.ecp.recognition.util.ImageUtil;

/**
 * The class Mat represents an n-dimensional dense numerical single-channel or multi-channel array. 
 * It can be used to store real or complex-valued vectors and matrices, grayscale or color images, voxel volumes, vector fields, 
 * point clouds, tensors, histograms (though, very high-dimensional histograms may be better stored in a SparseMat). 
 * 线段实体类
 * 也可以用来标记一条直线
 * @author Sandy
 * @date 2020-12-07 13:37
 */
public class Line {
    
    private Long id;

    private Point start;

    private Point end;
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Point getStart() {
		return start;
	}
	public void setStart(Point start) {
		this.start = start;
	}
	public Point getEnd() {
		return end;
	}
	public void setEnd(Point end) {
		this.end = end;
	}
	public Line(Point start, Point end) {
        setId(1L);
        setStart(start);
        setEnd(end);
    }
    /**
     * 
     * @param row 四通道，1cols
     */
    public Line(Mat row) {
        setStart(new Point(row.get(0, 0)[0], row.get(0, 0)[1]));
        setEnd(new Point(row.get(0, 0)[2], row.get(0, 0)[3]));
    }
    public Line(double startX, double startY, double endX, double endY) {
        setStart(new Point(startX, startY));
        setEnd(new Point(endX, endY));
    }
    
    /**
     * 线段长度
     * @return
     */
    public double getLength() {
        return Math.sqrt(Math.pow((start.x - end.x), 2) + Math.pow((start.y - end.y), 2));
    }
    
    /**
     * 计算线段的中间点
     * @return
     */
    public Point getMidPoint() {
        return new Point((start.x + end.x)/2, (start.y + end.y)/2);
    }
    
    /**
     * 计算原点(0,0)到当前线段所在直线的距离
     * @return
     */
    public double getDistanceToOrigin() {
        return ImageUtil.getDistance(new Point(0, 0), start, end);
    }
    
    /**
     * 计算线段的斜率
     * @return
     */
    public double getK() {
        if(end.x == start.x) {
            return Double.MAX_VALUE; // 跟Y轴平行
        }
        /*if(end.y == start.y) {
            return 0; // 跟X轴平行
        }*/
        return  (end.y - start.y) / (end.x - start.x);
    }
    
    public boolean equals(Line l) {
        if(l.getId().equals(getId())) {
            return true;
        }
        return false;
    }

}
