package model.desafio;

public enum E_EstadoDesafio {
    PENDIENTE("pendiente"),
    ACEPTADO("aceptado"),
    RECHAZADO("rechazado"),
    VALIDADO("validado");

    private final String estado;

    E_EstadoDesafio(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }
}

