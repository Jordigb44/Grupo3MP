package model.usuario;

import java.util.ArrayList;
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
 * The Operator class represents users with special permissions within the system.
 * It allows for managing players, characters, challenges, and various game elements.
 */
public class Operador extends Usuario {
    // Main attribute
    private FileManager fileManager;
    private A_Interfaz instanceInterface;
    private List<Usuario> usuarios;

    /**
     * Constructor for the Operator class.
     * 
     * @param interfaz    Interface to interact with the user
     * @param fileManager File manager for persistence
     * @param nick        Operator's nickname
     * @param nombre      Operator's full name
     * @param password    Operator's password
     */
    public Operador(A_Interfaz interfaz, FileManager fileManager, String nick, String nombre, String password) {
        super(nick, nombre, password, "operador", "activo");
        this.fileManager = fileManager;
        this.instanceInterface = interfaz;
        this.usuarios = this.fileManager.cargarUsuarios();
    }

    /**
     * Gets the main menu for the operator.
     * 
     * @return Main menu formatted as a string
     */
    public void getMenu() {
    	this.instanceInterface.limpiarPantalla();
    	
        StringBuilder menu = new StringBuilder();
        menu.append("=== OPERATOR MENU ===\n");
        menu.append("1. Player Management\n");
        menu.append("2. Challenge Management\n");
        menu.append("3. Character Management\n");
        menu.append("0. Exit\n");
        menu.append("Select an option: ");
        
        this.instanceInterface.mostrar(menu.toString());
        String option = this.instanceInterface.pedirEntrada();
        
//        TODO: IF-ELSE
//        Exit, tienes que llamar a sistema el mtodo CERRAR
    }

    /**
     * Gets the list of unblocked players.
     * 
     * @return Menu with the list of unblocked players
     */
    public String getMenuJugadoresSinBloquear() {
        List<Jugador> jugadores = this.fileManager.cargarJugadoresSinBloquear(usuarios);
        
        StringBuilder menu = new StringBuilder();
        menu.append("=== UNBLOCKED PLAYERS ===\n");
        
        if (jugadores.isEmpty()) {
            menu.append("There are no unblocked players.\n");
        } else {
            for (int i = 0; i < jugadores.size(); i++) {
                menu.append((i + 1) + ". " + jugadores.get(i).getNick() + " (" + jugadores.get(i).getNombre() + ")\n");
            }
        }
        
        menu.append("0. Back\n");
        menu.append("Select a player to block: ");
        
        return menu.toString();
    }

    /**
     * Blocks a specific player.
     * 
     * @param jugador Player to block
     * @return Result message
     */
    public String bloquearJugador(Jugador jugador) {
        if (jugador == null) {
            return "Error: Player not found.";
        }
        
        jugador.setEstado("bloqueado");
//        TO-DO: WHEN actualizarJugador(Usuario Jugador) esté en FileManager.java, llamarlo para actualizar el estado de ese jugador y no crear un nuevo jugador
        fileManager.actualizarJugador(jugador);
        
        return "Player " + jugador.getNick() + " successfully blocked.";
    }

    /**
     * Gets the list of blocked players.
     * 
     * @return Menu with the list of blocked players
     */
    public String getMenuJugadoresBloqueados() {
        List<Jugador> jugadores = this.fileManager.cargarJugadoresBloqueados(usuarios);
        
        StringBuilder menu = new StringBuilder();
        menu.append("=== BLOCKED PLAYERS ===\n");
        
        if (jugadores.isEmpty()) {
            menu.append("There are no blocked players.\n");
        } else {
            for (int i = 0; i < jugadores.size(); i++) {
                menu.append((i + 1) + ". " + jugadores.get(i).getNick() + " (" + jugadores.get(i).getNombre() + ")\n");
            }
        }
        
        menu.append("0. Back\n");
        menu.append("Select a player to unblock: ");
        
        return menu.toString();
    }

    /**
     * Unblocks a specific player.
     * 
     * @param jugador Player to unblock
     * @return Result message
     */
    public String desbloquearJugador(Jugador jugador) {
        if (jugador == null) {
            return "Error: Player not found.";
        }
        
        jugador.setEstado("activo");
        fileManager.guardarUsuario(jugador);
        
        return "Player " + jugador.getNick() + " successfully unblocked.";
    }

