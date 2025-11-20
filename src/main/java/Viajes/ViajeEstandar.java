package Viajes;

import Main.*;

public final class ViajeEstandar extends Viaje {

    public ViajeEstandar(Ruta ruta, Cliente cliente, Conductor conductor) {
        super(ruta, cliente, conductor, TipoViaje.Estandar);
    }

    // aquí podés agregar precio base, multiplicadores, etc.
}