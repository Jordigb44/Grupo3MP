package model.personaje.tipo;

import java.util.ArrayList;
import java.util.List;

import model.personaje.Personaje;
import model.personaje.habilidad.Talento;

public class Cazador extends Personaje {
    public Cazador(String nombre) {
		super(nombre);

	}

	private int voluntad;
    private List<Talento> talentos;

  

    protected void resetVoluntad() {
        this.voluntad = 100;
    }

    protected void restarVoluntad(int cantidad) {
        this.voluntad -= cantidad;
        if (this.voluntad < 0) {
            this.voluntad = 0;
        }
    }

    protected void usarTalento(Talento t, Personaje objetivo) {
        if (talentos.contains(t)) {
            restarVoluntad(t.getCostoVoluntad());
            t.aplicarEfecto(this, objetivo);
        }
    }
}