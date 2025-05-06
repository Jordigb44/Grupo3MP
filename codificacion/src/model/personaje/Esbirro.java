package model.personaje;

public class Esbirro {
    private String tipo;
    private String nombre;
    private int salud;

    // Constructor
    public Esbirro(String tipo, String nombre, int salud) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.salud = salud;
    }

    // Getters y Setters
    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getSalud() {
        return this.salud;
    }

    public void setSalud(int salud) {
        this.salud = salud;
    }
}
