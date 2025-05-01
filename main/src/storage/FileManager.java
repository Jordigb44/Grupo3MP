package storage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.Ranking;
import model.desafio.Combate;
import model.desafio.Desafio;
import model.desafio.E_EstadoDesafio;
import model.personaje.Esbirro;
import model.personaje.Personaje;
import model.personaje.habilidad.Arma;
import model.personaje.habilidad.Armadura;
import model.personaje.habilidad.Debilidad;
import model.personaje.habilidad.Fortaleza;
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
    		this.storage =  storage;
    	}
    }

    // TODO: Save and load methods for different types of data, delegating the work to the storage adapter

    public String guardarUsuario(Usuario usuario) {
        return storage.guardarUsuario(usuario);
    }
    
    public String darDeBajaUsuario(String nick) {
        return storage.darDeBajaUsuario(nick);
    }

    public List<Usuario> cargarUsuarios() {
        return storage.cargarUsuarios();
    }
    
    public void actualizarJugador(Jugador jugador) {
		// TODO Auto-generated method stub
		
	}

    public String guardarPersonajes(List<Personaje> personajes) {
        return storage.guardarPersonajes(personajes);
    }
    
    public List<Personaje> cargarPersonajesUsuario(String nick) {
        return storage.cargarPersonajesUsuario(nick);
    }

    public List<Personaje> cargarPersonajes() {
        return storage.cargarPersonajes();
    }

    public String guardarCombate(Combate combate) {
        return storage.guardarCombate(combate);
    }

    public List<model.desafio.Combate> cargarCombates() {
        return storage.cargarCombates();
    }

    public String guardarDesafio(Desafio desafio) {
        return storage.guardarDesafio(desafio);
    }

    public List<Desafio> cargarDesafios() {
        return storage.cargarDesafios();
    }

    public List<Arma> cargarArmas() {
        return storage.cargarArmas();
    }

    public List<Armadura> cargarArmaduras() {
        return storage.cargarArmaduras();
    }

    public List<String> getNotificacion(Usuario usuario) {
        return storage.getNotificacion(usuario);
    }

    public void setNotificacion(String nick, String mensaje) {
        storage.setNotificacion(nick, mensaje);
    }

    public void deleteNotificacion(Usuario usuario) {
        storage.deleteNotificacion(usuario);
    }

	public void actualizarDesafio(Desafio desafio) {
		// TODO Auto-generated method stub
		
	}

	public Desafio cargarDesafio(UUID desafioId) {
		// TODO Auto-generated method stub
		return null;
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
     * Loads all players that are not blocked.
     * 
     * @return List of unblocked players
     */
    public List<Jugador> cargarJugadoresSinBloquear(List<Usuario> usuarios) {
        List<Jugador> jugadoresSinBloquear = new ArrayList<>();
        
        for (Usuario usuario : usuarios) {
            if (!"bloqueado".equals(usuario.getEstado())) {
                if (usuario instanceof Jugador) {
                    jugadoresSinBloquear.add((Jugador) usuario);
                }
            }
        }
        
        return jugadoresSinBloquear;
    }
    
    /**
     * Loads all blocked players.
     * 
     * @return List of blocked players
     */
    public List<Jugador> cargarJugadoresBloqueados(List<Usuario> usuarios) {
        List<Jugador> jugadoresBloqueados = new ArrayList<>();
        
        for (Usuario usuario : usuarios) {
            if ("bloqueado".equals(usuario.getEstado())) {
                if (usuario instanceof Jugador) {
                    jugadoresBloqueados.add((Jugador) usuario);
                }
            }
        }
        
        return jugadoresBloqueados;
    }
    
    /**
     * Loads all pending challenges.
     * 
     * @return List of pending challenges
     */
    public List<Desafio> cargarDesafiosPendientes() {
        List<Desafio> desafios = cargarDesafios();
        List<Desafio> desafiosPendientes = new ArrayList<>();
        
        for (Desafio desafio : desafios) {
            if (E_EstadoDesafio.PENDIENTE.equals(desafio.getEstado())) {
                desafiosPendientes.add(desafio);
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
            if ("activo".equals(usuario.getEstado())) {
                if (usuario instanceof Jugador) {
                    jugadoresActivos.add((Jugador) usuario);
                }
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
        return cargarArmas();
    }
    
    /**
     * Loads all available armors.
     * 
     * @return List of available armors
     */
    public List<Armadura> cargarArmadurasDisponibles() {
        return cargarArmaduras();
    }
    
    /**
     * Loads all available strengths.
     * 
     * @return List of available strengths
     */
    public List<Fortaleza> cargarFortalezasDisponibles() {
        // Basic implementation, in a real system this would load from storage
        return new ArrayList<>();
    }
    
    /**
     * Loads all available weaknesses.
     * 
     * @return List of available weaknesses
     */
    public List<Debilidad> cargarDebilidadesDisponibles() {
        // Basic implementation, in a real system this would load from storage
        return new ArrayList<>();
    }
    
    /**
     * Loads all available minions.
     * 
     * @return List of available minions
     */
    public List<Esbirro> cargarEsbirrosDisponibles() {
        // Basic implementation, in a real system this would load from storage
        return new ArrayList<>();
    }

}