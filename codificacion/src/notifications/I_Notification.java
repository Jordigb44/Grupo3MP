package notifications;

import java.util.List;

import model.usuario.Usuario;
import storage.FileManager;

public interface I_Notification {
    public static final FileManager fileManager = null;

    // Obtener la notificación de un usuario
    List<String> getNotificacion(Usuario usuario);

    // Establecer una notificación para un usuario
    void setNotificacion(String nick, String mensaje);

    // Eliminar la notificación de un usuario
    void deleteNotificacion(Usuario usuario);

}