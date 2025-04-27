package model.personaje.habilidad;

public class Arma {
    private String nombre;
    private int ataque;
    private int manos;

    // Constructor
    public Arma(String nombre, int ataque, int manos) {
        this.nombre = nombre;
        this.ataque = ataque;
        this.manos = manos;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getManos() {
        return manos;
    }

    public void setManos(int manos) {
        this.manos = manos;
    }

	public int getDano() {
		// TODO Auto-generated method stub
		return 0;
	}
}