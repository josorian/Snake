package org.example.Grafica;

import java.io.Serializable;

public class Puntuacion implements Serializable {
    private String user;
    private int puntuacion;
    private String sala;
    // Constructor
    // Precondición: `user` y `sala` no deben ser nulos o vacíos, y `puntuacion` debe ser un valor no negativo.
    // Poscondición: Se crea un objeto `Puntuacion` con los valores inicializados.
    public Puntuacion(String user, int puntuacion,  String sala) {
        this.user = user;
        this.puntuacion = puntuacion;
        this.sala = sala;
    }

 // Método getUser
    // Precondición: Ninguna.
    // Poscondición: Devuelve el nombre del usuario asociado a la puntuación.
    public String getUser() {
        return user;
    }

    // Método getPuntuacion
    // Precondición: Ninguna.
    // Poscondición: Devuelve el valor de la puntuación.
    public int getPuntuacion() {
        return puntuacion;
    }

    // Método getSala
    // Precondición: Ninguna.
    // Poscondición: Devuelve el nombre de la sala asociada a la puntuación.
    public String getSala() {
        return sala;
    }

    // Método toString
    // Precondición: Ninguna.
    // Poscondición: Devuelve una representación en cadena del objeto `Puntuacion`, incluyendo el usuario y la puntuación.
    @Override
    public String toString() {
        return "Puntuacion{" +
                "user='" + user + '\'' +
                ", puntuacion=" + puntuacion +
                '}';
    }
}