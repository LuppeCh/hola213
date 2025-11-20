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

public class MainApp extends Application {

    private Stage stage;
    private GestorUsuarios gestorUsuarios;
    private Sistema sistema;
    private Cliente clienteActual;
    private GooglePlacesService placesService;
    private DistanceCalculator distanceCalculator;

    private String origenSeleccionado = null;
    private String destinoSeleccionado = null;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.gestorUsuarios = new GestorUsuarios();
        this.sistema = new Sistema();
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
        btnRegistrarse.setOnAction(e -> mostrarPantallaRegistro());

        root.getChildren().addAll(lblTitulo, btnIniciarSesion, btnRegistrarse);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #f093fb 0%, #f5576c 100%);");

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    // ========================================
    // PANTALLA 3: REGISTRO
    // ========================================
    private void mostrarPantallaRegistro() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.spacingProperty().bind(stage.heightProperty().divide(35));

        Label lblTitulo = new Label("Registro de Usuario");
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
        lblMensaje.styleProperty().bind(Bindings.concat(
                "-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: ", stage.widthProperty().divide(70), "px;"
        ));

        Button btnRegistrar = new Button("Registrarse");
        btnRegistrar.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(55), "px; ",
                "-fx-padding: ", stage.heightProperty().divide(70), " ", stage.widthProperty().divide(25), ";"
        ));
        btnRegistrar.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            String email = txtEmail.getText().trim();

            if (nombre.isEmpty() || email.isEmpty()) {
                lblMensaje.setText("⚠️ Todos los campos son obligatorios");
                lblMensaje.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                return;
            }

            if (gestorUsuarios.registrarCliente(nombre, email)) {
                lblMensaje.setText("✅ Registro exitoso! Ahora puedes iniciar sesión");
                lblMensaje.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                txtNombre.clear();
                txtEmail.clear();
            } else {
                lblMensaje.setText("❌ Email inválido o ya registrado (debe ser @gmail.com)");
                lblMensaje.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            }
        });

        Button btnVolver = new Button("← Volver");
        btnVolver.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(70), "px;"
        ));
        btnVolver.setOnAction(e -> mostrarPantallaLoginRegistro());

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
    // PANTALLA 4: LOGIN
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
        lblMensaje.styleProperty().bind(Bindings.concat(
                "-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: ", stage.widthProperty().divide(70), "px;"
        ));

        Button btnLogin = new Button("Ingresar");
        btnLogin.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(55), "px; ",
                "-fx-padding: ", stage.heightProperty().divide(70), " ", stage.widthProperty().divide(25), ";"
        ));
        btnLogin.setOnAction(e -> {
            String email = txtEmail.getText().trim();

            if (email.isEmpty()) {
                lblMensaje.setText("⚠️ Debe ingresar un email");
                lblMensaje.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                return;
            }

            var resultado = gestorUsuarios.iniciarSesion(email);

            if (resultado.isPresent()) {
                clienteActual = resultado.get();
                mostrarPantallaBienvenida();
            } else {
                lblMensaje.setText("❌ Usuario no encontrado");
                lblMensaje.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            }
        });

        Button btnVolver = new Button("← Volver");
        btnVolver.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(70), "px;"
        ));
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
    // PANTALLA 5: BIENVENIDA CON AUTOCOMPLETADO
    // ========================================
    private void mostrarPantallaBienvenida() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.spacingProperty().bind(stage.heightProperty().divide(40));

        Label lblBienvenida = new Label("¡Bienvenido/a " + clienteActual.nombre() + "! 🎉");
        lblBienvenida.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(28), "px; -fx-font-weight: bold;"
        ));

        Label lblEmail = new Label("Email: " + clienteActual.email());
        lblEmail.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(50), "px;"
        ));

        // Label para mensajes
        Label lblMensaje = new Label("");
        lblMensaje.setWrapText(true);
        lblMensaje.setMaxWidth(600);
        lblMensaje.setAlignment(Pos.CENTER);
        lblMensaje.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(60), "px; -fx-font-weight: bold;"
        ));

        // ===== ORIGEN =====
        HBox origenBox = new HBox(10);
        origenBox.setAlignment(Pos.CENTER);

        Button btnOrigen = new Button("Origen");
        btnOrigen.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(60), "px; ",
                "-fx-padding: ", stage.heightProperty().divide(80), " ", stage.widthProperty().divide(40), "; ",
                "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5;"
        ));

        TextField txtOrigen = new TextField();
        txtOrigen.setPromptText("Escribir origen...");
        txtOrigen.setPrefWidth(300);
        txtOrigen.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(70), "px;"
        ));

        ListView<String> listOrigen = new ListView<>();
        listOrigen.setPrefHeight(0);
        listOrigen.setVisible(false);
        listOrigen.maxWidthProperty().bind(txtOrigen.widthProperty());

        // Autocompletado Origen
        txtOrigen.textProperty().addListener((obs, old, nuevo) -> {
            if (nuevo.length() >= 3) {
                new Thread(() -> {
                    try {
                        var sugerencias = placesService.autocompletar(nuevo);
                        Platform.runLater(() -> {
                            listOrigen.getItems().setAll(sugerencias);
                            listOrigen.setPrefHeight(Math.min(sugerencias.size() * 24, 120));
                            listOrigen.setVisible(!sugerencias.isEmpty());
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                listOrigen.setVisible(false);
            }
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

        VBox origenContainer = new VBox(5, origenBox, listOrigen);
        origenContainer.setAlignment(Pos.CENTER);
        origenBox.getChildren().addAll(btnOrigen, txtOrigen);

        // ===== DESTINO =====
        HBox destinoBox = new HBox(10);
        destinoBox.setAlignment(Pos.CENTER);

        Button btnDestino = new Button("Destino");
        btnDestino.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(60), "px; ",
                "-fx-padding: ", stage.heightProperty().divide(80), " ", stage.widthProperty().divide(40), "; ",
                "-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 5;"
        ));

        TextField txtDestino = new TextField();
        txtDestino.setPromptText("Escribir destino...");
        txtDestino.setPrefWidth(300);
        txtDestino.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(70), "px;"
        ));

        ListView<String> listDestino = new ListView<>();
        listDestino.setPrefHeight(0);
        listDestino.setVisible(false);
        listDestino.maxWidthProperty().bind(txtDestino.widthProperty());

        // Autocompletado Destino
        txtDestino.textProperty().addListener((obs, old, nuevo) -> {
            if (nuevo.length() >= 3) {
                new Thread(() -> {
                    try {
                        var sugerencias = placesService.autocompletar(nuevo);
                        Platform.runLater(() -> {
                            listDestino.getItems().setAll(sugerencias);
                            listDestino.setPrefHeight(Math.min(sugerencias.size() * 24, 120));
                            listDestino.setVisible(!sugerencias.isEmpty());
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                listDestino.setVisible(false);
            }
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

        VBox destinoContainer = new VBox(5, destinoBox, listDestino);
        destinoContainer.setAlignment(Pos.CENTER);
        destinoBox.getChildren().addAll(btnDestino, txtDestino);

        // Contenedor para opciones de viaje (inicialmente vacío)
        VBox opcionesViajeContainer = new VBox(15);
        opcionesViajeContainer.setAlignment(Pos.CENTER);
        opcionesViajeContainer.setVisible(false);

        // ===== BOTÓN INICIAR VIAJE =====
        Button btnIniciarViaje = new Button("Iniciar Viaje");
        btnIniciarViaje.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(50), "px; ",
                "-fx-padding: ", stage.heightProperty().divide(60), " ", stage.widthProperty().divide(25), "; ",
                "-fx-background-color: #FF5722; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold;"
        ));

        btnIniciarViaje.setOnAction(e -> {
            if (origenSeleccionado == null || destinoSeleccionado == null) {
                lblMensaje.setText("⚠️ Por favor, selecciona origen y destino antes de iniciar el viaje");
                lblMensaje.setStyle("-fx-text-fill: #FF5722; -fx-font-weight: bold;");
                opcionesViajeContainer.setVisible(false);
                return;
            }

            // Mostrar mensaje de carga
            lblMensaje.setText("⏳ Calculando ruta, por favor espera...");
            lblMensaje.setStyle("-fx-text-fill: #2196F3; -fx-font-weight: bold;");
            opcionesViajeContainer.setVisible(false);

            // Calcular ruta
            calcularYMostrarOpciones(lblMensaje, opcionesViajeContainer);
        });

        Button btnCerrarSesion = new Button("Cerrar Sesión");
        btnCerrarSesion.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(60), "px; ",
                "-fx-padding: ", stage.heightProperty().divide(80), " ", stage.widthProperty().divide(40), ";"
        ));
        btnCerrarSesion.setOnAction(e -> {
            clienteActual = null;
            origenSeleccionado = null;
            destinoSeleccionado = null;
            mostrarPantallaTitulo();
        });

        root.getChildren().addAll(
                lblBienvenida,
                lblEmail,
                lblMensaje,
                origenContainer,
                destinoContainer,
                btnIniciarViaje,
                opcionesViajeContainer,
                btnCerrarSesion
        );
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #a1c4fd 0%, #c2e9fb 100%);");

        scrollPane.setContent(root);
        Scene scene = new Scene(scrollPane);
        stage.setScene(scene);
    }

    // ========================================
    // CALCULAR RUTA Y MOSTRAR OPCIONES DE VIAJE
    // ========================================
    private void calcularYMostrarOpciones(Label lblMensaje, VBox opcionesContainer) {
        new Thread(() -> {
            try {
                Ruta ruta = distanceCalculator.calculateRoute(origenSeleccionado, destinoSeleccionado);

                Platform.runLater(() -> {
                    lblMensaje.setText("✅ Ruta calculada exitosamente\n📍 Distancia: " + ruta.distanciaTexto() + " | ⏱️ Tiempo: " + ruta.tiempoTexto());
                    lblMensaje.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");

                    mostrarOpcionesViajeEnPantalla(ruta, opcionesContainer, lblMensaje);
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    lblMensaje.setText("❌ Error: No se pudo calcular la ruta\n" + e.getMessage());
                    lblMensaje.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
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

        Label titulo = new Label("Elige tu tipo de viaje:");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

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

        opcionesContainer.getChildren().addAll(titulo, btnEstandar, btnXL, btnLujo);
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
    private void confirmarViajeEnPantalla(Ruta ruta, TipoViaje tipo, Label lblMensaje, VBox opcionesContainer) {
        Conductor conductor = sistema.obtenerConductorDisponible();

        if (conductor == null) {
            lblMensaje.setText("❌ Error: No hay conductores disponibles en este momento");
            lblMensaje.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            opcionesContainer.setVisible(false);
            return;
        }

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
        lblMensaje.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");

        // Ocultar opciones
        opcionesContainer.setVisible(false);

        // Limpiar selecciones después de 3 segundos
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                Platform.runLater(() -> {
                    origenSeleccionado = null;
                    destinoSeleccionado = null;
                    mostrarPantallaBienvenida();
                });
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
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