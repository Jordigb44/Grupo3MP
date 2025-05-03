import model.Sistema;

public class App {
    public static void main(String[] args) {
    	Sistema sistema = null;
        try {
            // Crear una instancia de Sistema que iniciará todo el flujo de la aplicación
            sistema = new Sistema();
            
        } catch (Exception e) {
            System.err.println("Error al iniciar el sistema: " + e.getLocalizedMessage() + e.getMessage());
        } finally {
            // Cerrar el sistema al finalizar
        	if (sistema != null) {
        		sistema.cerrar();
        	}
        }   
    }
}

