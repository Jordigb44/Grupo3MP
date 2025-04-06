package model;
import java.util.ArrayList;
import java.util.List;

import auth.PasarelaAuthorization; // Asegúrate de importar la clase correcta
import model.desafio.Desafio;
import model.desafio.E_EstadoDesafio;
import model.usuario.Usuario;
import storage.FileManager;
import storage.XMLStorage;

public class Sistema {
    private static FileManager fileManager;
    private static PasarelaAuthorization pasarelaAuthorization;
    private String parentDir = "./";
    private Usuario usuario;
    private List<Desafio> desafios;

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
        usuario = pasarelaAuthorization.menuSesion();
    }

    // Método para obtener la instancia de FileManager
    public static FileManager getFileManager() {
        return fileManager;
    }

    // Método para establecer la instancia de FileManager
    public static void setFileManager(FileManager fileManager) {
        Sistema.fileManager = fileManager;
    }
    
    public Usuario getUsuario() {
    	return usuario;
    }
    
    public List<Desafio> getDesafios(){
    	if (desafios == null) {
    		desafios = fileManager.cargarDesafios();
    	}
		return desafios;
    }
    
    public List<Desafio> getDesafiosUsuario(Usuario usuario) {
        List<Desafio> todosLosDesafios = this.getDesafios(); // o this.desafios si ya los tenés cargados en memoria
        List<Desafio> desafiosDelUsuario = new ArrayList<>();

        for (Desafio desafio : todosLosDesafios) {
            if (desafio.getDesafiante().getUserId().equals(usuario.getUserId()) ||
                desafio.getDesafiado().getUserId().equals(usuario.getUserId())) {
                desafiosDelUsuario.add(desafio);
            }
        }

        return desafiosDelUsuario;
    }
    
    public List<Desafio> getDesafiosPendientesUsuario() {
        List<Desafio> todosLosDesafios = this.getDesafios(); // o this.desafios si los tenés ya cargados
        List<Desafio> desafiosPendientes = new ArrayList<>();

        for (Desafio desafio : todosLosDesafios) {
            boolean estaPendiente = desafio.getEstado() == E_EstadoDesafio.PENDIENTE;
            if (estaPendiente) {
                desafiosPendientes.add(desafio);
            }
        }

        return desafiosPendientes;
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