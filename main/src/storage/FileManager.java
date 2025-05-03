package storage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import model.desafio.Combate;
import model.desafio.Desafio;
import model.desafio.E_EstadoDesafio;
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
import notifications.I_Notification;

public class FileManager {
    private I_Storage storage;
    private Usuario usuario;
    private List<Personaje> personajes;
    private List<Combate> combates;
    private List<Desafio> desafios;
    private List<Arma> armas;
    private List<Armadura> armaduras;
    private List<I_Notification> notificaciones;

    // Constructor that accepts a storage adapter and initializes attributes
    public FileManager(I_Storage storage) {
    	if (this.storage == null || this.storage != storage) {
    		this.storage = storage;
    	}
    }

    // Save and load methods for different types of data, delegating the work to the storage adapter

    /**
     * Guarda un usuario en el almacenamiento.
     *
     * @param usuario el usuario a guardar
     * @return un mensaje indicando el resultado de la operación
     */
    public String guardarUsuario(Usuario usuario) {
        return this.storage.guardarUsuario(usuario);
    }

    /**
     * Da de baja (elimina o desactiva) a un usuario identificado por su nick.
     *
     * @param nick el nombre de usuario
     * @return un mensaje indicando el resultado de la operación
     */
    public String darDeBajaUsuario(String nick) {
        return this.storage.darDeBajaUsuario(nick);
    }

    /**
     * Carga todos los usuarios desde el almacenamiento.
     *
     * @return una lista de objetos Usuario
     */
    public List<Usuario> cargarUsuarios() {
        return this.storage.cargarUsuarios();
    }

    /**
     * Actualiza la información de un jugador existente.
     *
     * @param jugador el jugador a actualizar
     * @return un mensaje indicando el resultado de la operación
     */
    public String actualizarJugador(Jugador jugador) {
        return this.storage.guardarUsuario(jugador);
    }

    /**
     * Guarda una lista de personajes en el almacenamiento.
     *
     * @param personajes la lista de personajes a guardar
     * @return un mensaje indicando el resultado de la operación
     */
    public String guardarPersonajes(List<Personaje> personajes) {
        return this.storage.guardarPersonajes(personajes);
    }

    /**
     * Carga los personajes asociados a un usuario específico.
     *
     * @param nick el nombre del usuario
     * @return una lista de personajes pertenecientes al usuario
     */
    public List<Personaje> cargarPersonajesUsuario(String nick) {
        return this.storage.cargarPersonajesUsuario(nick);
    }
    
    public Desafio cargarDesafioUsuario(String nick, List<Usuario> usuarios) {
        return this.storage.cargarDesafioUsuario(nick, usuarios);
    }
    
    public String eliminarPersonajesUsuario(String nick, Personaje personaje) {
    	return this.storage.eliminarPersonajeUsuario(nick, personaje);
    }
    
    public boolean guardarPersonajesUsuario(String nick, List<Personaje> personajes) {
    	return this.storage.guardarPersonajesUsuario(nick, personajes);
    }

    /**
     * Carga todos los personajes desde el almacenamiento.
     *
     * @return una lista de todos los personajes
     */
    public List<Personaje> cargarPersonajes() {
        return this.storage.cargarPersonajes();
    }
    
    public int cargarSaludPorNombre(String nombrePersonaje) {
        return this.storage.cargarSaludPorNombre(nombrePersonaje);
    }

    /**
     * Guarda un combate en el almacenamiento.
     *
     * @param combate el combate a guardar
     * @return un mensaje indicando el resultado de la operación
     */
    public String guardarCombate(Combate combate) {
        return this.storage.guardarCombate(combate);
    }

    /**
     * Carga todos los combates almacenados.
     *
     * @return una lista de objetos Combate
     */
    public List<model.desafio.Combate> cargarCombates() {
        return this.storage.cargarCombates();
    }

