package org.example.Grafica;

import java.io.Serializable;

public class Puntuacion implements Serializable {
	private String user;
	private int puntuacion;
	public Puntuacion(String user,int puntuacion) {
		this.user=user;
		this.puntuacion=puntuacion;
	}
	public String toString() {
		return String.valueOf(puntuacion);
	}


	public String getUser() {
		return this.user;
	}
	public int getPuntuacion(){
		return this.puntuacion;
	}
}
