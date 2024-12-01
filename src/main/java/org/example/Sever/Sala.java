package org.example.Sever;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Sala implements Serializable{
    private String nombre;
    private int maxJugadores;
    private List<Jugador> jugadores = new CopyOnWriteArrayList<>();
    private int jugadoresListos = 0;;
    private boolean puntuacionesMostrar = false;
    
    // Constructor
    public Sala(String nombre, int maxJugadores) {
        this.nombre = nombre;
        this.maxJugadores = maxJugadores;
        this.jugadoresListos++;
    }

    // Método para agregar un jugador a la sala
    public boolean agregarJugador(String nombreJugador, String socket) {
        if (jugadores.size() < maxJugadores) {
            jugadores.add(new Jugador(nombreJugador, socket));
            return true;
        }
        return false;  // Si la sala está llena
    }

    // Método para verificar si la sala está llena
    public boolean estaLlena() {
        return jugadores.size() == maxJugadores;
    }
    

    public void mostrarPuntuacionesATodos(String nombreSala) {
    	File archivoXML = null;
    	for (Jugador jugador : jugadores) {
            archivoXML = new File("src/main/java/org/example/Sever/XML/" + nombreSala + ".xml");
        	String port = jugador.getSocket().split(":")[1];
        	String local = jugador.getSocket().split(":")[0];
            try(Socket s = new Socket(local,Integer.parseInt(port));
            	DataOutputStream out = new DataOutputStream(s.getOutputStream());
            	FileInputStream fis = new FileInputStream(archivoXML))
            {
            	String nombre= nombreSala+"\n";
            	out.write(nombre.getBytes());

                byte[] buffer = new byte[1028];
                int bytesLeidos;
                while((bytesLeidos=fis.read(buffer))!=-1) {
                	out.write(buffer, 0, bytesLeidos);
                }
                System.out.println("Puntuaciones enviadas a: " + jugador.getNombre());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    	archivoXML.delete();
    }

   

    // Getters
    public String getNombre() {
        return nombre;
    }

    public int getMaxJugadores() {
        return maxJugadores;
    }
    public void setMaxJugadores() {
    	this.maxJugadores --;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

}
