package Main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class GestorUsuarios {

    private static final String ARCHIVO_USUARIOS = "usuarios.json";
    private List<Cliente> clientes;
    private Gson gson;

    public GestorUsuarios() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.clientes = cargarUsuarios();
    }

    // ========================================
    // VALIDACIÓN DE EMAIL CON PATTERN MATCHING
    // ========================================
    public boolean validarEmail(String email) {
        Pattern patron = Pattern.compile("^[A-Za-z0-9+_.-]+@gmail\\.com$");
        return patron.matcher(email).matches();
    }

    // ========================================
    // REGISTRAR NUEVO CLIENTE
    // ========================================
    public boolean registrarCliente(String nombre, String email) {
        if (!validarEmail(email)) {
            return false;
        }

        if (buscarClientePorEmail(email).isPresent()) {
            return false;
        }

        Cliente nuevoCliente = new Cliente(nombre, email);
        clientes.add(nuevoCliente);
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
    // BUSCAR CLIENTE POR EMAIL
    // ========================================
    private Optional<Cliente> buscarClientePorEmail(String email) {
        return clientes.stream()
                .filter(cliente -> cliente.email().equalsIgnoreCase(email))
                .findFirst();
    }

    // ========================================
    // GUARDAR EN JSON
    // ========================================
    private void guardarUsuarios() {
        try (FileWriter writer = new FileWriter(ARCHIVO_USUARIOS)) {
            gson.toJson(clientes, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    // ========================================
    // CARGAR DESDE JSON
    // ========================================
    private List<Cliente> cargarUsuarios() {
        File archivo = new File(ARCHIVO_USUARIOS);

        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(ARCHIVO_USUARIOS)) {
            Type listType = new TypeToken<ArrayList<Cliente>>(){}.getType();
            List<Cliente> clientesCargados = gson.fromJson(reader, listType);
            return clientesCargados != null ? clientesCargados : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al cargar usuarios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ========================================
    // OBTENER TODOS LOS CLIENTES
    // ========================================
    public List<Cliente> obtenerTodosLosClientes() {
        return new ArrayList<>(clientes);
    }
}