package Main;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        DistanceCalculator calculator = new DistanceCalculator();

        System.out.println("=================================================");
        System.out.println("  Calculadora de distancia en Rosario  ");
        System.out.println("=================================================");

        try {
            System.out.print("Ingresa la dirección de ORIGEN: ");
            String origen = scanner.nextLine();

            System.out.print("Ingresa la dirección de DESTINO: ");
            String destino = scanner.nextLine();

            System.out.println("\nCalculando ruta...");

            String resultado = calculator.calculateRoute(origen, destino);

            System.out.println("-------------------------------------------------");
            System.out.println(resultado);
            System.out.println("-------------------------------------------------");

        } catch (Exception e) {
            System.err.println("\n❌ ERROR CRÍTICO: " + e.getMessage());
            System.err.println("Por favor, verifica la clave de API y tu conexión a internet.");
        } finally {

            scanner.close();
            calculator.closeContext();
        }
    }

}