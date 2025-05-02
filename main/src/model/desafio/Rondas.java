package model.desafio;

import java.util.Random;

import model.personaje.Personaje;
import model.personaje.habilidad.Disciplina;
import model.personaje.habilidad.Don;
import model.personaje.habilidad.Talento;
import model.personaje.tipo.Cazador;
import model.personaje.tipo.Licantropo;
import model.personaje.tipo.Vampiro;
import model.usuario.Jugador;

public class Rondas {
	private Jugador Desafiante;
	private Jugador Desafiando;
	private String Resultado;

	private Random random = new Random();
	
	public Rondas(Jugador desafiante, Jugador desafiando) {
        this.Desafiante = desafiante;
        this.Desafiando = desafiando;
    }
	
	 public void iniciarRonda() {
	        Personaje p1 = Desafiante.getPersonaje();
	        Personaje p2 = Desafiando.getPersonaje();

	        int ataqueP1 = calcularPotencialAtaque(p1);
	        int ataqueP2 = calcularPotencialAtaque(p2);

	        int defensaP1 = calcularPotencialDefensa(p1);
	        int defensaP2 = calcularPotencialDefensa(p2);

	        int exitosAtaqueP1 = contarExitos(ataqueP1);
	        int exitosAtaqueP2 = contarExitos(ataqueP2);

	        int exitosDefensaP1 = contarExitos(defensaP1);
	        int exitosDefensaP2 = contarExitos(defensaP2);

	        // Ataque del desafiante al desafiando
	        int dañoP1 = Math.max(0, exitosAtaqueP1 - exitosDefensaP2);
	        p2.recibirDano(dañoP1);

	        // Ataque del desafiando al desafiante
	        int dañoP2 = Math.max(0, exitosAtaqueP2 - exitosDefensaP1);
	        p1.recibirDano(dañoP2);

	        if (p1.getSalud() <= 0 && p2.getSalud() <= 0) {
	            Resultado = "Empate";
	        } else if (p1.getSalud() <= 0) {
	            Resultado = Desafiando.getNombre() + " gana";
	        } else if (p2.getSalud() <= 0) {
	            Resultado = Desafiante.getNombre() + " gana";
	        } else {
	            Resultado = "La batalla continúa";
	        }
	    }

	    private int contarExitos(int cantidadDados) {
	        int exitos = 0;
	        for (int i = 0; i < cantidadDados; i++) {
	            int dado = random.nextInt(6) + 1;
	            if (dado >= 5) {
	                exitos++;
	            }
	        }
	        return exitos;
	    }

	    private int calcularPotencialAtaque(Personaje personaje) {
	        int poderBase = personaje.getPoder();
	        //TODO (Revisar ya que Arma activa es un List no un arma solamente, posible solucion con preguntar size del List?)
	        int equipoAtaque = personaje.getArmaActiva() != null ? ((Disciplina) personaje.getArmaActiva()).getAtaque() : 0;
	        int modificador = calcularModificadorFortalezaDebilidad(personaje);

	        if (personaje instanceof Vampiro) {
	            Vampiro vampiro = (Vampiro) personaje;
	            Disciplina disciplina = vampiro.getDisciplinaActiva(); 
	            int extra = 0;

	            if (disciplina != null && vampiro.getPuntosSangre() >= disciplina.obtenerCostoSangre()) {
	                extra += disciplina.getAtaque();
	                vampiro.perderPuntosSangre(disciplina.obtenerCostoSangre());
	                if (vampiro.getPuntosSangre() >= 5) {
	                    extra += 2;
	                }
	                vampiro.aumentarPuntosSangre(4); 
	                if (vampiro.getPuntosSangre() > 10) vampiro.setPuntosSangre(10); 
	            }

	            return poderBase + equipoAtaque + extra + modificador;

	        } else if (personaje instanceof Licantropo) {
	            Licantropo lican = (Licantropo) personaje;
	            Don don = lican.getDonActivo(); 
	            int extra = 0;

	            if (don != null && lican.getRabia() >= don.getRabiaMinima()) {
	                extra += don.getAtaque();
	            }

	            return poderBase + equipoAtaque + extra + lican.getRabia() + modificador;

	        } else if (personaje instanceof Cazador) {
	            Cazador cazador = (Cazador) personaje;
	            Talento talento = cazador.getTalentoActivo();
	            int extra = 0;

	            if (talento != null) {
	                extra += talento.getAtaque();
	            }

	            return poderBase + equipoAtaque + extra + cazador.getVoluntad() + modificador;

	        } else {
	            return poderBase + equipoAtaque + modificador;
	        }
	    }

	    private int calcularPotencialDefensa(Personaje personaje) {
	        int defensaEquipo = personaje.getArmaduraActiva() != null ? personaje.getArmaduraActiva().getDefensa() : 0;
	        int poderBase = personaje.getPoder();
	        int modificador = calcularModificadorFortalezaDebilidad(personaje);

	        if (personaje instanceof Vampiro) {
	            Vampiro vampiro = (Vampiro) personaje;
	            Disciplina disciplina = vampiro.getDisciplinaActiva();
	            int extra = 0;

	            if (disciplina != null && vampiro.getPuntosSangre() >= disciplina.obtenerCostoSangre()) {
	                extra += disciplina.getDefensa();
	                vampiro.perderPuntosSangre(disciplina.obtenerCostoSangre());
	            }

	            return poderBase + defensaEquipo + extra + modificador;

	        } else if (personaje instanceof Licantropo) {
	            Licantropo lican = (Licantropo) personaje;
	            Don don = lican.getDonActivo();
	            int extra = 0;

	            if (don != null && lican.getRabia() >= don.getRabiaMinima()) {
	                extra += don.getDefensa();
	            }

	            return poderBase + defensaEquipo + extra + lican.getRabia() + modificador;

	        } else if (personaje instanceof Cazador) {
	            Cazador cazador = (Cazador) personaje;
	            Talento talento = cazador.getTalentoActivo();
	            int extra = 0;

	            if (talento != null) {
	                extra += talento.getDefensa();
	            }

	            return poderBase + defensaEquipo + extra + cazador.getVoluntad() + modificador;

	        } else {
	            return poderBase + defensaEquipo + modificador;
	        }
	    }
	    private int calcularModificadorFortalezaDebilidad(Personaje personaje) {
	        return 0;
	    }
	   

	public String getResultado() {
		return this.Resultado;
	}
	
	public void setResultado(String resultado) {
		this.Resultado = resultado;
	}
}