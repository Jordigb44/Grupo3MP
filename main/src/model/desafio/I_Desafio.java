package model.desafio;

import model.usuario.Jugador;
import ui.A_Interfaz;

public interface I_Desafio {
    
    /**
     * Intenta crear un desafío entre dos jugadores.
     * 
     * @param desafiante Jugador que inicia el desafío.
     * @param desafiado Jugador que es desafiado.
     * @param oroApostado Cantidad de oro apostado.
     * @return Mensaje indicando si se pudo crear el desafío.
     */
    String Desafiar(Jugador desafiante, Jugador desafiado, int oroApostado);

    /**
     * Acepta un desafío, lo cual inicia un combate.
     * 
     * @param d El desafío a aceptar.
     * @return Objeto Combate representando el combate generado.
     */
    void Aceptar(Desafio d);

    /**
     * Rechaza un desafío, aplicando una penalización al jugador desafiado.
     */
    void Rechazar(A_Interfaz interfaz1);
}
