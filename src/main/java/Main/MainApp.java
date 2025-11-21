package Main;

import Viajes.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.beans.binding.Bindings;
import javafx.scene.paint.Color;

public class MainApp extends Application {

    private Stage stage;
    private GestorUsuarios gestorUsuarios;
    private Sistema sistema;
    private Cliente clienteActual;
    private GooglePlacesService placesService;
    private DistanceCalculator distanceCalculator;
    private Conductor conductorActual;

    private String origenSeleccionado = null;
    private String destinoSeleccionado = null;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.stage = primaryStage;
        this.gestorUsuarios = new GestorUsuarios();
        this.sistema = new Sistema(gestorUsuarios);
        this.placesService = new GooglePlacesService("AIzaSyB6uynr_3ELge4l5JrkDNGh3JYs-zO53DI");
        this.distanceCalculator = new DistanceCalculator();

        stage.setTitle("Uber - Sistema de Viajes");
        stage.setWidth(900);
        stage.setHeight(700);
        mostrarPantallaTitulo();
        stage.show();
    }

    // ========================================
    // PANTALLA 1: TITLE SCREEN
    // ========================================
    private void mostrarPantallaTitulo() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.spacingProperty().bind(stage.heightProperty().divide(15));

        Label lblTitulo = new Label("🚗 BIENVENIDO A UBER 🚗");
        lblTitulo.setStyle("-fx-font-weight: bold;");
        lblTitulo.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(25), "px; -fx-font-weight: bold;"
        ));

        Button btnViajar = new Button("Viajar");
        btnViajar.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(40), "px; ",
                "-fx-padding: ", stage.heightProperty().divide(50), " ", stage.widthProperty().divide(20), ";"
        ));
        btnViajar.setOnAction(e -> mostrarPantallaLoginRegistro());

        root.getChildren().addAll(lblTitulo, btnViajar);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #667eea 0%, #764ba2 100%);");

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    // ========================================
    // PANTALLA 2: LOGIN / REGISTRO
    // ========================================
    // ========================================
// PANTALLA 2: LOGIN / REGISTRO
// ========================================
    private void mostrarPantallaLoginRegistro() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.spacingProperty().bind(stage.heightProperty().divide(20));

        Label lblTitulo = new Label("¿Qué deseas hacer?");
        lblTitulo.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(30), "px; -fx-font-weight: bold;"
        ));

        Button btnIniciarSesion = new Button("Iniciar Sesión");
        btnIniciarSesion.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(45), "px; ",
                "-fx-padding: ", stage.heightProperty().divide(60), " ", stage.widthProperty().divide(15), ";"
        ));
        btnIniciarSesion.setOnAction(e -> mostrarPantallaLogin());

        Button btnRegistrarse = new Button("Registrarse por primera vez");
        btnRegistrarse.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(45), "px; ",
                "-fx-padding: ", stage.heightProperty().divide(60), " ", stage.widthProperty().divide(15), ";"
        ));
        // 🔑 CAMBIO: Redirigir a la selección de rol
        btnRegistrarse.setOnAction(e -> mostrarPantallaSeleccionTipoRegistro());

        root.getChildren().addAll(lblTitulo, btnIniciarSesion, btnRegistrarse);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #f093fb 0%, #f5576c 100%);");

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    // ========================================
// PANTALLA 3 (NUEVA): SELECCIÓN DE TIPO DE REGISTRO
// ========================================
    private void mostrarPantallaSeleccionTipoRegistro() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #a18cd1 0%, #fbc2eb 100%);");

        Label lblTitulo = new Label("Elige tu rol");
        lblTitulo.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(30), "px; -fx-font-weight: bold; -fx-text-fill: white;"
        ));

        Button btnCliente = new Button("👤 Cliente (Necesito un viaje)");
        btnCliente.setPrefWidth(300);
        btnCliente.setStyle("-fx-font-size: 18px; -fx-padding: 10 20; -fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5;");
        // Redirige a la pantalla de registro, pasando TRUE (es Cliente)
        btnCliente.setOnAction(e -> mostrarPantallaRegistro(true));

        Button btnConductor = new Button("🚗 Conductor (Quiero trabajar)");
        btnConductor.setPrefWidth(300);
        btnConductor.setStyle("-fx-font-size: 18px; -fx-padding: 10 20; -fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-radius: 5;");
        // Redirige a la pantalla de registro, pasando FALSE (es Conductor)
        btnConductor.setOnAction(e -> mostrarPantallaRegistro(false));

        Button btnVolver = new Button("← Volver");
        btnVolver.setStyle("-fx-font-size: 14px;");
        btnVolver.setOnAction(e -> mostrarPantallaLoginRegistro());


        root.getChildren().addAll(lblTitulo, btnCliente, btnConductor, btnVolver);
        stage.setScene(new Scene(root));
    }

    // ========================================
