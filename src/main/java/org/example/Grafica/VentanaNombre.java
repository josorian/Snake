package org.example.Grafica;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class VentanaNombre extends JFrame {
    private JTextField textFieldNombre;
    private Vista vistaJuego;  // Ventana principal del juego

    private VentanaSalas salas;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    VentanaNombre frame = new VentanaNombre();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public VentanaNombre() {
        setTitle("Ingresar Nombre");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 200);
        getContentPane().setLayout(null);
        
        JLabel lblNombre = new JLabel("Ingresa tu nombre:");
        lblNombre.setBounds(50, 30, 150, 30);
        getContentPane().add(lblNombre);
        
        textFieldNombre = new JTextField();
        textFieldNombre.setBounds(150, 30, 200, 30);
        getContentPane().add(textFieldNombre);
        textFieldNombre.setColumns(10);
        
        JButton btnAceptar = new JButton("Single");
        btnAceptar.setBounds(150, 80, 100, 30);
        getContentPane().add(btnAceptar);
        
        JButton btnSalas = new JButton("Salas");
        btnSalas.setBounds(150, 100, 100, 30);
        getContentPane().add(btnSalas);
        
        // Acción para cuando el usuario hace clic en "Aceptar"
        btnAceptar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombreUsuario = textFieldNombre.getText();
                if (!nombreUsuario.isEmpty()) {
                    // Crear la vista del juego y pasar el nombre del usuario
                    vistaJuego = new Vista(nombreUsuario,"global");
                    vistaJuego.setVisible(true);  // Mostrar la ventana del juego
                    
                    // Cerrar la ventana de ingresar nombre
                    setVisible(false);
                    dispose();
                } else {
                    // Si el nombre está vacío, mostrar un mensaje de advertencia
                    System.out.println("Por favor ingrese un nombre.");
                }
            }
        });
        btnSalas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nombreUsuario = textFieldNombre.getText();
                if (!nombreUsuario.isEmpty()) {
                    // Crear la vista del juego y pasar el nombre del usuario
                    salas = new VentanaSalas(nombreUsuario);
                    salas.setVisible(true);  // Mostrar la ventana del juego
                    
                    // Cerrar la ventana de ingresar nombre
                    setVisible(false);
                    dispose();
                } else {
                    // Si el nombre está vacío, mostrar un mensaje de advertencia
                    System.out.println("Por favor ingrese un nombre.");
                }
            }
        });
    }
}
