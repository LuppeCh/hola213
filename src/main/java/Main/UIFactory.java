package Main;

import Viajes.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.beans.binding.Bindings;

/**
 * Fábrica que crea todas las pantallas de la aplicación.
 * Responsabilidad: SOLO crear componentes visuales.
 * Toda la lógica se delega al AppController.
 */
public class UIFactory {

    private final AppController controller;
    private final Stage stage;
    private final UIComponents components;

    public UIFactory(AppController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
        this.components = new UIComponents(stage);
    }

    // ========================================
    // PANTALLA 1: TÍTULO
    // ========================================

    public Scene crearPantallaTitulo() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.spacingProperty().bind(stage.heightProperty().divide(15));

        Label lblTitulo = new Label("🚗 BIENVENIDO A UBER 🚗");
        lblTitulo.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(25), "px; -fx-font-weight: bold;"
        ));

        Button btnViajar = components.crearBotonPrincipal("Viajar");
        btnViajar.setOnAction(e -> controller.navegarA(Pantalla.LOGIN_REGISTRO));

        root.getChildren().addAll(lblTitulo, btnViajar);
        components.aplicarGradiente(root, "#667eea", "#764ba2");

        return new Scene(root);
    }

    // ========================================
    // PANTALLA 2: LOGIN / REGISTRO
    // ========================================

    public Scene crearPantallaLoginRegistro() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.spacingProperty().bind(stage.heightProperty().divide(20));

        Label lblTitulo = components.crearTituloSeccion("¿Qué deseas hacer?");

        Button btnIniciarSesion = components.crearBotonSecundario("Iniciar Sesión");
        btnIniciarSesion.setOnAction(e -> controller.navegarA(Pantalla.LOGIN));

        Button btnRegistrarse = components.crearBotonSecundario("Registrarse por primera vez");
        btnRegistrarse.setOnAction(e -> controller.navegarA(Pantalla.SELECCION_TIPO_REGISTRO));

        root.getChildren().addAll(lblTitulo, btnIniciarSesion, btnRegistrarse);
        components.aplicarGradiente(root, "#f093fb", "#f5576c");

        return new Scene(root);
    }

    // ========================================
    // PANTALLA 3: SELECCIÓN TIPO REGISTRO
    // ========================================

    public Scene crearPantallaSeleccionTipoRegistro() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));

        Label lblTitulo = new Label("Elige tu rol");
        lblTitulo.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(30), "px; -fx-font-weight: bold; -fx-text-fill: white;"
        ));

        Button btnCliente = new Button("👤 Cliente (Necesito un viaje)");
        btnCliente.setPrefWidth(300);
        btnCliente.setStyle("-fx-font-size: 18px; -fx-padding: 10 20; -fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold;");
        btnCliente.setOnAction(e -> controller.navegarA(Pantalla.REGISTRO_CLIENTE));

        Button btnConductor = new Button("🚗 Conductor (Quiero trabajar)");
        btnConductor.setPrefWidth(300);
        btnConductor.setStyle("-fx-font-size: 18px; -fx-padding: 10 20; -fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold;");
        btnConductor.setOnAction(e -> controller.navegarA(Pantalla.REGISTRO_CONDUCTOR));

        Button btnVolver = components.crearBotonVolver();
        btnVolver.setOnAction(e -> controller.navegarA(Pantalla.LOGIN_REGISTRO));

        root.getChildren().addAll(lblTitulo, btnCliente, btnConductor, btnVolver);
        components.aplicarGradiente(root, "#a18cd1", "#fbc2eb");

        return new Scene(root);
    }

    // ========================================
    // PANTALLA 4: REGISTRO (UNIFICADA)
    // ========================================

    public Scene crearPantallaRegistro(boolean esCliente) {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.spacingProperty().bind(stage.heightProperty().divide(35));

        String tituloTexto = esCliente ? "Registro de Cliente" : "Registro de Conductor";
        Label lblTitulo = components.crearTituloSeccion(tituloTexto);

        Label lblNombre = components.crearLabelCampo("Nombre:");
        TextField txtNombre = components.crearCampoTexto("Ingrese su nombre");

        Label lblEmail = components.crearLabelCampo("Email:");
        TextField txtEmail = components.crearCampoTexto("ejemplo@gmail.com");

        Label lblMensaje = components.crearLabelMensaje();

        Button btnRegistrar = components.crearBotonAccion("Registrarse");
        btnRegistrar.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            String email = txtEmail.getText().trim();

            if (nombre.isEmpty() || email.isEmpty()) {
                components.mostrarMensajeError(lblMensaje, "⚠️ Todos los campos son obligatorios");
                return;
            }

            AppController.ResultadoOperacion resultado;
            if (esCliente) {
                resultado = controller.registrarCliente(nombre, email);
            } else {
                resultado = controller.registrarConductor(nombre, email);
            }

            if (resultado.exitoso()) {
                components.mostrarMensajeExito(lblMensaje, resultado.mensaje());
                txtNombre.clear();
                txtEmail.clear();
            } else {
                components.mostrarMensajeError(lblMensaje, resultado.mensaje());
            }
        });

        Button btnVolver = components.crearBotonVolver();
        btnVolver.setOnAction(e -> controller.navegarA(Pantalla.SELECCION_TIPO_REGISTRO));

        root.getChildren().addAll(lblTitulo, lblNombre, txtNombre, lblEmail, txtEmail, btnRegistrar, lblMensaje, btnVolver);
        components.aplicarGradiente(root, "#a8edea", "#fed6e3");

        return new Scene(root);
    }

    // ========================================
    // PANTALLA 5: LOGIN
    // ========================================

    public Scene crearPantallaLogin() {
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.spacingProperty().bind(stage.heightProperty().divide(35));

        Label lblTitulo = components.crearTituloSeccion("Iniciar Sesión");

        Label lblEmail = components.crearLabelCampo("Email:");
        TextField txtEmail = components.crearCampoTexto("ejemplo@gmail.com");

        Label lblMensaje = components.crearLabelMensaje();

        Button btnLogin = components.crearBotonAccion("Ingresar");
        btnLogin.setOnAction(e -> {
            String email = txtEmail.getText().trim();
            AppController.ResultadoOperacion resultado = controller.iniciarSesion(email);

            if (!resultado.exitoso()) {
                components.mostrarMensajeError(lblMensaje, resultado.mensaje());
            }
        });

        Button btnVolver = components.crearBotonVolver();
        btnVolver.setOnAction(e -> controller.navegarA(Pantalla.LOGIN_REGISTRO));

        root.getChildren().addAll(lblTitulo, lblEmail, txtEmail, btnLogin, lblMensaje, btnVolver);
        components.aplicarGradiente(root, "#ffecd2", "#fcb69f");

        return new Scene(root);
    }

    // ========================================
    // PANTALLA 6: BIENVENIDA CLIENTE
    // ========================================

    public Scene crearPantallaBienvenidaCliente() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #F8F9FA; -fx-background-color: #F8F9FA;");

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.spacingProperty().bind(stage.heightProperty().divide(25));

        Cliente cliente = controller.getClienteActual();

        Label lblBienvenida = new Label("¡Bienvenido/a " + cliente.nombre() + "!");
        lblBienvenida.setTextFill(Color.web("#343A40"));
        lblBienvenida.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(28), "px; -fx-font-weight: bold;"
        ));

        Label lblEmail = new Label("Email: " + cliente.email());
        lblEmail.setTextFill(Color.web("#6C757D"));
        lblEmail.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(50), "px;"
        ));

        Label lblMensaje = components.crearLabelMensaje();

        VBox inputContainer = crearCamposOrigenDestino(lblMensaje);
        VBox opcionesViajeContainer = new VBox(15);
        opcionesViajeContainer.setAlignment(Pos.CENTER);
        opcionesViajeContainer.setVisible(false);

        Button btnIniciarViaje = new Button("Iniciar Viaje");
        btnIniciarViaje.setStyle(
                "-fx-font-size: 20px; -fx-padding: 15 40; -fx-background-color: #007BFF; " +
                        "-fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold;"
        );
        btnIniciarViaje.setPrefWidth(300);
        btnIniciarViaje.setOnAction(e -> procesarInicioViaje(lblMensaje, opcionesViajeContainer, inputContainer, btnIniciarViaje));

        Button btnCerrarSesion = components.crearBotonAccion("Cerrar Sesión");
        btnCerrarSesion.setOnAction(e -> controller.cerrarSesion());

        VBox bottomButtons = new VBox(10, btnIniciarViaje, btnCerrarSesion);
        bottomButtons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(lblBienvenida, lblEmail, lblMensaje, inputContainer, bottomButtons, opcionesViajeContainer);
        root.setStyle("-fx-background-color: #FFFFFF;");

        scrollPane.setContent(root);
        return new Scene(scrollPane);
    }

    private VBox crearCamposOrigenDestino(Label lblMensaje) {
        VBox container = new VBox(25);
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(400);

        VBox origenContainer = crearCampoAutocompletado("Origen:", "Escribe tu origen", true, lblMensaje);
        VBox destinoContainer = crearCampoAutocompletado("Destino:", "Escribe tu destino", false, lblMensaje);

        container.getChildren().addAll(origenContainer, destinoContainer);
        return container;
    }

    private VBox crearCampoAutocompletado(String etiqueta, String prompt, boolean esOrigen, Label lblMensaje) {
        Label lblTitulo = new Label(etiqueta);
        lblTitulo.setTextFill(Color.web("#343A40"));
        lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        TextField txtCampo = new TextField();
        txtCampo.setPromptText(prompt);
        txtCampo.setPrefHeight(40);
        txtCampo.setStyle("-fx-font-size: 14px; -fx-border-color: #CED4DA; -fx-border-radius: 5; -fx-background-radius: 5;");

        ListView<String> listSugerencias = new ListView<>();
        listSugerencias.setPrefHeight(0);
        listSugerencias.setVisible(false);
        listSugerencias.maxWidthProperty().bind(txtCampo.widthProperty());

        txtCampo.textProperty().addListener((obs, old, nuevo) -> {
            if (nuevo.length() >= 3) {
                controller.autocompletarLugar(nuevo).thenAccept(sugerencias -> {
                    Platform.runLater(() -> {
                        listSugerencias.getItems().setAll(sugerencias);
                        listSugerencias.setPrefHeight(Math.min(sugerencias.size() * 30, 150));
                        listSugerencias.setVisible(!sugerencias.isEmpty());
                    });
                });
            } else {
                listSugerencias.setVisible(false);
            }

            if (esOrigen) {
                controller.setOrigen(nuevo);
            } else {
                controller.setDestino(nuevo);
            }
        });

        listSugerencias.setOnMouseClicked(event -> {
            String seleccion = listSugerencias.getSelectionModel().getSelectedItem();
            if (seleccion != null) {
                txtCampo.setText(seleccion);
                if (esOrigen) {
                    controller.setOrigen(seleccion);
                } else {
                    controller.setDestino(seleccion);
                }
                listSugerencias.setVisible(false);
                lblMensaje.setText("");
            }
        });

        return new VBox(5, lblTitulo, txtCampo, listSugerencias);
    }

    private void procesarInicioViaje(Label lblMensaje, VBox opcionesContainer, VBox inputContainer, Button btnIniciarViaje) {
        if (controller.getOrigen() == null || controller.getDestino() == null ||
                controller.getOrigen().isEmpty() || controller.getDestino().isEmpty()) {
            components.mostrarMensajeError(lblMensaje, "⚠️ Por favor, selecciona origen y destino");
            return;
        }

        components.mostrarMensajeCarga(lblMensaje, "⏳ Calculando ruta, por favor espera...");
        opcionesContainer.setVisible(false);

        controller.calcularRuta().thenAccept(ruta -> {
            Platform.runLater(() -> {
                inputContainer.setVisible(false);
                btnIniciarViaje.setVisible(false);
                mostrarOpcionesViaje(ruta, opcionesContainer, lblMensaje);
            });
        }).exceptionally(ex -> {
            Platform.runLater(() -> {
                components.mostrarMensajeError(lblMensaje, "❌ Error al calcular ruta: " + ex.getMessage());
            });
            return null;
        });
    }

    private void mostrarOpcionesViaje(Ruta ruta, VBox opcionesContainer, Label lblMensaje) {
        opcionesContainer.getChildren().clear();

        Label lblInfoRuta = new Label("📍 Distancia: " + ruta.distanciaTexto() + " | ⏱️ Tiempo: " + ruta.tiempoTexto());
        lblInfoRuta.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #4CAF50;");

        Label titulo = new Label("Elige tu tipo de viaje:");
        titulo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 20 0 15 0;");

        Button btnEstandar = crearBotonViaje("🚗 Estándar", ruta.tiempoTexto(), "#4CAF50");
        Button btnXL = crearBotonViaje("🚙 XL", ruta.tiempoTexto(), "#FF9800");
        Button btnLujo = crearBotonViaje("💎 Lujo", ruta.tiempoTexto(), "#9C27B0");

        btnEstandar.setOnAction(e -> confirmarViaje(TipoViaje.Estandar, lblMensaje, opcionesContainer));
        btnXL.setOnAction(e -> confirmarViaje(TipoViaje.XL, lblMensaje, opcionesContainer));
        btnLujo.setOnAction(e -> confirmarViaje(TipoViaje.Lujo, lblMensaje, opcionesContainer));

        VBox mapaContainer = components.crearComponenteMapa(ruta);

        opcionesContainer.getChildren().addAll(lblInfoRuta, titulo, btnEstandar, btnXL, btnLujo, mapaContainer);
        opcionesContainer.setVisible(true);
        lblMensaje.setVisible(false);
    }

    private Button crearBotonViaje(String texto, String tiempo, String color) {
        Button btn = new Button(texto + " - ⏱️ " + tiempo);
        btn.setStyle(
                "-fx-font-size: 14px; -fx-padding: 15 30; -fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold;"
        );
        btn.setPrefWidth(350);
        return btn;
    }

    private void confirmarViaje(TipoViaje tipo, Label lblMensaje, VBox opcionesContainer) {
        AppController.ResultadoOperacion resultado = controller.confirmarViaje(tipo);

        if (resultado.exitoso()) {
            components.mostrarMensajeExito(lblMensaje, resultado.mensaje());
        } else {
            components.mostrarMensajeError(lblMensaje, resultado.mensaje());
        }

        opcionesContainer.setVisible(false);

        new Thread(() -> {
            try {
                Thread.sleep(3000);
                Platform.runLater(() -> controller.navegarA(Pantalla.BIENVENIDA_CLIENTE));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    // ========================================
    // PANTALLA 7: DASHBOARD CONDUCTOR
    // ========================================

    public Scene crearPantallaDashboardConductor() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));

        Conductor conductor = controller.getConductorActual();

        Label lblBienvenida = new Label("Hola Conductor " + conductor.nombre());
        lblBienvenida.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #333;");

        Label lblEstado = new Label("🟢 ¡Estás disponible para viajes!");
        lblEstado.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #008000;");

        Label lblInfo = new Label("Esperando solicitudes de viaje...");
        lblInfo.setStyle("-fx-font-size: 16px; -fx-text-fill: #555;");

        Button btnCerrarSesion = new Button("Cerrar Sesión y Desconectarse");
        btnCerrarSesion.setStyle(
                "-fx-font-size: 18px; -fx-padding: 10 30; -fx-background-color: #DC3545; " +
                        "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8;"
        );
        btnCerrarSesion.setOnAction(e -> controller.cerrarSesion());

        root.getChildren().addAll(lblBienvenida, lblEstado, lblInfo, btnCerrarSesion);
        components.aplicarGradiente(root, "#d4fc79", "#96e6a1");

        return new Scene(root);
    }
}
