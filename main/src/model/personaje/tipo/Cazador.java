package model.personaje.tipo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.personaje.Personaje;
import model.personaje.habilidad.Don;
import model.personaje.habilidad.Talento;
import storage.FileManager;

public class Cazador extends Personaje {
	
	//ATRIBUTOS
	private int voluntad;
    private List<Talento> talentos;
    private FileManager fileManager;
    private Talento talentoActivo; //Para seleccionar el talentoActivo

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
    protected void usarTalento(Talento t, Personaje objetivo) { //NO LO USA NADIE?
        if (talentos.contains(t)) {
            restarVoluntad(t.getCostoVoluntad());
            t.aplicarEfecto(this, objetivo);
        }
    }

	public List<Talento> getTalentos() {
		return this.talentos;
	}
	
	/**
	 * Se esta pasando en Rondas un Talento que en teoria es el activo
	 * pero me da la sensacion que no se pregunta cual de ellos.
	 * 
	 * POSIBLE SOLUCION (tambien añadimos el atributo talentoActivo):
	 */
	public void setTalentoActivo() { //HECHO CON SCANNER POR SI SE QUIERE CAMBIAR
		//Mostramos todos los talentos
	    System.out.println("Selecciona un talento:");
	    for (int i = 0; i < talentos.size(); i++) {
	        Talento t = talentos.get(i);
	        System.out.println((i + 1) + ". " + t.getNombre() + " - Costo de voluntad: " + t.getCostoVoluntad());
	    }

	    Scanner scanner = new Scanner(System.in);
	    int opcion = -1;

	    while (opcion < 1 || opcion > talentos.size()) {
	        System.out.print("Introduce el número del talento que deseas activar: ");
	        if (scanner.hasNextInt()) {
	            opcion = scanner.nextInt();
	            if (opcion < 1 || opcion > talentos.size()) {
	                System.out.println("Opción inválida. Intenta nuevamente.");
	            }
	        } else {
	            System.out.println("Entrada no válida. Debes ingresar un número.");
	            scanner.next(); // Limpiar entrada inválida
	        }
	    }

	    this.talentoActivo = talentos.get(opcion - 1);
	    System.out.println("Talento activo establecido: " + talentoActivo.getNombre());
	}
	
	public Talento getTalentoActivo() { //ESTO FUNCIONA CORRECTAMENTE? (me refiero a lo que estaba antes)
		return this.talentoActivo; //Lo que ponia antes: return (Talento) talentos;
	}

	public int getVoluntad() {
		return this.voluntad;
	}
}