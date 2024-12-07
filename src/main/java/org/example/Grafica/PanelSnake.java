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
    // Constructor
    // Precondición: `tamanoMax` y `cantidad` deben ser mayores que 0.
    // Poscondición: Se inicializa el estado inicial de la serpiente, se genera la primera fruta y se inicia el hilo de movimiento.
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
    // Método paint
    // Precondición: El objeto `Graphics pintor` no debe ser nulo.
    // Poscondición: Dibuja la serpiente y la fruta en el panel.
    public void paint(Graphics pintor) {
        super.paint(pintor);
        pintor.setColor(colorSnake);
        for(int[] parte:this.snake) {
            pintor.fillRect(this.res/2+parte[0]*this.tamano,this.res/2+parte[1]*this.tamano,this.tamano-1,this.tamano-1);
        }
        pintor.setColor(colorComida);
        pintor.fillRect(this.res/2+this.comida[0]*this.tamano,this.res/2+this.comida[1]*this.tamano,this.tamano-1,this.tamano-1);

    }
    // Método avanzar
    // Precondición: La lista `snake` debe estar inicializada con al menos un segmento.
    // Poscondición: La serpiente avanza en la dirección indicada. Si colisiona consigo misma, el juego se detiene. Si come la fruta, crece.
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
    		observer.guardarPuntuacion();
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

    // Método generarFrutas
    // Precondición: La lista `snake` debe estar inicializada y contener la posición actual de la serpiente.
    // Poscondición: Se genera una nueva fruta en una posición aleatoria que no esté ocupada por la serpiente.
    public void generarFrutas() {
    	boolean existe=false;
    	int a = (int)(Math.random()*this.cantidad);
    	int b = (int)(Math.random()*this.cantidad);
    	for(int[] parte:this.snake) {
    		if(parte[0]==a && parte[1]==b) {
    			existe=true;
    			generarFrutas();
    			this.notificarCambioSnake();
    			break;
    		}
    	}
    	if(!existe) {
    		this.comida[0]=a;
    		this.comida[1]=b;
    	}

    }
    // Método cambiarDireccion
    // Precondición: `dir` debe ser un valor válido ("de", "iz", "ar", "ab").
    // Poscondición: Cambia la dirección próxima de la serpiente si es válida (no contradice la dirección actual).
    public void cambiarDireccion(String dir) {
    	if((this.direccion.equals("de")||this.direccion.equals("iz"))&&(dir.equals("ar")||dir.equals("ab"))) {
        	this.direccionProxima=dir;
    	}
    	if((this.direccion.equals("ar")||this.direccion.equals("ab"))&&(dir.equals("iz")||dir.equals("de"))) {
        	this.direccionProxima=dir;
    	}
    	
    }
    // Método igualarDir
    // Precondición: Ninguna.
    // Poscondición: La dirección actual de la serpiente se actualiza a la dirección próxima.
    public void igualarDir() {
    	this.direccion=this.direccionProxima;
    }
    // Método setObserver
    // Precondición: El parámetro `observer` no debe ser nulo.
    // Poscondición: Se asigna el observador para manejar actualizaciones de puntuación.
    public void setObserver(Vista observer) {
        this.observer = observer;
    }
    // Método notificarCambioSnake
    // Precondición: El observador (observer) debe estar configurado.
    // Poscondición: Informa al observador del cambio en la longitud de la serpiente.
    private void notificarCambioSnake() {
        if (observer != null) {
            observer.actualizarPuntuacion(snake.size()-2);
        }
    }
}