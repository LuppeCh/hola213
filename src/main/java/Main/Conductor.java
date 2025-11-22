package Main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Conductor extends Usuario {

    private boolean disponible;

    // Constructor vacío para Jackson
    public Conductor() {
    }

    // Constructor completo usado por Jackson para cargar desde JSON
    @JsonCreator
    public Conductor(
            @JsonProperty("nombre") String nombre,
            @JsonProperty("email") String email,
            @JsonProperty("disponible") boolean disponible) {
        super(nombre, email);
        this.disponible = disponible;
    }

    // Constructor simplificado para registro
    public Conductor(String nombre, String email) {
        super(nombre, email);
        this.disponible = false;
    }

    // GETTERS REQUERIDOS POR JACKSON
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disp) { this.disponible = disp; }

    // Método tipo record
    public boolean disponible() { return disponible; }
}