package test;

import model.usuario.Operador;
import storage.FileManager;
import storage.XMLStorage;
import ui.A_Interfaz;

/**
 * Clase para probar la funcionalidad básica del Operador.
 * Esta versión simplificada usa directamente las clases reales de tu proyecto.
 */
public class PruebaSimpleOperador {

    public static void main(String[] args) {
        System.out.println("Iniciando prueba simple del Operador...");
        
        try {
            // Crear los componentes necesarios
            A_Interfaz interfaz = new A_Interfaz();
            FileManager fileManager = new FileManager(new XMLStorage("./"));
            
            // Crear un operador para pruebas
            Operador operador = new Operador(interfaz, fileManager, "admin", "Administrador", "password123", 0);
            
            // Mostrar mensaje de inicio
            System.out.println("\nOperador creado correctamente.");
            System.out.println("A continuación se mostrará el menú del operador.");
            System.out.println("Puedes interactuar con él para probar sus funcionalidades.");
            System.out.println("\nPulsa Enter para comenzar...");
            new java.util.Scanner(System.in).nextLine();
            
            // Iniciar el menú principal del operador (interactivo)
            operador.getMenu();
            
            System.out.println("\nPrueba finalizada.");
            
        } catch (Exception e) {
            System.err.println("Error durante la prueba: " + e.getMessage());
            e.printStackTrace();
        }
    }
}