package com.trace.classloader.example;

import com.trace.classloader.ICalculator;


public class CalculatorAdvanced implements ICalculator {

	public String calculate(String expression) {
		return "Result is " + expression;
	}

	public String getVersion() {
		return "2.0";
	}

}
