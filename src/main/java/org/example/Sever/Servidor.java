package org.example.Sever;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
   

    public static void main(String[] args) {
        int puerto = 8888; // Puerto en el que el servidor escuchará
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor iniciado, esperando conexiones...");
            
            // Bucle para aceptar múltiples clientes
            while (true) {
                // Aceptar una nueva conexión de cliente
                Socket socket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado: " + socket.getInetAddress().getHostName());


                // Crear un hilo para manejar la conexión con este cliente
                ServerHilo serverHilo = new ServerHilo(socket);
                new Thread(serverHilo).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   
}
