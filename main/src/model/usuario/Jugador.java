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
	    this.oro = oro;
	    
	    this.interfaz = interfaz;
	    this.fileManager = fileManager;
	}
	
	public Jugador getJugador() {
	    return this;
	}
	
	public void borrarPersonaje(Personaje personaje) {
	    if (personaje != null) {
	    	personajes.remove(personaje);
	    	interfaz.mostrar("Personaje borrado exitosamente");
	    } else {
	    	interfaz.mostrar("No hay personajes disponibles que borrar"); ;
	    }
	}
	
	public int getOro() {
		return oro;
	}


	public void agregarPersonaje(Personaje personaje) {
	    // Verificar que el personaje no sea nulo
	    if (personaje == null) {
	    	interfaz.mostrar("Por favor introduzca, un personaje");;
	    }  else if (personajes.contains(personaje)) {
	    	interfaz.mostrar("No puede añadir un personaje duplicado");
	    } else {
	    	personajes.add(personaje);
	    } 
	}

	public void restarOro(int penalizacion) {
		// TODO Auto-generated method stub
		
	}

	public void agregarOro(int penalizacion) {
		// TODO Auto-generated method stub
		
	}

	public List<Personaje> getPersonajes() {
		// TODO Auto-generated method stub
		return null;
	}

	public void sumarPuntos(int i) {
		posicionRanking += i;
		
	}

	public Personaje getPersonaje() {
		// TODO Auto-generated method stub
		return (Personaje) personajes;
	}

	
}
