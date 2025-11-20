package Viajes;

import Main.*;

public final class ViajeXL extends Viaje {

    public ViajeXL(Ruta ruta, Cliente cliente, Conductor conductor) {
        super(ruta, cliente, conductor, TipoViaje.XL);
    }
}
