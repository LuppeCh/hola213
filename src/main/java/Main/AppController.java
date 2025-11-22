package Main;

import Viajes.*;
import javafx.stage.Stage;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AppController {

    private final Stage stage;
    private final SistemaFachada sistema;
    private final NavegadorPantallas navegador;
    private final UIFactory uiFactory;

    // Estado de sesión
    private Usuario usuarioActual;
    private String origenSeleccionado;
    private String destinoSeleccionado;
    private Ruta rutaActual;

    public AppController(Stage stage) {
        this.stage = stage;
        this.sistema = new SistemaFachada();
        this.navegador = new NavegadorPantallas(stage);
        this.uiFactory = new UIFactory(this, stage);

        registrarPantallas();
    }

    // ========================================
    // INICIALIZACIÓN
    // ========================================

    private void registrarPantallas() {
        navegador.registrarPantalla(Pantalla.TITULO, () -> uiFactory.crearPantallaTitulo());
        navegador.registrarPantalla(Pantalla.LOGIN_REGISTRO, () -> uiFactory.crearPantallaLoginRegistro());
        navegador.registrarPantalla(Pantalla.SELECCION_TIPO_REGISTRO, () -> uiFactory.crearPantallaSeleccionTipoRegistro());
        navegador.registrarPantalla(Pantalla.REGISTRO_CLIENTE, () -> uiFactory.crearPantallaRegistroCliente());
        navegador.registrarPantalla(Pantalla.REGISTRO_CONDUCTOR, () -> uiFactory.crearPantallaRegistroConductor());
        navegador.registrarPantalla(Pantalla.LOGIN, () -> uiFactory.crearPantallaLogin());
        navegador.registrarPantalla(Pantalla.BIENVENIDA_CLIENTE, () -> uiFactory.crearPantallaBienvenidaCliente());
        navegador.registrarPantalla(Pantalla.DASHBOARD_CONDUCTOR, () -> uiFactory.crearPantallaDashboardConductor());
    }

    public void iniciar() {
        navegador.navegarA(Pantalla.TITULO);
    }

    // ========================================
    // NAVEGACIÓN
    // ========================================

    public void navegarA(Pantalla pantalla) {
        navegador.navegarA(pantalla);
    }

    // ========================================
    // OPERACIONES DE AUTENTICACIÓN
    // ========================================

    public ResultadoOperacion registrarCliente(String nombre, String apellido, String dni,
                                               int edad, String contrasena, String gmail) {
        boolean exitoso = sistema.registrarCliente(nombre, apellido, dni, edad, contrasena, gmail);

        if (exitoso) {
            return new ResultadoOperacion(true, "✅ Registro exitoso! Ahora puedes iniciar sesión");
        } else {
            return new ResultadoOperacion(false, "❌ Email inválido o ya registrado (debe ser @gmail.com)");
        }
    }

    public ResultadoOperacion registrarConductor(String nombre, String apellido, String dni,
                                                 int edad, String contrasena, String gmail,
                                                 String modeloAuto, String patenteAuto,
                                                 boolean tieneAire, int capacidad,
                                                 double latitud, double longitud) {
        boolean exitoso = sistema.registrarConductor(
                nombre, apellido, dni, edad, contrasena, gmail,
                modeloAuto, patenteAuto, tieneAire, capacidad,
                latitud, longitud
        );

        if (exitoso) {
            return new ResultadoOperacion(true, "✅ Registro exitoso! Ahora puedes iniciar sesión");
        } else {
            return new ResultadoOperacion(false, "❌ Email inválido o ya registrado (debe ser @gmail.com)");
        }
    }

    public ResultadoOperacion iniciarSesion(String email) {
        if (email == null || email.trim().isEmpty()) {
            return new ResultadoOperacion(false, "⚠️ Debe ingresar un email");
        }

        ResultadoLogin resultado = sistema.iniciarSesion(email);

        switch (resultado) {
            case CLIENTE_LOGUEADO:
                usuarioActual = sistema.obtenerUsuario(email);
                navegarA(Pantalla.BIENVENIDA_CLIENTE);
                return new ResultadoOperacion(true, "Login exitoso como cliente");

            case CONDUCTOR_LOGUEADO:
                usuarioActual = sistema.obtenerUsuario(email);
                navegarA(Pantalla.DASHBOARD_CONDUCTOR);
                return new ResultadoOperacion(true, "Login exitoso como conductor");

            case USUARIO_NO_ENCONTRADO:
                return new ResultadoOperacion(false, "❌ Usuario no encontrado");

            default:
                return new ResultadoOperacion(false, "❌ Error al iniciar sesión");
        }
    }

    public void cerrarSesion() {
        if (usuarioActual instanceof Conductor conductor) {
            sistema.desactivarConductor(conductor);
        }

        usuarioActual = null;
        origenSeleccionado = null;
        destinoSeleccionado = null;
        rutaActual = null;

        navegarA(Pantalla.TITULO);
    }

    // ========================================
    // GESTIÓN DE VIAJES
    // ========================================

    public void setOrigen(String origen) {
        this.origenSeleccionado = origen;
    }

    public void setDestino(String destino) {
        this.destinoSeleccionado = destino;
    }

    public String getOrigen() {
        return origenSeleccionado;
    }

    public String getDestino() {
        return destinoSeleccionado;
    }

    public CompletableFuture<List<String>> autocompletarLugar(String texto) {
        return sistema.autocompletarAsync(texto);
    }

    public CompletableFuture<Ruta> calcularRuta() {
        if (origenSeleccionado == null || destinoSeleccionado == null) {
            return CompletableFuture.failedFuture(
                    new IllegalStateException("Debe seleccionar origen y destino")
            );
        }

        return sistema.calcularRutaAsync(origenSeleccionado, destinoSeleccionado)
                .thenApply(ruta -> {
                    this.rutaActual = ruta;
                    return ruta;
                });
    }

    public SistemaFachada.ResultadoPrecio calcularPrecio(Ruta ruta, TipoViaje tipo) {
        Cliente cliente = getClienteActual();
        if (cliente == null) {
            throw new IllegalStateException("No hay cliente logueado");
        }

        return sistema.calcularPrecioViaje(ruta, cliente, tipo,
                origenSeleccionado, destinoSeleccionado);
    }

    public ResultadoOperacion confirmarViaje(TipoViaje tipo) {
        if (!(usuarioActual instanceof Cliente cliente)) {
            return new ResultadoOperacion(false, "Error: No hay cliente en sesión");
        }

        if (rutaActual == null) {
            return new ResultadoOperacion(false, "Error: No hay ruta calculada");
        }

        SistemaFachada.ResultadoViaje resultado = sistema.crearViaje(rutaActual, cliente, tipo);

        if (resultado.exitoso()) {
            Viaje viaje = resultado.viaje();
            String mensaje = String.format(
                    "🎉 ¡Viaje Confirmado!\nTipo: %s | Conductor: %s\nDistancia: %s | Tiempo: %s\n¡Buen viaje! 🚗",
                    tipo,
                    viaje.getConductor().getNombre(),
                    rutaActual.distanciaTexto(),
                    rutaActual.tiempoTexto()
            );

            origenSeleccionado = null;
            destinoSeleccionado = null;
            rutaActual = null;

            return new ResultadoOperacion(true, mensaje);
        } else {
            return new ResultadoOperacion(false, "❌ " + resultado.mensaje());
        }
    }

    // ========================================
    // GETTERS DE ESTADO
    // ========================================

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public Cliente getClienteActual() {
        return usuarioActual instanceof Cliente ? (Cliente) usuarioActual : null;
    }

    public Conductor getConductorActual() {
        return usuarioActual instanceof Conductor ? (Conductor) usuarioActual : null;
    }

    public Ruta getRutaActual() {
        return rutaActual;
    }

    public Stage getStage() {
        return stage;
    }

    // ========================================
    // LIMPIEZA
    // ========================================

    public void cerrarAplicacion() {
        sistema.cerrarRecursos();
    }

    // ========================================
    // RECORD: RESULTADO DE OPERACIÓN
    // ========================================

    public record ResultadoOperacion(
            boolean exitoso,
            String mensaje
    ) {}
}