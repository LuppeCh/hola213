package Main;

import com.fasterxml.jackson.databind.ObjectMapper;
import Viajes.TipoViaje;
import java.io.File;
import java.io.IOException;

/**
 * Sistema de cálculo de tarifas basado en configuración JSON.
 * Lee los valores de bajada de bandera y precio por metro desde un archivo.
 */
public class SistemaTarifas {

    private static final String ARCHIVO_TARIFAS = "tarifas.json";
    private final ConfiguracionTarifas config;
    private final ObjectMapper objectMapper;

    public SistemaTarifas() {
        this.objectMapper = new ObjectMapper();
        this.config = cargarConfiguracion();
    }

    // ========================================
    // CARGA DE CONFIGURACIÓN
    // ========================================

    private ConfiguracionTarifas cargarConfiguracion() {
        File archivo = new File(ARCHIVO_TARIFAS);

        if (!archivo.exists()) {
            System.out.println("⚠️ Archivo de tarifas no encontrado. Creando configuración por defecto...");
            ConfiguracionTarifas configDefault = crearConfiguracionPorDefecto();
            guardarConfiguracion(configDefault);
            return configDefault;
        }

        try {
            ConfiguracionTarifas config = objectMapper.readValue(archivo, ConfiguracionTarifas.class);
            System.out.println("✅ Tarifas cargadas desde " + ARCHIVO_TARIFAS);
            return config;
        } catch (IOException e) {
            System.err.println("❌ Error al cargar tarifas: " + e.getMessage());
            return crearConfiguracionPorDefecto();
        }
    }

    private ConfiguracionTarifas crearConfiguracionPorDefecto() {
        return new ConfiguracionTarifas(
                500.0,      // bajada de bandera (ARS)
                0.15,       // precio por metro (ARS)
                1.0,        // multiplicador estándar
                1.5,        // multiplicador XL
                2.2         // multiplicador lujo
        );
    }

    private void guardarConfiguracion(ConfiguracionTarifas config) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(ARCHIVO_TARIFAS), config);
            System.out.println("✅ Configuración de tarifas guardada");
        } catch (IOException e) {
            System.err.println("❌ Error al guardar configuración: " + e.getMessage());
        }
    }

    // ========================================
    // CÁLCULO DE TARIFAS
    // ========================================

    /**
     * Calcula la tarifa base de un viaje (sin descuentos).
     *
     * Fórmula: bajadaDeBandera + (distanciaMetros * precioPorMetro * multiplicadorTipo)
     *
     * @param distanciaMetros Distancia del viaje en metros
     * @param tipo Tipo de viaje (Estandar, XL, Lujo)
     * @return Tarifa base en ARS
     */
    public double calcularTarifaBase(long distanciaMetros, TipoViaje tipo) {
        double multiplicador = obtenerMultiplicadorPorTipo(tipo);

        double costoDistancia = distanciaMetros * config.precioPorMetro() * multiplicador;
        double tarifaTotal = config.bajadaDeBandera() + costoDistancia;

        return Math.round(tarifaTotal * 100.0) / 100.0; // Redondear a 2 decimales
    }

    /**
     * Calcula la tarifa final aplicando todos los descuentos.
     *
     * @param distanciaMetros Distancia en metros
     * @param tipo Tipo de viaje
     * @param descuentos Descuentos a aplicar (en porcentaje, ej: 25 para 25%)
     * @return Tarifa final con descuentos
     */
    public ResultadoTarifa calcularTarifaConDescuentos(long distanciaMetros,
                                                       TipoViaje tipo,
                                                       double... descuentos) {
        double tarifaBase = calcularTarifaBase(distanciaMetros, tipo);
        double descuentoTotal = 0;

        // Sumar todos los descuentos (pero sin superar el 100%)
        for (double descuento : descuentos) {
            descuentoTotal += descuento;
        }

        // Limitar descuento máximo al 90% (para evitar viajes gratis)
        descuentoTotal = Math.min(descuentoTotal, 90);

        double montoDescuento = tarifaBase * (descuentoTotal / 100.0);
        double tarifaFinal = tarifaBase - montoDescuento;

        return new ResultadoTarifa(
                tarifaBase,
                descuentoTotal,
                montoDescuento,
                tarifaFinal,
                tipo,
                distanciaMetros
        );
    }

    /**
     * Obtiene el multiplicador según el tipo de viaje.
     */
    private double obtenerMultiplicadorPorTipo(TipoViaje tipo) {
        return switch (tipo) {
            case Estandar -> config.multiplicadorEstandar();
            case XL -> config.multiplicadorXL();
            case Lujo -> config.multiplicadorLujo();
        };
    }

    // ========================================
    // GETTERS PARA CONFIGURACIÓN
    // ========================================

    public double getBajadaDeBandera() {
        return config.bajadaDeBandera();
    }

    public double getPrecioPorMetro() {
        return config.precioPorMetro();
    }

    public ConfiguracionTarifas getConfiguracion() {
        return config;
    }

    // ========================================
    // RECORDS
    // ========================================

    /**
     * Configuración de tarifas desde JSON.
     */
    public record ConfiguracionTarifas(
            double bajadaDeBandera,
            double precioPorMetro,
            double multiplicadorEstandar,
            double multiplicadorXL,
            double multiplicadorLujo
    ) {}

    /**
     * Resultado detallado del cálculo de tarifa.
     */
    public record ResultadoTarifa(
            double tarifaBase,
            double descuentoPorcentaje,
            double montoDescuento,
            double tarifaFinal,
            TipoViaje tipo,
            long distanciaMetros
    ) {
        public String formatoDetallado() {
            return String.format("""
                📊 DETALLE DE TARIFA
                Tipo de viaje: %s
                Distancia: %.2f km
                Tarifa base: $%.2f
                Descuento: %.0f%% (-$%.2f)
                ════════════════════
                TOTAL A PAGAR: $%.2f
                """,
                    tipo,
                    distanciaMetros / 1000.0,
                    tarifaBase,
                    descuentoPorcentaje,
                    montoDescuento,
                    tarifaFinal
            );
        }

        @Override
        public String toString() {
            return String.format("Tarifa: $%.2f (Base: $%.2f, Descuento: %.0f%%)",
                    tarifaFinal, tarifaBase, descuentoPorcentaje);
        }
    }
}
