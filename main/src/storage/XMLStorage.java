package storage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import model.desafio.Combate;
import model.desafio.Desafio;
import model.desafio.E_EstadoDesafio;
import model.desafio.Rondas;
import model.personaje.Esbirro;
import model.personaje.Personaje;
import model.personaje.habilidad.Arma;
import model.personaje.habilidad.Armadura;
import model.personaje.habilidad.Debilidad;
import model.personaje.habilidad.Disciplina;
import model.personaje.habilidad.Don;
import model.personaje.habilidad.Fortaleza;
import model.personaje.habilidad.Talento;
import model.usuario.Jugador;
import model.usuario.Usuario;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
			rolElement.appendChild(doc.createTextNode(usuario.getRol() != null ? usuario.getRol() : "jugador"));
			usuarioElement.appendChild(rolElement);

			Element estadoElement = doc.createElement("estado");
			estadoElement.appendChild(doc.createTextNode(usuario.getEstado() != null ? usuario.getEstado() : "activo"));
			usuarioElement.appendChild(estadoElement);

			Element fechaElement = doc.createElement("fecha");
			fechaElement.appendChild(doc.createTextNode(usuario.getFecha() != null ? usuario.getFecha().toString() : ""));
			usuarioElement.appendChild(fechaElement);

			Element oroElement = doc.createElement("oro");
			oroElement.appendChild(doc.createTextNode(String.valueOf(usuario.getOro())));
			usuarioElement.appendChild(oroElement);

			Element puntosElement = doc.createElement("puntos");
			puntosElement.appendChild(doc.createTextNode(String.valueOf(usuario.getPuntos())));
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
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

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
					String idString = element.getElementsByTagName("id").item(0).getTextContent();
					UUID id = UUID.fromString(idString);
					String nick = element.getElementsByTagName("nick").item(0).getTextContent();
					String nombre = element.getElementsByTagName("nombre").item(0).getTextContent();
					String password = element.getElementsByTagName("password").item(0).getTextContent();
					String rol = element.getElementsByTagName("rol").item(0).getTextContent();
					String estado = element.getElementsByTagName("estado").item(0).getTextContent();
					String oroStr = element.getElementsByTagName("oro").item(0).getTextContent();
					Integer oro = Integer.parseInt(oroStr);
					String puntosStr = element.getElementsByTagName("puntos").item(0).getTextContent();
					Integer puntos = Integer.parseInt(puntosStr);

