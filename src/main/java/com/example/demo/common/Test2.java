package com.example.demo.common;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Test2 {
    public static void main(String[] args) {
        List<String> list = Lists.newArrayList("a","b","c","d");
        System.out.println(rawResult(list));
    }

    public static String rawResult(List<String> postFix) {
        Stack<String> processes = new Stack<>();

        for (String ele : postFix) {
            if (",".equals(ele)) {
                continue;
            }
            if (isLetter(ele)) {
                processes.push(ele);
            } else {
                String rightExpression1 = processes.pop();
                String leftExpression1 = processes.pop();

                String[] rightList = rightExpression1.split(";");
                String[] leftList = leftExpression1.split(";");
                if ("AND".equals(ele)) {
                    List<String> content = new ArrayList<>();
                    for (String leftExpression : leftList) {
                        //rawConents.add("(((A&B&C&D)&E) &F)");
                        for (String rightExpression : rightList) {
                            content.add(leftExpression + "->" + rightExpression);
//                            if (!leftExpression.contains("->") || !rightExpression.contains("->")) {//两端含有字符的话，那么就直接进行拼接
//
//                            }
                        }
                    }

//
//                    String UPPER_CASE_LETTER_ARR_ran = UPPER_CASE_LETTER_ARR[number1++];
//                    for (String leftItem : leftList) {
//                        finished.add(leftItem + "->" + UPPER_CASE_LETTER_ARR_ran);
//                    }
//                    for (String rightItem : rightList) {
//                        content.add(UPPER_CASE_LETTER_ARR_ran + "->" + rightItem);
//                    }
                    processes.push(StringUtils.join(content, ';'));
                } else {
                    List<String> content = new ArrayList<>();
                    List<String> letterGroup = new ArrayList<>();
                    for (String leftExpression : leftList) {
                        if (leftExpression.toCharArray().length == 1 && isLetter(leftExpression)) {
                            letterGroup.add(leftExpression);
                        } else {
                            content.add(leftExpression);
                        }
                    }
                    for (String rightExpression : rightList) {
                        if (rightExpression.toCharArray().length == 1 && isLetter(rightExpression)) {
                            letterGroup.add(rightExpression);
                        } else {
                            content.add(rightExpression);
                        }
                    }
                    if (!letterGroup.isEmpty()) {
                        content.add(StringUtils.join(letterGroup, ","));
                    }
                    processes.push(StringUtils.join(content, ';'));

//                    for (String leftExpression : leftList) {
//                        //rawConents.add("(((A&B&C&D)&E) &F)");
//                        for (String rightExpression : rightList) {
//                            content.add(leftExpression + "->" + rightExpression);
////                            if (!leftExpression.contains("->") || !rightExpression.contains("->")) {//两端含有字符的话，那么就直接进行拼接
////
////                            }
//                        }
//                    }
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String l : processes) {
            for (String m : l.split(";")) {
                stringBuilder.append(m + "->END;");
            }
        }
//        finished.addAll(processes);
//        return org.apache.commons.lang.StringUtils.join(processes, ".");
        return stringBuilder.toString();
    }

    private static boolean isLetter(String letter) {
        return !("AND".equals(letter) || "OR".equals(letter));
    }
}
