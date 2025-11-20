package Main;

import java.io.Serializable;

public record Ruta(
        long distanciaMetros,
        String distanciaTexto,
        String tiempoTexto,
        String linkMapa
) implements Serializable { }
