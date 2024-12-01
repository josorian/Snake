package org.example.Sever;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import org.example.Grafica.XMLViewer;

public class ServidorMostror {
	private String nombre;
	private final static String direccion= "src/main/java/org/example/Sever/XML/";

 public static void main(String[] args) {
	 try(ServerSocket server = new ServerSocket(9999);){
 		while(true) {
 			String id = UUID.randomUUID().toString();

 			Socket socket = server.accept();
 	 		new Thread(new ServidorMostrarHilo(socket,id,direccion)).start();
 		}
		 
} catch (IOException e1) {
	// TODO Auto-generated catch block
	e1.printStackTrace();
}
}
}
