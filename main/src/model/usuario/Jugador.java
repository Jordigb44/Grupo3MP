package model.usuario;
import java.util.List;
import model.personaje.*;
import storage.FileManager;
import storage.XMLStorage;
import ui.A_Interfaz;
import model.desafio.*;
import model.personaje.habilidad.*;

public class Jugador extends Usuario {
    public Jugador(String nick, String nombre, String password, String rol, String estado) {
		super(nick, nombre, password, rol, estado);
	}

    private int puntos;         
	private List<Personaje> personajes;
    private Desafio desafio;
    private int posicionRanking;
    private int oro;    
    
    private A_Interfaz interfaz;
    private FileManager fileManager;
    
    public Jugador(A_Interfaz interfaz, FileManager fileManager, String nick, String nombre, 
                  String password, String rol, String estado, List<Personaje> personajes, 
                  Desafio desafio, int posicionRanking, int oro) {
        super(nick, nombre, password, rol, estado);
        this.personajes = personajes;
        this.desafio = desafio;
        this.posicionRanking = posicionRanking;
        this.oro = oro;
        
        this.interfaz = interfaz;
        this.fileManager = fileManager;
    }
  
    public Jugador getJugador() {
        return this;
    }

    public void getMenu() {
        String opcion = " ";

        do {
            this.interfaz.mostrar("=== MENÚ DE JUGADOR ===");
            this.interfaz.mostrar("1. Agregar personaje");
            this.interfaz.mostrar("2. Borrar personaje");
            this.interfaz.mostrar("3. Equipar armadura");
            this.interfaz.mostrar("4. Equipar arma");
            this.interfaz.mostrar("5. Iniciar desafío");
            this.interfaz.mostrar("6. Consulta ranking general");
            this.interfaz.mostrar("7. Ver oro disponible");
            this.interfaz.mostrar("8. Salir");

            opcion = this.interfaz.pedirEntrada();

            switch (opcion) {
                case "1":
                    this.agregarPersonaje();
                    break;

                case "2":
                    if (personajes.isEmpty()) {
                        interfaz.mostrar("⚠️ No tienes personajes para borrar.");
                        break;
                    }

                    interfaz.mostrar("Seleccione el número del personaje a borrar:");
                    for (int i = 0; i < personajes.size(); i++) {
                        interfaz.mostrar((i + 1) + ". " + personajes.get(i).getNombre());
                    }

                    String sel = interfaz.pedirEntrada();
                    try {
                        int idx = Integer.parseInt(sel) - 1;
                        if (idx >= 0 && idx < personajes.size()) {
                            borrarPersonaje(personajes.get(idx));
                        } else {
                            interfaz.mostrar("⚠️ Selección inválida.");
                        }
                    } catch (NumberFormatException e) {
                        interfaz.mostrar("⚠️ Entrada no válida.");
                    }
                    break;

                case "3":
                    equiparArmaduraAPersonaje();
                    break;


                case "4":
                    equiparArmaAPersonaje();
                    break;


//                case "5":
//                    getMenuInicioDesafio(this.desafio);
//                    break;
//
                case "6":
                    consultarRankingGeneral();
                    break;

                case "7":
                    interfaz.mostrar("Oro total: " + getOro());
                    break;

                case "8":
                    interfaz.mostrar("Saliendo del menú...");
                    break;

                default:
                    interfaz.mostrar("Opción no válida.");
            }

        } while (!opcion.equals("8"));
    }


    
    
    
    public List<Personaje> getPersonajes() {
        return this.personajes;
    }
    
    public Personaje getPersonaje() {
        if (personajes == null || personajes.isEmpty()) {
            return null;
        }
        return personajes.get(0);
    }
    
    public void agregarPersonaje() {
        this.interfaz.mostrar("Ingrese el nombre del nuevo personaje:");
        String nombre = this.interfaz.pedirEntrada();

        this.interfaz.mostrar("Ingrese la salud inicial del personaje:");
        String saludStr = this.interfaz.pedirEntrada();

        try {
            int salud = Integer.parseInt(saludStr);
            Personaje nuevo = new Personaje(nombre, salud);
            this.personajes.add(nuevo);
            this.interfaz.mostrar("✅ Personaje agregado exitosamente: " + nombre);
        } catch (NumberFormatException e) {
            this.interfaz.mostrar("⚠️ Salud inválida. El personaje no fue creado.");
        }
    }

    
    public void borrarPersonaje(Personaje personaje) {
        if (personaje != null && this.personajes.contains(personaje)) {
            this.personajes.remove(personaje);
            interfaz.mostrar("✅ Personaje borrado exitosamente");
        } else {
            this.interfaz.mostrar("⚠️ No hay personajes disponibles que borrar");
        }
    }

    
    public void equiparArmaduraAPersonaje() {
        if (personajes.isEmpty()) {
            interfaz.mostrar("⚠️ No tienes personajes.");
            return;
        }

        interfaz.mostrar("Selecciona un personaje:");
        for (int i = 0; i < personajes.size(); i++) {
            interfaz.mostrar((i + 1) + ". " + personajes.get(i).getNombre());
        }

        String selPers = interfaz.pedirEntrada();
        try {
            int idxPers = Integer.parseInt(selPers) - 1;
            if (idxPers >= 0 && idxPers < personajes.size()) {
                Personaje p = personajes.get(idxPers);
                List<Armadura> armaduras = p.getArmaduras();

                if (armaduras.isEmpty()) {
                    interfaz.mostrar("⚠️ Este personaje no tiene armaduras.");
                    return;
                }

                interfaz.mostrar("Selecciona una armadura:");
                for (int j = 0; j < armaduras.size(); j++) {
                    interfaz.mostrar((j + 1) + ". " + armaduras.get(j).getNombre());
                }

                String selArm = interfaz.pedirEntrada();
                int idxArm = Integer.parseInt(selArm) - 1;

                if (idxArm >= 0 && idxArm < armaduras.size()) {
                    p.equiparArmadura(armaduras.get(idxArm));
                    interfaz.mostrar("✅ Armadura equipada.");
                } else {
                    interfaz.mostrar("⚠️ Selección inválida.");
                }
            } else {
                interfaz.mostrar("⚠️ Selección de personaje inválida.");
            }
        } catch (NumberFormatException e) {
            interfaz.mostrar("⚠️ Entrada inválida.");
        }
    }

