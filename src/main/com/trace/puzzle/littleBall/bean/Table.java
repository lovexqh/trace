package com.trace.puzzle.littleBall.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import com.trace.puzzle.littleBall.enumbean.Direction;

public class Table {
	private int[][] coordinates;
	private List<Ball> balls = new ArrayList<Ball>();
	private Ball lastMovedBall;
	Stack<Ball> tableStep = new Stack<Ball>();

	public Ball getLastMovedBall() {
		return lastMovedBall;
	}

	public void setLastMovedBall(Ball lastMovedBall) {
		this.lastMovedBall = lastMovedBall;
	}

	public int[][] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(int[][] coordinates) {
		this.coordinates = coordinates;
	}

	public List<Ball> getBalls() {
		return balls;
	}

	public void setBalls(List<Ball> balls) {
		this.balls = balls;
	}

	public boolean hasBall(int[] a) {
		if (balls != null && balls.size() > 0) {
			for (int i = 0; i < balls.size(); i++) {
				if (Arrays.equals(balls.get(i).getPosition(), a))
					return true;
			}
		}
		return false;
	}

	public Ball getBall(int[] a) {
		if (balls != null && balls.size() > 0) {
			for (int i = 0; i < balls.size(); i++) {
				if (Arrays.equals(balls.get(i).getPosition(), a))
					return balls.get(i);
			}
		}
		return null;
	}

	public boolean addBall(Ball ball) {
		if (hasBall(ball.getPosition()))
			return false;
//		ball.setTable(this);
		coordinates[ball.getPosition()[0]][ball.getPosition()[1]] = 1;
		return this.balls.add(ball);

	}

	public boolean removeBall(Ball ball) {
		if (!hasBall(ball.getPosition()))
			return false;
		coordinates[ball.getPosition()[0]][ball.getPosition()[1]] = 0;
		return this.balls.remove(ball);

	}

	public void printTable() {
		if (coordinates == null || coordinates.length == 0) {
			System.out.print("棋盘为空");
			return;
		}
		for (int i = 0; i < coordinates.length; i++) {
			for (int j = 0; j < coordinates[i].length; j++) {
				if(coordinates[i][j]==0){
					
					System.out.print("O,");
				}
				if(coordinates[i][j]==1){
					
					System.out.print("X,");
				}
			}
			System.out.println();
		}
	}

	public float[] getBallsCenter() {
		List<Integer> hengzb = new ArrayList<Integer>();
		List<Integer> zongzb = new ArrayList<Integer>();
		if (balls != null && balls.size() >= 2) {
			for (int i = 0; i < balls.size(); i++) {
				hengzb.add(balls.get(i).getPosition()[0]);
				zongzb.add(balls.get(i).getPosition()[0]);
			}
		}
		int maxa = Collections.max(hengzb);
		int mina = Collections.min(hengzb);
		int maxb = Collections.max(zongzb);
		int minb = Collections.min(zongzb);
		return new float[] { (float) (maxa + mina) / 2,
				(float) (maxb + minb) / 2 };
	}

	public float getDistance(float[] center, float[] dot) {
		double a = Math.pow((center[0] - dot[0]), 2)
				+ Math.pow((center[1] - dot[1]), 2);
		return (float) Math.sqrt(a);
	}

	public int getMovingNum() {
		int moving = 0;
		if (balls != null && balls.size() != 0) {
			for (int i = 0; i < balls.size(); i++) {
				if (balls.get(i).getIsStatic() == 0)
					moving++;
			}
		}
		return moving;
	}

	public List<Line> getMovableLines() {
		List<Line> lines = new ArrayList<Line>();
		List<Ball> balls = this.getBalls();
		int ballsNum = balls.size();
		int maxX = this.coordinates.length;
		int maxY = this.coordinates[0].length;
		for (int i = 0; i < maxY; i++) {
			int lineBallNum = 0;
			for (int j = 0; j < ballsNum; j++) {
				if (balls.get(j).getPosition()[1] == i)
					lineBallNum++;
			}
			if (lineBallNum >= 2) {
				lines.add(new Line("Y", i));
			}
		}
		for (int i = 0; i < maxX; i++) {
			int lineBallNum = 0;
			for (int j = 0; j < ballsNum; j++) {
				if (balls.get(j).getPosition()[0] == i)
					lineBallNum++;
			}
			if (lineBallNum >= 2) {
				lines.add(new Line("X", i));
			}
		}
		return lines;

	}

