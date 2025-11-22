
package Viajes;

import Main.*;

public final class ViajeLujo extends Viaje {

    // Constructor vacío para Jackson (deserialización)
    public ViajeLujo() {
        super();
    }

    // Constructor normal para crear viajes
    public ViajeLujo(Ruta ruta, Cliente cliente, Conductor conductor) {
        super(ruta, cliente, conductor, TipoViaje.Lujo);
    }
}