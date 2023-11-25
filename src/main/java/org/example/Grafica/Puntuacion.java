package org.example.Grafica;

public class Puntuacion {
	private String user;
	private int puntuacion;
	public Puntuacion(String user,int puntuacion) {
		this.user=user;
		this.puntuacion=puntuacion;
	}
	public String toString() {
		return String.valueOf(puntuacion);
	}
	
	
}
