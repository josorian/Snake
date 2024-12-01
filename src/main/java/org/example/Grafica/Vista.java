package org.example.Grafica;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;

import org.example.Sever.Servidor;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Vista extends JFrame {
	private PanelSnake panel;
	private JLabel puntuacionLabel;
	private String nombreUsuario;  // Recibido de la ventana VentanaNombre
    private int score = 0;  // Puntuación actual
    private final String SERVER_ADDRESS="localhost";
    private final int SERVER=8888;
    private String nombreSala;

	/**
	 * Create the frame.
	 */
	public Vista(String nombreUsuario, String nombreSala) {
        this.nombreUsuario = nombreUsuario;
        this.nombreSala = nombreSala;
		initCompoents();
		this.setLocationRelativeTo(null);
		panel= new PanelSnake(800,30);
		panel.setObserver(this);
		getContentPane().add(panel);
		panel.setBounds(10,10,800,800);
		panel.setOpaque(false);
		PanelFondo fondo = new PanelFondo(800,30);
		getContentPane().add(fondo);
		fondo.setBounds(10,10,800,800);
		this.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					panel.cambiarDireccion("iz");
					break;
				case KeyEvent.VK_RIGHT:
					panel.cambiarDireccion("de");
					break;
				case KeyEvent.VK_UP:
					panel.cambiarDireccion("ar");
					break;
				case KeyEvent.VK_DOWN:
					panel.cambiarDireccion("ab");
					break;
				}
			}
		});
		this.requestFocus(true);
	}

	private void initCompoents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        puntuacionLabel = new JLabel("Puntuación: 0");
        puntuacionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.TRAILING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap(833, Short.MAX_VALUE)
                    .addComponent(puntuacionLabel, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
                    .addGap(40))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(85)
                    .addComponent(puntuacionLabel)
                    .addContainerGap(744, Short.MAX_VALUE))
        );
        getContentPane().setLayout(layout);
        pack();
    }

    public void actualizarPuntuacion(int nuevaPuntuacion) {
        this.score = nuevaPuntuacion;  // Actualizamos la variable interna
        this.puntuacionLabel.setText("Puntuación: " + nuevaPuntuacion);
    }

    public int getPuntuacion() {
        return score;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public  void guardarPuntuacion() {
        // Obtener la puntuación final y el nombre del usuario
        int puntuacionFinal = this.getPuntuacion();
        String nombre = this.getNombreUsuario();
        

        // Crear el objeto Puntuacion
        Puntuacion puntuacion = new Puntuacion(nombre, puntuacionFinal,nombreSala);

        try(Socket s = new Socket(SERVER_ADDRESS,SERVER);
        	ObjectOutputStream o = new ObjectOutputStream(s.getOutputStream());
        		ObjectInputStream in = new ObjectInputStream(s.getInputStream())){
        	
        	o.writeObject("addPuntuacion");
        	o.writeObject(puntuacion);
        	o.flush();
        	o.writeObject("mostrarPuntuaciones");
        	o.writeObject(nombreSala);
        	o.flush();
        	
        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    }
    

}
