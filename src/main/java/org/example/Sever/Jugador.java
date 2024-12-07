package org.example.Sever;

import java.io.Serializable;

public class Jugador implements Serializable {
    private String nombre;
    private String socket;

    // Constructor de la clase Jugador
    // Precondición: El nombre y el socket no deben ser nulos.
    // Poscondición: Crea una instancia de Jugador con el nombre y el socket proporcionados.
    public Jugador(String nombre, String socket) {
        this.nombre = nombre;
        this.socket = socket;
    }

    // Método para obtener el nombre del jugador
    // Precondición: El objeto Jugador debe haber sido correctamente inicializado.
    // Poscondición: Devuelve el nombre del jugador.
    public String getNombre() {
        return nombre;
    }

    // Método para obtener el socket del jugador
    // Precondición: El objeto Jugador debe haber sido correctamente inicializado.
    // Poscondición: Devuelve el socket asociado al jugador.
    public String getSocket() {
        return socket;
    }
}
