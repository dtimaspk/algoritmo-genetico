import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.awt.event.*;

public class Parede{
	int x,y,w,h;
	public Parede(int x,int y,int w,int h){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	public void draw(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(x,y,w,h);
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getW(){
		return w;
	}
	public int getH(){
		return h;
	}
}