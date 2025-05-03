package model.usuario;
import java.util.ArrayList;
import java.util.List;
import model.personaje.*;
import storage.FileManager;
import storage.XMLStorage;
import ui.A_Interfaz;
import model.Ranking;
import model.desafio.*;
import model.personaje.habilidad.*;

public class Jugador extends Usuario {
    private int puntos;
	private List<Personaje> personajes;
    private Desafio desafio;
    private int posicionRanking;
    private int oro;

    private A_Interfaz interfaz;
    private FileManager fileManager;
    private Usuario usuario;
    private List<Usuario> usuarios;
   
    
    public Jugador(Usuario usuario, List<Personaje> personajes, Desafio desafio, List<Usuario> usuarios) {
        super(usuario);
        this.personajes = personajes;
        this.desafio = desafio;
        this.usuarios = usuarios;
    }


	public void setInterfaz(A_Interfaz interfaz) {
    	this.interfaz = interfaz;    	
    }
    
    public void setFileManger(FileManager fileManager) {
    	this.fileManager = fileManager;    	
    }

    public Jugador getJugador() {
        return this;
    }
    
    public void getDesafioMenu() {
    	if (this.desafio !=  null) {
    		this.desafio.setFileManager(this.fileManager);
    		this.interfaz.mostrar("=== Tienes un desafio nuevo ===");
            this.interfaz.mostrar("Desafiante: "+ this.desafio.getDesafiante());
            this.interfaz.mostrar("Oro apostado: "+ this.desafio.getOroApostado());
            this.interfaz.mostrar("¿Quieres aceptar el Desafio?:");
            this.interfaz.mostrar("1. Si");
            this.interfaz.mostrar("2. No");
            String opcion = this.interfaz.pedirEntrada();

            switch (opcion) {
                case "1":
                	this.desafio.Aceptar(desafio);
                    break;
                default:
                	this.desafio.Rechazar();
                	break;
            }
    	}
	}

