package model.usuario;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import model.personaje.Personaje;
import storage.FileManager;
import ui.A_Interfaz;

import java.util.ArrayList;

public class Usuario implements I_Usuario {
	protected LocalDateTime fecha;
    protected UUID userId;
    protected String nick;
    protected String nombre;
    protected String password;
    protected String rol;
    protected String estado;
    protected String tipo;
    protected Integer oro;
    protected Integer puntos;
//    protected List<Personaje> personajes;
    
    protected static A_Interfaz instanceInterface;
    protected static FileManager fileManager;

    // Constructor
    public Usuario(UUID userId, String nick, String nombre, String password, String rol, String estado, Integer oro, Integer puntos) {
        this.fecha = LocalDateTime.now();
        if (userId == null) {
        	this.userId = UUID.randomUUID();
        }
        this.userId = userId;
        this.nick = nick;
        this.nombre = nombre;
        this.password = password;
        this.rol = rol;
        this.estado = estado;
        this.oro = oro;
        this.puntos = puntos;
    }

	// Implementación de los métodos de la interfaz
    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public Integer getOro() {
        return this.oro;
    }

    public void setOro(Integer oro) {
        this.oro = oro;
    }

    public Integer getPuntos() {
        return this.puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }
    
    @Override
    public List<Object> getUser() {
        List<Object> userData = new ArrayList<>();
        userData.add(userId);
        userData.add(nick);
        userData.add(nombre);
        userData.add(password);
        userData.add(rol);
        userData.add(estado);
        userData.add(fecha);
        userData.add(oro);
        userData.add(puntos);
        return userData;
    }

    @Override
    public String setUser(List<Object> userData) {
        if (userData.size() < 7) {
            return "Datos insuficientes para establecer el usuario";
        }
        this.userId = (UUID) userData.get(0);
        this.nick = (String) userData.get(1);
        this.nombre = (String) userData.get(2);
        this.password = (String) userData.get(3);
        this.rol = (String) userData.get(4);
        this.estado = (String) userData.get(5);
        this.fecha = (LocalDateTime) userData.get(6);
        this.oro = (Integer) userData.get(7);
        this.puntos = (Integer) userData.get(8);
        return "Usuario actualizado correctamente";
    }

	public String getNick() {
		return this.nick;
	}
}