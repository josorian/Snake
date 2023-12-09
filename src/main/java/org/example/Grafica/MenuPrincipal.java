package org.example.Grafica;

import javax.swing.*;
import java.awt.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MenuPrincipal extends JFrame{
    private final String SERVER_ADDRESS="localhost";
    private final int SERVER=8888;
    public MenuPrincipal(){
        initCompoents();
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MenuPrincipal frame = new MenuPrincipal();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void initCompoents(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JButton btnJugar = new JButton("Play");
        btnJugar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            Vista frame = new Vista();
                            frame.setVisible(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        JButton btnPuntuacion = new JButton("Puntuaciones");
        btnPuntuacion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try(Socket socket = new Socket(SERVER_ADDRESS,SERVER); ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream objectInputStream=new ObjectInputStream(socket.getInputStream())){
                    outputStream.writeObject("mostrarPuntuaciones");
                    outputStream.flush();
                    Object o = objectInputStream.readObject();
                    if(o instanceof byte[]){
                        byte[] buffer = (byte[]) o;
                        FileOutputStream fileOutputStream = new FileOutputStream("puntuacion.xml");
                        fileOutputStream.write(buffer);
                        fileOutputStream.close();
                        System.out.println("Archivo XML recibido del servidor.");
                    } else {
                        System.out.println("Respuesta inesperada del servidor.");
                    }
                    XMLViewer viewer = new XMLViewer();
                    viewer.setVisible(true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setHorizontalGroup(
                layout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(59, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnPuntuacion)
                                                .addGap(52))
                                        .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(btnJugar)
                                                .addGap(72))))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(174, Short.MAX_VALUE)
                                .addComponent(btnJugar)
                                .addGap(18)
                                .addComponent(btnPuntuacion)
                                .addGap(62))
        );
        getContentPane().setLayout(layout);
        pack();
    }
}
