package Viajes;

import Main.Cliente;
import Main.Conductor;
import Main.Ruta;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipoClase"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ViajeEstandar.class, name = "Estandar"),
        @JsonSubTypes.Type(value = ViajeXL.class, name = "XL"),
        @JsonSubTypes.Type(value = ViajeLujo.class, name = "Lujo")
})
public sealed abstract class Viaje implements Serializable
        permits ViajeEstandar, ViajeXL, ViajeLujo {

    protected Ruta ruta;
    protected Cliente cliente;
    protected Conductor conductor;
    protected TipoViaje tipo;

    // Constructor vacío para Jackson
    public Viaje() {
    }

    public Viaje(Ruta ruta, Cliente cliente, Conductor conductor, TipoViaje tipo) {
        this.ruta = ruta;
        this.cliente = cliente;
        this.conductor = conductor;
        this.tipo = tipo;
    }

    public double calcularCosto(double tarifaBasePorKm) {
        double kms = ruta.distanciaMetros() / 1000.0;
        double precioBase = kms * tarifaBasePorKm;
        return tipo.calcularPrecio(precioBase);
    }

    // GETTERS para Jackson (serialización)
    public Ruta getRuta() { return ruta; }
    public Cliente getCliente() { return cliente; }
    public Conductor getConductor() { return conductor; }
    public TipoViaje getTipo() { return tipo; }

    // SETTERS para Jackson (deserialización)
    public void setRuta(Ruta ruta) { this.ruta = ruta; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public void setConductor(Conductor conductor) { this.conductor = conductor; }
    public void setTipo(TipoViaje tipo) { this.tipo = tipo; }

    // Métodos estilo record (para código más limpio)
    public Ruta ruta() { return ruta; }
    public Cliente cliente() { return cliente; }
    public Conductor conductor() { return conductor; }
    public TipoViaje tipo() { return tipo; }
}