    /**
     * Gets the challenge approval menu.
     * 
     * @return Menu of pending challenges
     */
    public String getMenuAprobarDesafios() {
        List<Desafio> desafios = fileManager.cargarDesafiosPendientes();
        
        StringBuilder menu = new StringBuilder();
        menu.append("=== PENDING CHALLENGES FOR VALIDATION ===\n");
        
        if (desafios.isEmpty()) {
            menu.append("There are no pending challenges for validation.\n");
        } else {
            for (int i = 0; i < desafios.size(); i++) {
                Desafio d = desafios.get(i);
                menu.append((i + 1) + ". Challenge: " + d.getDesafiante().getNick() + 
                        " vs " + d.getDesafiado().getNick() + 
                        " - Gold: " + d.getOroApostado() + "\n");
            }
        }
        
        menu.append("0. Back\n");
        menu.append("Select a challenge to validate: ");
        
        return menu.toString();
    }

    /**
     * Selects a challenge from the list.
     * 
     * @return Selected challenge or null if there's an error
     */
    public Desafio seleccionarDesafio() {
        String opcion = instanceInterface.pedirEntrada();
        List<Desafio> desafios = fileManager.cargarDesafiosPendientes();
        
        try {
            int index = Integer.parseInt(opcion);
            if (index == 0) {
                return null; // Return to previous menu
            }
            
            if (index > 0 && index <= desafios.size()) {
                return desafios.get(index - 1);
            } else {
                instanceInterface.mostrar("Invalid option.");
                return null;
            }
        } catch (NumberFormatException e) {
            instanceInterface.mostrar("Please enter a valid number.");
            return null;
        }
    }

    /**
     * Validates a specific challenge.
     * 
     * @param desafio Challenge to validate
     * @return Result message
     */
    public String validarDesafio(Desafio desafio) {
        if (desafio == null) {
            return "Error: Challenge not found.";
        }
        
        desafio.setEstado(E_EstadoDesafio.VALIDADO);
        fileManager.guardarDesafio(desafio);
        
        return "Challenge successfully validated.";
    }

    /**
     * Gets the challenge management menu.
     * 
     * @return Challenge menu
     */
    public String getMenuDesafio() {
        StringBuilder menu = new StringBuilder();
        menu.append("=== CHALLENGE MANAGEMENT ===\n");
        menu.append("1. View pending challenges\n");
        menu.append("2. View approved challenges\n");
        menu.append("3. View rejected challenges\n");
        menu.append("0. Back\n");
        menu.append("Select an option: ");
        
        return menu.toString();
    }

    /**
     * Selects a player type.
     * 
     * @return Selected player type or null if there's an error
     */
    public String seleccionarTipoJugador() {
        StringBuilder menu = new StringBuilder();
        menu.append("=== SELECT PLAYER TYPE ===\n");
        menu.append("1. Vampire\n");
        menu.append("2. Werewolf\n");
        menu.append("3. Hunter\n");
        menu.append("0. Back\n");
        menu.append("Select a type: ");
        
        instanceInterface.mostrar(menu.toString());
        String opcion = instanceInterface.pedirEntrada();
        
        switch (opcion) {
            case "1": return "Vampiro";
            case "2": return "Licantropo";
            case "3": return "Cazador";
            case "0": return null;
            default:
                instanceInterface.mostrar("Invalid option.");
                return null;
        }
    }