    public void equiparArmaAPersonaje() {
        if (personajes.isEmpty()) {
            interfaz.mostrar("⚠️ No tienes personajes.");
            return;
        }

        interfaz.mostrar("Selecciona un personaje:");
        for (int i = 0; i < personajes.size(); i++) {
            interfaz.mostrar((i + 1) + ". " + personajes.get(i).getNombre());
        }

        String selP = interfaz.pedirEntrada();
        try {
            int idxP = Integer.parseInt(selP) - 1;
            if (idxP >= 0 && idxP < personajes.size()) {
                Personaje p = personajes.get(idxP);
                List<Arma> armas = p.getArmas();

                if (armas.isEmpty()) {
                    interfaz.mostrar("⚠️ Este personaje no tiene armas.");
                    return;
                }

                interfaz.mostrar("Selecciona un arma:");
                for (int j = 0; j < armas.size(); j++) {
                    interfaz.mostrar((j + 1) + ". " + armas.get(j).getNombre());
                }

                String selArma = interfaz.pedirEntrada();
                int idxArma = Integer.parseInt(selArma) - 1;

                if (idxArma >= 0 && idxArma < armas.size()) {
                    p.equiparArma(armas.get(idxArma));
                    interfaz.mostrar("✅ Arma equipada.");
                } else {
                    interfaz.mostrar("⚠️ Selección inválida.");
                }
            } else {
                interfaz.mostrar("⚠️ Selección de personaje inválida.");
            }
        } catch (NumberFormatException e) {
            interfaz.mostrar("⚠️ Entrada inválida.");
        }
    }

 

	public Personaje seleccionarPersonaje(List<Personaje> personajes) {
	    if (personajes == null || personajes.isEmpty()) {
	        interfaz.mostrar("⚠️ No hay personajes disponibles.");
	        return null;
	    }
	
	    interfaz.mostrar("Seleccione el número del personaje:");
	    for (int i = 0; i < personajes.size(); i++) {
	        interfaz.mostrar((i + 1) + ". " + personajes.get(i).getNombre());
	    }
	
	    String sel = interfaz.pedirEntrada();
	    try {
	        int idx = Integer.parseInt(sel) - 1;
	        if (idx >= 0 && idx < personajes.size()) {
	            return personajes.get(idx);
	        } else {
	            interfaz.mostrar("⚠️ Selección inválida.");
	            return null;
	        }
	    } catch (NumberFormatException e) {
	        interfaz.mostrar("⚠️ Entrada no válida.");
	        return null;
	    }
	}
    
    public Desafio getDesafio() {
        return this.desafio;
    }
    
    public void setDesafio(Desafio desafio) {
        this.desafio = desafio;
    }
    
    public int getPosicionRanking() {
        return this.posicionRanking;
    }
    
    public void setPosicionRanking(int posicion) {
        this.posicionRanking = posicion;
    }
    
    public int getPuntos() {
        return this.puntos;
    }
    
    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
    
    public void sumarPuntos(int puntos) {
        this.puntos += puntos;
    }
    
    public int getOro() {
        return this.oro;
    }
    
    public void setOro(int oro) {
        this.oro = oro;
    }
    
    public void sumarOro(int cantidad) {
        this.oro += cantidad;
    }
    
    public void restarOro(int penalizacion) {
        if (penalizacion > this.oro) {
            this.oro = 0;
            interfaz.mostrar("Se ha deducido todo tu oro disponible");
        } else {
            this.oro -= penalizacion;
            interfaz.mostrar("Se ha deducido " + penalizacion + " de oro");
        }
    }
    
    public void aceptarDesafio(Desafio desafio) {
        this.desafio = desafio;
        interfaz.mostrar("Desafío aceptado");
    }
    
    public void rechazarDesafio() {
        this.desafio = null;
        interfaz.mostrar("Desafío rechazado");
    }
    
    public void consultarRankingGeneral() {
        try {
            XMLStorage xmlStorage = XMLStorage.cargarRanking();
            
            Ranking ranking = xmlStorage.cargarRanking();
            
            if (ranking == null) {
                System.out.println("No se pudo cargar el ranking.");
                return;
            }         
            List<Jugador> jugadores = xmlStorage.cargarUsuarios();         
            if (jugadores == null || jugadores.isEmpty()) {
                System.out.println("No hay jugadores registrados en el sistema.");
                return;
            }
            
            ranking.consultarRanking(jugadores);
            
        } catch (Exception e) {
            System.out.println("Error al consultar el ranking general: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
