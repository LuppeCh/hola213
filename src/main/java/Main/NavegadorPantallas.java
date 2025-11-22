package Main;

import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.Supplier;

/**
 * Navegador centralizado que gestiona las transiciones entre pantallas.
 * Implementa el patrón Strategy para la creación dinámica de escenas.
 *
 * Responsabilidades:
 * - Registrar fábricas de pantallas
 * - Navegar entre pantallas
 * - Gestionar historial de navegación (opcional)
 * - Cambiar escenas en el Stage
 */
public class NavegadorPantallas {

    private final Stage stage;
    private final Map<Pantalla, Supplier<Scene>> fabricasPantallas;
    private final Stack<Pantalla> historial;
    private Pantalla pantallaActual;

    public NavegadorPantallas(Stage stage) {
        this.stage = stage;
        this.fabricasPantallas = new HashMap<>();
        this.historial = new Stack<>();
    }

    // ========================================
    // REGISTRO DE PANTALLAS
    // ========================================

    /**
     * Registra una fábrica que sabe cómo crear la escena de una pantalla específica.
     * Usa el patrón Strategy: cada pantalla tiene su propia estrategia de creación.
     *
     * @param pantalla Identificador único de la pantalla (enum)
     * @param fabrica Función que crea y retorna la Scene de esa pantalla
     *
     * Ejemplo de uso:
     * navegador.registrarPantalla(Pantalla.LOGIN, () -> uiFactory.crearPantallaLogin());
     */
    public void registrarPantalla(Pantalla pantalla, Supplier<Scene> fabrica) {
        if (pantalla == null) {
            throw new IllegalArgumentException("La pantalla no puede ser null");
        }
        if (fabrica == null) {
            throw new IllegalArgumentException("La fábrica no puede ser null");
        }

        fabricasPantallas.put(pantalla, fabrica);
        System.out.println("✅ Pantalla registrada: " + pantalla);
    }

    // ========================================
    // NAVEGACIÓN
    // ========================================

    /**
     * Navega a la pantalla especificada.
     * Crea la escena usando la fábrica registrada y la muestra en el Stage.
     *
     * @param pantalla Pantalla a la que se desea navegar
     * @throws IllegalArgumentException si la pantalla no está registrada
     *
     * Proceso:
     * 1. Busca la fábrica de esa pantalla
     * 2. Crea la escena (llamando a la fábrica)
     * 3. Cambia la escena del Stage
     * 4. Actualiza el historial
     */
    public void navegarA(Pantalla pantalla) {
        if (pantalla == null) {
            throw new IllegalArgumentException("La pantalla no puede ser null");
        }

        Supplier<Scene> fabrica = fabricasPantallas.get(pantalla);

        if (fabrica == null) {
            throw new IllegalArgumentException(
                    "Pantalla no registrada: " + pantalla +
                            ". Debes registrarla primero usando registrarPantalla()."
            );
        }

        try {
            // Guardar pantalla actual en el historial (si existe)
            if (pantallaActual != null) {
                historial.push(pantallaActual);
            }

            // Crear la nueva escena usando la fábrica
            Scene escena = fabrica.get();

            // Cambiar la escena en el Stage
            stage.setScene(escena);

            // Actualizar pantalla actual
            this.pantallaActual = pantalla;

            // Log para debugging
            System.out.println("📱 Navegado a: " + pantalla);

        } catch (Exception e) {
            System.err.println("❌ Error al navegar a " + pantalla + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al crear la pantalla " + pantalla, e);
        }
    }

    /**
     * Navega a la pantalla anterior en el historial.
     * Útil para implementar un botón "Volver" genérico.
     *
     * @return true si se pudo volver atrás, false si no hay historial
     */
    public boolean volverAtras() {
        if (historial.isEmpty()) {
            System.out.println("⚠️ No hay pantallas en el historial");
            return false;
        }

        Pantalla pantallaAnterior = historial.pop();

        // Navegamos sin agregar al historial (para evitar loop infinito)
        Supplier<Scene> fabrica = fabricasPantallas.get(pantallaAnterior);
        if (fabrica != null) {
            Scene escena = fabrica.get();
            stage.setScene(escena);
            this.pantallaActual = pantallaAnterior;
            System.out.println("⬅️ Vuelto a: " + pantallaAnterior);
            return true;
        }

        return false;
    }

    /**
     * Limpia el historial de navegación.
     * Útil cuando se cierra sesión o se reinicia la app.
     */
    public void limpiarHistorial() {
        historial.clear();
        System.out.println("🗑️ Historial de navegación limpiado");
    }

    // ========================================
    // CONSULTAS
    // ========================================

    /**
     * Obtiene la pantalla actual.
     * @return Pantalla actual o null si aún no se ha navegado
     */
    public Pantalla getPantallaActual() {
        return pantallaActual;
    }

    /**
     * Verifica si una pantalla está registrada.
     * @param pantalla Pantalla a verificar
     * @return true si está registrada, false en caso contrario
     */
    public boolean estaPantallaRegistrada(Pantalla pantalla) {
        return fabricasPantallas.containsKey(pantalla);
    }

    /**
     * Obtiene el tamaño del historial de navegación.
     * @return Número de pantallas en el historial
     */
    public int getTamanoHistorial() {
        return historial.size();
    }

    /**
     * Verifica si hay historial disponible para volver atrás.
     * @return true si se puede volver atrás, false en caso contrario
     */
    public boolean puedeVolverAtras() {
        return !historial.isEmpty();
    }

    // ========================================
    // DEBUG Y UTILIDADES
    // ========================================

    /**
     * Imprime información de debug sobre el estado del navegador.
     */
    public void imprimirEstado() {
        System.out.println("====== ESTADO DEL NAVEGADOR ======");
        System.out.println("Pantalla actual: " + pantallaActual);
        System.out.println("Pantallas registradas: " + fabricasPantallas.keySet());
        System.out.println("Tamaño historial: " + historial.size());
        System.out.println("==================================");
    }

    /**
     * Obtiene el Stage asociado a este navegador.
     * @return Stage principal de la aplicación
     */
    public Stage getStage() {
        return stage;
    }
}
