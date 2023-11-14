package org.example;
import java.io.*;
import java.net.*;



public class Servidor {

    public static void main(String[] args) {
        ServerSocket servidor = null;
        Socket cliente = null;

        try {
            servidor = new ServerSocket(5000);
            System.out.println("Servidor iniciado. Esperando clientes...");

            while (true) {
                cliente = servidor.accept();
                System.out.println("Cliente conectado desde: " + cliente.getInetAddress().getHostName());

                // Crear un hilo para manejar la comunicación con el cliente
                Thread clienteThread = new ClienteHandler(cliente);
                clienteThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                servidor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


