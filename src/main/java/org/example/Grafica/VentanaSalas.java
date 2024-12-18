package org.example.Grafica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class VentanaSalas extends JFrame {
    private JTextField txtCrearSala;
    private JButton btnCrearSala;
    private JButton btnUnirseSala;
    private JButton btnIniciarJuego;  
    private String nombreUsuario;
    private final String SERVER_ADDRESS = "localhost";
    private final int SERVER_PORT = 8888;

    private JList<String> listaSalas;
    private DefaultListModel<String> modeloSalas;
    
    private String salaNombre;

    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    
    private Vista vistaJuego;  

    // Constructor de la ventana
    // Precondición: El parámetro `usuario` no debe ser nulo ni vacío.
    // Poscondición: Inicializa la ventana con elementos visuales para gestionar salas.
    public VentanaSalas(String usuario) {
        this.nombreUsuario = usuario;
        setTitle("Gestión de Salas");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel lblSala = new JLabel("Crear o unirse a una sala:");
        txtCrearSala = new JTextField(20);
        modeloSalas = new DefaultListModel<>();
        listaSalas = new JList<>(modeloSalas);
        listaSalas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(listaSalas);
        scrollPane.setPreferredSize(new Dimension(300, 150));
        // Botón para crear una sala
        // Precondición: El campo `txtCrearSala` no debe estar vacío.
        // Poscondición: Envía una solicitud al servidor para crear una nueva sala.
        btnCrearSala = new JButton("Crear Sala");
        btnCrearSala.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salaNombre = txtCrearSala.getText();
                if (!salaNombre.trim().isEmpty()) {
                    try {
                        outputStream.writeObject("crearSala");
                        outputStream.writeObject(salaNombre);
                        outputStream.writeObject(nombreUsuario);
                        outputStream.flush();

                        modeloSalas.addElement(salaNombre + " (1/2)");  // Iniciar con 0 jugadores
                        JOptionPane.showMessageDialog(VentanaSalas.this, "Sala creada: " + salaNombre);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(VentanaSalas.this, "Error al conectar con el servidor.");
                    }
                } else {
                    JOptionPane.showMessageDialog(VentanaSalas.this, "Por favor, ingresa un nombre de sala.");
                }
            }
        });
        // Botón para unirse a una sala
        // Precondición: Se debe seleccionar una sala de la lista.
        // Poscondición: Envía una solicitud al servidor para unirse a la sala seleccionada.
        btnUnirseSala = new JButton("Unirse a Sala");
        btnUnirseSala.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salaNombre = listaSalas.getSelectedValue();
                if (salaNombre != null) {
                    try {
                        outputStream.writeObject("unirseSala");
                        outputStream.writeObject(salaNombre.split(" ")[0]);  // Obtener solo el nombre de la sala
                        outputStream.writeObject(nombreUsuario);
                        outputStream.flush();

                        JOptionPane.showMessageDialog(VentanaSalas.this, "Sala unido: " + salaNombre);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(VentanaSalas.this, "Error al unirse a la sala.");
                    } 
                } else {
                    JOptionPane.showMessageDialog(VentanaSalas.this, "Por favor, selecciona una sala para unirte.");
                }
            }
        });

        // Botón para iniciar el juego
        // Precondición: Se debe seleccionar una sala de la lista.
        // Poscondición: Abre la ventana principal del juego y cierra esta ventana.        
        btnIniciarJuego = new JButton("Iniciar Juego");
        btnIniciarJuego.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	salaNombre = listaSalas.getSelectedValue();
				if (salaNombre != null) {
					// Crear la vista del juego y pasar el nombre del usuario
                    vistaJuego = new Vista(nombreUsuario,salaNombre);
                    vistaJuego.setVisible(true);  // Mostrar la ventana del juego
                    
                    // Cerrar la ventana de ingresar nombre
                    setVisible(false);
                    dispose();
				}
            }
        });

        panel.add(lblSala);
        panel.add(txtCrearSala);
        panel.add(btnCrearSala);
        panel.add(btnUnirseSala);
        panel.add(scrollPane);
        panel.add(btnIniciarJuego);  
        getContentPane().add(panel);

        // Establecer conexión al servidor
        // Precondición: El servidor debe estar en ejecución en `SERVER_ADDRESS` y `SERVER_PORT`.
        // Poscondición: Se conecta al servidor y comienza a recibir actualizaciones de salas.
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            // Enviar una solicitud para obtener la lista de salas
            outputStream.writeObject("obtenerSalas");
            outputStream.flush();

            // Hilo para escuchar y actualizar la lista de salas
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                        	Object mensaje = inputStream.readObject();

                        	if (mensaje instanceof String) {
                        	    String indicador = (String) mensaje;

                        	    switch (indicador) {
                        	        case "SALAS":
                        	            // Esperamos recibir una lista después
                        	            Object data = inputStream.readObject();
                        	            	List<String> salas = (List<String>) data;
                            	            SwingUtilities.invokeLater(() -> {
                            	                modeloSalas.clear();
                            	                for (String sala : salas) {
                            	                    modeloSalas.addElement(sala);
                            	                }
                            	            });
            
                        	            
                        	            break;
                        	     

                        	        default:
                        	            System.err.println("Indicador desconocido: " + indicador);
                        	    }
                        	}
                        	
                            
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(VentanaSalas.this, "Error al recibir las actualizaciones del servidor.");
                    } 
                }
            }).start();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar con el servidor.");
        }
        
        

        // Cerrar la conexión al cerrar la ventana
        // Precondición: La conexión debe estar abierta.
        // Poscondición: Libera los recursos de red utilizados.
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                try {
                    if (outputStream != null) outputStream.close();
                    if (inputStream != null) inputStream.close();
                    if (socket != null && !socket.isClosed()) socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Método para habilitar el botón "Iniciar Juego" cuando la sala está llena
    public void habilitarBotonIniciarJuego() {
        btnIniciarJuego.setEnabled(true);  // Habilitar el botón
    }

    // Método para actualizar el número de jugadores en la lista de salas
    public void actualizarNumeroJugadores(String nombreSala, int numJugadores, int maxJugadores) {
        for (int i = 0; i < modeloSalas.size(); i++) {
            String sala = modeloSalas.get(i);
            if (sala.startsWith(nombreSala)) {
                modeloSalas.set(i, nombreSala + " (" + numJugadores + "/" + maxJugadores + ")");
                break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaSalas("Usuario").setVisible(true);
            }
        });
    }
}
