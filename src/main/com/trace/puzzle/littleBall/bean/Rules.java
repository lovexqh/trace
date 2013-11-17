package com.trace.puzzle.littleBall.bean;

public class Rules {
	private Table table;
	private Ball ball;

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public Ball getBall() {
		return ball;
	}

	public void setBall(Ball ball) {
		this.ball = ball;
	}
	//任意一个球不能再其他所有球之外
	//所有任意两个球中，至少有一对在一条直线上
	//任意两个球不能在一起
	

}
