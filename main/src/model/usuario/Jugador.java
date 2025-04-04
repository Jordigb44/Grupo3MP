package model.usuario;
import java.util.List;
import model.personaje.*;
import model.desafio.*;

public class Jugador {
	private List<Personaje> personajes;
	private Desafio desafio;
	private int posicionRanking;
	
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
	
	
}
