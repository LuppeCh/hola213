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
// AGREGA ESTOS MÉTODOS A UIFactory.java
// REEMPLAZA los métodos crearPantallaRegistro
// ========================================

// ========================================
// PANTALLA: REGISTRO CLIENTE COMPLETO
// ========================================

    public Scene crearPantallaRegistroCliente() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        VBox root = new VBox(15);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));

        Label lblTitulo = new Label("📋 Registro de Cliente");
        lblTitulo.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        // CAMPOS DE FORMULARIO
        Label lblNombre = new Label("Nombre:");
        lblNombre.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField txtNombre = components.crearCampoTexto("Ej: Juan");

        Label lblApellido = new Label("Apellido:");
        lblApellido.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField txtApellido = components.crearCampoTexto("Ej: Pérez");

        Label lblDni = new Label("DNI:");
        lblDni.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField txtDni = components.crearCampoTexto("Ej: 12345678");

        Label lblEdad = new Label("Edad:");
        lblEdad.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField txtEdad = components.crearCampoTexto("Ej: 25");

        Label lblContrasena = new Label("Contraseña:");
        lblContrasena.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        PasswordField txtContrasena = new PasswordField();
        txtContrasena.setPromptText("Mínimo 6 caracteres");
        txtContrasena.setMaxWidth(300);
        txtContrasena.setPrefHeight(35);

        Label lblEmail = new Label("Email:");
        lblEmail.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField txtEmail = components.crearCampoTexto("ejemplo@gmail.com");

        Label lblMensaje = components.crearLabelMensaje();

        Button btnRegistrar = new Button("✅ Registrarse");
        btnRegistrar.setStyle(
                "-fx-font-size: 16px; -fx-padding: 12 40; -fx-background-color: #27AE60; " +
                        "-fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold;"
        );
        btnRegistrar.setOnAction(e -> {
            try {
                String nombre = txtNombre.getText().trim();
                String apellido = txtApellido.getText().trim();
                String dni = txtDni.getText().trim();
                String edadStr = txtEdad.getText().trim();
                String contrasena = txtContrasena.getText();
                String email = txtEmail.getText().trim();

                // VALIDACIONES
                if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() ||
                        edadStr.isEmpty() || contrasena.isEmpty() || email.isEmpty()) {
                    components.mostrarMensajeError(lblMensaje, "⚠️ Todos los campos son obligatorios");
                    return;
                }

                if (contrasena.length() < 6) {
                    components.mostrarMensajeError(lblMensaje, "⚠️ La contraseña debe tener al menos 6 caracteres");
                    return;
                }

                int edad = Integer.parseInt(edadStr);

                if (edad < 18) {
                    components.mostrarMensajeError(lblMensaje, "⚠️ Debes ser mayor de 18 años");
                    return;
                }

                // REGISTRAR
                AppController.ResultadoOperacion resultado = controller.registrarCliente(
                        nombre, apellido, dni, edad, contrasena, email
                );

                if (resultado.exitoso()) {
                    components.mostrarMensajeExito(lblMensaje, resultado.mensaje());
                    txtNombre.clear();
                    txtApellido.clear();
                    txtDni.clear();
                    txtEdad.clear();
                    txtContrasena.clear();
                    txtEmail.clear();
                } else {
                    components.mostrarMensajeError(lblMensaje, resultado.mensaje());
                }

            } catch (NumberFormatException ex) {
                components.mostrarMensajeError(lblMensaje, "⚠️ La edad debe ser un número válido");
            }
        });

        Button btnVolver = components.crearBotonVolver();
        btnVolver.setOnAction(e -> controller.navegarA(Pantalla.SELECCION_TIPO_REGISTRO));

        root.getChildren().addAll(
                lblTitulo,
                lblNombre, txtNombre,
                lblApellido, txtApellido,
                lblDni, txtDni,
                lblEdad, txtEdad,
                lblContrasena, txtContrasena,
                lblEmail, txtEmail,
                btnRegistrar,
                lblMensaje,
                btnVolver
        );

        root.setStyle("-fx-background-color: linear-gradient(to bottom, #a8edea 0%, #fed6e3 100%);");
        scrollPane.setContent(root);
        return new Scene(scrollPane);
    }

