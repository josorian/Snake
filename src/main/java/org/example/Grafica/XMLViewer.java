package org.example.Grafica;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.*;

public class XMLViewer extends JFrame {

    private JTextArea textArea;

    public XMLViewer() {
        setTitle("Visor XML");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Archivo");
        JMenuItem menuItem = new JMenuItem("Abrir");
        menuItem.addActionListener(e -> abrirArchivo());
        menu.add(menuItem);
        menuBar.add(menu);

        setJMenuBar(menuBar);
        add(scrollPane, BorderLayout.CENTER);
        convertirXMLToText();
        // Carga automáticamente el contenido de un archivo XML al iniciar la aplicación
        cargarArchivoAutomaticamente("puntuacion.txt"); // Reemplaza con la ruta de tu archivo XML
    }

    private void abrirArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar archivo XML");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos XML", "xml"));

        int seleccion = fileChooser.showOpenDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            try {
                String contenido = leerArchivo(fileChooser.getSelectedFile().getAbsolutePath());
                textArea.setText(contenido);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo XML", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarArchivoAutomaticamente(String rutaArchivo) {
        try {
            String contenido = leerArchivo(rutaArchivo);
            textArea.setText(contenido);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo XML", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String leerArchivo(String rutaArchivo) throws IOException {
        StringBuilder contenido = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo));
        String linea;
        while ((linea = reader.readLine()) != null) {
            contenido.append(linea).append("\n");
        }
        reader.close();
        return contenido.toString();
    }
    private void convertirXMLToText(){
        try {
            // Cargar el archivo XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File("puntuacion.xml"));

            // Preparar el archivo de texto para escribir
            BufferedWriter writer = new BufferedWriter(new FileWriter("puntuacion.txt"));

            // Obtener la lista de nodos de usuario
            NodeList userList = document.getElementsByTagName("usuario");

            // Escribir cada usuario en una línea del archivo de texto
            for (int i = 0; i < userList.getLength(); i++) {
                Element usuario = (Element) userList.item(i);
                String nombre = usuario.getElementsByTagName("nombre").item(0).getTextContent();
                String puntuacion = usuario.getElementsByTagName("puntuacion").item(0).getTextContent();

                // Escribir el usuario en una línea del archivo de texto
                writer.write("Nombre: " + nombre + ", Puntuacion: " + puntuacion);
                writer.newLine();
            }

            // Cerrar el escritor
            writer.close();
            System.out.println("Archivo de texto creado correctamente.");

        } catch (ParserConfigurationException | IOException | org.xml.sax.SAXException e) {
            e.printStackTrace();
        }
    }
}
