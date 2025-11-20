package Main;
import java.io.Serializable;

public sealed abstract class Usuario implements Serializable
        permits Cliente, Conductor {

    protected String nombre;
    protected String email;

    public Usuario(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    public String nombre() { return nombre; }
    public String email() { return email; }
}
