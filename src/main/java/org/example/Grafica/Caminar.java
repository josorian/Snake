package org.example.Grafica;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Caminar implements Runnable {
    private PanelSnake panel;
    private boolean estado = true;

    public Caminar(PanelSnake panel) {
        this.panel = panel;
    }

    @Override
    public void run() {
        while (estado) {
            try {
                Thread.sleep(50);
                panel.avanzar();
                panel.repaint();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                Logger.getLogger(Caminar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void parar() {
        this.estado = false;
        Thread.currentThread().interrupt();
    }
}
