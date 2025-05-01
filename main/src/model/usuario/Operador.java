package model.usuario;

import java.util.List;
import model.desafio.Desafio;
import model.desafio.E_EstadoDesafio;
import model.personaje.Esbirro;
import model.personaje.Personaje;
import model.personaje.habilidad.Arma;
import model.personaje.habilidad.Armadura;
import model.personaje.habilidad.Debilidad;
import model.personaje.habilidad.Fortaleza;
import storage.FileManager;
import ui.A_Interfaz;

/**
 * La clase Operador representa usuarios con permisos especiales en el sistema.
 * Permite gestionar jugadores, personajes, desafios y varios elementos del juego.
 */
public class Operador extends Usuario {
    // Atributo principal
    private FileManager fileManager;
    private A_Interfaz instanceInterface;
    private List<Usuario> usuarios;

    /**
     * Constructor de la clase Operador.
     * 
     * @param interfaz    Interfaz para interactuar con el usuario
     * @param fileManager Gestor de archivos para persistencia
     * @param nick        Apodo del operador
     * @param nombre      Nombre completo del operador
     * @param password    Contraseña del operador
     */
    public Operador(A_Interfaz interfaz, FileManager fileManager, String nick, String nombre, String password, Integer oro) {
        super(nick, nombre, password, "operador", "activo", oro);
        this.fileManager = fileManager;
        this.instanceInterface = interfaz;
        this.usuarios = this.fileManager.cargarUsuarios();
    }

    /**
     * Obtiene el men principal del operador y procesa la seleccion del usuario.
     */
    public void getMenu() {
        boolean salir = false;
        
        while (!salir) {
            this.instanceInterface.limpiarPantalla();
            
            StringBuilder menu = new StringBuilder();
            menu.append("=== MENÚ DE OPERADOR ===\n");
            menu.append("1. Gestión de Jugadores\n");
            menu.append("2. Gestión de Desafíos\n");
            menu.append("3. Gestión de Personajes\n");
            menu.append("0. Salir\n");
            menu.append("Seleccione una opción: ");
            
            this.instanceInterface.mostrar(menu.toString());
            String option = this.instanceInterface.pedirEntrada();
            
            switch (option) {
                case "1":
                    // Menu de gestion de jugadores
                    this.menuGestionJugadores();
                    break;
                case "2":
                    // Menu de gestion de desafios
                    this.menuGestionDesafios();
                    break;
                case "3":
                    // Menu de gestion de personajes
                    this.menuGestionPersonajes();
                    break;
                case "0":
                    // Salir
                    this.instanceInterface.mostrar("Saliendo del sistema...");
                    salir = true;
                    break;
                default:
                    this.instanceInterface.mostrar("Opción no válida. Presione Enter para continuar...");
                    this.instanceInterface.pedirEntrada();
                    break;
            }
        }
    }

