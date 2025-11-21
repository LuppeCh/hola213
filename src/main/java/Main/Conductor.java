package Main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Conductor extends Usuario {

    private boolean disponible;

    // CONSTRUCTOR COMPLETO (Usado por Jackson para cargar desde JSON)
    @JsonCreator
    public Conductor(
            @JsonProperty("nombre") String nombre,
            @JsonProperty("email") String email,
            @JsonProperty("disponible") boolean disponible) {
        super(nombre, email);
        this.disponible = disponible;
    }

    // CONSTRUCTOR SIMPLIFICADO (Usado en el registro)
    public Conductor(String nombre, String email) {
        super(nombre, email);
        this.disponible = false; // Se inicializa como FALSE por defecto
    }

    // GETTERS REQUERIDOS POR JACKSON
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disp) { this.disponible = disp; } // <--- EL SETTER ES CLAVE

    // Método tipo record
    public boolean disponible() { return disponible; }
}