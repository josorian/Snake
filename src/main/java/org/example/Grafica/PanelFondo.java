package org.example.Grafica;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class PanelFondo extends JPanel{
	private Color colorFondo=Color.GREEN;
	private int tamanoMax,tamano,cantidad,res;
	public PanelFondo (int tamanoMaximo,int cantidad) {
        this.tamanoMax=tamanoMaximo;
        this.cantidad=cantidad;
        this.tamano=tamanoMaximo/cantidad;
        this.res=tamanoMaximo%cantidad;
    }
    public void paint(Graphics pintor) {
        super.paint(pintor);
        pintor.setColor(colorFondo);
        for(int i=0;i<this.cantidad;i++) {
            for(int j=0;j<this.cantidad;j++) {
                pintor.fillRect(this.res/2+i*this.tamano, this.res/2+j*this.tamano, this.tamano-1, this.tamano-1);
            }
        }
    }

}
