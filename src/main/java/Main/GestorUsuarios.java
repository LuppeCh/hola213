package Main;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        this.objectMapper.registerSubtypes(Cliente.class, Conductor.class);
        this.usuarios = cargarUsuarios();
    }

    // ========================================
    // PERSISTENCIA
    // ========================================

    public void guardarUsuarios() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(ARCHIVO_USUARIOS), usuarios);
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<Usuario> cargarUsuarios() {
        File file = new File(ARCHIVO_USUARIOS);
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, new TypeReference<List<Usuario>>() {});
            } catch (IOException e) {
                System.err.println("Error al cargar usuarios, iniciando lista vacía: " + e.getMessage());
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    // ========================================
    // REGISTRO
    // ========================================

    public boolean registrarCliente(String nombre, String email) {
        if (!validarEmail(email)) return false;
        if (buscarUsuarioPorEmail(email).isPresent()) return false;

        Cliente nuevoCliente = new Cliente(nombre, email);
        usuarios.add(nuevoCliente);
        guardarUsuarios();
        return true;
    }

    public boolean registrarConductor(String nombre, String email) {
        if (!validarEmail(email)) return false;
        if (buscarUsuarioPorEmail(email).isPresent()) return false;

        Conductor nuevoConductor = new Conductor(nombre, email);
        usuarios.add(nuevoConductor);
        guardarUsuarios();
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
                .filter(u -> u.email().equalsIgnoreCase(email))
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