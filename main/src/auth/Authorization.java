package auth;

import java.util.List;

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
        String resultado = buscarUsuario(nick);
        if ("Usuario encontrado.".equals(resultado)) {
            return "Usuario encontrado.";
        } else {
            return "Usuario no encontrado.";
        }
    }
    
    private String buscarUsuario(String nick) {
        // Implementación real para buscar usuario
        // Aquí deberías usar el fileManager para buscar el usuario por su nick
        List<Usuario> usuarios = fileManager.cargarUsuarios();
        for (Usuario u : usuarios) {
            if (u.getNick().equals(nick)) {
                return "Usuario encontrado.";
            }
        }
        return "Usuario no encontrado.";
    }
    
    // Método para verificar la contraseña de un usuario
    public String checkPassword(String nick, String contraseña) {
        // Lógica para verificar la contraseña
        String resultado = verificarCredenciales(nick, contraseña);
        if ("Contraseña correcta.".equals(resultado)) {
            return "Contraseña correcta.";
        } else {
            return "Contraseña incorrecta.";
        }
    }
    
    private String verificarCredenciales(String nick, String contraseña) {
        // Implementación real para verificar credenciales
        // Aquí deberías usar el fileManager para cargar usuarios y verificar credenciales
        List<Usuario> usuarios = fileManager.cargarUsuarios();
        for (Usuario u : usuarios) {
            if (u.getNick().equals(nick) && u.getPassword().equals(contraseña)) {
                return "Contraseña correcta.";
            }
        }
        return "Contraseña incorrecta.";
    }
    
    // Método para guardar un nuevo usuario
    public String guardarUsuario(Usuario usuario) {
        // Lógica para guardar el usuario
        String resultado = fileManager.guardarUsuario(usuario);
        System.out.println("Resultado de guardar usuario: " + resultado);
        
        // No sabemos exactamente qué devuelve el método fileManager.guardarUsuario
        // así que comprobamos si contiene un mensaje de éxito
        if (resultado != null && resultado.contains("Usuario guardado correctamente.")) {
            return "Usuario guardado correctamente.";
        } else {
            return "Error al guardar el usuario.";
        }
    }
}