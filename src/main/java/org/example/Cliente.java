package org.example;

import java.io.*;
import java.net.*;

public class Cliente {

    public static void main(String[] args) {
        Socket socket = null;

        try {
            // Conectar al servidor en el puerto 5000 y en la dirección IP local
            socket = new Socket("localhost", 5000);
            System.out.println("Conectado al servidor.");

            ObjectOutputStream salida = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());

            // Enviar un mensaje al servidor
            Mensaje mensaje = new Mensaje("Hola desde el cliente");
            salida.writeObject(mensaje);
            salida.flush();

            // Recibir respuesta del servidor
            Mensaje respuesta = (Mensaje) entrada.readObject();
            System.out.println("Servidor dice: " + respuesta.getContenido());

            // Cerrar los flujos y el socket
            salida.close();
            entrada.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}