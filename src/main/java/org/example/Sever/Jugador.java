package org.example.Sever;

import java.io.Serializable;
import java.net.Socket;

public class Jugador implements Serializable{
    private String nombre;
    private String socket;

    // Constructor
    public Jugador(String nombre, String socket) {
        this.nombre = nombre;
        this.socket = socket;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getSocket() {
        return socket;
    }
}
