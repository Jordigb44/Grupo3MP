package model.personaje.habilidad;

public class Armadura {
    private String nombre;
    private int defensa;

    // Constructor
    public Armadura(String nombre, int defensa) {
        this.nombre = nombre;
        this.defensa = defensa;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) { //NO SE USA EN NINGUN LADO
        this.nombre = nombre;
    }

    public int getDefensa() {
        return defensa;
    }

    public void setDefensa(int defensa) { //NO SE USA EN NINGUN LADO
        this.defensa = defensa;
    }
}
