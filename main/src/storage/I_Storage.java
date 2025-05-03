package storage;

import java.util.List;
import java.util.UUID;
import model.desafio.Combate;
import model.desafio.Desafio;
import model.personaje.Esbirro;
import model.personaje.Personaje;
import model.personaje.habilidad.Arma;
import model.personaje.habilidad.Armadura;
import model.personaje.habilidad.Debilidad;
import model.personaje.habilidad.Disciplina;
import model.personaje.habilidad.Don;
import model.personaje.habilidad.Fortaleza;
import model.personaje.habilidad.Talento;
import model.usuario.Usuario;

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
     * Eliminar un usuario en el almacenamiento.
     * @param usuario Usuario a eliminar.
     * @return Mensaje de éxito o error.
     */
    String darDeBajaUsuario(String nick);

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
     * Carga todos los personajes almacenados de un jugador.
     * @return Lista de personajes.
     */
    List<Personaje> cargarPersonajesUsuario(String nick);
    
    boolean guardarPersonajesUsuario(String nick, List<Personaje> personajes);
    
    /**
     * Carga todos los personajes almacenados.
     * @return Lista de personajes.
     */
    List<Personaje> cargarPersonajes();
    
    List<String> getTiposPersonajes();
    
	String eliminarPersonajeUsuario(String nick, Personaje personaje);

    /**
     * Guarda un combate.
     * @param combate a almacenar.
     * @return Mensaje de éxito o error.
     */
    String guardarCombate(Combate combate);

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
    
    Desafio cargarDesafioUsuario(String nick);

    /**
     * Carga todas las armas almacenadas.
     * @return Lista de armas.
     */
    List<Arma> cargarArmas();

    /**
     * Carga todas las armaduras almacenadas.
     * @return Lista de armaduras.
     */
    List<Armadura> cargarArmaduras();

    /**
     * Carga todas las fortalezas almacenadas.
     * @return Lista de fortalezas.
     */
    List<Fortaleza> cargarFortalezas();

    /**
     * Carga todas las debilidades almacenadas.
     * @return Lista de debilidades.
     */
    List<Debilidad> cargarDebilidades();

    /**
     * Carga todos los esbirros almacenados.
     * @return Lista de esbirros.
     */
    List<Esbirro> cargarEsbirros();

    /**
     * Obtiene las notificaciones de un usuario.
     * @param usuario Usuario para el que se obtienen las notificaciones.
     * @return Lista de notificaciones.
     */
    List<String> getNotificacion(Usuario usuario);

    /**
     * Establece una notificación para un usuario.
     * @param nick Nick del usuario.
     * @param mensaje Mensaje de la notificación.
     */
    void setNotificacion(String nick, String mensaje);

    /**
     * Elimina las notificaciones de un usuario.
     * @param usuario Usuario para el que se eliminan las notificaciones.
     */
    void deleteNotificacion(Usuario usuario);

    /**
     * Carga un desafío específico.
     * @param desafioId ID del desafío a cargar.
     * @return Desafío cargado.
     */
    Desafio cargarDesafio(UUID desafioId);

	List<Talento> getTalentosCazador();

	int getVoluntadCazador();

	List<Don> getDonesLicantropo();

	int getRabiaLicantropo();

	int getPesoLicantropo();

	List<Disciplina> getDisciplinasVampiro();

	int getPuntosdeSangreVampiro();

	int getEdadVampiro();

}
