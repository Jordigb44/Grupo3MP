package model.usuario;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
   
    public Jugador(UUID userId, String nick, String nombre, String password, String rol, String estado, int oro, int puntos, List<Personaje> personajes, Desafio desafio) {
        super(userId, nick, nombre, password, rol, estado, oro, puntos);
        this.personajes = personajes;
        this.desafio = desafio;
    }
    
    public void setUsuarios(List<Usuario> usuarios) {	
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
        if (this.desafio != null) {
            this.desafio.setFileManager(this.fileManager);
            this.interfaz.mostrar("=== Tienes un desafío nuevo ===");
            this.interfaz.mostrar("Desafiante: " + this.desafio.getDesafiado().getNick());
            this.interfaz.mostrar("Oro apostado: " + this.desafio.getOroApostado());
            this.interfaz.mostrar("¿Quieres aceptar el Desafío?:");
            this.interfaz.mostrar("1. Si");
            this.interfaz.mostrar("2. No");
            this.interfaz.mostrar("3. Volver atrás");

            String opcion = this.interfaz.pedirEntrada();

            switch (opcion) {
	            case "1":
	            	this.interfaz.mostrar("✅ Has aceptado el desafío.");
	                this.desafio.Aceptar(desafio);
	                break;
	
	            case "2":
	            	this.interfaz.mostrar("❌ Has rechazado el desafío.");
	                this.desafio.Rechazar(this.interfaz);
	                break;
                case "3":
                    return; // Opción para volver atrás al menú anterior
                default:
                    this.interfaz.mostrar("⚠️ Opción no válida.");
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
                	this.personajes = fileManager.cargarPersonajesUsuario(this.nick);
                	if (this.personajes == null) {
                	    this.personajes = new ArrayList<>();
                	}
                    if (personajes.isEmpty()) {
                        interfaz.mostrar("⚠️ No tienes personajes para borrar.");
                        break;
                    }
                    borrarPersonaje();
                    break;

                case "3":
                    equiparArmaduraAPersonaje();
                    break;

                case "4":
                    equiparArmaAPersonaje();
                    break;

                case "5":
                	crearDesafioMenu();
                    // getMenuInicioDesafio(this.desafio);
                    break;

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


	public void borrarPersonaje() {
		interfaz.mostrar("Seleccione el número del personaje a borrar:");
		for (int i = 0; i < this.personajes.size(); i++) {
			this.interfaz.mostrar((i + 1) + ". " + this.personajes.get(i).getNombre());
		}

		String sel = this.interfaz.pedirEntrada();
		try {
		    int idx = Integer.parseInt(sel) - 1;
		    if (idx >= 0 && idx < this.personajes.size()) {
		        borrarPersonaje(this.personajes.get(idx));
		    } else {
		    	this.interfaz.mostrar("⚠️ Selección inválida.");
		    }
		} catch (NumberFormatException e) {
			this.interfaz.mostrar("⚠️ Entrada no válida.");
		}
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
            List<String> tiposDisponibles = null;
            try {
                tiposDisponibles = this.fileManager.getTiposPersonajes();
            } catch (Exception e) {
                this.interfaz.mostrar("Error al obtener tipos de personajes: " + e.getMessage());
                e.printStackTrace();
                return;
            }

            String tipo = "";
            boolean tipoValido = false;

            while (!tipoValido) {
                this.interfaz.mostrar("Ingrese el tipo de personaje (" + String.join("/", tiposDisponibles) + "):");
                tipo = this.interfaz.pedirEntrada().trim().toLowerCase();

                if (tiposDisponibles.contains(tipo)) {
                    tipoValido = true;
                } else {
                    this.interfaz.mostrar("Tipo de personaje no válido. Intenta nuevamente.");
                }
            }

            this.interfaz.mostrar("Ingrese el nombre del nuevo personaje:");
            String nombre = this.interfaz.pedirEntrada();

            List<Arma> armas = new ArrayList<>();
            List<Armadura> armaduras = new ArrayList<>();
            List<Esbirro> esbirros = new ArrayList<>();
            List<Fortaleza> fortalezas = new ArrayList<>();
            List<Debilidad> debilidades = new ArrayList<>();

            try {
                armas = seleccionarArmas();
                armaduras = seleccionarArmaduras();
                esbirros = seleccionarEsbirros();
                fortalezas = seleccionarFortalezas();
                debilidades = seleccionarDebilidades();
            } catch (Exception e) {
                this.interfaz.mostrar("Error al seleccionar atributos del personaje: " + e.getMessage());
                e.printStackTrace();
                return;
            }

            List<Arma> armaActiva = armas.isEmpty() ? new ArrayList<>() : List.of(armas.get(0));
            Armadura armaduraActiva = armaduras.isEmpty() ? null : armaduras.get(0);

            Personaje nuevo = null;
            try {
                Builder builder = new Builder(this.fileManager, this.interfaz, tipo, nombre, armaActiva, armaduraActiva, armas, armaduras, esbirros, fortalezas, debilidades);
                nuevo = builder.getPersonaje();
            	this.interfaz.mostrar("Personaje: " + nuevo.getNombre() + " (Tipo: " + this.getClass().getSimpleName() + ")");
            } catch (Exception e) {
                this.interfaz.mostrar("Error al construir el personaje: " + e.getMessage());
                e.printStackTrace();
                return;
            }
         // ⚠️ Asegura que la lista no sea null
            if (this.personajes == null) {
                this.personajes = new ArrayList<>();
            }
            this.personajes.add(nuevo);
            if (this.fileManager.guardarPersonajesUsuario(this.nick, this.personajes)) {
            	this.interfaz.mostrar("Personaje agregado exitosamente: " + nombre);            	
            } else {
                this.interfaz.mostrar("No se pudo agregar el personaje");
            } 
            

        } catch (Exception e) {
            this.interfaz.mostrar("Error inesperado al agregar el personaje: " + e.getMessage());
            e.printStackTrace();
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
            this.fileManager.eliminarPersonajesUsuario(nick, personaje);
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

        // Mostrar lista de personajes
        interfaz.mostrar("Selecciona un personaje:");
        for (int i = 0; i < personajes.size(); i++) {
            interfaz.mostrar((i + 1) + ". " + personajes.get(i).getNombre());
        }

        // Seleccionar personaje
        String selPers = interfaz.pedirEntrada();
        try {
            int idxPers = Integer.parseInt(selPers) - 1;
            if (idxPers >= 0 && idxPers < personajes.size()) {
                Personaje p = personajes.get(idxPers);
                
                // Obtener armaduras del sistema
                List<Armadura> armadurasDisponibles = fileManager.cargarArmaduras(); 
                
                if (armadurasDisponibles.isEmpty()) {
                    interfaz.mostrar("⚠️ No hay armaduras disponibles en el sistema.");
                    return;
                }

                // Mostrar lista de armaduras disponibles
                interfaz.mostrar("Selecciona una armadura para " + p.getNombre() + ":");
                for (int i = 0; i < armadurasDisponibles.size(); i++) {
                    Armadura a = armadurasDisponibles.get(i);
                    interfaz.mostrar((i + 1) + ". " + a.getNombre() + " (defensa: " + a.getDefensa() + ")");
                }




    		    // Seleccionar armadura
    		    int opcion = -1;
    		    while (opcion < 1 || opcion > armadurasDisponibles.size()) {
    		        interfaz.mostrar("Introduce el numero de la armadura que deseas activar:");
    		        try {
    		            opcion = Integer.parseInt(interfaz.pedirEntrada());
    		            if (opcion < 1 || opcion > armadurasDisponibles.size()) {
    		                interfaz.mostrar("Opcion invalida. Intenta nuevamente.");
    		            }
    		        } catch (NumberFormatException e) {
    		            interfaz.mostrar("Entrada no valida. Debes ingresar un numero.");
    		        }
    		    }
    		    
    		    // Equipar la armadura
    		    p.equiparArmadura(armadurasDisponibles.get(opcion - 1)); //Se resta 1 porque el numero mostrado no corresponde con su numero de la lista
    		    
    		    System.out.println("Nombre: " + p.getNombre());
    		    System.out.println("Oro: " + p.getOro());
    		    // Armas
    		    System.out.println("Armas:");
    		    for (Arma arma : p.getArmas()) {
    		        System.out.println(" - " + arma.getNombre());
    		    }
    		    // Armaduras
    		    System.out.println("Armaduras:");
    		    for (Armadura armadura : p.getArmaduras()) {
    		        System.out.println(" - " + armadura.getNombre());
    		    }
    		    // Fortalezas
    		    System.out.println("Fortalezas:");
    		    for (Fortaleza fortaleza : p.getFortalezas()) {
    		        System.out.println(" - " + fortaleza.getNombre());
    		    }
    		    // Debilidades
    		    System.out.println("Debilidades:");
    		    for (Debilidad debilidad : p.getDebilidades()) {
    		        System.out.println(" - " + debilidad.getNombre());
    		    }
    		    // Esbirros
    		    System.out.println("Esbirros:");
    		    for (Esbirro esbirro : p.getEsbirros()) {
    		        System.out.println(" - " + esbirro.getNombre());
    		    }
    		    
    		    this.fileManager.actualizarPersonajesUsuario(this.getNick(), p);
            } else {
                interfaz.mostrar("⚠️ Selección de personaje invalida.");
            }
        } catch (NumberFormatException e) {
            interfaz.mostrar("⚠️ Entrada invalida.");
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
            
            // Obtenemos todas las armas disponibles
            List<Arma> armasDisponibles = fileManager.cargarArmas(); // Método de FileManager
            
            if (armasDisponibles.isEmpty()) {
                interfaz.mostrar("⚠️ No hay armas disponibles en el sistema.");
                return;
            }
            
            // Mostrar lista de armas disponibles
            interfaz.mostrar("Selecciona un arma para equipar:");
            for (int i = 0; i < armasDisponibles.size(); i++) {
                Arma a = armasDisponibles.get(i);
                interfaz.mostrar((i + 1) + ". " + a.getNombre() + " (ataque: " + a.getAtaque() + ", manos: " + a.getManos() + ")");
            }

            // Seleccionar arma
            int opcion = -1;
            while (opcion < 1 || opcion > armasDisponibles.size()) {
                interfaz.mostrar("Introduce el número del arma que deseas equipar:");
                try {
                    opcion = Integer.parseInt(interfaz.pedirEntrada());
                    if (opcion < 1 || opcion > armasDisponibles.size()) {
                        interfaz.mostrar("Opción inválida. Intenta nuevamente.");
                    }
                } catch (NumberFormatException e) {
                    interfaz.mostrar("Entrada no válida. Debes ingresar un número.");
                }
            }

            // Equipar el arma seleccionada
            Arma armaSeleccionada = armasDisponibles.get(opcion - 1);
            p.equiparArma(armaSeleccionada);
            
            // Mensaje de confirmación
            interfaz.mostrar("✅ ¡" + armaSeleccionada.getNombre() + " equipada con éxito a " + 
                          p.getNombre() + "! Ataque: +" + armaSeleccionada.getAtaque());
 
        } else {
            interfaz.mostrar("⚠️ Selección de personaje inválida.");
        }
    } catch (NumberFormatException e) {
        interfaz.mostrar("⚠️ Entrada inválida.");
    }
}
    
    public void crearDesafioMenu() {
        while (true) {
            try {
                this.interfaz.mostrar("=== Crear Desafío ===");
                this.interfaz.mostrar("Introduce el nick del jugador a desafiar (o 0 para volver):");
                String nickContrincante = this.interfaz.pedirEntrada();

                if (nickContrincante.equals("0")) {
                    return; // Volver al menú anterior
                }

                if (nickContrincante.equalsIgnoreCase(this.nick)) {
                    this.interfaz.mostrar("No puedes desafiarte a ti mismo.");
                    continue;
                }

                // Buscar contrincante en this.usuarios
                Jugador desafiado = null;
                for (Usuario usuario : this.usuarios) {
                    if (usuario.getNick().equalsIgnoreCase(nickContrincante)) {
                    	desafiado = new Jugador(usuario.getUserId(), usuario.getNick(), usuario.getNombre(), usuario.getPassword(), usuario.getRol(), usuario.getEstado(), usuario.getOro(), usuario.getPuntos()
, this.fileManager.cargarPersonajesUsuario(usuario.nick), this.fileManager.cargarDesafioUsuario(usuario.nick, usuarios));
                        break;
                    }
                }

                if (desafiado == null) {
                    this.interfaz.mostrar("El jugador indicado no existe o no es un jugador válido.");
                    continue;
                }

                // Pedir oro a apostar
                int oroApostado = 0;
                while (true) {
                    this.interfaz.mostrar("Introduce la cantidad de oro a apostar (mayor a 0 y hasta " + this.oro + ") o 0 para volver:");
                    try {
                        oroApostado = Integer.parseInt(this.interfaz.pedirEntrada());
                        if (oroApostado == 0) {
                            return; // Volver
                        }
                        if (oroApostado > 0 && oroApostado <= this.oro) {
                            break;
                        } else {
                            this.interfaz.mostrar("Cantidad inválida.");
                      }
                    } catch (NumberFormatException e) {
                        this.interfaz.mostrar("Introduce un número válido.");
                    }
                }

                // Crear y configurar desafío
                Desafio desafio = new Desafio();
                desafio.setDesafioId(UUID.randomUUID());
                desafio.setDesafiante(this);
                desafio.setDesafiado(desafiado);
                desafio.setOroApostado(oroApostado);
                desafio.setFechaDesafio(LocalDateTime.now());
                desafio.setEstado(E_EstadoDesafio.PENDIENTE);

                // Guardar el desafío
                String resultado = this.fileManager.guardarDesafio(desafio);
                this.interfaz.mostrar(resultado);
                this.interfaz.mostrar("Presiona Enter para continuar...");
                this.interfaz.pedirEntrada();
                return;

            } catch (Exception e) {
                this.interfaz.mostrar("Error al crear desafío: " + e.getMessage());
                e.printStackTrace();
                this.interfaz.mostrar("Presiona Enter para continuar...");
                this.interfaz.pedirEntrada();
            }
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

    public void restarOro(int penalizacion, A_Interfaz interfaz1) {
        if (penalizacion > this.oro) {
            this.oro = 0;
            interfaz1.mostrar("Se ha deducido todo tu oro disponible");
        } else {
            this.oro -= penalizacion;
            interfaz1.mostrar("Se ha deducido " + penalizacion + " de oro");
        }
    }

    public void aceptarDesafio(Desafio desafio, A_Interfaz interfaz1) {
        this.desafio = desafio;
        interfaz1.mostrar("Desafío aceptado. Desafiante: "+ this.desafio.getDesafiante() + " Vs Desafiado: " + this.desafio.getDesafiado());
    }

    public void rechazarDesafio(A_Interfaz interfaz1) {
        this.desafio = null;
        interfaz1.mostrar("Desafío rechazado. Desafiante: "+ this.desafio.getDesafiante() + " Vs Desafiado: " + this.desafio.getDesafiado());
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
