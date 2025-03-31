package auth;

import model.Sistema;
import model.usuario.Usuario;
import storage.FileManager;

public class Authorization {
    public Authorization() {
        // Constructor de la clase Authorization
        FileManager fileManager = Sistema.getFileManager();
    } 

    // Método para verificar si un usuario existe
    public String existeUsuario(String nick) {
        // Lógica para verificar si el usuario existe
        if (fileManager.buscarUsuario(nick)) {
            return "Usuario encontrado.";
        } else {
            return "Usuario no encontrado.";
        }
    }

    // Método para verificar la contraseña de un usuario
    public String checkPassword(String nick, String contraseña) {
        // Lógica para verificar la contraseña
        if (fileManager.verificarCredenciales(nick, contraseña)) {
            return "Contraseña correcta.";
        } else {
            return "Contraseña incorrecta.";
        }
    }

    // Método para guardar un nuevo usuario
    public String guardarUsuario(Usuario usuario) {
        // Lógica para guardar el usuario
        if (fileManager.guardarUsuario(usuario)) {
            return "Usuario guardado correctamente.";
        } else {
            return "Error al guardar el usuario.";
        }
    }
}