//					System.out.println("Datos obtenidos: Nick=" + nick + ", Nombre=" + nombre);
					Usuario usuario = new Usuario(
							id,
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
//			System.out.println("Total de usuarios cargados: " + usuarios.size());
		} catch (Exception e) {
			// System.out.println("Error al cargar usuarios: " + e.getMessage());
			e.printStackTrace();
		}
		return usuarios;
	}

	@Override
	public String guardarPersonajes(List<Personaje> personajes) {
	    try {
	        // Crear el documento XML si no existe o cargar el existente
	        Document doc = getDoc("personajes");
	        if (doc == null) {
	            System.out.println("Error al cargar el documento de personajes.");
	            return "Error al cargar el documento";
	        }

	        // Obtener o crear el nodo raíz
	        Element rootElement = doc.getDocumentElement();
	        if (rootElement == null) {
	            rootElement = doc.createElement("personajes");
	            doc.appendChild(rootElement);
	        }

	        // Agregar personajes
	        for (Personaje personaje : personajes) {
	            Element personajeElement = doc.createElement("personaje");
	            rootElement.appendChild(personajeElement);

	            Element idElement = doc.createElement("id");
	            idElement.appendChild(doc.createTextNode(String.valueOf(personaje.getId())));
	            personajeElement.appendChild(idElement);

	            Element nombreElement = doc.createElement("nombre");
	            nombreElement.appendChild(doc.createTextNode(personaje.getNombre()));
	            personajeElement.appendChild(nombreElement);

	            Element oroElement = doc.createElement("oro");
	            oroElement.appendChild(doc.createTextNode(String.valueOf(personaje.getOro())));
	            personajeElement.appendChild(oroElement);

	            Element saludElement = doc.createElement("salud");
	            saludElement.appendChild(doc.createTextNode(String.valueOf(personaje.getSalud())));
	            personajeElement.appendChild(saludElement);

	            for (Arma arma : personaje.getArmas()) {
	                Element armaElement = doc.createElement("arma");
	                armaElement.appendChild(doc.createTextNode(arma.getNombre()));
	                personajeElement.appendChild(armaElement);
	            }

	            for (Armadura armadura : personaje.getArmaduras()) {
	                Element armaduraElement = doc.createElement("armadura");
	                armaduraElement.appendChild(doc.createTextNode(armadura.getNombre()));
	                personajeElement.appendChild(armaduraElement);
	            }

	            for (Fortaleza fortaleza : personaje.getFortalezas()) {
	                Element fortalezaElement = doc.createElement("fortaleza");
	                fortalezaElement.appendChild(doc.createTextNode(fortaleza.getNombre()));
	                personajeElement.appendChild(fortalezaElement);
	            }

	            for (Debilidad debilidad : personaje.getDebilidades()) {
	                Element debilidadElement = doc.createElement("debilidad");
	                debilidadElement.appendChild(doc.createTextNode(debilidad.getNombre()));
	                personajeElement.appendChild(debilidadElement);
	            }

	            for (Esbirro esbirro : personaje.getEsbirros()) {
	                Element esbirroElement = doc.createElement("esbirro");

	                Element tipoElement = doc.createElement("tipo");
	                tipoElement.appendChild(doc.createTextNode(esbirro.getTipo()));
	                esbirroElement.appendChild(tipoElement);

	                Element nombreEsbirroElement = doc.createElement("nombre");
	                nombreEsbirroElement.appendChild(doc.createTextNode(esbirro.getNombre()));
	                esbirroElement.appendChild(nombreEsbirroElement);

	                Element saludEsbirroElement = doc.createElement("salud");
	                saludEsbirroElement.appendChild(doc.createTextNode(String.valueOf(esbirro.getSalud())));
	                esbirroElement.appendChild(saludEsbirroElement);

	                personajeElement.appendChild(esbirroElement);
	            }
	        }

	        // Guardar el documento actualizado en el archivo
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        DOMSource source = new DOMSource(doc);
	        StreamResult result = new StreamResult(new File(getFilePath("personajes")));
	        transformer.transform(source, result);

	        return "Personajes guardados con éxito";

	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error al guardar los personajes";
	    }
	}
	
	public boolean guardarPersonajesUsuario(String nick, List<Personaje> personajes) {
	    try {
	        File file = new File(getFilePath("personajes_jugadores"));
	        if (!file.exists()) {
	            file.createNewFile(); // crea el archivo si no existe
	        }

	        Document doc = getDoc("personajes_jugadores");
	        if (doc == null) {
	            System.out.println("Error al cargar el documento de personajes.");
	            return false;
	        }

	        Element rootElement = doc.getDocumentElement();
	        if (rootElement == null) {
	            rootElement = doc.createElement("personajes");
	            doc.appendChild(rootElement);
	        }

	        for (Personaje personaje : personajes) {
	            NodeList listaPersonajes = rootElement.getElementsByTagName("personaje");
	            boolean reemplazado = false;

	            for (int i = 0; i < listaPersonajes.getLength(); i++) {
	                Element personajeExistente = (Element) listaPersonajes.item(i);

	                String jugadorExistente = personajeExistente.getElementsByTagName("jugador").item(0).getTextContent();
	                String nombreExistente = personajeExistente.getElementsByTagName("nombre").item(0).getTextContent();

	                if (jugadorExistente.equals(nick) && nombreExistente.equals(personaje.getNombre())) {
	                    Element nuevoElemento = crearElementoPersonaje(doc, personaje, nick);
	                    rootElement.replaceChild(nuevoElemento, personajeExistente);
	                    reemplazado = true;
	                    break;
	                }
	            }

	            if (!reemplazado) {
	                Element nuevoElemento = crearElementoPersonaje(doc, personaje, nick);
	                rootElement.appendChild(nuevoElemento);
	            }
	        }

	        // Guardar el documento en el archivo XML
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // para que quede bonito
	        DOMSource source = new DOMSource(doc);
	        StreamResult result = new StreamResult(file);
	        transformer.transform(source, result);

	        System.out.println("Personajes guardados correctamente en 'personajes_jugadores.xml'.");
	        return true;

	    } catch (Exception e) {
	        System.err.println("Error al guardar personajes del usuario: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public int cargarSaludPorNombre(String nombrePersonaje) {
	    try {
	        // Obtener la lista de personajes usando el método cargarPersonajes()
	        List<Personaje> personajes = cargarPersonajes();  // Método que carga los personajes desde el XML
	        
	        if (personajes == null || personajes.isEmpty()) {
	            System.out.println("No se encontraron personajes.");
	            return -1;  // Si no hay personajes cargados, retornamos -1
	        }

	        // Recorrer la lista de personajes
	        for (Personaje personaje : personajes) {
	            // Comparar el nombre del personaje
	            if (personaje.getNombre().equals(nombrePersonaje)) {
	                // Retornar la salud del personaje encontrado
	                return personaje.getSalud();
	            }
	        }

	        // Si no se encuentra el personaje, retornar un valor de error
	        System.out.println("Personaje no encontrado.");
	        return -1;
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        return -1;  // Si hay algún error, devolver -1
	    }
	}
	
	private Element crearElementoPersonaje(Document doc, Personaje personaje, String nick) {
	    Element personajeElement = doc.createElement("personaje");

	    Element jugadorElement = doc.createElement("jugador");
	    jugadorElement.appendChild(doc.createTextNode(nick));
	    personajeElement.appendChild(jugadorElement);

	    Element nombreElement = doc.createElement("nombre");
	    nombreElement.appendChild(doc.createTextNode(personaje.getNombre()));
	    personajeElement.appendChild(nombreElement);

	    for (Arma arma : personaje.getArmas()) {
	        Element armaElement = doc.createElement("arma");
	        armaElement.appendChild(doc.createTextNode(arma.getNombre()));
	        personajeElement.appendChild(armaElement);
	    }

	    for (Armadura armadura : personaje.getArmaduras()) {
	        Element armaduraElement = doc.createElement("armadura");
	        armaduraElement.appendChild(doc.createTextNode(armadura.getNombre()));
	        personajeElement.appendChild(armaduraElement);
	    }

	    for (Fortaleza fortaleza : personaje.getFortalezas()) {
	        Element fortalezaElement = doc.createElement("fortaleza");
	        fortalezaElement.appendChild(doc.createTextNode(fortaleza.getNombre()));
	        personajeElement.appendChild(fortalezaElement);
	    }

	    for (Debilidad debilidad : personaje.getDebilidades()) {
	        Element debilidadElement = doc.createElement("debilidad");
	        debilidadElement.appendChild(doc.createTextNode(debilidad.getNombre()));
	        personajeElement.appendChild(debilidadElement);
	    }

	    for (Esbirro esbirro : personaje.getEsbirros()) {
	        Element esbirroElement = doc.createElement("esbirro");
	        esbirroElement.appendChild(doc.createTextNode(esbirro.getNombre()));
	        personajeElement.appendChild(esbirroElement);
	    }

	    Element oroElement = doc.createElement("oro");
	    oroElement.appendChild(doc.createTextNode(String.valueOf(personaje.getOro())));
	    personajeElement.appendChild(oroElement);

	    return personajeElement;
	}
	
	@Override
	public List<Personaje> cargarPersonajesUsuario(String nick) {
	    List<Personaje> personajes = new ArrayList<>();
	    try {
	        // Obtener el documento XML desde el archivo "personajes_jugadores.xml"
	        Document doc = getDoc("personajes_jugadores");
	        if (doc == null) {
	            System.out.println("Archivo de personajes no encontrado en: " + getFilePath("personajes_jugadores"));
	            return personajes;
	        }

	        // Obtener la lista de personajes del archivo
	        NodeList nodeList = doc.getElementsByTagName("personaje");
	        for (int i = 0; i < nodeList.getLength(); i++) {
	            Node node = nodeList.item(i);
	            if (node.getNodeType() == Node.ELEMENT_NODE) {
	                Element personajeElement = (Element) node;

	                // Verificar si este personaje pertenece al usuario especificado
	                NodeList jugadorNodes = personajeElement.getElementsByTagName("jugador");
	                if (jugadorNodes.getLength() > 0) {
	                    String jugadorNick = jugadorNodes.item(0).getTextContent();

	                    // Si el personaje pertenece al usuario especificado, procesarlo
	                    if (jugadorNick.equals(nick)) {
	                        // Obtener atributos básicos del personaje
	                        String nombre = personajeElement.getElementsByTagName("nombre").item(0).getTextContent();

	                        // Cargar armas para este personaje
	                        List<Arma> armas = new ArrayList<>();
	                        NodeList armasNodes = personajeElement.getElementsByTagName("arma");
	                        for (int j = 0; j < armasNodes.getLength(); j++) {
	                            String nombreArma = armasNodes.item(j).getTextContent();
	                            // Buscar el arma en la lista de armas disponibles
	                            for (Arma arma : cargarArmas()) {
	                                if (arma.getNombre().equals(nombreArma)) {
	                                    armas.add(arma);
	                                    break;
	                                }
	                            }
	                        }

	                        // Cargar armaduras para este personaje
	                        List<Armadura> armaduras = new ArrayList<>();
	                        Armadura armaduraActiva = null;
	                        NodeList armadurasNodes = personajeElement.getElementsByTagName("armadura");
	                        for (int j = 0; j < armadurasNodes.getLength(); j++) {
	                            String nombreArmadura = armadurasNodes.item(j).getTextContent();
	                            // Buscar la armadura en la lista de armaduras disponibles
	                            for (Armadura armadura : cargarArmaduras()) {
	                                if (armadura.getNombre().equals(nombreArmadura)) {
	                                    armaduras.add(armadura);
	                                    // La primera armadura se establece como activa
	                                    if (armaduraActiva == null) {
	                                        armaduraActiva = armadura;
	                                    }
	                                    break;
	                                }
	                            }
	                        }

	                        // Cargar fortalezas para este personaje
	                        List<Fortaleza> fortalezas = new ArrayList<>();
	                        NodeList fortalezasNodes = personajeElement.getElementsByTagName("fortaleza");
	                        for (int j = 0; j < fortalezasNodes.getLength(); j++) {
	                            String nombreFortaleza = fortalezasNodes.item(j).getTextContent();
	                            for (Fortaleza fortaleza : cargarFortalezas()) {
	                                if (fortaleza.getNombre().equals(nombreFortaleza)) {
	                                    fortalezas.add(fortaleza);
	                                    break;
	                                }
	                            }
	                        }

	                        // Cargar debilidades para este personaje
	                        List<Debilidad> debilidades = new ArrayList<>();
	                        NodeList debilidadesNodes = personajeElement.getElementsByTagName("debilidad");
	                        for (int j = 0; j < debilidadesNodes.getLength(); j++) {
	                            String nombreDebilidad = debilidadesNodes.item(j).getTextContent();
	                            for (Debilidad debilidad : cargarDebilidades()) {
	                                if (debilidad.getNombre().equals(nombreDebilidad)) {
	                                    debilidades.add(debilidad);
	                                    break;
	                                }
	                            }
	                        }

	                        // Cargar esbirros para este personaje
	                        List<Esbirro> esbirros = new ArrayList<>();
	                        NodeList esbirrosNodes = personajeElement.getElementsByTagName("esbirro");
	                        for (int j = 0; j < esbirrosNodes.getLength(); j++) {
	                            String nombreEsbirro = esbirrosNodes.item(j).getTextContent();
	                            for (Esbirro esbirro : cargarEsbirros()) {
	                                if (esbirro.getNombre().equals(nombreEsbirro)) {
	                                    esbirros.add(esbirro);
	                                    break;
	                                }
	                            }
	                        }

	                        // Definir arma activa (asumimos que la primera arma es la activa)
	                        List<Arma> armaActiva = new ArrayList<>();
	                        if (!armas.isEmpty()) {
	                            armaActiva.add(armas.get(0));
	                        }

	                        // Crear un nuevo personaje usando el constructor adecuado
	                        Personaje personaje = new Personaje(nombre, armaActiva, armaduraActiva, armas, armaduras, esbirros, fortalezas, debilidades);

	                        // Si hay información sobre oro y salud, actualizar esos valores
	                        if (personajeElement.getElementsByTagName("oro").getLength() > 0) {
	                            int oro = Integer.parseInt(personajeElement.getElementsByTagName("oro").item(0).getTextContent());
	                            for (int j = 0; j < oro; j++) {
	                                personaje.sumarOro(1); // Sumamos de uno en uno
	                            }
	                        }

	                        // Añadir el personaje a la lista
	                        personajes.add(personaje);
	                    }
	                }
	            }
	        }
	    } catch (Exception e) {
	        System.err.println("Error al cargar personajes del usuario: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return personajes;
	}
	
	public boolean actualizarOroPersonaje(String nick, String nombrePersonaje, int nuevoOro) {
	    try {
	        Document doc = getDoc("personajes_jugadores");
	        if (doc == null) return false;

	        NodeList personajes = doc.getElementsByTagName("personaje");
	        for (int i = 0; i < personajes.getLength(); i++) {
	            Element personaje = (Element) personajes.item(i);
	            String jugador = personaje.getElementsByTagName("jugador").item(0).getTextContent();
	            String nombre = personaje.getElementsByTagName("nombre").item(0).getTextContent();

	            if (jugador.equals(nick) && nombre.equals(nombrePersonaje)) {
	                personaje.getElementsByTagName("oro").item(0).setTextContent(String.valueOf(nuevoOro));
	                
	                // Guardar directamente el documento sin usar guardarDoc
	                TransformerFactory transformerFactory = TransformerFactory.newInstance();
	                Transformer transformer = transformerFactory.newTransformer();
	                transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // para que el archivo quede legible

	                DOMSource source = new DOMSource(doc);
	                StreamResult result = new StreamResult(new File(getFilePath("personajes_jugadores")));
	                transformer.transform(source, result);
	                
	                return true;
	            }
	        }

	        return false;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public void actualizarArmaActivaJugador(String nick, String nombrePersonaje, List<Arma> armas) {
	    actualizarListaElemento(nick, nombrePersonaje, armas, "arma_activa");
	}
	
	public boolean actualizarArmadurasPersonaje(String nick, String nombrePersonaje, List<Armadura> armaduras) {
	    return actualizarListaElemento(nick, nombrePersonaje, armaduras, "armadura");
	}
	
	public boolean actualizarFortalezasPersonaje(String nick, String nombrePersonaje, List<Fortaleza> fortalezas) {
	    return actualizarListaElemento(nick, nombrePersonaje, fortalezas, "fortaleza");
	}
	
	public boolean actualizarDebilidadesPersonaje(String nick, String nombrePersonaje, List<Debilidad> debilidades) {
	    return actualizarListaElemento(nick, nombrePersonaje, debilidades, "debilidad");
	}
	
	public boolean actualizarEsbirrosPersonaje(String nick, String nombrePersonaje, List<Esbirro> esbirros) {
	    return actualizarListaElemento(nick, nombrePersonaje, esbirros, "esbirro");
	}
	
	private boolean actualizarListaElemento(String nick, String nombrePersonaje, List<?> elementos, String tag) {
	    try {
	        Document doc = getDoc("personajes_jugadores");
	        if (doc == null) return false;

	        NodeList personajes = doc.getElementsByTagName("personaje");
	        for (int i = 0; i < personajes.getLength(); i++) {
	            Element personaje = (Element) personajes.item(i);
	            String jugador = personaje.getElementsByTagName("jugador").item(0).getTextContent();
	            String nombre = personaje.getElementsByTagName("nombre").item(0).getTextContent();

	            if (jugador.equals(nick) && nombre.equals(nombrePersonaje)) {
	                limpiarNodos(personaje, tag);

	                for (Object obj : elementos) {
	                    Element elem = doc.createElement(tag);
	                    elem.setTextContent(obj.toString());
	                    personaje.appendChild(elem);
	                }

	                // Guardar directamente el documento
	                TransformerFactory transformerFactory = TransformerFactory.newInstance();
	                Transformer transformer = transformerFactory.newTransformer();
	                transformer.setOutputProperty(OutputKeys.INDENT, "yes");

	                DOMSource source = new DOMSource(doc);
	                StreamResult result = new StreamResult(new File(getFilePath("personajes_jugadores")));
	                transformer.transform(source, result);

	                return true;
	            }
	        }

	        return false;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	private void limpiarNodos(Element parent, String tag) {
	    NodeList nodos = parent.getElementsByTagName(tag);
	    while (nodos.getLength() > 0) {
	        parent.removeChild(nodos.item(0));
	    }
	}
	
	public String eliminarPersonajeUsuario(String nick, Personaje personaje) {
	    try {
	        Document doc = getDoc("personajes_jugadores");
	        if (doc == null) {
	            return "No se pudo eliminar";
	        }

	        NodeList nodeList = doc.getElementsByTagName("personaje");
	        boolean eliminado = false;

	        for (int i = nodeList.getLength() - 1; i >= 0; i--) { // recorrer al revés por seguridad al eliminar nodos
	            Node node = nodeList.item(i);
	            if (node.getNodeType() == Node.ELEMENT_NODE) {
	                Element personajeElement = (Element) node;

	                String jugadorNick = personajeElement.getElementsByTagName("jugador").item(0).getTextContent();
	                String nombrePersonaje = personajeElement.getElementsByTagName("nombre").item(0).getTextContent();

	                if (jugadorNick.equals(nick) && nombrePersonaje.equals(personaje.getNombre())) {
	                    personajeElement.getParentNode().removeChild(personajeElement);
	                    eliminado = true;
	                }
	            }
	        }

	        if (eliminado) {
	            guardarDocumentoXML(doc, "personajes_jugadores"); // usa el nuevo método si no usas writeDoc
	            System.out.println("Personaje(s) con nombre '" + personaje.getNombre() + "' del usuario '" + nick + "' eliminado(s) correctamente.");
	            return "Se ha eliminado correctamente";
	        } else {
	            return "No se encontró el personaje a eliminar";
	        }
	    } catch (Exception e) {
	        System.err.println("Error al eliminar personaje del usuario: " + e.getMessage());
	        e.printStackTrace();
	        return "No se pudo eliminar";
	    }
	}
	
	@Override
	public String actualizarPersonajeUsuario(String nick, Personaje personajeActualizado) {
	    try {
	        Document doc = getDoc("personajes_jugadores");
	        if (doc == null) {
	            return "No se pudo actualizar el personaje";
	        }

	        NodeList nodeList = doc.getElementsByTagName("personaje");
	        boolean actualizado = false;

	        for (int i = 0; i < nodeList.getLength(); i++) {
	            Node node = nodeList.item(i);
	            if (node.getNodeType() == Node.ELEMENT_NODE) {
	                Element personajeElement = (Element) node;

	                String jugadorNick = personajeElement.getElementsByTagName("jugador").item(0).getTextContent();
	                String nombrePersonaje = personajeElement.getElementsByTagName("nombre").item(0).getTextContent();

	                if (jugadorNick.equals(nick) && nombrePersonaje.equals(personajeActualizado.getNombre())) {
	                    Element nuevoElemento = crearElementoPersonaje(doc, personajeActualizado, nick);
	                    personajeElement.getParentNode().replaceChild(nuevoElemento, personajeElement);
	                    actualizado = true;
	                    break;
	                }
	            }
	        }

	        if (actualizado) {
	            guardarDocumentoXML(doc, "personajes_jugadores"); // usa esto en lugar de writeDoc
	            System.out.println("Personaje '" + personajeActualizado.getNombre() + "' actualizado correctamente.");
	            return "Personaje actualizado correctamente";
	        } else {
	            return "No se encontró el personaje a actualizar";
	        }

	    } catch (Exception e) {
	        System.err.println("Error al actualizar personaje del usuario: " + e.getMessage());
	        e.printStackTrace();
	        return "No se pudo actualizar el personaje";
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

	            // Normalizar el documento
	            doc.normalize();

	            NodeList nodeList = doc.getElementsByTagName("personaje");
	            for (int i = 0; i < nodeList.getLength(); i++) {
	                Node node = nodeList.item(i);
	                if (node.getNodeType() == Node.ELEMENT_NODE) {
	                    Element personajeElement = (Element) node;

	                    // Obtener atributos básicos del personaje
	                    String nombre = getTextValue(personajeElement, "nombre");
	                    String tipo = getTextValue(personajeElement, "tipo");

	                    // Cargar armas para este personaje
	                    List<Arma> armas = new ArrayList<>();
	                    NodeList armasNodes = personajeElement.getElementsByTagName("arma");
	                    for (int j = 0; j < armasNodes.getLength(); j++) {
	                        String nombreArma = armasNodes.item(j).getTextContent();
	                        for (Arma arma : cargarArmas()) {
	                            if (arma.getNombre().equals(nombreArma)) {
	                                armas.add(arma);
	                                break;
	                            }
	                        }
	                    }

	                    // Cargar armaduras para este personaje
	                    List<Armadura> armaduras = new ArrayList<>();
	                    Armadura armaduraActiva = null;
	                    NodeList armadurasNodes = personajeElement.getElementsByTagName("armadura");
	                    for (int j = 0; j < armadurasNodes.getLength(); j++) {
	                        String nombreArmadura = armadurasNodes.item(j).getTextContent();
	                        for (Armadura armadura : cargarArmaduras()) {
	                            if (armadura.getNombre().equals(nombreArmadura)) {
	                                armaduras.add(armadura);
	                                if (armaduraActiva == null) {
	                                    armaduraActiva = armadura;
	                                }
	                                break;
	                            }
	                        }
	                    }

	                    // Cargar fortalezas
	                    List<Fortaleza> fortalezas = new ArrayList<>();
	                    NodeList fortalezasNodes = personajeElement.getElementsByTagName("fortaleza");
	                    for (int j = 0; j < fortalezasNodes.getLength(); j++) {
	                        String nombreFortaleza = fortalezasNodes.item(j).getTextContent();
	                        for (Fortaleza fortaleza : cargarFortalezas()) {
	                            if (fortaleza.getNombre().equals(nombreFortaleza)) {
	                                fortalezas.add(fortaleza);
	                                break;
	                            }
	                        }
	                    }

	                    // Cargar debilidades
	                    List<Debilidad> debilidades = new ArrayList<>();
	                    NodeList debilidadesNodes = personajeElement.getElementsByTagName("debilidad");
	                    for (int j = 0; j < debilidadesNodes.getLength(); j++) {
	                        String nombreDebilidad = debilidadesNodes.item(j).getTextContent();
	                        for (Debilidad debilidad : cargarDebilidades()) {
	                            if (debilidad.getNombre().equals(nombreDebilidad)) {
	                                debilidades.add(debilidad);
	                                break;
	                            }
	                        }
	                    }

	                    // Cargar esbirros
	                    List<Esbirro> esbirros = new ArrayList<>();
	                    NodeList esbirrosNodes = personajeElement.getElementsByTagName("esbirro");
	                    for (int j = 0; j < esbirrosNodes.getLength(); j++) {
	                        String nombreEsbirro = esbirrosNodes.item(j).getTextContent();
	                        for (Esbirro esbirro : cargarEsbirros()) {
	                            if (esbirro.getNombre().equals(nombreEsbirro)) {
	                                esbirros.add(esbirro);
	                                break;
	                            }
	                        }
	                    }

	                    // Definir arma activa (asumimos que la primera arma es la activa)
	                    List<Arma> armaActiva = new ArrayList<>();
	                    if (!armas.isEmpty()) {
	                        armaActiva.add(armas.get(0));
	                    }

	                    // Crear un nuevo personaje
	                    Personaje personaje = new Personaje(nombre, armaActiva, armaduraActiva, armas, armaduras, esbirros, fortalezas, debilidades);

	                    // Cargar oro si existe
	                    if (personajeElement.getElementsByTagName("oro").getLength() > 0) {
	                        int oro = Integer.parseInt(personajeElement.getElementsByTagName("oro").item(0).getTextContent());
	                        personaje.setOro(oro);
	                    }

	                    personajes.add(personaje);
	                }
	            }
	        } else {
	            System.out.println("Archivo de personajes no encontrado en: " + getFilePath("personajes"));
	        }
	    } catch (Exception e) {
	        System.err.println("Error al cargar personajes: " + e.getMessage());
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
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
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
	
	public String actualizarEstadoDesafio(UUID desafioId, E_EstadoDesafio nuevoEstado) {
	    try {
	        File file = new File(getFilePath("desafios"));
	        if (!file.exists()) {
	            return "⚠️ Archivo de desafíos no encontrado.";
	        }

	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.parse(file);
	        doc.getDocumentElement().normalize();

	        NodeList nodeList = doc.getElementsByTagName("desafio");

	        boolean encontrado = false;
	        for (int i = 0; i < nodeList.getLength(); i++) {
	            Element desafioElement = (Element) nodeList.item(i);
	            String id = desafioElement.getElementsByTagName("id").item(0).getTextContent();
	            if (id.equals(desafioId.toString())) {
	                // Actualizar estado
	                Element estadoElement = (Element) desafioElement.getElementsByTagName("estado").item(0);
	                estadoElement.setTextContent(nuevoEstado.toString());
	                encontrado = true;
	                break;
	            }
	        }

	        if (!encontrado) {
	            return "❌ Desafío con ID " + desafioId + " no encontrado.";
	        }

	        // Guardar cambios en el archivo
	        Transformer transformer = TransformerFactory.newInstance().newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        DOMSource source = new DOMSource(doc);
	        StreamResult result = new StreamResult(file);
	        transformer.transform(source, result);

	        return "✅ Estado del desafío actualizado correctamente.";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "❌ Error al actualizar estado del desafío: " + e.getMessage();
	    }
	}

	private Element createElement(Document doc, String tag, String value) {
	    Element element = doc.createElement(tag);
	    element.setTextContent(value);
	    return element;
	}

	@Override
	public String guardarDesafio(Desafio desafio) {
		try {
			File file = new File(getFilePath("desafios"));
			Document doc;
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

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

			// Eliminar desafío existente con el mismo ID, si lo hay
			NodeList desafioNodes = doc.getElementsByTagName("desafio");
			for (int i = 0; i < desafioNodes.getLength(); i++) {
				Element existingDesafio = (Element) desafioNodes.item(i);
				String existingId = existingDesafio.getElementsByTagName("id").item(0).getTextContent();
				if (existingId.equals(desafio.getDesafioId().toString())) {
					root.removeChild(existingDesafio);
					break;
				}
			}

			// Crear <desafio>
			Element desafioElement = doc.createElement("desafio");

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

			// Guardar siempre el personaje del desafiante
			Element personajeDesafianteWrapper = doc.createElement("personajeDesafiante");
			personajeDesafianteWrapper.appendChild(convertirPersonajeEnElemento(doc, desafio.getDesafiante().getPersonaje()));
			desafioElement.appendChild(personajeDesafianteWrapper);

			// Guardar el personaje del desafiado solo si el desafío está aceptado
			if (desafio.getEstado() == E_EstadoDesafio.ACEPTADO) {
				Element personajeDesafiadoWrapper = doc.createElement("personajeDesafiado");
				personajeDesafiadoWrapper.appendChild(convertirPersonajeEnElemento(doc, desafio.getDesafiado().getPersonaje()));
				desafioElement.appendChild(personajeDesafiadoWrapper);
			}

			// Agregar el desafío al XML
			root.appendChild(desafioElement);

			// Guardar el documento
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
	
	public Element convertirPersonajeEnElemento(Document doc, Personaje personaje) {
	    Element personajeElement = doc.createElement("personaje");

	    // Nombre del personaje
	    Element nombre = doc.createElement("nombre");
	    nombre.setTextContent(personaje.getNombre());
	    personajeElement.appendChild(nombre);
	    
	 // Nombre del salud
	    Element tipo = doc.createElement("salud");
	    tipo.setTextContent(String.valueOf(cargarSaludPorNombre(personaje.getNombre())));
	    personajeElement.appendChild(tipo);

	    // Oro
	    Element oro = doc.createElement("oro");
	    oro.setTextContent(String.valueOf(personaje.getOro()));
	    personajeElement.appendChild(oro);

	    // Armas
	    for (Arma arma : personaje.getArmas()) {
	        Element armaElement = doc.createElement("arma");
	        armaElement.setTextContent(arma.getNombre());
	        personajeElement.appendChild(armaElement);
	    }

	    // Armaduras
	    for (Armadura armadura : personaje.getArmaduras()) {
	        Element armaduraElement = doc.createElement("armadura");
	        armaduraElement.setTextContent(armadura.getNombre());
	        personajeElement.appendChild(armaduraElement);
	    }

	    // Fortaleza
	    for (Fortaleza fortaleza : personaje.getFortalezas()) {
	        Element fortalezaElement = doc.createElement("fortaleza");
	        fortalezaElement.setTextContent(fortaleza.getNombre());
	        personajeElement.appendChild(fortalezaElement);
	    }

	    // Debilidad
	    for (Debilidad debilidad : personaje.getDebilidades()) {
	        Element debilidadElement = doc.createElement("debilidad");
	        debilidadElement.setTextContent(debilidad.getNombre());
	        personajeElement.appendChild(debilidadElement);
	    }

	    // Esbirros
	    for (Esbirro esbirro : personaje.getEsbirros()) {
	        Element esbirroElement = doc.createElement("esbirro");
	        esbirroElement.setTextContent(esbirro.getNombre());
	        personajeElement.appendChild(esbirroElement);
	    }

	    return personajeElement;
	}

	@Override
	public Desafio cargarDesafio(UUID desafioId) {
		try {
			String rutaArchivo = getFilePath("desafios");
			File file = new File(rutaArchivo);

			if (!file.exists()) {
//				System.out.println("El archivo de desafíos no existe");
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
						Jugador desafiante = crearJugadorDesdeUsuario1(UUID.fromString(desafianteId), usuarios);
						Jugador desafiado = crearJugadorDesdeUsuario1(UUID.fromString(desafiadoId), usuarios);

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
	
	public String obtenerTipoDePersonajeDesafiantePorDesafioId(UUID desafioId) {
	    try {
	        String rutaArchivo = getFilePath("desafios");
	        File file = new File(rutaArchivo);

	        if (!file.exists()) {
	            System.out.println("Archivo no encontrado");
	            return null;
	        }

	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.parse(file);

	        NodeList listaDesafios = doc.getElementsByTagName("desafio");

	        for (int i = 0; i < listaDesafios.getLength(); i++) {
	            Element desafioElement = (Element) listaDesafios.item(i);
	            String idText = desafioElement.getElementsByTagName("id").item(0).getTextContent();

	            if (desafioId.equals(UUID.fromString(idText))) {
	                NodeList personajeDesafianteList = desafioElement.getElementsByTagName("personajeDesafiante");
	                if (personajeDesafianteList.getLength() > 0) {
	                    Element personajeElement = (Element) ((Element) personajeDesafianteList.item(0))
	                            .getElementsByTagName("personaje").item(0);

	                    // Verifica si existe la etiqueta <tipo>
	                    NodeList tipoList = personajeElement.getElementsByTagName("esbirro");
	                    if (tipoList.getLength() > 0) {
	                        return tipoList.item(0).getTextContent();
	                    } else {
	                        System.out.println("El nodo <tipo> no existe en este personaje.");
	                        return null;
	                    }
	                }
	            }
	        }

	        System.out.println("Desafío no encontrado con ID: " + desafioId);
	        return null;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	public Jugador crearJugadorDesdeUsuario1(UUID id, List<Usuario> usuarios) {
	    for (Usuario usuario : usuarios) {
	        if (usuario.getUserId().equals(id)) {
	            if (usuario instanceof Jugador) {
	                return (Jugador) usuario;
	            } else {
	                System.out.println("⚠️ Usuario con ID " + id + " encontrado pero no es un Jugador.");
	                return null;
	            }
	        }
	    }
	    System.out.println("❌ No se encontró un Usuario con ID: " + id);
	    return null;
	}

	@Override
	public List<Desafio> cargarDesafios(List<Usuario> usuarios) {
	    List<Desafio> desafios = new ArrayList<>();
	    try {
	        String rutaArchivo = getFilePath("desafios");
	        File file = new File(rutaArchivo);

	        if (!file.exists()) {
	            return desafios;  // Si el archivo no existe, retorna la lista vacía
	        }

	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.parse(file);

	        NodeList nodeList = doc.getElementsByTagName("desafio");

	        for (int i = 0; i < nodeList.getLength(); i++) {
	            Node node = nodeList.item(i);
	            if (node.getNodeType() == Node.ELEMENT_NODE) {
	                Element element = (Element) node;

	                // Extraer los datos del desafío
	                String idText = element.getElementsByTagName("id").item(0).getTextContent();
	                String desafianteId = element.getElementsByTagName("desafiante").item(0).getTextContent();
	                String desafiadoId = element.getElementsByTagName("desafiado").item(0).getTextContent();
	                int oroApostado = Integer.parseInt(element.getElementsByTagName("oroApostado").item(0).getTextContent());
	                String estadoText = element.getElementsByTagName("estado").item(0).getTextContent();
	                String fechaText = element.getElementsByTagName("fecha").item(0).getTextContent();

	                // Buscar los usuarios usando sus UUIDs
	                Jugador desafiante = crearJugadorDesdeUsuario(UUID.fromString(desafianteId), usuarios);
	                Jugador desafiado = crearJugadorDesdeUsuario(UUID.fromString(desafiadoId), usuarios);

	                if (desafiante == null || desafiado == null) {
	                    System.out.println("⚠️ Desafío omitido por usuarios no válidos.");
	                    continue;
	                }

	                // Obtener el estado del desafío, pero este ya no será un filtro
	                E_EstadoDesafio estado = obtenerEstadoDesafio(estadoText);
	                if (estado == null) {
	                    continue;  // Si el estado no es válido, saltar este desafío
	                }

	                // Crear el objeto desafío
	                Desafio desafio = new Desafio();
	                desafio.setDesafioId(UUID.fromString(idText));
	                desafio.setDesafiante(desafiante);
	                desafio.setDesafiado(desafiado);
	                desafio.setOroApostado(oroApostado);
	                desafio.setEstado(estado);
	                desafio.setFechaDesafio(LocalDateTime.parse(fechaText));

	                desafios.add(desafio);  // Añadir el desafío a la lista
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return desafios;  // Retornar la lista con todos los desafíos cargados
	}
	
	private E_EstadoDesafio obtenerEstadoDesafio(String estadoText) {
	    // Recorremos todos los valores del enum E_EstadoDesafio
	    for (E_EstadoDesafio estado : E_EstadoDesafio.values()) {
	        // Comparamos el valor de la cadena con el nombre del enum (estado.toString())
	        if (estado.toString().equals(estadoText)) {
	            return estado; // Si encontramos una coincidencia, devolvemos el enum correspondiente
	        }
	    }
	    // Si no encontramos una coincidencia, devolvemos null o podrías lanzar una excepción, depende de lo que desees hacer
	    System.out.println("Estado no válido: " + estadoText);
	    return null; // Retorna null si el estado no es válido
	}
	
	public Desafio cargarDesafioUsuario(String nick, List<Usuario> usuarios) {
//		    System.out.println("Intentando cargar desafíos para el usuario: " + nick);
		    List<Desafio> todosDesafios = cargarDesafios(usuarios);
		    
		    for (Desafio desafio : todosDesafios) {
		        if (desafio.getDesafiante().getNick().equals(nick) || desafio.getDesafiado().getNick().equals(nick)) {
		            return desafio; // Devuelve el primer desafío que le corresponde al usuario
		        }
		    }
		    return null; // Si no se encuentra un desafío para el usuario
	}
//
//	// Helper method to find a user by ID
//	private Usuario findUsuarioById(List<Usuario> usuarios, UUID id) {
//		for (Usuario usuario : usuarios) {
//			if (usuario.getUserId().equals(id)) {
//				return usuario;
//			}
//		}
//		return null;
//	}
	
	// REVISAR
	private Jugador crearJugadorDesdeUsuario(UUID id, List<Usuario> usuarios) {
	    for (Usuario usuario : usuarios) {
	    	if (usuario.getUserId().equals(id)) {

	            return new Jugador(
	                usuario.getUserId(),
	                usuario.getNick(),
	                usuario.getNombre(),
	                usuario.getPassword(),
	                usuario.getRol(),
	                usuario.getEstado(),
	                usuario.getOro(),
	                usuario.getPuntos(),
	                null,
	                null
	            );
	        }
	    }
	    return null; // Si no encuentra el usuario
	}

	@Override
	public List<Arma> cargarArmas() {
		List<Arma> armas = new ArrayList<>();
		try {
			File file = new File(getFilePath("armas"));
			if (file.exists()) {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(file);

				// Normalize the document
				doc.normalize();

				NodeList nodeList = doc.getElementsByTagName("arma");
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element armaElement = (Element) node;

						String nombre = armaElement.getElementsByTagName("nombre").item(0).getTextContent();
						int ataque = Integer.parseInt(armaElement.getElementsByTagName("ataque").item(0).getTextContent());
						int manos = Integer.parseInt(armaElement.getElementsByTagName("manos").item(0).getTextContent());

						// Crear el objeto Arma
						Arma arma = new Arma(nombre, ataque, manos);
						armas.add(arma);
					}
				}
			} else {
				System.out.println("Archivo de armas no encontrado en: " + getFilePath("armas"));
			}
		} catch (Exception e) {
			System.err.println("Error al cargar armas: " + e.getMessage());
			e.printStackTrace();
		}
		return armas;
	}

	@Override
	public List<Armadura> cargarArmaduras() {
		List<Armadura> armaduras = new ArrayList<>();
		try {
			File file = new File(getFilePath("armaduras"));
			if (file.exists()) {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(file);

				// Normalize the document
				doc.normalize();

				NodeList nodeList = doc.getElementsByTagName("armadura");
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element armaduraElement = (Element) node;

						String nombre = armaduraElement.getElementsByTagName("nombre").item(0).getTextContent();
						int defensa = Integer.parseInt(armaduraElement.getElementsByTagName("defensa").item(0).getTextContent());

						// Crear el objeto Armadura
						Armadura armadura = new Armadura(nombre, defensa);
						armaduras.add(armadura);
					}
				}
			} else {
				System.out.println("Archivo de armaduras no encontrado en: " + getFilePath("armaduras"));
			}
		} catch (Exception e) {
			System.err.println("Error al cargar armaduras: " + e.getMessage());
			e.printStackTrace();
		}
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
	/**
	 * Loads strengths from the XML file
	 */
	@Override
	public List<Fortaleza> cargarFortalezas() {
		List<Fortaleza> fortalezas = new ArrayList<>();
		try {
			File file = new File(getFilePath("fortalezas"));
			if (file.exists()) {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(file);

				// Normalize the document
				doc.normalize();

				NodeList nodeList = doc.getElementsByTagName("fortaleza");
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element fortalezaElement = (Element) node;

						String nombre = fortalezaElement.getElementsByTagName("nombre").item(0).getTextContent();
						int nivel = Integer.parseInt(fortalezaElement.getElementsByTagName("nivel").item(0).getTextContent());

						// Crear el objeto Fortaleza
						Fortaleza fortaleza = new Fortaleza(nombre, nivel);
						fortalezas.add(fortaleza);
					}
				}
			} else {
				System.out.println("Archivo de fortalezas no encontrado en: " + getFilePath("fortalezas"));
			}
		} catch (Exception e) {
			System.err.println("Error al cargar fortalezas: " + e.getMessage());
			e.printStackTrace();
		}
		return fortalezas;
	}

	/**
	 * Loads weaknesses from the XML file
	 */
	@Override
	public List<Debilidad> cargarDebilidades() {
		List<Debilidad> debilidades = new ArrayList<>();
		try {
			File file = new File(getFilePath("debilidades"));
			if (file.exists()) {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(file);

				// Normalize the document
				doc.normalize();

				NodeList nodeList = doc.getElementsByTagName("debilidad");
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					if (node.getNodeType() == Node.ELEMENT_NODE) {
						Element debilidadElement = (Element) node;

						String nombre = debilidadElement.getElementsByTagName("nombre").item(0).getTextContent();
						int nivel = Integer.parseInt(debilidadElement.getElementsByTagName("nivel").item(0).getTextContent());

						// Crear el objeto Debilidad
						Debilidad debilidad = new Debilidad(nombre, nivel);
						debilidades.add(debilidad);
					}
				}
			} else {
				System.out.println("Archivo de debilidades no encontrado en: " + getFilePath("debilidades"));
			}
		} catch (Exception e) {
			System.err.println("Error al cargar debilidades: " + e.getMessage());
			e.printStackTrace();
		}
		return debilidades;
	}

	/**
	 * Loads minions from the XML file
	 */
	@Override
	public List<Esbirro> cargarEsbirros() {
		List<Esbirro> esbirros = new ArrayList<>();
		try {
			Document doc = getDoc("esbirros");

			// Normalize the document
			doc.normalize();

			NodeList nodeList = doc.getElementsByTagName("esbirro");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element esbirroElement = (Element) node;

					String nombre = esbirroElement.getElementsByTagName("nombre").item(0).getTextContent();
					String tipo = esbirroElement.getElementsByTagName("tipo").item(0).getTextContent();
					int nivel = Integer.parseInt(esbirroElement.getElementsByTagName("nivel").item(0).getTextContent());

					// Crear el objeto Esbirro
					Esbirro esbirro = new Esbirro(tipo, nombre, nivel);
					esbirros.add(esbirro);
				}
			}
		} catch (Exception e) {
			System.err.println("Error al cargar esbirros: " + e.getMessage());
			e.printStackTrace();
		}
		return esbirros;
	}
	
	// methods to get and write files
	private Document getDoc(String filename) {
	    try {
	        File file = new File(this.getFilePath(filename));
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();

	        // Si no existe o está vacío, crear documento nuevo
	        if (!file.exists() || file.length() == 0) {
	            Document doc = builder.newDocument();
	            Element root = doc.createElement("personajes"); // Usa tu etiqueta raíz real
	            doc.appendChild(root);
	            removeWhitespaceNodes(doc); // Eliminar nodos de texto vacíos
	            writeDoc(doc, filename);
	            return doc;
	        }

	        Document doc = builder.parse(file);
	        doc.normalize();
	        return doc;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	private void writeDoc(Document doc, String filename) {
	    try {
	        // Eliminar nodos de texto vacíos antes de guardar
	        removeWhitespaceNodes(doc);

	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();

	        // Configurar la indentación
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

	        DOMSource source = new DOMSource(doc);
	        StreamResult result = new StreamResult(new File(getFilePath(filename)));
	        transformer.transform(source, result);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	// methods of each personal type
	private String getTextContent(Element parentElement, String tagName) {
		NodeList nodeList = parentElement.getElementsByTagName(tagName);
		if (nodeList.getLength() > 0) {
			return nodeList.item(0).getTextContent();
		}
		return "";
	}

	private void setTextContent(Document doc, Element parentElement, String tagName, String value) {
		NodeList nodeList = parentElement.getElementsByTagName(tagName);
		if (nodeList.getLength() > 0) {
			nodeList.item(0).setTextContent(value);
		} else {
			Element newElement = doc.createElement(tagName);
			newElement.setTextContent(value);
			parentElement.appendChild(newElement);
		}
	}

	private void setTextContent(Document doc, Element parentElement, String tagName, int value) {
		setTextContent(doc, parentElement, tagName, String.valueOf(value));
	}

	// Getters
	
	public List<String> getTiposPersonajes() {
	    List<String> tipos = new ArrayList<>();
	    try {
	        String fileNameTipoPersonajes = "tipos_personajes";
	        Document doc = getDoc(fileNameTipoPersonajes);
	        
	        // Obtener el nodo raíz: <TipoPersonajes>
	        Element root = doc.getDocumentElement();

	        // Obtener solo los hijos directos (Cazador, Licantropo, Vampiro)
	        NodeList children = root.getChildNodes();

	        for (int i = 0; i < children.getLength(); i++) {
	            Node node = children.item(i);
	            if (node.getNodeType() == Node.ELEMENT_NODE) {
	                tipos.add(node.getNodeName().toLowerCase()); // O sin .toLowerCase() si quieres conservar mayúsculas
	            }
	        }
	    } catch (Exception e) {
	        System.out.println("Error al obtener los tipos de personajes: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return tipos;
	}
	
	public int getSaludPorTipoNombre(String nombreTipo) {
	    try {
//	        System.out.println("Buscando salud total para: " + nombreTipo);
	        String fileNameTipoPersonajes = "personajes";
	        Document doc = getDoc(fileNameTipoPersonajes);

	        Element root = doc.getDocumentElement();
	        NodeList personajes = root.getElementsByTagName("personaje");
//	        System.out.println("Cantidad de personajes encontrados: " + personajes.getLength());

	        for (int i = 0; i < personajes.getLength(); i++) {
	            Node personajeNode = personajes.item(i);
	            if (personajeNode.getNodeType() == Node.ELEMENT_NODE) {
	                Element personajeElement = (Element) personajeNode;

	                // Salud del personaje actual
	                String saludPersonajeText = personajeElement.getElementsByTagName("salud").item(0).getTextContent();
	                int saludPersonaje = Integer.parseInt(saludPersonajeText);

	                // Nombre del personaje
	                String nombre = personajeElement.getElementsByTagName("nombre").item(0).getTextContent().toLowerCase();
//	                System.out.println("Comparando con personaje: " + nombre);

	                if (nombre.equals(nombreTipo.toLowerCase())) {
//	                    System.out.println("¡Match con personaje! Salud: " + saludPersonaje);
	                    return saludPersonaje;
	                }

	                // Buscar esbirros y comparar nombre
	                NodeList esbirros = personajeElement.getElementsByTagName("esbirro");
	                for (int j = 0; j < esbirros.getLength(); j++) {
	                    Element esbirro = (Element) esbirros.item(j);
	                    String nombreEsbirro = esbirro.getElementsByTagName("nombre").item(0).getTextContent().toLowerCase();
//	                    System.out.println("Comparando con esbirro: " + nombreEsbirro);

	                    if (nombreEsbirro.equals(nombreTipo.toLowerCase())) {
	                        String saludEsbirroText = esbirro.getElementsByTagName("salud").item(0).getTextContent();
	                        int saludEsbirro = Integer.parseInt(saludEsbirroText);
	                        int totalSalud = saludPersonaje + saludEsbirro;
//	                        System.out.println("¡Match con esbirro! Salud personaje + esbirro: " + totalSalud);
	                        return totalSalud;
	                    }
	                }
	            }
	        }

//	        System.out.println("No se encontró personaje ni esbirro con ese nombre.");
	    } catch (Exception e) {
//	        System.out.println("Error al obtener la salud: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return 0;
	}

	public List<Talento> getTalentosCazador() {
		String fileNameTipoPersonajes = "tipos_personajes";
		Document doc = getDoc(fileNameTipoPersonajes);
		NodeList cazadorList = doc.getElementsByTagName("Cazador");
		List<Talento> talentos = new ArrayList<>();
		if (cazadorList.getLength() > 0) {
			Element cazadorElement = (Element) cazadorList.item(0);
			NodeList talentosList = cazadorElement.getElementsByTagName("Talentos");
			if (talentosList.getLength() > 0) {
				Element talentosElement = (Element) talentosList.item(0);
				NodeList talentoNodes = talentosElement.getElementsByTagName("Talento");
				for (int i = 0; i < talentoNodes.getLength(); i++) {
					Element talentoElement = (Element) talentoNodes.item(i);
					String nombre = getTextContent(talentoElement, "Nombre");
					int ataque = Integer.parseInt(getTextContent(talentoElement, "Ataque"));
					int defensa = Integer.parseInt(getTextContent(talentoElement, "Defensa"));
					int costoVoluntad = Integer.parseInt(getTextContent(talentoElement, "CostoVoluntad"));
					talentos.add(new Talento(nombre, ataque, defensa, costoVoluntad));
				}
			}
		}
		return talentos;
	}

	public int getVoluntadCazador() {
		String fileNameTipoPersonajes = "tipos_personajes";
		Document doc = getDoc(fileNameTipoPersonajes);
		NodeList cazadorList = doc.getElementsByTagName("Cazador");
		if (cazadorList.getLength() > 0) {
			Element cazadorElement = (Element) cazadorList.item(0);
			NodeList voluntadList = cazadorElement.getElementsByTagName("Voluntad");
			if (voluntadList.getLength() > 0) {
				return Integer.parseInt(voluntadList.item(0).getTextContent());
			}
		}
		return 0;
	}

	public List<Don> getDonesLicantropo() {
		String fileNameTipoPersonajes = "tipos_personajes";
		Document doc = getDoc(fileNameTipoPersonajes);
		NodeList licantropoList = doc.getElementsByTagName("Licantropo");
		List<Don> dones = new ArrayList<>();
		if (licantropoList.getLength() > 0) {
			Element licantropoElement = (Element) licantropoList.item(0);
			NodeList donesList = licantropoElement.getElementsByTagName("Dones");
			if (donesList.getLength() > 0) {
				Element donesElement = (Element) donesList.item(0);
				NodeList donNodes = donesElement.getElementsByTagName("Don");
				for (int i = 0; i < donNodes.getLength(); i++) {
					Element donElement = (Element) donNodes.item(i);
					String nombre = getTextContent(donElement, "Nombre");
					int ataque = Integer.parseInt(getTextContent(donElement, "Ataque"));
					int defensa = Integer.parseInt(getTextContent(donElement, "Defensa"));
					int rabiaMinima = Integer.parseInt(getTextContent(donElement, "RabiaMinima"));
					dones.add(new Don(nombre, ataque, defensa, rabiaMinima));
				}
			}
		}
		return dones;
	}

	public int getRabiaLicantropo() {
		String fileNameTipoPersonajes = "tipos_personajes";
		Document doc = getDoc(fileNameTipoPersonajes);
		NodeList licantropoList = doc.getElementsByTagName("Licantropo");
		if (licantropoList.getLength() > 0) {
			Element licantropoElement = (Element) licantropoList.item(0);
			NodeList rabiaList = licantropoElement.getElementsByTagName("Rabia");
			if (rabiaList.getLength() > 0) {
				return Integer.parseInt(rabiaList.item(0).getTextContent());
			}
		}
		return 0;
	}

	public int getPesoLicantropo() {
		String fileNameTipoPersonajes = "tipos_personajes";
		Document doc = getDoc(fileNameTipoPersonajes);
		NodeList licantropoList = doc.getElementsByTagName("Licantropo");
		if (licantropoList.getLength() > 0) {
			Element licantropoElement = (Element) licantropoList.item(0);
			NodeList pesoList = licantropoElement.getElementsByTagName("Peso");
			if (pesoList.getLength() > 0) {
				return Integer.parseInt(pesoList.item(0).getTextContent());
			}
		}
		return 0;
	}

	public List<Disciplina> getDisciplinasVampiro() {
		String fileNameTipoPersonajes = "tipos_personajes";
		Document doc = getDoc(fileNameTipoPersonajes);
		NodeList vampiroList = doc.getElementsByTagName("Vampiro");
		List<Disciplina> disciplinas = new ArrayList<>();
		if (vampiroList.getLength() > 0) {
			Element vampiroElement = (Element) vampiroList.item(0);
			NodeList disciplinasList = vampiroElement.getElementsByTagName("Disciplinas");
			if (disciplinasList.getLength() > 0) {
				Element disciplinasElement = (Element) disciplinasList.item(0);
				NodeList disciplinaNodes = disciplinasElement.getElementsByTagName("Disciplina");
				for (int i = 0; i < disciplinaNodes.getLength(); i++) {
					Element disciplinaElement = (Element) disciplinaNodes.item(i);
					String nombre = getTextContent(disciplinaElement, "Nombre");
					int ataque = Integer.parseInt(getTextContent(disciplinaElement, "Ataque"));
					int defensa = Integer.parseInt(getTextContent(disciplinaElement, "Defensa"));
					int costoSangre = Integer.parseInt(getTextContent(disciplinaElement, "CostoSangre"));
					disciplinas.add(new Disciplina(nombre, ataque, defensa, costoSangre));
				}
			}
		}
		return disciplinas;
	}

	public int getPuntosdeSangreVampiro() {
		String fileNameTipoPersonajes = "tipos_personajes";
		Document doc = getDoc(fileNameTipoPersonajes);
		NodeList vampiroList = doc.getElementsByTagName("Vampiro");
		if (vampiroList.getLength() > 0) {
			Element vampiroElement = (Element) vampiroList.item(0);
			NodeList puntosSangreList = vampiroElement.getElementsByTagName("PuntosSangre");
			if (puntosSangreList.getLength() > 0) {
				return Integer.parseInt(puntosSangreList.item(0).getTextContent());
			}
		}
		return 0;
	}

	public int getEdadVampiro() {
		String fileNameTipoPersonajes = "tipos_personajes";
		Document doc = getDoc(fileNameTipoPersonajes);
		NodeList vampiroList = doc.getElementsByTagName("Vampiro");
		if (vampiroList.getLength() > 0) {
			Element vampiroElement = (Element) vampiroList.item(0);
			NodeList edadList = vampiroElement.getElementsByTagName("Edad");
			if (edadList.getLength() > 0) {
				return Integer.parseInt(edadList.item(0).getTextContent());
			}
		}
		return 0;
	}

	// REVISAR - Setters

	private void guardarDocumentoXML(Document doc, String fileName) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(getFilePath(fileName)));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVoluntadCazador(int voluntad) {
        Document doc = getDoc("tipos_personajes");
        NodeList list = doc.getElementsByTagName("Cazador");
        if (list.getLength() > 0) {
            Element e = (Element) list.item(0);
            setTextContent(doc, e, "Voluntad", voluntad);
            guardarDocumentoXML(doc, "tipos_personajes");
        }
    }

    public void setRabiaLicantropo(int rabia) {
        Document doc = getDoc("tipos_personajes");
        NodeList list = doc.getElementsByTagName("Licantropo");
        if (list.getLength() > 0) {
            Element e = (Element) list.item(0);
            setTextContent(doc, e, "Rabia", rabia);
            guardarDocumentoXML(doc, "tipos_personajes");
        }
    }

    public void setPesoLicantropo(int peso) {
        Document doc = getDoc("tipos_personajes");
        NodeList list = doc.getElementsByTagName("Licantropo");
        if (list.getLength() > 0) {
            Element e = (Element) list.item(0);
            setTextContent(doc, e, "Peso", peso);
            guardarDocumentoXML(doc, "tipos_personajes");
        }
    }

    public void setPuntosdeSangreVampiro(int puntosSangre) {
        Document doc = getDoc("tipos_personajes");
        NodeList list = doc.getElementsByTagName("Vampiro");
        if (list.getLength() > 0) {
            Element e = (Element) list.item(0);
            setTextContent(doc, e, "PuntosSangre", puntosSangre);
            guardarDocumentoXML(doc, "tipos_personajes");
        }
    }

    public void setEdadVampiro(int edad) {
        Document doc = getDoc("tipos_personajes");
        NodeList list = doc.getElementsByTagName("Vampiro");
        if (list.getLength() > 0) {
            Element e = (Element) list.item(0);
            setTextContent(doc, e, "Edad", edad);
            guardarDocumentoXML(doc, "tipos_personajes");
        }
    }

    public void setTalentosCazador(List<Talento> talentos) {
        Document doc = getDoc("tipos_personajes");
        NodeList list = doc.getElementsByTagName("Cazador");
        if (list.getLength() > 0) {
            Element cazador = (Element) list.item(0);
            Element talentosElement;

            NodeList container = cazador.getElementsByTagName("Talentos");
            if (container.getLength() > 0) {
                talentosElement = (Element) container.item(0);
                NodeList nodes = talentosElement.getElementsByTagName("Talento");
                for (int i = nodes.getLength() - 1; i >= 0; i--) {
                    talentosElement.removeChild(nodes.item(i));
                }
            } else {
                talentosElement = doc.createElement("Talentos");
                cazador.appendChild(talentosElement);
            }

            for (Talento t : talentos) {
                Element e = doc.createElement("Talento");
                setTextContent(doc, e, "Nombre", t.getNombre());
                setTextContent(doc, e, "Ataque", t.getAtaque());
                setTextContent(doc, e, "Defensa", t.getDefensa());
                setTextContent(doc, e, "CostoVoluntad", t.getCostoVoluntad());
                talentosElement.appendChild(e);
            }

            guardarDocumentoXML(doc, "tipos_personajes");
        }
    }

    public void setDonesLicantropo(List<Don> dones) {
        Document doc = getDoc("tipos_personajes");
        NodeList list = doc.getElementsByTagName("Licantropo");
        if (list.getLength() > 0) {
            Element licantropo = (Element) list.item(0);
            Element donesElement;

            NodeList container = licantropo.getElementsByTagName("Dones");
            if (container.getLength() > 0) {
                donesElement = (Element) container.item(0);
                NodeList nodes = donesElement.getElementsByTagName("Don");
                for (int i = nodes.getLength() - 1; i >= 0; i--) {
                    donesElement.removeChild(nodes.item(i));
                }
            } else {
                donesElement = doc.createElement("Dones");
                licantropo.appendChild(donesElement);
            }

            for (Don d : dones) {
                Element e = doc.createElement("Don");
                setTextContent(doc, e, "Nombre", d.getNombre());
                setTextContent(doc, e, "Ataque", d.getAtaque());
                setTextContent(doc, e, "Defensa", d.getDefensa());
                setTextContent(doc, e, "RabiaMinima", d.getRabiaMinima());
                donesElement.appendChild(e);
            }

            guardarDocumentoXML(doc, "tipos_personajes");
        }
    }

    public void setDisciplinasVampiro(List<Disciplina> disciplinas) {
        Document doc = getDoc("tipos_personajes");
        NodeList list = doc.getElementsByTagName("Vampiro");
        if (list.getLength() > 0) {
            Element vampiro = (Element) list.item(0);
            Element disciplinasElement;

            NodeList container = vampiro.getElementsByTagName("Disciplinas");
            if (container.getLength() > 0) {
                disciplinasElement = (Element) container.item(0);
                NodeList nodes = disciplinasElement.getElementsByTagName("Disciplina");
                for (int i = nodes.getLength() - 1; i >= 0; i--) {
                    disciplinasElement.removeChild(nodes.item(i));
                }
            } else {
                disciplinasElement = doc.createElement("Disciplinas");
                vampiro.appendChild(disciplinasElement);
            }

            for (Disciplina d : disciplinas) {
                Element e = doc.createElement("Disciplina");
                setTextContent(doc, e, "Nombre", d.getNombre());
                setTextContent(doc, e, "Ataque", d.getAtaque());
                setTextContent(doc, e, "Defensa", d.getDefensa());
                setTextContent(doc, e, "CostoSangre", d.obtenerCostoSangre());
                disciplinasElement.appendChild(e);
            }

            guardarDocumentoXML(doc, "tipos_personajes");
        }
    }

}