    /**
     * Selects a character from a player.
     * 
     * @return Selected character or null if there's an error
     */
    public Personaje seleccionarPersonaje() {
        // First, we select a player
        instanceInterface.mostrar("Select a player:");
        List<Jugador> jugadores = fileManager.cargarJugadoresActivos();
        
        for (int i = 0; i < jugadores.size(); i++) {
            instanceInterface.mostrar((i + 1) + ". " + jugadores.get(i).getNick());
        }
        
        instanceInterface.mostrar("0. Back");
        String opcionJugador = instanceInterface.pedirEntrada();
        
        try {
            int indexJugador = Integer.parseInt(opcionJugador);
            
            if (indexJugador == 0) {
                return null;
            }
            
            if (indexJugador > 0 && indexJugador <= jugadores.size()) {
                Jugador jugador = jugadores.get(indexJugador - 1);
                
                // Now, we select a character from the player
                List<Personaje> personajes = jugador.getPersonajes();
                
                instanceInterface.mostrar("Select a character:");
                for (int i = 0; i < personajes.size(); i++) {
                    instanceInterface.mostrar((i + 1) + ". " + personajes.get(i).getNombre());
                }
                
                instanceInterface.mostrar("0. Back");
                String opcionPersonaje = instanceInterface.pedirEntrada();
                
                try {
                    int indexPersonaje = Integer.parseInt(opcionPersonaje);
                    
                    if (indexPersonaje == 0) {
                        return null;
                    }
                    
                    if (indexPersonaje > 0 && indexPersonaje <= personajes.size()) {
                        return personajes.get(indexPersonaje - 1);
                    } else {
                        instanceInterface.mostrar("Invalid option.");
                        return null;
                    }
                } catch (NumberFormatException e) {
                    instanceInterface.mostrar("Please enter a valid number.");
                    return null;
                }
            } else {
                instanceInterface.mostrar("Invalid option.");
                return null;
            }
        } catch (NumberFormatException e) {
            instanceInterface.mostrar("Please enter a valid number.");
            return null;
        }
    }
    
    /**
     * Edits a selected character.
     * 
     * @param personaje Character to edit
     * @return Edited character or null if there's an error
     */
    public Personaje editarPersonaje(Personaje personaje) {
        if (personaje == null) {
            instanceInterface.mostrar("Error: Character not found.");
            return null;
        }
        
        instanceInterface.mostrar(getMenuPersonaje());
        String opcion = instanceInterface.pedirEntrada();
        
        switch (opcion) {
            case "1": // Manage weapons
                gestionarArmas(personaje);
                break;
            case "2": // Manage armors
                gestionarArmaduras(personaje);
                break;
            case "3": // Manage strengths
                gestionarFortalezas(personaje);
                break;
            case "4": // Manage weaknesses
                gestionarDebilidades(personaje);
                break;
            case "5": // Manage minions
                gestionarEsbirros(personaje);
                break;
            case "0": // Return
                return personaje;
            default:
                instanceInterface.mostrar("Invalid option.");
                break;
        }
        
        // Save changes
        fileManager.guardarPersonaje(personaje);
        
        return personaje;
    }

    /**
     * Gets the character management menu.
     * 
     * @return Character menu
     */
    public String getMenuPersonaje() {
        StringBuilder menu = new StringBuilder();
        menu.append("=== CHARACTER EDITION ===\n");
        menu.append("1. Manage weapons\n");
        menu.append("2. Manage armors\n");
        menu.append("3. Manage strengths\n");
        menu.append("4. Manage weaknesses\n");
        menu.append("5. Manage minions\n");
        menu.append("0. Save and return\n");
        menu.append("Select an option: ");
        
        return menu.toString();
    }

    /**
     * Gets the armor management menu.
     * 
     * @return Armor menu
     */
    public String getMenuArmadura() {
        StringBuilder menu = new StringBuilder();
        menu.append("=== ARMOR MANAGEMENT ===\n");
        menu.append("1. Add armor\n");
        menu.append("2. Remove armor\n");
        menu.append("0. Back\n");
        menu.append("Select an option: ");
        
        return menu.toString();
    }

    /**
     * Gets the minion management menu.
     * 
     * @return Minion menu
     */
    public String getMenuEsbirro() {
        StringBuilder menu = new StringBuilder();
        menu.append("=== MINION MANAGEMENT ===\n");
        menu.append("1. Add minion\n");
        menu.append("0. Back\n");
        menu.append("Select an option: ");
        
        return menu.toString();
    }

    /**
     * Gets the strength management menu.
     * 
     * @return Strength menu
     */
    public String getMenuFortalezas() {
        StringBuilder menu = new StringBuilder();
        menu.append("=== STRENGTH MANAGEMENT ===\n");
        menu.append("1. Add strength\n");
        menu.append("2. Remove strength\n");
        menu.append("0. Back\n");
        menu.append("Select an option: ");
        
        return menu.toString();
    }

