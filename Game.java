import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.awt.event.*;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Game extends App{
	private final int populacao = 300;
	private final int total_turnos = 1000;
	private Agente[] agentes;
	private int[] objetivo;//objetivo onde oa agentes querem chegar
	private int turno = 1;
	private Random r = new Random();
	private int ger = 1;//contador de greações
	private double chance = 5;//1 em 100000//chance de mutação
	private int[] ult_melhor = new int[2];//guarda as coordenadas do melhor agente da ultima geração
	private Parede[] paredes;
	private int vel = 30;//velocidade em fps
	private boolean[] keymap = new boolean[3];
		/*KEYMAP
			*velocidade------------------S
			*ultimo melhor--------------A keymap[0]
			*fittest
				*mostrar somente o melhor-------Q keymap[1]
				*destacar o melhor----------W keymap[2]
		*/
	public void setup(){
		setFps(vel);
		setAltura(400);
		setLargura(720);
		objetivo =  new int[2];
		objetivo[0] = getLargura() - 25;
		objetivo[1] = getAltura()/2;
		agentes = new Agente[populacao];
		for(int i = 0; i < populacao; i++)
			agentes[i] = new Agente(10,getAltura()/2,objetivo,total_turnos);
		ult_melhor[0] = 10;
		ult_melhor[1] = getAltura()/2;
		paredes = new Parede[6];
		paredes[0] = new Parede(100,0,25,70);
		paredes[1] = new Parede(100,150,25,250);
		paredes[2] = new Parede(250,100,150,150);
		paredes[3] = new Parede(475,0,25,200);
		paredes[4] = new Parede(300,275,200,25);
		paredes[5] = new Parede(475,300,25,75);
		/*keys*/
		createKey("vel",KeyEvent.VK_S);
		createKey("ult_melhor",KeyEvent.VK_A);
		createKey("melhor",KeyEvent.VK_Q);
		createKey("destaca",KeyEvent.VK_W);
		for (boolean k: keymap)
			k = false;
		exportar_lista("Geracao\t\tFitness\tCoordenadas\tobjetivo",false);
	}
	//metodo para desenhar/renderizar os agentes e o mapa
	public void draw(Graphics g){
		//background
		g.setColor(new Color(215,215,215));
		g.fillRect(0,0,getLargura(),getAltura());
		//calcula o melhor agente no momento
		int i_aux = 0;
		double max_fit = 0;
		for(int i = 0; i < populacao; i++){
			if(max_fit < agentes[i].getFitness()){
				i_aux = i;
				max_fit = agentes[i].getFitness();
			}
		}
		//desenha todos os agentes ou apenas o melhor
		if(!keymap[1]){
			for(Agente agent:agentes)
				agent.draw(g);
		}else
			agentes[i_aux].draw(g);
		//destaca o melhor agente
		if(keymap[2])
			agentes[i_aux].draw_best(g);
		//desenha as paredes
		for (Parede parede: paredes) 
			parede.draw(g);
		//desenha o objetivo
		g.setColor(new Color(Color.CYAN.getRed(),Color.CYAN.getGreen(),Color.CYAN.getBlue(),70));
		g.fillRect(objetivo[0]-25,objetivo[1]-25,50,50);
		g.setColor(Color.CYAN);
		g.drawRect(objetivo[0]-25,objetivo[1]-25,50,50);
		g.setColor(Color.RED);
		//assinala a posição do melhor agente da ultima geração
		if(keymap[0])
			g.fillOval(ult_melhor[0]-5,ult_melhor[1]-5,10,10);
		//-------
		g.setColor(Color.BLACK);
		g.drawString("turno "+turno,10,20);
		g.drawString("ger "+ger,10,40);
		g.drawString("mut "+((chance/100000)*100)+"%",10,60);
		g.drawString("popul "+populacao,10,80);
		g.drawString("vel "+(vel/30),10,100);
	}
	//metodo onde é alojada a parte lógica
	public void update(){
		//
		if(turno < total_turnos){
			//mover os agentes e verificar colisões
			for(Agente agente:agentes){
				agente.update(turno);
				colisoes(agente);
			}
			//no ultimo turno fazer seleção da proxima geração
			if(turno == total_turnos-1)
				selecao();
			turno++;
		}else{
			turno = 0;
			ger++;
		}
	}
	//metodo para verificar input do teclado
	public void checkInput(){ 
		if(isKeyTyped("vel")){
			if(vel == 30)
				vel += 30;
			else if(vel == 60)
				vel += 30;
			else
				vel = 30;
			setFps(vel);
		}
		if(isKeyTyped("ult_melhor"))
			keymap[0] = keymap[0] ? false : true;

		if(isKeyTyped("melhor"))
			keymap[1] = keymap[1] ? false : true;

		if(isKeyTyped("destaca"))
			keymap[2] = keymap[2] ? false : true;
	}
	//metodo que faz a seleção da proxima geração
	public void selecao(){
		double[] fitness = new double[populacao];
		int[][][] dna = new int[populacao][total_turnos][2];
		int i_aux = 0;
		int k = 0;
		double max_fit = 0;
		//este ciclo vai ordenar os dnas do com 
		//maior fitness para o com menor
		for(int i = 0; i < populacao; i++){
			if(agentes[i].getFitness() > 0 && max_fit < agentes[i].getFitness()){
				i_aux = i;
				max_fit = agentes[i].getFitness();
			}
			if(i == populacao-1){
				max_fit = 0;
				fitness[k] = agentes[i_aux].getFitness();
				dna[k] = agentes[i_aux].getDNA();
				agentes[i_aux].setFitness(-1);
				if(k == 0){
					//no primeiro ciclo irá guardar 
					//as coordenadas do agente com maior fitness
					ult_melhor[0] = agentes[i_aux].getX();
					ult_melhor[1] = agentes[i_aux].getY();
				}
				if(k < populacao-1){
					i = -1;
					i_aux = 0;
				}
				k++;
			}
		}
		//selecionamos metade da população-2(a melhor metade)
		int selected = (populacao/2)-2;
		//os dnas dos dois melhores agentes são passados
		//para a proxima geração sem crossover ou mutação
		//para garantir que haja progresso
		agentes[0] = new Agente(10,getAltura()/2,objetivo,total_turnos);
		agentes[0].setDNA(dna[0]);
		agentes[1] = new Agente(10,getAltura()/2,objetivo,total_turnos);
		agentes[1].setDNA(dna[1]);
		int prox_i = 2;

		//agora fazemos o crossover duas vezes(com tecnicas diferentes para garantir divercidade)
		//para obter o dobro dos agentes

		//primeiro crossover
		//fazemos o crossover de um agente com o agente adjacente
		//exemplo:
		//{1,2,3,4,5,6,7,8}
		//{[1,2],[3,4],[5,6],[7,8]}
		for(int i = 0; i < selected; i+=2,prox_i+=2){
			int[][][] novo_dna = crossover(dna[i],dna[i+1]);
			agentes[prox_i] = new Agente(10,getAltura()/2,objetivo,total_turnos);
			agentes[prox_i].setDNA(novo_dna[0]);
			agentes[prox_i].mutacao(chance);//
			agentes[prox_i+1] = new Agente(10,getAltura()/2,objetivo,total_turnos);
			agentes[prox_i+1].setDNA(novo_dna[1]);
			agentes[prox_i+1].mutacao(chance);//
		}
		//segundo crossover
		//fazemos o crossover do primeiro agente com o ultimo,
		//do segundo com o penultimo e assim por diante
		//exemplo:
		//{1,2,3,4,5,6,7,8}
		//{[1,8],[2,7],[3,6],[4,5]}
		for(int i = 0, j = selected-1; i < selected/2; i++,prox_i+=2,j--){
			int[][][] novo_dna = crossover(dna[j],dna[i]);
			agentes[prox_i] = new Agente(10,getAltura()/2,objetivo,total_turnos);
			agentes[prox_i].setDNA(novo_dna[0]);
			agentes[prox_i].mutacao(chance);//
			agentes[prox_i+1] = new Agente(10,getAltura()/2,objetivo,total_turnos);
			agentes[prox_i+1].setDNA(novo_dna[1]);
			agentes[prox_i+1].mutacao(chance);//
		}
		//por ultimo irão sobrar duas posições 
		//e vamos preenche-los com os crossover dos dois melhores agentes
		int[][][] ultimo_dna = crossover(dna[1],dna[0]);
		agentes[populacao-2] = new Agente(10,getAltura()/2,objetivo,total_turnos);
		agentes[populacao-2].setDNA(ultimo_dna[0]);
		agentes[populacao-2].mutacao(chance);//
		agentes[populacao-1] = new Agente(10,getAltura()/2,objetivo,total_turnos);
		agentes[populacao-1].setDNA(ultimo_dna[1]);
		agentes[populacao-1].mutacao(chance);//
		//exportar para um ficheiro os dados dos do melhor agente
		exportar_lista(ger+"\t\t"+fitness[0]+"\t"+"("+ult_melhor[0]+","+ult_melhor[1]+")"+"\t"+"("+objetivo[0]+","+objetivo[1]+")",true);
	}
	//metodo para fazer o crossover do dna
	//exemplo:
	//a1{**********}
	//a2{++++++++++}
	// | ->pivot(aleatorio)
	//a1{****|******}
	//a2{++++|++++++}
	//dna[0]{****|++++++}
	//dna[1]{++++|******}
	public int[][][] crossover(int[][] a1, int[][] a2){
		int[][][] dna = new int[2][total_turnos][2];
		int pivot = r.nextInt(total_turnos);
		for(int i = 0; i < pivot; i++){
			dna[0][i] = a1[i];
		}
		for(int i = pivot; i < total_turnos; i++){
			dna[0][i] = a2[i];
		}
		for(int i = 0; i < pivot; i++){
			dna[1][i] = a2[i];
		}
		for(int i = pivot; i < total_turnos; i++){
			dna[1][i] = a1[i];
		}
		return dna;
	}
	public void colisoes(Agente agente){
		//limites do ecrã
		if(agente.getX() < 5)
			agente.setX(5);
		if(agente.getX() > getLargura()-5)
			agente.setX(getLargura()-5);
		if(agente.getY() < 5)
			agente.setY(5);
		if(agente.getY() > getAltura()-5)
			agente.setY(getAltura()-5);
		//parede		
		for (Parede parede: paredes){
			//esquerda
			if(agente.getX()+5 > parede.getX() && agente.getX()-5 < parede.getX()+7
				&& agente.getY()+5 > parede.getY() && agente.getY()-5 < parede.getY()+parede.getH())
				agente.setX(parede.getX()-5);
			//direita
			if(agente.getX()+5 > parede.getX()+parede.getW()-7 && agente.getX()-5 < parede.getX()+parede.getW()
				&& agente.getY()+5 > parede.getY() && agente.getY()-5 < parede.getY()+parede.getH())
				agente.setX(parede.getX()+parede.getW()+5);
			//cima
			if(agente.getX()+5 > parede.getX() && agente.getX()-5 < parede.getX()+parede.getW()
				&& agente.getY()+5 > parede.getY() && agente.getY()-5 < parede.getY()+7)
				agente.setY(parede.getY()-5);
			//baixo
			if(agente.getX()+5 > parede.getX() && agente.getX()-5 < parede.getX()+parede.getW()
				&& agente.getY()+5 > parede.getY()+parede.getH()-7 && agente.getY()-5 < parede.getY()+parede.getH())
				agente.setY(parede.getY()+parede.getH()+5);
			}
	}
	//metodo para exportar para um ficheiro .txt as informações sobre 
	//todos os melhores agentes de cada geração
	public void exportar_lista(String s,boolean state){
		try {
			FileWriter fwt = new FileWriter("best_list.txt",state);
			BufferedWriter fw = new BufferedWriter(fwt);
			fw.write(s,0,s.length());
			fw.newLine();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}