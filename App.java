import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class App{
	protected Input input;
	protected int largura = 720;
	protected int altura = 480;
	protected int fps = 30;//
	public void init(Input input){
		this.input = input;
	}

	public int getLargura(){return largura;}
	public int getAltura(){return altura;}
	public int getFps(){return fps;}//
	public void setLargura(int largura){this.largura = largura;}
	public void setAltura(int altura){this.altura = altura;}
	public void setFps(int fps){this.fps = fps;}//


	public void createKey(String s, int i){
		input.createKey(s,i);
	}

	public boolean isKeyPressed(String s){
		return input.isKeyPressed(s);
	}

	public boolean isKeyTyped(String s){
		return input.isKeyTyped(s);
	}

	public boolean isMouseClicked(int x, int y, int w, int z, int btn){
		return input.isMouseClicked(x,y,w,z,btn);
	}
	public boolean isMouseClicked(int x, int y, int r, int btn){
		return input.isMouseClicked(x,y,r,btn);
	}
	public boolean isMouseClicked(int btn){
		return input.isMouseClicked(btn);
	}

	public boolean isMousePressed(int x, int y, int w, int z, int btn){
		return input.isMousePressed(x,y,w,z,btn);
	}
	public boolean isMousePressed(int x, int y, int r, int btn){
		return input.isMousePressed(x,y,r,btn);
	}

	public boolean isMousePressed(int btn){
		return input.isMousePressed(btn);
	}

	public int getMouseLeftButton(){
		return input.getMouseLeftButton();
	}

	public int getMouseMiddleButton(){
		return input.getMouseMiddleButton();
	}

	public int getMouseRightButton(){
		return input.getMouseRightButton();
	}

	public int[] getMousePoint(){
		return input.getMousePoint();
	}
}