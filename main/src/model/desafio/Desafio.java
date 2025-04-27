package model.desafio;

import java.time.LocalDateTime;
import java.util.UUID;

import model.Sistema;
import model.usuario.Jugador;
import model.usuario.Usuario;
import storage.FileManager;

public class Desafio implements I_Desafio {
    private UUID desafioId;
    private LocalDateTime fechaDesafio;
    private Jugador desafiante;
    private Jugador desafiado;
    private int oroApostado;
    private E_EstadoDesafio estado;
    private FileManager fileManager;

    public Desafio() {
        // Inicializar el FileManager
        this.fileManager = Sistema.getFileManager(); // Asumiendo que tienes un método getInstance()
    }
    
    // Constructor adicional para cargar desafíos existentes
    public Desafio(UUID desafioId) {
        this();  // Llamar al constructor sin parámetros para inicializar fileManager
        Desafio desafioCargado = fileManager.cargarDesafio(desafioId);
        if (desafioCargado != null) {
            this.desafioId = desafioCargado.getDesafioId();
            this.fechaDesafio = (LocalDateTime) desafioCargado.getFecha();
            this.desafiante = desafioCargado.getDesafiante();
            this.desafiado = desafioCargado.getDesafiado();
            this.oroApostado = desafioCargado.getOroApostado();
            this.estado = desafioCargado.getEstado();
        }
    }

    public Combate Aceptar(Desafio d) {
        this.estado = E_EstadoDesafio.ACEPTADO;
        
        // Solo necesitas un método para guardar el desafío actualizado
        fileManager.guardarDesafio(this);

        return new Combate(d);
    }

    public String Desafiar(Jugador desafiante, Jugador desafiado, int oroApostado) {
        if (oroApostado <= 0 || oroApostado > desafiante.getOro()) {
            return "No se pudo crear desafio";
        }
        this.desafioId = UUID.randomUUID();
        this.fechaDesafio = LocalDateTime.now();
        this.desafiante = desafiante;
        this.desafiado = desafiado;
        this.oroApostado = oroApostado;
        this.estado = E_EstadoDesafio.PENDIENTE;

        fileManager.guardarDesafio(this);
        return "Se creo correctamente el desafio";
    }

    public void Rechazar() {
        this.estado = E_EstadoDesafio.RECHAZADO;
        int penalizacion = (int) Math.ceil(oroApostado * 0.10);
        desafiado.restarOro(penalizacion);
        desafiante.agregarOro(penalizacion);

        fileManager.guardarDesafio(this);
    }

    // Método para cargar un desafío por su ID
    public static Desafio cargarDesafio(UUID desafioId) {
        return new Desafio(desafioId);
    }

    public UUID getDesafioId() {
        return desafioId;
    }

    public void setDesafioId(UUID desafioId) {
        this.desafioId = desafioId;
    }

    public Jugador getDesafiante() {
        return desafiante;
    }

    public void setDesafiante(Jugador desafiante) {
        this.desafiante = desafiante;
    }

    public Jugador getDesafiado() {
        return desafiado;
    }

    public void setDesafiado(Jugador desafiado) {
        this.desafiado = desafiado;
    }

    public int getOroApostado() {
        return oroApostado;
    }

    public void setOroApostado(int oroApostado) {
        this.oroApostado = oroApostado;
    }

    public E_EstadoDesafio getEstado() {
        return this.estado;
    }
    
    public void setEstado(E_EstadoDesafio estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha() {
        return this.fechaDesafio;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

	public void setFechaDesafio(LocalDateTime localDateTime) {
		this.fechaDesafio = localDateTime;
		
	}
}