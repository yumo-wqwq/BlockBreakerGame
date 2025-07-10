package blockKuzushi;
import javax.swing.JFrame;

public class MainProgram extends JFrame {
	
	public MainProgram() {
		setTitle("ブロックくずしゲーム");
		setResizable(false);
		
		MainPanel panel = new MainPanel();
		getContentPane().add(panel);
		
		pack();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	
	public static void main(String[] args) {
		new MainProgram();
	}
}
