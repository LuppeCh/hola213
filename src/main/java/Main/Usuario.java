package Main;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.Serializable;
import java.time.LocalDate;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipoUsuario"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Cliente.class, name = "Cliente"),
        @JsonSubTypes.Type(value = Conductor.class, name = "Conductor")
})
public sealed abstract class Usuario implements Serializable
        permits Cliente, Conductor {

    protected String nombre;
    protected String apellido;
    protected String dni;
    protected int edad;
    protected String contrasena;
    protected String gmail;
    protected LocalDate fechaRegistro;

    // Constructor vacío para Jackson
    protected Usuario() {
        this.fechaRegistro = LocalDate.now();
    }

    // Constructor completo
    public Usuario(String nombre, String apellido, String dni, int edad,
                   String contrasena, String gmail) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.edad = edad;
        this.contrasena = contrasena;
        this.gmail = gmail;
        this.fechaRegistro = LocalDate.now();
    }

    // GETTERS
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getDni() { return dni; }
    public int getEdad() { return edad; }
    public String getContrasena() { return contrasena; }
    public String getGmail() { return gmail; }
    public LocalDate getFechaRegistro() { return fechaRegistro; }

    // SETTERS
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setDni(String dni) { this.dni = dni; }
    public void setEdad(int edad) { this.edad = edad; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setGmail(String gmail) { this.gmail = gmail; }
    public void setFechaRegistro(LocalDate fecha) { this.fechaRegistro = fecha; }

    // Métodos de acceso estilo record
    public String nombreCompleto() {
        return nombre + " " + apellido;
    }

    public boolean esNuevo() {
        return LocalDate.now().minusDays(7).isBefore(fechaRegistro);
    }
}