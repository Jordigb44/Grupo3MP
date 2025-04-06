package model.usuario;
import java.util.List;
import model.personaje.*;
import model.desafio.*;

public class Jugador {
	private List<Personaje> personajes;
	private boolean desafio;
	private int posicionRanking;
	
	public List<Personaje> getPersonajes() {
		return personajes;
	}
	public void setPersonajes(List<Personaje> personajes) {
		this.personajes = personajes;
	}
	public boolean getDesafio() {
		return desafio;
	}
	public void setDesafio(boolean b) {
		this.desafio = b;
	}
	public int getPosicionRanking() {
		return posicionRanking;
	}
	public void setPosicionRanking(int posicionRanking) {
		this.posicionRanking = posicionRanking;
	}
	public int getOro() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void restarOro(int penalizacion) {
		// TODO Auto-generated method stub
		
	}
	public void agregarOro(int penalizacion) {
		// TODO Auto-generated method stub
		
	}
	
	
}
