package storage;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        return directoryPath + objectType + ".xml";
    }

    // Helper method to remove whitespace-only text nodes
    private void removeWhitespaceNodes(Node node) {
        NodeList children = node.getChildNodes();
        for (int i = children.getLength() - 1; i >= 0; i--) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE && child.getNodeValue().trim().isEmpty()) {
                node.removeChild(child);
            } else if (child.getNodeType() == Node.ELEMENT_NODE) {
                removeWhitespaceNodes(child);
            }
        }
    }

    @Override
    public String guardarUsuario(Usuario usuario) {
        System.out.println("Intentando guardar el usuario en: " + getFilePath("usuarios"));
        File file = new File(getFilePath("usuarios"));
        Document doc;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // 🔹 Si el archivo existe pero está vacío, eliminarlo
            if (file.exists() && file.length() == 0) {
                System.out.println("El archivo está vacío. Eliminándolo...");
                file.delete();
            }

            // 🔹 Crear nuevo documento si el archivo no existe
            if (!file.exists()) {
                doc = builder.newDocument();
                Element rootElement = doc.createElement("usuarios");
                doc.appendChild(rootElement);
            } else {
                doc = builder.parse(file);
                // Normalize the document to remove excessive whitespace
                doc.normalize();
                // Remove all text nodes that are only whitespace
                removeWhitespaceNodes(doc.getDocumentElement());
            }

            Element root = doc.getDocumentElement();

            // Crear elemento usuario
            Element usuarioElement = doc.createElement("usuario");

            Element idElement = doc.createElement("id");
            idElement.appendChild(doc.createTextNode(usuario.getUserId().toString()));
            usuarioElement.appendChild(idElement);

            Element nickElement = doc.createElement("nick");
            nickElement.appendChild(doc.createTextNode(usuario.getNick()));
            usuarioElement.appendChild(nickElement);

            Element nombreElement = doc.createElement("nombre");
            nombreElement.appendChild(doc.createTextNode(usuario.getNombre()));
            usuarioElement.appendChild(nombreElement);

            Element passwordElement = doc.createElement("password");
            passwordElement.appendChild(doc.createTextNode(usuario.getPassword()));
            usuarioElement.appendChild(passwordElement);

            // 🔹 Evitar valores null en XML
            Element rolElement = doc.createElement("rol");
            rolElement.appendChild(doc.createTextNode(usuario.getRol() != null ? usuario.getRol() : ""));
            usuarioElement.appendChild(rolElement);

            Element estadoElement = doc.createElement("estado");
            estadoElement.appendChild(doc.createTextNode(usuario.getEstado() != null ? usuario.getEstado() : ""));
            usuarioElement.appendChild(estadoElement);

            Element fechaElement = doc.createElement("fecha");
            fechaElement.appendChild(doc.createTextNode(usuario.getFecha() != null ? usuario.getFecha().toString() : ""));
            usuarioElement.appendChild(fechaElement);

            // Agregar nuevo usuario al XML
            root.appendChild(usuarioElement);

            // Escribir cambios en el archivo XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            return "Usuario guardado correctamente.";
        } catch (Exception e) {
            System.err.println("Error al guardar usuario en XML: " + e.getMessage());
            e.printStackTrace();
            return "Error al guardar usuario: " + e.getMessage();
        }
    }

    @Override
    public List<Usuario> cargarUsuarios() {
        System.out.println("Intentando cargar usuarios");
        List<Usuario> usuarios = new ArrayList<>();
        try {
            String rutaArchivo = getFilePath("usuarios");
            System.out.println("Buscando archivo en: " + rutaArchivo);
            File file = new File(rutaArchivo);
            if (!file.exists()) {
                System.out.println("El archivo de usuarios no existe");
                return usuarios;
            }
            
            System.out.println("El archivo existe, procediendo a leerlo");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            
            System.out.println("Archivo XML parseado correctamente");
            NodeList nodeList = doc.getElementsByTagName("usuario");
            System.out.println("Número de usuarios encontrados: " + nodeList.getLength());

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    System.out.println("Procesando usuario #" + (i+1));
                    
                    String nick = element.getElementsByTagName("nick").item(0).getTextContent();
                    String nombre = element.getElementsByTagName("nombre").item(0).getTextContent();
                    String password = element.getElementsByTagName("password").item(0).getTextContent();
                    String rol = element.getElementsByTagName("rol").item(0).getTextContent();
                    String estado = element.getElementsByTagName("estado").item(0).getTextContent();
                    
                    System.out.println("Datos obtenidos: Nick=" + nick + ", Nombre=" + nombre);
                    
                    Usuario usuario = new Usuario(
                        nick,
                        nombre,
                        password,
                        rol,
                        estado
                    );
                    
                    String idText = element.getElementsByTagName("id").item(0).getTextContent();
                    String fechaText = element.getElementsByTagName("fecha").item(0).getTextContent();
                    System.out.println("ID=" + idText + ", Fecha=" + fechaText);
                    
                    usuario.setUserId(UUID.fromString(idText));
                    usuario.setFecha(LocalDateTime.parse(fechaText));
                    
                    usuarios.add(usuario);
                    System.out.println("Usuario añadido a la lista");
                }
            }
            System.out.println("Total de usuarios cargados: " + usuarios.size());
        } catch (Exception e) {
            System.out.println("Error al cargar usuarios: " + e.getMessage());
            e.printStackTrace();
        }
        return usuarios;
    }

    // TODO: Imprementar
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
            
            // ...
            
            // Guardar el XML en el archivo
            File file = new File(getFilePath("personajes"));
            if (!file.exists()) {
                file.createNewFile();
            }

            // Escribir el contenido en el archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
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
                
                // Normalize the document
                doc.normalize();

                NodeList nodeList = doc.getElementsByTagName("personaje");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {

                        // TO DO
                    	// Element personajeElement = (Element) node;
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
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
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
                
                // Normalize the document
                doc.normalize();

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
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Crear el elemento raíz
            Element rootElement = doc.createElement("combates");
            doc.appendChild(rootElement);

            // Implementar la lógica para guardar combates
            // ...

            // Guardar el XML en el archivo
            File file = new File(getFilePath("combates"));
            if (!file.exists()) {
                file.createNewFile();
            }

            // Escribir el contenido en el archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            return "Combates guardados con éxito";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al guardar los combates";
        }
    }

    @Override
    public List<Combate> cargarCombates() {
        List<Combate> combates = new ArrayList<>();
        // Implementar la lógica para cargar combates
        // ...
        return combates;
    }

    @Override
    public String guardarDesafio(Desafio desafio) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Crear el elemento raíz
            Element rootElement = doc.createElement("desafios");
            doc.appendChild(rootElement);

            // Implementar la lógica para guardar desafío
            // ...

            // Guardar el XML en el archivo
            File file = new File(getFilePath("desafios"));
            if (!file.exists()) {
                file.createNewFile();
            }

            // Escribir el contenido en el archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            return "Desafío guardado con éxito";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al guardar el desafío";
        }
    }

    @Override
    public List<Desafio> cargarDesafios() {
        List<Desafio> desafios = new ArrayList<>();
        // Implementar la lógica para cargar desafíos
        // ...
        return desafios;
    }

    @Override
    public List<Arma> cargarArmas() {
        List<Arma> armas = new ArrayList<>();
        // Implementar la lógica para cargar armas
        // ...
        return armas;
    }

    @Override
    public List<Armadura> cargarArmaduras() {
        List<Armadura> armaduras = new ArrayList<>();
        // Implementar la lógica para cargar armaduras
        // ...
        return armaduras;
    }

    @Override
    public I_Notification getNotificacion(Usuario usuario) {
        // Implementar la lógica para obtener notificación
        return null;
    }

    @Override
    public void setNotificacion(Usuario usuario, String mensaje) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc;
            File file = new File(getFilePath("notificaciones"));
            
            // Crear nuevo documento o cargar existente
            if (!file.exists() || file.length() == 0) {
                doc = builder.newDocument();
                Element rootElement = doc.createElement("notificaciones");
                doc.appendChild(rootElement);
            } else {
                doc = builder.parse(file);
                doc.normalize();
                removeWhitespaceNodes(doc.getDocumentElement());
            }
            
            // Implementar lógica para guardar notificación
            // ...
            
            // Escribir contenido en archivo
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteNotificacion(Usuario usuario) {
        // Implementar lógica para eliminar notificación
    }
}