// ========================================
// PANTALLA: REGISTRO CONDUCTOR COMPLETO
// ========================================

    public Scene crearPantallaRegistroConductor() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        VBox root = new VBox(15);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));

        Label lblTitulo = new Label("🚗 Registro de Conductor");
        lblTitulo.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #2C3E50;");

        // DATOS PERSONALES
        Label lblSeccionPersonal = new Label("📋 Datos Personales");
        lblSeccionPersonal.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #34495E; -fx-padding: 10 0 5 0;");

        TextField txtNombre = crearCampoConLabel("Nombre:", "Ej: Juan");
        TextField txtApellido = crearCampoConLabel("Apellido:", "Ej: Pérez");
        TextField txtDni = crearCampoConLabel("DNI:", "Ej: 12345678");
        TextField txtEdad = crearCampoConLabel("Edad:", "Ej: 30");

        PasswordField txtContrasena = new PasswordField();
        txtContrasena.setPromptText("Contraseña (mínimo 6 caracteres)");
        txtContrasena.setMaxWidth(300);
        txtContrasena.setPrefHeight(35);

        TextField txtEmail = crearCampoConLabel("Email:", "ejemplo@gmail.com");

        // DATOS DEL VEHÍCULO
        Label lblSeccionVehiculo = new Label("🚙 Datos del Vehículo");
        lblSeccionVehiculo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #34495E; -fx-padding: 15 0 5 0;");

        TextField txtModelo = crearCampoConLabel("Modelo del auto:", "Ej: Toyota Corolla 2020");
        TextField txtPatente = crearCampoConLabel("Patente:", "Ej: ABC123");

        CheckBox chkAire = new CheckBox("¿Tiene aire acondicionado?");
        chkAire.setStyle("-fx-font-size: 14px;");

        Label lblCapacidad = new Label("Capacidad de pasajeros:");
        lblCapacidad.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        ComboBox<Integer> cmbCapacidad = new ComboBox<>();
        cmbCapacidad.getItems().addAll(2, 3, 4, 5, 6, 7);
        cmbCapacidad.setValue(4);
        cmbCapacidad.setMaxWidth(300);

        // UBICACIÓN INICIAL
        Label lblSeccionUbicacion = new Label("📍 Ubicación Inicial");
        lblSeccionUbicacion.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #34495E; -fx-padding: 15 0 5 0;");

        TextField txtLatitud = crearCampoConLabel("Latitud:", "-32.9468 (Rosario)");
        TextField txtLongitud = crearCampoConLabel("Longitud:", "-60.6393 (Rosario)");

        Label lblMensaje = components.crearLabelMensaje();

        Button btnRegistrar = new Button("✅ Registrarse como Conductor");
        btnRegistrar.setStyle(
                "-fx-font-size: 16px; -fx-padding: 12 40; -fx-background-color: #3498DB; " +
                        "-fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold;"
        );
        btnRegistrar.setOnAction(e -> {
            try {
                String nombre = txtNombre.getText().trim();
                String apellido = txtApellido.getText().trim();
                String dni = txtDni.getText().trim();
                String edadStr = txtEdad.getText().trim();
                String contrasena = txtContrasena.getText();
                String email = txtEmail.getText().trim();
                String modelo = txtModelo.getText().trim();
                String patente = txtPatente.getText().trim();
                boolean tieneAire = chkAire.isSelected();
                int capacidad = cmbCapacidad.getValue();
                String latStr = txtLatitud.getText().trim();
                String lonStr = txtLongitud.getText().trim();

                // VALIDACIONES
                if (nombre.isEmpty() || apellido.isEmpty() || dni.isEmpty() ||
                        edadStr.isEmpty() || contrasena.isEmpty() || email.isEmpty() ||
                        modelo.isEmpty() || patente.isEmpty() || latStr.isEmpty() || lonStr.isEmpty()) {
                    components.mostrarMensajeError(lblMensaje, "⚠️ Todos los campos son obligatorios");
                    return;
                }

                if (contrasena.length() < 6) {
                    components.mostrarMensajeError(lblMensaje, "⚠️ La contraseña debe tener al menos 6 caracteres");
                    return;
                }

                int edad = Integer.parseInt(edadStr);
                if (edad < 21) {
                    components.mostrarMensajeError(lblMensaje, "⚠️ Los conductores deben tener al menos 21 años");
                    return;
                }

                double latitud = Double.parseDouble(latStr);
                double longitud = Double.parseDouble(lonStr);

                // REGISTRAR
                AppController.ResultadoOperacion resultado = controller.registrarConductor(
                        nombre, apellido, dni, edad, contrasena, email,
                        modelo, patente, tieneAire, capacidad,
                        latitud, longitud
                );

                if (resultado.exitoso()) {
                    components.mostrarMensajeExito(lblMensaje, resultado.mensaje());
                    // Limpiar campos
                    txtNombre.clear();
                    txtApellido.clear();
                    txtDni.clear();
                    txtEdad.clear();
                    txtContrasena.clear();
                    txtEmail.clear();
                    txtModelo.clear();
                    txtPatente.clear();
                    chkAire.setSelected(false);
                    txtLatitud.clear();
                    txtLongitud.clear();
                } else {
                    components.mostrarMensajeError(lblMensaje, resultado.mensaje());
                }

            } catch (NumberFormatException ex) {
                components.mostrarMensajeError(lblMensaje, "⚠️ Verifica que edad, latitud y longitud sean números válidos");
            }
        });

        Button btnVolver = components.crearBotonVolver();
        btnVolver.setOnAction(e -> controller.navegarA(Pantalla.SELECCION_TIPO_REGISTRO));

        root.getChildren().addAll(
                lblTitulo,
                lblSeccionPersonal,
                crearLabelCampo("Nombre:"), txtNombre,
                crearLabelCampo("Apellido:"), txtApellido,
                crearLabelCampo("DNI:"), txtDni,
                crearLabelCampo("Edad:"), txtEdad,
                crearLabelCampo("Contraseña:"), txtContrasena,
                crearLabelCampo("Email:"), txtEmail,
                lblSeccionVehiculo,
                crearLabelCampo("Modelo del auto:"), txtModelo,
                crearLabelCampo("Patente:"), txtPatente,
                chkAire,
                lblCapacidad, cmbCapacidad,
                lblSeccionUbicacion,
                crearLabelCampo("Latitud:"), txtLatitud,
                crearLabelCampo("Longitud:"), txtLongitud,
                btnRegistrar,
                lblMensaje,
                btnVolver
        );

        root.setStyle("-fx-background-color: linear-gradient(to bottom, #a8edea 0%, #fed6e3 100%);");
        scrollPane.setContent(root);
        return new Scene(scrollPane);
    }

