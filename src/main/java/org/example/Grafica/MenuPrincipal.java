package org.example.Grafica;

import javax.swing.*;
import java.awt.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuPrincipal extends JFrame{
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
