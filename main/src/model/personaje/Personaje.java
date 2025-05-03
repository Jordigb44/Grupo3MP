package model.personaje;

import java.util.List;
import java.util.UUID;
import model.personaje.habilidad.Arma;
import model.personaje.habilidad.Armadura;
import model.personaje.habilidad.Debilidad;
import model.personaje.habilidad.Fortaleza;
import model.personaje.habilidad.Talento;
import ui.A_Interfaz;

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
	    private Fortaleza fortalezaActiva;
	    private List<Debilidad> debilidades;
	    private Debilidad debilidadActiva;
	    private A_Interfaz interfaz; //PREGUNTAR JORDI
		

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
	    
	    public Armadura getArmaduraActiva() {
			return this.armaduraActiva;
		}

	    // METODOS (COMBATE) (REVISAR)
	    public void atacar(Personaje objetivo) { //NO SE USA EN NINGUN LADO
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
	    
	    // METODOS (ESBIRRO)
	    public void agregarEsbirro(Esbirro esbirro) {
	        this.esbirros.add(esbirro);
	    }
	    public int getPoder() {
			return Poder;
		}
	    public List<Esbirro> getEsbirros() {
	    	return this.esbirros;
	    }
	    
	    // METODOS (ORO)
	    public int getOro() {
	        return oro;
	    }

	    public void setOro(int oro) {
	    	this.oro = oro;
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
	    
	    //METODOS (FORTALEZAS Y DEBILIDADES)
	  	public List<Fortaleza> getFortalezas() {
	  		return this.fortalezas;
	  	}
	  		
	  	public List<Debilidad> getDebilidades() {
	  		return this.debilidades;
	  	}
	  	
	  	public void setFortalezaActiva() {
	  		// Mostrar lista de fortalezas
		    this.interfaz.mostrar("Selecciona una fortaleza:");
		    for (int i = 0; i < fortalezas.size(); i++) {
		        Fortaleza f = fortalezas.get(i);
		        this.interfaz.mostrar((i + 1) + ". " + f.getNombre() + "(nivel: " + f.getNivel() + ")");
		    }

		    // Seleccionar fortaleza
		    int opcion = -1;
		    while (opcion < 1 || opcion > fortalezas.size()) {
		        this.interfaz.mostrar("Introduce el número de la fortaleza que deseas activar:");
		        try {
		            opcion = Integer.parseInt(this.interfaz.pedirEntrada());
		            if (opcion < 1 || opcion > fortalezas.size()) {
		                this.interfaz.mostrar("Opción inválida. Intenta nuevamente.");
		            }
		        } catch (NumberFormatException e) {
		            this.interfaz.mostrar("Entrada no válida. Debes ingresar un número.");
		        }
		    }

		    this.fortalezaActiva = fortalezas.get(opcion - 1);
	  	}
	  	
	  	public void setDebilidadActiva() {
	  		// Mostrar lista de debilidades
		    this.interfaz.mostrar("Selecciona una debilidad:");
		    for (int i = 0; i < debilidades.size(); i++) {
		        Debilidad d = debilidades.get(i);
		        this.interfaz.mostrar((i + 1) + ". " + d.getNombre() + "(nivel: " + d.getNivel() + ")");
		    }

		    // Seleccionar debilidad
		    int opcion = -1;
		    while (opcion < 1 || opcion > debilidades.size()) {
		        this.interfaz.mostrar("Introduce el número de la debilidad que deseas activar:");
		        try {
		            opcion = Integer.parseInt(this.interfaz.pedirEntrada());
		            if (opcion < 1 || opcion > debilidades.size()) {
		                this.interfaz.mostrar("Opción inválida. Intenta nuevamente.");
		            }
		        } catch (NumberFormatException e) {
		            this.interfaz.mostrar("Entrada no válida. Debes ingresar un número.");
		        }
		    }

		    this.debilidadActiva = debilidades.get(opcion - 1);
	  	}
	  	
	  	public Fortaleza getFortalezaActiva() {
	  		return this.fortalezaActiva;
	  	}
	  	
	  	public Debilidad getDebilidadActiva() {
	  		return this.debilidadActiva;
	  	}

	    // GETTER ADICIONALES
		public UUID getUUID() {
			return this.uuid;
		}
		
		public void setUUID(UUID uuid) { //NO SE USA EN NINGUN LADO
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
	}