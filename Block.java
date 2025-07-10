package blockKuzushi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Block {
	public static final int WIDTH = 40;
	public static final int HEIGHT = 30;
	
	private int x, y;	
	private boolean isPainted;
	private static BufferedImage image;
	
	public Block(int x, int y) {
		this.x = x;
		this.y = y;
		isPainted = true;
	}
	
	public void draw (Graphics g) {
		if (image != null) {
	        g.drawImage(image, x, y, WIDTH, HEIGHT, null);
	    } else {
	        g.setColor(Color.CYAN);
	        g.fillRect(x, y, WIDTH, HEIGHT);
	        g.setColor(Color.BLACK);
	        g.drawRect(x, y, WIDTH, HEIGHT);
	    }
	}
	
	public void delete() {
		isPainted = false;
	}
	
	public boolean isPainted() {
		return isPainted;
	}

	public boolean intersects(int ballX, int ballY, int ballW, int ballH) {
        Rectangle blockRect = new Rectangle(x, y, WIDTH, HEIGHT);
        Rectangle ballRect = new Rectangle(ballX, ballY, ballW, ballH);
        return blockRect.intersects(ballRect);
    }
	
	public String getSide(int ballX, int ballY, int ballSize) {
        Rectangle blockRect = new Rectangle(x, y, WIDTH, HEIGHT);
        Rectangle ballRect = new Rectangle(ballX, ballY, ballSize, ballSize);
        Rectangle intersection = blockRect.intersection(ballRect);

        if (intersection.isEmpty()) {
            return "unknown";
        }

        if (intersection.width > intersection.height) {
            // 上下方項
            return (ballY < y) ? "top" : "bottom";
        } else {
            // 左右方向
            return (ballX < x) ? "left" : "right";
        }
    }

    static {
        try {
            image = ImageIO.read(new File("C:/workspace/blockKuzushi/apple.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
