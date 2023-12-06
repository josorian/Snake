package org.example.Sever;

import org.example.Grafica.Puntuacion;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Servidor {
    private static final int SERVER_PORT = 8888;
    private static List<Puntuacion> puntuacionList;

    public static void main(String[] args) {
        puntuacionList=parseXMLToList(new File("src/main/java/org/example/Sever/XML/usuarios.xml"));
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Servidor listo para recibir conexiones...");

            while (true) {
                try (Socket socket = serverSocket.accept();
                     ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                     ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())) {

                    System.out.println("Cliente conectado desde: " + socket.getInetAddress().getHostName());
                    Object object = inputStream.readObject();
                    if(object instanceof String){
                        String solicitud = (String) object;
                        if(solicitud.equals("mostrarPuntuaciones")){
                            enviarArchivoXML(outputStream);
                        }
                    }
                    else if (object instanceof Puntuacion){
                        Puntuacion puntuacion = (Puntuacion) object;
                        System.out.println("Puntuacion recibida del cliente: " + puntuacion.toString());
                        puntuacionList.add(puntuacion);

                        // Ordenar la lista de puntuaciones por valor (descendente)
                        puntuacionList.sort(Comparator.comparingInt(Puntuacion::getPuntuacion).reversed());

                        // Guardar la lista ordenada en XML
                        guardarPuntuacionEnXML(puntuacionList);
                    }
                    inputStream.reset();

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void guardarPuntuacionEnXML(List<Puntuacion> puntuacionList) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element rootElement = document.createElement("usuarios");
            document.appendChild(rootElement);

            for (Puntuacion puntuacion : puntuacionList) {
                Element usuarioElement = document.createElement("usuario");
                rootElement.appendChild(usuarioElement);

                Element nombreElement = document.createElement("nombre");
                nombreElement.appendChild(document.createTextNode(puntuacion.getUser()));
                usuarioElement.appendChild(nombreElement);

                Element puntuacionElement = document.createElement("puntuacion");
                puntuacionElement.appendChild(document.createTextNode(String.valueOf(puntuacion.getPuntuacion())));
                usuarioElement.appendChild(puntuacionElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File("src/main/java/org/example/Sever/XML/usuarios.xml"));
            transformer.transform(source, result);

            System.out.println("El archivo usuarios.xml ha sido creado correctamente.");
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }
    private static void enviarArchivoXML(ObjectOutputStream outputStream){
        try {
            File xmlFile = new File("src/main/java/org/example/Sever/XML/usuarios.xml");
            if(!xmlFile.exists()){
                System.out.println("El archivo no existe");
                return;
            }
            byte[] buffer = new byte[(int) xmlFile.length()];
            FileInputStream fileInputStream = new FileInputStream(xmlFile);
            fileInputStream.read(buffer);
            outputStream.writeObject(buffer);
            outputStream.flush();
            System.out.println("Archivo mandado");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static List<Puntuacion> parseXMLToList(File file){
        List<Puntuacion> puntuacions = new CopyOnWriteArrayList<>();
        if(file.exists()){
            try{
                SAXBuilder saxBuilder = new SAXBuilder();
                org.jdom2.Document document = saxBuilder.build(file);
                org.jdom2.Element rootElement = document.getRootElement();
                List<org.jdom2.Element> puntuacionList = rootElement.getChildren("usuario");
                for(org.jdom2.Element userElement: puntuacionList){
                    Puntuacion puntuacion = new Puntuacion(userElement.getChildText("nombre"),Integer.parseInt(userElement.getChildText("puntuacion")));
                    puntuacions.add(puntuacion);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JDOMException e) {
                throw new RuntimeException(e);
            }
        }
        return puntuacions;
    }
}
