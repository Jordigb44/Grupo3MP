package notifications;

import model.usuario.Usuario;
import storage.FileManager;

public interface I_Notification {
    public static final FileManager fileManager = null;

    // Obtener la notificación de un usuario
    String getNotificacion(Usuario usuario);

    // Establecer una notificación para un usuario
    void setNotificacion(Usuario usuario, String mensaje);

    // Eliminar la notificación de un usuario
    void deleteNotificacion(Usuario usuario);
}