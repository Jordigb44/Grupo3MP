package model.usuario;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface I_Usuario {
    // Attributes
    LocalDateTime fecha = null;
    UUID userId = null;
    String nick = null;
    String nombre = null;
    String password = null;
    String rol = null;
    String estado = null;
    Integer oro = null;

    // Methods
    List<Object> getUser();
    String setUser(List<Object> userData);
}
