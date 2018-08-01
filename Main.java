import java.awt.*;
import java.awt.image.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

public class Main extends Canvas implements Runnable{

	private double fps;//

	private boolean gameOn = false;

	private Thread thread;

	private Input input = new Input();

	private Game game = new Game();

	public Main(){
		game.init(input);
		game.setup();
		fps = 1000/game.getFps();//
		setPreferredSize(new Dimension(game.getLargura()-10,game.getAltura()-10));
		this.addKeyListener(input);
		this.addMouseListener(input);
		this.addMouseMotionListener(input);
	}

	public void start(){
		gameOn = true;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run(){
		long seg = System.currentTimeMillis();//
		long tempo = 0;//
		long frame_seg = 0;///
		int frame = 0;///
		while(gameOn){
			tempo += System.currentTimeMillis() - seg;//
			frame_seg += System.currentTimeMillis() - seg;///
			seg = System.currentTimeMillis();//
			if(tempo >= fps){//
				checkInput();
				update();	
				tempo = 0;//
				frame++;///
			}//
			draw();
			if(frame_seg >= 1000){///
				System.out.println("FPS: "+frame);///
				frame = 0;///
				frame_seg = 0;///
			}///

			fps = 1000/game.getFps();/*new add*/
		}
	}

	public void draw(){
		BufferStrategy buffer = getBufferStrategy();
		if(buffer == null){
			createBufferStrategy(3);
			return;
		}

		Graphics g = buffer.getDrawGraphics();

		game.draw(g);

		g.dispose();
		buffer.show();
	}

	public void update(){
		game.update();
	}

	public void checkInput(){
		game.checkInput();
	}

	public static void main(String []args){
		JFrame janela = new JFrame();
		Main jogo = new Main();

		janela.add(jogo);
		janela.pack();
		janela.setVisible(true);
		janela.setResizable(false);
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		janela.setLocationRelativeTo(null);

		jogo.start();
	}
}