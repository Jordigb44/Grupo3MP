package model;
import auth.PasarelaAuthorization; // Asegúrate de importar la clase correcta
import storage.FileManager;
import storage.XMLStorage;

public class Sistema {
    private static FileManager fileManager;
    private static PasarelaAuthorization pasarelaAuthorization;
    private String parentDir = "./";

    // Constructor de la clase Sistema
    public Sistema() {
        // Inicializa FileManager si no está ya inicializado
        if (fileManager == null) {
            fileManager = new FileManager(new XMLStorage(parentDir));
            System.out.println("FileManager inicializado.");
        }
        // Inicializa PasarelaAuthoritation si no está ya inicializado
        if (pasarelaAuthorization == null) {
            pasarelaAuthorization = new PasarelaAuthorization();
            System.out.println("PasarelaAuthoritation inicializada.");
        }
        // Llama al menú de sesión
        pasarelaAuthorization.menuSesion();
    }

    // Método para obtener la instancia de FileManager
    public static FileManager getFileManager() {
        return fileManager;
    }

    // Método para establecer la instancia de FileManager
    public static void setFileManager(FileManager fileManager) {
        Sistema.fileManager = fileManager;
    }

    // Método para cerrar el sistema
    public static void cerrar() {
        if (fileManager != null) {
            fileManager = null;
            System.out.println("FileManager cerrado.");
        }
        if (pasarelaAuthorization != null) {
        	pasarelaAuthorization = null;
            System.out.println("PasarelaAuthoritation cerrada.");
        }
    }
}