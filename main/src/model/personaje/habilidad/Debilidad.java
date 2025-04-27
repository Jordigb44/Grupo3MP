package model.personaje.habilidad;

public class Debilidad {
    private String nombre;
    private int nivel;

    // Constructor
    public Debilidad(String nombre, int nivel) {
        this.nombre = nombre;
        this.nivel = nivel;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
}