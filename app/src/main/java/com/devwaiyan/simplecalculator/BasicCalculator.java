package com.devwaiyan.simplecalculator;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BasicCalculator implements BasicCalculable {
    private double result;
    private List<String> list; // To make calculable values list

    public BasicCalculator() {
        this.result = 0.0;
        list = new ArrayList<>();
    }

    public String calculate(String values) throws Exception {
        String multiplier; // For multiplication
        String multiplicand; // For multiplication
        String dividend; // For division
        String divisor; // For division

        list.clear(); // At first, make empty list
        String number = "";
        char[] ch;
        values = values.trim().replace(" ", "");
        ch = values.toCharArray();


        //  When element is operator =>
        //  Step 1: Number which exist before this operator is added to list.
        //  Step 2: This current operator is added to list.
        //  Step 3: Set number variable to be empty.
        for (int i = 0; i < ch.length; i++) {
            switch (ch[i]) {
                case '-':
                    if (i == 0) {    // If the first place is MINUS, must assume as first number is ZERO.
                        list.add("0");
                        list.add(String.valueOf(ch[i]));
                        number = "";
                    } else {
                        list.add(number);
                        list.add(String.valueOf(ch[i]));
                        number = "";
                    }
                    break;
                case '+':
                case '\u00D7':
                case '\u00F7':
                    list.add(number);
                    list.add(String.valueOf(ch[i]));
                    number = "";
                    break;
                default:
                    number = number.concat(String.valueOf(ch[i]));
            }
        }

        list.add(number); // Last Number Added // IMPORTANT


        // Starts Calculation Here
        // High priority operation
        for (int i = 1; i < list.size(); i++) {
            switch (list.get(i).charAt(0)) {
                case '\u00D7':
                    try {
                        multiplier = list.get(i - 1); // Set multiplier
                        multiplicand = list.get(++i); // Set multiplicand
                        this.result = findProduct(multiplier, multiplicand);

                        list.remove(i); // Remove multiplicand from number list
                        list.add(i, String.valueOf(this.result)); // Add the result product as a member of number list
                        list.remove(i - 1); // Remove multiplication sign from number list
                        list.remove(i - 2); // Remove multiplier from number list
                        i = 0; // Set index to ZERO to be return loop for modified list.
                    } catch (NumberFormatException e) {
                        throw new Exception(); // User Fault Error
                    }
                    break;

                case '\u00F7':
                    try {
                        dividend = list.get(i - 1); // Set dividend
                        divisor = list.get(++i); // Set divisor
                        this.result = findQuotient(dividend, divisor);

                        list.remove(i); // Remove divisor from number list
                        list.add(i, String.valueOf(this.result)); // Add the result quotient as a member of number list
                        list.remove(i - 1); // Remove division sign from number list
                        list.remove(i - 2); // Remove dividend from number list
                        i = 0; // Set index to ZERO to be return loop for modified list.
                    } catch (NumberFormatException e) {
                        throw new Exception(); // User Fault Error
                    }
                    break;
            }
        }


        // Normal priority operation
        // At first, let's suppose result
        try {
            this.result = Double.parseDouble(list.get(0)); // Let's suppose as first number is result
        } catch (NumberFormatException e) {
            throw new Exception(); // User Fault Error
        }


        for (int i = 1; i < list.size(); i++) {
            switch (list.get(i).charAt(0)) {
                case '+':
                    this.result = findSum(String.valueOf(this.result), list.get(++i));
                    break;
                case '-':
                    this.result = findDifference(String.valueOf(this.result), list.get(++i));
                    break;
            }
        }
        // End Calculation Here

        return String.valueOf(this.result); // Return Final Result To Activity
    }

    @Override
    public double findSum(String firstValue, String secondValue) {
        double result = 0.0;
        try {
            double firstNum = Double.parseDouble(firstValue);
            double secondNum = Double.parseDouble(secondValue);
            result = firstNum + secondNum;
        } catch (NumberFormatException e) {
            throw new NumberFormatException();
        }
        return result; // Return current result
    }

    @Override
    public double findDifference(String firstValue, String secondValue) {
        double result = 0.0;
        try {
            double firstNum = Double.parseDouble(firstValue);
            double secondNum = Double.parseDouble(secondValue);
            result = firstNum - secondNum;
        } catch (NumberFormatException e) {
            throw new NumberFormatException();
        }
        return result; // Return current result
    }

    @Override
    public double findProduct(String firstValue, String secondValue) {
        double result = 0.0;
        try {
            double firstNum = Double.parseDouble(firstValue);
            double secondNum = Double.parseDouble(secondValue);
            result = firstNum * secondNum;
        } catch (NumberFormatException e) {
            throw new NumberFormatException();
        }
        return result; // Return current result
    }

    @Override
    public double findQuotient(String firstValue, String secondValue) {
        double result = 0.0;
        try {
            double firstNum = Double.parseDouble(firstValue);
            double secondNum = Double.parseDouble(secondValue);
            result = firstNum / secondNum;
        } catch (NumberFormatException e) {
            throw new NumberFormatException();
        }
        return result; // Return current result
    }


}