    /**
     * Guarda un desafío en el almacenamiento.
     *
     * @param desafio el desafío a guardar
     * @return un mensaje indicando el resultado de la operación
     */
    public String guardarDesafio(Desafio desafio) {
        return this.storage.guardarDesafio(desafio);
    }

    public String actualizarEstadoDesafio(UUID desafioId, E_EstadoDesafio nuevoEstado) {
        return this.storage.actualizarEstadoDesafio(desafioId, nuevoEstado);
    }

    
    /**
     * Carga todos los desafíos desde el almacenamiento.
     *
     * @return una lista de objetos Desafio
     */
    public List<Desafio> cargarDesafios(List<Usuario> usuarios) {
        return this.storage.cargarDesafios(usuarios);
    }

    /**
     * Carga todas las armas disponibles desde el almacenamiento.
     *
     * @return una lista de objetos Arma
     */
    public List<Arma> cargarArmas() {
        return this.storage.cargarArmas();
    }

    /**
     * Carga todas las armaduras disponibles desde el almacenamiento.
     *
     * @return una lista de objetos Armadura
     */
    public List<Armadura> cargarArmaduras() {
        return this.storage.cargarArmaduras();
    }

    /**
     * Obtiene las notificaciones asociadas a un usuario.
     *
     * @param usuario el usuario del cual se obtienen las notificaciones
     * @return una lista de mensajes de notificación
     */
    public List<String> getNotificacion(Usuario usuario) {
        return this.storage.getNotificacion(usuario);
    }

    /**
     * Establece (agrega) una notificación para un usuario.
     *
     * @param nick el nombre del usuario
     * @param mensaje el mensaje de notificación a agregar
     */
    public void setNotificacion(String nick, String mensaje) {
        this.storage.setNotificacion(nick, mensaje);
    }

    /**
     * Elimina todas las notificaciones de un usuario.
     *
     * @param usuario el usuario del cual se eliminarán las notificaciones
     */
    public void deleteNotificacion(Usuario usuario) {
        this.storage.deleteNotificacion(usuario);
    }
    
    /**
     * Carga un desafío específico usando su UUID.
     *
     * @param desafioId el identificador único del desafío
     * @return el objeto Desafio correspondiente
     */
    public Desafio cargarDesafio(UUID desafioId) {
        return this.storage.cargarDesafio(desafioId);
    }
	
	/**
	    * Obtiene la lista de talentos del personaje tipo Cazador desde el almacenamiento.
	    * 
	    * @return una lista de objetos Talento del Cazador.
	    */
	   public List<Talento> getTalentosCazador() {
	       return this.storage.getTalentosCazador();
	   }

	   /**
	    * Obtiene el valor de voluntad del personaje tipo Cazador desde el almacenamiento.
	    * 
	    * @return un valor entero que representa la voluntad del Cazador.
	    */
	   public int getVoluntadCazador() {
	       return this.storage.getVoluntadCazador();
	   }

	   /**
	    * Obtiene la lista de dones del personaje tipo Licántropo desde el almacenamiento.
	    * 
	    * @return una lista de objetos Don del Licántropo.
	    */
	   public List<Don> getDonesLicantropo() {
	       return this.storage.getDonesLicantropo();
	   }

	   /**
	    * Obtiene el valor de rabia del personaje tipo Licántropo desde el almacenamiento.
	    * 
	    * @return un valor entero que representa la rabia del Licántropo.
	    */
	   public int getRabiaLicantropo() {
	       return this.storage.getRabiaLicantropo();
	   }

	   /**
	    * Obtiene el peso del personaje tipo Licántropo desde el almacenamiento.
	    * 
	    * @return un valor entero que representa el peso del Licántropo.
	    */
	   public int getPesoLicantropo() {
	       return this.storage.getPesoLicantropo();
	   }

	   /**
	    * Obtiene la lista de disciplinas del personaje tipo Vampiro desde el almacenamiento.
	    * 
	    * @return una lista de objetos Disciplina del Vampiro.
	    */
	   public List<Disciplina> getDisciplinasVampiro() {
	       return this.storage.getDisciplinasVampiro();
	   }