// PANTALLA 4: REGISTRO (UNIFICADA)
// ========================================
    private void mostrarPantallaRegistro(boolean esCliente) {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.spacingProperty().bind(stage.heightProperty().divide(35));

        // Título dinámico
        String tituloTexto = esCliente ? "Registro de Cliente" : "Registro de Conductor";
        Label lblTitulo = new Label(tituloTexto);
        lblTitulo.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(35), "px; -fx-font-weight: bold;"
        ));

        Label lblNombre = new Label("Nombre:");
        lblNombre.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(60), "px;"
        ));

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Ingrese su nombre");
        txtNombre.maxWidthProperty().bind(stage.widthProperty().divide(4));
        txtNombre.prefHeightProperty().bind(stage.heightProperty().divide(20));
        txtNombre.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(70), "px;"
        ));

        Label lblEmail = new Label("Email:");
        lblEmail.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(60), "px;"
        ));

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("ejemplo@gmail.com");
        txtEmail.maxWidthProperty().bind(stage.widthProperty().divide(4));
        txtEmail.prefHeightProperty().bind(stage.heightProperty().divide(20));
        txtEmail.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(70), "px;"
        ));

        Label lblMensaje = new Label("");
        lblMensaje.setStyle("-fx-font-weight: bold;");
        lblMensaje.styleProperty().bind(Bindings.concat(
                "-fx-font-weight: bold; -fx-font-size: ", stage.widthProperty().divide(70), "px;"
        ));

        Button btnRegistrar = new Button("Registrarse");
        btnRegistrar.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(55), "px; ",
                "-fx-padding: ", stage.heightProperty().divide(70), " ", stage.widthProperty().divide(25), ";"
        ));

        // 🔑 LÓGICA DE REGISTRO CONDICIONAL
        btnRegistrar.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            String email = txtEmail.getText().trim();

            if (nombre.isEmpty() || email.isEmpty()) {
                lblMensaje.setText("⚠️ Todos los campos son obligatorios");
                lblMensaje.setTextFill(Color.RED);
                return;
            }

            boolean registroExitoso;

            if (esCliente) {
                registroExitoso = gestorUsuarios.registrarCliente(nombre, email);
            } else {
                registroExitoso = gestorUsuarios.registrarConductor(nombre, email);
            }

            if (registroExitoso) {
                lblMensaje.setText("✅ Registro exitoso! Ahora puedes iniciar sesión");
                lblMensaje.setTextFill(Color.GREEN);
                txtNombre.clear();
                txtEmail.clear();
            } else {
                lblMensaje.setText("❌ Email inválido o ya registrado (debe ser @gmail.com)");
                lblMensaje.setTextFill(Color.RED);
            }
        });

        Button btnVolver = new Button("← Volver");
        btnVolver.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(70), "px;"
        ));
        // Vuelve a la selección de tipo de usuario
        btnVolver.setOnAction(e -> mostrarPantallaSeleccionTipoRegistro());

        root.getChildren().addAll(
                lblTitulo,
                lblNombre, txtNombre,
                lblEmail, txtEmail,
                btnRegistrar,
                lblMensaje,
                btnVolver
        );

        root.setStyle("-fx-background-color: linear-gradient(to bottom, #a8edea 0%, #fed6e3 100%);");

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    // ========================================
// PANTALLA 5: LOGIN
// ========================================
    private void mostrarPantallaLogin() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.spacingProperty().bind(stage.heightProperty().divide(35));

        Label lblTitulo = new Label("Iniciar Sesión");
        lblTitulo.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(35), "px; -fx-font-weight: bold;"
        ));

        Label lblEmail = new Label("Email:");
        lblEmail.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(60), "px;"
        ));

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("ejemplo@gmail.com");
        txtEmail.maxWidthProperty().bind(stage.widthProperty().divide(4));
        txtEmail.prefHeightProperty().bind(stage.heightProperty().divide(20));
        txtEmail.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(70), "px;"
        ));

        Label lblMensaje = new Label("");
        lblMensaje.setStyle("-fx-font-weight: bold;");
        lblMensaje.styleProperty().bind(Bindings.concat(
                "-fx-font-weight: bold; -fx-font-size: ", stage.widthProperty().divide(70), "px;"
        ));

        Button btnLogin = new Button("Ingresar");
        btnLogin.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(55), "px; ",
                "-fx-padding: ", stage.heightProperty().divide(70), " ", stage.widthProperty().divide(25), ";"
        ));

        // 🔑 LÓGICA DE INICIO DE SESIÓN CONDICIONAL
        btnLogin.setOnAction(e -> {
            String email = txtEmail.getText().trim();

            if (email.isEmpty()) {
                lblMensaje.setText("⚠️ Debe ingresar un email");
                lblMensaje.setTextFill(Color.RED);
                return;
            }

            // Asumiendo que gestorUsuarios.iniciarSesion devuelve Optional<Usuario>
            var resultado = gestorUsuarios.iniciarSesion(email);

            if (resultado.isPresent()) {
                Usuario usuario = resultado.get();

                // Caso 1: Es un Cliente
                if (usuario instanceof Cliente) {
                    clienteActual = (Cliente) usuario;
                    conductorActual = null; // Limpiar conductor
                    mostrarPantallaBienvenida();

                    // Caso 2: Es un Conductor
                } else if (usuario instanceof Conductor) {
                    conductorActual = (Conductor) usuario;
                    clienteActual = null; // Limpiar cliente

                    // 🟢 ACTIVAR DISPONIBILIDAD y GUARDAR
                    conductorActual.setDisponible(true);
                    gestorUsuarios.guardarUsuarios();

                    // ➡️ Redirigir al dashboard del Conductor (pendiente de crear)
                    mostrarPantallaConductorDashboard();

                } else {
                    lblMensaje.setText("❌ Tipo de usuario no reconocido.");
                    lblMensaje.setTextFill(Color.RED);
                }
            } else {
                lblMensaje.setText("❌ Usuario no encontrado");
                lblMensaje.setTextFill(Color.RED);
            }
        });

        Button btnVolver = new Button("← Volver");
        btnVolver.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(70), "px;"
        ));
        // Vuelve a la selección de Login/Registro
        btnVolver.setOnAction(e -> mostrarPantallaLoginRegistro());

        root.getChildren().addAll(
                lblTitulo,
                lblEmail, txtEmail,
                btnLogin,
                lblMensaje,
                btnVolver
        );

        root.setStyle("-fx-background-color: linear-gradient(to bottom, #ffecd2 0%, #fcb69f 100%);");

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    // ========================================
// PANTALLA 6 (NUEVA): DASHBOARD CONDUCTOR
// ========================================
    private void mostrarPantallaConductorDashboard() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #d4fc79 0%, #96e6a1 100%);"); // Fondo verde/lima

        Label lblBienvenida = new Label("Hola Conductor " + conductorActual.nombre());
        lblBienvenida.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Label lblEstado = new Label("🟢 ¡Estás disponible para viajes!");
        lblEstado.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #008000;");

        // Placeholder para futuros viajes
        Label lblInfo = new Label("Esperando solicitudes de viaje...");
        lblInfo.setStyle("-fx-font-size: 16px; -fx-text-fill: #555; -fx-padding: 10 0 0 0;");

        Button btnCerrarSesion = new Button("Cerrar Sesión y Desconectarse");
        btnCerrarSesion.setStyle("-fx-font-size: 18px; -fx-padding: 10 30; -fx-background-color: #DC3545; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;");

        // 🔴 DESACTIVAR DISPONIBILIDAD y GUARDAR
        btnCerrarSesion.setOnAction(e -> {
            if (conductorActual != null) {
                conductorActual.setDisponible(false); // <--- Disponibilidad a FALSE
                gestorUsuarios.guardarUsuarios();    // Guardar el cambio inmediatamente
                conductorActual = null;              // Limpiar sesión
            }
            mostrarPantallaTitulo();
        });

        root.getChildren().addAll(lblBienvenida, lblEstado, lblInfo, btnCerrarSesion);
        stage.setScene(new Scene(root));
    }



    // ========================================
    // PANTALLA 5: BIENVENIDA CON AUTOCOMPLETADO
    // ========================================
    private void mostrarPantallaBienvenida() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        // Usar un color de fondo blanco suave para el ScrollPane
        scrollPane.setStyle("-fx-background: #F8F9FA; -fx-background-color: #F8F9FA;");

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50, 50, 50, 50)); // Más padding vertical
        root.spacingProperty().bind(stage.heightProperty().divide(25)); // Aumentar espaciado entre secciones

        // 1. ESTILO DE CABECERA (Más limpio)
        Label lblBienvenida = new Label("¡Bienvenido/a " + clienteActual.nombre() + "!");
        lblBienvenida.setTextFill(Color.web("#343A40")); // Gris oscuro para profesionalismo
        lblBienvenida.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(28), "px; -fx-font-weight: bold;"
        ));

        Label lblEmail = new Label("Email: " + clienteActual.email());
        lblEmail.setTextFill(Color.web("#6C757D")); // Gris claro
        lblEmail.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(50), "px; -fx-padding: 0 0 20 0;"
        ));

        // Label para mensajes
        Label lblMensaje = new Label("");
        lblMensaje.setWrapText(true);
        lblMensaje.setMaxWidth(600);
        lblMensaje.setAlignment(Pos.CENTER);
        lblMensaje.setStyle("-fx-font-weight: bold;");
        lblMensaje.styleProperty().bind(Bindings.concat(
                "-fx-font-weight: bold; -fx-font-size: ", stage.widthProperty().divide(60), "px;"
        ));

        // ==============================
        // CONTENEDORES DE ENTRADA (MÁS LIMPIOS)
        // ==============================

        // Contenedor principal de campos (para centrar y limitar ancho)
        VBox inputContainer = new VBox(25);
        inputContainer.setAlignment(Pos.CENTER);
        inputContainer.setMaxWidth(400); // Limitar el ancho de los campos

        // ===== ORIGEN =====
        Label lblOrigenTitulo = new Label("Origen:");
        lblOrigenTitulo.setTextFill(Color.web("#343A40"));
        lblOrigenTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        TextField txtOrigen = new TextField();
        txtOrigen.setPromptText("Escribe tu origen (ej: Avenida del Libertador 100)");
        txtOrigen.setPrefHeight(40);
        txtOrigen.setStyle("-fx-font-size: 14px; -fx-border-color: #CED4DA; -fx-border-radius: 5; -fx-background-radius: 5; -fx-border-width: 1px;");

        ListView<String> listOrigen = new ListView<>();
        listOrigen.setPrefHeight(0);
        listOrigen.setVisible(false);
        listOrigen.maxWidthProperty().bind(txtOrigen.widthProperty());
        listOrigen.setStyle("-fx-border-color: #ADB5BD; -fx-border-width: 1px 0 0 0; -fx-background-color: white;");

        // Autocompletado Origen (Lógica no necesita cambios)
        txtOrigen.textProperty().addListener((obs, old, nuevo) -> {
            if (nuevo.length() >= 3) {
                new Thread(() -> {
                    try {
                        var sugerencias = placesService.autocompletar(nuevo);
                        Platform.runLater(() -> {
                            listOrigen.getItems().setAll(sugerencias);
                            listOrigen.setPrefHeight(Math.min(sugerencias.size() * 30, 150)); // Altura ajustada
                            listOrigen.setVisible(!sugerencias.isEmpty());
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                listOrigen.setVisible(false);
            }
            origenSeleccionado = txtOrigen.getText(); // Actualizar selección al escribir
        });

        listOrigen.setOnMouseClicked(event -> {
            String seleccion = listOrigen.getSelectionModel().getSelectedItem();
            if (seleccion != null) {
                txtOrigen.setText(seleccion);
                origenSeleccionado = seleccion;
                listOrigen.setVisible(false);
                lblMensaje.setText("");
            }
        });

        VBox origenContainer = new VBox(5, lblOrigenTitulo, txtOrigen, listOrigen);

        // ===== DESTINO =====
        Label lblDestinoTitulo = new Label("Destino:");
        lblDestinoTitulo.setTextFill(Color.web("#343A40"));
        lblDestinoTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        TextField txtDestino = new TextField();
        txtDestino.setPromptText("Escribe tu destino");
        txtDestino.setPrefHeight(40);
        txtDestino.setStyle("-fx-font-size: 14px; -fx-border-color: #CED4DA; -fx-border-radius: 5; -fx-background-radius: 5; -fx-border-width: 1px;");

        ListView<String> listDestino = new ListView<>();
        listDestino.setPrefHeight(0);
        listDestino.setVisible(false);
        listDestino.maxWidthProperty().bind(txtDestino.widthProperty());
        listDestino.setStyle("-fx-border-color: #ADB5BD; -fx-border-width: 1px 0 0 0; -fx-background-color: white;");

        // Autocompletado Destino (Lógica no necesita cambios)
        txtDestino.textProperty().addListener((obs, old, nuevo) -> {
            if (nuevo.length() >= 3) {
                new Thread(() -> {
                    try {
                        var sugerencias = placesService.autocompletar(nuevo);
                        Platform.runLater(() -> {
                            listDestino.getItems().setAll(sugerencias);
                            listDestino.setPrefHeight(Math.min(sugerencias.size() * 30, 150));
                            listDestino.setVisible(!sugerencias.isEmpty());
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                listDestino.setVisible(false);
            }
            destinoSeleccionado = txtDestino.getText(); // Actualizar selección al escribir
        });

        listDestino.setOnMouseClicked(event -> {
            String seleccion = listDestino.getSelectionModel().getSelectedItem();
            if (seleccion != null) {
                txtDestino.setText(seleccion);
                destinoSeleccionado = seleccion;
                listDestino.setVisible(false);
                lblMensaje.setText("");
            }
        });

        VBox destinoContainer = new VBox(5, lblDestinoTitulo, txtDestino, listDestino);

        inputContainer.getChildren().addAll(origenContainer, destinoContainer);

        // Contenedor para opciones de viaje (inicialmente vacío)
        VBox opcionesViajeContainer = new VBox(15);
        opcionesViajeContainer.setAlignment(Pos.CENTER);
        opcionesViajeContainer.setVisible(false);

        // ===== BOTÓN INICIAR VIAJE (REDESIGN) =====
        Button btnIniciarViaje = new Button("Iniciar Viaje");
        // Color primario de Uber (Negro o Azul Oscuro)
        btnIniciarViaje.setStyle(
                "-fx-font-size: 20px; " +
                        "-fx-padding: 15 40; " +
                        "-fx-background-color: #007BFF; " + // Azul Limpio
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 8; " +
                        "-fx-font-weight: bold;"
        );
        btnIniciarViaje.setPrefWidth(300); // Ancho fijo para centrar mejor

        // ===== BOTÓN CERRAR SESIÓN (REDESIGN) =====
        Button btnCerrarSesion = new Button("Cerrar Sesión");
        btnCerrarSesion.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-padding: 10 25; " +
                        "-fx-background-color: #6C757D; " + // Gris neutro
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 5;"
        );
        btnCerrarSesion.setOnAction(e -> {
            clienteActual = null;
            origenSeleccionado = null;
            destinoSeleccionado = null;
            mostrarPantallaTitulo();
        });

        btnIniciarViaje.setOnAction(e -> {
            if (origenSeleccionado == null || destinoSeleccionado == null || origenSeleccionado.isEmpty() || destinoSeleccionado.isEmpty()) {
                lblMensaje.setText("⚠️ Por favor, selecciona origen y destino antes de iniciar el viaje");
                lblMensaje.setTextFill(Color.web("#DC3545")); // Rojo de error
                opcionesViajeContainer.setVisible(false);
                return;
            }

            // Limpiar campos de texto
            txtOrigen.clear();
            txtDestino.clear();
            listOrigen.setVisible(false);
            listDestino.setVisible(false);

            // Mostrar mensaje de carga
            lblMensaje.setText("⏳ Calculando ruta, por favor espera...");
            lblMensaje.setTextFill(Color.web("#007BFF")); // Azul de carga
            opcionesViajeContainer.setVisible(false);

            // Calcular ruta
            calcularYMostrarOpciones(lblMensaje, opcionesViajeContainer, root, lblBienvenida, lblEmail, origenContainer, destinoContainer, btnIniciarViaje, btnCerrarSesion);
        });

        // Contenedor para los botones inferiores
        VBox bottomButtons = new VBox(10, btnIniciarViaje, btnCerrarSesion);
        bottomButtons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(
                lblBienvenida,
                lblEmail,
                lblMensaje,
                inputContainer,
                bottomButtons,
                opcionesViajeContainer // Mantener al final para que aparezca después de los inputs
        );

        // Fondo blanco limpio
        root.setStyle("-fx-background-color: #FFFFFF;");

        scrollPane.setContent(root);
        Scene scene = new Scene(scrollPane);
        stage.setScene(scene);
    }
    // ========================================
// CALCULAR RUTA Y MOSTRAR OPCIONES DE VIAJE
// ========================================
    private void calcularYMostrarOpciones(Label lblMensaje, VBox opcionesContainer, VBox root,
                                          Label lblBienvenida, Label lblEmail,
                                          VBox origenContainer, VBox destinoContainer,
                                          Button btnIniciarViaje, Button btnCerrarSesion) {
        new Thread(() -> {
            try {
                // Asegúrate de que Ruta sea una clase que soporta Jackson (como se discutió previamente)
                Ruta ruta = distanceCalculator.calculateRoute(origenSeleccionado, destinoSeleccionado);

                Platform.runLater(() -> {
                    // 1. Ocultar los campos de entrada y el botón de Iniciar Viaje
                    // Esto asume que estos elementos están fuera del 'opcionesContainer'
                    origenContainer.setVisible(false);
                    destinoContainer.setVisible(false);
                    btnIniciarViaje.setVisible(false); // Asumiendo que estaba visible

                    // 2. Limpiar opcionesContainer (donde se van a mostrar las opciones)
                    opcionesContainer.getChildren().clear();

                    // 3. Mostrar las opciones de viaje en el contenedor
                    mostrarOpcionesViajeEnPantalla(ruta, opcionesContainer, lblMensaje);

                    // 4. Mostrar el contenedor de opciones y el mensaje de éxito
                    opcionesContainer.setVisible(true);
                    lblMensaje.setText("✅ Ruta calculada. Selecciona una opción:");
                    lblMensaje.setTextFill(Color.web("#28A745")); // Verde de éxito
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    // Mostrar el error y asegurar que los campos de entrada estén visibles
                    lblMensaje.setText("❌ Error: No se pudo calcular la ruta\n" + e.getMessage());
                    lblMensaje.setTextFill(Color.RED);
                    lblMensaje.setVisible(true);

                    // Asegurar que la UI de búsqueda vuelva a estar visible para reintentar
                    origenContainer.setVisible(true);
                    destinoContainer.setVisible(true);
                    btnIniciarViaje.setVisible(true);
                    opcionesContainer.setVisible(false);
                });
            }
        }).start();
    }
    // ========================================
    // MOSTRAR OPCIONES DE VIAJE EN LA MISMA PANTALLA
    // ========================================
    private void mostrarOpcionesViajeEnPantalla(Ruta ruta, VBox opcionesContainer, Label lblMensaje) {
        opcionesContainer.getChildren().clear();

        // Crear un nuevo label para distancia y tiempo (sin bindings)
        Label lblInfoRuta = new Label("📍 Distancia: " + ruta.distanciaTexto() + " | ⏱️ Tiempo: " + ruta.tiempoTexto());
        lblInfoRuta.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");
        lblInfoRuta.setWrapText(true);
        lblInfoRuta.setMaxWidth(600);
        lblInfoRuta.setAlignment(Pos.CENTER);

        // Ocultar el mensaje de carga original
        lblMensaje.setVisible(false);

        Label titulo = new Label("Elige tu tipo de viaje:");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 20 0 15 0;");

        // Botones para cada tipo de viaje
        Button btnEstandar = crearBotonViajeInline("🚗 Estándar", ruta.tiempoTexto(), "#4CAF50");
        Button btnXL = crearBotonViajeInline("🚙 XL", ruta.tiempoTexto(), "#FF9800");
        Button btnLujo = crearBotonViajeInline("💎 Lujo", ruta.tiempoTexto(), "#9C27B0");

        // Acciones de los botones
        btnEstandar.setOnAction(e -> {
            confirmarViajeEnPantalla(ruta, TipoViaje.Estandar, lblMensaje, opcionesContainer);
        });

        btnXL.setOnAction(e -> {
            confirmarViajeEnPantalla(ruta, TipoViaje.XL, lblMensaje, opcionesContainer);
        });

        btnLujo.setOnAction(e -> {
            confirmarViajeEnPantalla(ruta, TipoViaje.Lujo, lblMensaje, opcionesContainer);
        });

        // ===== MINIMAPA CON EL RECORRIDO USANDO IFRAME =====
        Label lblMapa = new Label("📍 Visualización del recorrido:");
        lblMapa.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 20 0 10 0;");

        javafx.scene.web.WebView webView = new javafx.scene.web.WebView();
        webView.setPrefHeight(400);
        webView.setPrefWidth(650);

        // Generar HTML con iframe que carga el mapa de Google Maps
        String htmlContent = generarHTMLMapaConIframe(ruta.linkMapa());
        webView.getEngine().loadContent(htmlContent);

        // Botón para abrir mapa en navegador
        Button btnVerMapaCompleto = new Button("🗺️ Ver mapa completo en navegador");
        btnVerMapaCompleto.setStyle(
                "-fx-font-size: 13px; " +
                        "-fx-padding: 10 20; " +
                        "-fx-background-color: #2196F3; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 5; " +
                        "-fx-font-weight: bold;"
        );
        btnVerMapaCompleto.setOnAction(e -> abrirMapaEnNavegador(ruta.linkMapa()));

        opcionesContainer.getChildren().addAll(
                lblInfoRuta,
                titulo,
                btnEstandar,
                btnXL,
                btnLujo,
                lblMapa,
                webView,
                btnVerMapaCompleto
        );
        opcionesContainer.setVisible(true);
    }

    // ========================================
    // CREAR BOTÓN DE VIAJE (para mostrar inline)
    // ========================================
    private Button crearBotonViajeInline(String texto, String tiempo, String color) {
        Button btn = new Button(texto + " - ⏱️ " + tiempo);
        btn.setStyle(
                "-fx-font-size: 14px; " +
                        "-fx-padding: 15 30; " +
                        "-fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 8; " +
                        "-fx-font-weight: bold;"
        );
        btn.setPrefWidth(350);
        return btn;
    }

    // ========================================
    // CONFIRMAR VIAJE (en la misma pantalla)
    // ========================================
    // ========================================
// CONFIRMAR VIAJE (en la misma pantalla)
// ========================================
    private void confirmarViajeEnPantalla(Ruta ruta, TipoViaje tipo, Label lblMensaje, VBox opcionesContainer) {

        // **NOTA IMPORTANTE:**
        // La lógica de obtener y bloquear la disponibilidad debe estar dentro del sistema,
        // pero para fines de demostración, la manejamos aquí:
        Conductor conductor = sistema.obtenerConductorDisponible();

        if (conductor == null) {
            lblMensaje.setText("❌ Error: No hay conductores disponibles en este momento");
            lblMensaje.setTextFill(Color.RED);
            opcionesContainer.setVisible(false);
            // Volver a la pantalla de búsqueda, asegurando que los campos estén visibles
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    Platform.runLater(() -> mostrarPantallaBienvenida());
                } catch (InterruptedException ex) { /* ignorar */ }
            }).start();
            return;
        }

        // 🔑 PASO CRUCIAL: Marcar como NO DISPONIBLE INMEDIATAMENTE
        conductor.setDisponible(false);
        // Y guardar ese estado para persistencia
        gestorUsuarios.guardarUsuarios();

        // Crear viaje según el tipo
        Viaje viaje = switch (tipo) {
            case Estandar -> new ViajeEstandar(ruta, clienteActual, conductor);
            case XL -> new ViajeXL(ruta, clienteActual, conductor);
            case Lujo -> new ViajeLujo(ruta, clienteActual, conductor);
        };

        sistema.agregarViaje(viaje);

        // Mostrar confirmación
        lblMensaje.setText(
                "🎉 ¡Viaje Confirmado!\n" +
                        "Tipo: " + tipo + " | Conductor: " + conductor.nombre() + "\n" +
                        "Distancia: " + ruta.distanciaTexto() + " | Tiempo: " + ruta.tiempoTexto() + "\n" +
                        "¡Buen viaje! 🚗"
        );
        lblMensaje.setTextFill(Color.web("#4CAF50"));
        lblMensaje.setVisible(true);

        // Ocultar opciones
        opcionesContainer.setVisible(false);

        // Limpiar selecciones y simular fin de viaje (3 segundos),
        // luego regresar a la pantalla de bienvenida.
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                Platform.runLater(() -> {
                    // El conductor debería volver a estar disponible AL FINALIZAR el viaje,
                    // pero esa lógica es compleja (manejo de estados).
                    // Por ahora, solo cerramos la sesión del cliente.
                    origenSeleccionado = null;
                    destinoSeleccionado = null;
                    mostrarPantallaBienvenida();
                });
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
    // ========================================
    // GENERAR HTML CON IFRAME DEL MAPA - CORREGIDO
    // ========================================
    // ========================================
    // GENERAR HTML CON IFRAME DEL MAPA - SOLUCIÓN PARA MOSTRAR SOLO MAPA
    // ========================================
    private String generarHTMLMapaConIframe(String linkMapa) {
        // ATENCIÓN: Debes usar la clave de API de tu instancia de GooglePlacesService.
        String API_KEY = "AIzaSyB6uynr_3ELge4l5JrkDNGh3JYs-zO53DI";

        String origen = "";
        String destino = "";

        // 1. Intentar extraer origen y destino del link original /dir/
        if (linkMapa.contains("/dir/")) {
            try {
                String path = linkMapa.substring(linkMapa.indexOf("/dir/") + 5);
                String[] parts = path.split("/");
                if (parts.length >= 2) {
                    // Los paths del link de Google ya vienen a menudo con URL encoding
                    origen = parts[0];
                    destino = parts[1];
                }
            } catch (Exception e) {
                // Si la extracción falla, la URL final será la de fallback
            }
        }

        String embedUrl;

        // 2. Construir la URL OFICIAL de Google Maps Embed API para DIRECCIONES
        if (!origen.isEmpty() && !destino.isEmpty()) {
            try {
                // Utilizar java.net.URLEncoder para asegurar que las partes sean seguras
                String encodedOrigin = java.net.URLEncoder.encode(origen, java.nio.charset.StandardCharsets.UTF_8);
                String encodedDestination = java.net.URLEncoder.encode(destino, java.nio.charset.StandardCharsets.UTF_8);

                // 🔑 ESTE ES EL FORMATO CLAVE CORREGIDO: USANDO "embed/v1/directions"
                embedUrl = String.format(
                        "https://www.google.com/maps/embed/v1/directions?key=%s&origin=%s&destination=%s",
                        API_KEY,
                        encodedOrigin,
                        encodedDestination
                );

            } catch (Exception e) {
                embedUrl = linkMapa; // Fallback
            }
        } else {
            // Si no hay origen/destino, carga un mapa simple centrado (ejemplo: Rosario)
            // FORMATO para MAPA SIMPLE: "embed/v1/place" o "embed/v1/view"
            embedUrl = String.format("https://www.google.com/maps/embed/v1/view?key=%s&center=-32.94682,-60.63932&zoom=12", API_KEY);
        }

        return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <style>
                body {
                    margin: 0;
                    padding: 0;
                    overflow: hidden;
                    background-color: #f0f0f0;
                }
                iframe {
                    width: 100%%;
                    height: 100%%;
                    border: none;
                    border-radius: 8px;
                }
            </style>
        </head>
        <body>
            <iframe src="%s" allowfullscreen loading="lazy"></iframe>
        </body>
        </html>
        """.formatted(embedUrl);
    }
    // ========================================
    // ABRIR MAPA EN NAVEGADOR EXTERNO
    // ========================================
    private void abrirMapaEnNavegador(String linkMapa) {
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(linkMapa));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al abrir el navegador: " + e.getMessage());
        }
    }

    @Override
    public void stop() throws Exception {
        placesService.cerrar();
        distanceCalculator.closeContext();
    }

    public static void main(String[] args) {
        launch(args);
    }
}