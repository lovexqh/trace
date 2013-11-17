package com.trace.puzzle.littleBall.enumbean;

public enum Direction {
	UP("向上"), LEFT("向左"), DOWN("向下"), RIGHT("向右");
	private String value;

	private Direction() {
	}

	private Direction(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
