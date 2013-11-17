package com.trace.puzzle.littleBall.factory;


import com.trace.puzzle.littleBall.bean.Ball;
import com.trace.puzzle.littleBall.bean.Table;

public class TableFactory {
	public Table getRandomTable(int[] a, int ballsize) {
		Table t = new Table();
		BallFacory bf = new BallFacory();
		int[][] coordinates = new int[a[0]][a[1]];
		t.setCoordinates(coordinates);
		
		
//		List<Ball> balls = new ArrayList<Ball>();
//		t.setBalls(balls);

		int actulNum = 0;
		while (actulNum<ballsize) {
			t.addBall(bf.getBall(coordinates));
			actulNum = t.getBalls().size();
		}
		return t;

	}
	public Table getTable(int[][] coordinates){
		Table t = new Table();
//		BallFacory bf = new BallFacory();
		t.setCoordinates(coordinates);
//		List<Ball> balls = new ArrayList<Ball>();
//		t.setBalls(balls);
		for (int i = 0; i < coordinates.length; i++) {
			for (int j = 0; j < coordinates[0].length; j++) {
				if(coordinates[i][j]==1){
					t.addBall(new Ball(i, j));
				}
			}
		}
		return t;
		
	}
}
