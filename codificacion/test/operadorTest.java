
import static org.junit.jupiter.api.Assertions.*;

import model.personaje.Personaje;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.desafio.Desafio;
import model.desafio.E_EstadoDesafio;
import model.usuario.Jugador;
import model.usuario.Operador;
import model.usuario.Usuario;
import storage.FileManager;
import storage.XMLStorage;
import ui.A_Interfaz;

/**
 * Tests unitarios para la clase Operador
 */
public class operadorTest {

    private FileManager fileManager;
    private A_Interfaz interfaz;
    private Operador operador;
    private Usuario usuarioBase;

    @BeforeEach
    public void setUp() {
        // Inicializar FileManager con XMLStorage real
        fileManager = new FileManager(new XMLStorage("./data"));

        // Inicializar la interfaz
        interfaz = new A_Interfaz();

        // Crear usuario base para el operador
        usuarioBase = new Usuario(
                UUID.randomUUID(),  // userId
                "admin",            // nick
                "Administrador",    // nombre
                "password123",      // password
                "operador",         // rol
                "activo",           // estado
                1000,               // oro
                500                 // puntos
        );

        // Inicializar el operador
        operador = new Operador(interfaz, fileManager, usuarioBase);
    }

    /**
     * Test para verificar la generación del menú de jugadores sin bloquear
     */
    @Test
    @DisplayName("Test generación del menú de jugadores sin bloquear")
    public void testGetMenuJugadoresSinBloquear() {
        // Acción: obtener el menú de jugadores sin bloquear
        String menu = operador.getMenuJugadoresSinBloquear();

        // Verificación: el menú no debe ser nulo y debe contener el título correcto
        assertNotNull(menu, "El menú no debe ser nulo");
        assertTrue(menu.contains("JUGADORES SIN BLOQUEAR"), "El menú debe contener el título correcto");
    }

    /**
     * Test para verificar la generación del menú de jugadores bloqueados
     */
    @Test
    @DisplayName("Test generación del menú de jugadores bloqueados")
    public void testGetMenuJugadoresBloqueados() {
        // Acción: obtener el menú de jugadores bloqueados
        String menu = operador.getMenuJugadoresBloqueados();

        // Verificación: el menú no debe ser nulo y debe contener el título correcto
        assertNotNull(menu, "El menú no debe ser nulo");
        assertTrue(menu.contains("JUGADORES BLOQUEADOS"), "El menú debe contener el título correcto");
    }

    /**
     * Test para verificar el bloqueo de un jugador
     */
    @Test
    @DisplayName("Test bloqueo de jugador")
    public void testBloquearJugador() {
        // Preparación: crear jugador para bloquear
        Jugador jugador = new Jugador(
                UUID.randomUUID(),
                "testPlayer",
                "Test Player",
                "pass123",
                "jugador",
                "activo",
                500,
                100,
                new ArrayList<>(),  // personajes vacío
                null                // desafío null
        );

        // Guardar el jugador en el sistema
        fileManager.guardarUsuario(jugador);

        // Acción: bloquear jugador
        String resultado = operador.bloquearJugador(jugador);

        // Verificación: el jugador debe quedar bloqueado
        assertEquals("Jugador testPlayer bloqueado correctamente.", resultado);
        assertEquals("bloqueado", jugador.getEstado());
    }

    /**
     * Test para verificar el desbloqueo de un jugador
     */
    @Test
    @DisplayName("Test desbloqueo de jugador")
    public void testDesbloquearJugador() {
        // Preparación: crear jugador bloqueado
        Jugador jugador = new Jugador(
                UUID.randomUUID(),
                "blockedPlayer",
                "Blocked Player",
                "pass123",
                "jugador",
                "bloqueado",
                500,
                100,
                new ArrayList<>(),  // personajes vacío
                null                // desafío null
        );

        // Guardar el jugador en el sistema
        fileManager.guardarUsuario(jugador);

        // Acción: desbloquear jugador
        String resultado = operador.desbloquearJugador(jugador);

        // Verificación: el jugador debe quedar activo
        assertEquals("Jugador blockedPlayer desbloqueado correctamente.", resultado);
        assertEquals("activo", jugador.getEstado());
    }


}