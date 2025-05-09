package model.personaje.estado;
import java.util.List;

import model.personaje.habilidad.Don;

public interface I_EstadoLicantropo {
    // Attributes
    int rabia = 0;
    List<Don> dones = null;
    float altura = 0.0f;
    int peso = 0;

    // Methods
    void aumentarRabia(int cantidad);
    void usarDon(Don don);
}
