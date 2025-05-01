package model.personaje.tipo;

import java.util.ArrayList;
import java.util.List;

import model.personaje.Personaje;
import model.personaje.habilidad.Disciplina;

public class Vampiro extends Personaje {

	private int puntosSangre;
    private int edad;
    private List<Disciplina> disciplinas;

    public Vampiro(String nombre) {
		super(nombre);
	}

    protected void resetPuntosSangre() {
        this.puntosSangre = 100;
    }

    public void aumentarPuntosSangre(int cantidad) {
        this.puntosSangre += cantidad;
    }

    public void perderPuntosSangre(int cantidad) {
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

	public Disciplina getDisciplinaActiva() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getPuntosSangre() {
		// TODO Auto-generated method stub
		return puntosSangre;
	}

	public void setPuntosSangre(int i) {
		
		this.puntosSangre = i;
		
	}
}