    /**
     * Gets the weakness management menu.
     * 
     * @return Weakness menu
     */
    public String getMenuDebilidades() {
        StringBuilder menu = new StringBuilder();
        menu.append("=== WEAKNESS MANAGEMENT ===\n");
        menu.append("1. Add weakness\n");
        menu.append("2. Remove weakness\n");
        menu.append("0. Back\n");
        menu.append("Select an option: ");
        
        return menu.toString();
    }

    /**
     * Gets the weapon management menu.
     * 
     * @return Weapon menu
     */
    public String getMenuArma() {
        StringBuilder menu = new StringBuilder();
        menu.append("=== WEAPON MANAGEMENT ===\n");
        menu.append("1. Add weapon\n");
        menu.append("2. Remove weapon\n");
        menu.append("0. Back\n");
        menu.append("Select an option: ");
        
        return menu.toString();
    }

    /**
     * Adds a weapon to a character.
     * 
     * @param personaje Character to add the weapon to
     * @param arma Weapon to add
     * @return Result message
     */
    public String añadirArmaPersonaje(Personaje personaje, Arma arma) {
        if (personaje == null) {
            return "Error: Character not found.";
        }
        
        if (arma == null) {
            return "Error: Weapon not found.";
        }
        
        // Add weapon to character
        // (Here you would normally call a method of the character)
        
        fileManager.guardarPersonaje(personaje);
        
        return "Weapon successfully added to character.";
    }

    /**
     * Removes a weapon from a character.
     * 
     * @param personaje Character to remove the weapon from
     * @return Result message
     */
    public String eliminarArmaPersonaje(Personaje personaje) {
        if (personaje == null) {
            return "Error: Character not found.";
        }
        
        // List of character's weapons to select which one to delete
        // (Here you would normally get the list and ask to select)
        
        // Remove selected weapon from character
        
        fileManager.guardarPersonaje(personaje);
        
        return "Weapon successfully removed from character.";
    }

    /**
     * Adds an armor to a character.
     * 
     * @param personaje Character to add the armor to
     * @param armadura Armor to add
     * @return Result message
     */
    public String añadirArmaduraPersonaje(Personaje personaje, Armadura armadura) {
        if (personaje == null) {
            return "Error: Character not found.";
        }
        
        if (armadura == null) {
            return "Error: Armor not found.";
        }
        
        // Add armor to character
        
        fileManager.guardarPersonaje(personaje);
        
        return "Armor successfully added to character.";
    }

    /**
     * Removes an armor from a character.
     * 
     * @param personaje Character to remove the armor from
     * @return Result message
     */
    public String quitarArmaduraPersonaje(Personaje personaje) {
        if (personaje == null) {
            return "Error: Character not found.";
        }
        
        // Remove armor from character
        
        fileManager.guardarPersonaje(personaje);
        
        return "Armor successfully removed from character.";
    }

    /**
     * Adds a strength to a character.
     * 
     * @param personaje Character to add the strength to
     * @param fortaleza Strength to add
     * @return Result message
     */
    public String añadirFortalezaPersonaje(Personaje personaje, Fortaleza fortaleza) {
        if (personaje == null) {
            return "Error: Character not found.";
        }
        
        if (fortaleza == null) {
            return "Error: Strength not found.";
        }
        
        // Add strength to character
        
        fileManager.guardarPersonaje(personaje);
        
        return "Strength successfully added to character.";
    }

    /**
     * Removes a strength from a character.
     * 
     * @param personaje Character to remove the strength from
     * @param fortaleza Strength to remove
     * @return Result message
     */
    public String quitarFortalezaPersonaje(Personaje personaje, Fortaleza fortaleza) {
        if (personaje == null) {
            return "Error: Character not found.";
        }
        
        if (fortaleza == null) {
            return "Error: Strength not found.";
        }
        
        // Remove strength from character
        
        fileManager.guardarPersonaje(personaje);
        
        return "Strength successfully removed from character.";
    }

    /**
     * Adds a weakness to a character.
     * 
     * @param personaje Character to add the weakness to
     * @param debilidad Weakness to add
     * @return Result message
     */
    public String añadirDebilidadPersonaje(Personaje personaje, Debilidad debilidad) {
        if (personaje == null) {
            return "Error: Character not found.";
        }
        
        if (debilidad == null) {
            return "Error: Weakness not found.";
        }
        
        // Add weakness to character
        
        fileManager.guardarPersonaje(personaje);
        
        return "Weakness successfully added to character.";
    }

