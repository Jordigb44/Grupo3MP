package storage;

import java.util.List;
import java.util.UUID;

import model.desafio.Combate;
import model.desafio.Desafio;
import model.desafio.I_Desafio;
import model.personaje.Personaje;
import model.personaje.habilidad.Arma;
import model.personaje.habilidad.Armadura;
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

    public List<Usuario> cargarUsuarios() {
        return storage.cargarUsuarios();
    }

    public String guardarPersonajes(List<Personaje> personajes) {
        return storage.guardarPersonajes(personajes);
    }

    public List<Personaje> cargarPersonajes() {
        return storage.cargarPersonajes();
    }

    public String guardarCombates(List<Combate> combates) {
        return storage.guardarCombates(combates);
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

	
		
	
}
