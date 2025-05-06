import notifications.NotificationInterna;
import model.usuario.Usuario;
import storage.FileManager;
import storage.XMLStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class notificationTest {

    private FileManager fileManager;
    private NotificationInterna notificationInterna;
    private Usuario usuario;

    private final String storagePath = "./"; // Ruta de almacenamiento

    @BeforeEach
    public void setUp() {
        // Inicializamos FileManager con un almacenamiento real (XMLStorage)
        fileManager = new FileManager(new XMLStorage(storagePath));
        // Inicializamos NotificationInterna con el FileManager real
        notificationInterna = new NotificationInterna(fileManager);

        // Creamos un usuario antes de cada test
        usuario = crearUsuario("juan", "1234", "Activo");

        // Limpiamos las notificaciones del usuario antes de cada test
        notificationInterna.deleteNotificacion(usuario);
    }

    @AfterEach
    public void tearDown() {
        // Limpiamos las notificaciones después de cada prueba para evitar efectos secundarios
        notificationInterna.deleteNotificacion(usuario);
    }

    private Usuario crearUsuario(String nick, String password, String estado) {
        return new Usuario(
                UUID.randomUUID(), // userId
                nick,              // nick
                "Nombre",          // nombre
                password,          // password
                "Jugador",         // rol
                estado,            // estado
                100,               // oro
                50                 // puntos
        );
    }

    @Test
    public void testGetNotificacion_ExistenNotificaciones() {
        // Agregamos una notificación al usuario
        notificationInterna.setNotificacion(usuario.getNick(), "Mensaje de prueba");

        // Llamamos al método que estamos probando
        List<String> notificaciones = notificationInterna.getNotificacion(usuario);

        // Verificamos que la lista de notificaciones no esté vacía
        assertTrue(notificaciones.size() > 0);
        assertTrue(notificaciones.get(0).startsWith("Mensaje de prueba"));
    }

    @Test
    public void testGetNotificacion_SinNotificaciones() {
        // Llamamos al método que estamos probando sin agregar notificaciones
        List<String> notificaciones = notificationInterna.getNotificacion(usuario);

        // Verificamos que la lista de notificaciones esté vacía
        assertTrue(notificaciones.isEmpty());
    }

    @Test
    public void testSetNotificacion_Exito() {
        // Llamamos al método para agregar una notificación
        notificationInterna.setNotificacion(usuario.getNick(), "Notificación de prueba set");

        // Verificamos que la notificación se haya guardado correctamente
        List<String> notificaciones = notificationInterna.getNotificacion(usuario);
        assertTrue(notificaciones.get(0).startsWith("Notificación de prueba set"));
    }

    @Test
    public void testDeleteNotificacion_Exito() {
        // Agregamos una notificación al usuario
        notificationInterna.setNotificacion(usuario.getNick(), "Notificación de prueba test Delete Notification");

        // Verificamos que la notificación existe
        List<String> notificaciones = notificationInterna.getNotificacion(usuario);
        assertTrue(
                notificaciones.stream().anyMatch(msg -> msg.contains("Notificación de prueba test Delete Notification"))
        );

        // Llamamos al método para eliminar la notificación
        notificationInterna.deleteNotificacion(usuario);

        // Verificamos que la notificación ya no esté presente
        notificaciones = notificationInterna.getNotificacion(usuario);
        boolean contieneMensaje = notificaciones.stream()
                .anyMatch(msg -> msg.contains("Notificación de prueba test Delete Notification"));

        assertFalse(contieneMensaje);
    }
}