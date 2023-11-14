package org.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
//Gestiona los mensajes usando hilos
public class ClienteHandler extends Thread {
    private Socket cliente;
    private ObjectInputStream entrada;
    private ObjectOutputStream salida;

    public ClienteHandler(Socket socket) {
        this.cliente = socket;
        try {
            entrada = new ObjectInputStream(cliente.getInputStream());
            salida = new ObjectOutputStream(cliente.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                // Leer objeto enviado por el cliente
                Mensaje mensaje = (Mensaje) entrada.readObject();
                System.out.println("Cliente dice: " + mensaje.getContenido());

                // Enviar un objeto de vuelta al cliente
                Mensaje respuesta = new Mensaje("Mensaje recibido: " + mensaje.getContenido());
                salida.writeObject(respuesta);
                salida.flush();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                entrada.close();
                salida.close();
                cliente.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}