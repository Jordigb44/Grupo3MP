package model.personaje;

import java.util.List;

import model.personaje.habilidad.Arma;
import model.personaje.habilidad.Armadura;
import model.personaje.habilidad.Debilidad;
import model.personaje.habilidad.Disciplina;
import model.personaje.habilidad.Fortaleza;
import model.personaje.tipo.Vampiro;
import storage.FileManager;
import ui.A_Interfaz;

public class Builder {
	private A_Interfaz interfaz; //QUITAR EN CASO DE NO USAR
	private Personaje personaje;
	private FileManager fileManager;
	
	//CONSTRUCTOR
    public Builder(FileManager fileManager, A_Interfaz interfaz, String tipo, String nombre, List<Arma> armaActiva, Armadura armaduraActiva, List<Arma> armas, List<Armadura> armaduras, List<Esbirro> esbirros, List<Fortaleza> fortalezas, List<Debilidad> debilidades) {
        this.fileManager = fileManager;
    	this.interfaz = interfaz;
    	this.personaje = new Personaje(nombre, armaActiva, armaduraActiva, armas, armaduras, esbirros, fortalezas, debilidades);
        
        switch (tipo) {
            case "vampiro":
                this.personaje = new Vampiro(this.fileManager, this.personaje);
            case "cazador":
            	this.personaje = new Cazador(this.fileManager, this.personaje);
            case "licantropo":
            	this.personaje = new Licantropo(this.fileManager, this.personaje);
        }
    }
    
}
