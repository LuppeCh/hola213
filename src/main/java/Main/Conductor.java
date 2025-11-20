package Main;

public final class Conductor extends Usuario {

    private boolean disponible;

    public Conductor(String nombre, String email, boolean disponible) {
        super(nombre, email);
        this.disponible = disponible;
    }

    public boolean disponible() { return disponible; }
    public void setDisponible(boolean disp) { this.disponible = disp; }
}
