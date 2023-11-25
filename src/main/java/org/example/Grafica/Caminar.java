package org.example.Grafica;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Caminar implements Runnable{
	private PanelSnake panel;
	private boolean estado = true;
	public Caminar(PanelSnake panel) {
		this.panel=panel;
	}
	@Override
	public void run() {
		while(estado) {
			this.panel.avanzar();
			this.panel.repaint();
			try {
				Thread.sleep(90);
			}catch(InterruptedException ex) {
				Logger.getLogger(Caminar.class.getName()).log(Level.SEVERE,null,ex);
			}
		}
		
	}
	public void parar() {
		this.estado=false;
	}


}
