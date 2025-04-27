package auth;

import java.util.List;

import model.Sistema;
import model.usuario.Operador;
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
        this.interfaz.mostrar("=== INICIO DE SESIÓN ===");
        this.interfaz.mostrar("Por favor, introduce tu nick:");
        String nick = this.interfaz.pedirEntrada();
        this.interfaz.mostrar("Por favor, introduce tu contraseña:");
        String contraseña = this.interfaz.pedirEntrada();

        Object resultado = this.auth.checkPassword(nick, contraseña);
        if (resultado.equals("Contraseña incorrecta.")) {
            this.badCredential++;
         // Si badCredential llega a 3, se envía una notificación al usuario
            if (this.badCredential == 3) {
                // Verificar si el usuario con el nick ingresado existe en el sistema
                Usuario usuarioExistente = obtenerUsuarioPorNick(nick);
                if (usuarioExistente != null) { // Si el usuario existe
                    String mensaje = "Se han intentado 3 inicios de sesión fallidos con tu cuenta";
                    this.notificationInterna.setNotificacion(nick, mensaje); // Enviar notificación
                    this.interfaz.mostrar("🚨 ¡Alerta! Se ha enviado una notificación al usuario: '" + nick + "'.");
                }
            }
            return null; // Devuelve null si no se pudo iniciar sesión
        }
        this.badCredential = 0; // Reinicia el contador de intentos fallidos
        this.interfaz.mostrar("✅ Inicio de sesión exitoso. ¡Bienvenido, " + nick + "!");
        // Si la autenticación fue exitosa, obtenemos y mostramos las notificaciones
        Usuario usuarioAutenticado = (Usuario) resultado;
        mostrarNotificaciones(usuarioAutenticado);
        return usuarioAutenticado; // <-- Retorna el objeto Usuario autenticado
    }

    // Método para registrar un usuario
    public Usuario registrarUsuario() {
        this.interfaz.mostrar("=== REGISTRO DE USUARIO ===");
        this.interfaz.mostrar("Por favor, introduce un nombre de usuario:");
        String nick = this.interfaz.pedirEntrada();
        this.interfaz.mostrar("Por favor, introduce su nombre:");
        String nombre = this.interfaz.pedirEntrada();
        this.interfaz.mostrar("Por favor, introduce una contraseña:");
        String contraseña = this.interfaz.pedirEntrada();
        
        Usuario nuevoUsuario = new Usuario(nick, nombre, contraseña, null, null); // Crea un nuevo usuario
        String resultado = this.auth.guardarUsuario(nuevoUsuario);
        if (resultado.equals("Usuario guardado correctamente.")) {
            this.interfaz.mostrar("✅ " + resultado + " ¡Bienvenido, " + nick + "!");
            return nuevoUsuario; // Devuelve el usuario registrado
        } else {
            this.interfaz.mostrar("❌ " + resultado);
            return null; // Devuelve null si no se pudo registrar
        }
    }

    // Método para mostrar el menú de sesión
    public Usuario menuSesion() {
        Usuario usuario = null;
        while (usuario == null) { // Repite hasta que se obtenga un usuario válido
            this.interfaz.mostrar("=== BIENVENIDO A LA PASARELA DE AUTORIZACIÓN ===");
            this.interfaz.mostrar("Por favor, selecciona una opción:");
            this.interfaz.mostrar("1. Iniciar sesión");
            this.interfaz.mostrar("2. Registrarse");
            this.interfaz.mostrar("3. Salir");
            this.interfaz.mostrar("==============================================");
            String opcion = this.interfaz.pedirEntrada();

            switch (opcion) {
                case "1":
                    usuario = iniciarSesion(); // Intenta iniciar sesión
                    if (usuario != null) {
                        // Si inició sesión bien, pedir el rol
                        this.interfaz.mostrar("¿Qué rol desea usar?");
                        this.interfaz.mostrar("1. Administrador");
                        this.interfaz.mostrar("2. Jugador");
                        String tipoUsuario = this.interfaz.pedirEntrada();
                        
                        if (tipoUsuario.equals("1")) {
                        	usuario.setTipo("operador");
                        } else if (tipoUsuario.equals("2")) {
                        	usuario.setTipo("jugador");
                        } else {
                            this.interfaz.mostrar("⚠️ Rol no válido. Se canceló la selección.");
                        }
                    }
                    break;
                case "2":
                    usuario = registrarUsuario(); // Intenta registrar un usuario
                    break;
                case "3":
                    this.interfaz.mostrar("👋 ¡Gracias por usar el sistema! Hasta pronto.");
                    return null; // Devuelve null para indicar que el usuario ha salido
                default:
                    this.interfaz.mostrar("⚠️ Opción no válida. Por favor, intenta de nuevo.");
                    break;
            }
        }
        return usuario; // Devuelve el usuario autenticado o registrado
    }
    
 // Método para obtener el usuario por su nick
    private Usuario obtenerUsuarioPorNick(String nick) {
        List<Usuario> usuarios = this.fileManager.cargarUsuarios();
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
            this.interfaz.mostrar("ℹ️ No tienes notificaciones nuevas.");
        } else {
            this.interfaz.mostrar("📩 Tienes las siguientes notificaciones:");
            for (String notificacion : notificaciones) {
                this.interfaz.mostrar(" - " + notificacion);
            }
            // Eliminar las notificaciones después de haberlas mostrado
            this.notificationInterna.deleteNotificacion(usuario);
            this.interfaz.mostrar("✅ Las notificaciones han sido eliminadas.");
        }
    }
}
