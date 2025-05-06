package model.personaje.tipo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.personaje.Personaje;
import model.personaje.habilidad.Don;
import model.personaje.habilidad.Talento;
import storage.FileManager;
import ui.A_Interfaz;

public class Cazador extends Personaje {
	
	//ATRIBUTOS
	private int voluntad;
    private List<Talento> talentos;
    private FileManager fileManager;
    private A_Interfaz interfaz;
    private Talento talentoActivo; //Para seleccionar el talentoActivo
	private String tipo;

    //CONSTRUCTOR
    public Cazador(FileManager fileManager, A_Interfaz interfaz, Personaje personaje) {
		super(personaje);
		this.fileManager = fileManager;
		this.interfaz = interfaz;
		this.tipo = "cazador";
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
    protected void usarTalento(Talento t, Personaje objetivo) { //NO LO USA NADIE?
        if (talentos.contains(t)) {
            restarVoluntad(t.getCostoVoluntad());
            t.aplicarEfecto(this, objetivo);
        }
    }

	public List<Talento> getTalentos() {
		return this.talentos;
	}
	
	public String getTipo() {
		return this.tipo;
	}
	
	public void setTalentoActivo() { // Es usado por OPERADOR - Para editar un Desafio antes de aprobarlo. Lo setea a ese Desafio NO AL TIPO.
	    // Mostrar lista de talentos
	    this.interfaz.mostrar("Selecciona un talento:");
	    for (int i = 0; i < talentos.size(); i++) {
	        Talento t = talentos.get(i);
	        this.interfaz.mostrar((i + 1) + ". " + t.getNombre() + " - Costo de voluntad: " + t.getCostoVoluntad());
	    }

	    int opcion = -1;
	    while (opcion < 1 || opcion > talentos.size()) {
	        this.interfaz.mostrar("Introduce el número del talento que deseas activar:");
	        try {
	            opcion = Integer.parseInt(this.interfaz.pedirEntrada());
	            if (opcion < 1 || opcion > talentos.size()) {
	                this.interfaz.mostrar("Opción inválida. Intenta nuevamente.");
	            }
	        } catch (NumberFormatException e) {
	            this.interfaz.mostrar("Entrada no válida. Debes ingresar un número.");
	        }
	    }

	    this.talentoActivo = talentos.get(opcion - 1);
	}
	
	public Talento getTalentoActivo() { //ESTO FUNCIONA CORRECTAMENTE? (me refiero a lo que estaba antes)
		return this.talentoActivo; //Lo que ponia antes: return (Talento) talentos;
	}

	public int getVoluntad() {
		return this.voluntad;
	}
}