package model.usuario;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

public class Usuario implements I_Usuario {
    private LocalDateTime fecha;
    private UUID userId;
    private String nick;
    private String nombre;
    private String password;
    private String rol;
    private String estado;

    // Constructor
    public Usuario(String nick, String nombre, String password, String rol, String estado) {
        this.fecha = LocalDateTime.now();
        this.userId = UUID.randomUUID();
        this.nick = nick;
        this.nombre = nombre;
        this.password = password;
        this.rol = rol;
        this.estado = estado;
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

    public String getNick() {
        return nick;
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
        return "Usuario actualizado correctamente";
    }
}