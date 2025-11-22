package Main;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Clase principal de la aplicación JavaFX.
 *
 * RESPONSABILIDADES:
 * - Iniciar la aplicación JavaFX
 * - Crear el controlador principal
 * - Cerrar recursos al finalizar
 *
 * Esta clase NO contiene:
 * - Lógica de negocio
 * - Creación de UI
 * - Gestión de estado
 *
 * Todo eso está delegado a AppController y sus colaboradores.
 */
public class MainApp extends Application {

    private AppController controller;

    @Override
    public void start(Stage primaryStage) {
        // Configuración inicial del Stage
        primaryStage.setTitle("Uber - Sistema de Viajes");
        primaryStage.setWidth(900);
        primaryStage.setHeight(700);

        // Crear el controlador principal (él gestiona todo)
        this.controller = new AppController(primaryStage);

        // Iniciar la aplicación
        controller.iniciar();

        // Mostrar el Stage
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        // Cerrar recursos cuando la aplicación se cierra
        if (controller != null) {
            controller.cerrarAplicacion();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}