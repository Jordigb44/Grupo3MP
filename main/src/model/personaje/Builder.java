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

public class Builder {
	
	//ATRIBUTOS
	private Personaje personaje;
	private FileManager fileManager;
	
	//CONSTRUCTOR
    public Builder(FileManager fileManager, String tipo, String nombre, List<Arma> armaActiva, Armadura armaduraActiva, List<Arma> armas, List<Armadura> armaduras, List<Esbirro> esbirros, List<Fortaleza> fortalezas, List<Debilidad> debilidades) {
        this.fileManager = fileManager;
    	this.personaje = new Personaje(nombre, armaActiva, armaduraActiva, armas, armaduras, esbirros, fortalezas, debilidades);
        
    	switch (tipo.toLowerCase()) {
        case "vampiro":
	            this.personaje = new Vampiro(this.fileManager, this.personaje);
	            break;
	        case "cazador":
	            this.personaje = new Cazador(this.fileManager, this.personaje);
	            break;
	        case "licantropo":
	            this.personaje = new Licantropo(this.fileManager, this.personaje);
	            break;
	        default:
	            throw new IllegalArgumentException("Tipo de personaje no reconocido: " + tipo);
	    }
    }
    
    public Personaje getPersonaje() {
        return this.personaje;
    }
    
}
