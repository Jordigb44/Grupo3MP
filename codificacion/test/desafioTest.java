import static org.junit.jupiter.api.Assertions.*;

import model.desafio.Desafio;
import model.desafio.E_EstadoDesafio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import model.usuario.Jugador;
import storage.FileManager;
import ui.A_Interfaz;

class DesafioTest {

    private Desafio desafio;
    private Jugador desafiante;
    private Jugador desafiado;
    private FileManager fileManager;
    private A_Interfaz interfaz;

    @BeforeEach
    void setUp() {
        // Configuración inicial para cada test
        desafiante = new Jugador(UUID.randomUUID(), "Jugador1", "Nombre1", "pass1",
                "Jugador", "Activo", 100, 0, null, null);
        desafiado = new Jugador(UUID.randomUUID(), "Jugador2", "Nombre2", "pass2",
                "Jugador", "Activo", 100, 0, null, null);

        fileManager = new FileManagerTest(); // Usamos una implementación de prueba
        interfaz = new InterfazTest(); // Implementación simple para testing

        desafio = new Desafio();
        desafio.setFileManager(fileManager);
    }

    @Test
    void testConstructorVacio() {
        assertNotNull(desafio);
        assertNull(desafio.getDesafioId());
        assertNull(desafio.getDesafiante());
        assertNull(desafio.getDesafiado());
        assertEquals(0, desafio.getOroApostado());
        assertNull(desafio.getEstado());
    }


    @Test
    void testDesafiarOroInvalido() {
        // Oro negativo
        String resultado1 = desafio.Desafiar(desafiante, desafiado, -10);
        assertEquals("No se pudo crear desafio", resultado1);

        // Oro mayor que el disponible
        String resultado2 = desafio.Desafiar(desafiante, desafiado, 150);
        assertEquals("No se pudo crear desafio", resultado2);
    }




    @Test
    void testSettersYGetters() {
        UUID id = UUID.randomUUID();
        LocalDateTime fecha = LocalDateTime.now();

        desafio.setDesafioId(id);
        desafio.setDesafiante(desafiante);
        desafio.setDesafiado(desafiado);
        desafio.setOroApostado(75);
        desafio.setEstado(E_EstadoDesafio.ACEPTADO);
        desafio.setFechaDesafio(fecha);

        assertEquals(id, desafio.getDesafioId());
        assertEquals(desafiante, desafio.getDesafiante());
        assertEquals(desafiado, desafio.getDesafiado());
        assertEquals(75, desafio.getOroApostado());
        assertEquals(E_EstadoDesafio.ACEPTADO, desafio.getEstado());
        assertEquals(fecha, desafio.getFecha());
    }

}

// Implementación de FileManager para testing
class FileManagerTest extends FileManager {
    private Desafio desafioACargar;

    public void setDesafioACargar(Desafio desafio) {
        this.desafioACargar = desafio;
    }

    @Override
    public Desafio cargarDesafio(UUID desafioId) {
        return desafioACargar;
    }

    @Override
    public String guardarDesafio(Desafio desafio) {
        // Simplemente no hacer nada para testing
        return null;
    }

    @Override
    public String actualizarEstadoDesafio(UUID desafioId, E_EstadoDesafio estado) {
        // Simplemente no hacer nada para testing
        return null;
    }
}

// Implementación simple de interfaz para testing
class InterfazTest extends A_Interfaz {
    @Override
    public void mostrar(String mensaje) {
        System.out.println(mensaje);
    }

    @Override
    public String pedirEntrada() {
        return "";
    }
}