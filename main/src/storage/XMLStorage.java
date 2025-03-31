package storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import model.Ranking;
import model.desafio.Combate;
import model.desafio.Desafio;
import model.personaje.Personaje;
import model.personaje.habilidad.Arma;
import model.personaje.habilidad.Armadura;
import model.usuario.Usuario;
import notifications.I_Notification;

public class XMLStorage implements I_Storage {
    private String directoryPath;

    // Constructor to initialize the directory path
    public XMLStorage(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    // Helper method to generate the file path for each object type
    private String getFilePath(String objectType) {
        return directoryPath + "/" + objectType + ".xml";
    }

    @Override
    public String guardarUsuario(Usuario usuario) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Crear el elemento raíz
            Element rootElement = doc.createElement("usuarios");
            doc.appendChild(rootElement);

            // Crear el elemento 'usuario'
            Element usuarioElement = doc.createElement("usuario");
            rootElement.appendChild(usuarioElement);

            // Añadir los detalles del usuario al XML
            Element idElement = doc.createElement("id");
            idElement.appendChild(doc.createTextNode(String.valueOf(usuario.getId())));
            usuarioElement.appendChild(idElement);

            Element nombreElement = doc.createElement("nombre");
            nombreElement.appendChild(doc.createTextNode(usuario.getNombre()));
            usuarioElement.appendChild(nombreElement);

            // Guardar el XML en el archivo
            File file = new File(getFilePath("usuarios"));
            if (!file.exists()) {
                file.createNewFile();
            }

            // Escribir el contenido en el archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            return "Usuario guardado con éxito";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al guardar el usuario";
        }
    }

    @Override
    public List<Usuario> cargarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        try {
            File file = new File(getFilePath("usuarios"));
            if (file.exists()) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(file);

                NodeList nodeList = doc.getElementsByTagName("usuario");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element usuarioElement = (Element) node;
                        
                        // TO DO
                        // int id = Integer.parseInt(usuarioElement.getElementsByTagName("id").item(0).getTextContent());
                        // String nombre = usuarioElement.getElementsByTagName("nombre").item(0).getTextContent();

                        // // Crear el objeto Usuario
                        // Usuario usuario = new Usuario(id, nombre);
                        // usuarios.add(usuario);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    @Override
    public String guardarPersonajes(List<Personaje> personajes) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Crear el elemento raíz
            Element rootElement = doc.createElement("personajes");
            doc.appendChild(rootElement);

            // Crear elementos para cada personaje
            for (Personaje personaje : personajes) {
                Element personajeElement = doc.createElement("personaje");
                rootElement.appendChild(personajeElement);

                // Añadir detalles del personaje
                Element idElement = doc.createElement("id");
                idElement.appendChild(doc.createTextNode(String.valueOf(personaje.getId())));
                personajeElement.appendChild(idElement);

                Element nombreElement = doc.createElement("nombre");
                nombreElement.appendChild(doc.createTextNode(personaje.getNombre()));
                personajeElement.appendChild(nombreElement);
            }

            // Guardar el XML en el archivo
            File file = new File(getFilePath("personajes"));
            if (!file.exists()) {
                file.createNewFile();
            }

            // Escribir el contenido en el archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            return "Personajes guardados con éxito";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al guardar los personajes";
        }
    }

    @Override
    public List<Personaje> cargarPersonajes() {
        List<Personaje> personajes = new ArrayList<>();
        try {
            File file = new File(getFilePath("personajes"));
            if (file.exists()) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(file);

                NodeList nodeList = doc.getElementsByTagName("personaje");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element personajeElement = (Element) node;

                        // TO DO
                        // int id = Integer.parseInt(personajeElement.getElementsByTagName("id").item(0).getTextContent());
                        // String nombre = personajeElement.getElementsByTagName("nombre").item(0).getTextContent();

                        // // Crear el objeto Personaje
                        // Personaje personaje = new Personaje(id, nombre);
                        // personajes.add(personaje);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return personajes;
    }

    @Override
    public String guardarRanking(Ranking ranking) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Crear el elemento raíz
            Element rootElement = doc.createElement("ranking");
            doc.appendChild(rootElement);

            // Añadir detalles del ranking
            Element puntuacionElement = doc.createElement("puntuacion");
            puntuacionElement.appendChild(doc.createTextNode(String.valueOf(ranking.getPuntuacion())));
            rootElement.appendChild(puntuacionElement);

            // Guardar el XML en el archivo
            File file = new File(getFilePath("ranking"));
            if (!file.exists()) {
                file.createNewFile();
            }

            // Escribir el contenido en el archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            return "Ranking guardado con éxito";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al guardar el ranking";
        }
    }

    @Override
    public List<Ranking> cargarRanking() {
        List<Ranking> rankingList = new ArrayList<>();
        try {
            File file = new File(getFilePath("ranking"));
            if (file.exists()) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(file);

                NodeList nodeList = doc.getElementsByTagName("ranking");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element rankingElement = (Element) node;
                        
                        // ___TO DO___
                        // int puntuacion = Integer.parseInt(rankingElement.getElementsByTagName("puntuacion").item(0).getTextContent());

                        // // Crear el objeto Ranking
                        // Ranking ranking = new Ranking(puntuacion);
                        // rankingList.add(ranking);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rankingList;
    }

    @Override
    public String guardarCombates(List<Combate> combates) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'guardarCombates'");
    }

    @Override
    public List<Combate> cargarCombates() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cargarCombates'");
    }

    @Override
    public String guardarDesafio(Desafio desafio) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'guardarDesafio'");
    }

    @Override
    public List<Desafio> cargarDesafios() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cargarDesafios'");
    }

    @Override
    public List<Arma> cargarArmas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cargarArmas'");
    }

    @Override
    public List<Armadura> cargarArmaduras() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cargarArmaduras'");
    }

    @Override
    public I_Notification getNotificacion(Usuario usuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNotificacion'");
    }

    @Override
    public void setNotificacion(Usuario usuario, String mensaje) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setNotificacion'");
    }

    @Override
    public void deleteNotificacion(Usuario usuario) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteNotificacion'");
    }
}