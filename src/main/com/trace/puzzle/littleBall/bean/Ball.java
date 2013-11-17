package com.trace.puzzle.littleBall.bean;

import java.util.ArrayList;
import java.util.List;

import com.trace.puzzle.littleBall.enumbean.Direction;

public class Ball implements Comparable<Ball> {
	private int[] position;
	private int[] unableX;
	private int[] unableY;
	private int isStatic;
	private float distance;
//	private Table table;
	private List<Direction> triedDir;

	public Ball(int a, int b) {
		position = new int[2];
		this.position[0] = a;
		this.position[1] = b;
		triedDir = new ArrayList<Direction>();
		// for (int i = 0; i < 3; i++) {
		// unableX[i]=a+i-1;
		// unableY[i]=b+i-1;
		// }
	}

	public List<Direction> getTriedDir() {
		return triedDir;
	}

	public void setTriedDir(List<Direction> triedDir) {
		this.triedDir = triedDir;
	}

	public Ball() {
	}

	public int[] getPosition() {
		return position;
	}

	public float[] getFloatPosition() {
		return new float[] { (float) position[0], (float) position[1] };
	}

	public void setPosition(int[] position) {
		this.position = position;
	}

	public int getIsStatic() {
		return isStatic;
	}

	public void setIsStatic(int isStatic) {
		this.isStatic = isStatic;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

//	public Table getTable() {
//		return table;
//	}
//
//	public void setTable(Table table) {
//		this.table = table;
//	}
//
//	public boolean addToTable() {
//		return table.addBall(this);
//	}

	public int[] getUnableX() {
		return unableX;
	}

	public void setUnableX(int[] unableX) {
		this.unableX = unableX;
	}

	public int[] getUnableY() {
		return unableY;
	}

	public void setUnableY(int[] unableY) {
		this.unableY = unableY;
	}

	public int move(Direction direct) {
		if (this.isStatic == 0)
			return 0;
		this.isStatic = 1;
		switch (direct) {
		case UP:
			this.position[0]--;
			break;
		case DOWN:
			this.position[0]++;
			break;
		case LEFT:
			this.position[1]--;
			break;
		case RIGHT:
			this.position[1]++;
			break;

		default:
			break;
		}
		return 1;

	}

	public Direction getLastMovedDir() {
		Direction laDirection = null;
		if (triedDir != null) {
			int size = triedDir.size();
			laDirection = triedDir.get(size - 1);
		}
		return laDirection;

	}
	public Direction getNextMovedDir() {
		for(Direction dir : Direction.values()){
			if(!triedDir.contains(dir))return dir;
		}
		return null;

	}

	public void Stop() {
		this.isStatic = 0;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + position[0];
		result = 37 * result + position[1];
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Ball))
			return false;
		Ball ball = (Ball) obj;
		return (position[0] == ball.getPosition()[0])
				&& (position[1] == ball.getPosition()[1]);
	};

	public int compareTo(Ball ball) {
		int result = 0;
		int compareY = position[0] - ball.getPosition()[0];
		int compareX = position[1] - ball.getPosition()[1];
		if (compareY == 0) {
			result = compareX;
		} else {
			result = compareY;
		}
		return result;
	}

	public String toString() {
		return ("该球的坐标是" + position[0] + "," + position[1]);
	}
}
