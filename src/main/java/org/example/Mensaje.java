package org.example;

import java.io.Serializable;
//·Esta clase es un ejemplo para saber que esta bien implentada la conexion entre servidor cliente al mandar objetos
public class Mensaje implements Serializable {
    private static final long serialVersionUID = 1L;
    private String contenido;

    public Mensaje(String contenido) {
        this.contenido = contenido;
    }

    public String getContenido() {
        return contenido;
    }
}
