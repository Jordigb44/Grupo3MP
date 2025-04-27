package model.personaje;

import java.util.ArrayList;
import java.util.List;
import model.personaje.habilidad.Arma;
import model.personaje.habilidad.Armadura;
import model.personaje.habilidad.Debilidad;
import model.personaje.habilidad.Fortaleza;

	public class Personaje implements I_Personaje{
	    private String nombre;
	    private int salud;
	    private int oro;
	    private List<Arma> armas;
	    private Arma armaActiva;
	    private List<Armadura> armaduras;
	    private Armadura armaduraActiva;
	    private List<Esbirro> esbirros;
	    private List<Fortaleza> fortalezas;
	    private List<Debilidad> debilidades;

	    public Personaje(String nombre) {
	        this.nombre = nombre;
	        this.salud = 100;
	        this.oro = 0;
	        this.armas = new ArrayList<>();
	        this.armaduras = new ArrayList<>();
	        this.esbirros = new ArrayList<>();
	        this.fortalezas = new ArrayList<>();
	        this.debilidades = new ArrayList<>();
	    }

	    // Métodos de Armas
	    public void equiparArma(Arma arma) {
	        this.armaActiva = arma;
	    }

	    public void desequiparArma(Arma arma) {
	        if (this.armaActiva == arma) {
	            this.armaActiva = null;
	        }
	    }

	    public List<Arma> getArmas() {
	        return armas;
	    }

	    // Métodos de Armaduras
	    public void equiparArmadura(Armadura armadura) {
	        this.armaduraActiva = armadura;
	    }

	    public void desequiparArmadura(Armadura armadura) {
	        if (this.armaduraActiva == armadura) {
	            this.armaduraActiva = null;
	        }
	    }

	    public List<Armadura> getArmaduras() {
	        return armaduras;
	    }

	    // Combate
	    public void atacar(Personaje objetivo) {
	        if (this.armaActiva != null) {
	            int dano = armaActiva.getAtaque();
	            objetivo.recibirDano(dano);
	        }
	    }

	    public void recibirDano(int cantidad) {
	        int defensa = 0;
	        if (this.armaduraActiva != null) {
	            defensa = armaduraActiva.getDefensa(); 
	        }
	        int danoRecibido = Math.max(0, cantidad - defensa);
	        this.salud -= danoRecibido;
	        if (this.salud < 0) {
	            this.salud = 0;
	        }
	    }

	    // Esbirros
	    public void agregarEsbirro(Esbirro esbirro) {
	        this.esbirros.add(esbirro);
	    }

	    // Oro
	    public int getOro() {
	        return oro;
	    }

	    public void sumarOro(int oro) {
	        this.oro += oro;
	    }

	    public void restarOro(int oro) {
	        this.oro -= oro;
	        if (this.oro < 0) {
	            this.oro = 0;
	        }
	    }

	    // Getters adicionales si quieres
	    public String getNombre() {
	        return nombre;
	    }

	    public int getSalud() {
	        return salud;
	    }

		@Override
		public void recibirDano(Integer cantidad) {
			// TODO Auto-generated method stub
			
		}

		public char[] getId() {
			// TODO Auto-generated method stub
			return null;
		}

		public Arma getArmaActiva() {
			// TODO Auto-generated method stub
			return (Arma) armas;
		}
		public Armadura getArmaduraActiva() {
			return (Armadura) armaduras;
		}
	}
    