	public List<Ball> getDistanceBalls() {
		List<Ball> tmpballs = new ArrayList<Ball>();
		float center[] = this.getBallsCenter();
		float tmp = 0.0f;
		for (int i = 0; i < balls.size(); i++) {
			Ball ball = this.balls.get(i);
			float distance = getDistance(center, ball.getFloatPosition());
			if (distance >= tmp) {
				ball.setDistance(distance);
				tmp = distance;
				tmpballs.clear();
				tmpballs.add(ball);
			}
		}
		return tmpballs;
	}

	public boolean hasUnableBall() {
		List<Ball> balls = getDistanceBalls();
		for (int i = 0; i < balls.size(); i++) {
			Ball ball = balls.get(i);
			if (movable(ball)) {
				return false;
			}
		}
		return true;

	}

	//
//	public boolean movable(Ball ball) {
//		int[] position = ball.getPosition();
//		int[] unableX = ball.getUnableX();
//		int[] unableY = ball.getUnableY();
//		int count = 0;
//		for (int j = 0; j < coordinates.length; j++) {
//			if (Arrays.binarySearch(unableY, j) < 0) {
//				count += coordinates[position[0]][j];
//			}
//		}
//		for (int j = 0; j < coordinates[0].length; j++) {
//			if (Arrays.binarySearch(unableX, j) < 0) {
//				count += coordinates[j][position[1]];
//			}
//		}
//		if (count > 0) {
//			return true;
//		}
//		return false;
//	}

	public boolean movable(Ball ball, Direction dir) {
		int[] position = ball.getPosition();
		// int count = 0;
		// boolean flag = true;
		switch (dir) {
		case UP:
			if (position[0] - 2 >= 0) {
				for (int i = position[0]; i > 1; i--) {
					boolean f = (coordinates[i][position[1]] + coordinates[i - 1][position[1]]) == 2;
					if (f)
						return false;
				}
			} else {
				return false;
			}
			break;
		case DOWN:
			if (position[0] + 2 < coordinates[0].length) {
				for (int i = position[0]; i < coordinates[0].length - 1; i++) {
					boolean f = (coordinates[i][position[1]] + coordinates[i + 1][position[1]]) == 2;
					if (f)
						return false;
				}
			} else {
				return false;
			}
			break;
		case LEFT:
			if (position[1] - 2 >= 0) {
				for (int i = position[1]; i > 1; i--) {
					boolean f = (coordinates[position[0]][i] + coordinates[position[0]][i - 1]) == 2;
					if (f)
						return false;
				}
			} else {
				return false;
			}
			break;
		case RIGHT:
			if (position[1] + 2 < coordinates[0].length) {
				for (int i = position[1]; i < coordinates.length - 1; i++) {
					boolean f = (coordinates[position[0]][i] + coordinates[position[0]][i + 1]) == 2;
					if (f)
						return false;
				}
			} else {
				return false;
			}
			break;
		}
		return true;
	}

	public boolean move(Ball ball, Direction dir) {
		int[] position = ball.getPosition();
		int count = 0;
		switch (dir) {
		case UP:
			if (movable(ball, dir)) {
				for (int i = position[0] - 2; i >= 0; i--) {
					if (coordinates[i][position[1]] == 1) {
						coordinates[i + 1][position[1]] = 1;
						coordinates[i][position[1]] = 0;
						count++;
					}
				}
				if (count > 0) {
					coordinates[position[0]][position[1]] = 0;
				}
			}
			break;
		case DOWN:
			if (movable(ball, dir)) {
				for (int i = position[0] + 2; i < coordinates[0].length; i++) {
					if (coordinates[i][position[1]] == 1) {
						coordinates[i - 1][position[1]] = 1;
						coordinates[i][position[1]] = 0;
						count++;
					}
				}
				if (count > 0) {
					coordinates[position[0]][position[1]] = 0;
				}
			}
			break;
		case LEFT:
			if (movable(ball, dir)) {
				for (int i = position[1] - 2; i > 0; i--) {
					if (coordinates[position[0]][i] == 1) {
						coordinates[position[0]][i + 1] = 1;
						coordinates[position[0]][i] = 0;
						count++;
					}
				}
				if (count > 0) {
					coordinates[position[0]][position[1]] = 0;
				}
			}
			break;
		case RIGHT:
			if (movable(ball, dir)) {
				for (int i = position[1] + 2; i < coordinates.length; i++) {
					if (coordinates[position[0]][i] == 1) {
						coordinates[position[0]][i - 1] = 1;
						coordinates[position[0]][i] = 0;
						count++;
					}
				}
				if (count > 0) {
					coordinates[position[0]][position[1]] = 0;
				}
			}
			break;
		}
		// 将该方向标记，表示已经尝试过
		ball.getTriedDir().add(dir);
		if (count > 0) {
			resetTable();
			setLastMovedBall(ball);
			return true;
		} else {
			return false;
		}
	}

