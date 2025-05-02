package model.personaje;

import java.util.List;

import model.personaje.habilidad.Arma;
import model.personaje.habilidad.Armadura;
import model.personaje.habilidad.Debilidad;
import model.personaje.habilidad.Disciplina;
import model.personaje.habilidad.Fortaleza;
import model.personaje.tipo.Vampiro;
import ui.A_Interfaz;

public class Builder {
	private A_Interfaz interfaz;
	
	//CONSTRUCTOR
    public Builder(A_Interfaz interfaz, String tipo, String nombre, List<Arma> armaActiva, Armadura armaduraActiva, List<Arma> armas, List<Armadura> armaduras, List<Esbirro> esbirros, List<Fortaleza> fortalezas, List<Debilidad> debilidades) {
        this.interfaz = interfaz;
    	Personaje personaje = new Personaje(nombre, armaActiva, armaduraActiva, armas, armaduras, esbirros, fortalezas, debilidades);
        
        switch (tipo) {
            case "vampiro":
                this.interfaz.mostrar("Numero de puntos de sangre");
                int puntosDeSangre = Integer.parseInt(this.interfaz.pedirEntrada());
                this.interfaz.mostrar("Edad: ");
                int edad = Integer.parseInt(this.interfaz.pedirEntrada());
                
                return new Vampiro(personaje, puntosDeSangre, edad);
            case "cazador":
                return new Cazador();
            case "licantropo":
                return new Licantropo();
        }
    }
}
