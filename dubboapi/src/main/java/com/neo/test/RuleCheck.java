package com.neo.test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleCheck {

    public static void main(String[] args) {
        Map<String, String> data = new HashMap<>();
        data.put("abc", "123");
        data.put("ef", "456");
        data.put("ec", "45");
        data.put("yu", "87a");

        String test = "(abc=123 and ef=456 or (ec=45)) and (yu=87)";
        if (isLegal(test)) {
            String booleanStr = switchToBoolean(test, data);
            System.out.println(booleanStr);

            Calculator s = new Calculator();
            System.out.println(s.evaluateExpression(booleanStr) != 0);
        }
    }

    private static String switchToBoolean(String source, Map<String, String> data) {
        Pattern pattern = Pattern.compile("([a-zA-Z0-9])+=([a-zA-Z0-9])+");
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            String[] kv = matcher.group().split("=");
            source = matcher.replaceFirst(kv[1].equals(data.get(kv[0])) ? "1" : "0");
            matcher = pattern.matcher(source);
        }

        source = source.toLowerCase().replace("and", "*").replace("or", "+");
        return source;
    }

    private static boolean isLegal(String source) {
        Stack<Character> stack = new Stack<>();
        try {
            for (char c : source.toCharArray()) {
                if (c == '(') {
                    stack.push(c);
                } else if (c == ')') {
                    stack.pop();
                }
            }
        } catch (Exception e) {
            return false;
        }

        return stack.size() == 0;
    }
}

class Calculator {
    public String insetBlanks(String s) {
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(' || s.charAt(i) == ')' ||
                    s.charAt(i) == '+' || s.charAt(i) == '-'
                    || s.charAt(i) == '*' || s.charAt(i) == '/')
                result += " " + s.charAt(i) + " ";
            else
                result += s.charAt(i);
        }
        return result;
    }

    public int evaluateExpression(String expression) {
        Stack<Integer> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();
        expression = insetBlanks(expression);
        String[] tokens = expression.split(" ");
        for (String token : tokens) {
            if (token.length() == 0)
                continue;
            else if (token.charAt(0) == '+' || token.charAt(0) == '-') {
                while (!operatorStack.isEmpty()&&(operatorStack.peek() == '-' || operatorStack.peek() == '+' || operatorStack.peek() == '/' || operatorStack.peek() == '*')) {
                    processAnOperator(operandStack, operatorStack);
                }
                operatorStack.push(token.charAt(0));
            }
            else if (token.charAt(0) == '*' || token.charAt(0) == '/') {
                while (!operatorStack.isEmpty()&&(operatorStack.peek() == '/' || operatorStack.peek() == '*')) {
                    processAnOperator(operandStack, operatorStack);
                }
                operatorStack.push(token.charAt(0));
            }
            else if (token.trim().charAt(0) == '(') {
                operatorStack.push('(');
            } else if (token.trim().charAt(0) == ')') {
                while (operatorStack.peek() != '(') {
                    processAnOperator(operandStack, operatorStack);
                }
                operatorStack.pop();
            } else {
                operandStack.push(Integer.parseInt(token));
            }
        }

        while (!operatorStack.isEmpty()) {
            processAnOperator(operandStack, operatorStack);
        }
        return operandStack.pop();
    }

    public void processAnOperator(Stack<Integer> operandStack, Stack<Character> operatorStack) {
        char op = operatorStack.pop();
        int op1 = operandStack.pop();
        int op2 = operandStack.pop();
        if (op == '+')
            operandStack.push(op1 + op2);
        else if (op == '-')
            operandStack.push(op2 - op1);
        else if (op == '*')
            operandStack.push(op1 * op2);
        else if (op == '/')
            operandStack.push(op2 / op1);
    }
}