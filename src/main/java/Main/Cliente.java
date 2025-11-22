package Main;

public final class Cliente extends Usuario {

    // Constructor vacío para Jackson
    public Cliente() {
        super();
    }

    // Constructor con 2 parámetros (COMPATIBLE con código actual)
    public Cliente(String nombre, String email) {
        super(nombre, "", "", 0, "", email);  // Llamar al constructor de 6 parámetros
        // nombre, apellido vacío, dni vacío, edad 0, contraseña vacía, email
    }
}