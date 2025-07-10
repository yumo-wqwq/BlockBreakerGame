package blockKuzushi;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Ball {
	public static final int SIZE = 8;
	
	private int x, y;
	private int vx, vy;
	
	public static final int INITIAL_SPEED = 8;
	
		
	public Ball() {	
		x = (MainPanel.WIDTH - SIZE) / 2;
		y = (MainPanel.HEIGHT * 2 / 3);
		
		Random rand = new Random();
		vx = (rand.nextBoolean() ? 1 : -1) * (rand.nextInt(3) + 3);
		vy = (rand.nextBoolean() ? 1 : -1) * (rand.nextInt(3) + 3);
	}
	
	public void draw (Graphics g) {
		g.setColor(Color.CYAN);
		g.fillOval(x, y, SIZE, SIZE);		
	}
	
	public void move () {
		x += vx;
		y += vy;
		
		if (x <= 0 || x  >= MainPanel.WIDTH - SIZE/2) {
			bounceX();
		}
		
		if (y <= 0) {
			bounceY();
		}
	}
	
	public void bounceX() {
		vx = -vx;
	}
	
	public void bounceY() {
		vy = -vy;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getSize() {
		return SIZE;
	}
}