package org.example.Grafica;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Caminar implements Runnable {
    private PanelSnake panel;
    private boolean estado = true;

    // Constructor que inicializa el panel asociado.
    // Precondición: Se debe proporcionar un objeto PanelSnake no nulo.
    // Poscondición: La instancia queda asociada al panel proporcionado y lista para ejecutarse.
    public Caminar(PanelSnake panel) {
        this.panel = panel;
    }

    @Override
    // Precondición: Este método debe ser invocado dentro de un hilo separado.
    //              El objeto PanelSnake debe estar correctamente inicializado.
    // Poscondición: Actualiza el panel periódicamente mientras `estado` sea true.
    //              Si el hilo es interrumpido, el bucle se detiene.
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
