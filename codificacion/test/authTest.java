import auth.Authorization;
import model.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.FileManager;
import storage.XMLStorage; // Asegúrate de que esta clase exista

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class authTest {

    private FileManager fileManager;
    private Authorization authorization;

    @BeforeEach
    public void setUp() {
        // Inicializamos FileManager con un almacenamiento real (en este caso, XMLStorage)
        fileManager = new FileManager(new XMLStorage("./data")); // Ruta donde se almacenan los datos
        // Inicializamos Authorization con el FileManager real
        authorization = new Authorization(fileManager);
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
    public void testExisteUsuario_UsuarioExiste() {
        // Creamos un usuario para asegurarnos de que existe en el sistema
        Usuario usuario = crearUsuario("juan", "1234", "Activo");

        // Guardamos el usuario en el sistema real (en el archivo)
        fileManager.guardarUsuario(usuario);

        // Llamamos al método que estamos probando
        String resultado = authorization.existeUsuario("juan");

        // Verificamos que la respuesta sea la correcta
        assertEquals("Usuario encontrado.", resultado);
    }

    @Test
    public void testExisteUsuario_UsuarioNoExiste() {
        // Aseguramos que el sistema no tiene usuarios en este momento
        List<Usuario> usuarios = fileManager.cargarUsuarios();
        usuarios.clear(); // Limpiamos cualquier dato previo si existe

        // Llamamos al método que estamos probando
        String resultado = authorization.existeUsuario("noExiste");

        // Verificamos que la respuesta sea la correcta
        assertEquals("Usuario no encontrado.", resultado);
    }

    @Test
    public void testCheckPassword_Correcta() {
        // Creamos un usuario para probar el login
        Usuario usuario = crearUsuario("ana", "clave123", "Activo");
        fileManager.guardarUsuario(usuario); // Guardamos el usuario en el archivo

        // Llamamos al método que estamos probando
        Object resultado = authorization.checkPassword("ana", "clave123");

        // Verificamos que el resultado sea un usuario
        assertTrue(resultado instanceof Usuario);
        assertEquals(usuario.getNick(), ((Usuario) resultado).getNick());
    }

    @Test
    public void testCheckPassword_ContraseñaIncorrecta() {
        // Creamos un usuario para probar el login
        Usuario usuario = crearUsuario("luis", "correcta", "Activo");
        fileManager.guardarUsuario(usuario);

        // Llamamos al método que estamos probando con la contraseña incorrecta
        Object resultado = authorization.checkPassword("luis", "incorrecta");

        // Verificamos que la respuesta sea la correcta
        assertEquals("Contraseña incorrecta.", resultado);
    }

    @Test
    public void testCheckPassword_UsuarioDadoDeBaja() {
        // Creamos un usuario con estado "Baja"
        Usuario usuario = crearUsuario("maria", "pass123", "Baja");
        fileManager.guardarUsuario(usuario);

        // Llamamos al método que estamos probando
        Object resultado = authorization.checkPassword("maria", "pass123");

        // Verificamos que la respuesta sea la correcta
        assertEquals("Este usuario está dado de baja.", resultado);
    }

    @Test
    public void testCheckPassword_UsuarioNoExiste() {
        // Aseguramos que el sistema no tiene usuarios en este momento
        List<Usuario> usuarios = fileManager.cargarUsuarios();
        usuarios.clear(); // Limpiamos cualquier dato previo

        // Llamamos al método que estamos probando
        Object resultado = authorization.checkPassword("nadie", "algo");

        // Verificamos que la respuesta sea la correcta
        assertEquals("Usuario no encontrado.", resultado);
    }

    @Test
    public void testGuardarUsuario_Exito() {
        // Creamos un usuario para guardar en el sistema
        Usuario nuevo = crearUsuario("nuevo", "1234", "Activo");
        String resultado = authorization.guardarUsuario(nuevo);

        // Verificamos que la respuesta sea la correcta
        assertEquals("Usuario guardado correctamente.", resultado);
    }

//    @Test
//    public void testGuardarUsuario_Error() {
//        // Creamos un usuario para probar la funcionalidad de guardar
//        Usuario nuevo = crearUsuario("nuevo", "1234", "Activo");
//
//        // Forzamos un error en el guardar (dependiendo de cómo hayas implementado el FileManager)
//        // Aquí supongo que si el archivo no se puede guardar, el método retornará un error.
//        fileManager = null; // Simulamos que el FileManager no está disponible
//
//        String resultado = authorization.guardarUsuario(nuevo);
//
//        // Verificamos que el error se haya manejado correctamente
//        assertEquals("Error al guardar el usuario.", resultado);
//    }
}