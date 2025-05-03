package model.personaje.tipo;

import java.util.ArrayList;
import java.util.List;

import model.personaje.Personaje;
import model.personaje.habilidad.Don;
import model.personaje.habilidad.Talento;
import storage.FileManager;

public class Licantropo extends Personaje {

	//ATRIBUTOS
	private int rabia;
    private List<Don> dones;
    private float altura; //En metros
    private int peso; //En kg
    private String estado;
    private FileManager fileManager;

    //CONSTRUCTOR
    public Licantropo(FileManager fileManager, Personaje personaje) {
		super(personaje);
		this.fileManager = fileManager;
    	this.dones = this.fileManager.getDonesLicantropo();
    	this.rabia = this.fileManager.getRabiaLicantropo(); //Empiezan con 0 y se suma cuando pierde salud
    	this.peso = this.fileManager.getPesoLicantropo();
    	this.estado = "humano"; //
	}
    
    //METODOS
    /**
     * Sumamos entre 0.5 y 1 metros de altura y entre 90 y 110kg al transformar a bestia
     * Restamos entre 0.5 y 1 metros de altura y entre 90 y 110kg al transformar a humano
     */
    protected void transformarBestia() {
        if (this.estado == "humano") {
        	this.estado = "bestia";
        	this.altura += Math.round((0.5 + Math.random() * 0.5) * 10) / 10.0f;
        	this.peso += (int)(Math.random() * 21) + 90;
        } else {
        	this.estado = "humano";
        	this.altura -= Math.round((0.5 + Math.random() * 0.5) * 10) / 10.0f;
        	this.peso -= (int)(Math.random() * 21) + 90;
        }
    }

    protected void resetRabia() { //Empiezan el combate con rabia 0
        this.rabia = 0;
    }

    protected void aumentarRabia(int cantidad) {
        this.rabia += cantidad;
    }

    protected void usarDon(Don don, Personaje objetivo) { //Tiene que ser superior al valor de rabia minimo
        if (dones.contains(don) && rabia >= don.getRabiaMinima()) {
            don.aplicarEfecto(this, objetivo);
        }
    }
    
    public List<Don> getDones() {
		return this.dones;
	}

	public Don getDonActivo() {
		//REVISAR
		return (Don) dones;
	}

	public int getRabia() {
		return this.rabia;
	}
}