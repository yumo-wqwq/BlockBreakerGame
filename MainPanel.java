package blockKuzushi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MainPanel extends JPanel implements Runnable, MouseMotionListener{
	
	public static final int WIDTH = 360;
	public static final int HEIGHT = 480;
	
	private static final int NUM_BLOCK_ROW = 7;
	private static final int NUM_BLOCK_COL = 6;
	
	private static final int NUM_BLOCK = NUM_BLOCK_ROW * NUM_BLOCK_COL;
		
	private Racket racket;
	private Ball ball;
	private JButton restartButton;
	private JButton nextButton;
	private Block[] block;
	private BufferedImage backgroundImage;
	
	private SoundPlayer bgmPlayer;
	private SoundPlayer bgmPlayer2;
	
	private Thread gameThread;
	private boolean isPlaying = false;
	private boolean isGameOver = false;
	private boolean isGameClear = false;
	
	private int kosuu = 0;
	private long startTime = 0;
	
	private void resetGame() {
	    kosuu = 0;
	    
	    isGameOver = false;
	    isGameClear = false;
	    isPlaying = true;

	    ball = new Ball();
	    racket = new Racket(); 

	    for (int i = 0; i < NUM_BLOCK_ROW; i++) {
	        for (int j = 0; j < NUM_BLOCK_COL; j++) {
	            int x = j * Block.WIDTH + Block.WIDTH + 20;
	            int y = i * Block.HEIGHT + Block.HEIGHT + 40;
	            block[i * NUM_BLOCK_COL + j] = new Block(x, y);
	        }
	    }
	    startTime = System.currentTimeMillis();
	    bgmPlayer.restart();
	    repaint();
	}
	
	public MainPanel(){
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setLayout(null);
		addMouseMotionListener(this);
		
		bgmPlayer = new SoundPlayer("C:/workspace/blockKuzushi/アホの子.wav");
		bgmPlayer2 = new SoundPlayer("C:/workspace/blockKuzushi/appleat.wav");
		
		bgmPlayer.playLoop();
		
		racket = new Racket();
		ball = new Ball();
		block = new Block[NUM_BLOCK];
		
		for (int i = 0 ; i<NUM_BLOCK_ROW ;i++ ) {
			for (int j = 0; j<NUM_BLOCK_COL; j++ ) {
				int x = j * Block.WIDTH + Block.WIDTH + 20;
				int y = i * Block.HEIGHT + Block.HEIGHT+ 40;
				block[i * NUM_BLOCK_COL + j] = new Block(x,y);
			}
		}
		
		restartButton = new JButton("再スタート"); //リスタートボタン
		restartButton.setBounds((WIDTH - 120) / 2, HEIGHT / 2 + 50, 120, 30);
		restartButton.setFocusable(false);
		restartButton.setVisible(false);
		
		restartButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        resetGame();
		        restartButton.setVisible(false);
		    }
		});
		this.add(restartButton);		

		nextButton = new JButton("もう一回！！"); //ネクストボタン
		nextButton.setBounds((WIDTH - 120) / 2, HEIGHT / 2 + 50, 120, 30);
		nextButton.setFocusable(false);
		nextButton.setVisible(false);
		
		nextButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        resetGame();
		        nextButton.setVisible(false);
		    }
		});
		this.add(nextButton);
		
		
		JButton startButton = new JButton("スタート");  //スタートボタン
		startButton.setBounds((WIDTH - 100) / 2, HEIGHT - 60, 100, 30);
		startButton.setFocusable(false);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!isPlaying) {
					isPlaying = true;
					startTime = System.currentTimeMillis();
					startButton.setVisible(false);
				}
			}
		});
		this.add(startButton);
		
		try {
		    backgroundImage = ImageIO.read(new File("C:/workspace/blockKuzushi/tree_keyaki.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void run() {
		while(true) {
			
			if (isPlaying) {
				ball.move();
				
				if (ball.getY() > HEIGHT) {
			        isPlaying = false;
			        isGameOver = true;
			        restartButton.setVisible(true);
			        bgmPlayer.stop();
			    }
			}
			
			for (int i = 0; i < NUM_BLOCK; i++) {
				
				if(!block[i].isPainted()) {
					continue;
				}
				
				if(block[i].intersects(ball.getX(),ball.getY(),ball.getSize(),ball.getSize())) {
					String side = block[i].getSide(ball.getX(), ball.getY(), ball.getSize());

	                if (side.equals("top") || side.equals("bottom")) {
	                    ball.bounceY();
	                    bgmPlayer2.start();
	                    
	                } else if (side.equals("left") || side.equals("right")) {
	                    ball.bounceX();
	                    bgmPlayer2.start();
	                }
	                
	                block[i].delete(); 
	                kosuu += 1;
	                
	                boolean allCleared = true;
	                for (int j = 0; j < NUM_BLOCK; j++) {
	                    if (block[j].isPainted()) {
	                        allCleared = false;
	                        break;
	                    }
	                }
	                
	                if (allCleared) {    //クリア後
	                    isPlaying = false;
	                    isGameClear = true;
	                    bgmPlayer.stop(); 
	                    nextButton.setVisible(true);
	                }
				}
			}
			
			if(racket.intersects(ball.getX(), ball.getY(), ball.getSize(), ball.getSize())) {
				ball.bounceY();
			}
			
			repaint();
			
			try {
				Thread.sleep(20);				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (backgroundImage != null) {
	        g.drawImage(backgroundImage, 0, 0, WIDTH, HEIGHT, null);
	    } else {
	        g.setColor(Color.BLACK);
	        g.fillRect(0, 0, WIDTH, HEIGHT);
	    }
	        
	    if (isPlaying) {
	        long elapsedMillis = System.currentTimeMillis() - startTime;
	        long seconds = (elapsedMillis / 1000) % 60;
	        long minutes = (elapsedMillis / 1000) / 60;
	        String timeStr = String.format("Time: %02d:%02d", minutes, seconds);
	        g.drawString(timeStr, WIDTH  / 8 - 20 , HEIGHT / 8 - 20); 
	        
	        g.setColor(Color.BLACK);
		    g.drawString("りんご: " + kosuu, WIDTH  / 8 - 20 , HEIGHT / 8);
	    }
		
		if (isGameOver) {
		    g.setColor(Color.RED);
		    g.drawString("Game Over", WIDTH  / 8 - 20 , HEIGHT / 8 - 20);
		    g.drawString("りんご: " + kosuu, WIDTH  / 8 - 20 , HEIGHT / 8 );
		}
		
		if (isGameClear) {
		    g.setColor(Color.WHITE);
		    g.drawString("Game Clear!!", WIDTH / 2 - 40, HEIGHT / 2);
		    g.drawString("りんご: " + kosuu, WIDTH / 2 - 30, HEIGHT / 2 + 20);
		}
		
		racket.draw(g);	
		ball.draw(g);
		
		for (int i = 0 ; i < NUM_BLOCK; i++) {
			if(block[i].isPainted()) {
				block[i].draw(g);
			}
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		
		int x = e.getX();
		racket.move(x);
		repaint();
	}
			
	public void mouseDragged(MouseEvent e) {
		
	}
	
	
}