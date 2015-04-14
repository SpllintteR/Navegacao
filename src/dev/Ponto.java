package dev;

public class Ponto {

	private int	x;
	private int	y;

	public Ponto(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(final int y) {
		this.y = y;
	}
	
	public boolean equals(final int x, final int y) {
		return (getX() == x) && (getY() == y);
	}
}
