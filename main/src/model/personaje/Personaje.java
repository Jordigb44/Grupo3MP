package model.personaje;

import java.util.List;
import java.util.UUID;
import model.personaje.habilidad.Arma;
import model.personaje.habilidad.Armadura;
import model.personaje.habilidad.Debilidad;
import model.personaje.habilidad.Fortaleza;

	public class Personaje implements I_Personaje{
		
		//ATRIBUTOS
		private UUID uuid; // Añadir esta declaración a la clase
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
		

	    //CONSTRUCTORES
		public Personaje(String nombre, List<Arma> armaActiva, Armadura armaduraActiva, List<Arma> armas, List<Armadura> armaduras, List<Esbirro> esbirros, List<Fortaleza> fortalezas, List<Debilidad> debilidades) {
			this.uuid = UUID.randomUUID(); // Generar un nuevo UUID
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
			this.uuid = personaje.getUUID(); // Obtener el UUID del personaje original
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

	    // METODOS (ARMAS)
		public String equiparArma(Arma arma) {
		    int manosOcupadas = 0;

		    for (Arma a : this.armaActiva) {
		        manosOcupadas += a.getManos();
		    }

		    if (manosOcupadas + arma.getManos() <= 2) {
		        this.armaActiva.add(arma);
		        return "Se agrego el arma.";
		    } else {
		        return "No se pudo agrego el arma.";
		    }
		}

	    public void desequiparArma(Arma arma) {
	        if (this.armaActiva == arma) {
	            this.armaActiva = null;
	        }
	    }

	    public List<Arma> getArmas() {
	        return armas;
	    }

	    // METODOS (ARMADURAS)
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

	    // Combate (REVISAR )
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
		public UUID getUUID() {
			return this.uuid;
		}
		
		public void setUUID(UUID uuid) {
			this.uuid = uuid;
		}
		
	    public String getNombre() {
	        return nombre;
	    }

	    public int getSalud() {
	        return salud;
	    }

		public char[] getId() {
			if (this.uuid == null) {
				return null;
			}
			return this.uuid.toString().toCharArray();
		}
		
		public List<Arma> getArmaActiva() { 
			return this.armaActiva;
		}
		
		public int getAtaqueArmasActivas() {
		    int ataqueTotal = 0;
		    
		    for (Arma arma : this.armaActiva) {
		        ataqueTotal += arma.getAtaque(); // Se asume que Arma tiene un método getAtaque()
		    }

		    return ataqueTotal;
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