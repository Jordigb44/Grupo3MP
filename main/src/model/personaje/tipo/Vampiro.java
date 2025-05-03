package model.personaje.tipo;

import java.util.ArrayList;
import java.util.List;

import model.personaje.Personaje;
import model.personaje.habilidad.Disciplina;
import model.personaje.habilidad.Don;
import storage.FileManager;
import ui.A_Interfaz;

public class Vampiro extends Personaje {

	//ATRIBUTOS
	private int puntosSangre;
    private int edad;
    private List<Disciplina> disciplinas;
    private FileManager fileManager;
    private A_Interfaz interfaz;
	private String tipo;

    //CONSTRUCTOR
    public Vampiro(FileManager fileManager, A_Interfaz interfaz, Personaje personaje) {
    	super(personaje);
        this.fileManager = fileManager;
        this.tipo = "vampiro";
    	this.disciplinas = this.fileManager.getDisciplinasVampiro();
    	this.puntosSangre = this.fileManager.getPuntosdeSangreVampiro(); //Por defecto, tienen 10 puntos de sangre
    	this.edad = this.fileManager.getEdadVampiro();
	}

    //METODOS
    public Vampiro getVampiro() { //NO SE USA EN NINGUN LADO
    	return this;
    }
    
    protected void resetPuntosSangre() { //NO SE USA EN NINGUN LADO?
        this.puntosSangre = 10;
    }
    
    public String getTipo() {
    	return this.tipo;
    }
    /**
     * Maximo 10 puntos de sangre. Aumentan 4 puntos de sangre si el ataque tiene éxito.
     */
    public void aumentarPuntosSangre(int cantidad) {
        if (this.puntosSangre+cantidad>=10) {
        	this.puntosSangre = 10;
        }
    }

    public void perderPuntosSangre(int cantidad) {
        this.puntosSangre -= cantidad;
        if (this.puntosSangre < 0) {
            this.puntosSangre = 0;
        }
    }

    /**
     * Daño Vampiro: Su poder (atributo de Personaje) + el valor de ataque de su disciplina + 
     * el valor de ataque de su equipo activo + 2 si tiene un valor de sangre mayor o igual que 5.
     * Si el ataque tiene éxito, el vampiro recupera 4 puntos de sangre.
     */
    protected void usarDisciplina(Disciplina disciplina, Personaje objetivo) {
        if (disciplinas.contains(disciplina)) {
            if (this.puntosSangre >= disciplina.obtenerCostoSangre()) {
                disciplina.aplicarEfecto(this, objetivo);
                perderPuntosSangre(disciplina.obtenerCostoSangre());
            }
        }
    }

	public List<Disciplina> getDisciplinas() {
		return this.disciplinas;
	}
	
	public Disciplina getDisciplinaActiva() {
		//REVISAR
		return (Disciplina) disciplinas;
	}

	public int getPuntosSangre() {
		return this.puntosSangre;
	}

	public void setPuntosSangre(int i) {
		this.puntosSangre = i;
	}
}
