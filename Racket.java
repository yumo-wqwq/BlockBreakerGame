package blockKuzushi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Racket {
	
	public static final int WIDTH = 100;
	public static final int HEIGHT = 25;
	private static BufferedImage image;
	
	
	private int centerPos;
	
	public Racket() {
		centerPos = MainPanel.WIDTH / 2;
	}
	public void draw(Graphics g) {
	    if (image != null) {
	        g.drawImage(image, centerPos - WIDTH / 2, MainPanel.HEIGHT - HEIGHT, WIDTH, HEIGHT, null);
	    } else {
	        g.setColor(Color.BLACK);
	        g.fillRect(centerPos - WIDTH / 2, MainPanel.HEIGHT - HEIGHT, WIDTH, HEIGHT);
	    }
	}
	
	public void move(int x) {
		centerPos = x;
		if (centerPos>= 320) {
			centerPos=centerPos-(centerPos-320);
		}
		if (centerPos <= 40) {
			centerPos=centerPos-(centerPos-40);
		}
	}
	
	public boolean intersects(int x, int y, int width, int height) {
		Rectangle thisRect = new Rectangle(centerPos - WIDTH / 2, MainPanel.HEIGHT - HEIGHT, WIDTH, HEIGHT);
		Rectangle targetRect = new Rectangle(x, y, width, height);
		return thisRect.intersects(targetRect);
	}
	
	static {
	    try {
	        image = ImageIO.read(new File("C:/workspace/blockKuzushi/basket_.jpeg"));
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
