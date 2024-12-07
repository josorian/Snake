package org.example.Sever;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Sala implements Serializable {
    private String nombre;
    private int maxJugadores;
    private List<Jugador> jugadores = new CopyOnWriteArrayList<>();
    private int jugadoresListos = 0;
    private boolean puntuacionesMostrar = false;
    
    // Constructor
    // Precondición: El nombre de la sala no debe ser nulo, y el número máximo de jugadores debe ser mayor que 0.
    // Poscondición: Crea una nueva sala con el nombre y el máximo de jugadores indicados, y establece el número de jugadores listos a 1.
    public Sala(String nombre, int maxJugadores) {
        this.nombre = nombre;
        this.maxJugadores = maxJugadores;
        this.jugadoresListos++;
    }

    // Método para agregar un jugador a la sala
    // Precondición: El nombre y socket del jugador no deben ser nulos y la sala no debe estar llena.
    // Poscondición: Si la sala no está llena, se agrega un nuevo jugador y se devuelve true; si la sala está llena, devuelve false.
    public boolean agregarJugador(String nombreJugador, String socket) {
        if (jugadores.size() < maxJugadores) {
            jugadores.add(new Jugador(nombreJugador, socket));
            return true;
        }
        return false;  // Si la sala está llena
    }

    // Método para verificar si la sala está llena
    // Precondición: La sala debe existir y debe tener jugadores.
    // Poscondición: Devuelve true si el número de jugadores es igual al máximo, de lo contrario devuelve false.
    public boolean estaLlena() {
        return jugadores.size() == maxJugadores;
    }
    
    // Método para enviar las puntuaciones a todos los jugadores de la sala
    // Precondición: Los jugadores deben tener sockets válidos, y debe existir el archivo XML con las puntuaciones de la sala.
    // Poscondición: Envía el archivo XML con las puntuaciones a cada jugador de la sala y elimina el archivo XML después de enviarlo.
    public void mostrarPuntuacionesATodos(String nombreSala) {
        File archivoXML = null;
        for (Jugador jugador : jugadores) {
            archivoXML = new File("src/main/java/org/example/Sever/XML/" + nombreSala + ".xml");
            String port = jugador.getSocket().split(":")[1];
            String local = jugador.getSocket().split(":")[0];
            try (Socket s = new Socket(local, Integer.parseInt(port));
                 DataOutputStream out = new DataOutputStream(s.getOutputStream());
                 FileInputStream fis = new FileInputStream(archivoXML)) {
                String nombre = nombreSala + "\n";
                out.write(nombre.getBytes());

                byte[] buffer = new byte[1028];
                int bytesLeidos;
                while ((bytesLeidos = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesLeidos);
                }
                System.out.println("Puntuaciones enviadas a: " + jugador.getNombre());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        archivoXML.delete();
    }

    // Getters
    // Precondición: La sala debe haber sido correctamente inicializada.
    // Poscondición: Devuelve el nombre de la sala.
    public String getNombre() {
        return nombre;
    }

    // Precondición: La sala debe haber sido correctamente inicializada.
    // Poscondición: Devuelve el número máximo de jugadores de la sala.
    public int getMaxJugadores() {
        return maxJugadores;
    }

    // Método para reducir el número máximo de jugadores por uno
    // Precondición: La cantidad de jugadores no debe ser mayor que cero.
    // Poscondición: Se decrementa el número máximo de jugadores en 1.
    public void setMaxJugadores() {
        this.maxJugadores--;
    }

    // Precondición: La sala debe haber sido correctamente inicializada.
    // Poscondición: Devuelve la lista de jugadores de la sala.
    public List<Jugador> getJugadores() {
        return jugadores;
    }
}
