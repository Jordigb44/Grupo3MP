package model.usuario;
import java.util.List;
import model.personaje.*;
import model.desafio.*;
import ui.A_Interfaz; 

public class Jugador {
	private List<Personaje> personajes;
	private Desafio desafio;
	private int posicionRanking;
    private A_Interfaz interfaz;
	
	public List<Personaje> getPersonajes() {
		return personajes;
	}
	public void setPersonajes(List<Personaje> personajes) {
		this.personajes = personajes;
	}
	public Desafio getDesafio() {
		return desafio;
	}
	public void setDesafio(Desafio desafio) {
		this.desafio = desafio;
	}
	public int getPosicionRanking() {
		return posicionRanking;
	}
	public void setPosicionRanking(int posicionRanking) {
		this.posicionRanking = posicionRanking;
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

	
}
