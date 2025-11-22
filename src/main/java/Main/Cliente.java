package Main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class Cliente extends Usuario {

    private int viajesRealizados;
    private List<RegistroViaje> historialViajes;

    // Constructor vacío para Jackson
    public Cliente() {
        super();
        this.viajesRealizados = 0;
        this.historialViajes = new ArrayList<>();
    }

    // Constructor COMPLETO con todos los datos
    public Cliente(String nombre, String apellido, String dni, int edad,
                   String contrasena, String gmail) {
        super(nombre, apellido, dni, edad, contrasena, gmail);
        this.viajesRealizados = 0;
        this.historialViajes = new ArrayList<>();
    }

    // GETTERS Y SETTERS
    public int getViajesRealizados() {
        return viajesRealizados;
    }

    public void setViajesRealizados(int viajes) {
        this.viajesRealizados = viajes;
    }

    public List<RegistroViaje> getHistorialViajes() {
        return historialViajes;
    }

    public void setHistorialViajes(List<RegistroViaje> historial) {
        this.historialViajes = historial;
    }

    /**
     * Agrega un viaje al historial del cliente
     */
    public void agregarViaje(String origen, String destino, LocalDate fecha) {
        this.historialViajes.add(new RegistroViaje(origen, destino, fecha));
        this.viajesRealizados++;
    }

    /**
     * Record que representa un viaje realizado
     */
    public record RegistroViaje(
            String origen,
            String destino,
            LocalDate fecha
    ) {}
}