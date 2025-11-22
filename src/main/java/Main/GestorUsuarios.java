package Main;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class GestorUsuarios {

    private static final String ARCHIVO_USUARIOS = "usuarios.json";
    private final List<Usuario> usuarios;
    private final ObjectMapper objectMapper;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@gmail\\.com$");

    public GestorUsuarios() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // Para LocalDate
        this.objectMapper.registerSubtypes(Cliente.class, Conductor.class);
        this.usuarios = cargarUsuarios();
    }

    // ========================================
    // PERSISTENCIA
    // ========================================

    public void guardarUsuarios() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(ARCHIVO_USUARIOS), usuarios);
            System.out.println("✅ Usuarios guardados exitosamente");
        } catch (IOException e) {
            System.err.println("❌ Error al guardar usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<Usuario> cargarUsuarios() {
        File file = new File(ARCHIVO_USUARIOS);
        if (file.exists() && file.length() > 0) {
            try {
                List<Usuario> cargados = objectMapper.readValue(
                        file,
                        new TypeReference<List<Usuario>>() {}
                );
                System.out.println("✅ Usuarios cargados: " + cargados.size());
                return cargados;
            } catch (IOException e) {
                System.err.println("❌ Error al cargar usuarios: " + e.getMessage());
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
        System.out.println("⚠️ Archivo de usuarios no existe, creando lista vacía");
        return new ArrayList<>();
    }

    // ========================================
    // REGISTRO - CON TODOS LOS CAMPOS
    // ========================================

    public boolean registrarCliente(String nombre, String apellido, String dni,
                                    int edad, String contrasena, String gmail) {
        if (!validarEmail(gmail)) {
            System.err.println("❌ Email inválido: " + gmail);
            return false;
        }

        if (buscarUsuarioPorEmail(gmail).isPresent()) {
            System.err.println("❌ Email ya registrado: " + gmail);
            return false;
        }

        Cliente nuevoCliente = new Cliente(nombre, apellido, dni, edad, contrasena, gmail);
        usuarios.add(nuevoCliente);
        guardarUsuarios();
        System.out.println("✅ Cliente registrado: " + gmail);
        return true;
    }

    public boolean registrarConductor(String nombre, String apellido, String dni,
                                      int edad, String contrasena, String gmail,
                                      String modeloAuto, String patenteAuto,
                                      boolean tieneAire, int capacidad,
                                      double latitud, double longitud) {
        if (!validarEmail(gmail)) {
            System.err.println("❌ Email inválido: " + gmail);
            return false;
        }

        if (buscarUsuarioPorEmail(gmail).isPresent()) {
            System.err.println("❌ Email ya registrado: " + gmail);
            return false;
        }

        Conductor nuevoConductor = new Conductor(
                nombre, apellido, dni, edad, contrasena, gmail,
                modeloAuto, patenteAuto, tieneAire, capacidad,
                latitud, longitud
        );
        usuarios.add(nuevoConductor);
        guardarUsuarios();
        System.out.println("✅ Conductor registrado: " + gmail);
        return true;
    }

    // ========================================
    // INICIO DE SESIÓN Y BÚSQUEDA
    // ========================================

    public Optional<Usuario> iniciarSesion(String email) {
        return buscarUsuarioPorEmail(email);
    }

    private Optional<Usuario> buscarUsuarioPorEmail(String email) {
        return usuarios.stream()
                .filter(u -> u.getGmail().equalsIgnoreCase(email))
                .findFirst();
    }

    // ========================================
    // UTILIDADES
    // ========================================

    public List<Cliente> obtenerTodosLosClientes() {
        return usuarios.stream()
                .filter(usuario -> usuario instanceof Cliente)
                .map(usuario -> (Cliente) usuario)
                .toList();
    }

    public List<Conductor> obtenerConductoresDisponibles() {
        return usuarios.stream()
                .filter(usuario -> usuario instanceof Conductor)
                .map(usuario -> (Conductor) usuario)
                .filter(Conductor::isDisponible)
                .toList();
    }

    private boolean validarEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
}