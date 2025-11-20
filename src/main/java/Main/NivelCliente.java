package Main;

public enum NivelCliente {
    ESTANDAR {
        @Override
        public double descuento() {
            return 1.0; // sin descuento
        }
    },
    PREMIUM {
        @Override
        public double descuento() {
            return 0.80; // 20% de descuento
        }
    };

    public abstract double descuento();
}