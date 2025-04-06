package model.usuario;
import java.util.List;
import model.personaje.*;
import storage.FileManager;
import model.desafio.*;


public class Jugador extends Usuario {
	private List<Personaje> personajes;
	private Desafio desafio;
	private int posicionRanking;
	private int oro;    
    
	public Jugador(List<Personaje> personajes, Desafio desafio, int posicionRanking, int oro) {
		super(instanceInterface, fileManager);
		this.personajes = personajes;
		this.desafio = desafio;
		this.posicionRanking = posicionRanking;
		this.oro = oro;
	}
	
	public Jugador getJugador() {
	    return this;
	}
	
	public void borrarPersonaje(Personaje personaje) {
	    if (personaje != null) {
	    	personajes.remove(personaje);
		    instanceInterface.mostrar("Personaje borrado exitosamente");
	    } else {
	    	instanceInterface.mostrar("No hay personajes disponibles que borrar"); ;
	    }
	}
	
	public int getOro() {
		return oro;
	}


	public void agregarPersonaje(Personaje personaje) {
	    // Verificar que el personaje no sea nulo
	    if (personaje == null) {
	    	instanceInterface.mostrar("Por favor introduzca, un personaje");;
	    }  else if (personajes.contains(personaje)) {
	    	instanceInterface.mostrar("No puede añadir un personaje duplicado");
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
	
}
