package model.personaje.habilidad;

public class Arma {
    private String nombre;
    private int ataque;
    private int manos;

    // Constructor
    public Arma(String nombre, int ataque, int manos) {
        this.nombre = nombre;
        this.ataque = ataque;
        this.manos = manos; //1 o 2
    }

    // Getters y Setters
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) { //NO SE USA EN NINGUN LADO
        this.nombre = nombre;
    }

    public int getAtaque() {
        return this.ataque;
    }

    public void setAtaque(int ataque) { //NO SE USA EN NINGUN LADO
        this.ataque = ataque;
    }

    public int getManos() {
        return this.manos;
    }

    public void setManos(int manos) { //NO SE USA EN NINGUN LADO
        this.manos = manos;
    }
}