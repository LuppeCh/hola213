package Viajes;

public enum TipoViaje {

    Estandar {
        @Override
        public double multiplicador() {
            return 1.0; // 100% del precio base
        }
    },
    XL {
        @Override
        public double multiplicador() {
            return 1.5; // 150% del precio base
        }
    },
    Lujo {
        @Override
        public double multiplicador() {
            return 2.2; // 220% del precio base
        }
    };

    // Método abstracto implementado por cada constante
    public abstract double multiplicador();

    // Método auxiliar para calcular tarifa
    public double calcularPrecio(double precioBase) {
        return precioBase * multiplicador();
    }
}

