package model.personaje.habilidad;

import model.personaje.Personaje;
import model.personaje.tipo.Vampiro;

public class Disciplina {
    private String nombre;
    private int ataque;
    private int defensa;
    private int costoSangre;

    public Disciplina(String nombre, int ataque, int defensa, int costoSangre) {
        this.nombre = nombre;
        this.ataque = ataque;
        this.defensa = defensa;
        this.costoSangre = costoSangre;
    }

    public int obtenerCostoSangre() {
        return costoSangre;
    }

    public void aplicarEfecto(Vampiro usuario, Personaje objetivo) {

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
