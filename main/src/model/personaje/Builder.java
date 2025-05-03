package model.personaje;

import java.util.List;

import model.personaje.habilidad.Arma;
import model.personaje.habilidad.Armadura;
import model.personaje.habilidad.Debilidad;
import model.personaje.habilidad.Fortaleza;
import model.personaje.tipo.Cazador;
import model.personaje.tipo.Licantropo;
import model.personaje.tipo.Vampiro;
import storage.FileManager;
import ui.A_Interfaz;

public class Builder {
	
	//ATRIBUTOS
	private Personaje personaje;
	private FileManager fileManager;
	private A_Interfaz interfaz;
	
	//CONSTRUCTOR
	public Builder(FileManager fileManager, A_Interfaz interfaz, String tipo, String nombre, List<Arma> armaActiva, Armadura armaduraActiva, List<Arma> armas, List<Armadura> armaduras, List<Esbirro> esbirros, List<Fortaleza> fortalezas, List<Debilidad> debilidades) {
	    this.fileManager = fileManager;
	    this.interfaz = interfaz;
	    this.personaje = new Personaje(nombre, armaActiva, armaduraActiva, armas, armaduras, esbirros, fortalezas, debilidades);

	    try {
	        switch (tipo.toLowerCase()) {
	            case "vampiro":
	            	personaje.setTipo("vampiro");
	                this.personaje = new Vampiro(this.fileManager, this.interfaz, this.personaje);
	                break;
	            case "cazador":
	            	personaje.setTipo("cazador");
	                this.personaje = new Cazador(this.fileManager, this.interfaz, this.personaje);
	                break;
	            case "licantropo":
	            	personaje.setTipo("licantropo");
	                this.personaje = new Licantropo(this.fileManager, this.interfaz, this.personaje);
	                break;
	            default:
	                throw new IllegalArgumentException("Tipo de personaje no reconocido: " + tipo);
	        }
	    } catch (Exception e) {
	        this.interfaz.mostrar("BUILDER ERROR - Error al construir personaje: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
    
    public Personaje getPersonaje() {
        return this.personaje;
    }
    
}
