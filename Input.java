import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input implements KeyListener, MouseListener, MouseMotionListener{

	private String[] key_names = new String[200];
	private boolean[] pressed_keys = new boolean[200];
	private boolean[] released_keys = new boolean[200];
	

	//mouse variables
	private final int MOUSE_BUTTON_LEFT = 1;
	private final int MOUSE_BUTTON_MIDDLE = 2;
	private final int MOUSE_BUTTON_RIGHT = 3;

	private int[]  mousePoint = new int[2];

	private int mouse_btn = 0;

	private boolean mouse_pressed = false;

	public Input(){}

	public void createKey(String s, int i){
		key_names[i] = s;
	}

	public boolean isKeyPressed(String s){
		for (int i = 0; i < 200; i++) {
			if(s == key_names[i] && pressed_keys[i]){
				return true;
			}
		}
		return false;
	}

	public boolean isKeyTyped(String s){
		for (int i = 0; i < 200; i++) {
			if(s == key_names[i] && released_keys[i]){
				released_keys[i] = false;
				return true;
			}
		}
		return false;
	}

	public boolean isMouseClicked(int x, int y, int w, int z, int btn){
		if(btn == mouse_btn){
			if(mousePoint[0] < w && mousePoint[0] > x &&
				mousePoint[1] < z && mousePoint[1] > y){
				mouse_btn = 0;
				return true;
			}
		}
		return false;
	}
	public boolean isMouseClicked(int x, int y, int r, int btn){
		if(btn == mouse_btn){
			int dif_x = Math.abs(x-mousePoint[0]);
			int dif_y = Math.abs(y-mousePoint[1]);
			//int dist = (int)Math.sqrt((dif_x*dif_x)+(dif_y*dif_y));
			int dist = (dif_x*dif_x)+(dif_y*dif_y);
			System.out.println("r: "+r+"		dist: "+dist);
			
			mouse_btn = 0;
			if(dist < (r*r)){
				return true;
			}
		}
		return false;
	}
	public boolean isMouseClicked(int btn){
		if(btn == mouse_btn){
			mouse_btn = 0;
			return true;
		}
		return false;
	}

	public boolean isMousePressed(int x, int y, int w, int z, int btn){
		if(btn == mouse_btn && mouse_pressed){
			if(mousePoint[0] < z && mousePoint[0] > x &&
				mousePoint[1] < w && mousePoint[1] > y){
				return true;
			}
		}	
		return false;
	}
	public boolean isMousePressed(int x, int y, int r, int btn){
		if(btn == mouse_btn && mouse_pressed){
			int dif_x = Math.abs(x-mousePoint[0]);
			int dif_y = Math.abs(y-mousePoint[1]);
			//int dist = (int)Math.sqrt((dif_x*dif_x)+(dif_y*dif_y));
			int dist = (dif_x*dif_x)+(dif_y*dif_y);
			if(dist < (r*r)){
				return true;
			}
		}
		return false;
	}

	public boolean isMousePressed(int btn){
		if(btn == mouse_btn && mouse_pressed){
			return true;
		}	
		return false;
	}


	@Override
	public void keyTyped(KeyEvent e){}
	@Override
	public void keyPressed(KeyEvent e){
		pressed_keys[e.getKeyCode()] = true;
	}
	@Override
	public void keyReleased(KeyEvent e){
		pressed_keys[e.getKeyCode()] = false;
		released_keys[e.getKeyCode()] = true;
	}
	//mouse listeners
	@Override
	public void mouseExited(MouseEvent e){

	}

	@Override
	public void mouseEntered(MouseEvent e){
		
	}

	@Override
	public void mouseReleased(MouseEvent e){
		mouse_pressed = false;
		mouse_btn = 0;
	}

	@Override
	public void mousePressed(MouseEvent e){
		mouse_btn = e.getButton();
		mouse_pressed = true;
	}

	@Override
	public void mouseClicked(MouseEvent e){
		mouse_btn = e.getButton();
	}

	
	@Override
    public void mouseMoved(MouseEvent e) {
	    /*System.out.println("X : " + e.getX());
	    System.out.println("Y : " + e.getY());*/
		mousePoint[0] = e.getX();
		mousePoint[1] = e.getY();
    }

	
	@Override
    public void mouseDragged(MouseEvent e) {
		mousePoint[0] = e.getX();
		mousePoint[1] = e.getY();
	}

	//getters

	public int getMouseLeftButton(){
		return MOUSE_BUTTON_LEFT;
	}

	public int getMouseMiddleButton(){
		return MOUSE_BUTTON_MIDDLE;
	}

	public int getMouseRightButton(){
		return MOUSE_BUTTON_RIGHT;
	}

	public int[] getMousePoint(){
		return mousePoint;
	}


}