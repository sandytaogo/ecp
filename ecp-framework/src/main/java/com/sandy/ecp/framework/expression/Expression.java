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

import java.io.Serializable;
import java.util.Stack;

/**
 * 企业云平台 公式函数解析
 * @author Sandy
 * @date 2024-04-04 13:12:12
 * @since 1.0.0
 */
public class Expression implements Serializable {

	private static final long serialVersionUID = 6003193461442015737L;

	public double calculate(String formula) {
		
        String[] tokens = formula.split("(?<=[-+])|(?=[-+])");
        
        Stack<Double> stack = new Stack<Double>();
        
        for (String token : tokens) {
            if (token.isEmpty()) continue;
            if (token.matches("\\d+")) {
                stack.push(Double.parseDouble(token));
            } else if (1 < stack.size()) {
                double b = stack.pop();
                double a = stack.pop();
                switch (token.charAt(0)) {
                    case '+': stack.push(a + b); break;
                    case '-': stack.push(a - b); break;
                }
            }
        }
        return stack.pop();
    }
}
