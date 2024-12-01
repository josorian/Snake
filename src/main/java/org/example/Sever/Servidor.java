package org.example.Sever;

import org.example.Grafica.Puntuacion;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

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
