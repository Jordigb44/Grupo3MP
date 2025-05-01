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

    public int getRabiaMinima() {
        return rabiaMinima;
    }

    public void aplicarEfecto(Licantropo usuario, Personaje objetivo) {
        objetivo.recibirDano(this.ataque);
    }

	public int getAtaque() {
		// TODO Auto-generated method stub
		return ataque;
	}

	public int getDefensa() {
		// TODO Auto-generated method stub
		return defensa;
	}
}
