package model.personaje.tipo;

import java.util.ArrayList;
import java.util.List;

import model.personaje.Personaje;
import model.personaje.habilidad.Don;
import storage.FileManager;

public class Licantropo extends Personaje {

	private int rabia;
    private List<Don> dones;
    private float altura;
    private int peso;
    private String estado;
    private FileManager fileManager;

    public Licantropo(FileManager fileManager, Personaje personaje) {
		super(personaje);
		this.fileManager = fileManager;
    	this.dones = this.fileManager.getDonesLicantropo();
    	this.rabia = this.fileManager.getRabiaLicantropo();
    	this.peso = this.fileManager.getPesoLicantropo();
    	this.estado = "humano";
	}

    protected void transformarBestia() {
        if (this.estado == "humano") {
        	this.estado = "bestia";
        } else {
        	this.estado = "humano";
        }
    }

    protected void resetRabia() {
        this.rabia = 0;
    }

    protected void aumentarRabia(int cantidad) {
        this.rabia += cantidad;
    }

    protected void usarDon(Don don, Personaje objetivo) {
        if (dones.contains(don) && rabia >= don.getRabiaMinima()) {
            don.aplicarEfecto(this, objetivo);
        }
    }

	public Don getDonActivo() {
	
		return (Don) dones;
	}

	public int getRabia() {
		
		return rabia;
	}
}