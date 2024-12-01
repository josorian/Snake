package org.example.Grafica;

import java.io.Serializable;

public class Puntuacion implements Serializable {
    private String user;
    private int puntuacion;
    private String sala;

    public Puntuacion(String user, int puntuacion,  String sala) {
        this.user = user;
        this.puntuacion = puntuacion;
        this.sala = sala;
    }

    public String getUser() {
        return user;
    }

    public int getPuntuacion() {
        return puntuacion;
    }
    public String getSala() {
    	return sala;
    }

    @Override
    public String toString() {
        return "Puntuacion{" +
                "user='" + user + '\'' +
                ", puntuacion=" + puntuacion +
                '}';
    }
}
