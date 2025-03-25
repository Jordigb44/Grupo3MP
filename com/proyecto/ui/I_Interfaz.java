package com.proyecto.ui;

/**
 * Interfaz para la gestión de la interacción con el usuario en la aplicación.
 */
public interface I_Interfaz {

    /**
     * Muestra la interfaz o la pantalla.
     * 
     * @param contenido El contenido a mostrar en la interfaz.
     */
    void mostrar(String contenido);

    /**
     * Solicita una entrada del usuario.
     * 
     * @return La entrada del usuario.
     */
    String pedirEntrada();

    /**
     * Limpia la pantalla o la interfaz de usuario.
     */
    void limpiarPantalla();
}
