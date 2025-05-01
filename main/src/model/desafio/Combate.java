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
        Jugador desafiante = desafio.getDesafiante();
        Jugador desafiando = desafio.getDesafiado();

        while (desafiante.getPersonaje().getSalud() > 0 && desafiando.getPersonaje().getSalud() > 0) {
            Rondas ronda = new Rondas(desafiante, desafiando);
            ronda.iniciarRonda();
            rondas.add(ronda);

            if (ronda.getResultado().equals(desafiante.getNombre() + " gana")) {
                ganador = desafiante;
                break;
            } else if (ronda.getResultado().equals(desafiando.getNombre() + " gana")) {
                ganador = desafiando;
                break;
            }
        }

        actualizarRanking();
    }
    
    public void actualizarRanking() {
    	// TODO: Agregar Guardar Combate
    	if (ganador != null) {
            ganador.sumarPuntos(10); // Por ejemplo 10 puntos por victoria
        }
    }
    
    public void getResultadoRondas() {
    	 for (int i = 0; i < rondas.size(); i++) {
             System.out.println("Ronda " + (i + 1) + ": " + rondas.get(i).getResultado());
         }
    }
    
    public Jugador getGanador() {
        return ganador;
    }
    
}
