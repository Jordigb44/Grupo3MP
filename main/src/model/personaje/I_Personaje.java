package model.personaje;

import java.util.List;

import model.personaje.habilidad.Arma;
import model.personaje.habilidad.Armadura;
import model.personaje.habilidad.Debilidad;
import model.personaje.habilidad.Fortaleza;

public interface I_Personaje {
	// Métodos para gestión de armas
	void equiparArma(Arma arma);
    void desequiparArma(Arma arma);
    void eliminarArmasActivas();
    List<Arma> getArmas();
    List<Arma> getArmaActiva();

    // Métodos para gestión de armaduras
    void equiparArmadura(Armadura armadura);
    void desequiparArmadura(Armadura armadura);
    List<Armadura> getArmaduras();
    Armadura getArmaduraActiva();

    // Combate
    void atacar(Personaje objetivo);
    void recibirDano(int cantidad);

    // Esbirros
    void agregarEsbirro(Esbirro esbirro);
    List<Esbirro> getEsbirros();

    // Recursos
    int getOro();
    void sumarOro(int oro);
    void restarOro(int oro);

    // Atributos generales
    String getNombre();
    int getSalud();
    int getPoder();
    List<Fortaleza> getFortalezas();
    List<Debilidad> getDebilidades();
    int getAtaqueArmasActivas();

    // Otros
    char[] getId(); // Esto parece un placeholder, verifica si lo necesitas realmente
}
