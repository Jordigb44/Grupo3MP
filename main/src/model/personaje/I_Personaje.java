package model.personaje;

import java.util.List;

import model.personaje.habilidad.Arma;
import model.personaje.habilidad.Armadura;
import model.personaje.habilidad.Debilidad;
import model.personaje.habilidad.Fortaleza;

public interface I_Personaje {
    // Attributes
    String nombre = null;
    Integer salud = null;
    int oro = 0;
    List<Arma> armas = null;
    Arma armaActiva = null;
    List<Armadura> armaduras = null;
    Armadura armaduraActiva = null;
    List<Esbirro> esbirros = null;
    List<Fortaleza> fortalezas = null;
    List<Debilidad> debilidades = null;

    // Methods
    void equiparArma(Arma arma);
    void desequiparArma(Arma arma);
    List<Arma> getArmas();
    void equiparArmadura(Armadura armadura);
    void desequiparArmadura(Armadura armadura);
    List<Armadura> getArmaduras();
    void atacar(Personaje objetivo);
    void recibirDano(Integer cantidad);
    void agregarEsbirro(Esbirro esbirro);
    int getOro();
    void sumarOro(int oro);
    void restarOro(int oro);
}
