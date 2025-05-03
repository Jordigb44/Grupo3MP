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
    // Atributos principales
    private FileManager fileManager;
    private A_Interfaz instanceInterface;
    private List<Usuario> usuarios;

    /**
     * Constructor de la clase Operador a partir de un usuario existente.
     * 
     * @param interfaz    Interfaz para interactuar con el usuario
     * @param fileManager Gestor de archivos para persistencia
     * @param usuario     Usuario base para el operador
     */
    public Operador(A_Interfaz interfaz, FileManager fileManager, Usuario usuario) {
        super(usuario.getUserId(), usuario.getNick(), usuario.getNombre(), usuario.getPassword(), usuario.getRol(), usuario.getEstado(), usuario.getOro(), usuario.getPuntos());
        this.fileManager = fileManager;
        this.instanceInterface = interfaz;
        this.usuarios = this.fileManager.cargarUsuarios();
    }

    /**
     * Obtiene el menú principal del operador y procesa la selección del usuario.
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
                    // Menú de gestión de jugadores
                    this.menuGestionJugadores();
                    break;
                case "2":
                    // Menú de gestión de desafíos
                    this.menuGestionDesafios();
                    break;
                case "3":
                    // Menú de gestión de personajes
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
     * Muestra el menú de gestión de jugadores y procesa la selección.
     */
    private void menuGestionJugadores() {
        boolean volver = false;
        
        while (!volver) {
            this.instanceInterface.limpiarPantalla();
            
            StringBuilder menu = new StringBuilder();
            menu.append("=== GESTIÓN DE JUGADORES ===\n");
            menu.append("1. Ver jugadores sin bloquear\n");
            menu.append("2. Ver jugadores bloqueados\n");
            menu.append("3. Listar todos los usuarios\n");
            menu.append("4. Dar de baja usuario\n");
            menu.append("0. Volver al menú principal\n");
            menu.append("Seleccione una opción: ");
            
            this.instanceInterface.mostrar(menu.toString());
            String option = this.instanceInterface.pedirEntrada();
            
            switch (option) {
                case "1":
                    // Ver jugadores sin bloquear
                    this.instanceInterface.mostrar(this.getMenuJugadoresSinBloquear());
                    String opcionJugador = this.instanceInterface.pedirEntrada();
                    
                    try {
                        int index = Integer.parseInt(opcionJugador);
                        if (index == 0) {
                            break; // Volver al menú anterior
                        }
                        
                        List<Jugador> jugadores = this.fileManager.cargarJugadoresActivos();
                        if (index > 0 && index <= jugadores.size()) {
                            Jugador jugador = jugadores.get(index - 1);
                            String resultado = this.bloquearJugador(jugador);
                            this.instanceInterface.mostrar(resultado);
                            this.instanceInterface.mostrar("Presione Enter para continuar...");
                            this.instanceInterface.pedirEntrada();
                        } else {
                            this.instanceInterface.mostrar("Opción inválida. Presione Enter para continuar...");
                            this.instanceInterface.pedirEntrada();
                        }
                    } catch (NumberFormatException e) {
                        this.instanceInterface.mostrar("Por favor, ingrese un número válido. Presione Enter para continuar...");
                        this.instanceInterface.pedirEntrada();
                    }
                    break;
                    
                case "2":
                    // Ver jugadores bloqueados
                    this.instanceInterface.mostrar(this.getMenuJugadoresBloqueados());
                    String opcionJugadorB = this.instanceInterface.pedirEntrada();
                    
                    try {
                        int index = Integer.parseInt(opcionJugadorB);
                        if (index == 0) {
                            break; // Volver al menú anterior
                        }
                        
                        List<Jugador> jugadores = this.fileManager.cargarJugadoresBloqueados();
                        if (index > 0 && index <= jugadores.size()) {
                            Jugador jugador = jugadores.get(index - 1);
                            String resultado = this.desbloquearJugador(jugador);
                            this.instanceInterface.mostrar(resultado);
                            this.instanceInterface.mostrar("Presione Enter para continuar...");
                            this.instanceInterface.pedirEntrada();
                        } else {
                            this.instanceInterface.mostrar("Opción inválida. Presione Enter para continuar...");
                            this.instanceInterface.pedirEntrada();
                        }
                    } catch (NumberFormatException e) {
                        this.instanceInterface.mostrar("Por favor, ingrese un número válido. Presione Enter para continuar...");
                        this.instanceInterface.pedirEntrada();
                    }
                    break;
                
                case "3":
                    // Listar todos los usuarios
                    listarUsuarios();
                    this.instanceInterface.mostrar("Presione Enter para continuar...");
                    this.instanceInterface.pedirEntrada();
                    break;
                
                case "4":
                    // Dar de baja usuario
                    this.instanceInterface.mostrar("Ingrese el nick del usuario a dar de baja: ");
                    String nick = this.instanceInterface.pedirEntrada();
                    String resultado = fileManager.darDeBajaUsuario(nick);
                    this.instanceInterface.mostrar(resultado);
                    this.instanceInterface.mostrar("Presione Enter para continuar...");
                    this.instanceInterface.pedirEntrada();
                    break;
                
                case "0":
                    volver = true;
                    break;
                    
                default:
                    this.instanceInterface.mostrar("Opción inválida. Presione Enter para continuar...");
                    this.instanceInterface.pedirEntrada();
                    break;
            }
        }
    }

    /**
     * Muestra el menú de gestión de desafíos y procesa la selección.
     */
    private void menuGestionDesafios() {
        boolean volver = false;
        
        while (!volver) {
            this.instanceInterface.limpiarPantalla();
            this.instanceInterface.mostrar(this.getMenuDesafio());
            String option = this.instanceInterface.pedirEntrada();
            
            switch (option) {
                case "1":
                    // Ver desafíos pendientes
                    this.instanceInterface.mostrar(this.getMenuAprobarDesafios());
                    Desafio desafioSeleccionado = this.seleccionarDesafio();
                    
                    if (desafioSeleccionado != null) {
                        String resultado = this.validarDesafio(desafioSeleccionado);
                        this.instanceInterface.mostrar(resultado);
                        this.instanceInterface.mostrar("Presione Enter para continuar...");
                        this.instanceInterface.pedirEntrada();
                    }
                    break;
                    
                case "2":
                    // Ver desafíos aprobados
                    this.instanceInterface.mostrar("Funcionalidad no implementada. Presione Enter para continuar...");
                    this.instanceInterface.pedirEntrada();
                    break;
                    
                case "3":
                    // Ver desafíos rechazados
                    this.instanceInterface.mostrar("Funcionalidad no implementada. Presione Enter para continuar...");
                    this.instanceInterface.pedirEntrada();
                    break;
                    
                case "0":
                    volver = true;
                    break;
                    
                default:
                    this.instanceInterface.mostrar("Opción inválida. Presione Enter para continuar...");
                    this.instanceInterface.pedirEntrada();
                    break;
            }
        }
    }

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
            menu.append("2. Listar todos los personajes\n");
            menu.append("3. Listar personajes por usuario\n");
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
                case "2":
                    // Listar todos los personajes
                    listarPersonajes();
                    this.instanceInterface.mostrar("Presione Enter para continuar...");
                    this.instanceInterface.pedirEntrada();
                    break;
                case "3":
                    // Listar personajes por usuario
                    this.instanceInterface.mostrar("Ingrese el nick del usuario: ");
                    String nick = this.instanceInterface.pedirEntrada();
                    listarPersonajesPorUsuario(nick);
                    this.instanceInterface.mostrar("Presione Enter para continuar...");
                    this.instanceInterface.pedirEntrada();
                    break;
                case "0":
                    volver = true;
                    break;
                default:
                    this.instanceInterface.mostrar("Opción inválida. Presione Enter para continuar...");
                    this.instanceInterface.pedirEntrada();
                    break;
            }
        }
    }

    /**
     * Lista todos los usuarios registrados
     */
    private void listarUsuarios() {
        List<Usuario> usuarios = fileManager.cargarUsuarios();
        if (usuarios.isEmpty()) {
            this.instanceInterface.mostrar("No hay usuarios registrados.");
            return;
        }
        StringBuilder listaUsuarios = new StringBuilder("\n--- Lista de Usuarios ---\n");
        for (Usuario u : usuarios) {
            listaUsuarios.append(String.format("Nick: %s | Nombre: %s | Estado: %s | Rol: %s%n",
                    u.getNick(), u.getNombre(), u.getEstado(), u.getRol()));
        }
        this.instanceInterface.mostrar(listaUsuarios.toString());
    }

    /**
     * Lista todos los personajes registrados
     */
    private void listarPersonajes() {
        List<Personaje> personajes = fileManager.cargarPersonajes();
        if (personajes.isEmpty()) {
            this.instanceInterface.mostrar("No hay personajes registrados.");
            return;
        }
        StringBuilder listaPersonajes = new StringBuilder("\n--- Lista de Personajes ---\n");
        for (Personaje p : personajes) {
            listaPersonajes.append(String.format("Nombre: %s | Salud: %d | Oro: %d%n",
                    p.getNombre(), p.getSalud(), p.getOro()));
        }
        this.instanceInterface.mostrar(listaPersonajes.toString());
    }

    /**
     * Lista personajes de un usuario específico
     */
    private void listarPersonajesPorUsuario(String nick) {
        List<Personaje> personajes = fileManager.cargarPersonajesUsuario(nick);
        if (personajes.isEmpty()) {
            this.instanceInterface.mostrar("No se encontraron personajes para el usuario: " + nick);
            return;
        }
        StringBuilder listaPersonajes = new StringBuilder("\n--- Personajes del usuario " + nick + " ---\n");
        for (Personaje p : personajes) {
            listaPersonajes.append(String.format("Nombre: %s | Salud: %d | Oro: %d%n",
                    p.getNombre(), p.getSalud(), p.getOro()));
        }
        this.instanceInterface.mostrar(listaPersonajes.toString());
    }

    /**
     * Obtiene la lista de jugadores sin bloquear.
     * 
     * @return Menú con la lista de jugadores sin bloquear
     */
    public String getMenuJugadoresSinBloquear() {
        List<Jugador> jugadores = this.fileManager.cargarJugadoresActivos();
        
        StringBuilder menu = new StringBuilder();
        menu.append("=== JUGADORES SIN BLOQUEAR ===\n");
        
        if (jugadores.isEmpty()) {
            menu.append("No hay jugadores sin bloquear.\n");
        } else {
            for (int i = 0; i < jugadores.size(); i++) {
                menu.append((i + 1) + ". " + jugadores.get(i).getNick() + " (" + jugadores.get(i).getNombre() + ")\n");
            }
        }
        
        menu.append("0. Volver\n");
        menu.append("Seleccione un jugador para bloquear: ");
        
        return menu.toString();
    }

    /**
     * Bloquea a un jugador específico.
     * 
     * @param jugador Jugador a bloquear
     * @return Mensaje de resultado
     */
    public String bloquearJugador(Jugador jugador) {
        if (jugador == null) {
            return "Error: Jugador no encontrado.";
        }
        
        jugador.setEstado("bloqueado");
        fileManager.actualizarJugador(jugador);
        
        return "Jugador " + jugador.getNick() + " bloqueado correctamente.";
    }

    /**
     * Obtiene la lista de jugadores bloqueados.
     * 
     * @return Menú con la lista de jugadores bloqueados
     */
    public String getMenuJugadoresBloqueados() {
        List<Jugador> jugadores = this.fileManager.cargarJugadoresBloqueados();
        
        StringBuilder menu = new StringBuilder();
        menu.append("=== JUGADORES BLOQUEADOS ===\n");
        
        if (jugadores.isEmpty()) {
            menu.append("No hay jugadores bloqueados.\n");
        } else {
            for (int i = 0; i < jugadores.size(); i++) {
                menu.append((i + 1) + ". " + jugadores.get(i).getNick() + " (" + jugadores.get(i).getNombre() + ")\n");
            }
        }
        
        menu.append("0. Volver\n");
        menu.append("Seleccione un jugador para desbloquear: ");
        
        return menu.toString();
    }

    /**
     * Desbloquea a un jugador específico.
     * 
     * @param jugador Jugador a desbloquear
     * @return Mensaje de resultado
     */
    public String desbloquearJugador(Jugador jugador) {
        if (jugador == null) {
            return "Error: Jugador no encontrado.";
        }
        
        jugador.setEstado("activo");
        fileManager.guardarUsuario(jugador);
        
        return "Jugador " + jugador.getNick() + " desbloqueado correctamente.";
    }

    /**
     * Obtiene el menú de aprobación de desafíos.
     * 
     * @return Menú de desafíos pendientes
     */
    public String getMenuAprobarDesafios() {
        List<Desafio> desafios = fileManager.cargarDesafiosPendientes(usuarios);
        
        StringBuilder menu = new StringBuilder();
        menu.append("=== DESAFÍOS PENDIENTES DE VALIDACIÓN ===\n");
        
        if (desafios.isEmpty()) {
            menu.append("No hay desafíos pendientes de validación.\n");
        } else {
            for (int i = 0; i < desafios.size(); i++) {
                Desafio d = desafios.get(i);
                menu.append((i + 1) + ". Desafío: " + d.getDesafiante().getNick() + 
                        " vs " + d.getDesafiado().getNick() + 
                        " - Oro: " + d.getOroApostado() + "\n");
            }
        }
        
        menu.append("0. Volver\n");
        menu.append("Seleccione un desafío para validar: ");
        
        return menu.toString();
    }

    /**
     * Selecciona un desafío de la lista.
     * 
     * @return Desafío seleccionado o null si hay error
     */
    public Desafio seleccionarDesafio() {
        String opcion = instanceInterface.pedirEntrada();
        List<Desafio> desafios = fileManager.cargarDesafiosPendientes(usuarios);
        
        try {
            int index = Integer.parseInt(opcion);
            if (index == 0) {
                return null; // Volver al menú anterior
            }
            
            if (index > 0 && index <= desafios.size()) {
                return desafios.get(index - 1);
            } else {
                instanceInterface.mostrar("Opción inválida.");
                return null;
            }
        } catch (NumberFormatException e) {
            instanceInterface.mostrar("Por favor, ingrese un número válido.");
            return null;
        }
    }

    /**
     * Valida un desafío específico.
     * 
     * @param desafio Desafío a validar
     * @return Mensaje de resultado
     */
    public String validarDesafio(Desafio desafio) {
        if (desafio == null) {
            return "Error: Desafío no encontrado.";
        }
        
        desafio.setEstado(E_EstadoDesafio.VALIDADO);
        fileManager.guardarDesafio(desafio);
        
        return "Desafío validado correctamente.";
    }

    /**
     * Obtiene el menú de gestión de desafíos.
     * 
     * @return Menú de desafíos
     */
    public String getMenuDesafio() {
        StringBuilder menu = new StringBuilder();
        menu.append("=== GESTIÓN DE DESAFÍOS ===\n");
        menu.append("1. Ver desafíos pendientes\n");
        menu.append("2. Ver desafíos aprobados\n");
        menu.append("3. Ver desafíos rechazados\n");
        menu.append("0. Volver\n");
        menu.append("Seleccione una opción: ");
        
        return menu.toString();
    }

    /**
     * Selecciona un tipo de jugador.
     * 
     * @return Tipo de jugador seleccionado o null si hay error
     */
    public String seleccionarTipoJugador() {
        StringBuilder menu = new StringBuilder();
        menu.append("=== SELECCIONAR TIPO DE JUGADOR ===\n");
        menu.append("1. Vampiro\n");
        menu.append("2. Licántropo\n");
        menu.append("3. Cazador\n");
        menu.append("0. Volver\n");
        menu.append("Seleccione un tipo: ");
        
        instanceInterface.mostrar(menu.toString());
        String opcion = instanceInterface.pedirEntrada();
        
        switch (opcion) {
            case "1": return "Vampiro";
            case "2": return "Licantropo";
            case "3": return "Cazador";
            case "0": return null;
            default:
                instanceInterface.mostrar("Opción inválida.");
                return null;
        }
    }

    public Personaje seleccionarPersonaje() {
        // Primero, seleccionamos un jugador
        instanceInterface.mostrar("Seleccione un jugador:");
        List<Jugador> jugadores = fileManager.cargarJugadoresActivos();
        
        for (int i = 0; i < jugadores.size(); i++) {
            instanceInterface.mostrar((i + 1) + ". " + jugadores.get(i).getNick());
        }
        
        instanceInterface.mostrar("0. Volver");
        String opcionJugador = instanceInterface.pedirEntrada();
        
        try {
            int indexJugador = Integer.parseInt(opcionJugador);
            
            if (indexJugador == 0) {
                return null;
            }
            
            if (indexJugador > 0 && indexJugador <= jugadores.size()) {
                Jugador jugador = jugadores.get(indexJugador - 1);
                
                // Ahora, cargamos los personajes del jugador usando FileManager
                List<Personaje> personajes = fileManager.cargarPersonajesUsuario(jugador.getNick());
                
                if (personajes.isEmpty()) {
                    instanceInterface.mostrar("Este jugador no tiene personajes.");
                    return null;
                }
                
                instanceInterface.mostrar("Seleccione un personaje:");
                for (int i = 0; i < personajes.size(); i++) {
                    instanceInterface.mostrar((i + 1) + ". " + personajes.get(i).getNombre());
                }
                
                instanceInterface.mostrar("0. Volver");
                String opcionPersonaje = instanceInterface.pedirEntrada();
                
                try {
                    int indexPersonaje = Integer.parseInt(opcionPersonaje);
                    
                    if (indexPersonaje == 0) {
                        return null;
                    }
                    
                    if (indexPersonaje > 0 && indexPersonaje <= personajes.size()) {
                        return personajes.get(indexPersonaje - 1);
                    } else {
                        instanceInterface.mostrar("Opción inválida.");
                        return null;
                    }
                } catch (NumberFormatException e) {
                    instanceInterface.mostrar("Por favor, ingrese un número válido.");
                    return null;
                }
            } else {
                instanceInterface.mostrar("Opción inválida.");
                return null;
            }
        } catch (NumberFormatException e) {
            instanceInterface.mostrar("Por favor, ingrese un número válido.");
            return null;
        }
    }
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
     * 
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
     * 
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
     * 
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
     * 
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
     * 
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
     * 
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
     * 
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
     * 
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
                personaje.desequiparArma(arma);
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
     * 
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
     * 
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
     * 
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
     * 
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
     * 
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
     * 
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
     * 
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
     * 
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