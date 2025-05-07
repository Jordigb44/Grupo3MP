import model.personaje.*;
import model.personaje.habilidad.*;
import model.usuario.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PersonajeTest {

    private Personaje personaje;
    private List<Arma> armas;
    private List<Armadura> armaduras;
    private List<Esbirro> esbirros;
    private List<Fortaleza> fortalezas;
    private List<Debilidad> debilidades;

    private Arma espada;
    private Arma hacha;
    private Arma daga;
    private Armadura armaduraCuero;

    @BeforeEach
    void setUp() {
        // Inventario inicial
        armas = new ArrayList<>();
        armaduras = new ArrayList<>();
        esbirros = new ArrayList<>();
        fortalezas = new ArrayList<>();
        debilidades = new ArrayList<>();

        espada = new Arma("Espada", 15, 1);
        hacha = new Arma("Hacha", 25, 2);
        daga = new Arma("Daga", 10, 1);
        armaduraCuero = new Armadura("Cuero reforzado", 10);

        armas.add(new Arma("Espada", 20, 1));
        armaduras.add(new Armadura("Cota de malla", 10));
        esbirros.add(new Esbirro("humano", "Esbirro1", 100));
        fortalezas.add(new Fortaleza("Resistencia", 2));
        debilidades.add(new Debilidad("Luz solar", 1));

        personaje = new Personaje("Draco", new ArrayList<>(), null, armas, armaduras, esbirros, fortalezas, debilidades);
    }

    // --- TESTS BÁSICOS DE CREACIÓN Y ATRIBUTOS ---

    @Test
    void testCreacionPersonaje() {
        assertNotNull(personaje.getUUID());
        assertEquals("Draco", personaje.getNombre());
        assertEquals(100, personaje.getSalud());
        assertEquals(0, personaje.getOro());
        assertEquals(1, personaje.getArmas().size());
        assertEquals(1, personaje.getArmaduras().size());
        assertEquals(1, personaje.getEsbirros().size());
        assertEquals(1, personaje.getFortalezas().size());
        assertEquals(1, personaje.getDebilidades().size());
    }

    @Test
    void testAsociacionPersonajeAUsuario() {
        Usuario usuario = new Usuario(
                UUID.randomUUID(), "nickTest", "TestNombre", "pass123", "Jugador", "Activo", 100, 10
        );

        Map<Usuario, Personaje> mapaAsociacion = new HashMap<>();
        mapaAsociacion.put(usuario, personaje);

        assertTrue(mapaAsociacion.containsKey(usuario));
        assertEquals(personaje, mapaAsociacion.get(usuario));
        assertEquals("Draco", mapaAsociacion.get(usuario).getNombre());
    }

    @Test
    void testAtributosBasicos() {
        personaje.setOro(50);
        personaje.sumarOro(20);
        personaje.restarOro(10);

        assertEquals(60, personaje.getOro());
        assertEquals("personaje", personaje.getTipo());
    }

    // --- TESTS DE EQUIPAR/DESEQUIPAR ARMAS ---

    @Test
    void testEquiparArmaUnaMano() {
        personaje.equiparArma(espada);
        assertEquals(1, personaje.getArmaActiva().size());
        assertEquals("Espada", personaje.getArmaActiva().get(0).getNombre());
    }

    @Test
    void testEquiparDosArmasDeUnaMano() {
        personaje.equiparArma(espada);
        personaje.equiparArma(daga);
        assertEquals(2, personaje.getArmaActiva().size());
    }

    @Test
    void testNoPermitirMasDeDosManos() {
        personaje.equiparArma(hacha); // 2 manos
        personaje.equiparArma(daga);  // no debería añadirse
        assertEquals(1, personaje.getArmaActiva().size());
        assertEquals("Hacha", personaje.getArmaActiva().get(0).getNombre());
    }

    // --- TESTS DE ARMADURA ---

    @Test
    void testEquiparArmadura() {
        personaje.equiparArmadura(armaduraCuero);
        assertNotNull(personaje.getArmaduraActiva());
        assertEquals("Cuero reforzado", personaje.getArmaduraActiva().getNombre());
    }

    @Test
    void testDesequiparArmadura() {
        personaje.equiparArmadura(armaduraCuero);
        personaje.desequiparArmadura(armaduraCuero);
        assertNull(personaje.getArmaduraActiva());
    }
}