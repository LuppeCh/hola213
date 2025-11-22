package Main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Conductor extends Usuario {

    private boolean disponible;

    // Constructor vacío para Jackson
    public Conductor() {
        super();
        this.disponible = false;
    }

    @JsonCreator
    public Conductor(
            @JsonProperty("nombre") String nombre,
            @JsonProperty("email") String email,
            @JsonProperty("disponible") boolean disponible) {
        super(nombre, "", "", 0, "", email);
        this.disponible = disponible;
    }

    public Conductor(String nombre, String email) {
        super(nombre, "", "", 0, "", email);
        this.disponible = false;
    }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disp) { this.disponible = disp; }

    // Método tipo record
    public boolean disponible() { return disponible; }

    // ========================================
    // RECORD COORDENADA (PARA FUNCIONALIDAD FUTURA)
    // ========================================

    public record Coordenada(double latitud, double longitud) {

        public double distanciaA(Coordenada otra) {
            final int R = 6371; // Radio de la Tierra en km

            double latDistance = Math.toRadians(otra.latitud - this.latitud);
            double lonDistance = Math.toRadians(otra.longitud - this.longitud);

            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(this.latitud))
                    * Math.cos(Math.toRadians(otra.latitud))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            return R * c;
        }

        @Override
        public String toString() {
            return String.format("(%.4f, %.4f)", latitud, longitud);
        }
    }
}