    /**
     * Removes a weakness from a character.
     * 
     * @param personaje Character to remove the weakness from
     * @param debilidad Weakness to remove
     * @return Result message
     */
    public String quitarDebilidadPersonaje(Personaje personaje, Debilidad debilidad) {
        if (personaje == null) {
            return "Error: Character not found.";
        }
        
        if (debilidad == null) {
            return "Error: Weakness not found.";
        }
        
        // Remove weakness from character
        
        fileManager.guardarPersonaje(personaje);
        
        return "Weakness successfully removed from character.";
    }

    /**
     * Adds a minion to a character.
     * 
     * @param personaje Character to add the minion to
     * @param esbirro Minion to add
     * @return Result message
     */
    public String añadirEsbirroPersonaje(Personaje personaje, Esbirro esbirro) {
        if (personaje == null) {
            return "Error: Character not found.";
        }
        
        if (esbirro == null) {
            return "Error: Minion not found.";
        }
        
        // Add minion to character
        
        fileManager.guardarPersonaje(personaje);
        
        return "Minion successfully added to character.";
    }

    // Private helper methods to manage character elements

    /**
     * Manages weapons for a character.
     * 
     * @param personaje Character to manage weapons for
     */
    private void gestionarArmas(Personaje personaje) {
        instanceInterface.mostrar(getMenuArma());
        String opcion = instanceInterface.pedirEntrada();
        
        switch (opcion) {
            case "1": // Add weapon
                List<Arma> armasDisponibles = fileManager.cargarArmasDisponibles();
                instanceInterface.mostrar("Select a weapon to add:");
                
                for (int i = 0; i < armasDisponibles.size(); i++) {
                    instanceInterface.mostrar((i + 1) + ". " + armasDisponibles.get(i));
                }
                
                instanceInterface.mostrar("0. Back");
                String opcionArma = instanceInterface.pedirEntrada();
                
                try {
                    int indexArma = Integer.parseInt(opcionArma);
                    
                    if (indexArma > 0 && indexArma <= armasDisponibles.size()) {
                        Arma arma = armasDisponibles.get(indexArma - 1);
                        añadirArmaPersonaje(personaje, arma);
                    }
                } catch (NumberFormatException e) {
                    instanceInterface.mostrar("Please enter a valid number.");
                }
                break;
                
            case "2": // Remove weapon
                eliminarArmaPersonaje(personaje);
                break;
                
            case "0": // Back
                break;
                
            default:
                instanceInterface.mostrar("Invalid option.");
                break;
        }
    }

    /**
     * Manages armors for a character.
     * 
     * @param personaje Character to manage armors for
     */
    private void gestionarArmaduras(Personaje personaje) {
        instanceInterface.mostrar(getMenuArmadura());
        String opcion = instanceInterface.pedirEntrada();
        
        switch (opcion) {
            case "1": // Add armor
                List<Armadura> armadurasDisponibles = fileManager.cargarArmadurasDisponibles();
                instanceInterface.mostrar("Select an armor to add:");
                
                for (int i = 0; i < armadurasDisponibles.size(); i++) {
                    instanceInterface.mostrar((i + 1) + ". " + armadurasDisponibles.get(i));
                }
                
                instanceInterface.mostrar("0. Back");
                String opcionArmadura = instanceInterface.pedirEntrada();
                
                try {
                    int indexArmadura = Integer.parseInt(opcionArmadura);
                    
                    if (indexArmadura > 0 && indexArmadura <= armadurasDisponibles.size()) {
                        Armadura armadura = armadurasDisponibles.get(indexArmadura - 1);
                        añadirArmaduraPersonaje(personaje, armadura);
                    }
                } catch (NumberFormatException e) {
                    instanceInterface.mostrar("Please enter a valid number.");
                }
                break;
                
            case "2": // Remove armor
                quitarArmaduraPersonaje(personaje);
                break;
                
            case "0": // Back
                break;
                
            default:
                instanceInterface.mostrar("Invalid option.");
                break;
        }
    }

