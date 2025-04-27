package model;
import java.util.ArrayList;
import java.util.List;

import auth.PasarelaAuthorization; // Asegúrate de importar la clase correcta
import model.desafio.Desafio;
import model.desafio.E_EstadoDesafio;
import model.usuario.Operador;
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
        if (this.fileManager == null) {
            this.fileManager = new FileManager(new XMLStorage(parentDir));
            System.out.println("FileManager inicializado.");
        }
        // Inicializa PasarelaAuthoritation si no está ya inicializado
        if (pasarelaAuthorization == null) {
            pasarelaAuthorization = new PasarelaAuthorization();
            System.out.println("PasarelaAuthoritation inicializada.");
        }
        // Llama al menú de sesión
        this.usuario = pasarelaAuthorization.menuSesion();
        
        switch (usuario.getTipo()) {
	        case "1":
//	            Operador.getMenu();
	            break;
	        case "2":
	            // Jugador.getMenu();
	            break;
	        default:
	            System.out.println("⚠️ Tipo de usuario no reconocido.");
	            break;
	    }        
    }

    // Método para obtener la instancia de FileManager
    public static FileManager getFileManager() {
        return fileManager;
    }
    
    public Usuario getUsuario() {
    	return this.usuario;
    }
    
    public List<Desafio> getDesafios(){
    	if (this.desafios == null) {
    		this.desafios = this.fileManager.cargarDesafios();
    	}
		return this.desafios;
    }
    
    public List<Desafio> getDesafiosUsuario(Usuario usuario) {
        List<Desafio> desafiosDelUsuario = new ArrayList<>();

        for (Desafio desafio : this.desafios) {
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
    
    public List<Usuario> getJugadores() {
    	return this.fileManager.cargarUsuarios();
    	
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