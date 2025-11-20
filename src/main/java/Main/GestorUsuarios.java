package Main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class GestorUsuarios {

    private static final String ARCHIVO_USUARIOS = "usuarios.dat";
    private List<Cliente> clientes;

    public GestorUsuarios() {
        this.clientes = cargarUsuarios();
    }

    // ========================================
    // VALIDACIÓN DE EMAIL CON PATTERN MATCHING
    // ========================================
    public boolean validarEmail(String email) {
        // Pattern matching para validar formato @gmail.com
        Pattern patron = Pattern.compile("^[A-Za-z0-9+_.-]+@gmail\\.com$");
        return patron.matcher(email).matches();
    }

    // ========================================
    // REGISTRAR NUEVO CLIENTE
    // ========================================
    public boolean registrarCliente(String nombre, String email) {
        // Validar email
        if (!validarEmail(email)) {
            return false;
        }

        // Verificar que no exista ya
        if (buscarClientePorEmail(email).isPresent()) {
            return false; // Ya existe
        }

        // Crear y agregar nuevo cliente
        Cliente nuevoCliente = new Cliente(nombre, email);
        clientes.add(nuevoCliente);

        // Guardar en archivo
        guardarUsuarios();

        return true;
    }

    // ========================================
    // INICIAR SESIÓN
    // ========================================
    public Optional<Cliente> iniciarSesion(String email) {
        return buscarClientePorEmail(email);
    }

    // ========================================
    // BUSCAR CLIENTE POR EMAIL (usando Streams)
    // ========================================
    private Optional<Cliente> buscarClientePorEmail(String email) {
        return clientes.stream()
                .filter(cliente -> cliente.email().equalsIgnoreCase(email))
                .findFirst();
    }

    // ========================================
    // SERIALIZACIÓN - GUARDAR
    // ========================================
    private void guardarUsuarios() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARCHIVO_USUARIOS))) {
            oos.writeObject(clientes);
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    // ========================================
    // DESERIALIZACIÓN - CARGAR
    // ========================================
    @SuppressWarnings("unchecked")
    private List<Cliente> cargarUsuarios() {
        File archivo = new File(ARCHIVO_USUARIOS);

        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ARCHIVO_USUARIOS))) {
            return (List<Cliente>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ========================================
    // OBTENER TODOS LOS CLIENTES (para futuro uso con Streams)
    // ========================================
    public List<Cliente> obtenerTodosLosClientes() {
        return new ArrayList<>(clientes);
    }
}