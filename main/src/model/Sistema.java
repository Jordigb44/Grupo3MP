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
// - Hacer un getter y setter [cargarUsuarios] de usuarios, ya que lo necesita tanto Operador como Jugador

public class Sistema {
    private FileManager fileManager;
    private PasarelaAuthorization pasarelaAuthorization;
    private String parentDir = "./";
    private Usuario usuario;
    private List<Usuario> usuarios;
    private List<Desafio> desafios;
    private A_Interfaz interfaz;
    private NotificationInterna notificationInterna;
    

    // Constructor de la clase Sistema
    public Sistema() {
        // Inicializa FileManager si no está ya inicializado
        if (this.fileManager == null) {
            this.fileManager = new FileManager(new XMLStorage(parentDir));
//            System.out.println("FileManager inicializado.");
        }
        // Inicializa Interfaz si no está ya inicializado
        if (this.interfaz == null) {
            this.interfaz = new A_Interfaz();
//            System.out.println("PasarelaAuthoritation inicializada.");
        }
        // Inicializa Interfaz si no está ya inicializado
        if (this.notificationInterna == null) {
        	this.notificationInterna = new NotificationInterna(this.fileManager);  // Usamos el patrón Singleton
        }
        
        // Inicializa PasarelaAuthoritation si no está ya inicializado
        if (this.pasarelaAuthorization == null) {
        	this.pasarelaAuthorization = new PasarelaAuthorization(this.fileManager, this.interfaz, this.notificationInterna);
//        	System.out.println("PasarelaAuthoritation inicializada.");
        }
        
        this.usuarios = this.fileManager.cargarUsuarios();

        // Llama al menú de sesión
        this.usuario = pasarelaAuthorization.menuSesion();
        
        if (this.usuario !=  null) {
	        switch (usuario.getTipo()) {
		        case "operador":
		        	Operador operador = new Operador(interfaz, fileManager, this.usuario);
		        	operador.getMenu();
		        	this.fileManager.guardarUsuario(operador); // guardamos copia actual del usuario
		            break;
		        case "jugador":
		        	int oro = this.usuario.getOro(); // NO BORRAR
		        	int puntos = this.usuario.getPuntos(); // NO BORRAR
		        	Jugador jugador = new Jugador(this.usuario.getUserId(), this.usuario.getNick(), this.usuario.getNombre(), this.usuario.getPassword(), this.usuario.getRol(), this.usuario.getEstado(), this.usuario.getOro(), this.usuario.getPuntos(), 
		        		    this.fileManager.cargarPersonajesUsuario(this.usuario.getNick()),
		        		    this.fileManager.cargarDesafioUsuario(this.usuario.getNick()));
		        	jugador.setOro(oro); // NO BORRAR
		        	jugador.setPuntos(puntos); // NO BORRAR
		        	this.interfaz.mostrar("->Jugador "+jugador.getNick()+" - "+jugador.getOro());
		        		jugador.setUsuarios(this.usuarios);
		        		jugador.setInterfaz(this.interfaz);
		        		jugador.setFileManger(this.fileManager);
		        	
		        		jugador.getDesafioMenu();
		        		jugador.getMenu();
		        		this.fileManager.guardarUsuario(jugador); // guardamos copia actual del usuario
		        default:
		            break;
		    }
        }
    	this.interfaz.mostrar("Cerrando aplicación...");
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
//            System.out.println("FileManager cerrado.");
        }
        if (pasarelaAuthorization != null) {
        	pasarelaAuthorization = null;
//            System.out.println("PasarelaAuthoritation cerrada.");
        }
    }
}