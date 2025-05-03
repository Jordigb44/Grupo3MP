package model.personaje.habilidad;

public class Debilidad {
	
	//ATRIBUTOS
    private String nombre;
    private int nivel; //Este es el nivel de debilidad (va del 1 al 5 en funcion de lo sesible que es)

    //CONSTRUCTOR
    public Debilidad(String nombre, int nivel) {
        this.nombre = nombre;
        this.nivel = nivel;
    }

    //GETTER y SETTER
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) { //NO SE USA EN NINGUN LADO (YA QUE SE PASAN LAS DEBILIDADES QUE HAY DECLARADAS)
        this.nombre = nombre;
    }

    public int getNivel() { //NO SE USA EN NINGUN LADO?
    	//TODO
        return this.nivel;
    }

    public void setNivel(int nivel) { //NO SE USA EN NINGUN LADO
        this.nivel = nivel;
    }
}