package org.example.Sever;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.example.Grafica.Puntuacion;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ServerHilo implements Runnable {

    private Socket socket;
    private List<Puntuacion> puntuaciones;
    private static List<Sala> salas = new CopyOnWriteArrayList<>(); // Lista de salas compartida entre todos los hilos

    public ServerHilo(Socket socket) {
        this.socket = socket;
        this.puntuaciones = new CopyOnWriteArrayList<>();
    }

    @Override
    public void run() {
        
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;

        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Cliente conectado desde: " + socket.getInetAddress().getHostName());

            // Mientras el socket esté abierto y el cliente esté conectado
            while (!socket.isClosed()) {
                try {
                    Object object = inputStream.readObject();

                    if (object instanceof String) {
                        String solicitud = (String) object;
                        if (solicitud.equals("mostrarPuntuaciones")) {
                        	String nombreSala = (String) inputStream.readObject();
                            String nombre = nombreSala.split(" ")[0];

                            Sala s = encontrarSalaPorNombre(nombre);
                            if (s==null) {
                                enviarArchivoXML(outputStream,nombre);

                            }else {
                            	outputStream.writeObject("SALAS");
                            	s.setMaxJugadores();
                            	if(s.getMaxJugadores()==0) {
                                	s.mostrarPuntuacionesATodos(s.getNombre());

                            	}
                            }

                        	
                        	
                        } else if (solicitud.equals("obtenerSalas")) {
                            enviarSalasDisponibles(outputStream);
                        } else if (solicitud.equals("crearSala")) {
                            String nombreSala = (String) inputStream.readObject();
                            int maxJugadores = 2;
                            Sala nuevaSala = new Sala(nombreSala, maxJugadores);
                            String nombreJugador = (String) inputStream.readObject();
                            Jugador j = new Jugador(nombreJugador, "localhost:9999");
                            nuevaSala.getJugadores().add(j);
                            salas.add(nuevaSala);
                            enviarSalasDisponibles(outputStream);
                        } else if (solicitud.equals("unirseSala")) {
                            String nombreSala = (String) inputStream.readObject();
                            String nombreJugador = (String) inputStream.readObject();
                            unirseSala(outputStream, nombreSala, nombreJugador);
                        } else if(solicitud.equals("addPuntuacion")) {
                        	Puntuacion p = (Puntuacion) inputStream.readObject();
                            String nombreArchivo = p.getSala();
                            String nombre = nombreArchivo.split(" ")[0]+".xml";
                            puntuaciones = parseXMLToList(new File("src/main/java/org/example/Sever/XML/"+ nombre));

                            puntuaciones.add(p);
                            puntuaciones.sort(Comparator.comparingInt(Puntuacion::getPuntuacion).reversed());
                            guardarPuntuacionEnXML(puntuaciones, nombre);
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    break;  // Salir del ciclo si ocurre una excepción, pero no cerrar el socket inmediatamente
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Solo cerramos los flujos y el socket cuando el cliente se desconecta explícitamente
                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // Método para enviar las salas disponibles al cliente
    private synchronized  void enviarSalasDisponibles(ObjectOutputStream outputStream) {
        try {
            outputStream.writeObject("SALAS"); // Indicador
            outputStream.flush();

            List<String> salasDisponibles = new ArrayList<>();
            for (Sala sala : salas) {
                salasDisponibles.add(sala.getNombre() + " (" + sala.getJugadores().size() + "/" + sala.getMaxJugadores() + ")");
            }
            outputStream.writeObject(salasDisponibles); // Datos reales
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para unirse a una sala
    private synchronized  void unirseSala(ObjectOutputStream outputStream, String nombreSala, String nombreJugador) {
        Sala sala = encontrarSalaPorNombre(nombreSala);
        if (sala != null && !sala.estaLlena()) {
            try {
                boolean jugadorAgregado = sala.agregarJugador(nombreJugador, "localhost:9999");
                if (jugadorAgregado) {

                    // Notificar a la interfaz gráfica que la sala está llena
                    if (sala.estaLlena()) {
                    	System.out.println("sala llena ");
                    }
                } else {
                    outputStream.writeObject("La sala está llena.");
                    outputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                outputStream.writeObject("La sala no existe o está llena.");
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para encontrar una sala por su nombre
    private Sala encontrarSalaPorNombre(String nombreSala) {
        for (Sala sala : salas) {
            if (sala.getNombre().equals(nombreSala)) {
            	System.out.println(sala.getJugadores().size());
                return sala;
            }
        }
        return null; // Sala no encontrada
    }


    // Función para guardar las puntuaciones en un archivo XML
    private synchronized void guardarPuntuacionEnXML(List<Puntuacion> puntuaciones, String nombreArchivo) {
        try {
            File file = new File("src/main/java/org/example/Sever/XML/"+nombreArchivo);
            if (!file.exists()) {
                file.createNewFile();
            }

            // Crear el documento XML
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element rootElement = document.createElement("usuarios");
            document.appendChild(rootElement);

            // Añadir las puntuaciones al XML
            for (Puntuacion puntuacion : puntuaciones) {
                Element usuarioElement = document.createElement("usuario");
                usuarioElement.setAttribute("nombre", puntuacion.getUser());
                usuarioElement.setAttribute("puntuacion", String.valueOf(puntuacion.getPuntuacion()));
                rootElement.appendChild(usuarioElement);
            }

            // Guardar el archivo XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            System.out.println("Puntuaciones guardadas en XML.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Función para enviar el archivo XML completo al cliente
    private static void enviarArchivoXML(ObjectOutputStream outputStream, String nombreArchivo) {
        try {
            File xmlFile = new File("src/main/java/org/example/Sever/XML/" + nombreArchivo);
            if (!xmlFile.exists()) {
                System.out.println("El archivo XML no existe.");
                return;
            }
            byte[] buffer = new byte[(int) xmlFile.length()];
            FileInputStream fileInputStream = new FileInputStream(xmlFile);
            fileInputStream.read(buffer);
            outputStream.writeObject(buffer);
            outputStream.flush();
            System.out.println("Archivo XML enviado al cliente.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Función que lee el XML y lo convierte a una lista de puntuaciones
    private static List<Puntuacion> parseXMLToList(File file){
        List<Puntuacion> puntuacions = new CopyOnWriteArrayList<>();
        if(file.exists()){
            try{
                SAXBuilder saxBuilder = new SAXBuilder();
                org.jdom2.Document document = saxBuilder.build(file);
                org.jdom2.Element rootElement = document.getRootElement();
                List<org.jdom2.Element> puntuacionList = rootElement.getChildren("usuario");
                for(org.jdom2.Element userElement: puntuacionList){
                    Puntuacion puntuacion = new Puntuacion(userElement.getAttributeValue("nombre"),Integer.parseInt(userElement.getAttributeValue("puntuacion")),file.getName());
                    puntuacions.add(puntuacion);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JDOMException e) {
                throw new RuntimeException(e);
            }
        } else {
        	try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return puntuacions;
    }
}