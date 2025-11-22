package Main;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Sistema de descuentos usando Java Streams.
 * Implementa 3 tipos de descuentos:
 * 1. 25% primera semana desde registro
 * 2. 30% cada 20 viajes
 * 3. 10% por viaje favorito (mismo viaje 3 veces en 5 días)
 */
public class SistemaDescuentos {

    // ========================================
    // DESCUENTO 1: USUARIO NUEVO (25%)
    // ========================================

    /**
     * Verifica si el cliente es nuevo (registrado hace menos de 7 días).
     * @param cliente Cliente a verificar
     * @return 25.0 si es nuevo, 0.0 en caso contrario
     */
    public double calcularDescuentoUsuarioNuevo(Cliente cliente) {
        long diasDesdeRegistro = ChronoUnit.DAYS.between(
                cliente.getFechaRegistro(),
                LocalDate.now()
        );

        if (diasDesdeRegistro <= 7) {
            System.out.println("🎉 ¡Descuento usuario nuevo aplicado! (25%)");
            return 25.0;
        }

        return 0.0;
    }

    // ========================================
    // DESCUENTO 2: CANTIDAD DE VIAJES (30%)
    // ========================================

    /**
     * Verifica si el cliente alcanzó un múltiplo de 20 viajes.
     * Aplica 30% en el viaje 20, 40, 60, etc.
     *
     * @param cliente Cliente a verificar
     * @return 30.0 si cumple, 0.0 en caso contrario
     */
    public double calcularDescuentoPorCantidad(Cliente cliente) {
        int viajesRealizados = cliente.getViajesRealizados();

        // Verificar si el próximo viaje es múltiplo de 20
        int proximoViaje = viajesRealizados + 1;

        if (proximoViaje % 20 == 0) {
            System.out.println("🎊 ¡Felicitaciones! Alcanzaste " + proximoViaje + " viajes. Descuento del 30%");
            return 30.0;
        }

        return 0.0;
    }

    // ========================================
    // DESCUENTO 3: VIAJE FAVORITO (10%)
    // ========================================

    /**
     * Detecta si el viaje actual es "favorito" usando Streams.
     *
     * Un viaje es favorito si:
     * - El mismo origen-destino se repitió 3+ veces en los últimos 5 días
     * - Se cuenta ida y vuelta como el mismo viaje
     *
     * @param cliente Cliente a analizar
     * @param origenActual Origen del viaje actual
     * @param destinoActual Destino del viaje actual
     * @return 10.0 si es viaje favorito, 0.0 en caso contrario
     */
    public double calcularDescuentoViajeFavorito(Cliente cliente,
                                                 String origenActual,
                                                 String destinoActual) {
        LocalDate hace5Dias = LocalDate.now().minusDays(5);

        // Filtrar viajes de los últimos 5 días
        List<Cliente.RegistroViaje> viajesRecientes = cliente.getHistorialViajes().stream()
                .filter(viaje -> viaje.fecha().isAfter(hace5Dias))
                .toList();

        // Normalizar rutas (ida y vuelta son iguales)
        Map<RutaNormalizada, Long> frecuenciasRutas = viajesRecientes.stream()
                .map(viaje -> new RutaNormalizada(viaje.origen(), viaje.destino()))
                .collect(Collectors.groupingBy(
                        ruta -> ruta,
                        Collectors.counting()
                ));

        // Verificar si la ruta actual aparece 3+ veces
        RutaNormalizada rutaActual = new RutaNormalizada(origenActual, destinoActual);
        long vecesRepetida = frecuenciasRutas.getOrDefault(rutaActual, 0L);

        if (vecesRepetida >= 2) { // 2 anteriores + 1 actual = 3 total
            System.out.println("⭐ ¡Viaje favorito detectado! Descuento del 10%");
            System.out.println("   Ruta: " + origenActual + " ↔ " + destinoActual);
            return 10.0;
        }

        return 0.0;
    }

    /**
     * Calcula todos los descuentos aplicables para un viaje.
     *
     * @param cliente Cliente que solicita el viaje
     * @param origen Origen del viaje
     * @param destino Destino del viaje
     * @return Array con todos los descuentos aplicables
     */
    public double[] calcularTodosLosDescuentos(Cliente cliente,
                                               String origen,
                                               String destino) {
        double descuentoNuevo = calcularDescuentoUsuarioNuevo(cliente);
        double descuentoCantidad = calcularDescuentoPorCantidad(cliente);
        double descuentoFavorito = calcularDescuentoViajeFavorito(cliente, origen, destino);

        // Filtrar solo descuentos mayores a 0
        return java.util.stream.DoubleStream.of(
                        descuentoNuevo,
                        descuentoCantidad,
                        descuentoFavorito
                )
                .filter(d -> d > 0)
                .toArray();
    }

    /**
     * Obtiene un resumen de los descuentos aplicados.
     */
    public String obtenerResumenDescuentos(Cliente cliente, String origen, String destino) {
        double[] descuentos = calcularTodosLosDescuentos(cliente, origen, destino);

        if (descuentos.length == 0) {
            return "Sin descuentos aplicables";
        }

        StringBuilder resumen = new StringBuilder("🎁 Descuentos aplicados:\n");

        double descuentoNuevo = calcularDescuentoUsuarioNuevo(cliente);
        if (descuentoNuevo > 0) {
            resumen.append("  • Usuario nuevo: 25%\n");
        }

        double descuentoCantidad = calcularDescuentoPorCantidad(cliente);
        if (descuentoCantidad > 0) {
            resumen.append("  • Viaje #").append(cliente.getViajesRealizados() + 1).append(": 30%\n");
        }

        double descuentoFavorito = calcularDescuentoViajeFavorito(cliente, origen, destino);
        if (descuentoFavorito > 0) {
            resumen.append("  • Viaje favorito: 10%\n");
        }

        double total = java.util.Arrays.stream(descuentos).sum();
        resumen.append("  ════════════════\n");
        resumen.append("  TOTAL: ").append(String.format("%.0f%%", Math.min(total, 90)));

        return resumen.toString();
    }

    // ========================================
    // RECORD: RUTA NORMALIZADA
    // ========================================

    /**
     * Representa una ruta normalizada donde A→B es igual a B→A.
     * Se usa para detectar viajes favoritos (ida y vuelta cuentan igual).
     */
    private record RutaNormalizada(String punto1, String punto2) {

        RutaNormalizada {
            // Normalizar: siempre ordenar alfabéticamente
            if (punto1.compareTo(punto2) > 0) {
                String temp = punto1;
                punto1 = punto2;
                punto2 = temp;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof RutaNormalizada other)) return false;
            return punto1.equals(other.punto1) && punto2.equals(other.punto2);
        }

        @Override
        public int hashCode() {
            return punto1.hashCode() + punto2.hashCode();
        }

        @Override
        public String toString() {
            return punto1 + " ↔ " + punto2;
        }
    }
}
