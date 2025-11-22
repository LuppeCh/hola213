package Viajes;

import Main.*;

public final class ViajeEstandar extends Viaje {

    // Constructor vacío para Jackson (deserialización)
    public ViajeEstandar() {
        super();
    }

    // Constructor normal para crear viajes
    public ViajeEstandar(Ruta ruta, Cliente cliente, Conductor conductor) {
        super(ruta, cliente, conductor, TipoViaje.Estandar);
    }
}