package model.desafio;

import model.usuario.Jugador;
import storage.FileManager;

public class Desafio implements I_Desafio {

	private Jugador desafiante;
	private Jugador desafiado;
	private int oroApostado;
	private E_EstadoDesafio estado;
	private FileManager intanceFileManager;

	public Desafio(FileManager intanceFileManager) {
		this.intanceFileManager = intanceFileManager;
	}

	public Combate Aceptar(Desafio d) {
		this.estado = E_EstadoDesafio.ACEPTADO;
		intanceFileManager.actualizarDesafio(this);

		this.intanceFileManager.guardarDesafio(this);

		return new Combate();

	}

	public String Desafiar(Jugador desafiante, Jugador desafiado, int oroApostado) {
		if (oroApostado <= 0 || oroApostado > desafiante.getOro()) {
			return "No se pudo crear desafio";
		}
		this.desafiante = desafiante;
		this.desafiado = desafiado;
		this.oroApostado = oroApostado;
		this.estado = E_EstadoDesafio.PENDIENTE;

		this.intanceFileManager.guardarDesafio(this);
		return "Se creo correctamente el desafio";
	}

	public void Rechazar() {
		this.estado = E_EstadoDesafio.RECHAZADO;
		int penalizacion = (int) Math.ceil(oroApostado * 0.10);
		desafiado.restarOro(penalizacion);
		desafiante.agregarOro(penalizacion);

		this.intanceFileManager.guardarDesafio(this);
		// System.out.println("Se ha rechazado el desafio");
	}
}
