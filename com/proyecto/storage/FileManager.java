package com.proyecto.storage;

import java.util.List;

import com.proyecto.model.personaje.Personaje;
import com.proyecto.model.personaje.habilidad.Arma;
import com.proyecto.model.personaje.habilidad.Armadura;

public class FileManager {
    private I_Storage storage;

    // Constructor that accepts a storage adapter
    public FileManager(I_Storage storage) {
        if (storage == null) {
            throw new IllegalArgumentException("Storage adapter cannot be null");
            this.storage = new storage;
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

    public List<Combate> cargarCombates() {
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

    public Notificacion getNotificacion(Usuario usuario) {
        return storage.getNotificacion(usuario);
    }

    public void setNotificacion(Usuario usuario, String mensaje) {
        storage.setNotificacion(usuario, mensaje);
    }

    public void deleteNotificacion(Usuario usuario) {
        storage.deleteNotificacion(usuario);
    }
}