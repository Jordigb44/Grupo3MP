package auth;

import model.Sistema;
import model.usuario.Usuario;
import storage.FileManager;

public class Authorization {
	private FileManager fileManager;
    public Authorization() {
        // Constructor de la clase Authorization
        fileManager = Sistema.getFileManager();
    } 

    // Método para verificar si un usuario existe
    public String existeUsuario(String nick) {
        // Lógica para verificar si el usuario existe
        if (buscarUsuario(nick) == "Usuario encontrado.") {
            return "Usuario encontrado.";
        } else {
            return "Usuario no encontrado.";
        }
    }

    private String buscarUsuario(String nick) {
		// TODO Auto-generated method stub
		return "Usuario encontrado.";
	}

	// Método para verificar la contraseña de un usuario
    public String checkPassword(String nick, String contraseña) {
        // Lógica para verificar la contraseña
        if (verificarCredenciales(nick, contraseña) == "Contraseña correcta.") {
            return "Contraseña correcta.";
        } else {
            return "Contraseña incorrecta.";
        }
    }

    private String verificarCredenciales(String nick, String contraseña) {
		// TODO Auto-generated method stub
		return "Contraseña correcta.";
	}

	// Método para guardar un nuevo usuario
    public String guardarUsuario(Usuario usuario) {
        // Lógica para guardar el usuario
        if (fileManager.guardarUsuario(usuario) == "Usuario guardado correctamente.") {
            return "Usuario guardado correctamente.";
        } else {
            return "Error al guardar el usuario.";
        }
    }
}
