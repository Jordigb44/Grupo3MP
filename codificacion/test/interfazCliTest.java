import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.InterfazCLI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class interfazCliTest {

    private final ByteArrayOutputStream salidaConsola = new ByteArrayOutputStream();

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(salidaConsola));
    }

    @Test
    public void testMostrar() {
        InterfazCLI interfaz = new InterfazCLI();
        salidaConsola.reset(); // Limpiar salida de constructor

        interfaz.mostrar("Hola Mundo");
        assertEquals("Hola Mundo\n", salidaConsola.toString());
    }

    @Test
    public void testPedirEntrada() {
        String inputSimulado = "entrada simulada\n";
        System.setIn(new ByteArrayInputStream(inputSimulado.getBytes()));

        InterfazCLI interfaz = new InterfazCLI();
        String entrada = interfaz.pedirEntrada();

        assertEquals("entrada simulada", entrada);
    }

    @Test
    public void testLimpiarPantalla() {
        InterfazCLI interfaz = new InterfazCLI();
        salidaConsola.reset(); // Limpiar salida de constructor

        interfaz.limpiarPantalla();
        String esperado = "[H\033[2J";
        String actual = salidaConsola.toString().trim();

        // Verifica que contiene la secuencia ANSI
        assertEquals(esperado, actual);
    }
}