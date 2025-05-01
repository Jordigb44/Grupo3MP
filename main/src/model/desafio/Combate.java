package model.desafio;

import java.util.ArrayList;
import java.util.List;

import model.usuario.Jugador;

public class Combate {
    private Desafio desafio; 
    private List<Rondas> rondas;
    private Jugador ganador;
    private int puntos;
    
    
    public Combate(Desafio desafio) {
    	 this.desafio = desafio;
         this.rondas = new ArrayList<>();
    }
    
    public void iniciarCombate() {
        Jugador desafiante = this.desafio.getDesafiante();
        Jugador desafiando = this.desafio.getDesafiado();

        while (desafiante.getPersonaje().getSalud() > 0 && desafiando.getPersonaje().getSalud() > 0) {
            Rondas ronda = new Rondas(desafiante, desafiando);
            ronda.iniciarRonda();
            this.rondas.add(ronda);

            if (ronda.getResultado().equals(desafiante.getNombre() + " gana")) {
				// TODO: actualizamos puntos del jugador
            	this.ganador = desafiante;
                break;
            } else if (ronda.getResultado().equals(desafiando.getNombre() + " gana")) {
				// TODO: actualizamos puntos del jugador
            	this.ganador = desafiando;
                break;
            }
        }

        actualizarRanking();
    }
    
    public Desafio getDesafio() {
    	return this.desafio;
    }
    
    public List<Rondas> getRondas(){
    	return this.rondas;
    }
    
    public void actualizarRanking() {
    	// TODO: Agregar Guardar Combate
    	if (this.ganador != null) {
    		this.ganador.sumarPuntos(10); // Por ejemplo 10 puntos por victoria
        }
    }
    
    public void getResultadoRondas() {
    	 for (int i = 0; i < this.rondas.size(); i++) {
             System.out.println("Ronda " + (i + 1) + ": " + this.rondas.get(i).getResultado());
         }
    }
    
    public Jugador getGanador() {
        return ganador;
    }
    
    public void setGanador(Jugador ganador) {
    	this.ganador = ganador;
    }
}