	public void resetTable() {
		balls.clear();
		for (int i = 0; i < coordinates.length; i++) {
			for (int j = 0; j < coordinates[0].length; j++) {
				if (coordinates[i][j] > 0) {
					Ball ball = new Ball(i, j);
					balls.add(ball);
				}
			}
		}
	}

	public boolean move(Ball ball) {
		boolean up = false;
		boolean down = false;
		boolean left = false;
		boolean right = false;
		up = move(ball, Direction.UP);
		if (!up) {
			left = move(ball, Direction.LEFT);
			if (!left) {
				down = move(ball, Direction.DOWN);
				if (!down) {
					right = move(ball, Direction.RIGHT);
				}
			}
		}
		return up || down || left || right;
	}
	public boolean movable(Ball ball) {
		boolean up = false;
		boolean down = false;
		boolean left = false;
		boolean right = false;
		up = movable(ball, Direction.UP);
		if (!up) {
			left = movable(ball, Direction.LEFT);
			if (!left) {
				down = movable(ball, Direction.DOWN);
				if (!down) {
					right = movable(ball, Direction.RIGHT);
				}
			}
		}
		return up || down || left || right;
	}

	// 该方法为move(Ball ball, Direction dir)的逆方法，
	// 调用一次move，一次reverse，该表应该不变
	public boolean reverse(Ball ball) {
		int[] position = ball.getPosition();
		Direction dir = ball.getLastMovedDir();
		int count = 0;
		switch (dir) {
		case UP:
			for (int i = position[0] - 1; i >= 1; i--) {
				if (coordinates[i][position[1]] == 1) {
					coordinates[i - 1][position[1]] = 1;
					coordinates[i][position[1]] = 0;
					i--;
					count++;
				}
			}
			coordinates[position[0]][position[1]] = 1;
			resetTable();
			break;
		case DOWN:
			for (int i = position[0] + 1; i < coordinates[0].length; i++) {
				if (coordinates[i][position[1]] == 1) {
					coordinates[i + 1][position[1]] = 1;
					coordinates[i][position[1]] = 0;
					i++;
					count++;
				}
			}
			coordinates[position[0]][position[1]] = 1;
			resetTable();
			break;
		case LEFT:
			for (int i = position[1] - 1; i > 0; i--) {
				if (coordinates[position[0]][i] == 1) {
					coordinates[position[0]][i - 1] = 1;
					coordinates[position[0]][i] = 0;
					i--;
					count++;
				}
			}
			coordinates[position[0]][position[1]] = 0;
			resetTable();
			break;
		case RIGHT:
			for (int i = position[1] + 1; i < coordinates.length; i++) {
				if (coordinates[position[0]][i] == 1) {
					coordinates[position[0]][i + 1] = 1;
					coordinates[position[0]][i] = 0;
					i++;
					count++;
				}
			}
			coordinates[position[0]][position[1]] = 0;
			resetTable();
			break;
		}
		return count > 0;
	}

	public String toString() {

		String s = "";
		if (lastMovedBall != null) {
			s += "移动的球的坐标是{";
			s += lastMovedBall.getPosition()[0] + 1 + ",";
			s += lastMovedBall.getPosition()[1] + 1 + "}";
			s += "移动的方向是";
			s += "" + lastMovedBall.getLastMovedDir().getValue();
		} else {
			s += "原始的棋盘";
		}
		printTable();
		return s;
	}
}
