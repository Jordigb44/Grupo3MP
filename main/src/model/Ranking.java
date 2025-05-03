package model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.usuario.Jugador;
import model.usuario.Usuario;
import storage.FileManager;
import ui.A_Interfaz;


public class Ranking {
    private List<Jugador> jugadores;
    private A_Interfaz interfaz;  
    private Sistema sistema;

    public Ranking(Sistema sistema) {
        this.sistema = sistema;
    }

    
    public void consultarRanking() {
        FileManager fileManager = sistema.getFileManager();
        List<Ranking> rankingList = fileManager.cargarRanking();
        List<Usuario> jugadores = sistema.getJugadores();

        interfaz.mostrar("===== RANKING DE JUGADORES =====");
        int posicion = 1;

        for (Ranking ranking : rankingList) {
            for (Usuario rankeado : sistema.getJugadores()) {
                for (Usuario jugador : jugadores) {
                    if (jugador.getNick().equals(rankeado.getNick())) {
                       interfaz.mostrar(posicion + ". " + jugador.getNick() + " - Puntos: " + jugador.getPuntos());
                        posicion++;
                        break;
                    }
                }
            }
        }
    }


 }
   
  

