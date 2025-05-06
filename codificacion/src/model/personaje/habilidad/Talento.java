package model.personaje.habilidad;

import model.personaje.Personaje;
import model.personaje.tipo.Cazador;

public class Talento {
	
	//ATRIBUTOS
    private String nombre;
    private int ataque;
    private int defensa;
    private int costoVoluntad;

    //CONSTRUCTOR
    public Talento(String nombre, int ataque, int defensa, int costoVoluntad) {
        this.nombre = nombre;
        this.ataque = ataque;
        this.defensa = defensa;
        this.costoVoluntad = costoVoluntad;
    }
    
    //METODOS
    public int getCostoVoluntad() {
        return this.costoVoluntad;
    }

    public void aplicarEfecto(Cazador usuario, Personaje objetivo) { //?TECNICAMENTE NO HACE FALTA QUE NOS PASEMOS COMO PARAMETRO
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