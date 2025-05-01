package model.usuario;
import java.util.List;
import model.personaje.*;
import storage.FileManager;
import ui.A_Interfaz;
import model.desafio.*;


public class Jugador extends Usuario {
	private List<Personaje> personajes;
	private Desafio desafio;
	private int posicionRanking;
	private int oro;    
	
	private A_Interfaz interfaz;
	private FileManager fileManager;
    
	public Jugador(A_Interfaz interfaz, FileManager fileManager, String nick, String nombre, String password, String rol, String estado, List<Personaje> personajes, Desafio desafio, int posicionRanking, int oro) {
	    super(nick, nombre, password, rol, estado);
	    this.personajes = personajes;
	    this.desafio = desafio;
	    this.posicionRanking = posicionRanking;
	    this.oro = oro; //easteregg1
	    
	    this.interfaz = interfaz;
	    this.fileManager = fileManager;
	}
	
	public void getMenu() {
	    while (true) {
	        interfaz.mostrar("\n=== MENÚ DEL JUGADOR ===");
	        interfaz.mostrar("1. Manejar personajes");
	        interfaz.mostrar("2. Elegir tus armas o armaduras");
	        interfaz.mostrar("3. Desafiar a otros usuarios");
	        interfaz.mostrar("4. Historial de desafíos");
	        interfaz.mostrar("5. Consultar el ranking global");
	        interfaz.mostrar("6. Cerrar sesión");
	        interfaz.mostrar("7. Darse de baja");
	        interfaz.mostrar("=========================");
	        interfaz.mostrar("Seleccione una opción:");

	        String opcion = interfaz.pedirEntrada();

	        switch (opcion) {
	            case "1":
	            	interfaz.mostrar("Funcionalidad para manejar personajes.");
	                break;
	            case "2":
	                interfaz.mostrar("Funcionalidad de armas/armaduras aún no implementada.");
	                break;
	            case "3":
	                interfaz.mostrar("Funcionalidad para desafiar usuarios aún no implementada.");
	                break;
	            case "4":
	                interfaz.mostrar("Funcionalidad para historial de desafíos aún no implementada.");
	                break;
	            case "5":
	                interfaz.mostrar("Funcionalidad para mostrar ranking global aún no implementada.");
	                break;
	            case "6":
	                interfaz.mostrar("👋 Cerrando sesión...");
	                return;
	            case "7":
	                interfaz.mostrar("⚠️ ¿Estás seguro que quieres darte de baja? (s/n)");
	                String confirmacion = interfaz.pedirEntrada();
	                if (confirmacion.equalsIgnoreCase("s")) {
	                    interfaz.mostrar("✅ Usuario dado de baja exitosamente.");
	                    // Aquí podrías agregar lógica para eliminar al usuario del sistema
	                    this.fileManager.darDeBajaUsuario(nick);
	                    return;
	                }
	                break;
	            default:
	                interfaz.mostrar("❌ Opción no válida. Intenta de nuevo.");
	        }
	    }
	}
	
	public Jugador getJugador() {
	    return this;
	}
	
	public void borrarPersonaje(Personaje personaje) {
	    if (personaje != null) {
	    	this.personajes.remove(personaje);
	    	interfaz.mostrar("Personaje borrado exitosamente");
	    } else {
	    	this.interfaz.mostrar("No hay personajes disponibles que borrar"); ;
	    }
	}
	
	public int getOro() {
		return this.oro;
	}


	public void agregarPersonaje(Personaje personaje) {
	    // Verificar que el personaje no sea nulo
	    if (personaje == null) {
	    	this.interfaz.mostrar("Por favor introduzca, un personaje");;
	    }  else if (personajes.contains(personaje)) {
	    	this.interfaz.mostrar("No puede añadir un personaje duplicado");
	    } else {
	    	this.personajes.add(personaje);
	    } 
	}

	public void restarOro(int penalizacion) {
		// TODO Auto-generated method stub
		
	}

	public void agregarOro(int penalizacion) {
		// TODO Auto-generated method stub
		
	}

	public List<Personaje> getPersonajes() {

		return null;
	}

	public void sumarPuntos(int i) {
		posicionRanking += i;
		
	}

	public Personaje getPersonaje() {
		return (Personaje) personajes;
	}
		
}
