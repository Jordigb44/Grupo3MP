package model.personaje.tipo;

import java.util.ArrayList;
import java.util.List;

import model.personaje.Personaje;
import model.personaje.habilidad.Disciplina;

public class Vampiro extends Personaje {
    public Vampiro(String nombre) {
		super(nombre);
	}

	private int puntosSangre;
    private int edad;
    private List<Disciplina> disciplinas;

   

    protected void resetPuntosSangre() {
        this.puntosSangre = 100;
    }

    protected void aumentarPuntosSangre(int cantidad) {
        this.puntosSangre += cantidad;
    }

    protected void perderPuntosSangre(int cantidad) {
        this.puntosSangre -= cantidad;
        if (this.puntosSangre < 0) {
            this.puntosSangre = 0;
        }
    }

    protected void usarDisciplina(Disciplina disciplina, Personaje objetivo) {
        if (disciplinas.contains(disciplina)) {
            if (this.puntosSangre >= disciplina.obtenerCostoSangre()) {
                disciplina.aplicarEfecto(this, objetivo);
                perderPuntosSangre(disciplina.obtenerCostoSangre());
            }
        }
    }
}
