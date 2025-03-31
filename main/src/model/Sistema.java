package model;

import security.PasarelaAuthoritation; // Asegúrate de importar la clase correcta
import storage.FileManager;

public class Sistema {
    private static FileManager fileManager;
    private static PasarelaAuthoritation pasarelaAuthoritation;

    // Constructor de la clase Sistema
    public Sistema() {
        if (fileManager == null) {
            fileManager = new FileManager();
            System.out.println("FileManager inicializado.");
        }
        if (pasarelaAuthoritation == null) {
            pasarelaAuthoritation = new PasarelaAuthoritation();
            System.out.println("PasarelaAuthoritation inicializada.");
        }
        pasarelaAuthoritation.
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
        if (pasarelaAuthoritation != null) {
            pasarelaAuthoritation = null;
            System.out.println("PasarelaAuthoritation cerrada.");
        }
    }
}