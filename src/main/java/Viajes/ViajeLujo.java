package Viajes;

import Main.*;

public final class ViajeLujo extends Viaje {

    public ViajeLujo(Ruta ruta, Cliente cliente, Conductor conductor) {
        super(ruta, cliente, conductor, TipoViaje.Lujo);
    }
}