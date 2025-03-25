package com.proyecto.storage;

import com.proyecto.model.usuario.Usuario;
import com.proyecto.model.personaje.Personaje;
import com.proyecto.model.desafio.Desafio;
import com.proyecto.model.desafio.Combate;
import com.proyecto.model.Ranking;
import java.util.List;

/**
 * Interfaz para la gestión del almacenamiento de datos.
 */
public interface I_Storage {
    
    // Ruta del archivo de almacenamiento
    String file_path = "data/storage.xml";

    /**
     * Guarda un usuario en el almacenamiento.
     * @param usuario Usuario a guardar.
     * @return Mensaje de éxito o error.
     */
    String guardarUsuario(Usuario usuario);

    /**
     * Carga todos los usuarios almacenados.
     * @return Lista de usuarios.
     */
    List<Usuario> cargarUsuarios();

    /**
     * Guarda una lista de personajes.
     * @param personajes Lista de personajes a almacenar.
     * @return Mensaje de éxito o error.
     */
    String guardarPersonajes(List<Personaje> personajes);

    /**
     * Carga todos los personajes almacenados.
     * @return Lista de personajes.
     */
    List<Personaje> cargarPersonajes();

    /**
     * Guarda una lista de combates.
     * @param combates Lista de combates a almacenar.
     * @return Mensaje de éxito o error.
     */
    String guardarCombates(List<Combate> combates);

    /**
     * Carga todos los combates almacenados.
     * @return Lista de combates.
     */
    List<Combate> cargarCombates();

    /**
     * Guarda una lista de desafíos.
     * @param desafios Lista de desafíos a almacenar.
     * @return Mensaje de éxito o error.
     */
    String guardarDesafios(List<Desafio> desafios);

    /**
     * Carga todos los desafíos almacenados.
     * @return Lista de desafíos.
     */
    List<Desafio> cargarDesafios();

    /**
     * Guarda el ranking del juego.
     * @param ranking Ranking a almacenar.
     * @return Mensaje de éxito o error.
     */
    String guardarRanking(Ranking ranking);

    /**
     * Carga el ranking almacenado.
     * @return Lista de rankings.
     */
    List<Ranking> cargarRanking();
}
