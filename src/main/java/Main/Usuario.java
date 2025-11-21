package Main;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.Serializable;

// =======================================================
// ANOTACIONES DE JACKSON PARA MANEJAR POLIMORFISMO
// =======================================================
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,              // Usar el nombre de la subclase
        include = JsonTypeInfo.As.PROPERTY,      // Incluirlo como una propiedad JSON
        property = "tipoUsuario"                 // Campo que se añadirá al JSON
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Cliente.class, name = "Cliente"),
        @JsonSubTypes.Type(value = Conductor.class, name = "Conductor")
})
public sealed abstract class Usuario implements Serializable
        permits Cliente, Conductor {

    protected String nombre;
    protected String email;

    // CONSTRUCTOR POR DEFECTO REQUERIDO POR JACKSON
    protected Usuario() {
    }

    public Usuario(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    // GETTERS REQUERIDOS POR JACKSON
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }

    // MÉTODOS EXISTENTES
    public String nombre() { return nombre; }
    public String email() { return email; }
}