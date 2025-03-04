/*

 * Copyright 2022-2030 the original author or authors.
 *
 * Licensed under the company, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.company.com/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sandy.ecp.framework.expression;

/**
 * 企业云平台 Count函数解析
 * @author Sandy
 * @date 2024-04-04 13:12:12
 * @since 1.0.0
 */
public class CountFunction implements FunctionVariadic {
	
	private static final long serialVersionUID = 2819091491259074340L;

	public double calculate(double... parameters) {
        if (parameters == null) return Double.NaN;
        if (parameters.length == 0) return Double.NaN;
        return parameters.length;
    }
	
    public FunctionVariadic clone() {
        return new CountFunction();
    }

}

