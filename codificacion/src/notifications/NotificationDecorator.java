package notifications;

import java.util.List;

import model.usuario.Usuario;

public abstract class NotificationDecorator implements I_Notification {
    protected I_Notification decorado;

    public NotificationDecorator(I_Notification decorado) {
        this.decorado = decorado;
    }

    @Override
    public List getNotificacion(Usuario usuario) {
        return decorado.getNotificacion(usuario);
    }

    @Override
    public void setNotificacion(String nick, String mensaje) {
        decorado.setNotificacion(nick, mensaje);
    }

    @Override
    public void deleteNotificacion(Usuario usuario) {
        decorado.deleteNotificacion(usuario);
    }
}