package auth;

import model.Sistema;
import model.usuario.Usuario;
import storage.FileManager;
import ui.InterfazAdapter; // Asegúrate de que este sea el nombre correcto del adaptador en la carpeta ui

public class PasarelaAuthorization {
    private Authorization auth;
    private InterfazAdapter interfaz; // Adaptador para la interacción con el usuario
    private FileManager fileManager;
    private int badCredential;

    // Constructor
    public PasarelaAuthorization() {
        this.fileManager = Sistema.getFileManager();
        this.auth = Authorization.getInstance(); // Usamos el patrón Singleton
        this.interfaz = interfaz; // Se recibe el adaptador como dependencia
        this.badCredential = 0;
    }

    // Método para iniciar sesión
    public Usuario iniciarSesion() {
        interfaz.mostrarMensaje("=== INICIO DE SESIÓN ===");
        interfaz.mostrarMensaje("Por favor, introduce tu nombre de usuario:");
        String nick = interfaz.leerEntrada();
        interfaz.mostrarMensaje("Por favor, introduce tu contraseña:");
        String contraseña = interfaz.leerEntrada();

        String resultado = auth.checkPassword(nick, contraseña);
        if (resultado.equals("Contraseña correcta.")) {
            badCredential = 0; // Reinicia el contador de intentos fallidos
            interfaz.mostrarMensaje("✅ Inicio de sesión exitoso. ¡Bienvenido, " + nick + "!");
            return new Usuario(nick, contraseña); // Devuelve el usuario autenticado
        } else {
            badCredential++;
            interfaz.mostrarMensaje("❌ Error: " + resultado);
            return null; // Devuelve null si no se pudo iniciar sesión
        }
    }

    // Método para registrar un usuario
    public Usuario registrarUsuario() {
        interfaz.mostrarMensaje("=== REGISTRO DE USUARIO ===");
        interfaz.mostrarMensaje("Por favor, introduce un nombre de usuario:");
        String nick = interfaz.leerEntrada();
        interfaz.mostrarMensaje("Por favor, introduce una contraseña:");
        String contraseña = interfaz.leerEntrada();

        Usuario nuevoUsuario = new Usuario(nick, contraseña); // Crea un nuevo usuario
        String resultado = auth.guardarUsuario(nuevoUsuario);
        if (resultado.equals("Usuario guardado correctamente.")) {
            interfaz.mostrarMensaje("✅ " + resultado + " ¡Bienvenido, " + nick + "!");
            return nuevoUsuario; // Devuelve el usuario registrado
        } else {
            interfaz.mostrarMensaje("❌ " + resultado);
            return null; // Devuelve null si no se pudo registrar
        }
    }

    // Método para mostrar el menú de sesión
    public Usuario menuSesion() {
        Usuario usuario = null;
        while (usuario == null) { // Repite hasta que se obtenga un usuario válido
            interfaz.mostrarMensaje("=== BIENVENIDO A LA PASARELA DE AUTORIZACIÓN ===");
            interfaz.mostrarMensaje("Por favor, selecciona una opción:");
            interfaz.mostrarMensaje("1. Iniciar sesión");
            interfaz.mostrarMensaje("2. Registrarse");
            interfaz.mostrarMensaje("3. Salir");
            interfaz.mostrarMensaje("==============================================");
            String opcion = interfaz.leerEntrada();

            switch (opcion) {
                case "1":
                    usuario = iniciarSesion(); // Intenta iniciar sesión
                    break;
                case "2":
                    usuario = registrarUsuario(); // Intenta registrar un usuario
                    break;
                case "3":
                    interfaz.mostrarMensaje("👋 ¡Gracias por usar el sistema! Hasta pronto.");
                    return null; // Devuelve null para indicar que el usuario ha salido
                default:
                    interfaz.mostrarMensaje("⚠️ Opción no válida. Por favor, intenta de nuevo.");
                    break;
            }
        }
        return usuario; // Devuelve el usuario autenticado o registrado
    }
}
