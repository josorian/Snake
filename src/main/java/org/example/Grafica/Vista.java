package org.example.Grafica;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.LayoutStyle.ComponentPlacement;

public class Vista extends JFrame {
	private PanelSnake panel;
	private JLabel puntuacion;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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

	/**
	 * Create the frame.
	 */
	public Vista() {
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
	private void initCompoents(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        puntuacion = new JLabel("Puntuación: ");
        puntuacion.setHorizontalAlignment(SwingConstants.CENTER);
        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap(833, Short.MAX_VALUE)
        			.addComponent(puntuacion, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE)
        			.addGap(40))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addGap(85)
        			.addComponent(puntuacion)
        			.addContainerGap(744, Short.MAX_VALUE))
        );
        getContentPane().setLayout(layout);
        pack();
    }
	public void actualizarPuntuacion(int puntuacion) {
		this.puntuacion.setText("Puntuacion: "+puntuacion);
	}
}
