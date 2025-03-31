package model;

import storage.FileManager;

public class Sistema {
    private static FileManager fileManager;

    // Método para inicializar el sistema
    public static void main() {
        if (fileManager == null) {
            fileManager = new FileManager();
            System.out.println("FileManager inicializado.");
        }
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
    }
}