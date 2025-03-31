package auth;

import model.Sistema;
import model.usuario.Usuario;
import storage.FileManager;
import ui.A_Interfaz; // Asegúrate de que este sea el nombre correcto del adaptador en la carpeta ui

public class PasarelaAuthorization {
    private Authorization auth;
    private A_Interfaz interfaz; // Adaptador para la interacción con el usuario
    private FileManager fileManager;
    private int badCredential;

    // Constructor
    public PasarelaAuthorization() {
        this.fileManager = Sistema.getFileManager();
        if (this.auth == null) {
        	this.auth = new Authorization(); // Usamos el patrón Singleton
        }
        this.interfaz = interfaz; // Se recibe el adaptador como dependencia
        this.badCredential = 0;
    }

    // Método para iniciar sesión
    public Usuario iniciarSesion() {
        interfaz.mostrar("=== INICIO DE SESIÓN ===");
        interfaz.mostrar("Por favor, introduce tu nombre de usuario:");
        String nick = interfaz.pedirEntrada();
        interfaz.mostrar("Por favor, introduce tu contraseña:");
        String contraseña = interfaz.pedirEntrada();

        String resultado = auth.checkPassword(nick, contraseña);
        if (resultado.equals("Contraseña correcta.")) {
            badCredential = 0; // Reinicia el contador de intentos fallidos
            interfaz.mostrar("✅ Inicio de sesión exitoso. ¡Bienvenido, " + nick + "!");
            //TODO: return new Usuario(nick, contraseña); // Devuelve el usuario autenticado
            return null;
        } else {
            badCredential++;
            interfaz.mostrar("❌ Error: " + resultado);
            return null; // Devuelve null si no se pudo iniciar sesión
        }
    }

    // Método para registrar un usuario
    public Usuario registrarUsuario() {
        interfaz.mostrar("=== REGISTRO DE USUARIO ===");
        interfaz.mostrar("Por favor, introduce un nombre de usuario:");
        String nick = interfaz.pedirEntrada();
        interfaz.mostrar("Por favor, introduce una contraseña:");
        String contraseña = interfaz.pedirEntrada();
        
        Usuario nuevoUsuario = null;
      //TODO: nuevoUsuario = new Usuario(nick, contraseña); // Crea un nuevo usuario
        String resultado = auth.guardarUsuario(nuevoUsuario);
        if (resultado.equals("Usuario guardado correctamente.")) {
            interfaz.mostrar("✅ " + resultado + " ¡Bienvenido, " + nick + "!");
            return nuevoUsuario; // Devuelve el usuario registrado
        } else {
            interfaz.mostrar("❌ " + resultado);
            return null; // Devuelve null si no se pudo registrar
        }
    }

    // Método para mostrar el menú de sesión
    public Usuario menuSesion() {
        Usuario usuario = null;
        while (usuario == null) { // Repite hasta que se obtenga un usuario válido
            interfaz.mostrar("=== BIENVENIDO A LA PASARELA DE AUTORIZACIÓN ===");
            interfaz.mostrar("Por favor, selecciona una opción:");
            interfaz.mostrar("1. Iniciar sesión");
            interfaz.mostrar("2. Registrarse");
            interfaz.mostrar("3. Salir");
            interfaz.mostrar("==============================================");
            String opcion = interfaz.pedirEntrada();

            switch (opcion) {
                case "1":
                    usuario = iniciarSesion(); // Intenta iniciar sesión
                    break;
                case "2":
                    usuario = registrarUsuario(); // Intenta registrar un usuario
                    break;
                case "3":
                    interfaz.mostrar("👋 ¡Gracias por usar el sistema! Hasta pronto.");
                    return null; // Devuelve null para indicar que el usuario ha salido
                default:
                    interfaz.mostrar("⚠️ Opción no válida. Por favor, intenta de nuevo.");
                    break;
            }
        }
        return usuario; // Devuelve el usuario autenticado o registrado
    }
}
