
package Viajes;

import Main.*;

public final class ViajeXL extends Viaje {

    // Constructor vacío para Jackson (deserialización)
    public ViajeXL() {
        super();
    }

    // Constructor normal para crear viajes
    public ViajeXL(Ruta ruta, Cliente cliente, Conductor conductor) {
        super(ruta, cliente, conductor, TipoViaje.XL);
    }
}