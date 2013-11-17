package com.trace.puzzle.littleBall;

import java.util.List;
import java.util.Stack;

import com.trace.puzzle.littleBall.bean.Ball;
import com.trace.puzzle.littleBall.bean.Table;
import com.trace.puzzle.littleBall.factory.TableFactory;

public class RunEngine {
	// private TableFactory tf;
	// private Table table;

	public boolean addMove(Table table) {
		if (table.getMovingNum() == 0) {

		}
		return false;
	}

	public static void main(String[] args) {
		walk();
	}

	public static void test1() {
		TableFactory tf = new TableFactory();
		// Table table = tf.getRandomTable(new int[] { 5, 5 }, 10);
		int[][] coordinates = { { 1, 0, 0, 0, 0 }, { 0, 0, 1, 0, 1 },
				{ 0, 1, 0, 1, 1 }, { 0, 0, 1, 0, 0 }, { 1, 0, 1, 1, 0 } };
		Table table = tf.getTable(coordinates);
		table.printTable();
		// List<Ball> balls = table.getDistanceBalls();
		List<Ball> balls = table.getBalls();
		int moveCount = 0;
		for (int i = 0; i < balls.size(); i++) {
			// if(table.move(balls.get(i),Direction.DOWN)){
			if (table.move(balls.get(i))) {
				System.out.println("第" + (++moveCount) + "次移动后的样子");
				System.out.println(table);
				if (moveCount == 3) {
					table.reverse(table.getLastMovedBall());
					System.out.println("恢复后的样子");
					System.out.println(table);
					break;
				}
				balls = table.getBalls();
				i = 0;
				// break;
			}
		}
		System.out.println("移动次数" + moveCount);
		// float[] center = table.getBallsCenter();
		// table.printTable();
		// System.out.println(center);
	}

	public static void walk() {
		int moveCount = 0;
		TableFactory tf = new TableFactory();
		Stack<Table> tableStep = new Stack<Table>();
		int[][] coordinates = { { 1, 0, 0, 0, 0 }, { 0, 0, 1, 0, 1 },
				{ 0, 1, 0, 1, 1 }, { 0, 0, 1, 0, 0 }, { 1, 0, 1, 1, 0 } };
		Table table = tf.getTable(coordinates);
		List<Ball> balls = table.getBalls();
		int ballNum = balls.size();
		System.out.println(table);
		// Direction dir = ball.getNextMovedDir();
		do {
			for (int i = 0; i < ballNum; i++) {
				balls = table.getBalls();
				Ball tmpball = balls.get(i);
				if (table.move(tmpball)) {
					tableStep.push(table);
					ballNum = table.getBalls().size();
					i = 0;
					System.out.println("第" + (++moveCount) + "次移动后的样子");
					System.out.println(table);
				}
				else if(!table.movable(tmpball)){
					System.out.println(table);
					continue;
				}
				

			}
		} while (!tableStep.isEmpty());
		System.out.println("移动次数" + moveCount);
	}

}
