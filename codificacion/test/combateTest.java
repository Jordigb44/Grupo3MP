import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.desafio.Combate;
import model.desafio.Desafio;
import model.desafio.E_EstadoDesafio;
import model.desafio.Rondas;
import model.personaje.Personaje;
import model.usuario.Jugador;
import storage.FileManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.XMLStorage;
import ui.A_Interfaz;

class combateTest {

    private Combate combate;
    private Desafio desafio;
    private Jugador desafiante;
    private Jugador desafiado;
    private FileManager fileManager;

    @BeforeEach
    void setUp() {
        // Configuración inicial para cada test
        desafiante = new Jugador(UUID.randomUUID(), "Jugador1", "Nombre1", "pass1", "Jugador", "Activo", 100, 0, new ArrayList<>(), null);
        desafiado = new Jugador(UUID.randomUUID(), "Jugador2", "Nombre2", "pass2", "Jugador", "Activo", 100, 0, new ArrayList<>(), null);

        desafio = new Desafio();
        desafio.setDesafioId(UUID.randomUUID());
        desafio.setDesafiante(desafiante);
        desafio.setDesafiado(desafiado);
        desafio.setOroApostado(50);
        desafio.setEstado(E_EstadoDesafio.PENDIENTE);

        fileManager = new FileManager(new XMLStorage("./data"));

        combate = new Combate(desafio);
    }

    @Test
    void testConstructor() {
        assertNotNull(combate);
        assertEquals(desafio, combate.getDesafio());
        assertNotNull(combate.getRondas());
        assertTrue(combate.getRondas().isEmpty());
        assertNull(combate.getGanador());
    }

    @Test
    void testGetResultadoRondas() {
        // Configurar rondas de prueba
        Rondas ronda1 = new Rondas(desafiante, desafiado);
        Rondas ronda2 = new Rondas(desafiante, desafiado);

        combate.getRondas().add(ronda1);
        combate.getRondas().add(ronda2);

        // Ejecutar y verificar que no hay excepciones
        assertDoesNotThrow(() -> combate.getResultadoRondas());
    }

    @Test
    void testActualizarRanking() {
        // Configurar ganador
        combate.setGanador(desafiante);

        // Ejecutar
        combate.actualizarRanking();

        // Verificar que se actualizaron los puntos
        assertEquals(10, desafiante.getPuntos());
    }

    @Test
    void testSettersYGetters() {
        // Probar setters y getters adicionales
        combate.setGanador(desafiado);
        assertEquals(desafiado, combate.getGanador());

        List<Rondas> nuevasRondas = new ArrayList<>();
        combate.getRondas().addAll(nuevasRondas);
        assertEquals(nuevasRondas.size(), combate.getRondas().size());
    }
}

// Subclase de FileManager para testing
