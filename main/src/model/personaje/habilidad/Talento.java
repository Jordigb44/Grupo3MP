package model.personaje.habilidad;

import model.personaje.Personaje;
import model.personaje.tipo.Cazador;

public class Talento {
    private String nombre;
    private int ataque;
    private int defensa;
    private int costoVoluntad;

    public Talento(String nombre, int ataque, int defensa, int costoVoluntad) {
        this.nombre = nombre;
        this.ataque = ataque;
        this.defensa = defensa;
        this.costoVoluntad = costoVoluntad;
    }

    public int getCostoVoluntad() {
        return costoVoluntad;
    }

    public void aplicarEfecto(Cazador usuario, Personaje objetivo) {
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