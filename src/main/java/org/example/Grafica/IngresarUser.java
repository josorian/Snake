package org.example.Grafica;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

public class IngresarUser extends JFrame {
	private JTextField textField;

	private final String SERVER_ADDRESS="localhost";
	private final int SERVER=8888;
	private Puntuacion puntuacion;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IngresarUser frame = new IngresarUser(0);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public IngresarUser(int punt) {
		initCompoents(punt);
	}
	    private void initCompoents(int punt){
	        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	        
	        JLabel lblNewLabel = new JLabel("Usuario");
	        
	        textField = new JTextField();
	        textField.setColumns(10);
	        
	        JButton btnNewButton = new JButton("Guardar");
	        btnNewButton.addActionListener(new ActionListener() {
	        	public void actionPerformed(ActionEvent e) {

					puntuacion = new Puntuacion(getUser(),punt-2);
					System.out.println(puntuacion.toString());
					enviarPuntuacionAlServidor();


				}
	        });
	        GroupLayout layout = new GroupLayout(getContentPane());
	        layout.setHorizontalGroup(
	        	layout.createParallelGroup(Alignment.TRAILING)
	        		.addGroup(layout.createSequentialGroup()
	        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
	        				.addGroup(layout.createSequentialGroup()
	        					.addGap(58)
	        					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
	        				.addGroup(layout.createSequentialGroup()
	        					.addGap(32)
	        					.addGroup(layout.createParallelGroup(Alignment.LEADING)
	        						.addGroup(layout.createSequentialGroup()
	        							.addGap(10)
	        							.addComponent(btnNewButton))
	        						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
	        			.addContainerGap(54, Short.MAX_VALUE))
	        );
	        layout.setVerticalGroup(
	        	layout.createParallelGroup(Alignment.TRAILING)
	        		.addGroup(Alignment.LEADING, layout.createSequentialGroup()
	        			.addGap(112)
	        			.addComponent(lblNewLabel)
	        			.addPreferredGap(ComponentPlacement.RELATED)
	        			.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
	        			.addPreferredGap(ComponentPlacement.RELATED)
	        			.addComponent(btnNewButton)
	        			.addContainerGap(119, Short.MAX_VALUE))
	        );
	        getContentPane().setLayout(layout);
	        pack();
	    }
	    public String getUser() {
	    	return this.textField.getText();
	    }
		private void enviarPuntuacionAlServidor(){
			try(Socket socket = new Socket(SERVER_ADDRESS,SERVER);ObjectOutputStream outputStream=new ObjectOutputStream(socket.getOutputStream())){
				outputStream.writeObject(puntuacion);
				outputStream.flush();
				System.out.println("Objero enviador al servidor");
			} catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
}
