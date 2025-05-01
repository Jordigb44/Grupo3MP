package model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.usuario.Jugador;
import ui.A_Interfaz;


public class Ranking {
    private List<Jugador> jugadores;
    private A_Interfaz interfaz;
   
    public Ranking() {
        this.jugadores = new ArrayList<>();
    }

    public Ranking(List<Jugador> jugadores, A_Interfaz interfaz) {
        this.jugadores = jugadores;
        this.interfaz = interfaz;
    }
 
    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }
    
    public void setInterfaz(A_Interfaz interfaz) {
        this.interfaz = interfaz;
    }
    
    public void ordenarRanking() {
        if (jugadores == null || jugadores.isEmpty()) {
            return;
        }
        
         Collections.sort(jugadores, new Comparator<Jugador>() {
            @Override
            public int compare(Jugador j1, Jugador j2) {
                return Integer.compare(j2.getPosicionRanking(), j1.getPosicionRanking());
            }
        });
    }
  
    public void consultarRanking(List<Jugador> jugadores) {
        if (jugadores == null || jugadores.isEmpty()) {
            if (interfaz != null) {
                interfaz.mostrar("No hay jugadores para mostrar en el ranking.");
            } else {
                System.out.println("No hay jugadores para mostrar en el ranking.");
            }
            return;
        }
        
     
        List<Jugador> jugadoresRanking = new ArrayList<>(jugadores);
     
        ordenarRanking();
    
        for (int i = 0; i < jugadoresRanking.size(); i++) {
            jugadoresRanking.get(i).setPosicionRanking(i + 1);
        }
        

        if (interfaz != null) {
            interfaz.mostrar("\n========== RANKING GENERAL DE JUGADORES ==========");
            interfaz.mostrar(String.format("%-10s | %-20s | %-10s", "Posición", "Nombre", "Puntos"));
            interfaz.mostrar("--------------------------------------------------");
            
            for (Jugador jugador : jugadoresRanking) {
                interfaz.mostrar(String.format("%-10d | %-20s | %-10d", 
                                  jugador.getPosicionRanking(), 
                                  jugador.getNombre(), 
                                  jugador.getPuntos()));
            }
            
            interfaz.mostrar("==================================================");
        } else {
            System.out.println("\n========== RANKING GENERAL DE JUGADORES ==========");
            System.out.printf("%-10s | %-20s | %-10s\n", "Posición", "Nombre", "Puntos");
            System.out.println("--------------------------------------------------");
            
            for (Jugador jugador : jugadoresRanking) {
                System.out.printf("%-10d | %-20s | %-10d\n", 
                                 jugador.getPosicionRanking(), 
                                 jugador.getNombre(), 
                                 jugador.getPuntos());
            }
            
            System.out.println("==================================================");
        }
        
    
        this.jugadores = jugadoresRanking;   
   
    }
   
  

}