    public void getMenu() {
        String opcion = " ";

        do {
            this.interfaz.mostrar("=== MENÚ DE JUGADOR ===");
            this.interfaz.mostrar("1. Agregar personaje");
            this.interfaz.mostrar("2. Borrar personaje");
            this.interfaz.mostrar("3. Equipar armadura");
            this.interfaz.mostrar("4. Equipar arma");
            this.interfaz.mostrar("5. Crear un desafío nuevo");
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
    
    // --- REVISAR
    public void agregarPersonaje() {
        try {
            this.interfaz.mostrar("Ingrese el tipo de personaje (vampiro/licantropo/cazador):");
            String tipo = this.interfaz.pedirEntrada().trim().toLowerCase();

            this.interfaz.mostrar("Ingrese el nombre del nuevo personaje:");
            String nombre = this.interfaz.pedirEntrada();

            // Simulación de selección de elementos
            List<Arma> armas = seleccionarArmas();
            List<Armadura> armaduras = seleccionarArmaduras();
            List<Esbirro> esbirros = seleccionarEsbirros();
            List<Fortaleza> fortalezas = seleccionarFortalezas();
            List<Debilidad> debilidades = seleccionarDebilidades();

            // Establecer arma activa como la primera si existe
            List<Arma> armaActiva = armas.isEmpty() ? new ArrayList<>() : List.of(armas.get(0));

            // Establecer armadura activa como la primera si existe
            Armadura armaduraActiva = armaduras.isEmpty() ? null : armaduras.get(0);

            // Crear personaje usando el Builder
            Builder builder = new Builder(fileManager, tipo, nombre, armaActiva, armaduraActiva, armas, armaduras, esbirros, fortalezas, debilidades);
            Personaje nuevo = builder.getPersonaje();

            this.personajes.add(nuevo);
            this.interfaz.mostrar("✅ Personaje agregado exitosamente: " + nombre);

        } catch (Exception e) {
            this.interfaz.mostrar("⚠️ Error al crear el personaje: " + e.getMessage());
        }
    }
    private List<Arma> seleccionarArmas() {
        List<Arma> disponibles = fileManager.cargarArmas();
        List<Arma> seleccionadas = new ArrayList<>();

        interfaz.mostrar("Seleccione armas disponibles (separadas por coma):");
        for (int i = 0; i < disponibles.size(); i++) {
            Arma arma = disponibles.get(i);
            interfaz.mostrar(i + ". " + arma.getNombre() + " (Ataque: " + arma.getAtaque() + ", Manos: " + arma.getManos() + ")");
        }

        String entrada = interfaz.pedirEntrada();
        for (String indiceStr : entrada.split(",")) {
            try {
                int idx = Integer.parseInt(indiceStr.trim());
                if (idx >= 0 && idx < disponibles.size()) {
                    seleccionadas.add(disponibles.get(idx));
                }
            } catch (NumberFormatException ignored) {}
        }

        return seleccionadas;
    }

    private List<Armadura> seleccionarArmaduras() {
        List<Armadura> disponibles = fileManager.cargarArmaduras();
        List<Armadura> seleccionadas = new ArrayList<>();

        interfaz.mostrar("Seleccione armaduras disponibles (separadas por coma):");
        for (int i = 0; i < disponibles.size(); i++) {
            Armadura a = disponibles.get(i);
            interfaz.mostrar(i + ". " + a.getNombre() + " (Defensa: " + a.getDefensa() + ")");
        }

        String entrada = interfaz.pedirEntrada();
        for (String indiceStr : entrada.split(",")) {
            try {
                int idx = Integer.parseInt(indiceStr.trim());
                if (idx >= 0 && idx < disponibles.size()) {
                    seleccionadas.add(disponibles.get(idx));
                }
            } catch (NumberFormatException ignored) {}
        }

        return seleccionadas;
    }

    private List<Esbirro> seleccionarEsbirros() {
        List<Esbirro> disponibles = fileManager.cargarEsbirrosDisponibles();
        List<Esbirro> seleccionados = new ArrayList<>();

        interfaz.mostrar("Seleccione esbirros disponibles (separados por coma):");
        for (int i = 0; i < disponibles.size(); i++) {
            Esbirro e = disponibles.get(i);
            interfaz.mostrar(i + ". " + e.getNombre());
        }

        String entrada = interfaz.pedirEntrada();
        for (String indiceStr : entrada.split(",")) {
            try {
                int idx = Integer.parseInt(indiceStr.trim());
                if (idx >= 0 && idx < disponibles.size()) {
                    seleccionados.add(disponibles.get(idx));
                }
            } catch (NumberFormatException ignored) {}
        }

        return seleccionados;
    }

    private List<Fortaleza> seleccionarFortalezas() {
        List<Fortaleza> disponibles = fileManager.cargarFortalezasDisponibles();
        List<Fortaleza> seleccionadas = new ArrayList<>();

        interfaz.mostrar("Seleccione fortalezas disponibles (separadas por coma):");
        for (int i = 0; i < disponibles.size(); i++) {
            Fortaleza f = disponibles.get(i);
            interfaz.mostrar(i + ". " + f.getNombre() + " (Nivel: " + f.getNivel() + ")");
        }

        String entrada = interfaz.pedirEntrada();
        for (String indiceStr : entrada.split(",")) {
            try {
                int idx = Integer.parseInt(indiceStr.trim());
                if (idx >= 0 && idx < disponibles.size()) {
                    seleccionadas.add(disponibles.get(idx));
                }
            } catch (NumberFormatException ignored) {}
        }

        return seleccionadas;
    }

    private List<Debilidad> seleccionarDebilidades() {
        List<Debilidad> disponibles = fileManager.cargarDebilidadesDisponibles();
        List<Debilidad> seleccionadas = new ArrayList<>();

        interfaz.mostrar("Seleccione debilidades disponibles (separadas por coma):");
        for (int i = 0; i < disponibles.size(); i++) {
            Debilidad d = disponibles.get(i);
            interfaz.mostrar(i + ". " + d.getNombre() + " (Nivel: " + d.getNivel() + ")");
        }

        String entrada = interfaz.pedirEntrada();
        for (String indiceStr : entrada.split(",")) {
            try {
                int idx = Integer.parseInt(indiceStr.trim());
                if (idx >= 0 && idx < disponibles.size()) {
                    seleccionadas.add(disponibles.get(idx));
                }
            } catch (NumberFormatException ignored) {}
        }

        return seleccionadas;
    }
    
    // --- FIN Revisar ----

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

    public Integer getPuntos() {
        return this.puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void sumarPuntos(int puntos) {
        this.puntos += puntos;
    }

    public Integer getOro() {
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
        interfaz.mostrar("Desafío aceptado. Desafiante: "+ this.desafio.getDesafiante() + " Vs Desafiado: " + this.desafio.getDesafiado());
    }

    public void rechazarDesafio() {
        this.desafio = null;
        interfaz.mostrar("Desafío rechazado. Desafiante: "+ this.desafio.getDesafiante() + " Vs Desafiado: " + this.desafio.getDesafiado());
    }
    
    public void consultarRankingGeneral() {
        try {
            Ranking ranking = new Ranking(this.usuarios, this.interfaz);
            // Muestra el ranking
            ranking.consultarRanking(usuarios);

        } catch (Exception e) {
            System.out.println("Error al consultar el ranking general: " + e.getMessage());
            e.printStackTrace();
        }
    }


	public void setPersonajes(List<Personaje> personajes2) {
		this.personajes = personajes2;
	}

}
