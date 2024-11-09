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


import org.opencv.core.Rect;

/**
 * 车牌字符识别结果
 * @author sandy
 * @date 2020-11-14 21:29
 */
//@Data
//@NoArgsConstructor
public class PlateRecoResult {

    /**
     * 字符序列
     */
    private Integer sort;
    
    /**
     * 字符
     */
    private String chars;
    
    /**
     * 识别置信度
     */
    private Double confi;
    
    /**
     * 字符所在轮廓，最小正矩形
     */
    private Rect rect;

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getChars() {
		return chars;
	}

	public void setChars(String chars) {
		this.chars = chars;
	}

	public Double getConfi() {
		return confi;
	}

	public void setConfi(Double confi) {
		this.confi = confi;
	}

	public Rect getRect() {
		return rect;
	}

	public void setRect(Rect rect) {
		this.rect = rect;
	}
}

