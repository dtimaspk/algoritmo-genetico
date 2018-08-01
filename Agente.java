import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.awt.event.*;

public class Agente{
	private int[][] dna;
	private Random r;
	private int x,y;
	private double fitness = 0;
	private int[] objetivo;
	public Agente(int x, int y,int[] objetivo,int total_turnos){
		this.x = x;
		this.y = y;
		this.objetivo = objetivo;
		r = new Random();
		dna = new int[total_turnos][2];
		for(int i = 0; i < dna.length; i++){
			dna[i][0] = r.nextInt(3)-1;
			dna[i][1] = r.nextInt(3)-1;
		}
	}
	public void draw(Graphics g){
		g.setColor(new Color(Color.GRAY.getRed(),Color.GRAY.getGreen(),Color.GRAY.getBlue(),150));
		g.fillRect((x-5),(y-5),10,10);
		g.setColor(Color.BLACK);
		g.drawRect((x-5),(y-5),10,10);
	}
	public void draw_best(Graphics g){
		g.setColor(Color.BLUE);
		g.drawLine(x,y,objetivo[0],objetivo[1]);
		g.fillRect((x-5),(y-5),10,10);
		g.setColor(Color.BLACK);
		g.drawRect((x-5),(y-5),10,10);
	}
	public void update(int turn){
		//mover o agente
		x += dna[turn][0]*5;
		y += dna[turn][1]*5;
		calc_fitness();
	}
	//calcular fitness
	private void calc_fitness(){
		double d_x = Math.abs(x - objetivo[0]);
		double d_y = Math.abs(y - objetivo[1]);
		double d_r = Math.sqrt((d_x*d_x)+(d_y*d_y));
		fitness = 1/d_r;
	}
	//fazer mutação no dna
	public void mutacao(double chance){
		for(int i = 0; i < dna.length; i++){
			if((r.nextInt(100000)+1) <= chance){
				dna[i][0] = r.nextInt(3)-1;
				dna[i][1] = r.nextInt(3)-1;	
			}
		}
	}
	public double getFitness(){
		return fitness;
	}
	public void setFitness(double fitness){
		this.fitness = fitness;
	}
	public int[][] getDNA(){
		return dna;
	}
	public void setDNA(int[][] dna){
		this.dna = dna;
	}
	public int getX(){
		return x;
	}
	public void setX(int x){
		this.x = x;
	}
	public int getY(){
		return y;
	}
	public void setY(int y){
		this.y = y;
	}
}