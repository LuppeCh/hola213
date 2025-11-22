package Main;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Conductor extends Usuario {

    private boolean disponible;
    private boolean premium;
    private Coordenada ubicacion;

    // NUEVOS CAMPOS
    private String modeloAuto;
    private String patenteAuto;
    private boolean tieneAireAcondicionado;
    private int capacidadPasajeros;

    // Constructor vacío para Jackson
    public Conductor() {
        super();
        this.disponible = false;
        this.premium = false;
        this.ubicacion = new Coordenada(-32.9468, -60.6393); // Rosario por defecto
        this.modeloAuto = "";
        this.patenteAuto = "";
        this.tieneAireAcondicionado = false;
        this.capacidadPasajeros = 4;
    }

    // Constructor COMPLETO
    public Conductor(String nombre, String apellido, String dni, int edad,
                     String contrasena, String gmail,
                     String modeloAuto, String patenteAuto,
                     boolean tieneAireAcondicionado, int capacidadPasajeros,
                     double latitud, double longitud) {
        super(nombre, apellido, dni, edad, contrasena, gmail);
        this.disponible = false;
        this.premium = false;
        this.ubicacion = new Coordenada(latitud, longitud);
        this.modeloAuto = modeloAuto;
        this.patenteAuto = patenteAuto;
        this.tieneAireAcondicionado = tieneAireAcondicionado;
        this.capacidadPasajeros = capacidadPasajeros;
    }

    // GETTERS Y SETTERS
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disp) { this.disponible = disp; }
    public boolean disponible() { return disponible; }

    public boolean isPremium() { return premium; }
    public void setPremium(boolean premium) { this.premium = premium; }
    public boolean esPremium() { return premium; }

    public Coordenada getUbicacion() { return ubicacion; }
    public void setUbicacion(Coordenada ubicacion) { this.ubicacion = ubicacion; }

    public String getModeloAuto() { return modeloAuto; }
    public void setModeloAuto(String modelo) { this.modeloAuto = modelo; }

    public String getPatenteAuto() { return patenteAuto; }
    public void setPatenteAuto(String patente) { this.patenteAuto = patente; }

    public boolean isTieneAireAcondicionado() { return tieneAireAcondicionado; }
    public void setTieneAireAcondicionado(boolean aire) { this.tieneAireAcondicionado = aire; }

    public int getCapacidadPasajeros() { return capacidadPasajeros; }
    public void setCapacidadPasajeros(int capacidad) { this.capacidadPasajeros = capacidad; }

    /**
     * Calcula la distancia a una ubicación usando la fórmula de Haversine
     */
    public double distanciaA(Coordenada otra) {
        if (this.ubicacion == null || otra == null) {
            return Double.MAX_VALUE;
        }
        return this.ubicacion.distanciaA(otra);
    }

    // ========================================
    // RECORD COORDENADA
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