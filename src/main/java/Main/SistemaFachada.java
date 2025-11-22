package Main;

import Viajes.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SistemaFachada {

    private final GestorUsuarios gestorUsuarios;
    private final Sistema sistema;
    private final GooglePlacesService placesService;
    private final DistanceCalculator distanceCalculator;

    public SistemaFachada() {
        this.gestorUsuarios = new GestorUsuarios();
        this.sistema = new Sistema(gestorUsuarios);
        this.placesService = new GooglePlacesService("AIzaSyB6uynr_3ELge4l5JrkDNGh3JYs-zO53DI");
        this.distanceCalculator = new DistanceCalculator();
    }

    // ========================================
    // GESTIÓN DE USUARIOS
    // ========================================

    public boolean registrarCliente(String nombre, String email) {
        return gestorUsuarios.registrarCliente(nombre, email);
    }

    public boolean registrarConductor(String nombre, String email) {
        return gestorUsuarios.registrarConductor(nombre, email);
    }

    public ResultadoLogin iniciarSesion(String email) {
        return sistema.iniciarSesionYActivar(email);
    }

    public Usuario obtenerUsuario(String email) {
        return gestorUsuarios.iniciarSesion(email).orElse(null);
    }

    // ========================================
    // GESTIÓN DE CONDUCTORES
    // ========================================

    public void activarConductor(Conductor conductor) {
        sistema.activarConductor(conductor);
    }

    public void desactivarConductor(Conductor conductor) {
        sistema.desactivarConductor(conductor);
    }

    public Conductor obtenerConductorDisponible() {
        return sistema.obtenerConductorDisponible();
    }

    // ========================================
    // AUTOCOMPLETADO DE LUGARES
    // ========================================

    public CompletableFuture<List<String>> autocompletarAsync(String texto) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return placesService.autocompletar(texto);
            } catch (Exception e) {
                e.printStackTrace();
                return List.of();
            }
        });
    }

    // ========================================
    // CÁLCULO DE RUTAS
    // ========================================

    public CompletableFuture<Ruta> calcularRutaAsync(String origen, String destino) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return distanceCalculator.calculateRoute(origen, destino);
            } catch (Exception e) {
                throw new RuntimeException("Error al calcular ruta: " + e.getMessage(), e);
            }
        });
    }

    // ========================================
    // GESTIÓN DE VIAJES
    // ========================================

    public ResultadoViaje crearViaje(Ruta ruta, Cliente cliente, TipoViaje tipo) {
        if (cliente == null) {
            return new ResultadoViaje(false, "No hay cliente en sesión", null);
        }

        Conductor conductor = sistema.obtenerConductorDisponible();

        if (conductor == null) {
            return new ResultadoViaje(false, "No hay conductores disponibles", null);
        }

        conductor.setDisponible(false);
        gestorUsuarios.guardarUsuarios();

        Viaje viaje = switch (tipo) {
            case Estandar -> new ViajeEstandar(ruta, cliente, conductor);
            case XL -> new ViajeXL(ruta, cliente, conductor);
            case Lujo -> new ViajeLujo(ruta, cliente, conductor);
        };

        sistema.agregarViaje(viaje);

        return new ResultadoViaje(true, "Viaje creado exitosamente", viaje);
    }

    // ========================================
    // LIMPIEZA DE RECURSOS
    // ========================================

    public void cerrarRecursos() {
        placesService.cerrar();
        distanceCalculator.closeContext();
    }

    // ========================================
    // RECORD
    // ========================================

    public record ResultadoViaje(
            boolean exitoso,
            String mensaje,
            Viaje viaje
    ) {}
}