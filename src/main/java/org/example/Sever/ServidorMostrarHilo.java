package org.example.Sever;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.rmi.server.UID;
import java.util.UUID;

import javax.swing.SwingUtilities;

import org.example.Grafica.XMLViewer;

public class ServidorMostrarHilo implements Runnable {

	private Socket socket;
	private String id;
	private String direccion;
	public ServidorMostrarHilo (Socket socket,String id,String direccion) {
		this.socket = socket;
		this.id=id;
		this.direccion=direccion;
	}
	@Override
	public void run() {
		try(DataInputStream dis = new DataInputStream(socket.getInputStream())){
			String nombre = dis.readLine();
			System.out.println(nombre);
	       try(FileOutputStream fileOutputStream = new FileOutputStream(direccion +nombre +id+".xml")){
	    	   byte[] buffer = new byte[1028];
               int bytesLeidos;
               while((bytesLeidos=dis.read(buffer))!=-1) {
              	 fileOutputStream.write(buffer, 0, bytesLeidos);
               }
            
               fileOutputStream.close();
               System.out.println("Archivo XML recibido del servidor.");
           
               SwingUtilities.invokeLater(() -> {
            	    XMLViewer viewer = new XMLViewer(direccion+nombre+id);
            	    viewer.setVisible(true);
            	});
	       }
			

		                
	 			
	             
	 		} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				try {
					if(socket!=null) {
						socket.close();
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}
	}

}
