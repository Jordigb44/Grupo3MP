package model;
import java.util.ArrayList;
import java.util.List;

import auth.PasarelaAuthorization; // Asegúrate de importar la clase correcta
import model.desafio.Desafio;
import model.desafio.E_EstadoDesafio;
import model.usuario.Jugador;
import model.usuario.Operador;
import model.usuario.Usuario;
import notifications.NotificationInterna;
import storage.FileManager;
import storage.XMLStorage;
import ui.A_Interfaz;
// TODO: JORDI
// - Metodo de: cargarRanking(): List<Ranking> # Ordenador
// - Sistema crea y devuelve la interfaz, ya que ahora la crea la apsarela y eso esta mal
// - Hacer un getter y setter [cargarUsuarios] de usuarios, ya que lo necesita tanto Operador como Jugador

public class Sistema {
    private FileManager fileManager;
    private PasarelaAuthorization pasarelaAuthorization;
    private String parentDir = "./";
    private Usuario usuario;
    private List<Desafio> desafios;
    private A_Interfaz interfaz;
    private NotificationInterna notificationInterna;
    

    // Constructor de la clase Sistema
    public Sistema() {
        // Inicializa FileManager si no está ya inicializado
        if (this.fileManager == null) {
            this.fileManager = new FileManager(new XMLStorage(parentDir));
            System.out.println("FileManager inicializado.");
        }
        // Inicializa Interfaz si no está ya inicializado
        if (this.interfaz == null) {
            this.interfaz = new A_Interfaz();
            System.out.println("PasarelaAuthoritation inicializada.");
        }
        // Inicializa Interfaz si no está ya inicializado
        if (this.notificationInterna == null) {
        	this.notificationInterna = new NotificationInterna(this.fileManager);  // Usamos el patrón Singleton
        }
        
        // Inicializa PasarelaAuthoritation si no está ya inicializado
        if (this.pasarelaAuthorization == null) {
        	this.pasarelaAuthorization = new PasarelaAuthorization(this.fileManager, this.interfaz, this.notificationInterna);
        	System.out.println("PasarelaAuthoritation inicializada.");
        }

        // Llama al menú de sesión
        this.usuario = pasarelaAuthorization.menuSesion();
        
        switch (usuario.getTipo()) {
	        case "operador":
	        	Operador operador = new Operador(interfaz, fileManager, parentDir, parentDir, parentDir, null);
	        	operador.getMenu();
	            break;
	        case "jugador":
	        	Jugador jugador = new Jugador( // TODO: Jordi - Trabajar
	        			new A_Interfaz(),
	        		    this.fileManager,
	        		    this.usuario.getNick(),
	        		    this.usuario.getNombre(),
	        		    this.usuario.getPassword(),
	        		    this.usuario.getRol(),
	        		    this.usuario.getEstado(),
	        		    this.usuario.getOro(),
	        		    new ArrayList<>(), // TODO: personajes
	        		    null,              // TODO: desafío actual
	        		    0                 // TODO: ranking
	        		);
	        	
	        		jugador.getDesafioMenu();
	        		jugador.getMenu();
	        default:
	            System.out.println("⚠️ Tipo de usuario no reconocido.");
	            break;
	    }        
    }

    // Método para obtener la instancia de FileManager
    public FileManager getFileManager() {
        return this.fileManager;
    }
    
    public A_Interfaz getInterfaz() {
		return this.interfaz;
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
    public void cerrar() {
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