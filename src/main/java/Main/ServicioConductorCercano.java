package Main;

import Main.Conductor.Coordenada;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para encontrar el conductor más cercano usando Java Streams.
 * Considera:
 * - Distancia geográfica (Haversine)
 * - Disponibilidad del conductor
 * - Tipo de vehículo (premium vs estándar)
 */
public class ServicioConductorCercano {

    // ========================================
    // BÚSQUEDA DE CONDUCTOR MÁS CERCANO
    // ========================================

    /**
     * Encuentra el conductor disponible más cercano a una ubicación.
     * Usa Streams para filtrar, ordenar y seleccionar.
     *
     * @param conductores Lista de todos los conductores
     * @param ubicacionCliente Ubicación del cliente
     * @return Optional con el conductor más cercano o vacío si no hay disponibles
     */
    public Optional<Conductor> encontrarMasCercano(List<Conductor> conductores,
                                                   Coordenada ubicacionCliente) {
        return conductores.stream()
                // 1. Filtrar solo disponibles
                .filter(Conductor::disponible)

                // 2. Ordenar por distancia (menor a mayor)
                .sorted(Comparator.comparingDouble(
                        conductor -> conductor.distanciaA(ubicacionCliente)
                ))

                // 3. Tomar el primero (el más cercano)
                .findFirst();
    }

    /**
     * Encuentra el conductor más cercano con preferencia por tipo premium.
     * Si hay conductores premium cercanos, los prioriza.
     *
     * @param conductores Lista de conductores
     * @param ubicacionCliente Ubicación del cliente
     * @param preferirPremium Si true, prioriza conductores premium
     * @return Optional con el conductor seleccionado
     */
    public Optional<Conductor> encontrarConPreferencia(List<Conductor> conductores,
                                                       Coordenada ubicacionCliente,
                                                       boolean preferirPremium) {
        if (!preferirPremium) {
            return encontrarMasCercano(conductores, ubicacionCliente);
        }

        // Buscar primero entre conductores premium
        Optional<Conductor> conductorPremium = conductores.stream()
                .filter(Conductor::disponible)
                .filter(Conductor::esPremium)
                .sorted(Comparator.comparingDouble(c -> c.distanciaA(ubicacionCliente)))
                .findFirst();

        // Si no hay premium disponible, buscar cualquiera
        return conductorPremium.or(() -> encontrarMasCercano(conductores, ubicacionCliente));
    }

    /**
     * Encuentra los N conductores más cercanos.
     * Útil para mostrar opciones al cliente.
     *
     * @param conductores Lista de conductores
     * @param ubicacionCliente Ubicación del cliente
     * @param cantidad Cantidad de conductores a retornar
     * @return Lista con los N conductores más cercanos
     */
    public List<ConductorConDistancia> encontrarNCercanos(List<Conductor> conductores,
                                                          Coordenada ubicacionCliente,
                                                          int cantidad) {
        return conductores.stream()
                .filter(Conductor::disponible)
                .map(conductor -> new ConductorConDistancia(
                        conductor,
                        conductor.distanciaA(ubicacionCliente)
                ))
                .sorted(Comparator.comparingDouble(ConductorConDistancia::distancia))
                .limit(cantidad)
                .toList();
    }

    /**
     * Calcula estadísticas de disponibilidad de conductores.
     *
     * @param conductores Lista de conductores
     * @param ubicacionCliente Ubicación del cliente
     * @return Estadísticas detalladas
     */
    public EstadisticasConductores calcularEstadisticas(List<Conductor> conductores,
                                                        Coordenada ubicacionCliente) {
        long totalConductores = conductores.size();

        long disponibles = conductores.stream()
                .filter(Conductor::disponible)
                .count();

        long premium = conductores.stream()
                .filter(Conductor::disponible)
                .filter(Conductor::esPremium)
                .count();

        double distanciaPromedio = conductores.stream()
                .filter(Conductor::disponible)
                .mapToDouble(c -> c.distanciaA(ubicacionCliente))
                .average()
                .orElse(0.0);

        Optional<Conductor> masCercano = encontrarMasCercano(conductores, ubicacionCliente);
        double distanciaMasCercano = masCercano
                .map(c -> c.distanciaA(ubicacionCliente))
                .orElse(Double.MAX_VALUE);

        return new EstadisticasConductores(
                totalConductores,
                disponibles,
                premium,
                distanciaPromedio,
                distanciaMasCercano,
                masCercano.isPresent()
        );
    }

    /**
     * Filtra conductores por radio de búsqueda.
     *
     * @param conductores Lista de conductores
     * @param ubicacionCliente Ubicación del cliente
     * @param radioKm Radio de búsqueda en kilómetros
     * @return Lista de conductores dentro del radio
     */
    public List<Conductor> filtrarPorRadio(List<Conductor> conductores,
                                           Coordenada ubicacionCliente,
                                           double radioKm) {
        return conductores.stream()
                .filter(Conductor::disponible)
                .filter(conductor -> conductor.distanciaA(ubicacionCliente) <= radioKm)
                .toList();
    }

    /**
     * Agrupa conductores por proximidad (cerca, medio, lejos).
     *
     * @param conductores Lista de conductores
     * @param ubicacionCliente Ubicación del cliente
     * @return Mapa con conductores agrupados
     */
    public java.util.Map<ProximidadCategoria, List<Conductor>> agruparPorProximidad(
            List<Conductor> conductores,
            Coordenada ubicacionCliente) {

        return conductores.stream()
                .filter(Conductor::disponible)
                .collect(java.util.stream.Collectors.groupingBy(conductor -> {
                    double distancia = conductor.distanciaA(ubicacionCliente);
                    if (distancia <= 2.0) return ProximidadCategoria.CERCA;
                    if (distancia <= 5.0) return ProximidadCategoria.MEDIO;
                    return ProximidadCategoria.LEJOS;
                }));
    }

    // ========================================
    // RECORDS Y ENUMS
    // ========================================

    /**
     * Conductor con su distancia calculada.
     */
    public record ConductorConDistancia(
            Conductor conductor,
            double distancia
    ) {
        public String formatoDistancia() {
            return String.format("%.2f km", distancia);
        }

        @Override
        public String toString() {
            return String.format("%s - %s - %.2f km",
                    conductor.nombreCompleto(),
                    conductor.esPremium() ? "Premium" : "Estándar",
                    distancia
            );
        }
    }

    /**
     * Estadísticas de conductores disponibles.
     */
    public record EstadisticasConductores(
            long totalConductores,
            long disponibles,
            long premium,
            double distanciaPromedio,
            double distanciaMasCercano,
            boolean hayDisponibles
    ) {
        public String formatoResumen() {
            return String.format("""
                📊 ESTADÍSTICAS DE CONDUCTORES
                Total: %d
                Disponibles: %d (%.0f%%)
                Premium: %d
                Distancia promedio: %.2f km
                Más cercano: %.2f km
                """,
                    totalConductores,
                    disponibles,
                    (disponibles * 100.0 / totalConductores),
                    premium,
                    distanciaPromedio,
                    distanciaMasCercano
            );
        }
    }

    /**
     * Categorías de proximidad.
     */
    public enum ProximidadCategoria {
        CERCA,   // 0-2 km
        MEDIO,   // 2-5 km
        LEJOS    // 5+ km
    }
}