    /**
     * Manages strengths for a character.
     * 
     * @param personaje Character to manage strengths for
     */
    private void gestionarFortalezas(Personaje personaje) {
        instanceInterface.mostrar(getMenuFortalezas());
        String opcion = instanceInterface.pedirEntrada();
        
        switch (opcion) {
            case "1": // Add strength
                List<Fortaleza> fortalezasDisponibles = fileManager.cargarFortalezasDisponibles();
                instanceInterface.mostrar("Select a strength to add:");
                
                for (int i = 0; i < fortalezasDisponibles.size(); i++) {
                    instanceInterface.mostrar((i + 1) + ". " + fortalezasDisponibles.get(i));
                }
                
                instanceInterface.mostrar("0. Back");
                String opcionFortaleza = instanceInterface.pedirEntrada();
                
                try {
                    int indexFortaleza = Integer.parseInt(opcionFortaleza);
                    
                    if (indexFortaleza > 0 && indexFortaleza <= fortalezasDisponibles.size()) {
                        Fortaleza fortaleza = fortalezasDisponibles.get(indexFortaleza - 1);
                        añadirFortalezaPersonaje(personaje, fortaleza);
                    }
                } catch (NumberFormatException e) {
                    instanceInterface.mostrar("Please enter a valid number.");
                }
                break;
                
            case "2": // Remove strength
                // Get character's strengths and allow selecting one to remove
                break;
                
            case "0": // Back
                break;
                
            default:
                instanceInterface.mostrar("Invalid option.");
                break;
        }
    }

    /**
     * Manages weaknesses for a character.
     * 
     * @param personaje Character to manage weaknesses for
     */
    private void gestionarDebilidades(Personaje personaje) {
        instanceInterface.mostrar(getMenuDebilidades());
        String opcion = instanceInterface.pedirEntrada();
        
        switch (opcion) {
            case "1": // Add weakness
                List<Debilidad> debilidadesDisponibles = fileManager.cargarDebilidadesDisponibles();
                instanceInterface.mostrar("Select a weakness to add:");
                
                for (int i = 0; i < debilidadesDisponibles.size(); i++) {
                    instanceInterface.mostrar((i + 1) + ". " + debilidadesDisponibles.get(i));
                }
                
                instanceInterface.mostrar("0. Back");
                String opcionDebilidad = instanceInterface.pedirEntrada();
                
                try {
                    int indexDebilidad = Integer.parseInt(opcionDebilidad);
                    
                    if (indexDebilidad > 0 && indexDebilidad <= debilidadesDisponibles.size()) {
                        Debilidad debilidad = debilidadesDisponibles.get(indexDebilidad - 1);
                        añadirDebilidadPersonaje(personaje, debilidad);
                    }
                } catch (NumberFormatException e) {
                    instanceInterface.mostrar("Please enter a valid number.");
                }
                break;
                
            case "2": // Remove weakness
                // Get character's weaknesses and allow selecting one to remove
                break;
                
            case "0": // Back
                break;
                
            default:
                instanceInterface.mostrar("Invalid option.");
                break;
        }
    }

    /**
     * Manages minions for a character.
     * 
     * @param personaje Character to manage minions for
     */
    private void gestionarEsbirros(Personaje personaje) {
        instanceInterface.mostrar(getMenuEsbirro());
        String opcion = instanceInterface.pedirEntrada();
        
        switch (opcion) {
            case "1": // Add minion
                List<Esbirro> esbirrosDisponibles = fileManager.cargarEsbirrosDisponibles();
                instanceInterface.mostrar("Select a minion to add:");
                
                for (int i = 0; i < esbirrosDisponibles.size(); i++) {
                    instanceInterface.mostrar((i + 1) + ". " + esbirrosDisponibles.get(i));
                }
                
                instanceInterface.mostrar("0. Back");
                String opcionEsbirro = instanceInterface.pedirEntrada();
                
                try {
                    int indexEsbirro = Integer.parseInt(opcionEsbirro);
                    
                    if (indexEsbirro > 0 && indexEsbirro <= esbirrosDisponibles.size()) {
                        Esbirro esbirro = esbirrosDisponibles.get(indexEsbirro - 1);
                        añadirEsbirroPersonaje(personaje, esbirro);
                    }
                } catch (NumberFormatException e) {
                    instanceInterface.mostrar("Please enter a valid number.");
                }
                break;
                
            case "0": // Back
                break;
                
            default:
                instanceInterface.mostrar("Invalid option.");
                break;
        }
    }
}