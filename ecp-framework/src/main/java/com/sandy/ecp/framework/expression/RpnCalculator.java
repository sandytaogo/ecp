/*
 * Copyright 2022-2040 the original author or authors.
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

import java.util.Stack;

/**
 * 自定义解析器实现高性能计算
 * 如果需要更高的灵活性，可以自定义解析器（如基于逆波兰表示法 (RPN) 或 ANTLR）。适用于需要完全控制解析逻辑的场景。
 */
public class RpnCalculator {
	
    public static double evaluate(String expression) {
        String[] tokens = expression.split(" ");
        Stack<Double> stack = new Stack<>();
        for (String token : tokens) {
            if (token.matches("-?\\d+(\\.\\d+)?")) {
                stack.push(Double.parseDouble(token));
            } else {
                double b = stack.pop();
                double a = stack.pop();
                switch (token) {
                    case "+": stack.push(a + b); break;
                    case "-": stack.push(a - b); break;
                    case "*": stack.push(a * b); break;
                    case "/": stack.push(a / b); break;
                }
            }
        }
        return stack.pop();
    }

    public static void main(String[] args) {
        String expression = "3 5 2 * + 4 2 / -"; // 3 + 5 * 2 - 4 / 2
        double result = evaluate(expression);
        System.out.println("RPN Result: " + result);
    }
}
