package model.personaje.habilidad;

import model.personaje.Personaje;
import model.personaje.tipo.Licantropo;

public class Don {
    private String nombre;
    private int ataque;
    private int defensa;
    private int rabiaMinima;

    public Don(String nombre, int ataque, int defensa, int rabiaMinima) {
        this.nombre = nombre;
        this.ataque = ataque;
        this.defensa = defensa;
        this.rabiaMinima = rabiaMinima;
    }
    
    public String getNombre() {
    	return this.nombre;
    }

    public int getRabiaMinima() {
        return this.rabiaMinima;
    }

    public void aplicarEfecto(Licantropo usuario, Personaje objetivo) {
        objetivo.recibirDano(this.ataque);
    }

	public int getAtaque() {
		return this.ataque;
	}

	public int getDefensa() {
		return this.defensa;
	}
}
