package org.example.Grafica;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
public class PanelSnake extends JPanel {
    private Color colorSnake = Color.BLUE;
    private Color colorComida = Color.RED;
    private int tamanoMax;
    private int tamano;
    private int cantidad;
    private int res;
    private List<int[]> snake = new ArrayList<>();
    private int[] comida=new int[2];
    private String direccion="de";
    private String direccionProxima="de";
    Thread hilo;
    Caminar caminar;
    private Vista observer;
    public PanelSnake(int tamanoMax,int cantidad) {
        this.tamanoMax=tamanoMax;
        this.cantidad=cantidad;
        this.tamano=tamanoMax/cantidad;
        this.res=tamanoMax%cantidad;
        int [] a = {cantidad/2-1,cantidad/2-1};
        int [] b = {cantidad/2,cantidad/2-1};
        this.snake.add(a);
        this.snake.add(b);
        this.generarFrutas();
        this.caminar=new Caminar(this);
        this.hilo = new Thread(caminar);
        this.hilo.start();
        
    }
    public void paint(Graphics pintor) {
        super.paint(pintor);
        pintor.setColor(colorSnake);
        for(int[] parte:this.snake) {
            pintor.fillRect(this.res/2+parte[0]*this.tamano,this.res/2+parte[1]*this.tamano,this.tamano-1,this.tamano-1);
        }
        pintor.setColor(colorComida);
        pintor.fillRect(this.res/2+this.comida[0]*this.tamano,this.res/2+this.comida[1]*this.tamano,this.tamano-1,this.tamano-1);

    }
    public void avanzar() {
    	notificarCambioSnake();
    	igualarDir();
    	int [] ultimo = this.snake.get(this.snake.size()-1);
    	int agregarX=0;
    	int agregarY=0;
    	switch(this.direccion) {
    	case "de":agregarX=1;break;
    	case "iz":agregarX=-1;break;
    	case "ar":agregarY=-1;break;
    	case "ab":agregarY=1;break;
    	}
    	int [] nuevo= {Math.floorMod(ultimo[0]+agregarX,this.cantidad),Math.floorMod(ultimo[1]+agregarY,this.cantidad)};
    	boolean existe=false;
    	for(int i=0;i<this.snake.size();i++) {
    		if(nuevo[0]==this.snake.get(i)[0]&&nuevo[1]==this.snake.get(i)[1]) {
    			existe=true;
    			break;
    		}
    	}
    	if (existe) {
    		IngresarUser user = new IngresarUser(this.snake.size());
    		user.setVisible(true);
    		this.caminar.parar();
    	}else {
    		if(nuevo[0]==comida[0]&&nuevo[1]==comida[1]) {
    			this.snake.add(nuevo);
    			generarFrutas();
    		}else {
    			this.snake.add(nuevo);
    			this.snake.remove(0);
    		}
    	}    	
    }
    public void generarFrutas() {
    	boolean existe=false;
    	int a = (int)(Math.random()*this.cantidad);
    	int b = (int)(Math.random()*this.cantidad);
    	for(int[] parte:this.snake) {
    		if(parte[0]==a && parte[1]==b) {
    			existe=true;
    			generarFrutas();
    			break;
    		}
    	}
    	if(!existe) {
    		this.comida[0]=a;
    		this.comida[1]=b;
    	}

    }
    public void cambiarDireccion(String dir) {
    	if((this.direccion.equals("de")||this.direccion.equals("iz"))&&(dir.equals("ar")||dir.equals("ab"))) {
        	this.direccionProxima=dir;
    	}
    	if((this.direccion.equals("ar")||this.direccion.equals("ab"))&&(dir.equals("iz")||dir.equals("de"))) {
        	this.direccionProxima=dir;
    	}
    	
    }
    public void igualarDir() {
    	this.direccion=this.direccionProxima;
    }
    public void setObserver(Vista observer) {
        this.observer = observer;
    }
    private void notificarCambioSnake() {
        if (observer != null) {
            observer.actualizarPuntuacion(snake.size()-2);
        }
    }
}