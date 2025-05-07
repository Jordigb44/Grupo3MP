import static org.junit.jupiter.api.Assertions.*;

import model.personaje.habilidad.Armadura;
import model.usuario.Jugador;
import model.personaje.Personaje;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import storage.FileManager;
import storage.XMLStorage;
import ui.A_Interfaz;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class jugadorTest {

    private Jugador jugador;
    private FileManager fileManager;
    private A_Interfaz interfaz;

    private Personaje personaje;
    private Armadura armadura1;
    private Armadura armadura2;

    @BeforeEach
    public void setUp() {
        fileManager = new FileManager(new XMLStorage("./data"));
        interfaz = new A_Interfaz();

        // Crear armaduras
        armadura1 = new Armadura("Armadura Ligera", 10);
        armadura2 = new Armadura("Armadura Pesada", 20);

        // Crear personaje con armaduras
        List<Armadura> armaduras = new ArrayList<>();
        armaduras.add(armadura1);
        armaduras.add(armadura2);

        personaje = new Personaje(
                "PersonajeTest",
                new ArrayList<>(), // armaActiva
                null,              // armaduraActiva
                new ArrayList<>(), // armas
                armaduras,         // armaduras
                new ArrayList<>(), // esbirros
                new ArrayList<>(), // fortalezas
                new ArrayList<>()  // debilidades
        );

        personaje = new Personaje(
                "PersonajeTest",
                new ArrayList<>(), // armaActiva
                null,              // armaduraActiva
                new ArrayList<>(), // armas
                new ArrayList<>(), // armaduras
                new ArrayList<>(), // esbirros
                new ArrayList<>(), // fortalezas
                new ArrayList<>()  // debilidades
        );


        jugador = new Jugador(
                UUID.randomUUID(),
                "jugador1",
                "Jugador Uno",
                "pass",
                "jugador",
                "activo",
                0,
                0,
                new ArrayList<>(),
                null
        );
        jugador.setFileManger(fileManager);
        jugador.setInterfaz(interfaz);
    }

    @Test
    @DisplayName("Test equipar una armadura al personaje")
    public void testEquiparArmadura() {
        // Acción
        personaje.equiparArmadura(armadura2);

        // Verificación
        assertNotNull(personaje.getArmaduraActiva(), "Debe haber una armadura equipada");
        assertEquals("Armadura Pesada", personaje.getArmaduraActiva().getNombre(), "La armadura activa debe ser la esperada");
        assertEquals(20, personaje.getArmaduraActiva().getDefensa(), "La defensa debe coincidir con la armadura equipada");
    }

    @Test
    @DisplayName("Test sumar puntos al jugador")
    public void testSumarPuntos() {
        jugador.sumarPuntos(50);
        assertEquals(50, jugador.getPuntos(), "Los puntos deben aumentar correctamente");
    }

    @Test
    @DisplayName("Test sumar oro al jugador")
    public void testSumarOro() {
        jugador.sumarOro(300);
        assertEquals(300, jugador.getOro(), "El oro debe aumentar correctamente");
    }

    @Test
    @DisplayName("Test restar oro dejando oro en cero")
    public void testRestarOro_Excesivo() {
        jugador.restarOro(1000, interfaz);
        assertEquals(0, jugador.getOro(), "El oro no debe ser negativo");
    }

    @Test
    @DisplayName("Test agregar personaje a la lista")
    public void testAgregarPersonaje() {
        List<Personaje> lista = new ArrayList<>();
        jugador.setPersonajes(lista);

        lista.add(personaje);
        jugador.setPersonajes(lista);

        assertEquals(1, jugador.getPersonajes().size(), "Debe tener un personaje agregado");
    }

    @Test
    @DisplayName("Test borrar personaje existente")
    public void testBorrarPersonaje() {
        List<Personaje> lista = new ArrayList<>();
        lista.add(personaje);
        jugador.setPersonajes(lista);

        jugador.borrarPersonaje(personaje);

        assertTrue(jugador.getPersonajes().isEmpty(), "La lista debe quedar vacía tras borrar");
    }

}
