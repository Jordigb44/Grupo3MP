package storage;

import java.util.List;
import java.util.UUID;

import model.Ranking;
import model.desafio.Combate;
import model.desafio.Desafio;
import model.personaje.Personaje;
import model.personaje.habilidad.Arma;
import model.personaje.habilidad.Armadura;
import model.usuario.Usuario;
import notifications.I_Notification;

/**
 * Interfaz para la gestión del almacenamiento de datos.
 */
public interface I_Storage {
    
    // Ruta del archivo de almacenamiento
    String directoryPath = "data";

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
     * @param desafio Lista de desafíos a almacenar.
     * @return Mensaje de éxito o error.
     */
    String guardarDesafio(Desafio desafio);

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

    List<Arma> cargarArmas();

    List<Armadura> cargarArmaduras();

    List<String> getNotificacion(Usuario usuario);

    void setNotificacion(String nick, String mensaje);

    void deleteNotificacion(Usuario usuario);

	Desafio cargarDesafio(UUID desafioId);
}
