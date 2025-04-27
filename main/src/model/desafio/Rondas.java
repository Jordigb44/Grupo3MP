package model.desafio;

import java.util.Random;

import model.personaje.Personaje;
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
	        int ataqueBase = personaje.getArmaActiva() != null ? personaje.getArmaActiva().getAtaque() : 0;
	        return ataqueBase; // Aquí puedes añadir más lógica si hay fortalezas, talentos, etc.
	    }

	    private int calcularPotencialDefensa(Personaje personaje) {
	        int defensaBase = personaje.getArmaduraActiva() != null ? personaje.getArmaduraActiva().getDefensa() : 0;
	        return defensaBase; // Aquí puedes añadir más lógica si hay habilidades defensivas.
	    }

	public String getResultado() {
		return Resultado;
	}
}