package com.trace.puzzle;
import java.util.Stack;

public class MiGong {
	int migong_juzhen[][];
	boolean visited[][] = new boolean[9][10];
	Stack<String> migong = new Stack<String>();
	final int finished_x = 7;
	final int finished_y = 8;
	boolean finished = false;

	public MiGong() {
		initMiGong_juzhen();
		tack();
	}

	private void initMiGong_juzhen() {
		migong_juzhen = new int[][] { { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
				{ 1, 0, 0, 1, 0, 0, 0, 1, 0, 1 },
				{ 1, 0, 0, 1, 0, 0, 0, 1, 0, 1 },
				{ 1, 0, 0, 0, 0, 1, 1, 0, 0, 1 },
				{ 1, 0, 1, 1, 1, 0, 0, 0, 0, 1 },
				{ 1, 0, 1, 0, 0, 0, 1, 0, 0, 1 },
				{ 1, 0, 0, 1, 1, 0, 1, 1, 0, 1 },
				{ 1, 1, 0, 0, 0, 0, 0, 0, 0, 1 },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, };
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 10; j++)
				visited[i][j] = false;

		migong.push("1,1");
		visited[1][1] = true;
	}

	public void tack() {
		while (!finished) {
			if (!migong.isEmpty()) {
				String temp_p = migong.peek();
				System.out.println("temp_p" + temp_p);
				String x_y[] = temp_p.split(",");
				int t_x = Integer.parseInt(x_y[0]);
				int t_y = Integer.parseInt(x_y[1]);
				go(t_x, t_y);
			} else {
				finished = true;
				System.out.println("没有出路");
			}
		}
		System.out.println("出路:" + migong.toString());
	}

	private void go(int t_x, int t_y) {
		int wall_count = 0;
		int temp_count = 0;
		int n_x, n_y;
		// 东
		n_x = t_x;
		n_y = t_y + 1;
		if (!visited[n_x][n_y]) {
			if (!isWall(n_x, n_y)) {
				push_S(n_x, n_y);
				return;
			} else {
				wall_count++;
				temp_count++;
			}
		} else
			temp_count++;
		// 南
		n_x = t_x + 1;
		n_y = t_y;
		if (!visited[n_x][n_y]) {
			if (!isWall(n_x, n_y)) {
				push_S(n_x, n_y);
				return;
			} else {
				wall_count++;
				temp_count++;
			}
		} else
			temp_count++;
		// 西
		n_x = t_x;
		n_y = t_y - 1;
		if (!visited[n_x][n_y]) {
			if (!isWall(n_x, n_y)) {
				push_S(n_x, n_y);
				return;
			} else {
				wall_count++;
				temp_count++;
			}
		} else
			temp_count++;
		// 北
		n_x = t_x - 1;
		n_y = t_y;
		if (!visited[n_x][n_y]) {
			if (!isWall(n_x, n_y)) {
				push_S(n_x, n_y);
				return;
			} else {
				wall_count++;
				temp_count++;
			}
		} else
			temp_count++;

		if (wall_count >= 3) {
			migong.pop();
		}
		if (temp_count >= 4) {
			migong.pop();
		}
	}

	private void push_S(int t_x, int t_y) {
		migong.push(t_x + "," + t_y);
		visited[t_x][t_y] = true;
		if (t_x == finished_x && t_y == finished_y) {
			finished = true;
		}
	}

	private boolean isWall(int t_x, int t_y) {
		if (migong_juzhen[t_x][t_y] == 1)
			return true;
		else
			return false;
	}

	public static void main(String args[]) {
		new MiGong();
	}
}