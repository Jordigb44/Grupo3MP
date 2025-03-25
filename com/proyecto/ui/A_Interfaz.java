import java.util.Scanner;

public class A_Interfaz implements I_Interfaz {
    // Implementing the methods of I_Interfaz

    @Override
    public void mostrar(String contenido) {
        // Implementation of mostrar() with content as a parameter
        System.out.println(contenido);
    }

    @Override
    public String pedirEntrada() {
        // Implementation of pedirEntrada() to return user input
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine(); // Return the user input
    }

    @Override
    public void limpiarPantalla() {
        // Implementation of limpiarPantalla()
        System.out.println("\u000C"); // This is a common way to clear the console screen in Java.
    }
}