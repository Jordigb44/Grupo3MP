package model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.usuario.Jugador;
import model.usuario.Usuario;
import ui.A_Interfaz;


public class Ranking {
    private List<Usuario> usuarios;
    private A_Interfaz interfaz;

    public Ranking(List<Usuario> jugadores, A_Interfaz interfaz) {
        this.usuarios = usuarios;
        this.interfaz = interfaz;
    }
  
    public void consultarRanking(List<Usuario> usuarios) {
        if (usuarios == null || usuarios.isEmpty()) {
            if (interfaz != null) {
                interfaz.mostrar("No hay usuarios para mostrar en el ranking.");
            } else {
                System.out.println("No hay usuarios para mostrar en el ranking.");
            }
            return;
        }

        // Ordenamos los usuarios por puntos de mayor a menor
        usuarios.sort((u1, u2) -> Integer.compare(u2.getPuntos(), u1.getPuntos()));

        // Mostrar el ranking
        if (interfaz != null) {
            interfaz.mostrar("\n========== RANKING GENERAL DE USUARIOS ==========");
            interfaz.mostrar(String.format("%-10s | %-20s | %-10s", "Posición", "Nombre", "Puntos"));
            interfaz.mostrar("--------------------------------------------------");

            // Asignamos la posición y mostramos el ranking
            for (int i = 0; i < usuarios.size(); i++) {
                Usuario usuario = usuarios.get(i);
                interfaz.mostrar(String.format("%-10d | %-20s | %-10d", 
                        i + 1, // La posición es simplemente el índice + 1
                        usuario.getNombre(), 
                        usuario.getPuntos()));
            }

            interfaz.mostrar("==================================================");
        } else {
            System.out.println("\n========== RANKING GENERAL DE USUARIOS ==========");
            System.out.printf("%-10s | %-20s | %-10s\n", "Posición", "Nombre", "Puntos");
            System.out.println("--------------------------------------------------");

            // Asignamos la posición y mostramos el ranking
            for (int i = 0; i < usuarios.size(); i++) {
                Usuario usuario = usuarios.get(i);
                System.out.printf("%-10d | %-20s | %-10d\n", 
                        i + 1, // La posición es simplemente el índice + 1
                        usuario.getNombre(), 
                        usuario.getPuntos());
            }

            System.out.println("==================================================");
        }
    }

}