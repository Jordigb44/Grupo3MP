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
    private A_Interfaz interfaz;
    private FileManager fileManager;
    private NotificationInterna notificationInterna;
    private int badCredential;

    // Constructor
    public PasarelaAuthorization(FileManager fileManager, A_Interfaz interfaz, NotificationInterna notificationInterna) {
        this.fileManager = fileManager;
        if (this.auth == null) {
        	this.auth = new Authorization(this.fileManager); // Usamos el patrón Singleton
        }
        this.interfaz = interfaz;
        this.notificationInterna = notificationInterna;  // Usamos el patrón Singleton
        this.badCredential = 0;
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
            this.interfaz.mostrar("❌ Usuario o contraseña incorrecto, intenta de nuevo en 2 segundos. ❌\n");
            try {
                Thread.sleep(2000); // Espera 2 segundos antes de limpiar la pantalla
            } catch (InterruptedException e) {
                e.printStackTrace(); // Manejo básico de error
            }
            this.interfaz.limpiarPantalla();
            return null; // Devuelve null si no se pudo iniciar sesión
        }
        if (resultado.equals("Este usuario está dado de baja.")) {
        	this.interfaz.mostrar("Su usuario esta dado de baja, contacte con el administrador o cree un nuevo usuario, intente de nuevo en 2 segundos\n");
        	try {
                Thread.sleep(2000); // Espera 2 segundos antes de limpiar la pantalla
            } catch (InterruptedException e) {
                e.printStackTrace(); // Manejo básico de error
            }
        	this.interfaz.limpiarPantalla();
        	return null;
        }
        try {
            this.badCredential = 0; // Reinicia el contador de intentos fallidos
            // Si la autenticación fue exitosa, obtenemos y mostramos las notificaciones
            Usuario usuarioAutenticado = (Usuario) resultado;
            this.interfaz.mostrar("✅ Inicio de sesión exitoso. ¡Bienvenido, " + usuarioAutenticado.getNick()+ " - "+ usuarioAutenticado.getOro() + "!");
            mostrarNotificaciones(usuarioAutenticado);
            return usuarioAutenticado; // <-- Retorna el objeto Usuario autenticado

        } catch (Exception e) {
            // Aquí puedes manejar la excepción que pueda ocurrir dentro del bloque try
            this.interfaz.mostrar("❌ Usuario o contraseña incorrecto, intenta de nuevo en 2 segundos. ❌\n");
            try {
                Thread.sleep(2000); // Espera 2 segundos antes de limpiar la pantalla
            } catch (InterruptedException e1) {
                e1.printStackTrace(); // Manejo básico de error
            }
            this.interfaz.limpiarPantalla();
            return null;
        }
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
        
        Usuario nuevoUsuario = new Usuario(null, nick, nombre, contraseña, "jugador", "activo", 0, 0); // Crea un nuevo usuario
        String resultado = this.auth.guardarUsuario(nuevoUsuario);
        if (resultado.equals("Usuario guardado correctamente.")) {
            return nuevoUsuario; // Devuelve el usuario registrado
        } else {
            this.interfaz.mostrar("❌ " + resultado);
            return null; // Devuelve null si no se pudo registrar
        }
    }

    // Método para mostrar el menú de sesión
    public Usuario menuSesion() {
        Usuario usuario = null;
        int intentosFallidos = 0;

        while (usuario == null) {
            this.interfaz.mostrar("=== BIENVENIDO A LA PASARELA DE AUTORIZACIÓN ===");
            this.interfaz.mostrar("Por favor, selecciona una opción:");
            this.interfaz.mostrar("1. Iniciar sesión");
            this.interfaz.mostrar("2. Registrarse");
            this.interfaz.mostrar("3. Salir");
            this.interfaz.mostrar("==============================================");
            String opcion = this.interfaz.pedirEntrada().trim();

            switch (opcion) {
                case "1":
                    usuario = iniciarSesion();
                    if (usuario != null) {
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
                    intentosFallidos = 0;
                    break;

                case "2":
                    registrarUsuario();
                    break;

                case "3":
                    this.interfaz.mostrar("👋 ¡Gracias por usar el sistema! Hasta pronto.");
                    return null;

                default:
                    intentosFallidos++;
                    this.interfaz.mostrar("⚠️ Opción no válida. Intentos restantes: " + (3 - intentosFallidos));
                    if (intentosFallidos >= 3) {
                        this.interfaz.mostrar("🚫 Demasiados intentos inválidos.");
                        return null;
                    }
                    break;
            }

            this.interfaz.limpiarPantalla(); // opcional, si tu interfaz lo soporta
        }

        return usuario;
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
