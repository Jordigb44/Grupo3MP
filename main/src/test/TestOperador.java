package test;

import java.util.List;
import java.util.Scanner;
import model.personaje.Esbirro;
import model.personaje.Personaje;
import model.personaje.habilidad.Arma;
import model.personaje.habilidad.Armadura;
import model.personaje.habilidad.Debilidad;
import model.personaje.habilidad.Fortaleza;
import model.usuario.Jugador;
import model.usuario.Operador;
import model.usuario.Usuario;
import storage.FileManager;
import storage.XMLStorage;
import ui.A_Interfaz;

/**
 * Test para la clase Operador que verifica su funcionamiento con archivos XML
 */
public class TestOperador {

    public static void main(String[] args) {
        System.out.println("=== TEST DE LA CLASE OPERADOR ===");
        Scanner scanner = new Scanner(System.in);
        
        try {
            // Inicializar componentes
            System.out.println("Inicializando componentes...");
            A_Interfaz interfaz = new A_Interfaz();
            FileManager fileManager = new FileManager(new XMLStorage("./"));
            
            // Crear un operador para las pruebas
            Usuario usuario = new Usuario(null, "admin", "Administrador", "password123", "operador", "activo", 100, 50);
            Operador operador = new Operador(interfaz, fileManager, usuario);
            System.out.println("Operador creado correctamente");
            
            // Menú principal de pruebas
            boolean salir = false;
            while (!salir) {
                System.out.println("\n=== MENÚ DE PRUEBAS ===");
                System.out.println("1. Verificar generación de menús");
                System.out.println("2. Verificar carga de recursos");
                System.out.println("3. Probar gestión de jugadores");
                System.out.println("4. Probar gestión de personajes");
                System.out.println("5. Probar gestión de desafíos");
                System.out.println("6. Ejecutar menú completo del operador");
                System.out.println("0. Salir");
                System.out.print("Seleccione una opción: ");
                
                String opcion = scanner.nextLine();
                
                switch (opcion) {
                    case "1":
                        verificarMenus(operador);
                        break;
                    case "2":
                        verificarRecursos(fileManager);
                        break;
                    case "3":
                        probarGestionJugadores(operador, fileManager);
                        break;
                    case "4":
                        probarGestionPersonajes(operador, fileManager);
                        break;
                    case "5":
                        probarGestionDesafios(operador, fileManager);
                        break;
                    case "6":
                        System.out.println("\nIniciando menú completo del operador...");
                        operador.getMenu();
                        break;
                    case "0":
                        salir = true;
                        break;
                    default:
                        System.out.println("Opción no válida");
                        break;
                }
            }
            
            System.out.println("\n=== TEST FINALIZADO ===");
            
        } catch (Exception e) {
            System.err.println("Error durante el test: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
    
    /**
     * Verifica que los menús se generen correctamente
     */
    private static void verificarMenus(Operador operador) {
        System.out.println("\n--- Verificación de Menús ---");
        
        try {
            System.out.println("Menú de jugadores sin bloquear: " + 
                (operador.getMenuJugadoresSinBloquear() != null ? "OK" : "ERROR"));
            
            System.out.println("Menú de jugadores bloqueados: " + 
                (operador.getMenuJugadoresBloqueados() != null ? "OK" : "ERROR"));
            
            System.out.println("Menú de desafíos: " + 
                (operador.getMenuDesafio() != null ? "OK" : "ERROR"));
            
            System.out.println("Menú de aprobar desafíos: " + 
                (operador.getMenuAprobarDesafios() != null ? "OK" : "ERROR"));
            
            System.out.println("Menú de armas: " + 
                (operador.getMenuArma() != null ? "OK" : "ERROR"));
            
            System.out.println("Menú de armaduras: " + 
                (operador.getMenuArmadura() != null ? "OK" : "ERROR"));
            
            System.out.println("Menú de fortalezas: " + 
                (operador.getMenuFortalezas() != null ? "OK" : "ERROR"));
            
            System.out.println("Menú de debilidades: " + 
                (operador.getMenuDebilidades() != null ? "OK" : "ERROR"));
            
            System.out.println("Menú de esbirros: " + 
                (operador.getMenuEsbirro() != null ? "OK" : "ERROR"));
            
        } catch (Exception e) {
            System.out.println("Error al verificar menús: " + e.getMessage());
        }
        
        System.out.println("Presione Enter para continuar...");
        new Scanner(System.in).nextLine();
    }
    
    /**
     * Verifica la carga de recursos desde XML
     */
    private static void verificarRecursos(FileManager fileManager) {
        System.out.println("\n--- Verificación de Recursos ---");
        
        try {
            List<Usuario> usuarios = fileManager.cargarUsuarios();
            System.out.println("Usuarios cargados: " + usuarios.size());
            
            List<Personaje> personajes = fileManager.cargarPersonajes();
            System.out.println("Personajes cargados: " + personajes.size());
            
            List<Arma> armas = fileManager.cargarArmasDisponibles();
            System.out.println("Armas cargadas: " + armas.size());
            
            List<Armadura> armaduras = fileManager.cargarArmadurasDisponibles();
            System.out.println("Armaduras cargadas: " + armaduras.size());
            
            List<Fortaleza> fortalezas = fileManager.cargarFortalezasDisponibles();
            System.out.println("Fortalezas cargadas: " + fortalezas.size());
            
            List<Debilidad> debilidades = fileManager.cargarDebilidadesDisponibles();
            System.out.println("Debilidades cargadas: " + debilidades.size());
            
            List<Esbirro> esbirros = fileManager.cargarEsbirrosDisponibles();
            System.out.println("Esbirros cargados: " + esbirros.size());
            
        } catch (Exception e) {
            System.out.println("Error al verificar recursos: " + e.getMessage());
        }
        
        System.out.println("Presione Enter para continuar...");
        new Scanner(System.in).nextLine();
    }
    
    /**
     * Prueba la gestión de jugadores
     */
    private static void probarGestionJugadores(Operador operador, FileManager fileManager) {
        System.out.println("\n--- Prueba de Gestión de Jugadores ---");
        Scanner scanner = new Scanner(System.in);
        
        try {
            // Mostrar menú de jugadores sin bloquear
            System.out.println("\n" + operador.getMenuJugadoresSinBloquear());
            System.out.print("¿Desea bloquear un jugador? (s/n): ");
            String respuesta = scanner.nextLine();
            
            if (respuesta.equalsIgnoreCase("s")) {
                System.out.print("Ingrese el número del jugador: ");
                String indice = scanner.nextLine();
                
                try {
                    int index = Integer.parseInt(indice);
                    List<Jugador> jugadores = fileManager.cargarJugadoresActivos();
                    
                    if (index > 0 && index <= jugadores.size()) {
                        Jugador jugador = jugadores.get(index - 1);
                        String resultado = operador.bloquearJugador(jugador);
                        System.out.println(resultado);
                    } else {
                        System.out.println("Índice fuera de rango");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Por favor ingrese un número válido");
                }
            }
            
            // Mostrar menú de jugadores bloqueados
            System.out.println("\n" + operador.getMenuJugadoresBloqueados());
            System.out.print("¿Desea desbloquear un jugador? (s/n): ");
            respuesta = scanner.nextLine();
            
            if (respuesta.equalsIgnoreCase("s")) {
                System.out.print("Ingrese el número del jugador: ");
                String indice = scanner.nextLine();
                
                try {
                    int index = Integer.parseInt(indice);
                    List<Jugador> jugadores = fileManager.cargarJugadoresBloqueados();
                    
                    if (index > 0 && index <= jugadores.size()) {
                        Jugador jugador = jugadores.get(index - 1);
                        String resultado = operador.desbloquearJugador(jugador);
                        System.out.println(resultado);
                    } else {
                        System.out.println("Índice fuera de rango");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Por favor ingrese un número válido");
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error en la gestión de jugadores: " + e.getMessage());
        }
        
        System.out.println("Presione Enter para continuar...");
        scanner.nextLine();
    }
    
    /**
     * Prueba la gestión de personajes
     */
    private static void probarGestionPersonajes(Operador operador, FileManager fileManager) {
        System.out.println("\n--- Prueba de Gestión de Personajes ---");
        Scanner scanner = new Scanner(System.in);
        
        try {
            // Intentar seleccionar un personaje
            System.out.println("Seleccionando un personaje...");
            Personaje personaje = operador.seleccionarPersonaje();
            
            if (personaje != null) {
                System.out.println("Personaje seleccionado: " + personaje.getNombre());
                System.out.print("¿Desea editar este personaje? (s/n): ");
                String respuesta = scanner.nextLine();
                
                if (respuesta.equalsIgnoreCase("s")) {
                    // Llamar al método de edición
                    operador.editarPersonaje(personaje);
                }
            } else {
                System.out.println("No se seleccionó ningún personaje o no hay personajes disponibles");
            }
            
        } catch (Exception e) {
            System.out.println("Error en la gestión de personajes: " + e.getMessage());
        }
        
        System.out.println("Presione Enter para continuar...");
        scanner.nextLine();
    }
    
    /**
     * Prueba la gestión de desafíos
     */
    private static void probarGestionDesafios(Operador operador, FileManager fileManager) {
        System.out.println("\n--- Prueba de Gestión de Desafíos ---");
        Scanner scanner = new Scanner(System.in);
        
        try {
            // Mostrar desafíos pendientes
            System.out.println("\n" + operador.getMenuAprobarDesafios());
            System.out.print("¿Desea validar un desafío? (s/n): ");
            String respuesta = scanner.nextLine();
            
            if (respuesta.equalsIgnoreCase("s")) {
                // Intentar seleccionar y validar un desafío
                System.out.println("Seleccionando un desafío...");
                System.out.println("Ingrese el número del desafío: ");
                // En este punto, el método seleccionarDesafio de Operador se encargará de solicitar la entrada
                // así que no necesitamos leer aquí
                
                System.out.println("(En el método seleccionarDesafio se pedirá que ingrese el número)");
                
                // Intentar validar el desafío
                operador.validarDesafio(operador.seleccionarDesafio());
            }
            
        } catch (Exception e) {
            System.out.println("Error en la gestión de desafíos: " + e.getMessage());
        }
        
        System.out.println("Presione Enter para continuar...");
        scanner.nextLine();
    }
}