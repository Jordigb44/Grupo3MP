package model.personaje.tipo;

import java.util.ArrayList;
import java.util.List;

import model.personaje.Personaje;
import model.personaje.habilidad.Disciplina;
import storage.FileManager;

public class Vampiro extends Personaje {

	private int puntosSangre;
    private int edad;
    private List<Disciplina> disciplinas;
    private FileManager fileManager;

    //CONSTRUCTOR
    public Vampiro(FileManager fileManager, Personaje personaje) {
    	super(personaje);
        this.fileManager = fileManager;
    	this.disciplinas = this.fileManager.getDisciplinasVampiro();
    	this.puntosSangre = this.fileManager.getPuntosdeSangreVampiro();
    	this.edad = this.fileManager.getEdadVampiro();
	}

    //METODOS
    public Vampiro getVampiro() {
    	return this;
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

	public List<Disciplina> getDisciplinas() { //TODO
		return this.disciplinas;
	}

	public int getPuntosSangre() {
		return this.puntosSangre;
	}

	public void setPuntosSangre(int i) {
		this.puntosSangre = i;
	}
}
