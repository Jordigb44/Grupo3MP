package model.personaje.tipo;

import java.util.ArrayList;
import java.util.List;

import model.personaje.Personaje;
import model.personaje.habilidad.Don;
import model.personaje.habilidad.Talento;
import storage.FileManager;

public class Cazador extends Personaje {
	
	//ATRIBUTOS
	private int voluntad;
    private List<Talento> talentos;
    private FileManager fileManager;

    //CONSTRUCTOR
    public Cazador(FileManager fileManager, Personaje personaje) {
		super(personaje);
		this.fileManager = fileManager;
    	this.talentos = this.fileManager.getTalentosCazador();
    	this.voluntad = this.fileManager.getVoluntadCazador(); //Empiezan partida con 3 y el valor esta entre 0 y 3
	}

    //METODOS
    protected void resetVoluntad() { //Empiezan el combate con voluntad 3
        this.voluntad = 3;
    }

    protected void restarVoluntad(int cantidad) { //Se resta voluntad en 1 cada vez que pierde salud
        this.voluntad -= cantidad;
        if (this.voluntad < 0) {
            this.voluntad = 0;
        }
    }

    /**
     * Daño Cazador:  Su poder (atributo de Personaje) + el valor de ataque de su Talento + el valor
     * de ataque de su equipo activo + su voluntad actual
     */
    protected void usarTalento(Talento t, Personaje objetivo) {
        if (talentos.contains(t)) {
            restarVoluntad(t.getCostoVoluntad());
            t.aplicarEfecto(this, objetivo);
        }
    }

	public List<Talento> getTalentos() {
		return this.talentos;
	}
	
	public Talento getTalentoActivo() {
		//REVISAR
		return (Talento) talentos;
	}

	public int getVoluntad() {
		return this.voluntad;
	}
	
}