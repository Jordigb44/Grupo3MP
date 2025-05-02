package model.personaje.habilidad;

import model.personaje.Personaje;
import model.personaje.tipo.Vampiro;

public class Disciplina {
	
	//ATRIBUTOS
    private String nombre;
    private int ataque;
    private int defensa;
    private int costoSangre;

    //CONSTRUCTOR
    public Disciplina(String nombre, int ataque, int defensa, int costoSangre) {
        this.nombre = nombre;
        this.ataque = ataque;
        this.defensa = defensa;
        this.costoSangre = costoSangre;
    }

    //METODOS
    public int obtenerCostoSangre() {
        return costoSangre;
    }

    public void aplicarEfecto(Vampiro usuario, Personaje objetivo) {
        objetivo.recibirDano(this.ataque);
    }

	public int getAtaque() {
		return this.ataque;
	}

	public int getDefensa() {
		return this.defensa;
	}

	public String getNombre() {
		return this.nombre;
	}
}
