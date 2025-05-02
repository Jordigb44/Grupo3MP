package ui;

import java.util.Scanner;

public class InterfazCLI implements I_Interfaz {
    private Scanner scanner;

    public InterfazCLI() {
        System.out.println("InterfazCLI inicializada.");
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void mostrar(String contenido) {
        System.out.println(contenido);
    }

    @Override
    public String pedirEntrada() {
        System.out.print(">> ");
        return scanner.nextLine();
    }

    @Override
    public void limpiarPantalla() {
        System.out.print("\033[H\033[2J"); // Secuencia ANSI para limpiar pantalla
        System.out.flush();
    }
}