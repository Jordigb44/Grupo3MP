package storage;

import java.util.List;

import model.desafio.Combate;
import model.desafio.Desafio;
import model.personaje.Personaje;
import model.personaje.habilidad.Arma;
import model.personaje.habilidad.Armadura;
import model.usuario.Usuario;
import notifications.I_Notification;

public class FileManager {
    private I_Storage storage;

    // Constructor that accepts a storage adapter
    public FileManager(I_Storage storage) {
        if (storage == null) {
            throw new IllegalArgumentException("Storage adapter cannot be null");
        }
        this.storage = storage;
    }

    // Save and load methods for different types of data, delegating the work to the storage adapter

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

    public I_Notification getNotificacion(Usuario usuario) {
        return storage.getNotificacion(usuario);
    }

    public void setNotificacion(Usuario usuario, String mensaje) {
        storage.setNotificacion(usuario, mensaje);
    }

    public void deleteNotificacion(Usuario usuario) {
        storage.deleteNotificacion(usuario);
    }
}