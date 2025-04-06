package auth;

import java.util.List;

import model.Sistema;
import model.usuario.Usuario;
import notifications.NotificationInterna;
import storage.FileManager;
import ui.A_Interfaz; // Asegúrate de que este sea el nombre correcto del adaptador en la carpeta ui

public class PasarelaAuthorization {
    private Authorization auth;
    private A_Interfaz interfaz; // Adaptador para la interacción con el usuario
    private FileManager fileManager;
    private int badCredential;
    private NotificationInterna notificationInterna; // Instancia de la clase de notificaciones

    // Constructor
    public PasarelaAuthorization() {
        this.fileManager = Sistema.getFileManager();
        if (this.auth == null) {
        	this.auth = new Authorization(); // Usamos el patrón Singleton
        }
        if (this.interfaz == null) {        	
        	this.interfaz = new A_Interfaz(); // Se recibe el adaptador como dependencia
        }
        this.badCredential = 0;
        this.notificationInterna = new NotificationInterna(); // Inicializar notificaciones
    }

    // Método para iniciar sesión
    public Usuario iniciarSesion() {
        interfaz.mostrar("=== INICIO DE SESIÓN ===");
        interfaz.mostrar("Por favor, introduce tu nick:");
        String nick = interfaz.pedirEntrada();
        interfaz.mostrar("Por favor, introduce tu contraseña:");
        String contraseña = interfaz.pedirEntrada();

        Object resultado = auth.checkPassword(nick, contraseña);
        if (resultado.equals("Contraseña incorrecta.")) {
            badCredential++;
         // Si badCredential llega a 3, se envía una notificación al usuario
            if (badCredential == 3) {
                // Verificar si el usuario con el nick ingresado existe en el sistema
                Usuario usuarioExistente = obtenerUsuarioPorNick(nick);
                if (usuarioExistente != null) { // Si el usuario existe
                    String mensaje = "Se han intentado 3 inicios de sesión fallidos con tu cuenta";
                    notificationInterna.setNotificacion(nick, mensaje); // Enviar notificación
                    interfaz.mostrar("🚨 ¡Alerta! Se ha enviado una notificación al usuario: '" + nick + "'.");
                }
            }
            return null; // Devuelve null si no se pudo iniciar sesión
        }
        badCredential = 0; // Reinicia el contador de intentos fallidos
        interfaz.mostrar("✅ Inicio de sesión exitoso. ¡Bienvenido, " + nick + "!");
        // Si la autenticación fue exitosa, obtenemos y mostramos las notificaciones
        Usuario usuarioAutenticado = (Usuario) resultado;
        mostrarNotificaciones(usuarioAutenticado);
        return usuarioAutenticado; // <-- Retorna el objeto Usuario autenticado
    }

    // Método para registrar un usuario
    public Usuario registrarUsuario() {
        interfaz.mostrar("=== REGISTRO DE USUARIO ===");
        interfaz.mostrar("Por favor, introduce un nombre de usuario:");
        String nick = interfaz.pedirEntrada();
        interfaz.mostrar("Por favor, introduce su nombre:");
        String nombre = interfaz.pedirEntrada();
        interfaz.mostrar("Por favor, introduce una contraseña:");
        String contraseña = interfaz.pedirEntrada();
        
        Usuario nuevoUsuario = new Usuario(nick, nombre, contraseña, null, null); // Crea un nuevo usuario
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
    
 // Método para obtener el usuario por su nick
    private Usuario obtenerUsuarioPorNick(String nick) {
        List<Usuario> usuarios = fileManager.cargarUsuarios();
        for (Usuario u : usuarios) {
            if (u.getNick().equals(nick)) {
                return u; // Retorna el usuario si lo encuentra
            }
        }
        return null; // Si no lo encuentra, retorna null
    }
    
 // Método para mostrar las notificaciones del usuario y eliminarlas después de mostrarlas
    private void mostrarNotificaciones(Usuario usuario) {
        List<String> notificaciones = notificationInterna.getNotificacion(usuario);
        
        if (notificaciones.isEmpty()) {
            interfaz.mostrar("ℹ️ No tienes notificaciones nuevas.");
        } else {
            interfaz.mostrar("📩 Tienes las siguientes notificaciones:");
            for (String notificacion : notificaciones) {
                interfaz.mostrar(" - " + notificacion);
            }
            // Eliminar las notificaciones después de haberlas mostrado
            notificationInterna.deleteNotificacion(usuario);
            interfaz.mostrar("✅ Las notificaciones han sido eliminadas.");
        }
    }
}