	   /**
	    * Obtiene la cantidad de puntos de sangre del personaje tipo Vampiro desde el almacenamiento.
	    * 
	    * @return un valor entero que representa los puntos de sangre del Vampiro.
	    */
	   public int getPuntosdeSangreVampiro() {
	       return this.storage.getPuntosdeSangreVampiro();
	   }

	   /**
	    * Obtiene la edad del personaje tipo Vampiro desde el almacenamiento.
	    * 
	    * @return un valor entero que representa la edad del Vampiro.
	    */
	   public int getEdadVampiro() {
	       return this.storage.getEdadVampiro();
	   }

    //==================================================
    // OPERATOR METHODS
    //==================================================
    
	/**
     * Saves a specific character.
     * 
     * @param personaje The character to save
     */
    public void guardarPersonaje(Personaje personaje) {
        List<Personaje> personajes = cargarPersonajes();
        boolean found = false;
        
        // Update the character if it exists
        for (int i = 0; i < personajes.size(); i++) {
            if (personajes.get(i).getId().equals(personaje.getId())) {
                personajes.set(i, personaje);
                found = true;
                break;
            }
        }
        
        // Add the character if it doesn't exist
        if (!found) {
            personajes.add(personaje);
        }
        
        // Save the updated list
        guardarPersonajes(personajes);
    }
    
    /**
     * Loads all pending challenges.
     * 
     * @return List of pending challenges
     */
    public List<Desafio> cargarDesafiosPendientes(List<Usuario> usuarios) {
        List<Desafio> desafios = cargarDesafios(usuarios);
//        System.out.println("Desafios cargados: "+desafios);
        List<Desafio> desafiosPendientes = new ArrayList<>();
        
        for (Desafio desafio : desafios) {
            if (desafio.getEstado() != null) {
                // Agregar el desafío pendiente a la lista
                if (E_EstadoDesafio.PENDIENTE.equals(desafio.getEstado())) {
                    desafiosPendientes.add(desafio);
                }
            }
        }
        
        return desafiosPendientes;
    }
    
    /**
     * Loads all pending challenges.
     * 
     * @return List of pending challenges
     */
    public List<Desafio> cargarDesafiosAceptados(List<Usuario> usuarios) {
        List<Desafio> desafios = cargarDesafios(usuarios);
//        System.out.println("Desafios cargados: "+desafios);
        List<Desafio> desafiosPendientes = new ArrayList<>();
        
        for (Desafio desafio : desafios) {
            if (desafio.getEstado() != null) {
                // Agregar el desafío pendiente a la lista
                if (E_EstadoDesafio.ACEPTADO.equals(desafio.getEstado())) {
                    desafiosPendientes.add(desafio);
                }
            }
        }
        
        return desafiosPendientes;
    }
    
    /**
     * Loads all pending challenges.
     * 
     * @return List of pending challenges
     */
    public List<Desafio> cargarDesafiosRechazados(List<Usuario> usuarios) {
        List<Desafio> desafios = cargarDesafios(usuarios);
//        System.out.println("Desafios cargados: "+desafios);
        List<Desafio> desafiosPendientes = new ArrayList<>();
        
        for (Desafio desafio : desafios) {
            if (desafio.getEstado() != null) {
                // Agregar el desafío pendiente a la lista
                if (E_EstadoDesafio.RECHAZADO.equals(desafio.getEstado())) {
                    desafiosPendientes.add(desafio);
                }
            }
        }
        
        return desafiosPendientes;
    }
    
