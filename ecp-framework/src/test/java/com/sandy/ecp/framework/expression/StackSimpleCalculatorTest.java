package com.sandy.ecp.framework.expression;

import java.util.Stack;

public class StackSimpleCalculatorTest {
 
    public static int evaluate(String formula) {
        String[] tokens = formula.split("(?<=[-+])|(?=[-+])");
        Stack<Integer> stack = new Stack<>();
        for (String token : tokens) {
            if (token.isEmpty()) continue;
            if (token.matches("\\d+")) {
                stack.push(Integer.parseInt(token));
            } else if (1 < stack.size()) {
                int b = stack.pop();
                int a = stack.pop();
                switch (token.charAt(0)) {
                    case '+': stack.push(a + b); break;
                    case '-': stack.push(a - b); break;
                }
            }
        }
        return stack.pop();
    }
 
    public static void main(String[] args) {
        String formula = "1+2-3";
        int result = evaluate(formula);
        System.out.println("Result: " + result); // Outputs "Result: -2"
    }
}
