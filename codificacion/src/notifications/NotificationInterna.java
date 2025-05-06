package notifications;

import java.util.List;

import model.Sistema;
import model.usuario.Usuario;
import storage.FileManager;

public class NotificationInterna implements I_Notification {
    private FileManager fileManager;

    public NotificationInterna(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    @Override
    public List<String> getNotificacion(Usuario usuario) {
        return fileManager.getNotificacion(usuario);
    }

    @Override
    public void setNotificacion(String nick, String mensaje) {
        fileManager.setNotificacion(nick, mensaje);
    }

    @Override
    public void deleteNotificacion(Usuario usuario) {
        fileManager.deleteNotificacion(usuario);
    }
}