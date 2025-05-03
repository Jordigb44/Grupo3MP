package auth;

import java.util.List;

import model.Sistema;
import model.usuario.Usuario;
import storage.FileManager;

public class Authorization {
    private FileManager fileManager;
    
    public Authorization(FileManager fileManager) {
        // Constructor de la clase Authorization
        this.fileManager = fileManager;
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
        List<Usuario> usuarios = this.fileManager.cargarUsuarios();
        for (Usuario u : usuarios) {
            if (u.getNick().equals(nick)) {
                return "Usuario encontrado.";
            }
        }
        return "Usuario no encontrado.";
    }
    
    // Método para verificar la contraseña de un usuario
    public Object checkPassword(String nick, String contraseña) {
        // Lógica para verificar la contraseña
        Object resultado = verificarCredenciales(nick, contraseña);
        return resultado;
    }
    
    private Object verificarCredenciales(String nick, String contraseña) {
        List<Usuario> usuarios = this.fileManager.cargarUsuarios();
        
        for (Usuario u : usuarios) {
            if (u.getNick().equals(nick)) {
                if (!u.getPassword().equals(contraseña)) {
                    return "Contraseña incorrecta.";
                }
                if ("bloqueado".equalsIgnoreCase(u.getEstado())) {
                    return "Este usuario está bloqueado y no puede iniciar sesión.";
                }
                return u; // Credenciales correctas y usuario no bloqueado
            }
        }

        return "Usuario no encontrado.";
    }
    
    // Método para guardar un nuevo usuario
    public String guardarUsuario(Usuario usuario) {
        // Lógica para guardar el usuario
        String resultado = this.fileManager.guardarUsuario(usuario);
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