// ========================================
// MÉTODOS AUXILIARES
// ========================================

    private TextField crearCampoConLabel(String labelText, String prompt) {
        TextField txt = new TextField();
        txt.setPromptText(prompt);
        txt.setMaxWidth(300);
        txt.setPrefHeight(35);
        txt.setStyle("-fx-font-size: 13px;");
        return txt;
    }

    private Label crearLabelCampo(String texto) {
        Label lbl = new Label(texto);
        lbl.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        return lbl;
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
                resultado = controller.registrarCliente(nombre, apellido, dni, edad, contrasena, gmail);
            } else {
                resultado = controller.registrarConductor(nombre, apellido, dni, edad, contrasena, gmail);
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

        Label lblBienvenida = new Label("¡Bienvenido/a " + cliente.getNombre() + "!");
        lblBienvenida.setTextFill(Color.web("#343A40"));
        lblBienvenida.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ", stage.widthProperty().divide(28), "px; -fx-font-weight: bold;"
        ));

        Label lblEmail = new Label("Email: " + cliente.getGmail());
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

        // ✅ CALCULAR PRECIOS PARA CADA TIPO
        SistemaFachada.ResultadoPrecio precioEstandar = controller.calcularPrecio(ruta, TipoViaje.Estandar);
        SistemaFachada.ResultadoPrecio precioXL = controller.calcularPrecio(ruta, TipoViaje.XL);
        SistemaFachada.ResultadoPrecio precioLujo = controller.calcularPrecio(ruta, TipoViaje.Lujo);

        // ✅ CREAR BOTONES CON PRECIO
        Button btnEstandar = crearBotonViajeConPrecio("🚗 Estándar", precioEstandar, "#4CAF50");
        Button btnXL = crearBotonViajeConPrecio("🚙 XL", precioXL, "#FF9800");
        Button btnLujo = crearBotonViajeConPrecio("💎 Lujo", precioLujo, "#9C27B0");

        btnEstandar.setOnAction(e -> confirmarViaje(TipoViaje.Estandar, lblMensaje, opcionesContainer));
        btnXL.setOnAction(e -> confirmarViaje(TipoViaje.XL, lblMensaje, opcionesContainer));
        btnLujo.setOnAction(e -> confirmarViaje(TipoViaje.Lujo, lblMensaje, opcionesContainer));

        // ✅ MOSTRAR DESCUENTOS SI APLICAN
        VBox descuentosBox = new VBox(5);
        descuentosBox.setAlignment(Pos.CENTER);

        if (precioEstandar.descuentoPorcentaje() > 0 && !precioEstandar.resumenDescuentos().isEmpty()) {
            Label lblDescuentos = new Label(precioEstandar.resumenDescuentos());
            lblDescuentos.setStyle("-fx-font-size: 14px; -fx-text-fill: #FF5722; -fx-font-weight: bold; -fx-background-color: #FFF3E0; -fx-padding: 10; -fx-border-color: #FF9800; -fx-border-radius: 5; -fx-background-radius: 5;");
            lblDescuentos.setMaxWidth(400);
            lblDescuentos.setWrapText(true);
            descuentosBox.getChildren().add(lblDescuentos);
        }

        VBox mapaContainer = components.crearComponenteMapa(ruta);

        opcionesContainer.getChildren().addAll(lblInfoRuta, descuentosBox, titulo, btnEstandar, btnXL, btnLujo, mapaContainer);
        opcionesContainer.setVisible(true);
        lblMensaje.setVisible(false);
    }

    // ✅ NUEVO MÉTODO: Botón con precio
    private Button crearBotonViajeConPrecio(String tipo, SistemaFachada.ResultadoPrecio precio, String color) {
        String textoPrecio;
        if (precio.descuentoPorcentaje() > 0) {
            textoPrecio = String.format("%s\n💰 $%.2f (Antes: $%.2f | Descuento: %.0f%%)",
                    tipo, precio.tarifaFinal(), precio.tarifaBase(), precio.descuentoPorcentaje());
        } else {
            textoPrecio = String.format("%s\n💰 $%.2f", tipo, precio.tarifaFinal());
        }

        Button btn = new Button(textoPrecio);
        btn.setStyle(
                "-fx-font-size: 14px; -fx-padding: 15 30; -fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; -fx-background-radius: 8; -fx-font-weight: bold;"
        );
        btn.setPrefWidth(400);
        btn.setWrapText(true);
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

        Label lblBienvenida = new Label("Hola Conductor " + conductor.getNombre());
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