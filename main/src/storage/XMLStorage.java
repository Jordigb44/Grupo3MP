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
import model.desafio.Rondas;
import model.personaje.Personaje;
import model.personaje.habilidad.Arma;
import model.personaje.habilidad.Armadura;
import model.usuario.Jugador;
import model.usuario.Usuario;

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
//        System.out.println("Intentando guardar el usuario en: " + getFilePath("usuarios"));
        File file = new File(getFilePath("usuarios"));
        Document doc;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Si el archivo existe pero está vacío, eliminarlo
            if (file.exists() && file.length() == 0) {
//                System.out.println("El archivo está vacío. Eliminándolo...");
                file.delete();
            }

            // Crear nuevo documento si el archivo no existe
            if (!file.exists()) {
                doc = builder.newDocument();
                Element rootElement = doc.createElement("usuarios");
                doc.appendChild(rootElement);
            } else {
                doc = builder.parse(file);
                doc.normalize();
                removeWhitespaceNodes(doc.getDocumentElement());
            }

            Element root = doc.getDocumentElement();

            // 💡 Buscar si el usuario ya existe
            NodeList usuarios = root.getElementsByTagName("usuario");
            for (int i = 0; i < usuarios.getLength(); i++) {
                Element usuarioExistente = (Element) usuarios.item(i);
                String nickExistente = usuarioExistente.getElementsByTagName("nick").item(0).getTextContent();

                if (nickExistente.equals(usuario.getNick().toString())) {
                    root.removeChild(usuarioExistente);  // Eliminar usuario existente
                    break;
                }
            }

            // Crear elemento usuario nuevo/actualizado
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

            Element rolElement = doc.createElement("rol");
            rolElement.appendChild(doc.createTextNode(usuario.getRol() != null ? usuario.getRol() : ""));
            usuarioElement.appendChild(rolElement);

            Element estadoElement = doc.createElement("estado");
            estadoElement.appendChild(doc.createTextNode(usuario.getEstado() != null ? usuario.getEstado() : ""));
            usuarioElement.appendChild(estadoElement);

            Element fechaElement = doc.createElement("fecha");
            fechaElement.appendChild(doc.createTextNode(usuario.getFecha() != null ? usuario.getFecha().toString() : ""));
            usuarioElement.appendChild(fechaElement);

            Element oroElement = doc.createElement("oro");
            oroElement.appendChild(doc.createTextNode(usuario.getOro() != null ? usuario.getOro().toString() : "0"));
            usuarioElement.appendChild(oroElement);

            Element puntosElement = doc.createElement("puntos");
            puntosElement.appendChild(doc.createTextNode(usuario.getPuntos() != null ? usuario.getPuntos().toString() : "0"));
            usuarioElement.appendChild(puntosElement);

            // Agregar el nuevo nodo al root
            root.appendChild(usuarioElement);

            // Guardar el documento XML
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
//            System.out.println("Usuario guardado correctamente.");
            return "Usuario guardado correctamente.";
        } catch (Exception e) {
            System.err.println("Error al guardar usuario en XML: " + e.getMessage());
            e.printStackTrace();
            return "Error al guardar usuario: " + e.getMessage();
        }
    }
    
    @Override
    public String darDeBajaUsuario(String nick) {
        System.out.println("Marcando usuario '" + nick + "' como dado de baja en: " + getFilePath("usuarios"));
        File file = new File(getFilePath("usuarios"));

        if (!file.exists() || file.length() == 0) {
            return "El archivo no existe o está vacío.";
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.normalize();
            removeWhitespaceNodes(doc.getDocumentElement());

            Element root = doc.getDocumentElement();
            NodeList usuarios = root.getElementsByTagName("usuario");
            boolean actualizado = false;

            for (int i = 0; i < usuarios.getLength(); i++) {
                Element usuarioElement = (Element) usuarios.item(i);
                Element nickElement = (Element) usuarioElement.getElementsByTagName("nick").item(0);
                String nickTexto = nickElement.getTextContent();

                if (nickTexto.equals(nick)) {
                    Element estadoElement = (Element) usuarioElement.getElementsByTagName("estado").item(0);
                    if (estadoElement == null) {
                        estadoElement = doc.createElement("estado");
                        usuarioElement.appendChild(estadoElement);
                    }
                    estadoElement.setTextContent("Baja");
                    actualizado = true;
                    break;
                }
            }

            if (!actualizado) {
                return "Usuario con nick '" + nick + "' no encontrado.";
            }

            // Guardar los cambios
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            return "Usuario con nick '" + nick + "' ha sido dado de baja.";
        } catch (Exception e) {
            System.err.println("Error al dar de baja al usuario: " + e.getMessage());
            e.printStackTrace();
            return "Error al dar de baja al usuario: " + e.getMessage();
        }
    }

    @Override
    public List<Usuario> cargarUsuarios() {
        // System.out.println("Intentando cargar usuarios");
        List<Usuario> usuarios = new ArrayList<>();
        try {
            String rutaArchivo = getFilePath("usuarios");
            // System.out.println("Buscando archivo en: " + rutaArchivo);
            File file = new File(rutaArchivo);
            if (!file.exists()) {
                // System.out.println("El archivo de usuarios no existe");
                return usuarios;
            }
            
            // System.out.println("El archivo existe, procediendo a leerlo");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            
            // System.out.println("Archivo XML parseado correctamente");
            NodeList nodeList = doc.getElementsByTagName("usuario");
            // System.out.println("Número de usuarios encontrados: " + nodeList.getLength());

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    // System.out.println("Procesando usuario #" + (i+1));
                    
                    String nick = element.getElementsByTagName("nick").item(0).getTextContent();
                    String nombre = element.getElementsByTagName("nombre").item(0).getTextContent();
                    String password = element.getElementsByTagName("password").item(0).getTextContent();
                    String rol = element.getElementsByTagName("rol").item(0).getTextContent();
                    String estado = element.getElementsByTagName("estado").item(0).getTextContent();
                    String oroStr = element.getElementsByTagName("oro").item(0).getTextContent();
                    Integer oro = oroStr != null && !oroStr.isEmpty() ? Integer.parseInt(oroStr) : 0;
                    String puntosStr = element.getElementsByTagName("puntos").item(0).getTextContent();
                    Integer puntos = puntosStr != null && !puntosStr.isEmpty() ? Integer.parseInt(puntosStr) : 0;
                    
                    // System.out.println("Datos obtenidos: Nick=" + nick + ", Nombre=" + nombre);
                    
                    Usuario usuario = new Usuario(
                        nick,
                        nombre,
                        password,
                        rol,
                        estado,
                        oro,
                        puntos
                    );
                    
                    String idText = element.getElementsByTagName("id").item(0).getTextContent();
                    String fechaText = element.getElementsByTagName("fecha").item(0).getTextContent();
                    // System.out.println("ID=" + idText + ", Fecha=" + fechaText);
                    
                    usuario.setUserId(UUID.fromString(idText));
                    usuario.setFecha(LocalDateTime.parse(fechaText));
                    
                    usuarios.add(usuario);
                    // System.out.println("Usuario añadido a la lista");
                }
            }
            // System.out.println("Total de usuarios cargados: " + usuarios.size());
        } catch (Exception e) {
            // System.out.println("Error al cargar usuarios: " + e.getMessage());
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
            
            // TODO:
            
            // Guardar el XML en el archivo
            File file = new File(getFilePath("personajes"));
            if (!file.exists()) {
                file.createNewFile();
            }

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

            return "Personajes guardados con éxito";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al guardar los personajes";
        }
    }

    // TODO: Imprementar
    @Override
    public List<Personaje> cargarPersonajesUsuario(String nick) {
        List<Personaje> personajes = new ArrayList<>();
        try {
            File file = new File(getFilePath("personajes_usuario"));
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

                        // TODO:
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
    
    // TODO: Imprementar
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

                        // TODO:
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
    public String guardarCombate(Combate combate) {
        try {
            File file = new File("combates.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc;
            Element root;

            if (file.exists()) {
                doc = dBuilder.parse(file);
                root = doc.getDocumentElement();
            } else {
                doc = dBuilder.newDocument();
                root = doc.createElement("combates");
                doc.appendChild(root);
            }

            Element combateElem = doc.createElement("combate");

            // Guardar solo ID del Desafio
            Element desafioIdElem = doc.createElement("desafioId");
            desafioIdElem.setTextContent(combate.getDesafio().getDesafioId().toString());
            combateElem.appendChild(desafioIdElem);

            // Ganador
            if (combate.getGanador() != null) {
                Element ganadorElem = doc.createElement("ganador");
                ganadorElem.setTextContent(combate.getGanador().getNombre());
                combateElem.appendChild(ganadorElem);
            }

            // Rondas
            Element rondasElem = doc.createElement("rondas");
            for (Rondas ronda : combate.getRondas()) {
                Element rondaElem = doc.createElement("ronda");
                Element resultadoElem = doc.createElement("resultado");
                resultadoElem.setTextContent(ronda.getResultado());
                rondaElem.appendChild(resultadoElem);
                rondasElem.appendChild(rondaElem);
            }
            combateElem.appendChild(rondasElem);

            root.appendChild(combateElem);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(file));

            return "Combate guardado";
        } catch (Exception e) {
            e.printStackTrace();
            return "No se pudo guardar el combate";
        }
    }
    
    public List<Combate> cargarCombates() {
        List<Combate> combates = new ArrayList<>();

        try {
            File file = new File("combates.xml");
            if (!file.exists()) return combates;

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            NodeList combateNodes = doc.getElementsByTagName("combate");

            for (int i = 0; i < combateNodes.getLength(); i++) {
                Element combateElem = (Element) combateNodes.item(i);

                // Recuperar Desafio por UUID
                String desafioIdStr = getTextValue(combateElem, "desafioId");
                UUID desafioId = UUID.fromString(desafioIdStr);
                Desafio desafio = new Desafio(desafioId);

                Combate combate = new Combate(desafio);

                // Ganador (por nombre)
                String ganadorNombre = getOptionalTextValue(combateElem, "ganador");
                if (ganadorNombre != null) {
                    Jugador desafiante = desafio.getDesafiante();
                    Jugador desafiado = desafio.getDesafiado();

                    if (ganadorNombre.equals(desafiante.getNombre())) {
                        combate.setGanador(desafiante);
                    } else if (ganadorNombre.equals(desafiado.getNombre())) {
                        combate.setGanador(desafiado);
                    }
                }

                // Rondas
                NodeList rondaNodes = ((Element) combateElem.getElementsByTagName("rondas").item(0)).getElementsByTagName("ronda");
                for (int j = 0; j < rondaNodes.getLength(); j++) {
                    Element rondaElem = (Element) rondaNodes.item(j);
                    String resultado = getTextValue(rondaElem, "resultado");

                    Rondas ronda = new Rondas(desafio.getDesafiante(), desafio.getDesafiado());
                    ronda.iniciarRonda();
                    ronda.setResultado(resultado);
                    combate.getRondas().add(ronda);
                }

                combates.add(combate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return combates;
    }

    private String getTextValue(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list == null || list.getLength() == 0) return null;
        return list.item(0).getTextContent();
    }

    private String getOptionalTextValue(Element parent, String tagName) {
        NodeList list = parent.getElementsByTagName(tagName);
        if (list.getLength() > 0) {
            return list.item(0).getTextContent();
        }
        return null;
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
        // TODO: Implementar la lógica para cargar armas
        // ...
        return armas;
    }

    @Override
    public List<Armadura> cargarArmaduras() {
        List<Armadura> armaduras = new ArrayList<>();
        // TODO: Implementar la lógica para cargar armaduras
        // ...
        return armaduras;
    }

    @Override
    public List<String> getNotificacion(Usuario usuario) {
        List<String> notificaciones = new ArrayList<>();
        try {
            // Definir la ruta del archivo de notificaciones
            File file = new File(getFilePath("notificaciones"));
            
            // Verificar si el archivo existe y tiene contenido
            if (!file.exists() || file.length() == 0) {
                return notificaciones; // Devuelve una lista vacía si no hay notificaciones
            }

            // Preparar el parseo del archivo XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.normalize();
            
            // Eliminar nodos de espacios en blanco
            removeWhitespaceNodes(doc.getDocumentElement());

            // Obtener los usuarios del archivo XML
            NodeList usuarios = doc.getElementsByTagName("usuario");

            // Iterar sobre los usuarios
            for (int i = 0; i < usuarios.getLength(); i++) {
                Element u = (Element) usuarios.item(i);

                // Verificar si el ID del usuario coincide con el del usuario solicitado
                if (u.getAttribute("nick").equals(String.valueOf(usuario.getNick()))) {
                    // Obtener las notificaciones (mensajes) para este usuario
                    NodeList mensajes = u.getElementsByTagName("mensaje");

                    // Iterar sobre los mensajes de este usuario
                    for (int j = 0; j < mensajes.getLength(); j++) {
                        Element mensaje = (Element) mensajes.item(j);
                        String fecha = mensaje.getAttribute("fecha");
                        String texto = mensaje.getTextContent();

                        // Formatear la notificación
                        String notificacion = texto + " (Fecha: " + fecha + ")";
                        notificaciones.add(notificacion); // Añadir la notificación a la lista
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return notificaciones; // Devolver todas las notificaciones encontradas
    }
    
    @Override
    public void setNotificacion(String nick, String mensaje) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc;
            File file = new File(getFilePath("notificaciones"));

            if (!file.exists() || file.length() == 0) {
                doc = builder.newDocument();
                Element rootElement = doc.createElement("notificaciones");
                doc.appendChild(rootElement);
            } else {
                doc = builder.parse(file);
                doc.normalize();
                removeWhitespaceNodes(doc.getDocumentElement());
            }

            Element root = doc.getDocumentElement();
            NodeList usuarios = root.getElementsByTagName("usuario");
            Element usuarioExistente = null;

            for (int i = 0; i < usuarios.getLength(); i++) {
                Element u = (Element) usuarios.item(i);
                if (u.getAttribute("nick").equals(String.valueOf(nick))) {
                    usuarioExistente = u;
                    break;
                }
            }

            if (usuarioExistente != null) {
                NodeList mensajes = usuarioExistente.getElementsByTagName("mensaje");
                if (mensajes.getLength() > 0) {
                    Element mensajeElement = (Element) mensajes.item(0);
                    mensajeElement.setTextContent(mensaje);
                    mensajeElement.setAttribute("fecha", java.time.LocalDateTime.now().toString());
                } else {
                    Element mensajeElement = doc.createElement("mensaje");
                    mensajeElement.setTextContent(mensaje);
                    mensajeElement.setAttribute("fecha", java.time.LocalDateTime.now().toString());
                    usuarioExistente.appendChild(mensajeElement);
                }
            } else {
                Element nuevoUsuario = doc.createElement("usuario");
                nuevoUsuario.setAttribute("nick", String.valueOf(nick));

                Element mensajeElement = doc.createElement("mensaje");
                mensajeElement.setTextContent(mensaje);
                mensajeElement.setAttribute("fecha", java.time.LocalDateTime.now().toString());

                nuevoUsuario.appendChild(mensajeElement);
                root.appendChild(nuevoUsuario);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
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
        try {
            File file = new File(getFilePath("notificaciones"));

            if (!file.exists() || file.length() == 0) return;

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.normalize();

            Element root = doc.getDocumentElement();
            NodeList usuarios = root.getElementsByTagName("usuario");

            for (int i = 0; i < usuarios.getLength(); i++) {
                Element u = (Element) usuarios.item(i);
                if (u.getAttribute("nick").equals(usuario.getNick())) {
                    root.removeChild(u);
                    break;
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}