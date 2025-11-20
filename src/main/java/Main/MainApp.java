package Main;

import javafx.application.Application;
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
    private Cliente clienteActual;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.gestorUsuarios = new GestorUsuarios();

        stage.setTitle("Uber - Sistema de Viajes");
        stage.setWidth(900);  // Tamaño inicial
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

        // Espaciado dinámico basado en altura de ventana
        root.spacingProperty().bind(stage.heightProperty().divide(15));

        Label lblTitulo = new Label("🚗 BIENVENIDO A UBER 🚗");
        lblTitulo.setStyle("-fx-font-weight: bold;");
        // Tamaño de fuente dinámico
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
    // PANTALLA 2: LOGIN / REGISTRO (SIN BOTÓN VOLVER)
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

        // Campo Nombre
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

        // Campo Email
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

        // Etiqueta de error/éxito
        Label lblMensaje = new Label("");
        lblMensaje.styleProperty().bind(Bindings.concat(
                "-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: ", stage.widthProperty().divide(70), "px;"
        ));

        // Botón Registrar
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

        // Botón Volver
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

        // Campo Email
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

        // Etiqueta de mensaje
        Label lblMensaje = new Label("");
        lblMensaje.styleProperty().bind(Bindings.concat(
                "-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: ", stage.widthProperty().divide(70), "px;"
        ));

        // Botón Iniciar Sesión
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

            if (resultado instanceof java.util.Optional<?> opt && opt.isPresent()) {
                clienteActual = (Cliente) opt.get();
                mostrarPantallaBienvenida();
            } else {
                lblMensaje.setText("❌ Usuario no encontrado");
                lblMensaje.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            }
        });

        // Botón Volver
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
    // PANTALLA 5: BIENVENIDA
    // ========================================
    private void mostrarPantallaBienvenida() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.spacingProperty().bind(stage.heightProperty().divide(25));

        Label lblBienvenida = new Label("¡Bienvenido/a " + clienteActual.nombre() + "! 🎉");
        lblBienvenida.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(28), "px; -fx-font-weight: bold;"
        ));

        Label lblEmail = new Label("Email: " + clienteActual.email());
        lblEmail.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(50), "px;"
        ));

        // Botones de Origen y Destino
        HBox botonesViaje = new HBox();
        botonesViaje.setAlignment(Pos.CENTER);
        botonesViaje.spacingProperty().bind(stage.widthProperty().divide(40));

        Button btnOrigen = new Button("Origen de viaje");
        btnOrigen.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(55), "px; ",
                "-fx-padding: ", stage.heightProperty().divide(70), " ", stage.widthProperty().divide(30), "; ",
                "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5;"
        ));
        btnOrigen.setOnAction(e -> {
            // Lógica para seleccionar origen (próximamente)
            System.out.println("Seleccionar origen");
        });

        Button btnDestino = new Button("Destino");
        btnDestino.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(55), "px; ",
                "-fx-padding: ", stage.heightProperty().divide(70), " ", stage.widthProperty().divide(30), "; ",
                "-fx-background-color: #2196F3; -fx-text-fill: white; -fx-background-radius: 5;"
        ));
        btnDestino.setOnAction(e -> {
            // Lógica para seleccionar destino (próximamente)
            System.out.println("Seleccionar destino");
        });

        botonesViaje.getChildren().addAll(btnOrigen, btnDestino);

        Button btnCerrarSesion = new Button("Cerrar Sesión");
        btnCerrarSesion.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(55), "px; ",
                "-fx-padding: ", stage.heightProperty().divide(70), " ", stage.widthProperty().divide(30), ";"
        ));
        btnCerrarSesion.setOnAction(e -> {
            clienteActual = null;
            mostrarPantallaTitulo();
        });

        root.getChildren().addAll(lblBienvenida, lblEmail, botonesViaje, btnCerrarSesion);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #a1c4fd 0%, #c2e9fb 100%);");

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}