    /**
     * Loads all pending challenges.
     * 
     * @return List of pending challenges
     */
    public List<Desafio> cargarDesafiosValidados(List<Usuario> usuarios) {
        List<Desafio> desafios = cargarDesafios(usuarios);
//        System.out.println("Desafios cargados: "+desafios);
        List<Desafio> desafiosPendientes = new ArrayList<>();
        
        for (Desafio desafio : desafios) {
            if (desafio.getEstado() != null) {
                // Agregar el desafío pendiente a la lista
                if (E_EstadoDesafio.VALIDADO.equals(desafio.getEstado())) {
                    desafiosPendientes.add(desafio);
                }
            }
        }
        
        return desafiosPendientes;
    }
    
    
    /**
     * Loads all active players.
     * 
     * @return List of active players
     */
    public List<Jugador> cargarJugadoresActivos() {
        List<Usuario> usuarios = cargarUsuarios();
        List<Jugador> jugadoresActivos = new ArrayList<>();
        
        for (Usuario usuario : usuarios) {
            if ("activo".equals(usuario.getEstado()) && "jugador".equals(usuario.getRol())) {
                // Crear un nuevo Jugador a partir del Usuario
                List<Personaje> personajes = cargarPersonajesUsuario(usuario.getNick());
                Desafio desafio = this.storage.cargarDesafioUsuario(usuario.getNick(), usuarios);
                Jugador jugador = new Jugador(usuario.getUserId(), usuario.getNick(), usuario.getNombre(), usuario.getPassword(), usuario.getRol(), usuario.getEstado(), usuario.getOro(), usuario.getPuntos()
, personajes, desafio);
                jugadoresActivos.add(jugador);
            }
        }
        
        return jugadoresActivos;
    }
    
    public List<Jugador> cargarJugadoresBloqueados() {
        List<Usuario> usuarios = cargarUsuarios();
        List<Jugador> jugadoresActivos = new ArrayList<>();
        
        for (Usuario usuario : usuarios) {
            if ("bloqueado".equals(usuario.getEstado()) && "jugador".equals(usuario.getRol())) {
                // Crear un nuevo Jugador a partir del Usuario
                List<Personaje> personajes = cargarPersonajesUsuario(usuario.getNick());
                Desafio desafio = this.storage.cargarDesafioUsuario(usuario.getNick(), usuarios);
                Jugador jugador = new Jugador(usuario.getUserId(), usuario.getNick(), usuario.getNombre(), usuario.getPassword(), usuario.getRol(), usuario.getEstado(), usuario.getOro(), usuario.getPuntos()
, personajes, desafio);
                jugadoresActivos.add(jugador);
            }
        }
        
        return jugadoresActivos;
    }
    
    /**
     * Loads all available weapons.
     * 
     * @return List of available weapons
     */
    public List<Arma> cargarArmasDisponibles() {
        return this.storage.cargarArmas();
    }
    
    /**
     * Loads all available armors.
     * 
     * @return List of available armors
     */
    public List<Armadura> cargarArmadurasDisponibles() {
        return this.storage.cargarArmaduras();
    }
    
    /**
     * Loads all available strengths.
     * 
     * @return List of available strengths
     */
    public List<Fortaleza> cargarFortalezasDisponibles() {
        return this.storage.cargarFortalezas();
    }
    
    public String obtenerTipoDePersonajeDesafiantePorDesafioId(UUID desafioId) {
    	return this.storage.obtenerTipoDePersonajeDesafiantePorDesafioId(desafioId);
    }
    
    /**
     * Loads all available weaknesses.
     * 
     * @return List of available weaknesses
     */
    public List<Debilidad> cargarDebilidadesDisponibles() {
        return this.storage.cargarDebilidades();
    }
    
    /**
     * Loads all available minions.
     * 
     * @return List of available minions
     */
    public List<Esbirro> cargarEsbirrosDisponibles() {
        return this.storage.cargarEsbirros();
    }
    
    public List<String> getTiposPersonajes() {
    	return this.storage.getTiposPersonajes();
    }
    
    public int getSaludPorTipoNombre(String nombreTipo) {
    	return this.storage.getSaludPorTipoNombre(nombreTipo);
    }

	public String actualizarPersonajesUsuario(String nick, Personaje personaje) {
		return this.storage.actualizarPersonajeUsuario(nick, personaje);
	}
    
}