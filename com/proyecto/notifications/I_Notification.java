package com.proyecto.notifications;

public class I_Notification {
    private FileManager fileManager;

    public String getNotificacion(Usuario usuario) {
        // Implement logic to get notification for the user
        return fileManager.readNotification(usuario);
    }

    public void setNotificacion(Usuario usuario, String mensaje) {
        // Implement logic to set notification for the user
        fileManager.writeNotification(usuario, mensaje);
    }

    public void deleteNotificacion(Usuario usuario) {
        // Implement logic to delete notification for the user
        fileManager.deleteNotification(usuario);
    }
}