    /**
     * Muestra el menu de gestion de jugadores y procesa la seleccion.
     */
    /**
 * Muestra el menú de gestión de personajes y procesa la selección.
 */
private void menuGestionPersonajes() {
    boolean volver = false;
    while (!volver) {
        this.instanceInterface.limpiarPantalla();
        StringBuilder menu = new StringBuilder();
        menu.append("=== GESTIÓN DE PERSONAJES ===\n");
        menu.append("1. Seleccionar y editar personaje\n");
        menu.append("0. Volver al menú principal\n");
        menu.append("Seleccione una opción: ");
        this.instanceInterface.mostrar(menu.toString());
        String option = this.instanceInterface.pedirEntrada();
        switch (option) {
            case "1":
                // Seleccionar y editar personaje
                Personaje personajeSeleccionado = this.seleccionarPersonaje();
                if (personajeSeleccionado != null) {
                    this.editarPersonaje(personajeSeleccionado);
                }
                break;
            case "0":
                volver = true;
                break;
            default:
                this.instanceInterface.mostrar("Opción no válida. Presione Enter para continuar...");
                this.instanceInterface.pedirEntrada();
                break;
        }
    }
}

/**
 * Edita un personaje seleccionado.
 * @param personaje Personaje a editar
 * @return Personaje editado o null si hay error
 */
public Personaje editarPersonaje(Personaje personaje) {
    if (personaje == null) {
        instanceInterface.mostrar("Error: Personaje no encontrado.");
        return null;
    }
    boolean editar = true;
    while (editar) {
        instanceInterface.limpiarPantalla();
        instanceInterface.mostrar("=== PERSONAJE: " + personaje.getNombre() + " ===");
        instanceInterface.mostrar(getMenuPersonaje());
        String opcion = instanceInterface.pedirEntrada();
        switch (opcion) {
            case "1": // Añadir arma
                instanceInterface.mostrar(getMenuArma());
                añadirArmaPersonaje(personaje);
                break;
            case "2": // Quitar arma
                quitarArmaPersonaje(personaje);
                break;
            case "3": // Añadir armadura
                instanceInterface.mostrar(getMenuArmadura());
                añadirArmaduraPersonaje(personaje);
                break;
            case "4": // Quitar armadura
                quitarArmaduraPersonaje(personaje);
                break;
            case "5": // Añadir fortaleza
                instanceInterface.mostrar(getMenuFortalezas());
                añadirFortalezaPersonaje(personaje);
                break;
            case "6": // Quitar fortaleza
                quitarFortalezaPersonaje(personaje);
                break;
            case "7": // Añadir debilidad
                instanceInterface.mostrar(getMenuDebilidades());
                añadirDebilidadPersonaje(personaje);
                break;
            case "8": // Quitar debilidad
                quitarDebilidadPersonaje(personaje);
                break;
            case "9": // Añadir esbirro
                instanceInterface.mostrar(getMenuEsbirro());
                añadirEsbirroPersonaje(personaje);
                break;
            case "10": // Quitar esbirro
                quitarEsbirroPersonaje(personaje);
                break;
            case "0": // Volver
                editar = false;
                break;
            default:
                instanceInterface.mostrar("Opción inválida. Presione Enter para continuar...");
                instanceInterface.pedirEntrada();
                break;
        }
        // Guardar cambios después de cada modificación
        if (opcion.matches("[1-9]|10")) {
            fileManager.guardarPersonaje(personaje);
            instanceInterface.mostrar("Cambios guardados. Presione Enter para continuar...");
            instanceInterface.pedirEntrada();
        }
    }
    return personaje;
}

/**
 * Obtiene el menú de edición de personajes.
 * @return Menú de personajes
 */
public String getMenuPersonaje() {
    StringBuilder menu = new StringBuilder();
    menu.append("=== EDICIÓN DE PERSONAJE ===\n");
    menu.append("1. Añadir arma\n");
    menu.append("2. Quitar arma\n");
    menu.append("3. Añadir armadura\n");
    menu.append("4. Quitar armadura\n");
    menu.append("5. Añadir fortaleza\n");
    menu.append("6. Quitar fortaleza\n");
    menu.append("7. Añadir debilidad\n");
    menu.append("8. Quitar debilidad\n");
    menu.append("9. Añadir esbirro\n");
    menu.append("10. Quitar esbirro\n");
    menu.append("0. Volver\n");
    menu.append("Seleccione una opción: ");
    return menu.toString();
}

/**
 * Obtiene el menú de armas disponibles.
 * @return Menú de armas
 */
public String getMenuArma() {
    StringBuilder menu = new StringBuilder();
    menu.append("=== ARMAS DISPONIBLES ===\n");
    List<Arma> armasDisponibles = fileManager.cargarArmasDisponibles();
    if (armasDisponibles.isEmpty()) {
        menu.append("No hay armas disponibles.\n");
    } else {
        for (int i = 0; i < armasDisponibles.size(); i++) {
            menu.append((i + 1) + ". " + armasDisponibles.get(i).getNombre() + "\n");
        }
    }
    menu.append("0. Cancelar\n");
    menu.append("Seleccione un arma para añadir: ");
    return menu.toString();
}

/**
 * Obtiene el menú de armaduras disponibles.
 * @return Menú de armaduras
 */
public String getMenuArmadura() {
    StringBuilder menu = new StringBuilder();
    menu.append("=== ARMADURAS DISPONIBLES ===\n");
    List<Armadura> armadurasDisponibles = fileManager.cargarArmadurasDisponibles();
    if (armadurasDisponibles.isEmpty()) {
        menu.append("No hay armaduras disponibles.\n");
    } else {
        for (int i = 0; i < armadurasDisponibles.size(); i++) {
            menu.append((i + 1) + ". " + armadurasDisponibles.get(i).getNombre() + "\n");
        }
    }
    menu.append("0. Cancelar\n");
    menu.append("Seleccione una armadura para añadir: ");
    return menu.toString();
}

/**
 * Obtiene el menú de fortalezas disponibles.
 * @return Menú de fortalezas
 */
public String getMenuFortalezas() {
    StringBuilder menu = new StringBuilder();
    menu.append("=== FORTALEZAS DISPONIBLES ===\n");
    List<Fortaleza> fortalezasDisponibles = fileManager.cargarFortalezasDisponibles();
    if (fortalezasDisponibles.isEmpty()) {
        menu.append("No hay fortalezas disponibles.\n");
    } else {
        for (int i = 0; i < fortalezasDisponibles.size(); i++) {
            menu.append((i + 1) + ". " + fortalezasDisponibles.get(i).getNombre() + "\n");
        }
    }
    menu.append("0. Cancelar\n");
    menu.append("Seleccione una fortaleza para añadir: ");
    return menu.toString();
}

/**
 * Obtiene el menú de debilidades disponibles.
 * @return Menú de debilidades
 */
public String getMenuDebilidades() {
    StringBuilder menu = new StringBuilder();
    menu.append("=== DEBILIDADES DISPONIBLES ===\n");
    List<Debilidad> debilidadesDisponibles = fileManager.cargarDebilidadesDisponibles();
    if (debilidadesDisponibles.isEmpty()) {
        menu.append("No hay debilidades disponibles.\n");
    } else {
        for (int i = 0; i < debilidadesDisponibles.size(); i++) {
            menu.append((i + 1) + ". " + debilidadesDisponibles.get(i).getNombre() + "\n");
        }
    }
    menu.append("0. Cancelar\n");
    menu.append("Seleccione una debilidad para añadir: ");
    return menu.toString();
}

/**
 * Obtiene el menú de esbirros disponibles.
 * @return Menú de esbirros
 */
public String getMenuEsbirro() {
    StringBuilder menu = new StringBuilder();
    menu.append("=== ESBIRROS DISPONIBLES ===\n");
    List<Esbirro> esbirrosDisponibles = fileManager.cargarEsbirrosDisponibles();
    if (esbirrosDisponibles.isEmpty()) {
        menu.append("No hay esbirros disponibles.\n");
    } else {
        for (int i = 0; i < esbirrosDisponibles.size(); i++) {
            menu.append((i + 1) + ". " + esbirrosDisponibles.get(i).getNombre() + "\n");
        }
    }
    menu.append("0. Cancelar\n");
    menu.append("Seleccione un esbirro para añadir: ");
    return menu.toString();
}

/**
 * Añade un arma al personaje.
 * @param personaje Personaje al que añadir el arma
 */
private void añadirArmaPersonaje(Personaje personaje) {
    String opcion = instanceInterface.pedirEntrada();
    try {
        int index = Integer.parseInt(opcion);
        if (index == 0) {
            return; // Cancelar
        }
        List<Arma> armasDisponibles = fileManager.cargarArmasDisponibles();
        if (index > 0 && index <= armasDisponibles.size()) {
            Arma arma = armasDisponibles.get(index - 1);
            personaje.getArmas().add(arma);
            instanceInterface.mostrar("Arma '" + arma.getNombre() + "' añadida correctamente.");
        } else {
            instanceInterface.mostrar("Opción inválida.");
        }
    } catch (NumberFormatException e) {
        instanceInterface.mostrar("Por favor, ingrese un número válido.");
    }
}

/**
 * Quita un arma del personaje.
 * @param personaje Personaje del que quitar el arma
 */
private void quitarArmaPersonaje(Personaje personaje) {
    instanceInterface.limpiarPantalla();
    instanceInterface.mostrar("=== QUITAR ARMA ===");
    List<Arma> armas = personaje.getArmas();
    if (armas.isEmpty()) {
        instanceInterface.mostrar("El personaje no tiene armas para quitar.");
        instanceInterface.pedirEntrada();
        return;
    }
    
    // Mostrar armas del personaje
    instanceInterface.mostrar("Armas del personaje:");
    for (int i = 0; i < armas.size(); i++) {
        instanceInterface.mostrar((i + 1) + ". " + armas.get(i).getNombre());
    }
    instanceInterface.mostrar("0. Cancelar");
    instanceInterface.mostrar("Seleccione un arma para quitar:");
    
    String opcion = instanceInterface.pedirEntrada();
    try {
        int index = Integer.parseInt(opcion);
        if (index == 0) {
            return; // Cancelar
        }
        if (index > 0 && index <= armas.size()) {
            Arma arma = armas.get(index - 1);
            armas.remove(index - 1);
            instanceInterface.mostrar("Arma '" + arma.getNombre() + "' quitada correctamente.");
        } else {
            instanceInterface.mostrar("Opción inválida.");
        }
    } catch (NumberFormatException e) {
        instanceInterface.mostrar("Por favor, ingrese un número válido.");
    }
}

/**
 * Añade una armadura al personaje.
 * @param personaje Personaje al que añadir la armadura
 */
private void añadirArmaduraPersonaje(Personaje personaje) {
    String opcion = instanceInterface.pedirEntrada();
    try {
        int index = Integer.parseInt(opcion);
        if (index == 0) {
            return; // Cancelar
        }
        List<Armadura> armadurasDisponibles = fileManager.cargarArmadurasDisponibles();
        if (index > 0 && index <= armadurasDisponibles.size()) {
            Armadura armadura = armadurasDisponibles.get(index - 1);
            personaje.getArmaduras().add(armadura);
            instanceInterface.mostrar("Armadura '" + armadura.getNombre() + "' añadida correctamente.");
        } else {
            instanceInterface.mostrar("Opción inválida.");
        }
    } catch (NumberFormatException e) {
        instanceInterface.mostrar("Por favor, ingrese un número válido.");
    }
}

/**
 * Quita una armadura del personaje.
 * @param personaje Personaje del que quitar la armadura
 */
private void quitarArmaduraPersonaje(Personaje personaje) {
    instanceInterface.limpiarPantalla();
    instanceInterface.mostrar("=== QUITAR ARMADURA ===");
    List<Armadura> armaduras = personaje.getArmaduras();
    if (armaduras.isEmpty()) {
        instanceInterface.mostrar("El personaje no tiene armaduras para quitar.");
        instanceInterface.pedirEntrada();
        return;
    }
    
    // Mostrar armaduras del personaje
    instanceInterface.mostrar("Armaduras del personaje:");
    for (int i = 0; i < armaduras.size(); i++) {
        instanceInterface.mostrar((i + 1) + ". " + armaduras.get(i).getNombre());
    }
    instanceInterface.mostrar("0. Cancelar");
    instanceInterface.mostrar("Seleccione una armadura para quitar:");
    
    String opcion = instanceInterface.pedirEntrada();
    try {
        int index = Integer.parseInt(opcion);
        if (index == 0) {
            return; // Cancelar
        }
        if (index > 0 && index <= armaduras.size()) {
            Armadura armadura = armaduras.get(index - 1);
            armaduras.remove(index - 1);
            instanceInterface.mostrar("Armadura '" + armadura.getNombre() + "' quitada correctamente.");
        } else {
            instanceInterface.mostrar("Opción inválida.");
        }
    } catch (NumberFormatException e) {
        instanceInterface.mostrar("Por favor, ingrese un número válido.");
    }
}

/**
 * Añade una fortaleza al personaje.
 * @param personaje Personaje al que añadir la fortaleza
 */
private void añadirFortalezaPersonaje(Personaje personaje) {
    String opcion = instanceInterface.pedirEntrada();
    try {
        int index = Integer.parseInt(opcion);
        if (index == 0) {
            return; // Cancelar
        }
        List<Fortaleza> fortalezasDisponibles = fileManager.cargarFortalezasDisponibles();
        if (index > 0 && index <= fortalezasDisponibles.size()) {
            Fortaleza fortaleza = fortalezasDisponibles.get(index - 1);
            personaje.getFortalezas().add(fortaleza);
            instanceInterface.mostrar("Fortaleza '" + fortaleza.getNombre() + "' añadida correctamente.");
        } else {
            instanceInterface.mostrar("Opción inválida.");
        }
    } catch (NumberFormatException e) {
        instanceInterface.mostrar("Por favor, ingrese un número válido.");
    }
}

/**
 * Quita una fortaleza del personaje.
 * @param personaje Personaje del que quitar la fortaleza
 */
private void quitarFortalezaPersonaje(Personaje personaje) {
    instanceInterface.limpiarPantalla();
    instanceInterface.mostrar("=== QUITAR FORTALEZA ===");
    List<Fortaleza> fortalezas = personaje.getFortalezas();
    if (fortalezas.isEmpty()) {
        instanceInterface.mostrar("El personaje no tiene fortalezas para quitar.");
        instanceInterface.pedirEntrada();
        return;
    }
    
    // Mostrar fortalezas del personaje
    instanceInterface.mostrar("Fortalezas del personaje:");
    for (int i = 0; i < fortalezas.size(); i++) {
        instanceInterface.mostrar((i + 1) + ". " + fortalezas.get(i).getNombre());
    }
    instanceInterface.mostrar("0. Cancelar");
    instanceInterface.mostrar("Seleccione una fortaleza para quitar:");
    
    String opcion = instanceInterface.pedirEntrada();
    try {
        int index = Integer.parseInt(opcion);
        if (index == 0) {
            return; // Cancelar
        }
        if (index > 0 && index <= fortalezas.size()) {
            Fortaleza fortaleza = fortalezas.get(index - 1);
            fortalezas.remove(index - 1);
            instanceInterface.mostrar("Fortaleza '" + fortaleza.getNombre() + "' quitada correctamente.");
        } else {
            instanceInterface.mostrar("Opción inválida.");
        }
    } catch (NumberFormatException e) {
        instanceInterface.mostrar("Por favor, ingrese un número válido.");
    }
}

/**
 * Añade una debilidad al personaje.
 * @param personaje Personaje al que añadir la debilidad
 */
private void añadirDebilidadPersonaje(Personaje personaje) {
    String opcion = instanceInterface.pedirEntrada();
    try {
        int index = Integer.parseInt(opcion);
        if (index == 0) {
            return; // Cancelar
        }
        List<Debilidad> debilidadesDisponibles = fileManager.cargarDebilidadesDisponibles();
        if (index > 0 && index <= debilidadesDisponibles.size()) {
            Debilidad debilidad = debilidadesDisponibles.get(index - 1);
            personaje.getDebilidades().add(debilidad);
            instanceInterface.mostrar("Debilidad '" + debilidad.getNombre() + "' añadida correctamente.");
        } else {
            instanceInterface.mostrar("Opción inválida.");
        }
    } catch (NumberFormatException e) {
        instanceInterface.mostrar("Por favor, ingrese un número válido.");
    }
}

/**
 * Quita una debilidad del personaje.
 * @param personaje Personaje del que quitar la debilidad
 */
private void quitarDebilidadPersonaje(Personaje personaje) {
    instanceInterface.limpiarPantalla();
    instanceInterface.mostrar("=== QUITAR DEBILIDAD ===");
    List<Debilidad> debilidades = personaje.getDebilidades();
    if (debilidades.isEmpty()) {
        instanceInterface.mostrar("El personaje no tiene debilidades para quitar.");
        instanceInterface.pedirEntrada();
        return;
    }
    
    // Mostrar debilidades del personaje
    instanceInterface.mostrar("Debilidades del personaje:");
    for (int i = 0; i < debilidades.size(); i++) {
        instanceInterface.mostrar((i + 1) + ". " + debilidades.get(i).getNombre());
    }
    instanceInterface.mostrar("0. Cancelar");
    instanceInterface.mostrar("Seleccione una debilidad para quitar:");
    
    String opcion = instanceInterface.pedirEntrada();
    try {
        int index = Integer.parseInt(opcion);
        if (index == 0) {
            return; // Cancelar
        }
        if (index > 0 && index <= debilidades.size()) {
            Debilidad debilidad = debilidades.get(index - 1);
            debilidades.remove(index - 1);
            instanceInterface.mostrar("Debilidad '" + debilidad.getNombre() + "' quitada correctamente.");
        } else {
            instanceInterface.mostrar("Opción inválida.");
        }
    } catch (NumberFormatException e) {
        instanceInterface.mostrar("Por favor, ingrese un número válido.");
    }
}

/**
 * Añade un esbirro al personaje.
 * @param personaje Personaje al que añadir el esbirro
 */
private void añadirEsbirroPersonaje(Personaje personaje) {
    String opcion = instanceInterface.pedirEntrada();
    try {
        int index = Integer.parseInt(opcion);
        if (index == 0) {
            return; // Cancelar
        }
        List<Esbirro> esbirrosDisponibles = fileManager.cargarEsbirrosDisponibles();
        if (index > 0 && index <= esbirrosDisponibles.size()) {
            Esbirro esbirro = esbirrosDisponibles.get(index - 1);
            personaje.getEsbirros().add(esbirro);
            instanceInterface.mostrar("Esbirro '" + esbirro.getNombre() + "' añadido correctamente.");
        } else {
            instanceInterface.mostrar("Opción inválida.");
        }
    } catch (NumberFormatException e) {
        instanceInterface.mostrar("Por favor, ingrese un número válido.");
    }
}

/**
 * Quita un esbirro del personaje.
 * @param personaje Personaje del que quitar el esbirro
 */
private void quitarEsbirroPersonaje(Personaje personaje) {
    instanceInterface.limpiarPantalla();
    instanceInterface.mostrar("=== QUITAR ESBIRRO ===");
    List<Esbirro> esbirros = personaje.getEsbirros();
    if (esbirros.isEmpty()) {
        instanceInterface.mostrar("El personaje no tiene esbirros para quitar.");
        instanceInterface.pedirEntrada();
        return;
    }
    
    // Mostrar esbirros del personaje
    instanceInterface.mostrar("Esbirros del personaje:");
    for (int i = 0; i < esbirros.size(); i++) {
        instanceInterface.mostrar((i + 1) + ". " + esbirros.get(i).getNombre());
    }
    instanceInterface.mostrar("0. Cancelar");
    instanceInterface.mostrar("Seleccione un esbirro para quitar:");
    
    String opcion = instanceInterface.pedirEntrada();
    try {
        int index = Integer.parseInt(opcion);
        if (index == 0) {
            return; // Cancelar
        }
        if (index > 0 && index <= esbirros.size()) {
            Esbirro esbirro = esbirros.get(index - 1);
            esbirros.remove(index - 1);
            instanceInterface.mostrar("Esbirro '" + esbirro.getNombre() + "' quitado correctamente.");
        } else {
            instanceInterface.mostrar("Opción inválida.");
        }
    } catch (NumberFormatException e) {
        instanceInterface.mostrar("Por favor, ingrese un número válido.");
    }
}

}