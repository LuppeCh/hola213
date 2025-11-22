package Main;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Servicio para obtener la fecha actual desde una API externa.
 * Usa WorldTimeAPI como fuente de tiempo confiable.
 *
 * API: http://worldtimeapi.org/api/timezone/America/Argentina/Buenos_Aires
 */
public class ServicioFecha {

    private static final String API_URL = "http://worldtimeapi.org/api/timezone/America/Argentina/Buenos_Aires";
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public ServicioFecha() {
        this.client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // ========================================
    // OBTENER FECHA ACTUAL
    // ========================================

    /**
     * Obtiene la fecha actual desde la API.
     * Si falla, usa la fecha local del sistema como fallback.
     *
     * @return LocalDate con la fecha actual
     */
    public LocalDate obtenerFechaActual() {
        try {
            Request request = new Request.Builder()
                    .url(API_URL)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    JsonNode root = objectMapper.readTree(json);

                    // La API retorna fecha en formato ISO: "2025-01-21T15:30:00.123456-03:00"
                    String datetime = root.get("datetime").asText();

                    // Parsear solo la parte de la fecha
                    LocalDateTime dateTime = LocalDateTime.parse(
                            datetime.substring(0, 19),
                            DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    );

                    System.out.println("✅ Fecha obtenida desde API: " + dateTime.toLocalDate());
                    return dateTime.toLocalDate();
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al obtener fecha desde API: " + e.getMessage());
            System.err.println("   Usando fecha local del sistema como fallback");
        }

        // Fallback: usar fecha local
        LocalDate fechaLocal = LocalDate.now();
        System.out.println("📅 Usando fecha local: " + fechaLocal);
        return fechaLocal;
    }

    /**
     * Obtiene la fecha y hora actual completa.
     *
     * @return LocalDateTime con fecha y hora actual
     */
    public LocalDateTime obtenerFechaHoraActual() {
        try {
            Request request = new Request.Builder()
                    .url(API_URL)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String json = response.body().string();
                    JsonNode root = objectMapper.readTree(json);

                    String datetime = root.get("datetime").asText();

                    LocalDateTime dateTime = LocalDateTime.parse(
                            datetime.substring(0, 19),
                            DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    );

                    System.out.println("✅ Fecha/Hora obtenida desde API: " + dateTime);
                    return dateTime;
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al obtener fecha/hora desde API");
        }

        return LocalDateTime.now();
    }

    /**
     * Verifica si la API está disponible.
     *
     * @return true si la API responde, false en caso contrario
     */
    public boolean verificarDisponibilidad() {
        try {
            Request request = new Request.Builder()
                    .url(API_URL)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                return response.isSuccessful();
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene información detallada de la fecha actual.
     *
     * @return InfoFecha con todos los detalles
     */
    public InfoFecha obtenerInfoCompleta() {
        LocalDate fecha = obtenerFechaActual();

        return new InfoFecha(
                fecha,
                fecha.getDayOfWeek().toString(),
                fecha.getMonth().toString(),
                fecha.getYear(),
                fecha.getDayOfMonth(),
                fecha.getDayOfYear()
        );
    }

    // ========================================
    // RECORD: INFO FECHA
    // ========================================

    public record InfoFecha(
            LocalDate fecha,
            String diaSemana,
            String mes,
            int anio,
            int dia,
            int diaDelAnio
    ) {
        @Override
        public String toString() {
            return String.format(
                    "📅 %s, %d de %s de %d (Día %d del año)",
                    diaSemana, dia, mes, anio, diaDelAnio
            );
        }
    }

    /**
     * Cierra el cliente HTTP.
     */
    public void cerrar() {
        // OkHttpClient maneja sus propios recursos automáticamente
        // pero podemos forzar el cierre si es necesario
        client.dispatcher().executorService().shutdown();
        client.connectionPool().evictAll();
    }
}
