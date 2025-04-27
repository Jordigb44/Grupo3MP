package model.personaje.tipo;

import java.util.ArrayList;
import java.util.List;

import model.personaje.Personaje;
import model.personaje.habilidad.Don;

public class Licantropo extends Personaje {
    public Licantropo(String nombre) {
		super(nombre);
		
	}

	private int rabia;
    private List<Don> dones;
    private float altura;
    private int peso;
    private String estado;

   

    protected void transformarBestia() {
        this.estado = "bestia";
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
}