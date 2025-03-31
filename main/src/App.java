import model.Sistema;

public class App {
    public static void main(String[] args) {
        try {
            // Crear una instancia de Sistema que iniciará todo el flujo de la aplicación
            new Sistema();
            
        } catch (Exception e) {
            System.err.println("Error al iniciar el sistema: " + e.getMessage());
        } finally {
            // Cerrar el sistema al finalizar
            Sistema.cerrar();
        }
    }
}
