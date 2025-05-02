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
	    private List<Arma> armaActiva;
	    private int Poder;
	    private List<Armadura> armaduras;
	    private Armadura armaduraActiva;
	    private List<Esbirro> esbirros;
	    private List<Fortaleza> fortalezas;
	    private List<Debilidad> debilidades;

	    public Personaje(String nombre, List<Arma> armaActiva, Armadura armaduraActiva, List<Arma> armas, List<Armadura> armaduras, List<Esbirro> esbirros, List<Fortaleza> fortalezas, List<Debilidad> debilidades) {
	        this.nombre = nombre;
	        this.salud = 100;
	        this.oro = 0;
	        this.armas = armas;
	        this.armaduras = armaduras;
	        this.esbirros = esbirros;
	        this.fortalezas = fortalezas;
	        this.debilidades = debilidades;
	    }
	    
	    public Personaje(Personaje personaje) {
	        this.nombre = personaje.getNombre();
	        this.salud = personaje.getSalud();
	        this.oro = personaje.getOro();
	        this.armaActiva = personaje.getArmaActiva();
	        this.armaduraActiva = personaje.getArmaduraActiva();
	        this.armas = personaje.getArmas();
	        this.armaduras = personaje.getArmaduras();
	        this.esbirros = personaje.getEsbirros();
	        this.fortalezas = personaje.getFortalezas();
	        this.debilidades = personaje.getDebilidades();
	    }

	    // Métodos de Armas
	    public void equiparArma(List<Arma> arma) {
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
	    	if (armaActiva != null && !armaActiva.isEmpty()) {
	            for (Arma arma : armaActiva) {
	                int dano = arma.getAtaque();
	                objetivo.recibirDano(dano);
	            }
	        }
	    }

	    public void recibirDano(int cantidad) {
	        int defensa = 0;
	        if (this.armaduraActiva != null) {
	            defensa = armaduraActiva.getDefensa(); 
	        }

	        int danoFinal = Math.max(0, cantidad - defensa);

	        // Aplicar daño a esbirros primero
	        for (Esbirro esbirro : esbirros) {
	            if (danoFinal <= 0) break;

	            int saludEsbirro = esbirro.getSalud();
	            if (saludEsbirro > 0) {
	                int danoAEsbirro = Math.min(danoFinal, saludEsbirro);
	                esbirro.setSalud(saludEsbirro - danoAEsbirro);
	                danoFinal -= danoAEsbirro;
	            }
	        }

	        // Si sobra daño después de esbirros, aplicarlo al personaje
	        if (danoFinal > 0) {
	            this.salud -= danoFinal;
	            if (this.salud < 0) {
	                this.salud = 0;
	            }
	        }
	    }
	    // Esbirros
	    public void agregarEsbirro(Esbirro esbirro) {
	        this.esbirros.add(esbirro);
	    }
	    public int getPoder() {
			return Poder;
		}
	    public List<Esbirro> getEsbirros() {
	    	return this.esbirros;
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

		public List<Arma> getArmaActiva() { 
			return this.armaActiva;
		}
		public Armadura getArmaduraActiva() {
			return this.getArmaduraActiva();
		}
		public List<Fortaleza> getFortalezas() {
			return this.fortalezas;
		}
		public List<Debilidad> getDebilidades() {
			return this.debilidades;
		}
		
	}