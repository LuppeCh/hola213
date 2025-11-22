package Main;

import Viajes.Viaje;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sistema {

    private static final String ARCHIVO_VIAJES = "viajes.json";

    // DEPENDENCIAS
    private GestorUsuarios gestorUsuarios;
    private List<Viaje> viajes;
    private ObjectMapper objectMapper;

    // CONSTRUCTOR
    public Sistema(GestorUsuarios gestorUsuarios) {
        this.gestorUsuarios = gestorUsuarios;
        this.objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        this.viajes = cargarViajes();
    }

    // ========================================
    // FACHADA DE LOGIN (CLAVE PARA EL DESACOPLAMIENTO)
    // ========================================
    /**
     * Intenta iniciar sesión y ejecuta la lógica de negocio (como activar
     * al conductor).
     * @param email Email del usuario a loguear.
     * @return Instrucción de navegación limpia para el Frontend.
     */
    public ResultadoLogin iniciarSesionYActivar(String email) {
        var resultadoBusqueda = gestorUsuarios.iniciarSesion(email);

        if (resultadoBusqueda.isEmpty()) {
            return ResultadoLogin.USUARIO_NO_ENCONTRADO;
        }

        Usuario usuario = resultadoBusqueda.get();

        // 🔑 Lógica de negocio encapsulada
        if (usuario instanceof Conductor conductor) {
            activarConductor(conductor); // Activa y persiste
            return ResultadoLogin.CONDUCTOR_LOGUEADO;
        }

        if (usuario instanceof Cliente) {
            return ResultadoLogin.CLIENTE_LOGUEADO;
        }

        return ResultadoLogin.ERROR_GENERAL;
    }

    // ========================================
    // FACHADA: LÓGICA DE NEGOCIO DEL CONDUCTOR
    // ========================================

    // Activa el conductor y persiste el estado
    public void activarConductor(Conductor conductor) {
        if (conductor != null) {
            conductor.setDisponible(true);
            gestorUsuarios.guardarUsuarios(); // Persistencia
        }
    }

    // Desactiva el conductor y persiste el estado
    public void desactivarConductor(Conductor conductor) {
        if (conductor != null) {
            conductor.setDisponible(false);
            gestorUsuarios.guardarUsuarios(); // Persistencia
        }
    }

    // ========================================
    // OBTENER CONDUCTOR DISPONIBLE
    // ========================================
    public Conductor obtenerConductorDisponible() {
        List<Conductor> disponibles = gestorUsuarios.obtenerConductoresDisponibles();

        if (disponibles.isEmpty()) {
            return null;
        }

        // Simular la selección de un conductor al azar
        Random random = new Random();
        return disponibles.get(random.nextInt(disponibles.size()));
    }

    // ========================================
    // GESTIÓN DE VIAJES
    // ========================================
    public void agregarViaje(Viaje viaje) {
        viajes.add(viaje);
        guardarViajes();
    }

    private void guardarViajes() {
        try {
            objectMapper.writeValue(new File(ARCHIVO_VIAJES), viajes);
        } catch (IOException e) {
            System.err.println("Error al guardar viajes: " + e.getMessage());
        }
    }

    private List<Viaje> cargarViajes() {
        File archivo = new File(ARCHIVO_VIAJES);

        if (!archivo.exists() || archivo.length() == 0) {
            System.out.println("⚠️ Archivo de viajes vacío o no existe. Iniciando lista vacía.");
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(archivo, new TypeReference<List<Viaje>>() {});
        } catch (IOException e) {
            System.err.println("❌ Error al cargar viajes (archivo corrupto): " + e.getMessage());
            System.err.println("   Se creará un nuevo archivo al guardar el próximo viaje.");

            // OPCIÓN: Hacer backup del archivo corrupto
            try {
                Files.copy(archivo.toPath(),
                        new File(ARCHIVO_VIAJES + ".backup").toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
                System.out.println("✅ Backup creado: viajes.json.backup");
            } catch (IOException backupError) {
                System.err.println("⚠️ No se pudo crear backup: " + backupError.getMessage());
            }

            return new ArrayList<>();
        }
    }
}