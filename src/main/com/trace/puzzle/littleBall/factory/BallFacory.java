package com.trace.puzzle.littleBall.factory;

import java.util.Random;

import com.trace.puzzle.littleBall.bean.Ball;

public class BallFacory {
	public Ball getBall(int[][] cor){
		if(cor==null||cor.length==0){
			return getBall();
		}
		Random r = new Random();
		int a = r.nextInt(cor.length);
		int b = r.nextInt(cor[0].length);
		Ball ball = new Ball(a,b);
		return ball;
	}
	public Ball getBall(){
		return new Ball();
	}
}
