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
import model.desafio.E_EstadoDesafio;
import model.personaje.Personaje;
import model.personaje.habilidad.Arma;
import model.personaje.habilidad.Armadura;
import model.usuario.Jugador;
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
            File file = new File(getFilePath("desafios"));
            Document doc;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Create new document if file doesn't exist or is empty
            if (!file.exists() || file.length() == 0) {
                doc = builder.newDocument();
                Element rootElement = doc.createElement("desafios");
                doc.appendChild(rootElement);
            } else {
                doc = builder.parse(file);
                doc.normalize();
                removeWhitespaceNodes(doc.getDocumentElement());
            }

            Element root = doc.getDocumentElement();

            // Comprobar si ya existe un desafío con este ID
            NodeList desafioNodes = doc.getElementsByTagName("desafio");
            for (int i = 0; i < desafioNodes.getLength(); i++) {
                Element existingDesafio = (Element) desafioNodes.item(i);
                String existingId = existingDesafio.getElementsByTagName("id").item(0).getTextContent();
                if (existingId.equals(desafio.getDesafioId().toString())) {
                    // Si ya existe, eliminarlo para actualizarlo
                    root.removeChild(existingDesafio);
                    break;
                }
            }

            // Create challenge element
            Element desafioElement = doc.createElement("desafio");
            
            // Add challenge attributes
            Element idElement = doc.createElement("id");
            idElement.appendChild(doc.createTextNode(desafio.getDesafioId().toString()));
            desafioElement.appendChild(idElement);
            
            Element fechaElement = doc.createElement("fecha");
            fechaElement.appendChild(doc.createTextNode(desafio.getFecha().toString()));
            desafioElement.appendChild(fechaElement);
            
            Element desafianteElement = doc.createElement("desafiante");
            desafianteElement.appendChild(doc.createTextNode(desafio.getDesafiante().getUserId().toString()));
            desafioElement.appendChild(desafianteElement);
            
            Element desafiadoElement = doc.createElement("desafiado");
            desafiadoElement.appendChild(doc.createTextNode(desafio.getDesafiado().getUserId().toString()));
            desafioElement.appendChild(desafiadoElement);
            
            Element oroApostadoElement = doc.createElement("oroApostado");
            oroApostadoElement.appendChild(doc.createTextNode(String.valueOf(desafio.getOroApostado())));
            desafioElement.appendChild(oroApostadoElement);
            
            Element estadoElement = doc.createElement("estado");
            estadoElement.appendChild(doc.createTextNode(desafio.getEstado().toString()));
            desafioElement.appendChild(estadoElement);
            
            // Add challenge to XML
            root.appendChild(desafioElement);
            
            // Write changes to XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
            
            return "Desafío guardado con éxito";
        } catch (Exception e) {
            System.err.println("Error al guardar desafío en XML: " + e.getMessage());
            e.printStackTrace();
            return "Error al guardar el desafío: " + e.getMessage();
        }
    }

    @Override
    public Desafio cargarDesafio(UUID desafioId) {
        try {
            String rutaArchivo = getFilePath("desafios");
            File file = new File(rutaArchivo);
            
            if (!file.exists()) {
                System.out.println("El archivo de desafíos no existe");
                return null;
            }
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            
            NodeList nodeList = doc.getElementsByTagName("desafio");
            
            // Get list of all users to match IDs
            List<Usuario> usuarios = cargarUsuarios();
            
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    
                    // Extract challenge ID
                    String idText = element.getElementsByTagName("id").item(0).getTextContent();
                    UUID id = UUID.fromString(idText);
                    
                    // If this is the desafio we're looking for
                    if (id.equals(desafioId)) {
                        // Extract all challenge data
                        String desafianteId = element.getElementsByTagName("desafiante").item(0).getTextContent();
                        String desafiadoId = element.getElementsByTagName("desafiado").item(0).getTextContent();
                        int oroApostado = Integer.parseInt(element.getElementsByTagName("oroApostado").item(0).getTextContent());
                        String estadoText = element.getElementsByTagName("estado").item(0).getTextContent();
                        String fechaText = element.getElementsByTagName("fecha").item(0).getTextContent();
                        
                        // Find users by ID
                        Jugador desafiante = (Jugador) findUsuarioById(usuarios, UUID.fromString(desafianteId));
                        Jugador desafiado = (Jugador) findUsuarioById(usuarios, UUID.fromString(desafiadoId));
                        
                        if (desafiante == null || desafiado == null) {
                            System.out.println("No se encontró el usuario desafiante o desafiado");
                            return null;
                        }
                        
                        // Find matching E_EstadoDesafio enum value by string
                        E_EstadoDesafio estado = null;
                        for (E_EstadoDesafio e : E_EstadoDesafio.values()) {
                            if (e.toString().equals(estadoText)) {
                                estado = e;
                                break;
                            }
                        }
                        
                        if (estado == null) {
                            System.out.println("Estado no válido: " + estadoText);
                            return null;
                        }
                        
                        // Create new Desafio object
                        Desafio desafio = new Desafio();
                        desafio.setDesafioId(id);
                        desafio.setDesafiante(desafiante);
                        desafio.setDesafiado(desafiado);
                        desafio.setOroApostado(oroApostado);
                        desafio.setEstado(estado);
                        // Asumimos que getFecha() devuelve un LocalDateTime
                        desafio.setFechaDesafio(LocalDateTime.parse(fechaText));
                        
                        return desafio;
                    }
                }
            }
            // If no matching desafio was found
            return null;
        } catch (Exception e) {
            System.out.println("Error al cargar desafío: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Desafio> cargarDesafios() {
        System.out.println("Intentando cargar desafíos");
        List<Desafio> desafios = new ArrayList<>();
        try {
            String rutaArchivo = getFilePath("desafios");
            System.out.println("Buscando archivo en: " + rutaArchivo);
            File file = new File(rutaArchivo);
            
            if (!file.exists()) {
                System.out.println("El archivo de desafíos no existe");
                return desafios;
            }
            
            System.out.println("El archivo existe, procediendo a leerlo");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            
            System.out.println("Archivo XML parseado correctamente");
            NodeList nodeList = doc.getElementsByTagName("desafio");
            System.out.println("Número de desafíos encontrados: " + nodeList.getLength());
            
            // Get list of all users to match IDs
            List<Usuario> usuarios = cargarUsuarios();
            
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    System.out.println("Procesando desafío #" + (i+1));
                    
                    // Extract challenge data
                    String idText = element.getElementsByTagName("id").item(0).getTextContent();
                    String desafianteId = element.getElementsByTagName("desafiante").item(0).getTextContent();
                    String desafiadoId = element.getElementsByTagName("desafiado").item(0).getTextContent();
                    int oroApostado = Integer.parseInt(element.getElementsByTagName("oroApostado").item(0).getTextContent());
                    String estadoText = element.getElementsByTagName("estado").item(0).getTextContent();
                    String fechaText = element.getElementsByTagName("fecha").item(0).getTextContent();
                    
                    System.out.println("Datos obtenidos: ID=" + idText + ", Oro=" + oroApostado);
                    
                    // Find users by ID
                    Jugador desafiante = (Jugador) findUsuarioById(usuarios, UUID.fromString(desafianteId));
                    Jugador desafiado = (Jugador) findUsuarioById(usuarios, UUID.fromString(desafiadoId));
                    
                    if (desafiante == null || desafiado == null) {
                        System.out.println("No se encontró el usuario desafiante o desafiado, saltando este desafío");
                        continue;
                    }
                    
                    // Find matching E_EstadoDesafio enum value by string
                    E_EstadoDesafio estado = null;
                    for (E_EstadoDesafio e : E_EstadoDesafio.values()) {
                        if (e.toString().equals(estadoText)) {
                            estado = e;
                            break;
                        }
                    }
                    
                    if (estado == null) {
                        System.out.println("Estado no válido: " + estadoText + ", saltando este desafío");
                        continue;
                    }
                    
                    // Create challenge object
                    Desafio desafio = new Desafio();
                    desafio.setDesafioId(UUID.fromString(idText));
                    desafio.setDesafiante(desafiante);
                    desafio.setDesafiado(desafiado);
                    desafio.setOroApostado(oroApostado);
                    // Asumimos que el método para establecer estado es setEstado()
                    desafio.setEstado(estado);
                    // Asumimos que necesitamos un método para establecer la fecha
                    desafio.setFechaDesafio(LocalDateTime.parse(fechaText));
                    
                    desafios.add(desafio);
                    System.out.println("Desafío añadido a la lista");
                }
            }
            System.out.println("Total de desafíos cargados: " + desafios.size());
        } catch (Exception e) {
            System.out.println("Error al cargar desafíos: " + e.getMessage());
            e.printStackTrace();
        }
        return desafios;
    }

    // Método para actualizar un desafío existente
    @Override
    public void actualizarDesafio(Desafio desafio) {
        // Este método simplemente llama a guardarDesafio, ya que ese método ya maneja
        // la lógica para actualizar un desafío existente
        guardarDesafio(desafio);
    }

    // Helper method to find a user by ID
    private Usuario findUsuarioById(List<Usuario> usuarios, UUID id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getUserId().equals(id)) {
                return usuario;
            }
        }
        return null;
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